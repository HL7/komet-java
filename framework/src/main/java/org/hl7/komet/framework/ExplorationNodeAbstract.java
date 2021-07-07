package org.hl7.komet.framework;


import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import org.hl7.komet.framework.activity.ActivityStream;
import org.hl7.komet.framework.activity.ActivityStreams;
import org.hl7.komet.framework.graphics.Icon;
import org.hl7.komet.preferences.KometPreferences;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.tinkar.common.id.PublicIdStringKey;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Optional;

public abstract class ExplorationNodeAbstract implements KometNode {

    protected final SimpleObjectProperty<PublicIdStringKey<ActivityStream>> activityStreamKeyProperty = new SimpleObjectProperty<>();
    protected final SimpleStringProperty toolTipProperty = new SimpleStringProperty("uninitialized tool tip");
    protected final SimpleStringProperty titleProperty = new SimpleStringProperty(getDefaultTitle());
    protected final SimpleObjectProperty menuIconProperty = new SimpleObjectProperty(getMenuIconGraphic());
    protected final ViewProperties viewProperties;
    protected final KometPreferences nodePreferences;

    protected Label titleNode = Icon.makeIcon(getStyleId());


    public ExplorationNodeAbstract(ViewProperties viewProperties, KometPreferences nodePreferences) {
        this.viewProperties = viewProperties;
        this.nodePreferences = nodePreferences;
        PublicIdStringKey<ActivityStream> activityStreamKey = this.nodePreferences.getObject(
                KometNode.PreferenceKey.ACTIVITY_STREAM_KEY, ActivityStreams.UNLINKED);
        this.activityStreamKeyProperty.setValue(activityStreamKey);
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
