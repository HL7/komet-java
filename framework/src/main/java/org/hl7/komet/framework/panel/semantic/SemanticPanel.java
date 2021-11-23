package org.hl7.komet.framework.panel.semantic;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableSet;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import org.hl7.komet.framework.PseudoClasses;
import org.hl7.komet.framework.observable.ObservableSemantic;
import org.hl7.komet.framework.observable.ObservableSemanticSnapshot;
import org.hl7.komet.framework.observable.ObservableSemanticVersion;
import org.hl7.komet.framework.panel.ComponentIsFinalPanel;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.tinkar.coordinate.stamp.calculator.Latest;
import org.hl7.tinkar.entity.PatternEntityVersion;
import org.hl7.tinkar.entity.SemanticVersionRecord;
import org.hl7.tinkar.terms.EntityFacade;
import org.hl7.tinkar.terms.TinkarTerm;

public class SemanticPanel extends ComponentIsFinalPanel<
        ObservableSemanticSnapshot,
        ObservableSemantic,
        ObservableSemanticVersion,
        SemanticVersionRecord> {

    public SemanticPanel(ObservableSemanticSnapshot semanticSnapshot, ViewProperties viewProperties, SimpleObjectProperty<EntityFacade> topEnclosingComponentProperty, ObservableSet<Integer> referencedNids) {
        super(semanticSnapshot, viewProperties, topEnclosingComponentProperty, referencedNids);
        Latest<PatternEntityVersion> latestPatternVersion = viewProperties.calculator().latestPatternEntityVersion(semanticSnapshot.patternNid());

        latestPatternVersion.ifPresent(patternEntityVersion -> {
            StringBuilder sb = new StringBuilder("[");
            sb.append(viewProperties.calculator().getPreferredDescriptionTextWithFallbackOrNid(patternEntityVersion.semanticMeaningNid()));
            if (patternEntityVersion.semanticMeaningNid() != patternEntityVersion.semanticPurposeNid()) {
                sb.append("] of component for [");
                sb.append(viewProperties.calculator().getPreferredDescriptionTextWithFallbackOrNid(patternEntityVersion.semanticPurposeNid()));
            }
            sb.append("] in [");
            sb.append(viewProperties.calculator().getPreferredDescriptionTextWithFallbackOrNid(patternEntityVersion.nid()));
            sb.append("]");
            collapsiblePane.setText(sb.toString());
            HBox referencedComponentInfo = new HBox(3);
            referencedComponentInfo.setAlignment(Pos.CENTER_LEFT);

            ContextMenu contextMenu = new ContextMenu();
            collapsiblePane.setContextMenu(contextMenu);
            MenuItem patternMenuItem = new MenuItem("Focus on pattern: " +
                    viewProperties.calculator().getPreferredDescriptionTextWithFallbackOrNid(patternEntityVersion.nid()));
            patternMenuItem.setOnAction(event -> {
                topEnclosingComponentProperty.setValue(patternEntityVersion.chronology());
            });
            contextMenu.getItems().add(patternMenuItem);

            MenuItem topEnclosingComponentMenuItem = new MenuItem("Focus on top enclosing component: " +
                    viewProperties.calculator().getPreferredDescriptionTextWithFallbackOrNid(semanticSnapshot.observableEntity().topEnclosingComponentNid()));
            topEnclosingComponentMenuItem.setOnAction(event -> {
                topEnclosingComponentProperty.setValue(semanticSnapshot.observableEntity().topEnclosingComponent());
            });
            contextMenu.getItems().add(topEnclosingComponentMenuItem);

        });


        if (semanticSnapshot.patternNid() == TinkarTerm.EL_PLUS_PLUS_STATED_AXIOMS_PATTERN.nid() ||
                semanticSnapshot.patternNid() == TinkarTerm.EL_PLUS_PLUS_INFERRED_AXIOMS_PATTERN.nid()) {
            this.getComponentPanelBox().pseudoClassStateChanged(PseudoClasses.LOGICAL_DEFINITION_PSEUDO_CLASS, true);
            this.getComponentDetailPane().pseudoClassStateChanged(PseudoClasses.LOGICAL_DEFINITION_PSEUDO_CLASS, true);
        } else if (semanticSnapshot.patternNid() == TinkarTerm.DESCRIPTION_PATTERN.nid()) {
            this.getComponentPanelBox().pseudoClassStateChanged(PseudoClasses.DESCRIPTION_PSEUDO_CLASS, true);
            this.getComponentDetailPane().pseudoClassStateChanged(PseudoClasses.DESCRIPTION_PSEUDO_CLASS, true);
        } else {
            this.getComponentPanelBox().pseudoClassStateChanged(PseudoClasses.SEMANTIC_PSEUDO_CLASS, true);
            this.getComponentDetailPane().pseudoClassStateChanged(PseudoClasses.SEMANTIC_PSEUDO_CLASS, true);
        }
    }
}
