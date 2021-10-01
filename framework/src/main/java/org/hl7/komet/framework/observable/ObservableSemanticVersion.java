package org.hl7.komet.framework.observable;

import org.eclipse.collections.api.list.ImmutableList;
import org.hl7.tinkar.entity.SemanticEntity;
import org.hl7.tinkar.entity.SemanticEntityVersion;
import org.hl7.tinkar.entity.SemanticVersionRecord;

public class ObservableSemanticVersion
        extends ObservableNonStampVersion<SemanticVersionRecord>
        implements SemanticEntityVersion {
    ObservableSemanticVersion(SemanticVersionRecord semanticVersionRecord) {
        super(semanticVersionRecord);
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
    protected SemanticVersionRecord withStampNid(int stampNid) {
        return version().withStampNid(stampNid);
    }

    @Override
    public ImmutableList<Object> fields() {
        return version().fields();
    }
}
