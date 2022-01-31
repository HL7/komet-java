package org.hl7.komet.framework.propsheet.editor;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.MultipleSelectionModel;
import org.eclipse.collections.api.list.primitive.MutableIntList;
import org.eclipse.collections.impl.factory.primitive.IntLists;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.tinkar.common.id.IntIdList;
import org.hl7.tinkar.common.id.IntIds;
import org.hl7.tinkar.common.service.Executor;

public class IntIdListEditor extends IntIdCollectionEditor<IntIdList> {

    public IntIdListEditor(ViewProperties viewProperties, SimpleObjectProperty<IntIdList> intIdListProperty) {
        super(viewProperties, intIdListProperty);

    }

    void updateListView(IntIdList newValue) {
        Executor.threadPool().execute(() -> {
            Platform.runLater(() -> {
                listView.getItems().clear();
                if (newValue != null) {
                    listView.getItems().addAll(newValue.intStream().mapToObj(nid -> nid).toList());
                }
            });
        });
    }

    @Override
    void deleteSelectedItems(MultipleSelectionModel<Integer> selectionModel) {
        MutableIntList currentList = IntLists.mutable.of(getValue().toArray());
        for (Integer index : listView.getSelectionModel().getSelectedIndices().sorted((x, y) -> Integer.compare(y, x))) {
            currentList.removeAtIndex(index);
        }
        setValue(IntIds.list.of(currentList.toArray()));
    }
}
