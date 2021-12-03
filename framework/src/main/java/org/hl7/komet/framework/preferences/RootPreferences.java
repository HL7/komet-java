package org.hl7.komet.framework.preferences;

import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.komet.preferences.KometPreferences;

import java.util.List;
import java.util.prefs.BackingStoreException;

import static org.hl7.komet.framework.preferences.PreferenceGroup.Keys.CHILDREN_NODES;
import static org.hl7.komet.framework.preferences.PreferenceGroup.Keys.GROUP_NAME;

/**
 * @author kec
 */
public class RootPreferences extends AbstractPreferences {

    public RootPreferences(KometPreferences preferencesNode, ViewProperties viewProperties,
                           KometPreferencesController kpc) {
        super(preferencesNode, preferencesNode.get(GROUP_NAME, "Root"), viewProperties,
                kpc);
        if (!initialized()) {
            // Add children nodes and reflection classes for children
            addChild("Configuration", ConfigurationPreferencePanel.class);
            addChild("User", UserPreferencesPanel.class);
        }
        List<String> childPreferences = this.preferencesNode.getList(CHILDREN_NODES);
        this.preferencesNode.putList(CHILDREN_NODES, childPreferences);

        save();
    }

    @Override
    protected void saveFields() throws BackingStoreException {
        // No additional fields. Nothing to do.
    }

    @Override
    protected void revertFields() {
        // No additional fields. Nothing to do.
    }


}
