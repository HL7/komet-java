package org.hl7.komet.framework.panel.semantic;

import javafx.scene.control.Label;
import org.hl7.komet.framework.PseudoClasses;
import org.hl7.komet.framework.panel.ComponentIsFinalPanel;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.tinkar.entity.SemanticEntity;
import org.hl7.tinkar.entity.SemanticEntityVersion;
import org.hl7.tinkar.terms.TinkarTerm;

public class SemanticPanel<T extends SemanticEntity> extends ComponentIsFinalPanel<T, SemanticEntityVersion> {

    public SemanticPanel(T semanticEntity, ViewProperties viewProperties) {
        super(semanticEntity, viewProperties);

        Label label = new Label("Semantic Panel: " +
                viewProperties.calculator().getDescriptionTextOrNid(semanticEntity.patternNid()));
        label.setWrapText(true);
        this.getComponentPanelBox().getChildren().add(label);
        if (semanticEntity.patternNid() == TinkarTerm.EL_PLUS_PLUS_STATED_AXIOMS_PATTERN.nid() ||
                semanticEntity.patternNid() == TinkarTerm.EL_PLUS_PLUS_INFERRED_AXIOMS_PATTERN.nid()) {
            this.getComponentPanelBox().pseudoClassStateChanged(PseudoClasses.LOGICAL_DEFINITION_PSEUDO_CLASS, true);
        } else if (semanticEntity.patternNid() == TinkarTerm.DESCRIPTION_PATTERN.nid()) {
            this.getComponentPanelBox().pseudoClassStateChanged(PseudoClasses.DESCRIPTION_PSEUDO_CLASS, true);
        } else {
            this.getComponentPanelBox().pseudoClassStateChanged(PseudoClasses.SEMANTIC_PSEUDO_CLASS, true);
        }
    }
}
