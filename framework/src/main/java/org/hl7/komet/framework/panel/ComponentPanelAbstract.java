package org.hl7.komet.framework.panel;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.hl7.komet.framework.StyleClasses;
import org.hl7.komet.framework.panel.concept.ConceptPanel;
import org.hl7.komet.framework.panel.pattern.PatternPanel;
import org.hl7.komet.framework.panel.semantic.SemanticPanel;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.tinkar.common.service.Executor;
import org.hl7.tinkar.common.service.PrimitiveData;
import org.hl7.tinkar.coordinate.view.calculator.ViewCalculator;
import org.hl7.tinkar.entity.ConceptEntity;
import org.hl7.tinkar.entity.Entity;
import org.hl7.tinkar.entity.PatternEntity;
import org.hl7.tinkar.entity.SemanticEntity;
import org.hl7.tinkar.terms.EntityFacade;

import java.util.Optional;

public abstract class ComponentPanelAbstract<C extends EntityFacade> {
    protected final BorderPane componentDetailPane = new BorderPane();
    protected final VBox componentPanelBox = new VBox(8);
    protected final ViewProperties viewProperties;

    {
        this.componentDetailPane.setCenter(this.componentPanelBox);
        this.componentPanelBox.getStyleClass().add(StyleClasses.COMPONENT_PANEL.toString());
    }

    protected ComponentPanelAbstract(ViewProperties viewProperties) {
        this.viewProperties = viewProperties;
    }

    public abstract Optional<C> getComponent();

    public ViewCalculator calculator() {
        return viewProperties.calculator();
    }

    public VBox getComponentPanelBox() {
        return componentPanelBox;
    }

    protected void addSemanticReferences(C entity) {
        if (entity != null) {
            Executor.threadPool().execute(() -> {
                PrimitiveData.get().forEachSemanticNidForComponent(entity.nid(), semanticNid -> {
                    SemanticEntity semanticEntity = Entity.getFast(semanticNid);
                    ComponentPanelAbstract<?> semanticPanel = makeComponentPanel(semanticEntity);
                    Platform.runLater(() -> ComponentPanelAbstract.this.componentPanelBox.getChildren().add(semanticPanel.getComponentDetailPane()));
                });
            });
        }
    }

    public ComponentPanelAbstract<?> makeComponentPanel(EntityFacade facade) {
        Entity<?> entity = Entity.getFast(facade);
        if (entity instanceof ConceptEntity conceptEntity) {
            return new ConceptPanel(conceptEntity, viewProperties);
        } else if (entity instanceof SemanticEntity semanticEntity) {
            return new SemanticPanel(semanticEntity, viewProperties, true);
        } else if (entity instanceof PatternEntity patternEntity) {
            return new PatternPanel(patternEntity, viewProperties);
        } else {
            throw new IllegalStateException("Can't handle: " + entity);
        }
    }

    public Node getComponentDetailPane() {
        return componentDetailPane;
    }

}
