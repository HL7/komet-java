package org.hl7.komet.classification;

import org.hl7.tinkar.common.service.PrimitiveData;
import org.hl7.tinkar.terms.ConceptFacade;
import org.hl7.tinkar.terms.TinkarTerm;

public enum LogicalMeaning {
    AND(TinkarTerm.AND),
    CONCEPT(TinkarTerm.CONCEPT_REFERENCE),
    DEFINITION_ROOT(TinkarTerm.DEFINITION_ROOT),
    NECESSARY_SET(TinkarTerm.NECESSARY_SET),
    OR(TinkarTerm.OR),
    PROPERTY_PATTERN_IMPLICATION(TinkarTerm.PROPERTY_PATTERN_IMPLICATION),
    PROPERTY_SET(TinkarTerm.PROPERTY_SET),
    ROLE(TinkarTerm.ROLE_TYPE),
    //ROLE_SOME(TinkarTerm.EXISTENTIAL_RESTRICTION),
    SUFFICIENT_SET(TinkarTerm.SUFFICIENT_SET),
    DISJOINT_WITH(TinkarTerm.DISJOINT_WITH),
    ROLE_ALL(TinkarTerm.UNIVERSAL_RESTRICTION),
    FEATURE(TinkarTerm.FEATURE),
    LITERAL_BOOLEAN(TinkarTerm.BOOLEAN_LITERAL),
    LITERAL_FLOAT(TinkarTerm.FLOAT_LITERAL),
    LITERAL_INSTANT(TinkarTerm.INSTANT_LITERAL),
    LITERAL_INTEGER(TinkarTerm.INTEGER_LITERAL),
    LITERAL_STRING(TinkarTerm.STRING_LITERAL),
    TEMPLATE(TinkarTerm.TEMPLATE_CONCEPT),
    SUBSTITUTION_CONCEPT(TinkarTerm.CONCEPT_SUBSTITUTION),
    SUBSTITUTION_BOOLEAN(TinkarTerm.BOOLEAN_SUBSTITUTION),
    SUBSTITUTION_FLOAT(TinkarTerm.FLOAT_SUBSTITUTION),
    SUBSTITUTION_INSTANT(TinkarTerm.INSTANT_SUBSTITUTION),
    SUBSTITUTION_INTEGER(TinkarTerm.INTEGER_SUBSTITUTION),
    SUBSTITUTION_STRING(TinkarTerm.STRING_SUBSTITUTION);

    public final int nid;

    LogicalMeaning(ConceptFacade meaningFacade) {
        this.nid = meaningFacade.nid();
    }

    public static LogicalMeaning get(ConceptFacade meaningFacade) {
        return get(meaningFacade.nid());
    }

    public static LogicalMeaning get(int meaningNid) {
        for (LogicalMeaning meaning : LogicalMeaning.values()) {
            if (meaning.nid == meaningNid) {
                return meaning;
            }
        }
        throw new IllegalStateException("No meaning for nid: " + meaningNid + " " + PrimitiveData.text(meaningNid));
    }


}
