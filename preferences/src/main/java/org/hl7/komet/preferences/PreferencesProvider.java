package org.hl7.komet.preferences;

import org.hl7.tinkar.common.service.Executor;
import org.hl7.tinkar.common.service.SimpleIndeterminateTracker;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;


//~--- classes ----------------------------------------------------------------

/**
 * @author kec
 */
public class PreferencesProvider
        implements PreferencesService {

    private static Logger LOG = Logger.getLogger(KometPreferencesImpl.class.getName());

    public static final PreferencesProvider singleton = new PreferencesProvider();

    //~--- methods -------------------------------------------------------------


    /**
     * Start me.
     */
    public void start() {
        SimpleIndeterminateTracker progressTask = new SimpleIndeterminateTracker("Preference provider startup");
        Executor.threadPool().submit(progressTask);
        try {
            //Just doing this to make sure it starts without errors
            KometPreferencesImpl.getConfigurationRootPreferences();
        } catch (Throwable ex) {
            LOG.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        } finally {
            progressTask.finished();
        }
    }

    /**
     * Stop me.
     */
    public void stop() {
        SimpleIndeterminateTracker progressTask = new SimpleIndeterminateTracker("Preference provider save");
        Executor.threadPool().submit(progressTask);
        try {
            KometPreferencesImpl.getConfigurationRootPreferences().sync();
        } catch (Throwable ex) {
            LOG.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        } finally {
            progressTask.finished();
        }
    }

    //~--- get methods ---------------------------------------------------------

    @Override
    public void reloadConfigurationPreferences() {
        KometPreferencesImpl.reloadConfigurationPreferences();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KometPreferences getConfigurationPreferences() {
        return KometPreferencesImpl.getConfigurationRootPreferences();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KometPreferences getSystemPreferences() {
        return new PreferencesWrapper(Preferences.systemRoot());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KometPreferences getUserPreferences() {
        return new PreferencesWrapper(Preferences.userRoot());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearConfigurationPreferences() {
        try {
            getConfigurationPreferences().removeNode();
            getConfigurationPreferences().flush();
        } catch (BackingStoreException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearSystemPreferences() {
        try {
            Preferences.systemRoot().removeNode();
            Preferences.systemRoot().flush();
        } catch (BackingStoreException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearUserPreferences() {
        try {
            Preferences.userRoot().removeNode();
            Preferences.userRoot().flush();
        } catch (BackingStoreException e) {
            throw new RuntimeException(e);
        }
    }
}