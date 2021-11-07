package org.hl7.komet.framework.rulebase.actions;

import org.hl7.komet.framework.rulebase.GeneratedActionSuggested;
import org.hl7.tinkar.coordinate.edit.EditCoordinate;
import org.hl7.tinkar.coordinate.view.calculator.ViewCalculator;

public abstract non-sealed class AbstractActionSuggested extends AbstractActionGenerated implements GeneratedActionSuggested {

    public AbstractActionSuggested(String text, ViewCalculator viewCalculator, EditCoordinate editCoordinate) {
        super(text, viewCalculator, editCoordinate);
    }
}
