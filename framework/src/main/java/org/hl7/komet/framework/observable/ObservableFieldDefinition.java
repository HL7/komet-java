package org.hl7.komet.framework.observable;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.hl7.tinkar.common.alert.AlertObject;
import org.hl7.tinkar.common.alert.AlertStreams;
import org.hl7.tinkar.common.service.PrimitiveData;
import org.hl7.tinkar.entity.*;
import org.hl7.tinkar.entity.transaction.Transaction;
import org.hl7.tinkar.terms.EntityFacade;

import java.util.concurrent.atomic.AtomicReference;

public class ObservableFieldDefinition
        implements FieldDefinitionForEntity {
    final AtomicReference<FieldDefinitionRecord> fieldDefinitionReference;
    final SimpleObjectProperty<EntityFacade> dataTypeProperty = new SimpleObjectProperty<>(this, "Field data type");
    final SimpleObjectProperty<EntityFacade> purposeProperty = new SimpleObjectProperty<>(this, "Field purpose");
    final SimpleObjectProperty<EntityFacade> meaningProperty = new SimpleObjectProperty<>(this, "Field meaning");

    public ObservableFieldDefinition(FieldDefinitionRecord fieldDefinitionRecord) {
        fieldDefinitionReference = new AtomicReference<>(fieldDefinitionRecord);
        dataTypeProperty.set(Entity.getFast(fieldDefinitionRecord.dataTypeNid()));
        dataTypeProperty.addListener(this::dataTypeChanged);
        purposeProperty.set(Entity.getFast(fieldDefinitionRecord.purposeNid()));
        purposeProperty.addListener(this::purposeChanged);
        meaningProperty.set(Entity.getFast(fieldDefinitionRecord.meaningNid()));
        meaningProperty.addListener(this::meaningChanged);
    }

    private void dataTypeChanged(ObservableValue<? extends EntityFacade> observableDataType, EntityFacade oldDataType, EntityFacade newDataType) {
        if (!handleUncommitted(FIELD.DATATYPE, observableDataType, newDataType)) {
            fieldDefinitionReference.set(fieldDefinitionReference.get().withDataTypeNid(newDataType.nid()));
        }
    }

    private void purposeChanged(ObservableValue<? extends EntityFacade> observablePurpose, EntityFacade oldPurpose, EntityFacade newPurpose) {
        handleUncommitted(FIELD.PURPOSE, observablePurpose, newPurpose);
    }

    private void meaningChanged(ObservableValue<? extends EntityFacade> observableMeaning, EntityFacade oldMeaning, EntityFacade newMeaning) {
        handleUncommitted(FIELD.MEANING, observableMeaning, newMeaning);
    }

    private boolean handleUncommitted(FIELD changedField, ObservableValue<? extends EntityFacade> observableValue, EntityFacade newValue) {
        StampRecord oldStamp = Entity.getStamp(fieldDefinitionReference.get().patternVersionStampNid());
        if (oldStamp.lastVersion().committed()) {
            FieldDefinitionRecord oldFieldDefinition = fieldDefinitionReference.get();
            PatternRecord patternRecord = Entity.getFast(oldFieldDefinition.patternNid());
            PatternVersionRecord oldPatternVersion = patternRecord.getVersionFast(oldStamp.nid());
            // Create new version...
            Transaction t = Transaction.make();
            // newStamp already written to the entity store.
            StampEntity newStamp = t.getStampForEntities(oldStamp.state(), oldStamp.authorNid(), oldStamp.moduleNid(),
                    oldStamp.pathNid(), patternRecord);

            FieldDefinitionRecord newFieldDefinition = switch (changedField) {
                case DATATYPE -> oldFieldDefinition.with().patternVersionStampNid(newStamp.nid()).dataTypeNid(newValue.nid()).build();
                case PURPOSE -> oldFieldDefinition.with().patternVersionStampNid(newStamp.nid()).purposeNid(newValue.nid()).build();
                case MEANING -> oldFieldDefinition.with().patternVersionStampNid(newStamp.nid()).meaningNid(newValue.nid()).build();
            };

            fieldDefinitionReference.set(newFieldDefinition);
            int fieldCount = oldPatternVersion.fieldDefinitions().size();
            MutableList<FieldDefinitionRecord> newFieldDefinitionMutableList = Lists.mutable.withInitialCapacity(fieldCount);
            ImmutableList<FieldDefinitionRecord> oldFieldDefinitionList = oldPatternVersion.fieldDefinitions();
            for (int fieldIndexInPattern = 0; fieldIndexInPattern < fieldCount; fieldIndexInPattern++) {
                if (fieldIndexInPattern == newFieldDefinition.indexInPattern()) {
                    newFieldDefinitionMutableList.add(newFieldDefinition);
                } else {
                    newFieldDefinitionMutableList.add(oldFieldDefinitionList.get(fieldIndexInPattern)
                            .withPatternVersionStampNid(newStamp.nid()));
                }
            }

            PatternVersionRecord newVersionRecord = oldPatternVersion.with()
                    .stampNid(newStamp.nid())
                    .fieldDefinitions(newFieldDefinitionMutableList.toImmutable())
                    .build();
            PatternRecord newPattern = patternRecord.analogueBuilder().add(newVersionRecord).build();
            // Entity provider will broadcast the nid of the changed entity.
            Entity.provider().putEntity(newPattern);
            
            if (observableValue instanceof ObjectProperty objectProperty) {
                AlertStreams.getRoot().dispatch(AlertObject.makeWarning("Changing committed version",
                        "Changing value to " + PrimitiveData.textWithNid(newValue.nid()) + " for " + objectProperty.getName()));
            } else {
                AlertStreams.getRoot().dispatch(AlertObject.makeWarning("Changing committed version",
                        "Changing value to " + PrimitiveData.textWithNid(newValue.nid()) + " for " + observableValue.toString()));
            }
            return true;
        }
        // TODO handle updates to uncommitted version...

        return false;
    }

    public SimpleObjectProperty<EntityFacade> dataTypeProperty() {
        return dataTypeProperty;
    }

    public SimpleObjectProperty<EntityFacade> purposeProperty() {
        return purposeProperty;
    }

    public SimpleObjectProperty<EntityFacade> meaningProperty() {
        return meaningProperty;
    }

    @Override
    public int dataTypeNid() {
        return dataTypeProperty.get().nid();
    }

    @Override
    public int purposeNid() {
        return purposeProperty.get().nid();
    }

    @Override
    public int meaningNid() {
        return meaningProperty.get().nid();
    }

    enum FIELD {DATATYPE, PURPOSE, MEANING}
}
