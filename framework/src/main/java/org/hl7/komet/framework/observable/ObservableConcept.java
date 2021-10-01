package org.hl7.komet.framework.observable;

import org.hl7.tinkar.coordinate.view.calculator.ViewCalculator;
import org.hl7.tinkar.entity.ConceptEntity;
import org.hl7.tinkar.entity.ConceptVersionRecord;

public class ObservableConcept
        extends ObservableEntity<ObservableConceptVersion, ConceptVersionRecord>
        implements ConceptEntity<ObservableConceptVersion> {
    ObservableConcept(ConceptEntity<ConceptVersionRecord> conceptEntity) {
        super(conceptEntity);
    }

    @Override
    protected ObservableConceptVersion wrap(ConceptVersionRecord version) {
        return new ObservableConceptVersion(version);
    }

    @Override
    public ObservableConceptSnapshot getSnapshot(ViewCalculator calculator) {
        return new ObservableConceptSnapshot(calculator, this);
    }
}
