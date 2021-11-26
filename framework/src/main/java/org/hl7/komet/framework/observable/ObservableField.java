package org.hl7.komet.framework.observable;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.MutableList;
import org.hl7.tinkar.component.FieldDataType;
import org.hl7.tinkar.entity.*;
import org.hl7.tinkar.entity.transaction.Transaction;

import java.util.Optional;

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
        if (stamp.lastVersion().committed()) {
            // Get current version
            SemanticVersionRecord version = Entity.getVersionFast(field().semanticNid(), field().semanticVersionStampNid());
            Optional<SemanticVersionRecord> optionalVersion = Entity.getVersion(field().semanticNid(), field().semanticVersionStampNid());

            MutableList fieldsForNewVersion = Lists.mutable.of(version.fieldValues().toArray());
            fieldsForNewVersion.set(fieldIndex(), newValue);

            // Create transaction
            Transaction t = Transaction.make();
            // newStamp already written to the entity store.
            StampEntity newStamp = t.getStampForEntities(stamp.state(), stamp.authorNid(), stamp.moduleNid(), stamp.pathNid(), version.entity());

            // Create new version...
            SemanticVersionRecord newVersion = version.with().fieldValues(fieldsForNewVersion.toImmutable()).stampNid(newStamp.nid()).build();
            newVersion.chronology().versionRecords().add(newVersion);

            // Entity provider will broadcast the identity of the changed entity.
            Entity.provider().putEntity(newVersion.chronology());

//            if (newValue instanceof EntityFacade entityFacade) {
//                AlertStreams.getRoot().dispatch(AlertObject.makeWarning("Changing committed version",
//                        "Changing value to " + PrimitiveData.textWithNid(entityFacade.nid()) + " for " + fieldProperty.get().toString()));
//            } else {
//                AlertStreams.getRoot().dispatch(AlertObject.makeWarning("Changing committed version",
//                        "Changing value to " + newValue + " for " + fieldProperty.get().toString()));
//            }
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
