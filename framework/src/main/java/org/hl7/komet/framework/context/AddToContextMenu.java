package org.hl7.komet.framework.context;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ContextMenu;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.tinkar.terms.EntityFacade;

public interface AddToContextMenu {
    void addToContextMenu(ContextMenu contextMenu, ViewProperties viewProperties,
                          ObservableValue<EntityFacade> conceptFocusProperty,
                          SimpleIntegerProperty selectionIndexProperty,
                          Runnable unlink);
}
