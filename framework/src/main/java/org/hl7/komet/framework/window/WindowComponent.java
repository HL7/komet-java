package org.hl7.komet.framework.window;

import org.eclipse.collections.api.list.ImmutableList;
import org.hl7.komet.framework.view.ObservableViewNoOverride;
import org.hl7.komet.preferences.KometPreferences;

/**
 * WindowComponents must have a create method:
 * <br>public static WindowComponent create(ObservableViewNoOverride windowView, KometPreferences nodePreferences);
 * <br> so that they can be constructed with default values saved to preferences, and reconstructed from preferences.
 * Two scenarios:
 * <p>
 * 1. First creation of a WindowComponent
 * <p>Look for an absent INITIALIZED key, and then set defaults accordingly.
 * <p> 2. Restore a WindowComponent from its preferences.
 * <p>
 * If INITIALIZED key is present, read configuration from preferences and set fields accordingly.
 * </p>
 */
public interface WindowComponent {
    ObservableViewNoOverride windowView();

    KometPreferences nodePreferences();

    ImmutableList<WindowComponent> children();

    void saveConfiguration();

    enum Keys {
        INITIALIZED,
        FACTORY_CLASS
    }
}
