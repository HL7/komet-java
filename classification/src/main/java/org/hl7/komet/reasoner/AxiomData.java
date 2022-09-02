package org.hl7.komet.reasoner;

import au.csiro.ontology.model.Axiom;
import au.csiro.ontology.model.Concept;
import au.csiro.ontology.model.Feature;
import au.csiro.ontology.model.Role;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.primitive.ImmutableIntList;
import org.hl7.tinkar.collection.SpinedIntObjectMap;
import org.hl7.tinkar.common.sets.ConcurrentHashSet;
import org.roaringbitmap.buffer.ImmutableRoaringBitmap;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class AxiomData<A> {
    public final SpinedIntObjectMap<ImmutableList<A>> nidAxiomsMap = new SpinedIntObjectMap<>();
    public final ConcurrentHashSet<A> axiomsSet = new ConcurrentHashSet<>();
    public final ConcurrentHashMap<Integer, Concept> nidConceptMap = new ConcurrentHashMap<>();
    public final ConcurrentHashMap<Integer, Feature> nidFeatureMap = new ConcurrentHashMap<>();
    public final ConcurrentHashMap<Integer, Role> nidRoleMap = new ConcurrentHashMap<>();
    public final AtomicInteger processedSemantics = new AtomicInteger();

    public ImmutableIntList classificationConceptSet = null;
}
