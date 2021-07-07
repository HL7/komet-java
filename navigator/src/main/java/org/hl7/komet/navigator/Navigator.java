package org.hl7.komet.navigator;

import org.eclipse.collections.api.collection.ImmutableCollection;
import org.hl7.tinkar.coordinate.view.calculator.ViewCalculator;

public interface Navigator {

    /**
     * Gets the parent nids.
     *
     * @param childNid the child id
     * @return the taxonomy parent nids
     */
    int[] getParentNids(int childNid);

    /**
     * Gets the child nids.
     *
     * @param parentNid the parent id
     * @return the child nids
     */
    int[] getChildNids(int parentNid);

    /**
     * For circumstances where there is more than one type of navigable relationship.
     * @param childNid
     * @return an ImmutableCollection of all the parent Edges.
     */
    ImmutableCollection<Edge> getParentLinks(int childNid);

    /**
     * For circumstances where there is more than one type of navigable relationship.
     * @param parentNid
     * @return an ImmutableCollection of all the child Edges.
     */
    ImmutableCollection<Edge> getChildLinks(int parentNid);

    /**
     *
     * @param conceptNid concept to test if it is a leaf node
     * @return true if the node is a leaf (it has no children)
     */
    boolean isLeaf(int conceptNid);

    /**
     * Checks if child of.
     *
     * @param childNid the child id
     * @param parentNid the parent id
     * @return true, if child of
     */
    boolean isChildOf(int childNid, int parentNid);

    /**
     * Checks if descendant  of.
     *
     * @param descendantNid the descendant id
     * @param ancestorNid the parent id
     * @return true, if kind of
     */
    boolean isDescendentOf(int descendantNid, int ancestorNid);

    /**
     * Gets the roots.
     *
     * @return the root concept nids
     */
    int[] getRootNids();

    /**
     * Get the ViewCalculator which defines the parent/child relationships of this tree.
     * @return ViewCalculator
     */
    ViewCalculator getViewCalculator();

}
