package org.hl7.komet.framework.rulebase.rules;

import org.hl7.komet.framework.performance.Observation;
import org.hl7.komet.framework.performance.Statement;
import org.hl7.komet.framework.performance.Topic;
import org.hl7.komet.framework.rulebase.RuntimeRule;
import org.hl7.komet.framework.rulebase.actions.AbstractActionGenerated;
import org.hl7.komet.framework.rulebase.actions.RemoveFromTinkarBaseModelActionGenerated;
import org.hl7.tinkar.common.service.PrimitiveData;
import org.hl7.tinkar.coordinate.edit.EditCoordinate;
import org.hl7.tinkar.coordinate.stamp.calculator.Latest;
import org.hl7.tinkar.coordinate.view.calculator.ViewCalculator;
import org.hl7.tinkar.entity.ConceptEntityVersion;
import org.hl7.tinkar.entity.EntityVersion;

import java.util.Optional;

import static org.hl7.tinkar.terms.TinkarTerm.TINKAR_BASE_MODEL_COMPONENT_PATTERN;

@RuntimeRule
public class RemoveFromTinkarBaseModelRule extends AbstractComponentRule {
    @Override
    public String name() {
        return "Remove from Tinkar base model";
    }

    @Override
    public String description() {
        return "Remove concept as a mandatory part of the Tinkar base model. ";
    }

    @Override
    public Topic topicToProcess() {
        return Topic.COMPONENT_FOCUSED;
    }

    @Override
    boolean conditionsMet(Statement statement, ViewCalculator viewCalculator) {
        if (statement.subject() instanceof ConceptEntityVersion conceptVersion) {
            int[] semanticNidsForComponent = PrimitiveData.get().semanticNidsForComponentOfPattern(conceptVersion.nid(), TINKAR_BASE_MODEL_COMPONENT_PATTERN.nid());
            // case 1: never a member
            if (semanticNidsForComponent.length == 0) {
                return false;
            }
            // case 2: a member, and maybe active.
            Latest<EntityVersion> latestSemanticVersion = viewCalculator.latest(semanticNidsForComponent[0]);
            if (latestSemanticVersion.isPresent() && latestSemanticVersion.get().isActive()) {
                return true;
            }
        }
        return false;
    }

    @Override
    Optional<AbstractActionGenerated> makeAction(Statement statement, ViewCalculator viewCalculator, EditCoordinate editCoordinate) {
        if (statement instanceof Observation observation && observation.isPresent() && statement.subject() instanceof ConceptEntityVersion conceptEntityVersion) {
            return Optional.of(new RemoveFromTinkarBaseModelActionGenerated(conceptEntityVersion, viewCalculator, editCoordinate));
        }
        return Optional.empty();
    }
}