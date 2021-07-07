import org.hl7.komet.framework.KometNodeFactory;
import org.hl7.komet.navigator.NavigatorNodeFactory;

module org.hl7.komet.navigator {

    requires transitive org.hl7.komet.framework;
    requires static org.hl7.tinkar.autoservice;
    requires org.hl7.komet.executor;

    opens org.hl7.komet.navigator;
    exports org.hl7.komet.navigator;
    
    provides KometNodeFactory
            with NavigatorNodeFactory;

}