package org.hl7.komet.framework.builder;

import io.soabase.recordbuilder.core.RecordBuilder;
import org.hl7.tinkar.terms.ConceptFacade;
import org.hl7.tinkar.terms.PatternFacade;

@RecordBuilder
public record AcceptabilityRecord(PatternFacade acceptabilityPattern, ConceptFacade acceptabilityValue)
        implements AcceptabilityRecordBuilder.With {

}
