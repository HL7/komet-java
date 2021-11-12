package org.hl7.komet.framework.panel.concept;


import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableSet;
import org.hl7.komet.framework.PseudoClasses;
import org.hl7.komet.framework.panel.ComponentIsFinalPanel;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.tinkar.entity.ConceptEntity;
import org.hl7.tinkar.entity.ConceptEntityVersion;
import org.hl7.tinkar.terms.EntityFacade;

public class ConceptPanel<T extends ConceptEntity<ConceptEntityVersion>> extends ComponentIsFinalPanel<T, ConceptEntityVersion> {

    public ConceptPanel(T conceptEntity,
                        ViewProperties viewProperties,
                        SimpleObjectProperty<EntityFacade> topEnclosingComponentProperty,
                        ObservableSet<Integer> referencedNids) {
        super(conceptEntity, viewProperties, topEnclosingComponentProperty, referencedNids);
        this.collapsiblePane.setText("Concept panel");
        this.getComponentPanelBox().pseudoClassStateChanged(PseudoClasses.CONCEPT_PSEUDO_CLASS, true);
        this.getComponentDetailPane().pseudoClassStateChanged(PseudoClasses.CONCEPT_PSEUDO_CLASS, true);
    }
}
