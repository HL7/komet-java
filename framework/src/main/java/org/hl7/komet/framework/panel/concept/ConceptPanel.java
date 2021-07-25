package org.hl7.komet.framework.panel.concept;


import javafx.application.Platform;
import javafx.scene.control.Label;
import org.hl7.komet.framework.PseudoClasses;
import org.hl7.komet.framework.panel.ComponentPanelAbstract;
import org.hl7.tinkar.common.service.Executor;
import org.hl7.tinkar.common.service.PrimitiveData;
import org.hl7.tinkar.entity.ConceptEntity;
import org.hl7.tinkar.entity.Entity;
import org.hl7.tinkar.entity.SemanticEntity;

public class ConceptPanel<T extends ConceptEntity> extends ComponentPanelAbstract<T> {

    private final ConceptEntity conceptEntity;
    public ConceptPanel(ConceptEntity conceptEntity) {
        this.conceptEntity = conceptEntity;
        this.getComponentPanelBox().getChildren().add(new Label(conceptEntity.toString()));
        this.getComponentPanelBox().pseudoClassStateChanged(PseudoClasses.CONCEPT_PSEUDO_CLASS, true);
        addSemanticReferences(conceptEntity);
    }
}
