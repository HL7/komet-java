import org.hl7.komet.framework.NodeFactory;
import org.hl7.komet.search.SearchNodeFactory;

module org.hl7.komet.search {

    requires transitive org.hl7.komet.framework;
    requires static org.hl7.tinkar.autoservice;
    requires org.hl7.komet.view;

    opens org.hl7.komet.search;
    exports org.hl7.komet.search;
    
    provides NodeFactory
            with SearchNodeFactory;

}