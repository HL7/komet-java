package org.hl7.komet.framework.panel;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.hl7.komet.framework.StyleClasses;
import org.hl7.komet.framework.panel.concept.ConceptPanel;
import org.hl7.komet.framework.panel.pattern.PatternPanel;
import org.hl7.komet.framework.panel.semantic.SemanticPanel;
import org.hl7.tinkar.common.service.Executor;
import org.hl7.tinkar.common.service.PrimitiveData;
import org.hl7.tinkar.entity.*;

public abstract class ComponentPanelAbstract<T extends Entity<? extends EntityVersion>> {
    private final BorderPane detailPane = new BorderPane();
    private final VBox componentPanelBox = new VBox(8);
    private final ScrollPane scrollPane = new ScrollPane(componentPanelBox);
    {
        this.scrollPane.setFitToWidth(true);
        this.scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        this.detailPane.setCenter(this.scrollPane);
        this.componentPanelBox.getStyleClass().add(StyleClasses.COMPONENT_PANEL.toString());
    }

    public Node getDetailPane() {
        return detailPane;
    }

    public VBox getComponentPanelBox() {
        return componentPanelBox;
    }


    protected void addSemanticReferences(Entity entity) {
        if (entity != null) {
            Executor.threadPool().execute(() -> {
                PrimitiveData.get().forEachSemanticNidForComponent(entity.nid(), semanticNid -> {
                    SemanticEntity semanticEntity = Entity.getFast(semanticNid);
                    ComponentPanelAbstract<?> semanticPanel = makeComponentPanel(semanticEntity);
                    Platform.runLater(() -> ComponentPanelAbstract.this.componentPanelBox.getChildren().add(semanticPanel.getDetailPane()));
                });
            });
        }
    }

    public ComponentPanelAbstract<?> makeComponentPanel(Entity<? extends EntityVersion> entity) {
        if (entity instanceof ConceptEntity conceptEntity) {
            return new ConceptPanel(conceptEntity);
        } else if (entity instanceof SemanticEntity semanticEntity) {
            return new SemanticPanel(semanticEntity);
        } else if (entity instanceof PatternEntity patternEntity) {
            return new PatternPanel(patternEntity);
        } else {
            throw new IllegalStateException("Can't handle: " + entity);
        }
    }

}
