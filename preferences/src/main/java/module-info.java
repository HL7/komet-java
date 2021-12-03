import org.hl7.komet.preferences.PreferencesService;
import org.hl7.komet.preferences.PreferencesServiceFactory;

module org.hl7.komet.preferences {
    exports org.hl7.komet.preferences;
    requires java.prefs;
    requires org.hl7.tinkar.entity;
    requires java.xml;
    provides PreferencesService with PreferencesServiceFactory;
    uses PreferencesService;
}