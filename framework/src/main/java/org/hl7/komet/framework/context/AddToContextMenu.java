package org.hl7.komet.framework.context;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.tinkar.terms.EntityFacade;

/**
 * See also: MenuSupplierForFocusedEntity TODO:
 */
public interface AddToContextMenu {
    void addToContextMenu(Control controlWithContext, ContextMenu contextMenu, ViewProperties viewProperties,
                          ObservableValue<EntityFacade> conceptFocusProperty,
                          SimpleIntegerProperty selectionIndexProperty,
                          Runnable unlink);
}
