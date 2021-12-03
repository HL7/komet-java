package org.hl7.komet.framework.preferences;


import javafx.scene.control.TreeItem;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.komet.preferences.KometPreferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

import static org.hl7.komet.framework.preferences.PreferenceGroup.Keys.CHILDREN_NODES;
import static org.hl7.komet.framework.preferences.PreferenceGroup.Keys.PROPERTY_SHEET_CLASS;

/**
 * @author kec
 */
public class PreferencesTreeItem extends TreeItem<PreferenceGroup> {
    private static final Logger LOG = LoggerFactory.getLogger(PreferencesTreeItem.class);
    ;
    final KometPreferencesController controller;
    KometPreferences preferences;

    private PreferencesTreeItem(PreferenceGroup value,
                                KometPreferences preferences, ViewProperties viewProperties, KometPreferencesController controller) {
        super(value);
        this.preferences = preferences;
        this.controller = controller;
        List<String> propertySheetChildren = preferences.getList(CHILDREN_NODES);
        for (String child : propertySheetChildren) {
            Optional<PreferencesTreeItem> childTreeItem = from(preferences.node(child), viewProperties, controller);
            if (childTreeItem.isPresent()) {
                getChildren().add(childTreeItem.get());
                childTreeItem.get().getValue().setTreeItem(childTreeItem.get());
            }
        }
    }

    static public Optional<PreferencesTreeItem> from(KometPreferences preferences,
                                                     ViewProperties viewProperties, KometPreferencesController controller) {
        Optional<String> optionalPropertySheetClass = preferences.get(PROPERTY_SHEET_CLASS);
        if (optionalPropertySheetClass.isPresent()) {
            try {
                String propertySheetClassName = optionalPropertySheetClass.get();

                Class preferencesSheetClass = Class.forName(propertySheetClassName);
                Constructor<PreferenceGroup> c = preferencesSheetClass.getConstructor(
                        KometPreferences.class,
                        ViewProperties.class,
                        KometPreferencesController.class);
                PreferenceGroup preferencesSheet = c.newInstance(preferences, viewProperties, controller);
                PreferencesTreeItem preferencesTreeItem = new PreferencesTreeItem(preferencesSheet, preferences,
                        viewProperties, controller);
                preferencesSheet.setTreeItem(preferencesTreeItem);
                return Optional.of(preferencesTreeItem);
            } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                PreferencesTreeItem.LOG.error("PropertySheetClass: " + optionalPropertySheetClass + " " + ex.getLocalizedMessage(), ex);
            }
        } else {
            preferences.put(PROPERTY_SHEET_CLASS, "org.hl7.komet.framework.preferences.RootPreferences");
            return from(preferences, viewProperties, controller);
        }
        return Optional.empty();
    }

    public KometPreferences getPreferences() {
        return preferences;
    }

    public void setPreferences(KometPreferences preferences) {
        this.preferences = preferences;
    }

    public KometPreferencesController getController() {
        return controller;
    }

    public void select() {
        this.controller.getPreferenceTree().getSelectionModel().select(this);
    }

    public void removeChild(String uuid) {
        PreferenceGroup.removeChild(preferences, uuid);
    }

    @Override
    public String toString() {
        return getValue().getGroupName();
    }
}
