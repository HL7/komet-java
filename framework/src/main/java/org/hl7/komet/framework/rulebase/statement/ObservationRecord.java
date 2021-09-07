package org.hl7.komet.framework.rulebase.statement;

import org.hl7.komet.framework.rulebase.Measure;
import org.hl7.komet.framework.rulebase.Observation;
import org.hl7.komet.framework.rulebase.Topic;

public record ObservationRecord(Topic topic, Object subject, Measure value) implements Observation {
}
