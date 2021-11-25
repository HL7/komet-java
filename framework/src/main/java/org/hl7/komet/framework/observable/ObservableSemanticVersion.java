package org.hl7.komet.framework.observable;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.hl7.tinkar.entity.*;

public final class ObservableSemanticVersion
        extends ObservableVersion<SemanticVersionRecord>
        implements SemanticEntityVersion {
    ObservableSemanticVersion(SemanticVersionRecord semanticVersionRecord) {
        super(semanticVersionRecord);
    }

    @Override
    protected SemanticVersionRecord withStampNid(int stampNid) {
        return version().withStampNid(stampNid);
    }

    @Override
    public SemanticVersionRecord getVersionRecord() {
        return version();
    }

    @Override
    public SemanticEntity entity() {
        return version().entity();
    }

    @Override
    public SemanticEntity chronology() {
        return version().chronology();
    }

    @Override
    public ImmutableList<Object> fieldValues() {
        return version().fieldValues();
    }

    @Override
    public ImmutableList<ObservableField> fields(PatternEntityVersion patternVersion) {
        ObservableField[] fieldArray = new ObservableField[fieldValues().size()];
        for (int i = 0; i < fieldArray.length; i++) {
            Object value = fieldValues().get(i);
            FieldDefinitionForEntity fieldDef = patternVersion.fieldDefinitions().get(i);
            FieldDefinitionRecord fieldDefinitionRecord = new FieldDefinitionRecord(fieldDef.dataTypeNid(),
                    fieldDef.purposeNid(), fieldDef.meaningNid(), patternVersion.stampNid(), patternVersion.nid());
            fieldArray[i] = new ObservableField(new FieldRecord(value, this.nid(), this.stampNid(), i, fieldDefinitionRecord));
        }
        return Lists.immutable.of(fieldArray);
    }


}
