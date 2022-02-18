import org.hl7.komet.classification.ClassificationResultsNodeFactory;
import org.hl7.komet.framework.KometNodeFactory;

module org.hl7.komet.classification {
    requires transitive org.hl7.komet.framework;
    requires static org.hl7.tinkar.autoservice;
    requires org.hl7.tinkar.collection;

    opens org.hl7.komet.classification;
    exports org.hl7.komet.classification;

    provides KometNodeFactory
            with ClassificationResultsNodeFactory;

}