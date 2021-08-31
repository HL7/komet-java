package org.hl7.komet.framework.context;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.tinkar.common.service.PrimitiveData;
import org.hl7.tinkar.entity.Entity;
import org.hl7.tinkar.terms.EntityFacade;

public class AddToContextMenuSimple implements AddToContextMenu {
    private static String recursiveEntityToString(EntityFacade entityFacade) {
        return recursiveEntityToString(entityFacade.nid());
    }

    private static String recursiveEntityToString(int nid) {
        StringBuilder sb = new StringBuilder();
        recursiveEntityToString(nid, sb);
        return sb.toString();
    }

    private static void recursiveEntityToString(int nid, StringBuilder sb) {
        Entity entity = Entity.getFast(nid);
        sb.append(entity.toString());
        sb.append("\n\n");
        for (int semanticNid : PrimitiveData.get().semanticNidsForComponent(nid)) {
            recursiveEntityToString(semanticNid, sb);
        }
    }

    @Override
    public void addToContextMenu(ContextMenu contextMenu, ViewProperties viewProperties,
                                 ObservableValue<EntityFacade> entityFocusProperty,
                                 SimpleIntegerProperty selectionIndexProperty, Runnable unlink) {

        MenuItem copyEntityToString = new MenuItem("Copy entity toString()");
        copyEntityToString.setOnAction(event -> {
            EntityFacade entityFacade = entityFocusProperty.getValue();
            if (entityFacade != null) {
                Entity entity = Entity.getFast(entityFacade.nid());
                final Clipboard clipboard = Clipboard.getSystemClipboard();
                final ClipboardContent content = new ClipboardContent();
                content.putString(entity.toString());
                clipboard.setContent(content);
            }
        });
        contextMenu.getItems().add(copyEntityToString);

        MenuItem copyEntityToStringRecursive = new MenuItem("Copy entity toString() recursive");
        copyEntityToStringRecursive.setOnAction(event -> {
            EntityFacade entityFacade = entityFocusProperty.getValue();
            if (entityFacade != null) {
                final Clipboard clipboard = Clipboard.getSystemClipboard();
                final ClipboardContent content = new ClipboardContent();
                content.putString(recursiveEntityToString(entityFacade));
                clipboard.setContent(content);
            }
        });
        contextMenu.getItems().add(copyEntityToStringRecursive);

        MenuItem copyPublicId = new MenuItem("Copy text and public id");
        copyPublicId.setOnAction(event -> {
            EntityFacade entityFacade = entityFocusProperty.getValue();
            if (entityFacade != null) {
                String text = PrimitiveData.textFast(entityFacade.nid());
                final Clipboard clipboard = Clipboard.getSystemClipboard();
                final ClipboardContent content = new ClipboardContent();
                content.putString(text + ": " + entityFacade.publicId().asUuidList().toString());
                clipboard.setContent(content);
            }
        });
        contextMenu.getItems().add(copyPublicId);
    }
}
