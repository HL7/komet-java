package org.hl7.komet.classification;

import au.csiro.ontology.Factory;
import au.csiro.ontology.model.*;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.hl7.tinkar.common.alert.AlertStreams;
import org.hl7.tinkar.common.service.PrimitiveData;
import org.hl7.tinkar.common.service.TrackingCallable;
import org.hl7.tinkar.coordinate.logic.LogicCoordinateRecord;
import org.hl7.tinkar.coordinate.view.calculator.ViewCalculator;
import org.hl7.tinkar.entity.graph.DiTreeEntity;
import org.hl7.tinkar.entity.graph.EntityVertex;
import org.hl7.tinkar.terms.ConceptFacade;
import org.hl7.tinkar.terms.EntityFacade;
import org.hl7.tinkar.terms.PatternFacade;
import org.hl7.tinkar.terms.TinkarTerm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class ExtractAxiomsTask extends TrackingCallable<AxiomData> {
    private static final Logger LOG = LoggerFactory.getLogger(ExtractAxiomsTask.class);
    final ViewCalculator viewCalculator;
    final PatternFacade axiomPattern;
    AxiomData axiomData = new AxiomData();


    public ExtractAxiomsTask(ViewCalculator viewCalculator, PatternFacade axiomPattern) {
        super(false, true);
        this.viewCalculator = viewCalculator;
        this.axiomPattern = axiomPattern;
        updateTitle("Fetching axioms from: " + viewCalculator.getPreferredDescriptionTextWithFallbackOrNid(axiomPattern));
    }

    @Override
    protected AxiomData compute() throws Exception {


        AtomicInteger axiomCounter = axiomData.processedSemantics;
        PrimitiveData.get().forEachSemanticNidOfPattern(axiomPattern.nid(), i -> axiomCounter.incrementAndGet());
        final int totalAxiomCount = axiomCounter.get();
        updateProgress(0, totalAxiomCount);
        LogicCoordinateRecord logicCoordinate = viewCalculator.logicCalculator().logicCoordinateRecord();
        axiomCounter.set(0);

        viewCalculator.forEachSemanticVersionOfPatternParallel(
                logicCoordinate.statedAxiomsPatternNid(),
                (semanticEntityVersion, patternEntityVersion) -> {
                    updateProgress(axiomCounter.incrementAndGet(), totalAxiomCount);
                    int conceptNid = semanticEntityVersion.referencedComponentNid();
                    Concept concept = getConcept(conceptNid);
                    DiTreeEntity<EntityVertex> definition = (DiTreeEntity) semanticEntityVersion.fieldValues().get(0);
                    ImmutableList<Axiom> axiomsForDefinition = processDefinition(definition, conceptNid);
                    if (axiomData.nidAxiomsMap.compareAndSet(semanticEntityVersion.nid(), null, axiomsForDefinition)) {
                        axiomData.axiomsSet.addAll(axiomsForDefinition.castToList());
                    } else {
                        AlertStreams.dispatchToRoot(new IllegalStateException("Definition for " + conceptNid + " already exists. "));

                    }
                    int axiomCount = axiomCounter.incrementAndGet();
                    if (axiomCount % 100 == 0) {
                        updateProgress(axiomCount, totalAxiomCount);
                    }
                    if (axiomCounter.get() < 5) {
                        LOG.info("Axiom: \n" + semanticEntityVersion);
                    }
                }
        );
        updateProgress(totalAxiomCount, totalAxiomCount);
        updateMessage("In " + durationString());
        return axiomData;
    }

    private ImmutableList<Axiom> processDefinition(DiTreeEntity<EntityVertex> definition, int conceptNid) {
        return processRoot(definition.root(), conceptNid, definition, Lists.mutable.empty());
    }

    private ImmutableList<Axiom> processRoot(EntityVertex rootVertex,
                                             int conceptNid,
                                             DiTreeEntity<EntityVertex> definition, MutableList<Axiom> axioms)
            throws IllegalStateException {

        for (final EntityVertex childVertex : definition.successors(rootVertex)) {
            switch (LogicalMeaning.get(childVertex.getMeaningNid())) {
                case SUFFICIENT_SET -> {
                    processSufficientSet(childVertex, conceptNid, definition, axioms);
                }
                case NECESSARY_SET -> {
                    processNecessarySet(childVertex, conceptNid, definition, axioms);
                }
                case PROPERTY_SET -> {
                    processPropertySet(childVertex, conceptNid, definition, axioms);
                }

                default -> throw new IllegalStateException("Unexpected value: " + PrimitiveData.text(childVertex.getMeaningNid()));
            }
        }
        return axioms.toImmutable();
    }

    private void processNecessarySet(EntityVertex sufficientSetVertex,
                                     int conceptNid,
                                     DiTreeEntity<EntityVertex> definition, MutableList<Axiom> axioms) {
        final ImmutableList<EntityVertex> childVertexList = definition.successors(sufficientSetVertex);
        if (childVertexList.size() == 1) {
            final Optional<Concept> conjunctionConcept = generateAxioms(childVertexList.get(0), conceptNid, definition, axioms);

            if (conjunctionConcept.isPresent()) {
                axioms.add(new ConceptInclusion(getConcept(conceptNid), conjunctionConcept.get()));
            } else {
                throw new IllegalStateException("Child node must return a conjunction concept. Concept: " + conceptNid +
                        " definition: " + definition);
            }
        } else {
            throw new IllegalStateException("Necessary sets require a single AND child... " + childVertexList);
        }
    }

    private void processSufficientSet(EntityVertex necessarySetVertex,
                                      int conceptNid,
                                      DiTreeEntity<EntityVertex> definition, MutableList<Axiom> axioms) {
        final ImmutableList<EntityVertex> childVertexList = definition.successors(necessarySetVertex);
        if (childVertexList.size() == 1) {
            final Optional<Concept> conjunctionConcept = generateAxioms(childVertexList.get(0), conceptNid, definition, axioms);

            if (conjunctionConcept.isPresent()) {
                axioms.add(
                        new ConceptInclusion(
                                getConcept(conceptNid),
                                conjunctionConcept.get()));
                axioms.add(
                        new ConceptInclusion(
                                conjunctionConcept.get(),
                                getConcept(conceptNid)));
            } else {
                throw new IllegalStateException("Child node must return a conjunction concept. Concept: " + conceptNid +
                        " definition: " + definition);
            }
        } else {
            throw new IllegalStateException("Sufficient sets require a single AND child... " + childVertexList);
        }
    }

    /**
     * Gets the role.
     *
     * @param roleNid the name
     * @return the role
     */
    private Role getRole(int roleNid) {
        return Factory.createNamedRole(Integer.toString(roleNid));
        //return axiomData.nidRoleMap.computeIfAbsent(roleNid, nid -> Factory.createNamedRole(Integer.toString(roleNid)));
    }

    private Concept getConcept(int conceptNid) {
        return Factory.createNamedConcept(Integer.toString(conceptNid));
        //return axiomData.nidConceptMap.computeIfAbsent(conceptNid, nid -> Factory.createNamedConcept(Integer.toString(conceptNid)));

        /*
        //The function is applied with the current value of the element at index i as its first argument,
        // and the given update as the second argument.
        return (Concept) nidConceptMap.accumulateAndGet(conceptNid, null, (currentValue, nullValue) -> {
            if (currentValue == null) {
                return Factory.createNamedConcept(Integer.toString(conceptNid));
            }
            return currentValue;
        });
        */

    }


    private Feature getFeature(int featureNid) {
        return Factory.createNamedFeature(Integer.toString(featureNid));
        // The function is applied with the current value of the element at index i as its first argument,
        // and the given update as the second argument.
        //return axiomData.nidFeatureMap.computeIfAbsent(featureNid, nid -> Factory.createNamedFeature(Integer.toString(featureNid)));
    }

    /**
     * Generate axioms.
     *
     * @param logicVertex the logic node
     * @param conceptNid  the concept nid
     * @param definition  the logical definition
     * @return the optional
     */
    private Optional<Concept> generateAxioms(EntityVertex logicVertex,
                                             int conceptNid,
                                             DiTreeEntity<EntityVertex> definition, MutableList<Axiom> axioms) {
        switch (LogicalMeaning.get(logicVertex.getMeaningNid())) {
            case AND:
                return processAnd(logicVertex, conceptNid, definition, axioms);

            case CONCEPT:
                final ConceptFacade concept = logicVertex.propertyFast(TinkarTerm.CONCEPT_REFERENCE);
                return Optional.of(getConcept(concept.nid()));

            case DEFINITION_ROOT:
                processRoot(logicVertex, conceptNid, definition, axioms);
                break;

            case DISJOINT_WITH:
                throw new UnsupportedOperationException("Not supported by SnoRocket/EL++.");

            case FEATURE:
                return processFeatureNode(logicVertex, conceptNid, definition, axioms);

            case PROPERTY_SET:
                processPropertySet(logicVertex, conceptNid, definition, axioms);
                break;

            case OR:
                throw new UnsupportedOperationException("Not supported by SnoRocket/EL++.");

            case ROLE_ALL:
                throw new UnsupportedOperationException("Not supported by SnoRocket/EL++.");

            case ROLE:
                ConceptFacade roleOperator = logicVertex.propertyFast(TinkarTerm.ROLE_OPERATOR);
                if (roleOperator.nid() == TinkarTerm.EXISTENTIAL_RESTRICTION.nid()) {
                    return processRoleNodeSome(logicVertex, conceptNid, definition, axioms);
                } else {
                    throw new UnsupportedOperationException("Role: " + PrimitiveData.text(roleOperator.nid()) + " not supported. ");
                }

            case SUBSTITUTION_BOOLEAN:
                throw new UnsupportedOperationException("Supported, but not yet implemented.");

            case SUBSTITUTION_CONCEPT:
                throw new UnsupportedOperationException("Supported, but not yet implemented.");

            case SUBSTITUTION_FLOAT:
                throw new UnsupportedOperationException("Supported, but not yet implemented.");

            case SUBSTITUTION_INSTANT:
                throw new UnsupportedOperationException("Supported, but not yet implemented.");

            case SUBSTITUTION_INTEGER:
                throw new UnsupportedOperationException("Supported, but not yet implemented.");

            case SUBSTITUTION_STRING:
                throw new UnsupportedOperationException("Supported, but not yet implemented.");

            case TEMPLATE:
                throw new UnsupportedOperationException("Supported, but not yet implemented.");

            case LITERAL_BOOLEAN:
            case LITERAL_FLOAT:
            case LITERAL_INSTANT:
            case LITERAL_INTEGER:
            case LITERAL_STRING:
                throw new UnsupportedOperationException("Expected concept logicNode, found literal logicNode: " + logicVertex +
                        " Concept: " + conceptNid + " definition: " + definition);

            case SUFFICIENT_SET:
            case NECESSARY_SET:
                throw new UnsupportedOperationException("Not expected here: " + logicVertex);
            default:
                throw new UnsupportedOperationException("ar Can't handle: " + logicVertex);
        }

        return Optional.empty();
    }

    private void processPropertySet(EntityVertex propertySetNode,
                                    int conceptNid,
                                    DiTreeEntity<EntityVertex> definition, MutableList<Axiom> axioms) {
        final ImmutableList<EntityVertex> children = definition.successors(propertySetNode);

        if (children.size() != 1) {
            throw new IllegalStateException("PropertySetNode can only have one child. Concept: " + conceptNid +
                    " definition: " + definition);
        }

        if (!(children.get(0).getMeaningNid() == TinkarTerm.AND.nid())) {
            throw new IllegalStateException("PropertySetNode can only have AND for a child. Concept: " + conceptNid +
                    " definition: " + definition);
        }


        for (EntityVertex node : definition.successors(children.get(0))) {
            switch (LogicalMeaning.get(node.getMeaningNid())) {
                case CONCEPT:
                    final ConceptFacade successorConcept = node.propertyFast(TinkarTerm.CONCEPT_REFERENCE);
                    // TODO is this right? Getting roles for a property set?
                    axioms.add(new RoleInclusion(
                            getRole(conceptNid),
                            getRole(successorConcept.nid())));
                    break;

                case PROPERTY_PATTERN_IMPLICATION:
                    LOG.warn("Can't currently handle: " + node + " in: " + definition);
                    break;

                default:
                    throw new UnsupportedOperationException("Can't handle: " + node + " in: " + definition);
            }
        }
    }

    /**
     * Process and.
     *
     * @param andNode    the and node
     * @param conceptNid the concept nid
     * @param definition the logical definition
     * @return the optional
     */
    private Optional<Concept> processAnd(EntityVertex andNode, int conceptNid, DiTreeEntity<EntityVertex> definition,
                                         MutableList<Axiom> axioms) {
        final ImmutableList<EntityVertex> childrenLogicNodes = definition.successors(andNode);
        final Concept[] conjunctionConcepts = new Concept[childrenLogicNodes.size()];

        for (int i = 0; i < childrenLogicNodes.size(); i++) {
            conjunctionConcepts[i] = generateAxioms(childrenLogicNodes.get(i), conceptNid, definition, axioms).get();
        }

        return Optional.of(Factory.createConjunction(conjunctionConcepts));
    }

    /**
     * Process role node some.
     *
     * @param roleNodeSome the role node some
     * @param conceptNid   the concept nid
     * @param definition   the logical definition
     * @return the optional
     */
    private Optional<Concept> processRoleNodeSome(EntityVertex roleNodeSome,
                                                  int conceptNid,
                                                  DiTreeEntity<EntityVertex> definition, MutableList<Axiom> axioms) {
        ConceptFacade roleType = roleNodeSome.propertyFast(TinkarTerm.ROLE_TYPE);
        final Role theRole = getRole(roleType.nid());
        final ImmutableList<EntityVertex> children = definition.successors(roleNodeSome);

        if (children.size() != 1) {
            throw new IllegalStateException("RoleNodeSome can only have one child. Concept: " + conceptNid + " definition: " +
                    definition);
        }

        final Optional<Concept> restrictionConcept = generateAxioms(children.get(0), conceptNid, definition, axioms);

        if (restrictionConcept.isPresent()) {
            return Optional.of(Factory.createExistential(theRole, restrictionConcept.get()));
        }

        throw new UnsupportedOperationException("Child of role node can not return null concept. Concept: " +
                conceptNid + " definition: " + definition);
    }

    /**
     * Process feature node.
     *
     * @param featureNode the feature node
     * @param conceptNid  the concept nid
     * @param definition  the logical definition
     * @return the optional
     */
    private Optional<Concept> processFeatureNode(EntityVertex featureNode,
                                                 int conceptNid,
                                                 DiTreeEntity<EntityVertex> definition, MutableList<Axiom> axioms) {
        EntityFacade featureFacade = featureNode.propertyFast(TinkarTerm.FEATURE);
        final Feature theFeature = getFeature(featureFacade.nid());
        throw new UnsupportedOperationException();
        /*
        final ImmutableList<EntityVertex> children = logicGraph.successors(featureNode);

        if (children.size() != 1) {
            throw new IllegalStateException("FeatureNode can only have one child. Concept: " + conceptNid + " graph: " +
                    logicGraph);
        }

        final Optional<Literal> optionalLiteral = generateLiterals(children[0], getConcept(conceptNid), logicGraph);

        if (optionalLiteral.isPresent()) {
            switch (featureNode.getOperator()) {
                case EQUALS:
                    return Optional.of(Factory.createDatatype(theFeature, Operator.EQUALS, optionalLiteral.get()));

                case GREATER_THAN:
                    return Optional.of(Factory.createDatatype(theFeature, Operator.GREATER_THAN, optionalLiteral.get()));

                case GREATER_THAN_EQUALS:
                    return Optional.of(Factory.createDatatype(theFeature, Operator.GREATER_THAN_EQUALS, optionalLiteral.get()));

                case LESS_THAN:
                    return Optional.of(Factory.createDatatype(theFeature, Operator.LESS_THAN, optionalLiteral.get()));

                case LESS_THAN_EQUALS:
                    return Optional.of(Factory.createDatatype(theFeature, Operator.LESS_THAN_EQUALS, optionalLiteral.get()));

                default:
                    throw new UnsupportedOperationException(featureNode.getOperator().toString());
            }
        }

        throw new UnsupportedOperationException("Child of FeatureNode node cannot return null concept. Concept: " +
                conceptNid + " graph: " + logicGraph);

         */
    }
}
