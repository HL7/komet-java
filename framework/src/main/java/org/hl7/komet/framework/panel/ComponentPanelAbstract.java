package org.hl7.komet.framework.panel;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.hl7.komet.framework.StyleClasses;
import org.hl7.komet.framework.observable.*;
import org.hl7.komet.framework.panel.concept.ConceptPanel;
import org.hl7.komet.framework.panel.pattern.PatternPanel;
import org.hl7.komet.framework.panel.semantic.SemanticPanel;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.tinkar.common.service.Executor;
import org.hl7.tinkar.common.service.PrimitiveData;
import org.hl7.tinkar.coordinate.view.calculator.ViewCalculator;
import org.hl7.tinkar.entity.Entity;
import org.hl7.tinkar.entity.SemanticEntity;
import org.hl7.tinkar.terms.EntityFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Optional;

public abstract class ComponentPanelAbstract {
    private static final Logger LOG = LoggerFactory.getLogger(ComponentPanelAbstract.class);
    protected final ObservableSet<Integer> referencedNids;
    protected final BorderPane componentDetailPane = new BorderPane();
    protected final VBox componentPanelBox = new VBox(8);
    protected final ViewProperties viewProperties;


    {
        this.componentDetailPane.setCenter(this.componentPanelBox);
        this.componentPanelBox.getStyleClass().add(StyleClasses.COMPONENT_PANEL.toString());
    }

    protected ComponentPanelAbstract(ViewProperties viewProperties) {
        this(viewProperties, FXCollections.observableSet(new HashSet<>()));
    }

    protected ComponentPanelAbstract(ViewProperties viewProperties, ObservableSet<Integer> referencedNids) {
        this.viewProperties = viewProperties;
        this.referencedNids = referencedNids;
    }

    public final ObservableSet<Integer> getReferencedNids() {
        return referencedNids;
    }

    public abstract <C extends EntityFacade> Optional<C> getComponent();

    public ViewCalculator calculator() {
        return viewProperties.calculator();
    }

    public VBox getComponentPanelBox() {
        return componentPanelBox;
    }

    protected void addSemanticReferences(ObservableEntitySnapshot entity, SimpleObjectProperty<EntityFacade> topEnclosingComponentProperty) {
        if (entity != null) {
            Executor.threadPool().execute(() -> {
                PrimitiveData.get().forEachSemanticNidForComponent(entity.nid(), semanticNid -> {
                    Platform.runLater(() -> referencedNids.add(semanticNid));
                    SemanticEntity semanticEntity = Entity.getFast(semanticNid);
                    if (!semanticEntity.canceled()) {
                        Platform.runLater(() -> {
                            ObservableSemanticSnapshot semanticSnapshot = (ObservableSemanticSnapshot) ObservableEntity.get(semanticEntity).getSnapshot(this.viewProperties.calculator());
                            ComponentPanelAbstract semanticPanel = makeComponentPanel(semanticSnapshot, topEnclosingComponentProperty);
                            ComponentPanelAbstract.this.componentPanelBox.getChildren().add(semanticPanel.getComponentDetailPane());
                        });
                    }
                });
            });
        }
    }

    public ComponentPanelAbstract makeComponentPanel(ObservableEntitySnapshot entitySnapshot, SimpleObjectProperty<EntityFacade> topEnclosingComponentProperty) {
        if (entitySnapshot instanceof ObservableConceptSnapshot conceptSnapshot) {
            return new ConceptPanel(conceptSnapshot, viewProperties, topEnclosingComponentProperty, referencedNids);
        } else if (entitySnapshot instanceof ObservableSemanticSnapshot semanticEntity) {
            return new SemanticPanel(semanticEntity, viewProperties, topEnclosingComponentProperty, referencedNids);
        } else if (entitySnapshot instanceof ObservablePatternSnapshot patternEntity) {
            return new PatternPanel(patternEntity, viewProperties, topEnclosingComponentProperty, referencedNids);
        } else {
            throw new IllegalStateException("Can't handle: " + entitySnapshot);
        }
    }

    public Node getComponentDetailPane() {
        return componentDetailPane;
    }

}
