/**
 * Copyright (c) 2009 International Health Terminology Standards Development
 * Organisation
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p>
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 * <p>
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 * <p>
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 * <p>
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 * <p>
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 * <p>
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 * <p>
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 * <p>
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 * <p>
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 * <p>
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 * <p>
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 * <p>
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 * <p>
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 * <p>
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */

package au.csiro.snorocket.core;

import au.csiro.ontology.Node;
import au.csiro.ontology.Ontology;
import au.csiro.ontology.classification.IReasoner;
import au.csiro.ontology.model.Concept;
import au.csiro.ontology.model.Conjunction;
import au.csiro.ontology.model.Datatype;
import au.csiro.ontology.model.Existential;
import au.csiro.ontology.model.*;
import au.csiro.snorocket.core.concurrent.CR;
import au.csiro.snorocket.core.concurrent.Context;
import au.csiro.snorocket.core.model.BigIntegerLiteral;
import au.csiro.snorocket.core.model.DateLiteral;
import au.csiro.snorocket.core.model.DecimalLiteral;
import au.csiro.snorocket.core.model.FloatLiteral;
import au.csiro.snorocket.core.model.IntegerLiteral;
import au.csiro.snorocket.core.model.StringLiteral;
import au.csiro.snorocket.core.model.*;
import au.csiro.snorocket.core.util.IConceptMap;
import au.csiro.snorocket.core.util.IConceptSet;
import au.csiro.snorocket.core.util.IntIterator;
import au.csiro.snorocket.core.util.RoleSet;
import org.hl7.tinkar.common.service.TrackingCallable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.*;

/**
 * This class represents an instance of the reasoner. It uses the internal
 * ontology model. If you need to use an OWL model refer to the
 * {@link SnorocketOWLReasoner} class.
 *
 * @author Alejandro Metke
 *
 */
final public class SnorocketReasoner implements IReasoner, Serializable {

    public static final int BUFFER_SIZE = 10;
    /**
     * Serialisation version.
     */
    private static final long serialVersionUID = 1L;
    private final static Logger log = LoggerFactory.getLogger(SnorocketReasoner.class);
    private NormalisedOntology no = null;
    private IFactory factory = null;
    private boolean isClassified = false;

    /**
     * Creates an instance of Snorocket using the given base ontology.
     *
     */
    public SnorocketReasoner() {
        factory = new CoreFactory();
        no = new NormalisedOntology(factory);
    }


    private static IConceptSet getAncestors(NormalisedOntology no, int conceptInt) {
        return no.getContextIndex().get(conceptInt).getS();
    }

    /**
     * The {@link CoreFactory} can currently hold very different types of objects. These include:
     *
     * <ul>
     *   <li>NamedConcept: for TOP and BOTTOM</li>
     *   <li>Strings: for named concepts</li>
     *   <li>au.csiro.snorocket.core.model.Conjunction</li>
     *   <li>au.csiro.snorocket.core.model.Datatype</li>
     *   <li>au.csiro.snorocket.core.model.Existential</li>
     * </ul>
     *
     * @param obj
     * @return
     */
    protected Concept transformToModel(Object obj) {
        if (obj instanceof NamedConcept) {
            return (NamedConcept) obj;
        } else if (obj instanceof String) {
            return new NamedConcept((String) obj);
        } else if (obj instanceof au.csiro.snorocket.core.model.Conjunction) {
            au.csiro.snorocket.core.model.Conjunction conj = (au.csiro.snorocket.core.model.Conjunction) obj;
            List<Concept> conjs = new ArrayList<>();
            for (AbstractConcept ac : conj.getConcepts()) {
                conjs.add(transformToModel(ac));
            }
            return new Conjunction(conjs.toArray(new Concept[conjs.size()]));
        } else if (obj instanceof au.csiro.snorocket.core.model.Existential) {
            au.csiro.snorocket.core.model.Existential ex = (au.csiro.snorocket.core.model.Existential) obj;
            String roleId = (String) factory.lookupRoleId(ex.getRole());
            Concept con = transformToModel(ex.getConcept());
            return new Existential(new NamedRole(roleId), con);
        } else if (obj instanceof au.csiro.snorocket.core.model.Datatype) {
            au.csiro.snorocket.core.model.Datatype dt = (au.csiro.snorocket.core.model.Datatype) obj;
            String featureId = factory.lookupFeatureId(dt.getFeature());
            Literal l = transformLiteralToModel(dt.getLiteral());
            return new Datatype(new NamedFeature(featureId), dt.getOperator(), l);
        } else if (obj instanceof au.csiro.snorocket.core.model.Concept) {
            au.csiro.snorocket.core.model.Concept c = (au.csiro.snorocket.core.model.Concept) obj;
            Object id = factory.lookupConceptId(c.hashCode());
            if (id instanceof String) {
                return new NamedConcept((String) id);
            } else if (id instanceof NamedConcept) {
                // If TOP or BOTTOM
                return (NamedConcept) id;
            } else {
                return transformToModel(id);
            }
        } else {
            throw new RuntimeException("Unexpected abstract concept " + obj.getClass());
        }
    }

    /**
     * Transforms literal from the internal representation to the canonical representation.
     *
     * @param al
     * @return
     */
    protected Literal transformLiteralToModel(AbstractLiteral al) {
        if (al instanceof BigIntegerLiteral) {
            return new au.csiro.ontology.model.BigIntegerLiteral(((BigIntegerLiteral) al).getValue());
        } else if (al instanceof DateLiteral) {
            return new au.csiro.ontology.model.DateLiteral(((DateLiteral) al).getValue());
        } else if (al instanceof DecimalLiteral) {
            return new au.csiro.ontology.model.DecimalLiteral(((DecimalLiteral) al).getValue());
        } else if (al instanceof FloatLiteral) {
            return new au.csiro.ontology.model.FloatLiteral(((FloatLiteral) al).getValue());
        } else if (al instanceof IntegerLiteral) {
            return new au.csiro.ontology.model.IntegerLiteral(((IntegerLiteral) al).getValue());
        } else if (al instanceof StringLiteral) {
            return new au.csiro.ontology.model.StringLiteral(((StringLiteral) al).getValue());
        } else {
            throw new RuntimeException("Unexpected abstract literal " + al);
        }
    }

    /**
     * Ideally we'd return some kind of normal form axioms here.  However, in
     * the presence of GCIs this is not well defined (as far as I know -
     * Michael).
     * <p>
     * Instead, we will return stated form axioms for Sufficient conditions
     * (i.e. for INamedConcept on the RHS), and SNOMED CT DNF-based axioms for
     * Necessary conditions. The former is just a filter over the stated axioms,
     * the latter requires looking at the Taxonomy and inferred relationships.
     * <p>
     * Note that there will be <i>virtual</i> INamedConcepts that need to be
     * skipped/expanded and redundant IExistentials that need to be filtered.
     *
     * @return
     * @param trackingCallable
     */
    public Collection<Axiom> getInferredAxioms(TrackingCallable trackingCallable) {
        final Collection<Axiom> inferred = new HashSet<>();

        if (!isClassified) classify(trackingCallable);

        if (!no.isTaxonomyComputed()) {
            log.info("Building taxonomy");
            no.buildTaxonomy(trackingCallable);
        }

        final Map<String, Node> taxonomy = no.getTaxonomy();
        final IConceptMap<Context> contextIndex = no.getContextIndex();
        final IntIterator itr = contextIndex.keyIterator();
        while (itr.hasNext()) {
            final int key = itr.next();
            final String id = factory.lookupConceptId(key).toString();

            if (factory.isVirtualConcept(key) || NamedConcept.BOTTOM.equals(id)) {
                continue;
            }

            Concept rhs = getNecessary(contextIndex, taxonomy, key);

            final Concept lhs = new NamedConcept(id);
            if (!lhs.equals(rhs) && !rhs.equals(NamedConcept.TOP_CONCEPT)) { // skip trivial axioms
                inferred.add(new ConceptInclusion(lhs, rhs));
            }
        }

        return inferred;
    }

    protected Concept getNecessary(IConceptMap<Context> contextIndex, Map<String, Node> taxonomy, int key) {
        final Object id = factory.lookupConceptId(key);
        final List<Concept> result = new ArrayList<>();

        final Node node = taxonomy.get(id);
        if (node != null) {
            for (final Node parent : node.getParents()) {
                final String parentId = parent.getEquivalentConcepts().iterator().next();
                if (!NamedConcept.TOP.equals(parentId)) {      // Top is redundant
                    result.add(new NamedConcept(parentId));
                }
            }
            // Look for Datatype concepts
            final IntIterator ancestorItr = contextIndex.get(key).getS().iterator();
            while (ancestorItr.hasNext()) {
                int anc = ancestorItr.next();
                if (factory.isVirtualConcept(anc)) {
                    Object c = factory.lookupConceptId(anc);
                    if (c instanceof au.csiro.snorocket.core.model.Datatype) {
                        addDatatype(result, (au.csiro.snorocket.core.model.Datatype) c);
                    }
                }
            }
        } else if (id instanceof au.csiro.snorocket.core.model.Conjunction) {
            // In this case, we have a result of normalisation so we reach inside and grab out the parents
            for (AbstractConcept conjunct : ((au.csiro.snorocket.core.model.Conjunction) id).getConcepts()) {
                if (conjunct instanceof au.csiro.snorocket.core.model.Concept) {
                    final int conjunctInt = ((au.csiro.snorocket.core.model.Concept) conjunct).hashCode();
                    final String conjunctId = factory.lookupConceptId(conjunctInt).toString();
                    result.add(new NamedConcept(conjunctId));
                } else if (conjunct instanceof au.csiro.snorocket.core.model.Datatype) {
                    addDatatype(result, (au.csiro.snorocket.core.model.Datatype) conjunct);
                }
            }
        } else {
            // Ignore
        }

        final Context ctx = contextIndex.get(key);
        CR succ = ctx.getSucc();

        for (int roleId : succ.getRoles()) {
            NamedRole role = new NamedRole(factory.lookupRoleId(roleId).toString());
            IConceptSet values = getLeaves(succ.lookupConcept(roleId));
            for (IntIterator itr2 = values.iterator(); itr2.hasNext(); ) {
                int valueInt = itr2.next();
                if (!factory.isVirtualConcept(valueInt)) {
                    final String valueId = factory.lookupConceptId(valueInt).toString();
                    result.add(new Existential(role, new NamedConcept(valueId)));
                } else {
                    final Concept valueConcept = getNecessary(contextIndex, taxonomy, valueInt);
                    result.add(new Existential(role, Builder.build(no, valueConcept)));
                }
            }
        }

        if (result.size() == 0) {
            return NamedConcept.TOP_CONCEPT;
        } else if (result.size() == 1) {
            return result.get(0);
        } else {
            return Builder.build(no, result.toArray(new Concept[result.size()]));
        }
    }

    protected void addDatatype(final List<Concept> result, au.csiro.snorocket.core.model.Datatype datatype) {
        Feature feature = new NamedFeature(factory.lookupFeatureId(datatype.getFeature()));
        Operator operator = Operator.EQUALS;
        Literal literal;
        if (datatype.getLiteral() instanceof DecimalLiteral) {
            literal = new au.csiro.ontology.model.DecimalLiteral(((DecimalLiteral) datatype.getLiteral()).getValue());
        } else if (datatype.getLiteral() instanceof IntegerLiteral) {
            literal = new au.csiro.ontology.model.IntegerLiteral(((IntegerLiteral) datatype.getLiteral()).getValue());
        } else if (datatype.getLiteral() instanceof StringLiteral) {
            literal = new au.csiro.ontology.model.StringLiteral(((StringLiteral) datatype.getLiteral()).getValue());
        } else {
            throw new UnsupportedOperationException("Literals of type " + datatype.getLiteral().getClass().getName() +
                    " not yet supported");
        }
        result.add(new Datatype(feature, operator, literal));
    }

    /**
     * Identifies any equivalent concepts and retains only one of them.
     *
     * @param concepts
     * @return
     */
    private IConceptSet filterEquivalents(final IConceptSet concepts) {
        int[] cArray = concepts.toArray();
        boolean[] toExclude = new boolean[cArray.length];

        for (int i = 0; i < cArray.length; i++) {
            if (toExclude[i]) continue;
            final IConceptSet iAncestors = IConceptSet.FACTORY.createConceptSet(getAncestors(no, cArray[i]));
            for (int j = i + 1; j < cArray.length; j++) {
                if (iAncestors.contains(cArray[j])) {
                    final IConceptSet jAncestors = IConceptSet.FACTORY.createConceptSet(getAncestors(no, cArray[j]));
                    if (jAncestors.contains(cArray[i])) {
                        // These concepts are equivalent to mark the second concept as excluded
                        toExclude[j] = true;
                    }
                }
            }
        }

        IConceptSet res = IConceptSet.FACTORY.createConceptSet();
        for (int i = 0; i < cArray.length; i++) {
            if (!toExclude[i]) {
                res.add(cArray[i]);
            }
        }

        return res;
    }

    /*
    protected String getName(Object obj) {
        if(obj instanceof NamedConcept) {
            return ((NamedConcept) obj).getId();
        } else if(obj instanceof String) {
            return (String) obj;
        } else if(obj instanceof au.csiro.snorocket.core.model.Conjunction) {
            au.csiro.snorocket.core.model.Conjunction conj = (au.csiro.snorocket.core.model.Conjunction) obj;
            StringBuilder sb = new StringBuilder();
            AbstractConcept[] cs = conj.getConcepts();
            sb.append("(");
            sb.append(getName(cs[0]));
            for(int i = 1; i < cs.length; i++) {
                sb.append(" + ");
                sb.append(getName(cs[i]));
            }
            sb.append(")");
            return sb.toString();
        } else if(obj instanceof au.csiro.snorocket.core.model.Existential) {
            au.csiro.snorocket.core.model.Existential ex = (au.csiro.snorocket.core.model.Existential) obj;
            String roleId = (String) factory.lookupRoleId(ex.getRole());
            String conceptId = getName(ex.getConcept());
            return "(E" + roleId + "." + conceptId+")";
        } else if(obj instanceof au.csiro.snorocket.core.model.Datatype) {
            // TODO: implement
            return "MISSING";
        } else if(obj instanceof au.csiro.snorocket.core.model.Concept) {
            au.csiro.snorocket.core.model.Concept c = (au.csiro.snorocket.core.model.Concept) obj;
            return getName(factory.lookupConceptId(c.hashCode()));
        } else {
            throw new RuntimeException("Unknown type: "+obj.getClass());
        }
    }*/

    /**
     * Given a set of concepts, computes the subset such that no member of the subset is subsumed by another member.
     *
     * result = {c | c in bs and not c' in b such that c' [ c}
     *
     * @param concepts set of subsumptions to filter
     * @return
     */
    private IConceptSet getLeaves(final IConceptSet concepts) {
        // Deal with any equivalent concepts. If there are equivalent concepts in the set then we only keep one of them.
        // Otherwise, both will get eliminated from the final set.
        final IConceptSet filtered = filterEquivalents(concepts);

        final IConceptSet leafBs = IConceptSet.FACTORY.createConceptSet(filtered);
        final IConceptSet set = IConceptSet.FACTORY.createConceptSet(leafBs);

        for (final IntIterator bItr = set.iterator(); bItr.hasNext(); ) {
            final int b = bItr.next();

            final IConceptSet ancestors = IConceptSet.FACTORY.createConceptSet(getAncestors(no, b));
            ancestors.remove(b);
            leafBs.removeAll(ancestors);
        }
        return leafBs;
    }

    /**
     * Prints a concept given its internal id. Useful for debugging.
     *
     * @param id
     * @return
     */
    protected String printConcept(int id) {
        Object oid = factory.lookupConceptId(id);
        if (factory.isVirtualConcept(id)) {
            if (oid instanceof AbstractConcept) {
                return printAbstractConcept((AbstractConcept) oid);
            } else {
                return oid.toString();
            }
        } else {
            return (String) oid;
        }
    }

    /**
     * Prints an abstract concept. Useful for debugging.
     *
     * @param ac
     * @return
     */
    private String printAbstractConcept(AbstractConcept ac) {
        if (ac instanceof au.csiro.snorocket.core.model.Concept) {
            au.csiro.snorocket.core.model.Concept c = (au.csiro.snorocket.core.model.Concept) ac;
            Object o = factory.lookupConceptId(c.hashCode());
            if (o instanceof String) {
                return (String) o;
            } else {
                return printAbstractConcept((AbstractConcept) o);
            }
        } else if (ac instanceof au.csiro.snorocket.core.model.Conjunction) {
            au.csiro.snorocket.core.model.Conjunction c = (au.csiro.snorocket.core.model.Conjunction) ac;
            AbstractConcept[] acs = c.getConcepts();
            StringBuilder sb = new StringBuilder();
            if (acs != null && acs.length > 0) {
                sb.append(printAbstractConcept(acs[0]));
                for (int i = 1; i < acs.length; i++) {
                    sb.append(" + ");
                    sb.append(printAbstractConcept(acs[i]));
                }
            }
            return sb.toString();
        } else if (ac instanceof au.csiro.snorocket.core.model.Existential) {
            au.csiro.snorocket.core.model.Existential e = (au.csiro.snorocket.core.model.Existential) ac;
            return "E" + factory.lookupRoleId(e.getRole()).toString() + "." + printAbstractConcept(e.getConcept());
        } else if (ac instanceof au.csiro.snorocket.core.model.Datatype) {
            au.csiro.snorocket.core.model.Datatype d = (au.csiro.snorocket.core.model.Datatype) ac;
            return "F" + factory.lookupFeatureId(d.getFeature()) + ".(" + d.getOperator() + ", " + d.getLiteral() + ")";
        } else {
            throw new RuntimeException("Unexpected concept: " + ac);
        }
    }

    /**
     * Sets the number of threads to use in the saturation phase.
     *
     * @param numThreads
     */
    public void setNumThreads(int numThreads) {
        no.setNumThreads(numThreads);
    }

    final static class Builder {
        final private NormalisedOntology no;
        final private IFactory factory;
        final private Map<Integer, RoleSet> rc;

        final private Set<Existential> items = new HashSet<>();

        private Builder(NormalisedOntology no) {
            this.no = no;
            this.factory = no.factory;
            this.rc = no.getRoleClosureCache();
        }

        static Concept build(NormalisedOntology no, Concept... concepts) {
            final List<Concept> list = new ArrayList<>();
            final Builder b = new Builder(no);

            for (final Concept member : concepts) {
                if (member instanceof Existential) {
                    final Existential existential = (Existential) member;

                    b.build((NamedRole) existential.getRole(), build(no, existential.getConcept()));
                } else {
                    list.add(buildOne(no, member));
                }
            }

            list.addAll(b.get());

            if (list.size() == 1) {
                return list.get(0);
            } else {
                return new Conjunction(list);
            }
        }

        private static Concept buildOne(NormalisedOntology no, Concept concept) {
            if (concept instanceof Existential) {
                final Existential existential = (Existential) concept;

                return new Existential(existential.getRole(), buildOne(no, existential.getConcept()));
            } else if (concept instanceof Conjunction) {
                return build(no, ((Conjunction) concept).getConcepts());
            } else if (concept instanceof NamedConcept) {
                return concept;
            } else if (concept instanceof Datatype) {
                return concept;
            } else {
                throw new RuntimeException("Unexpected type: " + concept);
            }
        }

        /**
         * Incomplete implementation - only handles symmetrically structured expressions
         * which is (should be) sufficient for SCT & AMT as of 2014
         *
         * @param a
         * @param b
         * @return
         */
        boolean subsumesOrEqual(Concept a, Concept b) {
            if (a.equals(b)) {  // This handles the (limited) Datatype case
                return true;
            } else if (a instanceof NamedConcept && b instanceof NamedConcept) {
                final int aId = factory.getConcept(((NamedConcept) a).getId());
                final int bId = factory.getConcept(((NamedConcept) b).getId());

                return getAncestors(no, bId).contains(aId);
            } else if (a instanceof Existential && b instanceof Existential) {
                final int arId = factory.getRole(((NamedRole) ((Existential) a).getRole()).getId());
                final int brId = factory.getRole(((NamedRole) ((Existential) b).getRole()).getId());

                if (rc.get(brId).contains(arId)) {
                    return subsumesOrEqual(((Existential) a).getConcept(), ((Existential) b).getConcept());
                }
            } else if (a instanceof Conjunction && b instanceof Conjunction) {
                for (Concept ac : ((Conjunction) a).getConcepts()) {
                    boolean subsumes = false;
                    for (Concept bc : ((Conjunction) b).getConcepts()) {
                        subsumes |= subsumesOrEqual(ac, bc);
                        if (subsumes) {
                            break;
                        }
                    }
                    if (!subsumes) {
                        return false;
                    }
                }

                return true;
            }

            return false;
        }

        /**
         * Two cases to handle:<ol>
         * <li> We are trying to add something that is redundant
         * <li> We are trying to add something that makes an already-added thing redundant
         * </ol>
         */
        private void build(NamedRole role, Concept concept) {
            final List<Existential> remove = new ArrayList<>();
            boolean subsumed = false;

            if (log.isTraceEnabled()) log.trace("check for subsumption: " + role + "." + concept);

            if (!(concept instanceof NamedConcept)) {
//                log.debug("WARNING: pass through of complex value: " + concept);
//                doAdd(role, concept);
                final Existential existential = new Existential(role, concept);
                for (Existential candidate : items) {
                    if (subsumesOrEqual(existential, candidate)) {
                        subsumed = true;
                    } else if (subsumesOrEqual(candidate, existential)) {
                        remove.add(candidate);
                    }
                }
            } else {
                final int cInt = factory.getConcept(((NamedConcept) concept).getId());
                final IConceptSet cAncestorSet = getAncestors(no, cInt);
                final int rInt = factory.getRole(role.getId());
                final RoleSet rSet = rc.get(rInt);

                for (Existential candidate : items) {
                    final Concept value = candidate.getConcept();
                    if (!(value instanceof NamedConcept)) {
                        log.debug("WARNING: pass through of nested complex value: " + value);
                        continue;
                    }

                    final int dInt = factory.getConcept(((NamedConcept) value).getId());
                    final IConceptSet dAncestorSet = getAncestors(no, dInt);
                    final int sInt = factory.getRole(((NamedRole) candidate.getRole()).getId());
                    final RoleSet sSet = rc.get(sInt);

                    if (rInt == sInt && cInt == dInt) {
                        subsumed = true;
                    } else {
                        if (rSet.contains(sInt)) {
                            if (cAncestorSet.contains(dInt)) {
                                remove.add(candidate);
                                if (log.isTraceEnabled()) log.trace("\tremove " + candidate);
                            }
                        }

                        if (sSet.contains(rInt)) {
                            if (dAncestorSet.contains(cInt)) {
                                subsumed = true;
                                if (log.isTraceEnabled()) log.trace("\tsubsumed");
                            }
                        }
                    }
                }
            }

            if (subsumed && !remove.isEmpty()) {
                throw new AssertionError("Should not have items to remove if item to be added is already subsumed.");
            }

            items.removeAll(remove);
            if (!subsumed) {
                doAdd(role, concept);
            }
        }

        private Collection<Existential> get() {
            return items;
        }

        private void doAdd(NamedRole role, Concept concept) {
            items.add(new Existential(role, concept));
        }

    }

    @Override
    public void loadAxioms(Set<Axiom> axioms, TrackingCallable trackingCallable) {
        if (!isClassified) {
            no.loadAxioms(axioms, trackingCallable);
        } else {
            no.loadIncremental(axioms);
        }
    }

    @Override
    public void loadAxioms(Iterator<Axiom> axioms, TrackingCallable trackingCallable) {
        Set<Axiom> axiomSet = new HashSet<>();
        while (axioms.hasNext()) {
            Axiom axiom = axioms.next();
            if (axiom == null) continue;
            axiomSet.add(axiom);
            if (axiomSet.size() == BUFFER_SIZE) {
                loadAxioms(axiomSet, trackingCallable);
                axiomSet.clear();
            }
        }

        if (!axiomSet.isEmpty()) {
            loadAxioms(axiomSet, trackingCallable);
        }
    }

    @Override
    public void loadAxioms(Ontology ont, TrackingCallable trackingCallable) {
        loadAxioms(new HashSet<>(ont.getStatedAxioms()), trackingCallable);
    }

    @Override
    public IReasoner classify(TrackingCallable trackingCallable) {
        if (!isClassified) {
            no.classify(trackingCallable);
            isClassified = true;
        } else {
            no.classifyIncremental();
        }
        return this;
    }

    @Override
    public void prune() {
        // TODO: implement
        throw new UnsupportedOperationException();
    }

    @Override
    public Ontology getClassifiedOntology(TrackingCallable trackingCallable) {
        // Check ontology is classified
        if (!isClassified) classify(trackingCallable);

        log.info("Building taxonomy");
        no.buildTaxonomy(trackingCallable);
        Map<String, Node> taxonomy = no.getTaxonomy();
        Set<Node> affectedNodes = no.getAffectedNodes();

        return new Ontology(null, null, null, taxonomy, affectedNodes);
    }

    @Override
    public Ontology getClassifiedOntology(Ontology ont, TrackingCallable trackingCallable) {
        // Check ontology is classified
        if (!isClassified) classify(trackingCallable);
        trackingCallable.updateMessage("Building taxonomy");
        log.info("Building taxonomy");
        no.buildTaxonomy(trackingCallable);
        Map<String, Node> nodeMap = no.getTaxonomy();
        Set<Node> affectedNodes = no.getAffectedNodes();

        ont.setNodeMap(nodeMap);
        ont.setAffectedNodes(affectedNodes);

        return ont;
    }

    @Override
    public void save(OutputStream out) {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(out);
            oos.writeObject(this);
            oos.flush();
        } catch (Exception e) {
            log.error("Problem saving reasoner.", e);
            throw new RuntimeException(e);
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (Exception e) {
                }
            }
        }
    }

    @Override
    public boolean isClassified() {
        return isClassified;
    }


}
