package org.hl7.komet.framework;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.ContextMenu;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.tinkar.terms.EntityFacade;

public interface AddToContextMenu {
    void addToContextMenu(ContextMenu contextMenu, ViewProperties viewProperties,
                          SimpleObjectProperty<EntityFacade> conceptFocusProperty,
                          SimpleIntegerProperty selectionIndexProperty,
                          Runnable unlink);
}
