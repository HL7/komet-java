package org.hl7.komet.preferences;

public interface PreferencesService {
    /**
     * Reload the configuration preferences from disk. For example, used when git fetches updates to the
     * configuration preferences, and the preferences should be reloaded to to reflect the newly downloaded preferences.
     */
    void reloadConfigurationPreferences();
    /**
     * @return a database specific preferences store which is stored within the data store folder.
     */
    KometPreferences getConfigurationPreferences();

    /**
     * @return a preference store that is stored globally within an OS store, for any user that runs the application
     * on this specific computer.
     */
    KometPreferences getSystemPreferences();

    /**
     * @return a preference store that is stored within a user profile folder of the current OS user, so it will apply
     * to any database a user opens on this computer.
     */
    KometPreferences getUserPreferences();

    /**
     * Remove any and all application preferences stored data store folder
     * The behavior of any handles to the system preferences that are still held after this operation is undefined.
     */
    void clearConfigurationPreferences();

    /**
     * Remove any and all preferences stored in the system store
     * The behavior of any handles to the system preferences that are still held after this operation is undefined.
     */
    void clearSystemPreferences();

    /**
     * Remove any and all preferences stored in the system user store
     * The behavior of any handles to the user preferences that are still held after this operation is undefined.
     */
    void clearUserPreferences();
}
