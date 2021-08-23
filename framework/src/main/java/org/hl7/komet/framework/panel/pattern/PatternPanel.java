package org.hl7.komet.framework.panel.pattern;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Label;
import org.hl7.komet.framework.PseudoClasses;
import org.hl7.komet.framework.panel.ComponentIsFinalPanel;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.tinkar.entity.PatternEntity;
import org.hl7.tinkar.entity.PatternEntityVersion;
import org.hl7.tinkar.terms.EntityFacade;

public class PatternPanel<T extends PatternEntity> extends ComponentIsFinalPanel<T, PatternEntityVersion> {

    public PatternPanel(T patternEntity, ViewProperties viewProperties, SimpleObjectProperty<EntityFacade> topEnclosingComponentProperty) {
        super(patternEntity, viewProperties, topEnclosingComponentProperty);
        Label label = new Label("Pattern Panel: " + viewProperties.calculator().getDescriptionTextOrNid(patternEntity));
        label.setWrapText(true);
        this.getComponentPanelBox().getChildren().add(label);
        this.getComponentDetailPane().pseudoClassStateChanged(PseudoClasses.PATTERN_PSEUDO_CLASS, true);
        this.getComponentPanelBox().pseudoClassStateChanged(PseudoClasses.PATTERN_PSEUDO_CLASS, true);
    }
}
