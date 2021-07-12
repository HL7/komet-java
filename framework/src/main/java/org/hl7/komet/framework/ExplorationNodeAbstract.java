package org.hl7.komet.framework;


import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.hl7.komet.framework.activity.ActivityStream;
import org.hl7.komet.framework.activity.ActivityStreamOption;
import org.hl7.komet.framework.activity.ActivityStreams;
import org.hl7.komet.framework.graphics.Icon;
import org.hl7.komet.preferences.KometPreferences;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.tinkar.common.id.PublicIdStringKey;
import org.kordamp.ikonli.javafx.FontIcon;

import static org.hl7.komet.framework.KometNode.PreferenceKey.ACTIVITY_STREAM_OPTION_KEY;

public abstract class ExplorationNodeAbstract implements KometNode {

    protected final SimpleObjectProperty<PublicIdStringKey<ActivityStream>> activityStreamKeyProperty = new SimpleObjectProperty<>();
    protected final SimpleObjectProperty<PublicIdStringKey<ActivityStreamOption>> optionForActivityStreamKeyProperty = new SimpleObjectProperty<>();
    protected final SimpleStringProperty toolTipProperty = new SimpleStringProperty("uninitialized tool tip");
    protected final SimpleStringProperty titleProperty = new SimpleStringProperty(getDefaultTitle());
    protected final SimpleObjectProperty menuIconProperty = new SimpleObjectProperty(getMenuIconGraphic());
    protected final ViewProperties viewProperties;
    protected final KometPreferences nodePreferences;

    protected HBox titleNode = new HBox(2);
    {
        titleNode.alignmentProperty().setValue(Pos.CENTER);
        titleNode.getChildren().add(Icon.makeIcon(getStyleId()));
        activityStreamKeyProperty.addListener((observable, oldActivityStreamKey, newActivityStreamKey) -> {
            titleNode.getChildren().clear();
            titleNode.getChildren().add(Icon.makeIcon(getStyleId()));
            if (newActivityStreamKey != null) {
                if (!ActivityStreams.UNLINKED.equals(newActivityStreamKey)) {
                    ActivityStream activityStream = ActivityStreams.get(newActivityStreamKey);
                    titleNode.getChildren().add(activityStream.getStreamIcon());
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

    public SimpleObjectProperty<PublicIdStringKey<ActivityStreamOption>> optionForActivityStreamKeyPropertyProperty() {
        return optionForActivityStreamKeyProperty;
    }

    @Override
    public final ActivityStream getActivityStream() {
        return ActivityStreams.get(activityStreamKeyProperty.get());
    }


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
