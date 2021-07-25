package org.hl7.komet.framework;


import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.eclipse.collections.api.list.ImmutableList;
import org.hl7.komet.framework.activity.ActivityStream;
import org.hl7.komet.framework.activity.ActivityStreamOption;
import org.hl7.komet.framework.activity.ActivityStreams;
import org.hl7.komet.framework.alerts.AlertObject;
import org.hl7.komet.framework.alerts.AlertStreams;
import org.hl7.komet.framework.graphics.Icon;
import org.hl7.komet.preferences.KometPreferences;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.tinkar.common.id.PublicIdStringKey;
import org.hl7.tinkar.terms.ConceptFacade;
import org.hl7.tinkar.terms.EntityFacade;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicReference;

import static org.hl7.komet.framework.KometNode.PreferenceKey.ACTIVITY_STREAM_OPTION_KEY;

public abstract class ExplorationNodeAbstract implements KometNode, Flow.Subscriber<ImmutableList<EntityFacade>> {

    protected final SimpleObjectProperty<PublicIdStringKey<ActivityStream>> activityStreamKeyProperty = new SimpleObjectProperty<>();
    protected final SimpleObjectProperty<PublicIdStringKey<ActivityStreamOption>> optionForActivityStreamKeyProperty = new SimpleObjectProperty<>();
    protected final SimpleStringProperty toolTipProperty = new SimpleStringProperty("uninitialized tool tip");
    protected final SimpleStringProperty titleProperty = new SimpleStringProperty(getDefaultTitle());
    protected final SimpleObjectProperty menuIconProperty = new SimpleObjectProperty(getMenuIconGraphic());
    protected final ViewProperties viewProperties;
    protected final KometPreferences nodePreferences;
    private AtomicReference<Flow.Subscription> flowSubscriptionReference = new AtomicReference<>();

    protected HBox titleNode = new HBox(2);
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

    /**
     * Subclasses can override if it does not want activity stream icon shown in title node.
     * @return
     */
    protected boolean showActivityStreamIcon() {
        return true;
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

    public ExplorationNodeAbstract() {
        this.viewProperties = null;
        this.nodePreferences = null;
    }

    public abstract String getStyleId();
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

    protected void updateActivityStream() {
        if (this.optionForActivityStreamKeyProperty.get().equals(ActivityStreamOption.SUBSCRIBE.keyForOption()) ||
            this.optionForActivityStreamKeyProperty.get().equals(ActivityStreamOption.SYNCHRONIZE.keyForOption()) ) {
            this.getActivityStream().subscribe(this);
        }

        if (this.optionForActivityStreamKeyProperty.get().equals(ActivityStreamOption.PUBLISH.keyForOption()) ||
                this.optionForActivityStreamKeyProperty.get().equals(ActivityStreamOption.SYNCHRONIZE.keyForOption()) ) {
            // Dispatch is handled dynamically, no need for static setup.
        }
    }
    @Override
    public Node getTitleNode() {
        return titleNode;
    }

    @Override
    public Node getMenuIconGraphic() {
        Label menuIcon = new Label("", new FontIcon());
        menuIcon.setId(getStyleId());
        return menuIcon;
    }

    @Override
    public final Scene getScene() {
        return getNode().getScene();
    }

    @Override
    public final SimpleObjectProperty<Node> getMenuIconProperty() {
        return menuIconProperty;
    }

    @Override
    public final ViewProperties getViewProperties() {
        return this.viewProperties;
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
    public final ActivityStream getActivityStream() {
        return ActivityStreams.get(activityStreamKeyProperty.get());
    }

    public void dispatchActivity(ImmutableList<EntityFacade> entities) {
        getActivityStream().dispatch(entities);
    }

    public abstract void handleActivity(ImmutableList<EntityFacade> entities);

    private Runnable nodeSelectionMethod = () -> {}; // default to an empty operation.


    @Override
    public final ReadOnlyProperty<String> getToolTip() {
        return this.toolTipProperty;
    }

    @Override
    public final void setNodeSelectionMethod(Runnable nodeSelectionMethod) {
        this.nodeSelectionMethod = nodeSelectionMethod;
    }

    protected final Runnable getNodeSelectionMethod() {
        return nodeSelectionMethod;
    }

    //~--- get methods ---------------------------------------------------------
    @Override
    public final StringPropertyBase getTitle() {
        return this.titleProperty;
    }

    @Override
    public KometPreferences getNodePreferences() {
        return nodePreferences;
    }
}
