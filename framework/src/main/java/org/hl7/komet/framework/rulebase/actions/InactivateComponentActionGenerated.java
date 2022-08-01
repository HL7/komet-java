package org.hl7.komet.framework.rulebase.actions;

import javafx.event.ActionEvent;
import org.hl7.tinkar.coordinate.edit.EditCoordinate;
import org.hl7.tinkar.coordinate.edit.EditCoordinateRecord;
import org.hl7.tinkar.coordinate.view.calculator.ViewCalculator;
import org.hl7.tinkar.entity.EntityVersion;

public class InactivateComponentActionGenerated extends AbstractActionSuggested {

    final EntityVersion entityVersion;

    public InactivateComponentActionGenerated(EntityVersion entityVersion, ViewCalculator viewCalculator, EditCoordinate editCoordinate) {
        super("Inactivate", viewCalculator, editCoordinate);
        this.entityVersion = entityVersion;
    }

    public final void doAction(ActionEvent actionEvent, EditCoordinateRecord editCoordinate) {
        throw new UnsupportedOperationException();
    }

}
