package org.hl7.komet.framework.preferences;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import org.hl7.komet.framework.propsheet.SheetItem;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.komet.preferences.KometPreferences;
import org.hl7.tinkar.terms.TinkarTerm;

import java.util.prefs.BackingStoreException;

import static org.hl7.komet.framework.preferences.PreferenceGroup.Keys.GROUP_NAME;

/**
 * @author kec
 */
public class ConfigurationPreferencePanel extends AbstractPreferences implements ConfigurationPreference {

    private final SimpleStringProperty nameProperty
            = new SimpleStringProperty(this, TinkarTerm.CONFIGURATION_NAME.toXmlFragment());
    private final BooleanProperty enableEdit = new SimpleBooleanProperty(this, TinkarTerm.ENABLE_EDITING.toXmlFragment());
    private final SimpleStringProperty datastoreLocationProperty
            = new SimpleStringProperty(this, TinkarTerm.DATASTORE_LOCATION.toXmlFragment());

    public ConfigurationPreferencePanel(KometPreferences preferencesNode, ViewProperties viewProperties,
                                        KometPreferencesController kpc) {
        super(preferencesNode, preferencesNode.get(GROUP_NAME, "KOMET"), viewProperties,
                kpc);
        this.nameProperty.set(groupNameProperty().get());
        this.enableEdit.setValue(preferencesNode.getBoolean(this.enableEdit.getName(), true));
        revertFields();
        save();
        getItemList().add(SheetItem.make(this.nameProperty));
        getItemList().add(SheetItem.make(this.enableEdit));
    }

    @Override
    protected void saveFields() throws BackingStoreException {
        getPreferencesNode().put(Keys.CONFIGURATION_NAME, this.nameProperty.get());
        getPreferencesNode().putBoolean(Keys.ENABLE_EDITING, this.enableEdit.get());
    }

    @Override
    final protected void revertFields() {
        this.nameProperty.set(getPreferencesNode().get(Keys.CONFIGURATION_NAME, getGroupName()));
        this.enableEdit.set(getPreferencesNode().getBoolean(Keys.ENABLE_EDITING, true));
    }

    public enum Keys {
        ENABLE_EDITING,
        CONFIGURATION_NAME,
    }


}
