package org.hl7.komet.reasoner;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import org.eclipse.collections.api.list.primitive.ImmutableIntList;
import org.eclipse.collections.api.set.primitive.ImmutableIntSet;
import org.eclipse.collections.impl.factory.primitive.IntLists;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.tinkar.common.service.TrackingCallable;
import org.hl7.tinkar.common.util.text.NaturalOrder;

import java.util.concurrent.ConcurrentSkipListSet;

public class PrepareConceptSetTask extends TrackingCallable<Void> {


    private final ImmutableIntList affectedConceptList;
    private final ObservableList<Integer> affectedConceptsForDisplay;
    final ViewProperties viewProperties;

    public PrepareConceptSetTask(String title, ImmutableIntSet affectedConcepts,
                                 ObservableList<Integer> affectedConceptsForDisplay,
                                 final ViewProperties viewProperties) {
        this(title, IntLists.immutable.of(affectedConcepts.toArray()), affectedConceptsForDisplay, viewProperties);
    }
    public PrepareConceptSetTask(String title, ImmutableIntList affectedConcepts,
                                 ObservableList<Integer> affectedConceptsForDisplay,
                                 final ViewProperties viewProperties) {
        this.affectedConceptList = affectedConcepts;
        this.affectedConceptsForDisplay = affectedConceptsForDisplay;
        this.viewProperties = viewProperties;
        this.updateTitle(title);
        this.addToTotalWork(affectedConcepts.size());
        Platform.runLater(() -> {
            affectedConceptsForDisplay.setAll(affectedConcepts.toList().collect(i -> Integer.valueOf(i)));
        });
    }

    @Override
    protected Void compute() throws Exception {

            ConcurrentSkipListSet<Integer> concurrentSortedSet = new ConcurrentSkipListSet<>(this::compare);

            this.affectedConceptList.primitiveParallelStream().forEach(nid -> {
                concurrentSortedSet.add(nid);
                this.completedUnitOfWork();
            });
            Platform.runLater(() -> {
                this.affectedConceptsForDisplay.setAll(concurrentSortedSet);
            });
            return null;
    }

    private int compare(Integer o1, Integer o2) {

        return NaturalOrder.compareStrings(
                this.viewProperties.calculator().getFullyQualifiedDescriptionTextWithFallbackOrNid(o1),
                this.viewProperties.calculator().getFullyQualifiedDescriptionTextWithFallbackOrNid(o2));
    }
}
