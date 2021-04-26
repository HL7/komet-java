package org.hl7.komet.builder;

import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Node;
import javafx.scene.control.Label;
import org.hl7.komet.framework.ActivityFeed;
import org.hl7.komet.framework.ExplorationNode;
import org.hl7.komet.framework.ViewProperties;
import org.kordamp.ikonli.javafx.FontIcon;

public class ConceptBuilderNode implements ExplorationNode {
    protected static final String STYLE_ID = "concept-builder-node";
    protected static final String TITLE = "Concept Builder";

    SimpleStringProperty titleProperty = new SimpleStringProperty(TITLE);
    Label titleNode = new Label("", new FontIcon());
    {
        titleNode.setId(STYLE_ID);
        titleProperty.addListener((observable, oldValue, newValue) -> {
            titleNode.setText(newValue);
        });
    }

    @Override
    public ReadOnlyProperty<String> getTitle() {
        return titleProperty;
    }

    @Override
    public Node getNode() {
        return new Label(titleProperty.getValue());
    }

    @Override
    public Node getTitleNode() {
        return titleNode;
    }

    @Override
    public ReadOnlyProperty<String> getToolTip() {
        return null;
    }

    @Override
    public ViewProperties getViewProperties() {
        return null;
    }

    @Override
    public ActivityFeed getActivityFeed() {
        return null;
    }

    @Override
    public SimpleObjectProperty<ActivityFeed> activityFeedProperty() {
        return null;
    }

    @Override
    public void close() {

    }

    @Override
    public boolean canClose() {
        return false;
    }

    @Override
    public void setNodeSelectionMethod(Runnable nodeSelectionMethod) {

    }

    @Override
    public void savePreferences() {

    }

    @Override
    public void revertPreferences() {

    }
}