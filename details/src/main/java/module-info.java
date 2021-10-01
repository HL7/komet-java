import org.hl7.komet.details.DetailsNodeFactory;
import org.hl7.komet.details.concept.ConceptDetaisNodeFactory;
import org.hl7.komet.framework.KometNodeFactory;

module org.hl7.komet.details {

    requires static org.hl7.tinkar.autoservice;
    requires transitive org.hl7.komet.framework;

    opens org.hl7.komet.details;
    exports org.hl7.komet.details;
    exports org.hl7.komet.details.concept;

    provides KometNodeFactory
            with DetailsNodeFactory, ConceptDetaisNodeFactory;

}