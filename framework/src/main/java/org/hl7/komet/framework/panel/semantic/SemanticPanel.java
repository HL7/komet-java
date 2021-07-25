package org.hl7.komet.framework.panel.semantic;

import javafx.scene.control.Label;
import org.hl7.komet.framework.PseudoClasses;
import org.hl7.komet.framework.panel.ComponentPanelAbstract;
import org.hl7.tinkar.entity.SemanticEntity;
import org.hl7.tinkar.terms.TinkarTerm;

public class SemanticPanel<T extends SemanticEntity> extends ComponentPanelAbstract<T> {
    private final SemanticEntity semanticEntity;
    public SemanticPanel(SemanticEntity semanticEntity) {
        this.semanticEntity = semanticEntity;
        this.getComponentPanelBox().getChildren().add(new Label(semanticEntity.toString()));
        if (semanticEntity.patternNid() == TinkarTerm.EL_PLUS_PLUS_STATED_AXIOMS_PATTERN.nid() ||
                semanticEntity.patternNid() == TinkarTerm.EL_PLUS_PLUS_INFERRED_AXIOMS_PATTERN.nid()) {
            this.getComponentPanelBox().pseudoClassStateChanged(PseudoClasses.LOGICAL_DEFINITION_PSEUDO_CLASS, true);
        } else if (semanticEntity.patternNid() == TinkarTerm.DESCRIPTION_PATTERN.nid()) {
            this.getComponentPanelBox().pseudoClassStateChanged(PseudoClasses.DESCRIPTION_PSEUDO_CLASS, true);
        } else {
            this.getComponentPanelBox().pseudoClassStateChanged(PseudoClasses.SEMANTIC_PSEUDO_CLASS, true);
        }
        addSemanticReferences(semanticEntity);
    }
}
