package org.hl7.komet.framework.rulebase.actions;

import javafx.event.ActionEvent;
import org.controlsfx.control.action.Action;
import org.hl7.komet.framework.rulebase.SuggestedAction;

public abstract class AbstractAction extends Action implements SuggestedAction {

    public AbstractAction(String text) {
        super(text);
        setEventHandler(this::doAction);
    }

    public abstract void doAction(ActionEvent t);

}
