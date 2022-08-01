package org.hl7.komet.reasoner;

import org.eclipse.collections.api.block.procedure.primitive.IntObjectProcedure;
import org.eclipse.collections.api.list.primitive.ImmutableIntList;
import org.eclipse.collections.api.map.primitive.MutableIntObjectMap;
import org.eclipse.collections.api.set.primitive.ImmutableIntSet;
import org.eclipse.collections.api.set.primitive.IntSet;
import org.eclipse.collections.api.set.primitive.MutableIntSet;
import org.eclipse.collections.impl.factory.primitive.IntObjectMaps;
import org.eclipse.collections.impl.factory.primitive.IntSets;
import org.hl7.tinkar.common.binary.*;
import org.hl7.tinkar.coordinate.view.ViewCoordinateRecord;

import java.time.Instant;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class ClassifierResults implements Encodable {

    public static final int marshalVersion = 1;
    /**
     * Set of concepts potentially affected by the last classification.
     */
    private final MutableIntSet classificationConceptSet = IntSets.mutable.empty();

    private final MutableIntSet conceptsWithInferredChanges = IntSets.mutable.empty();

    /**
     * The equivalent sets.
     */
    private final Set<int[]> equivalentSets;

    /**
     * The commit record.
     */
    private final ViewCoordinateRecord viewCoordinate;
    //A map of a concept nid, to a HashSet of int arrays, where each int[] is a cycle present on the concept.
    private MutableIntObjectMap<Set<int[]>> conceptsWithCycles = IntObjectMaps.mutable.empty();
    private MutableIntSet orphanedConcepts = IntSets.mutable.empty();

    private ClassifierResults(DecoderInput data) {
        for (int nid : data.readNidArray()) {
            classificationConceptSet.add(nid);
        }
        int equivalentSetSize = data.readInt();
        equivalentSets = new HashSet<>(equivalentSetSize);
        for (int i = 0; i < equivalentSetSize; i++) {
            equivalentSets.add(data.readNidArray());
        }
        cleanUpEquivalentSets();
        if (data.readBoolean()) {
            int cycleMapSize = data.readInt();
            for (int i = 0; i < cycleMapSize; i++) {
                int key = data.readInt();
                int setCount = data.readInt();
                Set<int[]> cycleSets = new TreeSet<>();
                for (int j = 0; j < setCount; j++) {
                    cycleSets.add(data.readNidArray());
                }
                this.conceptsWithCycles.put(key, cycleSets);
            }
        }
        for (int orphanNid : data.readNidArray()) {
            orphanedConcepts.add(orphanNid);
        }
        this.viewCoordinate = ViewCoordinateRecord.decode(data);
    }

    private final void cleanUpEquivalentSets() {
        // remove any duplicate sets...
        HashSet<IntSet> cleanEquivalentSets = new HashSet<>();
        for (int[] set : equivalentSets) {
            cleanEquivalentSets.add(IntSets.immutable.of(set));
        }
        equivalentSets.clear();
        for (IntSet intSet : cleanEquivalentSets) {
            equivalentSets.add(intSet.toSortedArray());
        }
    }

    /**
     * Instantiates a new classifier results.
     *
     * @param classificationConceptSet the affected concepts
     * @param equivalentSets           the equivalent sets
     * @param viewCoordinateRecord
     */
    public ClassifierResults(Set<Integer> classificationConceptSet,
                             Set<ImmutableIntList> equivalentSets,
                             ViewCoordinateRecord viewCoordinateRecord) {
        this.classificationConceptSet.addAll(classificationConceptSet.stream().mapToInt(nid -> nid).toArray());
        this.equivalentSets = new HashSet<>();
        for (ImmutableIntList set : equivalentSets) {
            this.equivalentSets.add(set.toArray());
        }
        cleanUpEquivalentSets();
        this.viewCoordinate = viewCoordinateRecord;
        verifyCoordinates();
    }

    private final void verifyCoordinates() {
        if (viewCoordinate.stampCoordinate().stampPosition().time() == Long.MAX_VALUE) {
            throw new IllegalStateException("Filter position time must reflect the actual commit time, not 'latest' (Long.MAX_VALUE) ");
        }
    }

    /**
     * This constructor is only intended to be used when a classification wasn't performed, because there were cycles present.
     *
     * @param conceptsWithCycles
     * @param orphans
     * @param viewCoordinateRecord
     */
    public ClassifierResults(Map<Integer, Set<int[]>> conceptsWithCycles, Set<Integer> orphans,
                             ViewCoordinateRecord viewCoordinateRecord) {
        this.equivalentSets = new HashSet<>();
        conceptsWithCycles.forEach((key, value) -> {
            this.conceptsWithCycles.put(key, value);
        });
        this.orphanedConcepts.addAll(orphans.stream().mapToInt(nid -> nid).toArray());
        this.viewCoordinate = viewCoordinateRecord;
        verifyCoordinates();
    }

    @Decoder
    public static ClassifierResults decode(DecoderInput in) {
        return new ClassifierResults(in);
    }

    @Encoder
    public final void encode(EncoderOutput out) {
        out.writeNidArray(this.classificationConceptSet.toArray());
        out.writeInt(equivalentSets.size());
        for (int[] equivalentSet : equivalentSets) {
            out.writeNidArray(equivalentSet);
        }
        if (!conceptsWithCycles.isEmpty()) {
            out.writeBoolean(true);
            out.writeInt(conceptsWithCycles.size());
            conceptsWithCycles.forEachKeyValue(new IntObjectProcedure<Set<int[]>>() {
                @Override
                public void value(int conceptNid, Set<int[]> cycleNids) {
                    out.writeNid(conceptNid);
                    out.writeInt(cycleNids.size());
                    for (int[] cycle : cycleNids) {
                        out.writeNidArray(cycle);
                    }
                }
            });
        } else {
            out.writeBoolean(false);
        }
        out.writeNidArray(orphanedConcepts.toArray());
        this.viewCoordinate.encode(out);
    }

    @Override
    public String toString() {
        return "ClassifierResults{"
                + " affectedConcepts=" + this.classificationConceptSet.size() + ", equivalentSets="
                + this.equivalentSets.size() + ", Orphans detected=" + orphanedConcepts.size()
                + " Concepts with cycles=" + conceptsWithCycles.size() + '}';
    }

    public IntSet getClassificationConceptSet() {
        return this.classificationConceptSet;
    }

    public Set<int[]> getEquivalentSets() {
        return this.equivalentSets;
    }

    public MutableIntObjectMap<Set<int[]>> getCycles() {
        return conceptsWithCycles;
    }

    public void addOrphans(Set<Integer> orphans) {
        orphanedConcepts.addAll(orphans.stream().mapToInt(nid -> nid).toArray());
    }

    public ImmutableIntSet getOrphans() {
        return orphanedConcepts.toImmutable();
    }

    public ViewCoordinateRecord getViewCoordinate() {
        return viewCoordinate;
    }

    public Instant getCommitTime() {
        return this.viewCoordinate.stampCoordinate().stampPosition().instant();
    }

    public ImmutableIntSet getConceptsWithInferredChanges() {
        return conceptsWithInferredChanges.toImmutable();
    }
}
