package org.hl7.komet.framework.performance.impl;

import org.hl7.komet.framework.performance.Measure;
import org.hl7.tinkar.terms.ConceptFacade;

import java.util.Optional;

public class PresentMeasure implements Measure {
    @Override
    public float getLowerBound() {
        return 0;
    }

    @Override
    public boolean includeLowerBound() {
        return false;
    }

    @Override
    public float getUpperBound() {
        return Float.POSITIVE_INFINITY;
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
