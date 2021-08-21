package org.hl7.komet.framework.panel.concept;


import javafx.scene.control.Label;
import org.hl7.komet.framework.PseudoClasses;
import org.hl7.komet.framework.StyleClasses;
import org.hl7.komet.framework.panel.ComponentIsFinalPanel;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.tinkar.entity.ConceptEntity;
import org.hl7.tinkar.entity.ConceptEntityVersion;

public class ConceptPanel<T extends ConceptEntity> extends ComponentIsFinalPanel<T, ConceptEntityVersion> {

    public ConceptPanel(T conceptEntity, ViewProperties viewProperties) {
        super(conceptEntity, viewProperties);
        Label label = new Label("Concept Panel: " + viewProperties.calculator().getDescriptionTextOrNid(conceptEntity));
        label.setWrapText(true);
        label.getStyleClass().add(StyleClasses.HEADER_TEXT.toString());
        this.getComponentPanelBox().getChildren().add(label);
        this.getComponentPanelBox().pseudoClassStateChanged(PseudoClasses.CONCEPT_PSEUDO_CLASS, true);
    }
}
