package org.hl7.komet.framework.propsheet.editor;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import org.eclipse.collections.api.list.primitive.MutableIntList;
import org.eclipse.collections.impl.factory.primitive.IntLists;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.tinkar.common.alert.AlertObject;
import org.hl7.tinkar.common.alert.AlertStreams;
import org.hl7.tinkar.common.id.IntIdSet;
import org.hl7.tinkar.common.service.Executor;
import org.hl7.tinkar.coordinate.view.calculator.ViewCalculator;

public class IntIdSetEditor extends IntIdCollectionEditor<IntIdSet> {

    public IntIdSetEditor(ViewProperties viewProperties, SimpleObjectProperty<IntIdSet> intIdSetProperty) {
        super(viewProperties, intIdSetProperty);

    }

    void newItem(ActionEvent actionEvent) {
        AlertStreams.getRoot().dispatch(AlertObject.makeWarning("Not yet implemented", "Adding items not yet supported"));
    }

    void updateList(IntIdSet newValue) {
        listView.getItems().clear();
        Executor.threadPool().execute(() -> {
            MutableIntList nidList = IntLists.mutable.ofAll(newValue.intStream());
            ViewCalculator calculator = viewProperties.calculator();
            nidList.sortThis((nid1, nid2) -> calculator.getDescriptionTextOrNid(nid1)
                    .compareTo(calculator.getDescriptionTextOrNid(nid2)));
            Platform.runLater(() -> listView.getItems().addAll(nidList.toList().primitiveStream().mapToObj(nid -> nid).toList()));
        });
    }
}


