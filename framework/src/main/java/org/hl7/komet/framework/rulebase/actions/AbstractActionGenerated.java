package org.hl7.komet.framework.rulebase.actions;

import javafx.event.ActionEvent;
import org.controlsfx.control.action.Action;
import org.hl7.komet.framework.Dialogs;
import org.hl7.komet.framework.rulebase.GeneratedAction;
import org.hl7.tinkar.coordinate.edit.EditCoordinate;
import org.hl7.tinkar.coordinate.edit.EditCoordinateRecord;
import org.hl7.tinkar.coordinate.view.calculator.ViewCalculator;

public sealed abstract class AbstractActionGenerated
        extends Action
        implements GeneratedAction
        permits AbstractActionImmediate, AbstractActionSuggested {

    protected final ViewCalculator viewCalculator;
    private final EditCoordinate editCoordinate;

    public AbstractActionGenerated(String text, ViewCalculator viewCalculator, EditCoordinate editCoordinate) {
        super(text);
        this.viewCalculator = viewCalculator;
        this.editCoordinate = editCoordinate;
        setEventHandler(this::doAction);
    }

    public final void doAction(ActionEvent actionEvent) {
        try {
            doAction(actionEvent, editCoordinate.toEditCoordinateRecord());
        } catch (Throwable ex) {
            Dialogs.showErrorDialog("Error executing " + getText(), ex.getMessage(), ex);
        }
    }

    public abstract void doAction(ActionEvent t, EditCoordinateRecord editCoordinate);

    public ViewCalculator viewCalculator() {
        return viewCalculator;
    }

}
