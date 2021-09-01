package org.hl7.komet.framework.panel;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import org.hl7.komet.framework.panel.concept.ConceptVersionPanel;
import org.hl7.komet.framework.panel.pattern.PatternVersionPanel;
import org.hl7.komet.framework.panel.semantic.SemanticVersionPanel;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.tinkar.common.service.Executor;
import org.hl7.tinkar.component.graph.DiTree;
import org.hl7.tinkar.entity.*;
import org.hl7.tinkar.entity.graph.VersionVertex;
import org.hl7.tinkar.terms.EntityFacade;

import java.util.List;
import java.util.Optional;

import static org.hl7.komet.framework.StyleClasses.COMPONENT_COLLAPSIBLE_PANEL;

/**
 * @param <C>
 */
public class ComponentIsFinalPanel<C extends Entity<V>, V extends EntityVersion> extends ComponentPanelAbstract {
    protected final TitledPane collapsiblePane = new TitledPane("Component", componentDetailPane);
    private final C component;

    {
        collapsiblePane.getStyleClass().add(COMPONENT_COLLAPSIBLE_PANEL.toString());
    }

    public ComponentIsFinalPanel(C component, ViewProperties viewProperties, SimpleObjectProperty<EntityFacade> topEnclosingComponentProperty) {
        super(viewProperties);
        if (component == null) {
            throw new NullPointerException();
        }
        this.component = component;
        this.collapsiblePane.setContentDisplay(ContentDisplay.LEFT);
        // TODO finish good identicon graphic.
        //this.collapsiblePane.setGraphic(Identicon.generateIdenticon(component.publicId(), 24, 24));
        List<DiTree<VersionVertex<V>>> versionGraph = viewProperties.calculator().getVersionGraphList(component);
        Executor.threadPool().execute(() -> {
            for (DiTree<VersionVertex<V>> diTree : versionGraph) {
                dfsAddVersion(diTree.root(), diTree);
            }
            addSemanticReferences(component, topEnclosingComponentProperty);
        });
    }

    private void dfsAddVersion(VersionVertex<V> versionVertex, DiTree<VersionVertex<V>> versionGraph) {
        //versionGraph.predecessor(versionVertex);
        ComponentVersionIsFinalPanel<V> versionPanel = makeVersionPanel(versionVertex.version());
        BorderPane.setAlignment(versionPanel.versionDetailsPane, Pos.TOP_LEFT);
        Platform.runLater(() -> ComponentIsFinalPanel.this.componentPanelBox.getChildren().add(versionPanel.getVersionDetailsPane()));
        for (VersionVertex<V> childVertex : versionGraph.successors(versionVertex)) {
            dfsAddVersion(childVertex, versionGraph);
        }
    }

    private ComponentVersionIsFinalPanel<V> makeVersionPanel(V version) {
        if (version instanceof SemanticEntityVersion semanticEntityVersion) {
            return (ComponentVersionIsFinalPanel<V>) new SemanticVersionPanel(semanticEntityVersion, viewProperties);
        } else if (version instanceof ConceptEntityVersion conceptEntityVersion) {
            return (ComponentVersionIsFinalPanel<V>) new ConceptVersionPanel(conceptEntityVersion, viewProperties);
        } else if (version instanceof PatternEntityVersion patternEntityVersion) {
            return (ComponentVersionIsFinalPanel<V>) new PatternVersionPanel(patternEntityVersion, viewProperties);
        }
        throw new UnsupportedOperationException("Can't handle version type: " + version.toString());
    }

    @Override
    public final Optional<C> getComponent() {
        return Optional.of(component);
    }


    public Node getComponentDetailPane() {
        return collapsiblePane;
    }

}
