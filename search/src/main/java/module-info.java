import org.hl7.komet.framework.KometNodeFactory;
import org.hl7.komet.search.SearchNodeFactory;

module org.hl7.komet.search {

    requires transitive org.hl7.komet.framework;
    requires static org.hl7.tinkar.autoservice;

    opens org.hl7.komet.search;
    exports org.hl7.komet.search;
    
    provides KometNodeFactory
            with SearchNodeFactory;

}