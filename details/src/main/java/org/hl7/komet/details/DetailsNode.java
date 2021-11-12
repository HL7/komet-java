package org.hl7.komet.details;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import org.eclipse.collections.api.list.ImmutableList;
import org.hl7.komet.framework.ExplorationNodeAbstract;
import org.hl7.komet.framework.KometNode;
import org.hl7.komet.framework.TopPanelFactory;
import org.hl7.komet.framework.controls.EntityLabelWithDragAndDrop;
import org.hl7.komet.framework.panel.ComponentPanel;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.komet.preferences.KometPreferences;
import org.hl7.tinkar.common.flow.FlowSubscriber;
import org.hl7.tinkar.entity.Entity;
import org.hl7.tinkar.terms.EntityFacade;

public class DetailsNode extends ExplorationNodeAbstract {
    protected static final String STYLE_ID = "concept-details-node";
    protected static final String TITLE = "Generic Details";
    final SimpleObjectProperty<EntityFacade> entityFocusProperty = new SimpleObjectProperty<>();
    private final BorderPane detailsPane = new BorderPane();
    private final ComponentPanel componentPanel;
    private final FlowSubscriber<Integer> invalidationSubscriber;

    {
        entityFocusProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                titleProperty.set(viewProperties.calculator().getPreferredDescriptionTextWithFallbackOrNid(newValue));
                toolTipTextProperty.set(viewProperties.calculator().getFullyQualifiedDescriptionTextWithFallbackOrNid(newValue));
            } else {
                titleProperty.set(EntityLabelWithDragAndDrop.EMPTY_TEXT);
                toolTipTextProperty.set(EntityLabelWithDragAndDrop.EMPTY_TEXT);
            }
        });
    }

    public DetailsNode(ViewProperties viewProperties, KometPreferences nodePreferences) {
        super(viewProperties, nodePreferences);
        this.componentPanel = new ComponentPanel(entityFocusProperty, viewProperties);
        this.invalidationSubscriber = new FlowSubscriber<>(nid -> {
            if (entityFocusProperty.get() != null && entityFocusProperty.get().nid() == nid) {
                // component has changed, need to update.
                Platform.runLater(() -> entityFocusProperty.set(null));
                Platform.runLater(() -> entityFocusProperty.set(Entity.provider().getEntityFast(nid)));
            }
        });
        this.detailsPane.sceneProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                invalidationSubscriber.cancel();
            }
        });
        Entity.provider().subscribe(this.invalidationSubscriber);

        this.viewProperties.nodeView().addListener((observable, oldValue, newValue) -> {
            setupTopPanel(viewProperties);
            this.componentPanel.changed(entityFocusProperty, null, entityFocusProperty.getValue());
        });

        Platform.runLater(() -> {
            setupTopPanel(viewProperties);
        });
    }

    private void setupTopPanel(ViewProperties viewProperties) {
        this.detailsPane.setCenter(this.componentPanel.getComponentDetailPane());
        Node topPanel = TopPanelFactory.make(viewProperties, entityFocusProperty,
                activityStreamKeyProperty, optionForActivityStreamKeyProperty, true);
        this.detailsPane.setTop(topPanel);
    }

    protected static void addDefaultNodePreferences(KometPreferences nodePreferences) {
        nodePreferences.put(PreferenceKey.TEST, "test");
        nodePreferences.get(KometNode.PreferenceKey.PARENT_ALERT_STREAM_KEY);
    }

    @Override
    public String getDefaultTitle() {
        return TITLE;
    }

    @Override
    public void handleActivity(ImmutableList<EntityFacade> entities) {
        if (entities.isEmpty()) {
            entityFocusProperty.set(null);
        } else {
            entityFocusProperty.set(entities.get(0));
        }
    }

    @Override
    public String getStyleId() {
        return STYLE_ID;
    }

    @Override
    public Node getNode() {
        return this.detailsPane;
    }

    @Override
    public void close() {

    }

    @Override
    public boolean canClose() {
        return false;
    }

    @Override
    public void savePreferences() {

    }

    @Override
    public void revertPreferences() {

    }

    enum PreferenceKey {
        TEST
    }
}