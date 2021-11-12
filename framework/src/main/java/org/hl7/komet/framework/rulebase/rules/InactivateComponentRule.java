package org.hl7.komet.framework.rulebase.rules;

import org.hl7.komet.framework.performance.Observation;
import org.hl7.komet.framework.performance.Statement;
import org.hl7.komet.framework.performance.Topic;
import org.hl7.komet.framework.rulebase.RuntimeRule;
import org.hl7.komet.framework.rulebase.actions.AbstractActionGenerated;
import org.hl7.komet.framework.rulebase.actions.InactivateComponentActionGenerated;
import org.hl7.tinkar.coordinate.edit.EditCoordinate;
import org.hl7.tinkar.coordinate.view.calculator.ViewCalculator;
import org.hl7.tinkar.entity.EntityVersion;

import java.util.Optional;

@RuntimeRule
public class InactivateComponentRule extends AbstractComponentRule {

    @Override
    public String name() {
        return "Retire component";
    }

    @Override
    public String description() {
        return "An action that will retire an active component, leaving component in an uncommitted state";
    }

    @Override
    public Topic topicToProcess() {
        return Topic.COMPONENT_FOCUSED;
    }

    @Override
    boolean conditionsMet(Statement statement, ViewCalculator viewCalculator) {
        if (statement instanceof Observation observation && observation.isPresent() && statement.subject() instanceof EntityVersion entityVersion) {
            if (entityVersion.active()) {
                return true;
            }
        }
        return false;
    }

    @Override
    Optional<AbstractActionGenerated> makeAction(Statement statement, ViewCalculator viewCalculator, EditCoordinate editCoordinate) {
        if (statement.subject() instanceof EntityVersion entityVersion) {
            return Optional.of(new InactivateComponentActionGenerated(entityVersion, viewCalculator, editCoordinate));
        }
        return Optional.empty();
    }
}
