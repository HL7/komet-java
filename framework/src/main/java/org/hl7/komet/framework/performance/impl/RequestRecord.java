package org.hl7.komet.framework.performance.impl;

import org.hl7.komet.framework.performance.Request;
import org.hl7.komet.framework.performance.Topic;

public record RequestRecord(Topic topic, Object subject) implements Request {
    public static RequestRecord make(Topic topic, Object subject) {
        return new RequestRecord(topic, subject);
    }
}
