package org.hl7.komet.framework;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import org.hl7.komet.framework.activity.ActivityStream;
import org.hl7.komet.framework.activity.ActivityStreamOption;
import org.hl7.komet.framework.alerts.AlertObject;
import org.hl7.komet.framework.alerts.AlertStream;
import org.hl7.komet.framework.alerts.AlertStreams;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.komet.preferences.KometPreferences;
import org.hl7.tinkar.common.id.PublicIdStringKey;

/**
 * ExplorationNode: A node that enables traversal (a taxonomy view) of content
 * or search of content for the purpose of discovery.
 * A ExplorationNode provides an entry point to navigate to find details to look at, as opposed to the
 * DetailNode subclass which displays the details selected via an exploration node.
 *
 * @author kec
 */

public interface KometNode {
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
    ReadOnlyProperty<String> toolTipTextProperty();

    Tooltip makeToolTip();

    /**
     * @return the Manifold associated with this DetailNode.
     */
    ViewProperties getViewProperties();

    ActivityStream getActivityStream();

    SimpleObjectProperty<PublicIdStringKey<ActivityStream>> activityStreamKeyProperty();

    SimpleObjectProperty<PublicIdStringKey<ActivityStreamOption>> optionForActivityStreamKeyProperty();

    default Scene getScene() {
        return getNode().getScene();
    }

    /**
     * @return The node to be displayed
     */
    Node getNode();

    ObjectProperty<Node> getMenuIconProperty();

    /**
     * When called, the node should release all resources, as it is closed.
     */
    void close();

    /**
     * Indicate if a node should not close for any reason (uncommitted work, etc).
     */
    default boolean canClose() {
        return false;
    }

    void setNodeSelectionMethod(Runnable nodeSelectionMethod);

    /**
     * Save preferences for this node to the preferences provider.
     */
    void savePreferences();

    void revertPreferences();

    default Node getTitleIconGraphic() {
        return getMenuIconGraphic();
    }

    Node getMenuIconGraphic();

    default void dispatchAlert(AlertObject alertObject) {
        PublicIdStringKey<AlertStream> streamKey = getNodePreferences().getObject(PreferenceKey.PARENT_ALERT_STREAM_KEY, AlertStreams.ROOT_ALERT_STREAM_KEY);
        AlertStreams.get(streamKey).dispatch(alertObject);
    }

    KometPreferences getNodePreferences();

    enum PreferenceKey {
        ACTIVITY_STREAM_KEY,
        ACTIVITY_STREAM_OPTION_KEY,
        PARENT_ALERT_STREAM_KEY
    }
}
