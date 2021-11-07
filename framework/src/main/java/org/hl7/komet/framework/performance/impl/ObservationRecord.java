package org.hl7.komet.framework.performance.impl;

import org.hl7.komet.framework.performance.Measure;
import org.hl7.komet.framework.performance.Observation;
import org.hl7.komet.framework.performance.Topic;

public record ObservationRecord(Topic topic, Object subject, Measure value) implements Observation {
}
