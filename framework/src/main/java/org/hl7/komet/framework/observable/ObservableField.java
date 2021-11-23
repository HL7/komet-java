package org.hl7.komet.framework.observable;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.hl7.tinkar.component.FieldDataType;
import org.hl7.tinkar.entity.Entity;
import org.hl7.tinkar.entity.Field;
import org.hl7.tinkar.entity.FieldRecord;
import org.hl7.tinkar.terms.ConceptFacade;

public class ObservableField<T> implements Field<T> {

    SimpleObjectProperty<FieldRecord<T>> fieldProperty = new SimpleObjectProperty<>();
    SimpleObjectProperty<T> valueProperty = new SimpleObjectProperty<>();
    SimpleObjectProperty<ConceptFacade> dataTypeProperty = new SimpleObjectProperty<>();
    SimpleObjectProperty<ConceptFacade> purposeProperty = new SimpleObjectProperty<>();
    SimpleObjectProperty<ConceptFacade> meaningProperty = new SimpleObjectProperty<>();

    public ObservableField(FieldRecord<T> fieldRecord) {
        fieldProperty.set(fieldRecord);
        valueProperty.set(fieldRecord.value());
        dataTypeProperty.set(Entity.provider().getEntityFast(fieldRecord.dataTypeNid()));
        purposeProperty.set(Entity.provider().getEntityFast(fieldRecord.purposeNid()));
        meaningProperty.set(Entity.provider().getEntityFast(fieldRecord.meaningNid()));
        valueProperty.addListener((observable, oldValue, newValue) -> {
            fieldProperty.set(field().withValue(newValue));
        });
        dataTypeProperty.addListener((observable, oldValue, newValue) -> {
            fieldProperty.set(field().withFieldDefinition(field().fieldDefinition().withDataTypeNid(newValue.nid())));
        });
        purposeProperty.addListener((observable, oldValue, newValue) -> {
            fieldProperty.set(field().withFieldDefinition(field().fieldDefinition().withPurposeNid(newValue.nid())));
        });
        meaningProperty.addListener((observable, oldValue, newValue) -> {
            fieldProperty.set(field().withFieldDefinition(field().fieldDefinition().withMeaningNid(newValue.nid())));
        });

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

    public ObjectProperty<ConceptFacade> purposeProperty() {
        return purposeProperty;
    }

    public ObjectProperty<ConceptFacade> meaningProperty() {
        return meaningProperty;
    }

    public ObjectProperty<ConceptFacade> dataTypeProperty() {
        return dataTypeProperty;
    }
}
