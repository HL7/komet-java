package org.hl7.komet.framework.rulebase.rules;

import org.hl7.komet.framework.performance.Observation;
import org.hl7.komet.framework.performance.Statement;
import org.hl7.komet.framework.performance.Topic;
import org.hl7.komet.framework.rulebase.RuntimeRule;
import org.hl7.komet.framework.rulebase.actions.AbstractActionGenerated;
import org.hl7.komet.framework.rulebase.actions.ActivateComponentActionGenerated;
import org.hl7.tinkar.coordinate.edit.EditCoordinate;
import org.hl7.tinkar.coordinate.view.calculator.ViewCalculator;
import org.hl7.tinkar.entity.EntityVersion;

import java.util.Optional;

@RuntimeRule
public class ActivateComponentRule extends AbstractComponentRule {

    @Override
    boolean conditionsMet(Statement statement, ViewCalculator viewCalculator) {
        if (statement.subject() instanceof EntityVersion entityVersion) {
            if (!entityVersion.active()) {
                return true;
            }
        }
        return false;
    }

    @Override
    Optional<AbstractActionGenerated> makeAction(Statement statement, ViewCalculator viewCalculator, EditCoordinate editCoordinate) {
        if (statement instanceof Observation observation && observation.isPresent() && statement.subject() instanceof EntityVersion entityVersion) {
            return Optional.of(new ActivateComponentActionGenerated(entityVersion, viewCalculator, editCoordinate));
        }
        return Optional.empty();
    }

    @Override
    public String name() {
        return "Activate component";
    }

    @Override
    public String description() {
        return "An action that will activate a retired component, leaving component in an uncommitted state";
    }

    @Override
    public Topic topicToProcess() {
        return Topic.COMPONENT_FOCUSED;
    }
}
