import org.hl7.komet.framework.KometNodeFactory;

module org.hl7.komet.tabs {
    requires transitive javafx.base;
    requires transitive javafx.controls;
    requires transitive javafx.graphics;
    requires transitive org.hl7.komet.framework;

    requires java.logging;

    exports org.hl7.komet.tabs;

    uses KometNodeFactory;

}