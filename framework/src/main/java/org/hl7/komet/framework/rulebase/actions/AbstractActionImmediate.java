package org.hl7.komet.framework.rulebase.actions;

import org.hl7.komet.framework.rulebase.GeneratedActionImmediate;
import org.hl7.tinkar.coordinate.edit.EditCoordinate;
import org.hl7.tinkar.coordinate.view.calculator.ViewCalculator;

public abstract non-sealed class AbstractActionImmediate extends AbstractActionGenerated implements GeneratedActionImmediate {

    public AbstractActionImmediate(String text, ViewCalculator viewCalculator, EditCoordinate editCoordinate) {
        super(text, viewCalculator, editCoordinate);
    }
}
