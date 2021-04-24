import org.hl7.komet.framework.NodeFactory;

module org.hl7.komet.tabs {
    requires transitive javafx.base;
    requires transitive javafx.controls;
    requires transitive javafx.graphics;
    requires transitive org.hl7.komet.framework;
    requires transitive org.hl7.komet.graphics;

    requires java.logging;

    exports org.hl7.komet.tabs;

    uses NodeFactory;

}