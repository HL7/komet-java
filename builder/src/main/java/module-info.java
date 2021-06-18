import org.hl7.komet.builder.ConceptBuilderNodeFactory;
import org.hl7.komet.framework.NodeFactory;

module org.hl7.komet.builder {
    requires static org.hl7.tinkar.autoservice;
    requires transitive org.hl7.komet.framework;
    requires org.hl7.komet.view;

    opens org.hl7.komet.builder;
    exports org.hl7.komet.builder;
    
    provides NodeFactory
            with ConceptBuilderNodeFactory;

}