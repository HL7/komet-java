package org.hl7.komet.framework;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.Scene;

import java.util.Optional;

/**
 * ExplorationNode: A node that enables traversal (a taxonomy view) of content
 * or search of content for the purpose of discovery.
 * A ExplorationNode provides an entry point to navigate to find details to look at, as opposed to a
 * DetailNode which displays the details selected via an exploration node.
 *
 * @author kec
 */

public interface ExplorationNode {
    /**
     * A title as might be used to provide a title in a tab for a PanelNode.
     *
     * @return the read-only property for the title.
     */
    ReadOnlyProperty<String> getTitle();

    /**
     * An optional node to use in addition the title. If the PanelNode wishes to
     * support drag and drop within a tab or other titled control, use this option.
     * The optional node may include the text of the current focused concept. If duplicate
     * display of the concept text may result, make the title property display an empty
     * string if the titleNode has been constructed.
     *
     * @return a Node to represent the title of this DetailNode.
     */
    Node getTitleNode();

    /**
     * Tool tip text to explain this node in more detail than a title would.
     *
     * @return the read-only property for the tool-tip text.
     */
    ReadOnlyProperty<String> getToolTip();

    /**
     *
     * @return the Manifold associated with this DetailNode.
     */
    ViewProperties getViewProperties();

    ActivityFeed getActivityFeed();

    SimpleObjectProperty<ActivityFeed> activityFeedProperty();

    /**
     * @return The node to be displayed
     */
    Node getNode();

    default Scene getScene() {
        return getNode().getScene();
    }

    /**
     * When called, the node should release all resources, as it is closed.
     */
    void close();

    /**
     * Indicate if a node should not close for any reason (uncommitted work, etc).
     */
    boolean canClose();

    void setNodeSelectionMethod(Runnable nodeSelectionMethod);

    /**
     * Save preferences for this node to the preferences provider.
     */
    void savePreferences();

    void revertPreferences();
}
