package org.hl7.komet.framework.observable;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.hl7.tinkar.component.FieldDataType;
import org.hl7.tinkar.entity.Entity;
import org.hl7.tinkar.entity.Field;
import org.hl7.tinkar.entity.FieldRecord;
import org.hl7.tinkar.entity.SemanticEntityVersion;
import org.hl7.tinkar.terms.ConceptFacade;

import java.util.Optional;

public class ObservableField<T> implements Field<T> {

    SimpleObjectProperty<FieldRecord<T>> fieldProperty = new SimpleObjectProperty<>();
    SimpleObjectProperty<T> valueProperty = new SimpleObjectProperty<>();
    SimpleStringProperty narrativeProperty = new SimpleStringProperty();
    SimpleObjectProperty<ConceptFacade> dataTypeProperty = new SimpleObjectProperty<>();
    SimpleObjectProperty<ConceptFacade> purposeProperty = new SimpleObjectProperty<>();
    SimpleObjectProperty<ConceptFacade> meaningProperty = new SimpleObjectProperty<>();


    public ObservableField(FieldRecord<T> fieldRecord) {
        fieldProperty.set(fieldRecord);
        if (fieldRecord.narrativeOptional().isPresent()) {
            narrativeProperty.set(narrativeOptional().get());
        }
        valueProperty.set(fieldRecord.value());
        dataTypeProperty.set(Entity.provider().getEntityFast(fieldRecord.dataTypeNid()));
        purposeProperty.set(Entity.provider().getEntityFast(fieldRecord.purposeNid()));
        meaningProperty.set(Entity.provider().getEntityFast(fieldRecord.meaningNid()));
        valueProperty.addListener((observable, oldValue, newValue) -> {
            fieldProperty.set(field().withValue(newValue));
        });
        narrativeProperty.addListener((observable, oldValue, newValue) -> {
            fieldProperty.set(field().withNarrativeValue(newValue));
        });
        dataTypeProperty.addListener((observable, oldValue, newValue) -> {
            fieldProperty.set(field().withDataTypeNid(newValue.nid()));
        });
        purposeProperty.addListener((observable, oldValue, newValue) -> {
            fieldProperty.set(field().withPurposeNid(newValue.nid()));
        });
        meaningProperty.addListener((observable, oldValue, newValue) -> {
            fieldProperty.set(field().withMeaningNid(newValue.nid()));
        });

    }

    @Override
    public Optional<String> narrativeOptional() {
        return Field.super.narrativeOptional();
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

    @Override
    public SemanticEntityVersion enclosingSemanticVersion() {
        //TODO should return observable?
        return field().enclosingSemanticVersion();
    }

    public ObjectProperty<T> valueProperty() {
        return valueProperty;
    }

    public StringProperty narrativeProperty() {
        return narrativeProperty;
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
