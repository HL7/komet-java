package org.hl7.komet.framework;


import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import org.eclipse.collections.api.list.ImmutableList;
import org.hl7.komet.framework.activity.ActivityStream;
import org.hl7.komet.framework.activity.ActivityStreamOption;
import org.hl7.komet.framework.activity.ActivityStreams;
import org.hl7.komet.framework.graphics.Icon;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.komet.preferences.KometPreferences;
import org.hl7.tinkar.common.alert.AlertObject;
import org.hl7.tinkar.common.alert.AlertStreams;
import org.hl7.tinkar.common.id.PublicIdStringKey;
import org.hl7.tinkar.coordinate.logic.calculator.LogicCalculator;
import org.hl7.tinkar.coordinate.navigation.calculator.NavigationCalculator;
import org.hl7.tinkar.coordinate.view.ViewCoordinateRecord;
import org.hl7.tinkar.coordinate.view.calculator.ViewCalculator;
import org.hl7.tinkar.coordinate.view.calculator.ViewCalculatorDelegate;
import org.hl7.tinkar.terms.EntityFacade;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicReference;

import static org.hl7.komet.framework.KometNode.PreferenceKey.ACTIVITY_STREAM_OPTION_KEY;

public abstract class ExplorationNodeAbstract implements KometNode, Flow.Subscriber<ImmutableList<EntityFacade>>, ViewCalculatorDelegate {

    protected final SimpleObjectProperty<PublicIdStringKey<ActivityStream>> activityStreamKeyProperty = new SimpleObjectProperty<>();
    protected final SimpleObjectProperty<PublicIdStringKey<ActivityStreamOption>> optionForActivityStreamKeyProperty = new SimpleObjectProperty<>();
    protected final SimpleStringProperty titleProperty = new SimpleStringProperty(getDefaultTitle());
    protected final SimpleStringProperty toolTipTextProperty = new SimpleStringProperty("");
    protected final SimpleObjectProperty menuIconProperty = new SimpleObjectProperty(getMenuIconGraphic());
    protected final ViewProperties viewProperties;
    protected final KometPreferences nodePreferences;
    protected HBox titleNode = new HBox(2);
    private AtomicReference<Flow.Subscription> flowSubscriptionReference = new AtomicReference<>();
    private Runnable nodeSelectionMethod = () -> {
    }; // default to an empty operation.

    {
        titleNode.alignmentProperty().setValue(Pos.CENTER);
        titleNode.getChildren().add(Icon.makeIcon(getStyleId()));
        activityStreamKeyProperty.addListener((observable, oldActivityStreamKey, newActivityStreamKey) -> {
            titleNode.getChildren().clear();
            titleNode.getChildren().add(Icon.makeIcon(getStyleId()));
            if (newActivityStreamKey != null) {
                if (showActivityStreamIcon()) {
                    if (!ActivityStreams.UNLINKED.equals(newActivityStreamKey)) {
                        ActivityStream activityStream = ActivityStreams.get(newActivityStreamKey);
                        titleNode.getChildren().add(activityStream.getStreamIcon());
                    }
                }
            }
        });
    }

    public ExplorationNodeAbstract(ViewProperties viewProperties, KometPreferences nodePreferences) {
        this.viewProperties = viewProperties;
        this.nodePreferences = nodePreferences;
        PublicIdStringKey<ActivityStream> activityStreamKey = this.nodePreferences.getObject(
                KometNode.PreferenceKey.ACTIVITY_STREAM_KEY, ActivityStreams.UNLINKED);
        this.activityStreamKeyProperty.setValue(activityStreamKey);

        PublicIdStringKey<ActivityStreamOption> activityStreamOptionKey = this.nodePreferences.getObject(
                ACTIVITY_STREAM_OPTION_KEY, ActivityStreamOption.PUBLISH.keyForOption());
        this.optionForActivityStreamKeyProperty.setValue(activityStreamOptionKey);

        updateActivityStream();

        this.optionForActivityStreamKeyProperty.addListener((observable, oldValue, newValue) -> {
            updateActivityStream();
        });
        this.activityStreamKeyProperty.addListener((observable, oldValue, newValue) -> {
            updateActivityStream();
        });

        // TODO the title label...
        //this.titleLabel = new EntityLabelWithDragAndDrop();
    }

    protected void updateActivityStream() {
        if (this.optionForActivityStreamKeyProperty.get().equals(ActivityStreamOption.SUBSCRIBE.keyForOption()) ||
                this.optionForActivityStreamKeyProperty.get().equals(ActivityStreamOption.SYNCHRONIZE.keyForOption())) {
            this.getActivityStream().subscribe(this);
        }

        if (this.optionForActivityStreamKeyProperty.get().equals(ActivityStreamOption.PUBLISH.keyForOption()) ||
                this.optionForActivityStreamKeyProperty.get().equals(ActivityStreamOption.SYNCHRONIZE.keyForOption())) {
            // Dispatch is handled dynamically, no need for static setup.
        }
    }

    public ExplorationNodeAbstract() {
        this.viewProperties = null;
        this.nodePreferences = null;
    }

    @Override
    public final ViewCalculator viewCalculator() {
        return viewProperties.calculator();
    }

    @Override
    public LogicCalculator logicCalculator() {
        return viewProperties.calculator();
    }

    @Override
    public NavigationCalculator navigationCalculator() {
        return viewProperties.calculator();
    }

    @Override
    public ViewCoordinateRecord viewCoordinateRecord() {
        return viewProperties.nodeView().getOriginalValue();
    }

    /**
     * Subclasses can override if it does not want activity stream icon shown in title node.
     *
     * @return
     */
    protected boolean showActivityStreamIcon() {
        return true;
    }

    public abstract String getDefaultTitle();

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        flowSubscriptionReference.getAndUpdate(existingSubscription -> {
            if (existingSubscription != null) {
                existingSubscription.cancel();
            }
            return subscription;
        });
        subscription.request(1);
    }

    @Override
    public void onNext(ImmutableList<EntityFacade> items) {
        handleActivity(items);
        flowSubscriptionReference.get().request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        AlertStreams.getRoot().dispatch(AlertObject.makeError(throwable));
    }

    @Override
    public void onComplete() {
        // nothing to do.
    }

    public abstract void handleActivity(ImmutableList<EntityFacade> entities);

    public void dispatchActivity(ImmutableList<EntityFacade> entities) {
        getActivityStream().dispatch(entities);
    }

    protected final Runnable getNodeSelectionMethod() {
        return nodeSelectionMethod;
    }

    @Override
    public final void setNodeSelectionMethod(Runnable nodeSelectionMethod) {
        this.nodeSelectionMethod = nodeSelectionMethod;
    }

    @Override
    public Node getMenuIconGraphic() {
        Label menuIcon = new Label("", new FontIcon());
        menuIcon.setId(getStyleId());
        return menuIcon;
    }

    public abstract String getStyleId();

    @Override
    public KometPreferences getNodePreferences() {
        return nodePreferences;
    }

    //~--- get methods ---------------------------------------------------------
    @Override
    public final StringPropertyBase getTitle() {
        return this.titleProperty;
    }

    @Override
    public Node getTitleNode() {
        return titleNode;
    }

    @Override
    public final ReadOnlyProperty<String> toolTipTextProperty() {
        return this.toolTipTextProperty;
    }

    @Override
    public Tooltip makeToolTip() {
        Tooltip tooltip = new Tooltip(toolTipTextProperty().getValue());
        tooltip.textProperty().bind(toolTipTextProperty());
        return tooltip;
    }

    @Override
    public final ViewProperties getViewProperties() {
        return this.viewProperties;
    }

    @Override
    public final ActivityStream getActivityStream() {
        return ActivityStreams.get(activityStreamKeyProperty.get());
    }

    @Override
    public final SimpleObjectProperty<PublicIdStringKey<ActivityStream>> activityStreamKeyProperty() {
        return activityStreamKeyProperty;
    }

    @Override
    public SimpleObjectProperty<PublicIdStringKey<ActivityStreamOption>> optionForActivityStreamKeyProperty() {
        return optionForActivityStreamKeyProperty;
    }

    @Override
    public final Scene getScene() {
        return getNode().getScene();
    }

    @Override
    public final SimpleObjectProperty<Node> getMenuIconProperty() {
        return menuIconProperty;
    }
}
