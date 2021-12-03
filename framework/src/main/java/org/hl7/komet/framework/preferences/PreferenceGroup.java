package org.hl7.komet.framework.preferences;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Node;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.komet.preferences.KometPreferences;

import java.util.List;

/**
 * @author kec
 */
public interface PreferenceGroup {
    public static void removeChild(KometPreferences preferences, String uuidStr) {
        List<String> propertySheetChildren = preferences.getList(Keys.CHILDREN_NODES);
        propertySheetChildren.remove(uuidStr);
        preferences.putList(Keys.CHILDREN_NODES, propertySheetChildren);
    }

    /**
     * @param viewProperties
     * @return property sheet for editing properties in this group.
     */
    Node getCenterPanel(ViewProperties viewProperties);

    /**
     * @param viewProperties
     * @return possibly null panel
     */
    Node getRightPanel(ViewProperties viewProperties);

    /**
     * @param viewProperties
     * @return possibly null panel
     */
    Node getTopPanel(ViewProperties viewProperties);

    /**
     * @param viewProperties
     * @return possibly null panel
     */
    Node getBottomPanel(ViewProperties viewProperties);

    /**
     * @param viewProperties
     * @return possibly null panel
     */
    Node getLeftPanel(ViewProperties viewProperties);

    /**
     * @return name for this group. Will be used in tree view navigation of
     * preferences.
     */
    String getGroupName();

    SimpleStringProperty groupNameProperty();

    /**
     * Save preferences in group to preferences store
     */
    void save();

    /**
     * Revert any changed preferences to values currently in preferences store
     */
    void revert();

    /**
     * @return True of this PreferenceGroup is previously initialized, and was
     * read from preferences. False if this PreferenceGroup is to be newly created
     * with default values.
     */
    boolean initialized();

    PreferencesTreeItem getTreeItem();

    void setTreeItem(PreferencesTreeItem item);


    enum Keys {
        INITIALIZED,
        GROUP_NAME,
        PROPERTY_SHEET_CLASS,
        CHILDREN_NODES
    }

}
