package org.hl7.komet.navigator;

/**
 * The type and destination parts of a relationship displayed in a tree.
 * @author kec
 */
public interface Edge {
    /**
     *
     * @return the concept nid for the type of the linkage to the destination
     */
    int getTypeNid();

    /**
     *
     * @return the destination concept nid.
     */
    int getDestinationNid();
}
