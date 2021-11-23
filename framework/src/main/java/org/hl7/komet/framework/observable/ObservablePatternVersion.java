package org.hl7.komet.framework.observable;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.hl7.tinkar.entity.FieldDefinitionRecord;
import org.hl7.tinkar.entity.PatternEntityVersion;
import org.hl7.tinkar.entity.PatternVersionRecord;

public class ObservablePatternVersion
        extends ObservableNonStampVersion<PatternVersionRecord>
        implements PatternEntityVersion {
    ImmutableList<ObservableFieldDefinition> observableFieldDefinitions;

    ObservablePatternVersion(PatternVersionRecord patternVersionRecord) {
        super(patternVersionRecord);
        MutableList<ObservableFieldDefinition> mutableFieldDefinitions = Lists.mutable.ofInitialCapacity(patternVersionRecord.fieldDefinitions().size());
        for (FieldDefinitionRecord fieldDefinition : patternVersionRecord.fieldDefinitions()) {
            mutableFieldDefinitions.add(new ObservableFieldDefinition(fieldDefinition));
        }
        this.observableFieldDefinitions = mutableFieldDefinitions.toImmutable();
    }

    @Override
    protected PatternVersionRecord withStampNid(int stampNid) {
        return version().withStampNid(stampNid);
    }

    @Override
    public ImmutableList<ObservableFieldDefinition> fieldDefinitions() {
        return this.observableFieldDefinitions;
    }

    @Override
    public int semanticPurposeNid() {
        return version().semanticPurposeNid();
    }

    @Override
    public int semanticMeaningNid() {
        return version().semanticMeaningNid();
    }
}
