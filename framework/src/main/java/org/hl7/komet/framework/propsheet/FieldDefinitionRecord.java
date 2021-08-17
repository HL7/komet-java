package org.hl7.komet.framework.propsheet;

import org.hl7.tinkar.entity.PatternEntityVersion;
import org.hl7.tinkar.terms.ConceptFacade;

public record FieldDefinitionRecord(ConceptFacade value, String propertyDescription, String propertyName,
                                    PatternEntityVersion enclosingPatternVersion) {
}
