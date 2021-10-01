package org.hl7.komet.framework.observable;

import org.eclipse.collections.api.list.ImmutableList;
import org.hl7.tinkar.entity.FieldDefinitionForEntity;
import org.hl7.tinkar.entity.PatternEntityVersion;
import org.hl7.tinkar.entity.PatternVersionRecord;

public class ObservablePatternVersion
        extends ObservableNonStampVersion<PatternVersionRecord>
        implements PatternEntityVersion {
    ObservablePatternVersion(PatternVersionRecord patternVersionRecord) {
        super(patternVersionRecord);
    }

    @Override
    protected PatternVersionRecord withStampNid(int stampNid) {
        return version().withStampNid(stampNid);
    }

    @Override
    public int semanticPurposeNid() {
        return version().semanticPurposeNid();
    }

    @Override
    public int semanticMeaningNid() {
        return version().semanticMeaningNid();
    }

    @Override
    public ImmutableList<FieldDefinitionForEntity> fieldDefinitions() {
        return version().fieldDefinitions();
    }
}
