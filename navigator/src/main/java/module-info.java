import org.hl7.komet.framework.KometNodeFactory;
import org.hl7.komet.navigator.graph.GraphNavigatorNodeFactory;
import org.hl7.komet.navigator.pattern.PatternNavigatorFactory;

module org.hl7.komet.navigator {

    requires transitive org.hl7.komet.framework;
    requires static org.hl7.tinkar.autoservice;
    requires org.hl7.komet.executor;

    opens org.hl7.komet.navigator.graph;
    opens org.hl7.komet.navigator.pattern;
    exports org.hl7.komet.navigator.pattern;
    exports org.hl7.komet.navigator.graph;

    provides KometNodeFactory
            with GraphNavigatorNodeFactory,
                    PatternNavigatorFactory;

}