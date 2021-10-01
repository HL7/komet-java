package org.hl7.komet.framework.panel.pattern;

import javafx.beans.property.SimpleObjectProperty;
import org.hl7.komet.framework.PseudoClasses;
import org.hl7.komet.framework.panel.ComponentIsFinalPanel;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.tinkar.entity.PatternEntity;
import org.hl7.tinkar.entity.PatternEntityVersion;
import org.hl7.tinkar.terms.EntityFacade;

public class PatternPanel<T extends PatternEntity<PatternEntityVersion>> extends ComponentIsFinalPanel<T, PatternEntityVersion> {

    public PatternPanel(T patternEntity, ViewProperties viewProperties, SimpleObjectProperty<EntityFacade> topEnclosingComponentProperty) {
        super(patternEntity, viewProperties, topEnclosingComponentProperty);
        this.collapsiblePane.setText("Pattern");
        this.getComponentDetailPane().pseudoClassStateChanged(PseudoClasses.PATTERN_PSEUDO_CLASS, true);
        this.getComponentPanelBox().pseudoClassStateChanged(PseudoClasses.PATTERN_PSEUDO_CLASS, true);
    }
}
