package org.hl7.komet.framework.rulebase.rules;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.hl7.komet.framework.rulebase.*;
import org.hl7.komet.framework.rulebase.actions.AbstractAction;

import java.util.Optional;
import java.util.UUID;

public abstract class AbstractComponentRule implements Rule {

    @Override
    public final ImmutableList<Consequence<?>> execute(ObservationStore observations) {
        MutableList<Consequence<?>> consequences = Lists.mutable.empty();
        for (Observation observation : observations.observationsForTopic(Topic.COMPONENT_FOCUSED)) {
            if (observation.isPresent() && conditionsMet(observation)) {
                makeAction(observation).ifPresent(suggestedAction -> {
                    suggestedAction.setLongText(description());
                    suggestedAction.setText(name());
                    consequences.add(new ConsequenceAction(UUID.randomUUID(),
                            ruleUUID(), suggestedAction));
                });
            }
        }
        return consequences.toImmutable();
    }

    abstract boolean conditionsMet(Observation observation);

    abstract Optional<AbstractAction> makeAction(Observation observation);

}
