package org.hl7.komet.framework.context;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.tinkar.terms.EntityFacade;

public class AddToContextMenuSimple implements AddToContextMenu {
    @Override
    public void addToContextMenu(ContextMenu contextMenu, ViewProperties viewProperties, ObservableValue<EntityFacade> conceptFocusProperty, SimpleIntegerProperty selectionIndexProperty, Runnable unlink) {
        contextMenu.getItems().add(new MenuItem("No op menu item"));
    }
}
