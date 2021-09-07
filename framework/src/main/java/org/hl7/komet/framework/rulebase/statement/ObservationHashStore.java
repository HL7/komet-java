package org.hl7.komet.framework.rulebase.statement;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.hl7.komet.framework.rulebase.Observation;
import org.hl7.komet.framework.rulebase.ObservationStore;
import org.hl7.komet.framework.rulebase.Topic;

import java.util.HashMap;

public class ObservationHashStore implements ObservationStore {
    HashMap<Topic, MutableList<Observation>> observationMap = new HashMap<>();

    public ObservationHashStore(Observation... observations) {
        addObservations(observations);
    }

    private void addObservations(Observation... observations) {
        for (Observation observation : observations) {
            observationMap.computeIfAbsent(observation.topic(), topic -> Lists.mutable.empty()).add(observation);
        }
    }

    @Override
    public ImmutableList<Observation> observationsForTopic(Topic topic) {
        return observationMap.get(topic).toImmutable();
    }

    @Override
    public void addObservation(Observation observation) {
        addObservations(observation);
    }
}
