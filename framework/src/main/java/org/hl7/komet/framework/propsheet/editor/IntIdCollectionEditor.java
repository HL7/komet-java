package org.hl7.komet.framework.propsheet.editor;

import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ToolBar;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import org.controlsfx.property.editor.PropertyEditor;
import org.hl7.komet.framework.PseudoClasses;
import org.hl7.komet.framework.dnd.DragImageMaker;
import org.hl7.komet.framework.dnd.KometClipboard;
import org.hl7.komet.framework.graphics.Icon;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.tinkar.common.id.IntIdCollection;
import org.hl7.tinkar.coordinate.stamp.calculator.Latest;
import org.hl7.tinkar.entity.Entity;
import org.hl7.tinkar.entity.EntityVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class IntIdCollectionEditor<T extends IntIdCollection> implements PropertyEditor<T> {
    private static final Logger LOG = LoggerFactory.getLogger(IntIdCollectionEditor.class);
    protected final BorderPane editorPane = new BorderPane();
    protected final Button newItem = new Button("", Icon.PLUS.makeIcon());
    protected final ToolBar editorToolbar = new ToolBar(newItem);
    protected final ListView<Integer> listView = new ListView();
    protected final ViewProperties viewProperties;
    SimpleObjectProperty<T> entitiesListProperty;

    public IntIdCollectionEditor(ViewProperties viewProperties, SimpleObjectProperty<T> entitiesListProperty) {
        this.editorPane.setCenter(listView);
        this.entitiesListProperty = entitiesListProperty;
        this.editorPane.setTop(editorToolbar);
        this.newItem.setOnAction(this::newItem);
        this.viewProperties = viewProperties;
        listView.setCellFactory(param -> new IntIdCollectionEditor.EntityCell());
        updateList(entitiesListProperty.getValue());
        this.entitiesListProperty.addListener((observable, oldValue, newValue) -> {
            updateList(newValue);
        });
    }

    abstract void newItem(ActionEvent actionEvent);

    abstract void updateList(T newValue);

    @Override
    public Node getEditor() {
        return editorPane;
    }

    @Override
    public T getValue() {
        return entitiesListProperty.getValue();
    }

    @Override
    public void setValue(T value) {
        entitiesListProperty.setValue(value);
    }

    class EntityCell extends ListCell<Integer> {

        int entityNid = Integer.MIN_VALUE;
        Latest<EntityVersion> latestEntity;
        String entityText;

        public EntityCell() {
            setOnDragDetected(this::handleDragDetected);
            setOnDragDone(this::handleDragDone);
        }

        private void handleDragDetected(MouseEvent event) {
            LOG.debug("Drag detected: " + event);

            DragImageMaker dragImageMaker = new DragImageMaker(this);
            Dragboard db = startDragAndDrop(TransferMode.COPY);

            db.setDragView(dragImageMaker.getDragImage());

            KometClipboard content = new KometClipboard((Entity) Entity.getFast(entityNid));
            db.setContent(content);
            event.consume();
        }

        private void handleDragDone(DragEvent event) {
            LOG.debug("Dragging done: " + event);
        }

        @Override
        protected void updateItem(Integer item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                entityNid = Integer.MIN_VALUE;
                setText("");
                entityText = null;
                setGraphic(null);
                this.pseudoClassStateChanged(PseudoClasses.INACTIVE_PSEUDO_CLASS, false);
            } else {
                if (item != entityNid) {
                    entityNid = item;
                    latestEntity = viewProperties.calculator().latest(entityNid);
                    entityText = viewProperties.calculator().getDescriptionTextOrNid(entityNid);
                    setText(entityText);
                    this.pseudoClassStateChanged(PseudoClasses.INACTIVE_PSEUDO_CLASS, latestEntity.get().inactive());
                }
            }
        }
    }
}
