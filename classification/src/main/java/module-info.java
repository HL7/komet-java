import org.hl7.komet.framework.KometNodeFactory;
import org.hl7.komet.reasoner.ReasonerResultsNodeFactory;

module org.hl7.komet.classification {
    requires transitive org.hl7.komet.framework;
    requires static org.hl7.tinkar.autoservice;
    requires org.hl7.tinkar.collection;
    requires org.jgrapht.core;

    opens org.hl7.komet.reasoner;
    exports org.hl7.komet.reasoner;
    exports org.hl7.komet.reasoner.expression;
    opens org.hl7.komet.reasoner.expression;

    provides KometNodeFactory
            with ReasonerResultsNodeFactory;

}