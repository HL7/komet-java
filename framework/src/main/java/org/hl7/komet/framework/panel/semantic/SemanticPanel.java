package org.hl7.komet.framework.panel.semantic;

import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.hl7.komet.framework.PseudoClasses;
import org.hl7.komet.framework.controls.EntityLabelWithDragAndDrop;
import org.hl7.komet.framework.panel.ComponentIsFinalPanel;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.tinkar.coordinate.stamp.calculator.Latest;
import org.hl7.tinkar.entity.PatternEntityVersion;
import org.hl7.tinkar.entity.SemanticEntity;
import org.hl7.tinkar.entity.SemanticEntityVersion;
import org.hl7.tinkar.terms.EntityFacade;
import org.hl7.tinkar.terms.EntityProxy;
import org.hl7.tinkar.terms.TinkarTerm;

public class SemanticPanel<T extends SemanticEntity> extends ComponentIsFinalPanel<T, SemanticEntityVersion> {

    public SemanticPanel(T semanticEntity, ViewProperties viewProperties) {
        this(semanticEntity, viewProperties, false);
    }

    public SemanticPanel(T semanticEntity, ViewProperties viewProperties, boolean showReferencedComponent) {
        super(semanticEntity, viewProperties);
        Latest<PatternEntityVersion> latestPatternVersion = viewProperties.calculator().latestPatternEntityVersion(semanticEntity.patternNid());
        if (latestPatternVersion.isPresent()) {
            HBox referencedComponentInfo = new HBox(3);
            referencedComponentInfo.setAlignment(Pos.CENTER_LEFT);
            this.getComponentPanelBox().getChildren().add(referencedComponentInfo);

            PatternEntityVersion patternEntityVersion = latestPatternVersion.get();
            SimpleObjectProperty<EntityFacade> patternProperty = new SimpleObjectProperty<>();
            patternProperty.set(EntityProxy.Pattern.make(semanticEntity.patternNid()));
            EntityLabelWithDragAndDrop patternLabel = EntityLabelWithDragAndDrop.make(viewProperties, patternProperty);
            // TODO make label drag only...
            referencedComponentInfo.getChildren().add(new Label("Pattern:"));
            referencedComponentInfo.getChildren().add(patternLabel);

            SimpleObjectProperty<EntityFacade> semanticMeaningProperty = new SimpleObjectProperty<>();
            semanticMeaningProperty.set(EntityProxy.Pattern.make(patternEntityVersion.semanticMeaningNid()));
            EntityLabelWithDragAndDrop semanticMeaningLabel = EntityLabelWithDragAndDrop.make(viewProperties, semanticMeaningProperty);
            // TODO make label drag only...
            referencedComponentInfo.getChildren().add(new Label("Meaning:"));
            referencedComponentInfo.getChildren().add(semanticMeaningLabel);

            SimpleObjectProperty<EntityFacade> semanticPurposeProperty = new SimpleObjectProperty<>();
            semanticPurposeProperty.set(EntityProxy.Pattern.make(patternEntityVersion.semanticPurposeNid()));
            EntityLabelWithDragAndDrop semanticPurposeLabel = EntityLabelWithDragAndDrop.make(viewProperties, semanticPurposeProperty);
            // TODO make label drag only...
            referencedComponentInfo.getChildren().add(new Label("Purpose:"));
            referencedComponentInfo.getChildren().add(semanticPurposeLabel);

            if (showReferencedComponent) {
                SimpleObjectProperty<EntityFacade> referencedComponentProperty = new SimpleObjectProperty<>();
                referencedComponentProperty.set(EntityProxy.Pattern.make(semanticEntity.referencedComponentNid()));
                EntityLabelWithDragAndDrop referencedComponentLabel = EntityLabelWithDragAndDrop.make(viewProperties, referencedComponentProperty);
                // TODO make label drag only...
                this.getComponentPanelBox().getChildren().add(referencedComponentLabel);
            }
        }


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
