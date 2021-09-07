package org.hl7.komet.framework.rulebase;

import org.eclipse.collections.api.list.ImmutableList;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public interface Rule {
    default UUID ruleUUID() {
        return UUID.nameUUIDFromBytes(this.getClass().getName().getBytes(StandardCharsets.UTF_8));
    }

    String name();

    String description();

    ImmutableList<Topic> topicsToProcess();

    ImmutableList<Consequence<?>> execute(ObservationStore observations);
}
