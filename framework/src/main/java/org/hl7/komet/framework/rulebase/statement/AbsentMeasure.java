package org.hl7.komet.framework.rulebase.statement;

import org.hl7.komet.framework.rulebase.Measure;
import org.hl7.tinkar.terms.ConceptFacade;

import java.util.Optional;

public class AbsentMeasure implements Measure {
    @Override
    public float getLowerBound() {
        return 0;
    }

    @Override
    public boolean includeLowerBound() {
        return true;
    }

    @Override
    public float getUpperBound() {
        return 0;
    }

    @Override
    public boolean includeUpperBound() {
        return true;
    }

    @Override
    public Optional<Float> getResolution() {
        return Optional.empty();
    }

    @Override
    public Optional<ConceptFacade> getMeasureSemantic() {
        return Optional.empty();
    }
}
