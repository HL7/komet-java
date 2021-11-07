package org.hl7.komet.framework.rulebase.actions;

import javafx.event.ActionEvent;
import org.hl7.tinkar.coordinate.edit.EditCoordinate;
import org.hl7.tinkar.coordinate.edit.EditCoordinateImmutable;
import org.hl7.tinkar.coordinate.view.calculator.ViewCalculator;
import org.hl7.tinkar.entity.EntityVersion;

public class ActivateComponentActionGenerated extends AbstractActionSuggested {

    final EntityVersion entityVersion;

    public ActivateComponentActionGenerated(EntityVersion entityVersion, ViewCalculator viewCalculator, EditCoordinate editCoordinate) {
        super("Activate", viewCalculator, editCoordinate);
        this.entityVersion = entityVersion;
    }

    public final void doAction(ActionEvent actionEvent, EditCoordinateImmutable editCoordinate) {
        throw new UnsupportedOperationException();
    }

}
