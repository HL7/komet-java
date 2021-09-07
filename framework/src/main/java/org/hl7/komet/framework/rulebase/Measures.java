package org.hl7.komet.framework.rulebase;

import org.hl7.komet.framework.rulebase.statement.AbsentMeasure;
import org.hl7.komet.framework.rulebase.statement.MeasureRecord;
import org.hl7.komet.framework.rulebase.statement.PresentMeasure;
import org.hl7.tinkar.terms.ConceptFacade;

/**
 * Factory access for measures.
 */
public class Measures {
    private static final Measure presentMeasure = new PresentMeasure();
    private static final Measure absentMeasure = new AbsentMeasure();

    public static final Measure present() {
        return presentMeasure;
    }

    public static final Measure absent() {
        return absentMeasure;
    }

    public static final Measure ofInclusive(Float measureValue) {
        return new MeasureRecord(measureValue, true, measureValue, true, null, null);
    }

    public static final Measure ofInclusive(Float measureValue, ConceptFacade measureSemantic) {
        return new MeasureRecord(measureValue, true, measureValue, true, null, measureSemantic);
    }

    public static final Measure ofInclusive(Float lowerBound, Float upperBound) {
        return new MeasureRecord(lowerBound, true, upperBound, true, null, null);
    }

    public static final Measure ofInclusive(Float lowerBound, Float upperBound, ConceptFacade measureSemantic) {
        return new MeasureRecord(lowerBound, true, upperBound, true, null, measureSemantic);
    }

    public static final Measure ofExclusive(Float lowerBound, Float upperBound) {
        return new MeasureRecord(lowerBound, false, upperBound, false, null, null);
    }

    public static final Measure ofExclusive(Float lowerBound, Float upperBound, ConceptFacade measureSemantic) {
        return new MeasureRecord(lowerBound, false, upperBound, false, null, measureSemantic);
    }

    public static final Measure of(Float lowerBound, boolean includeLowerBound, Float upperBound, boolean includeUpperBound,
                                   Float resolution, ConceptFacade measureSemantic) {
        return new MeasureRecord(lowerBound, includeLowerBound, upperBound, includeUpperBound, resolution, measureSemantic);
    }

    public static final Measure of(Float lowerBound, boolean includeLowerBound, Float upperBound, boolean includeUpperBound) {
        return new MeasureRecord(lowerBound, includeLowerBound, upperBound, includeUpperBound, null, null);
    }
}
