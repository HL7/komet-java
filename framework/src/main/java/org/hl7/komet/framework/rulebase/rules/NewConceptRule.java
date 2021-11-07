package org.hl7.komet.framework.rulebase.rules;

import org.hl7.komet.framework.performance.Statement;
import org.hl7.komet.framework.performance.Topic;
import org.hl7.komet.framework.performance.impl.RequestRecord;
import org.hl7.komet.framework.rulebase.RuntimeRule;
import org.hl7.komet.framework.rulebase.actions.AbstractActionGenerated;
import org.hl7.komet.framework.rulebase.actions.NewConceptFromTextActionGenerated;
import org.hl7.tinkar.coordinate.edit.EditCoordinate;
import org.hl7.tinkar.coordinate.view.calculator.ViewCalculator;

import java.util.Optional;

@RuntimeRule
public class NewConceptRule extends AbstractComponentRule {
    @Override
    public String name() {
        return "New concept from text";
    }

    @Override
    public String description() {
        return "Create a new concept from provided text";
    }

    @Override
    public Topic topicToProcess() {
        return Topic.NEW_CONCEPT_REQUEST;
    }

    @Override
    boolean conditionsMet(Statement statement, ViewCalculator viewCalculator) {
        return statement.topic().equals(Topic.NEW_CONCEPT_REQUEST) && statement.subject() instanceof String;
    }

    @Override
    Optional<AbstractActionGenerated> makeAction(Statement statement, ViewCalculator viewCalculator, EditCoordinate editCoordinate) {
        if (statement.topic().equals(Topic.NEW_CONCEPT_REQUEST) &&
                statement instanceof RequestRecord request &&
                request.subject() instanceof String newConceptText) {
            return Optional.of(new NewConceptFromTextActionGenerated(request, viewCalculator, editCoordinate));
        }
        return Optional.empty();
    }
}
