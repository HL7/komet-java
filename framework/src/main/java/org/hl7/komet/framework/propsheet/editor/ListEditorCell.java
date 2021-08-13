package org.hl7.komet.framework.propsheet.editor;

import javafx.event.Event;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.controlsfx.property.editor.PropertyEditor;
import org.hl7.komet.framework.graphics.Icon;
import org.hl7.komet.framework.view.ViewProperties;

/**
 * @param <T>
 * @author kec
 */
public class ListEditorCell<T extends Object> extends ListCell<T> {
    private final ListView listView;
    private final ViewProperties viewProperties;
    Button cancelButton = new Button("", Icon.CANCEL.makeIcon());
    Button duplicateButton = new Button("", Icon.DUPLICATE.makeIcon());
    VBox buttonBox = new VBox(cancelButton, duplicateButton);
    GridPane gridPane = new GridPane();
    private PropertyEditor<T> propertyEditor;

    public ListEditorCell(ListView listView,
                          ViewProperties viewProperties) {
        this.listView = listView;
        this.viewProperties = viewProperties;
        this.cancelButton.setOnAction(this::cancel);
        this.duplicateButton.setOnAction(this::duplicate);

    }

    private void cancel(Event event) {
        listView.getItems().remove(this.getIndex());
    }

    private void duplicate(Event event) {
        throw new UnsupportedOperationException();
//        T newObject = this.newObjectSupplier.get();
//        // TODO: copy all fields.
//        listView.getItems().add(this.getIndex() + 1, newObject);
    }

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
            setText("");
            if (this.propertyEditor != null) {
                propertyEditor.setValue(null);
                this.propertyEditor = null;
                this.gridPane.getChildren().clear();
            }
        } else {
//            if (this.newEditorSupplier != null) {
//                if (this.propertyEditor == null) {
//                    this.propertyEditor = newEditorSupplier.apply(viewProperties);
//                    Node editorNode = propertyEditor.getEditor();
//                    GridPane.setConstraints(this.buttonBox,
//                            0,
//                            0,
//                            1,
//                            1,
//                            HPos.LEFT,
//                            VPos.TOP,
//                            Priority.NEVER,
//                            Priority.NEVER);
//                    GridPane.setConstraints(editorNode,
//                            1,
//                            0,
//                            1,
//                            2,
//                            HPos.LEFT,
//                            VPos.TOP,
//                            Priority.ALWAYS,
//                            Priority.NEVER);
//                    this.gridPane.getChildren().setAll(this.buttonBox, this.propertyEditor.getEditor());
//                    this.setGraphic(this.gridPane);
//                }
//                if (item != propertyEditor.getValue()) {
//                    propertyEditor.setValue(item);
//                }
//            }
            setText(item.toString());
        }
    }
}
