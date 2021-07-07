package org.hl7.komet.preferences;

import org.hl7.tinkar.common.service.ServiceKeys;
import org.hl7.tinkar.common.service.ServiceProperties;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.AbstractPreferences;
import java.util.prefs.BackingStoreException;

//~--- classes ----------------------------------------------------------------

/**
 * @author kec
 */
public class KometPreferencesImpl
        extends AbstractPreferences  {
    private static Logger LOG = Logger.getLogger(KometPreferencesImpl.class.getName());

    public static final String DB_PREFERENCES_FOLDER = "preferences";

    public static final KometPreferencesImpl preferencesImpl = new KometPreferencesImpl();
    public static final KometPreferencesWrapper preferencesWrapper = new KometPreferencesWrapper(preferencesImpl);

    //~--- fields --------------------------------------------------------------

    private final ConcurrentSkipListMap<String, String> preferencesTree = new ConcurrentSkipListMap<>();
    private final File directory;
    private final File preferencesFile;
    private final File temporaryFile;

    //~--- constructors --------------------------------------------------------

    private KometPreferencesImpl() {
        //For HK2
        super(null, "");
        File configuredRoot = ServiceProperties.get(ServiceKeys.DATA_STORE_ROOT, new File("target/IsaacPreferencesDefault"));
        this.directory = new File(configuredRoot, "preferences");
        this.preferencesFile = new File(this.directory, "preferences.xml");
        this.temporaryFile = new File(this.directory, "preferences-tmp.xml");
        init();
    }

    //We only enforce singleton for the root preferences.  Its up to the AbstractPreferences to keep track of
    //child references properly.
    private KometPreferencesImpl(KometPreferencesImpl parent, String name) {
        super(parent, name);

        if (!isValidPath(name)) {
            throw new IllegalStateException("Name is not a valid file name or path: " + name);
        }

        this.directory = new File(parent.directory, name);
        this.preferencesFile = new File(this.directory, "preferences.xml");
        this.temporaryFile = new File(this.directory, "preferences-tmp.xml");
        init();
    }

    @Override
    public String toString() {
        return "Configuration Preference Node: " + this.absolutePath();
    }

    /**
     * The public mechanism to get a handle to a preferences store that stores its data inside the datastore folder.
     *
     * @return This class, wrapped by a {@link KometPreferencesWrapper}
     */
    public static KometPreferences getConfigurationRootPreferences() {
        return preferencesWrapper;
    }

    public static void reloadConfigurationPreferences() {
        recursiveInit(preferencesImpl);
    }

    private static void recursiveInit(KometPreferencesImpl preferences) {
        preferences.init();
        for (AbstractPreferences childPreferences : preferences.cachedChildren()) {
            recursiveInit((KometPreferencesImpl) childPreferences);
        }
    }
    //~--- methods -------------------------------------------------------------

    private void init() {
        preferencesTree.clear();
        if (preferencesFile.exists()) {
            try (FileInputStream fis = new FileInputStream(preferencesFile)) {
                importMap(fis, preferencesTree);
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            }
        }
    }

    static void exportMap(OutputStream os, Map<String, String> map)
            throws Exception {
        XmlForKometPreferences.exportMap(os, map);
    }

    static void importMap(InputStream is, Map<String, String> map)
            throws Exception {
        XmlForKometPreferences.importMap(is, map);
    }

    @Override
    protected KometPreferencesImpl childSpi(String name) {
        return new KometPreferencesImpl(this, name);
    }

    @Override
    public boolean isRemoved() {
        return super.isRemoved();
    }

    public Object getLock() {
        return lock;
    }

    @Override
    protected String[] childrenNamesSpi()
            throws BackingStoreException {
        List<String> result = new ArrayList<>();
        File[] dirContents = directory.listFiles();

        if (dirContents != null) {
            for (File dirContent : dirContents) {
                if (dirContent.isDirectory()) {
                    result.add(dirContent.getName());
                }
            }
        }

        return result.toArray(new String[result.size()]);
    }

    @Override
    protected void flushSpi()
            throws BackingStoreException {
        // nothing to do per the FileSystemPreferences implementation.
    }

    @Override
    protected String[] keysSpi()
            throws BackingStoreException {
        return preferencesTree.keySet()
                .toArray(new String[preferencesTree.size()]);
    }

    @Override
    protected void putSpi(String key, String value) {
        preferencesTree.put(key, value);
    }

    @Override
    protected void removeNodeSpi()
            throws BackingStoreException {
        if (this.preferencesFile.exists()) {
            this.preferencesFile.delete();
        }

        if (this.temporaryFile.exists()) {
            this.temporaryFile.delete();
        }

        File[] extras = directory.listFiles();

        if (extras != null && extras.length != 0) {
            LOG.warning("Found extraneous files when removing node: " + Arrays.asList(extras));

            for (File extra : extras) {
                extra.delete();
            }
        }

        if (!directory.delete()) {
            throw new BackingStoreException("Couldn't delete: " + directory);
        }
    }

    @Override
    protected void removeSpi(String key) {
        preferencesTree.remove(key);
    }

    @Override
    protected void syncSpi()
            throws BackingStoreException {
        try {
            if (!directory.exists() && !directory.mkdirs()) {
                throw new BackingStoreException(directory + " create failed.");
            }

            try (FileOutputStream fos = new FileOutputStream(temporaryFile)) {
                exportMap(fos, preferencesTree);
            }
            Files.move(temporaryFile.toPath(), preferencesFile.toPath(), REPLACE_EXISTING);
        } catch (Exception e) {
            if (e instanceof BackingStoreException) {
                throw (BackingStoreException) e;
            }

            throw new BackingStoreException(e);
        }
    }

    //~--- get methods ---------------------------------------------------------

    @Override
    protected String getSpi(String key) {
        return preferencesTree.get(key);
    }

    public static boolean isValidPath(String path) {
        try {
            Paths.get(path);
        } catch (InvalidPathException | NullPointerException ex) {
            return false;
        }
        return true;
    }
}