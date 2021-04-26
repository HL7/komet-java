import org.hl7.komet.classification.ClassificationResultsNodeFactory;
import org.hl7.komet.framework.NodeFactory;

module org.hl7.komet.classification {

    requires transitive org.hl7.komet.framework;
    requires static org.hl7.tinkar.autoservice;

    opens org.hl7.komet.classification;
    exports org.hl7.komet.classification;
    
    provides NodeFactory
            with ClassificationResultsNodeFactory;

}