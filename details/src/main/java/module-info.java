import org.hl7.komet.framework.KometNodeFactory;
import org.hl7.komet.details.DetailsNodeFactory;

module org.hl7.komet.details {

    requires static org.hl7.tinkar.autoservice;
    requires transitive org.hl7.komet.framework;

    opens org.hl7.komet.details;
    exports org.hl7.komet.details;
    
    provides KometNodeFactory
            with DetailsNodeFactory;

}