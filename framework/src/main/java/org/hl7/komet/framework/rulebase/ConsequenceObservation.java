package org.hl7.komet.framework.rulebase;

import org.hl7.komet.framework.performance.Observation;

import java.util.UUID;

public record ConsequenceObservation(UUID consequenceUUID,
                                     UUID ruleUUID,
                                     Observation newObservation) implements Consequence<Observation> {
    @Override
    public Observation get() {
        return newObservation;
    }
}
