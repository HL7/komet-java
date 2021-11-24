package org.hl7.komet.framework.observable;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.hl7.tinkar.common.alert.AlertObject;
import org.hl7.tinkar.common.alert.AlertStreams;
import org.hl7.tinkar.component.FieldDataType;
import org.hl7.tinkar.entity.Entity;
import org.hl7.tinkar.entity.Field;
import org.hl7.tinkar.entity.FieldRecord;
import org.hl7.tinkar.entity.StampRecord;

public class ObservableField<T> implements Field<T> {

    SimpleObjectProperty<FieldRecord<T>> fieldProperty = new SimpleObjectProperty<>();
    SimpleObjectProperty<T> valueProperty = new SimpleObjectProperty<>();

    public ObservableField(FieldRecord<T> fieldRecord) {
        fieldProperty.set(fieldRecord);
        valueProperty.set(fieldRecord.value());
        valueProperty.addListener((observable, oldValue, newValue) -> {
            alertIllegalChange(newValue);
            fieldProperty.set(field().withValue(newValue));
        });
    }

    private void alertIllegalChange(Object newValue) {
        StampRecord stamp = Entity.getStamp(fieldProperty.get().semanticVersionStampNid());
        if (stamp.lastVersion().committed()) {
            AlertStreams.getRoot().dispatch(AlertObject.makeWarning("Changing committed version",
                    "Changing value to " + newValue + " for " + fieldProperty.get().toString()));
        }
    }

    public FieldRecord<T> field() {
        return fieldProperty.get();
    }

    @Override
    public T value() {
        return field().value();
    }

    @Override
    public FieldDataType fieldDataType() {
        return field().fieldDataType();
    }

    @Override
    public int meaningNid() {
        return field().meaningNid();
    }

    @Override
    public int purposeNid() {
        return field().purposeNid();
    }

    @Override
    public int dataTypeNid() {
        return field().dataTypeNid();
    }

    public ObjectProperty<T> valueProperty() {
        return valueProperty;
    }

}
