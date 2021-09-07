package org.hl7.komet.framework.rulebase.actions;

import javafx.event.ActionEvent;
import org.hl7.tinkar.entity.EntityVersion;

public class InactivateComponentAction extends AbstractAction {

    final EntityVersion entityVersion;

    public InactivateComponentAction(EntityVersion entityVersion) {
        super("Inactivate");
        this.entityVersion = entityVersion;
    }

    public final void doAction(ActionEvent t) {
        throw new UnsupportedOperationException();
    }

}
