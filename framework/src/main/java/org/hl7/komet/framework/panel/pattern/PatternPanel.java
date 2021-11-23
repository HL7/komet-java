package org.hl7.komet.framework.panel.pattern;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableSet;
import org.hl7.komet.framework.PseudoClasses;
import org.hl7.komet.framework.observable.ObservablePattern;
import org.hl7.komet.framework.observable.ObservablePatternSnapshot;
import org.hl7.komet.framework.observable.ObservablePatternVersion;
import org.hl7.komet.framework.panel.ComponentIsFinalPanel;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.tinkar.entity.PatternVersionRecord;
import org.hl7.tinkar.terms.EntityFacade;

public class PatternPanel extends ComponentIsFinalPanel<
        ObservablePatternSnapshot,
        ObservablePattern,
        ObservablePatternVersion,
        PatternVersionRecord> {

    public PatternPanel(ObservablePatternSnapshot patternSnapshot, ViewProperties viewProperties,
                        SimpleObjectProperty<EntityFacade> topEnclosingComponentProperty,
                        ObservableSet<Integer> referencedNids) {
        super(patternSnapshot, viewProperties, topEnclosingComponentProperty, referencedNids);
        this.collapsiblePane.setText("Pattern");
        this.getComponentDetailPane().pseudoClassStateChanged(PseudoClasses.PATTERN_PSEUDO_CLASS, true);
        this.getComponentPanelBox().pseudoClassStateChanged(PseudoClasses.PATTERN_PSEUDO_CLASS, true);
    }
}
