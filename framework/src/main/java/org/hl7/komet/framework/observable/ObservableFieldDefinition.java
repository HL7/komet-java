package org.hl7.komet.framework.observable;

import org.hl7.tinkar.entity.FieldDefinitionForEntity;
import org.hl7.tinkar.entity.FieldDefinitionRecord;

public class ObservableFieldDefinition
        implements FieldDefinitionForEntity {

    public ObservableFieldDefinition(FieldDefinitionRecord fieldDefinitionRecord) {

    }

    /*
        int dataTypeNid, int purposeNid, int meaningNid,
        PatternVersionRecord patternVersion
         */
    @Override
    public int dataTypeNid() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int purposeNid() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int meaningNid() {
        throw new UnsupportedOperationException();
    }
}
