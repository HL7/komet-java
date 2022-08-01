package org.hl7.komet.framework.builder;

import io.soabase.recordbuilder.core.RecordBuilder;
import org.hl7.tinkar.terms.ConceptFacade;

/**
 * TODO: Deprecate?
 */
@RecordBuilder
public record AxiomPropertyRecord(ConceptFacade propertyMeaning, Object propertyValue)
        implements AxiomPart, AxiomPropertyRecordBuilder.With {
}
