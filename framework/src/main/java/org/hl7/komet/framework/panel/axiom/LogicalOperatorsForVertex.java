package org.hl7.komet.framework.panel.axiom;

import org.hl7.tinkar.common.service.PrimitiveData;
import org.hl7.tinkar.entity.graph.EntityVertex;
import org.hl7.tinkar.terms.ConceptFacade;
import org.hl7.tinkar.terms.EntityFacade;
import org.hl7.tinkar.terms.TinkarTerm;

import java.util.Optional;

public enum LogicalOperatorsForVertex {
    /**
     * The necessary set.
     */
    NECESSARY_SET(TinkarTerm.NECESSARY_SET),

    /**
     * The sufficient set.
     */
    SUFFICIENT_SET(TinkarTerm.SUFFICIENT_SET),

    /**
     * The and.
     */
    AND(TinkarTerm.AND),

    /**
     * The or.
     */
    OR(TinkarTerm.OR),

    /**
     * The disjoint with.
     */
    DISJOINT_WITH(TinkarTerm.DISJOINT_WITH),

    /**
     * The definition root.
     */
    DEFINITION_ROOT(TinkarTerm.DEFINITION_ROOT),

    /**
     * The role all.
     */
    ROLE(TinkarTerm.ROLE_TYPE),


    /**
     * The concept.
     */
    CONCEPT(TinkarTerm.CONCEPT_REFERENCE),

    /**
     * The feature.
     */
    FEATURE(TinkarTerm.FEATURE),

    /**
     * The literal boolean.
     */
    LITERAL(TinkarTerm.LITERAL_VALUE),

    PROPERTY_SET(TinkarTerm.PROPERTY_SET),

    PROPERTY_PATTERN_IMPLICATION(TinkarTerm.PROPERTY_PATTERN_IMPLICATION);

    final ConceptFacade logicalMeaning;

    LogicalOperatorsForVertex(ConceptFacade logicalMeaning) {
        this.logicalMeaning = logicalMeaning;
    }

    public static LogicalOperatorsForVertex get(EntityFacade facade) {
        return get(facade.nid());
    }

    public static LogicalOperatorsForVertex get(int meaningNid) {
        for (LogicalOperatorsForVertex logicalOperator : LogicalOperatorsForVertex.values()) {
            if (logicalOperator.logicalMeaning.nid() == meaningNid) {
                return logicalOperator;
            }
        }
        throw new IllegalStateException("No logical operator for: " + PrimitiveData.text(meaningNid));
    }

    public static LogicalOperatorsForVertex get(EntityVertex logicVertex) {
        return get(logicVertex.getMeaningNid());
    }

    public boolean semanticallyEqual(EntityFacade entityFacade) {
        return entityFacade.nid() == logicalMeaning.nid();
    }

    public boolean semanticallyEqual(int nid) {
        return nid == logicalMeaning.nid();
    }

    public ConceptFacade getPropertyFast(EntityVertex entityVertex) {
        return entityVertex.propertyFast(this.logicalMeaning);
    }

    public <T> Optional<T> getProperty(EntityVertex entityVertex) {
        return entityVertex.property(this.logicalMeaning);
    }
}
