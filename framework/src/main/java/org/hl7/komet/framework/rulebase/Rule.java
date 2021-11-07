package org.hl7.komet.framework.rulebase;

import org.eclipse.collections.api.list.ImmutableList;
import org.hl7.komet.framework.performance.StatementStore;
import org.hl7.komet.framework.performance.Topic;
import org.hl7.tinkar.coordinate.edit.EditCoordinate;
import org.hl7.tinkar.coordinate.view.calculator.ViewCalculator;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public interface Rule {
    default UUID ruleUUID() {
        return UUID.nameUUIDFromBytes(this.getClass().getName().getBytes(StandardCharsets.UTF_8));
    }

    String name();

    String description();

    Topic topicToProcess();

    ImmutableList<Consequence<?>> execute(StatementStore observations, ViewCalculator viewCalculator, EditCoordinate editCoordinate);
}
