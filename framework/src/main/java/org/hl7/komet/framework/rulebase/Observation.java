package org.hl7.komet.framework.rulebase;

public interface Observation {

    Topic topic();

    /**
     * @return the subject of this observation.
     */
    Object subject();

    default boolean isPresent() {
        return value().isPresent();
    }

    Measure value();

    default boolean isAbsent() {
        return value().isAbsent();
    }

    default boolean mightBePresent() {
        return value().mightBePresent();
    }

    default boolean mightBeAbsent() {
        return value().mightBeAbsent();
    }

    default boolean withinRange(Float rangeBottom, Float rangeTop) {
        return value().withinRange(rangeBottom, rangeTop);
    }

    default boolean withinRange(Float numberToTest) {
        return value().withinRange(numberToTest);
    }

}
