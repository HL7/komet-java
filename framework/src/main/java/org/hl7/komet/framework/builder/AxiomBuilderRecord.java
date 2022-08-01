package org.hl7.komet.framework.builder;

import org.eclipse.collections.api.RichIterable;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.MutableList;
import org.hl7.tinkar.common.id.VertexId;
import org.hl7.tinkar.common.id.VertexIds;
import org.hl7.tinkar.common.service.PrimitiveData;
import org.hl7.tinkar.component.Concept;
import org.hl7.tinkar.component.graph.Vertex;
import org.hl7.tinkar.terms.ConceptFacade;
import org.hl7.tinkar.terms.TinkarTerm;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public record AxiomBuilderRecord(ConceptFacade axiomMeaning, MutableList<AxiomPropertyRecord> properties,
                                 MutableList<AxiomBuilderRecord> children, UUID vertexUuid, int vertexIndex,
                                 AtomicInteger nextAxiomIndex)
        implements AxiomPart, Vertex {
    public AxiomBuilderRecord(AtomicInteger nextAxiomIndex) {
        this(TinkarTerm.DEFINITION_ROOT, Lists.mutable.empty(), Lists.mutable.empty(), UUID.randomUUID(),
                nextAxiomIndex.getAndIncrement(), nextAxiomIndex);
    }

    @Override
    public VertexId vertexId() {
        return VertexIds.of(vertexUuid);
    }

    @Override
    public int vertexIndex() {
        return vertexIndex;
    }

    @Override
    public Concept meaning() {
        return axiomMeaning;
    }

    @Override
    public <T> Optional<T> property(Concept propertyConcept) {
        for (AxiomPropertyRecord propertyRecord : properties) {
            if (propertyRecord.propertyMeaning().nid() == PrimitiveData.nid(propertyConcept.publicId())) {
                return Optional.of((T) propertyRecord.propertyValue());
            }
        }
        return Optional.empty();
    }

    @Override
    public <T> T propertyFast(Concept propertyConcept) {
        for (AxiomPropertyRecord propertyRecord : properties) {
            if (propertyRecord.propertyMeaning().nid() == PrimitiveData.nid(propertyConcept.publicId())) {
                return (T) propertyRecord.propertyValue();
            }
        }
        return null;
    }

    @Override
    public <C extends Concept> RichIterable<C> propertyKeys() {
        MutableList<C> keys = Lists.mutable.empty();
        for (AxiomPropertyRecord propertyRecord : properties) {
            keys.add((C) propertyRecord.propertyMeaning());
        }
        return keys;
    }

    public AxiomBuilderRecord withAnd(AxiomBuilderRecord... andChildren) {
        MutableList<AxiomBuilderRecord> andChildrenList = Lists.mutable.of(andChildren);
        AxiomBuilderRecord and = new AxiomBuilderRecord(TinkarTerm.AND, Lists.mutable.empty(), andChildrenList,
                UUID.randomUUID(), nextAxiomIndex.getAndIncrement(), nextAxiomIndex);
        this.children.add(and);
        return and;
    }

    public AxiomBuilderRecord makeSome(ConceptFacade roleType, ConceptFacade roleRestriction) {
        AxiomPropertyRecord roleTypeRecord = new AxiomPropertyRecord(TinkarTerm.ROLE_TYPE, roleType);
        AxiomPropertyRecord roleOperatorRecord = new AxiomPropertyRecord(TinkarTerm.ROLE_OPERATOR, TinkarTerm.EXISTENTIAL_RESTRICTION);
        MutableList<AxiomPropertyRecord> properties = Lists.mutable.of(roleTypeRecord, roleOperatorRecord);
        AxiomBuilderRecord some = new AxiomBuilderRecord(TinkarTerm.ROLE_TYPE, properties, Lists.mutable.empty(),
                UUID.randomUUID(), nextAxiomIndex.getAndIncrement(), nextAxiomIndex);
        some.children.add(makeConceptReference(roleRestriction));
        return some;
    }

    public AxiomBuilderRecord makeConceptReference(ConceptFacade referencedConcept) {
        AxiomBuilderRecord conceptReference = AxiomBuilderRecord.make(TinkarTerm.CONCEPT_REFERENCE, nextAxiomIndex,
                new AxiomPropertyRecord(TinkarTerm.CONCEPT_REFERENCE, referencedConcept));
        return conceptReference;
    }

    public static AxiomBuilderRecord make(ConceptFacade axiomMeaning, AtomicInteger nextAxiomIndex, AxiomPart... axiomParts) {
        MutableList<AxiomBuilderRecord> children = Lists.mutable.empty();
        MutableList<AxiomPropertyRecord> properties = Lists.mutable.empty();

        for (AxiomPart part : axiomParts) {
            switch (part) {
                case AxiomBuilderRecord axiomBuilderRecord -> children.add(axiomBuilderRecord);
                case AxiomPropertyRecord axiomPropertyRecord -> properties.add(axiomPropertyRecord);
            }
        }
        return new AxiomBuilderRecord(axiomMeaning, properties, children, UUID.randomUUID(),
                nextAxiomIndex.getAndIncrement(), nextAxiomIndex);
    }

    public void withNecessarySet(AxiomBuilderRecord... setElements) {
        withSet(TinkarTerm.NECESSARY_SET, setElements);
    }

    public void withSet(ConceptFacade setType, AxiomBuilderRecord... setElements) {
        AxiomBuilderRecord logicalSet = make(setType);
        children.add(logicalSet);

        MutableList<AxiomBuilderRecord> andChildrenList = Lists.mutable.of(setElements);
        AxiomBuilderRecord and = new AxiomBuilderRecord(TinkarTerm.AND, Lists.mutable.empty(), andChildrenList,
                UUID.randomUUID(), nextAxiomIndex.getAndIncrement(), nextAxiomIndex);
        logicalSet.children.add(and);
    }

    public AxiomBuilderRecord make(ConceptFacade axiomMeaning) {
        MutableList<AxiomBuilderRecord> children = Lists.mutable.empty();
        MutableList<AxiomPropertyRecord> properties = Lists.mutable.empty();
        return new AxiomBuilderRecord(axiomMeaning, properties, children, UUID.randomUUID(),
                nextAxiomIndex.getAndIncrement(), nextAxiomIndex);
    }

    public void withSufficientSet(AxiomBuilderRecord... setElements) {
        withSet(TinkarTerm.SUFFICIENT_SET, setElements);
    }

    public AxiomBuilderRecord makeRoleGroup(AxiomBuilderRecord... groupElements) {
        AxiomPropertyRecord roleTypeRecord = new AxiomPropertyRecord(TinkarTerm.ROLE_TYPE, TinkarTerm.ROLE_GROUP);
        AxiomPropertyRecord roleOperatorRecord = new AxiomPropertyRecord(TinkarTerm.ROLE_OPERATOR, TinkarTerm.EXISTENTIAL_RESTRICTION);
        MutableList<AxiomPropertyRecord> properties = Lists.mutable.of(roleTypeRecord, roleOperatorRecord);
        AxiomBuilderRecord group = new AxiomBuilderRecord(TinkarTerm.ROLE_TYPE, properties, Lists.mutable.empty(),
                UUID.randomUUID(), nextAxiomIndex.getAndIncrement(), nextAxiomIndex);

        MutableList<AxiomBuilderRecord> andChildrenList = Lists.mutable.of(groupElements);
        AxiomBuilderRecord and = new AxiomBuilderRecord(TinkarTerm.AND, Lists.mutable.empty(), andChildrenList,
                UUID.randomUUID(), nextAxiomIndex.getAndIncrement(), nextAxiomIndex);
        group.children.add(and);
        return group;
    }
}
