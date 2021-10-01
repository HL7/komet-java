package org.hl7.komet.framework.rulebase.rules;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.hl7.komet.framework.rulebase.Observation;
import org.hl7.komet.framework.rulebase.RuntimeRule;
import org.hl7.komet.framework.rulebase.Topic;
import org.hl7.komet.framework.rulebase.actions.AbstractAction;
import org.hl7.komet.framework.rulebase.actions.ActivateComponentAction;
import org.hl7.tinkar.coordinate.view.calculator.ViewCalculator;
import org.hl7.tinkar.entity.EntityVersion;

import java.util.Optional;

@RuntimeRule
public class ActivateComponentRule extends AbstractComponentRule {

    @Override
    boolean conditionsMet(Observation observation, ViewCalculator viewCalculator) {
        if (observation.subject() instanceof EntityVersion entityVersion) {
            if (!entityVersion.isActive()) {
                return true;
            }
        }
        return false;
    }

    @Override
    Optional<AbstractAction> makeAction(Observation observation, ViewCalculator viewCalculator) {
        if (observation.isPresent() && observation.subject() instanceof EntityVersion entityVersion) {
            return Optional.of(new ActivateComponentAction(entityVersion));
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
    public ImmutableList<Topic> topicsToProcess() {
        return Lists.immutable.of(Topic.COMPONENT_FOCUSED);
    }
}
