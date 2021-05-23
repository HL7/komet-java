module org.hl7.komet.framework {
    requires transitive javafx.graphics;
    requires transitive org.kordamp.ikonli.javafx;
    requires transitive javafx.controls;
    requires transitive org.hl7.tinkar.common;
    requires transitive org.hl7.tinkar.entity;
    requires transitive org.hl7.komet.terms;

    exports org.hl7.komet.framework;
}