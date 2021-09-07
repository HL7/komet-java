package org.hl7.komet.framework.rulebase;

import org.eclipse.collections.api.list.ImmutableList;
import org.hl7.komet.framework.rulebase.statement.ObservationHashStore;

public interface ObservationStore {
    static ObservationStore make(Observation... observations) {
        return new ObservationHashStore(observations);
    }

    ImmutableList<Observation> observationsForTopic(Topic topic);

    void addObservation(Observation observation);
}
