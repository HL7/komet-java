package org.hl7.komet.framework.rulebase;

import java.util.UUID;

public record ConsequenceAction(UUID consequenceUUID,
                                UUID ruleUUID,
                                SuggestedAction suggestedAction) implements Consequence<SuggestedAction> {
    @Override
    public SuggestedAction get() {
        return suggestedAction;
    }
}
