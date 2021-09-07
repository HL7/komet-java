package org.hl7.komet.framework.rulebase.actions;

import javafx.event.ActionEvent;
import org.hl7.tinkar.entity.EntityVersion;

public class ActivateComponentAction extends AbstractAction {

    final EntityVersion entityVersion;

    public ActivateComponentAction(EntityVersion entityVersion) {
        super("Activate");
        this.entityVersion = entityVersion;
    }

    public final void doAction(ActionEvent t) {
        throw new UnsupportedOperationException();
    }

}
