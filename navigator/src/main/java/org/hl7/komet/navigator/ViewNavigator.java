package org.hl7.komet.navigator;

import org.eclipse.collections.api.collection.ImmutableCollection;
import org.hl7.tinkar.coordinate.view.ViewCoordinate;
import org.hl7.tinkar.coordinate.view.calculator.ViewCalculator;
import org.hl7.tinkar.coordinate.view.calculator.ViewCalculatorWithCache;
import org.hl7.tinkar.terms.TinkarTerm;

public class ViewNavigator implements Navigator {

    private final ViewCalculator viewCalculator;
    public ViewNavigator(ViewCoordinate viewCoordinate) {
        viewCalculator = ViewCalculatorWithCache.getCalculator(viewCoordinate.toViewCoordinateRecord());
    }

    @Override
    public int[] getParentNids(int childNid) {
        return viewCalculator.sortedParentsOf(childNid).toArray();
    }

    @Override
    public int[] getChildNids(int parentNid) {
        return viewCalculator.sortedChildrenOf(parentNid).toArray();
    }

    @Override
    public ImmutableCollection<Edge> getParentLinks(int childNid) {
        //TODO: Create edges with type nid coming from navitation pattern...
        return null;
    }

    @Override
    public ImmutableCollection<Edge> getChildLinks(int parentNid) {
        //TODO: Create edges with type nid coming from navitation pattern...
        return null;
    }

    @Override
    public boolean isLeaf(int conceptNid) {
        return viewCalculator.unsortedChildrenOf(conceptNid).isEmpty();
    }

    @Override
    public boolean isChildOf(int childNid, int parentNid) {
        return viewCalculator.unsortedChildrenOf(parentNid).contains(childNid);
    }

    @Override
    public boolean isDescendentOf(int descendantNid, int ancestorNid) {
        return viewCalculator.unsortedDescendentsOf(ancestorNid).contains(descendantNid);
    }

    @Override
    public int[] getRootNids() {
        return new int[] {TinkarTerm.ROOT_VERTEX.nid()};
    }

    @Override
    public ViewCalculator getViewCalculator() {
        return viewCalculator;
    }
}
