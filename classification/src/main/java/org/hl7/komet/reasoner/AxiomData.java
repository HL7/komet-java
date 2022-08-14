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

public class AxiomData {
    final SpinedIntObjectMap<ImmutableList<Axiom>> nidAxiomsMap = new SpinedIntObjectMap<>();
    final ConcurrentHashSet<Axiom> axiomsSet = new ConcurrentHashSet<>();
    final ConcurrentHashMap<Integer, Concept> nidConceptMap = new ConcurrentHashMap<>();
    final ConcurrentHashMap<Integer, Feature> nidFeatureMap = new ConcurrentHashMap<>();
    final ConcurrentHashMap<Integer, Role> nidRoleMap = new ConcurrentHashMap<>();
    final AtomicInteger processedSemantics = new AtomicInteger();

    ImmutableIntList classificationConceptSet = null;
}
