package org.hl7.komet.framework.performance.impl;

import org.hl7.komet.framework.performance.Measure;
import org.hl7.tinkar.terms.ConceptFacade;

import java.util.Optional;

public record MeasureRecord(Float lowerBound, boolean includeLowerBound, Float upperBound, boolean includeUpperBound,
                            Float resolution, ConceptFacade measureSemantic) implements Measure {
    @Override
    public float getLowerBound() {
        return lowerBound;
    }

    @Override
    public float getUpperBound() {
        return upperBound;
    }

    @Override
    public Optional<Float> getResolution() {
        return Optional.ofNullable(resolution);
    }

    @Override
    public Optional<ConceptFacade> getMeasureSemantic() {
        return Optional.ofNullable(measureSemantic);
    }
}
