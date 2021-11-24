package org.hl7.komet.framework.propsheet.editor;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import org.eclipse.collections.api.list.primitive.MutableIntList;
import org.eclipse.collections.impl.factory.primitive.IntLists;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.tinkar.common.id.IntIdList;
import org.hl7.tinkar.common.service.Executor;

public class IntIdListEditor extends IntIdCollectionEditor<IntIdList> {

    public IntIdListEditor(ViewProperties viewProperties, SimpleObjectProperty<IntIdList> intIdSetProperty) {
        super(viewProperties, intIdSetProperty);

    }

    void newItem(ActionEvent actionEvent) {
        throw new UnsupportedOperationException();
    }

    void updateList(IntIdList newValue) {
        listView.getItems().clear();
        Executor.threadPool().execute(() -> {
            MutableIntList nidList = IntLists.mutable.ofAll(newValue.intStream());
            Platform.runLater(() -> listView.getItems().addAll(nidList.toList().primitiveStream().mapToObj(nid -> nid).toList()));
        });
    }
}
