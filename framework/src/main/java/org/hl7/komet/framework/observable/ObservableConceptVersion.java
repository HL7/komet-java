package org.hl7.komet.framework.observable;

import org.hl7.tinkar.entity.ConceptEntityVersion;
import org.hl7.tinkar.entity.ConceptVersionRecord;

public class ObservableConceptVersion extends ObservableNonStampVersion<ConceptVersionRecord> implements ConceptEntityVersion {
    ObservableConceptVersion(ConceptVersionRecord conceptVersionRecord) {
        super(conceptVersionRecord);
    }

    @Override
    protected ConceptVersionRecord withStampNid(int stampNid) {
        return version().withStampNid(stampNid);
    }
}
