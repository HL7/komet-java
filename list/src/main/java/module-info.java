import org.hl7.komet.framework.NodeFactory;
import org.hl7.komet.list.ListNodeFactory;

module org.hl7.komet.list {

    requires transitive org.hl7.komet.framework;
    requires static org.hl7.tinkar.autoservice;

    opens org.hl7.komet.list;
    exports org.hl7.komet.list;
    
    provides NodeFactory
            with ListNodeFactory;

}