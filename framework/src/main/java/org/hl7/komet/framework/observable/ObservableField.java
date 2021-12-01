package org.hl7.komet.framework.observable;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.MutableList;
import org.hl7.tinkar.component.FieldDataType;
import org.hl7.tinkar.entity.*;
import org.hl7.tinkar.entity.transaction.Transaction;

public class ObservableField<T> implements Field<T> {

    SimpleObjectProperty<FieldRecord<T>> fieldProperty = new SimpleObjectProperty<>();
    SimpleObjectProperty<T> valueProperty = new SimpleObjectProperty<>();

    public ObservableField(FieldRecord<T> fieldRecord) {
        fieldProperty.set(fieldRecord);
        valueProperty.set(fieldRecord.value());
        valueProperty.addListener((observable, oldValue, newValue) -> {
            handleValueChange(newValue);
            fieldProperty.set(field().withValue(newValue));
        });
    }

    private void handleValueChange(Object newValue) {
        StampRecord stamp = Entity.getStamp(fieldProperty.get().semanticVersionStampNid());
        // Get current version
        SemanticVersionRecord version = Entity.getVersionFast(field().semanticNid(), field().semanticVersionStampNid());
        SemanticRecord semantic = Entity.getFast(field().semanticNid());
        MutableList fieldsForNewVersion = Lists.mutable.of(version.fieldValues().toArray());
        fieldsForNewVersion.set(fieldIndex(), newValue);

        if (stamp.lastVersion().committed()) {

            // Create transaction
            Transaction t = Transaction.make();
            // newStamp already written to the entity store.
            StampEntity newStamp = t.getStampForEntities(stamp.state(), stamp.authorNid(), stamp.moduleNid(), stamp.pathNid(), version.entity());

            // Create new version...
            SemanticVersionRecord newVersion = version.with().fieldValues(fieldsForNewVersion.toImmutable()).stampNid(newStamp.nid()).build();

            SemanticRecord analogue = semantic.with(newVersion).build();

            // Entity provider will broadcast the nid of the changed entity.
            Entity.provider().putEntity(analogue);
        } else {
            SemanticVersionRecord newVersion = version.withFieldValues(fieldsForNewVersion.toImmutable());
            // if a version with the same stamp as newVersion exists, that version will be removed
            // prior to adding the new version so you don't get duplicate versions with the same stamp.
            SemanticRecord analogue = semantic.with(newVersion).build();
            // Entity provider will broadcast the nid of the changed entity.
            Entity.provider().putEntity(analogue);
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

    @Override
    public int fieldIndex() {
        return field().fieldIndex();
    }

    public ObjectProperty<T> valueProperty() {
        return valueProperty;
    }

}
