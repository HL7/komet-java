module org.hl7.komet.framework {

    exports org.hl7.komet.framework.activity;
    exports org.hl7.komet.framework.alerts;
    exports org.hl7.komet.framework.controls;
    exports org.hl7.komet.framework.dnd;
    exports org.hl7.komet.framework.graphics;
    exports org.hl7.komet.framework.uncertain;
    exports org.hl7.komet.framework.view;
    exports org.hl7.komet.framework;

    requires org.kordamp.ikonli.fontawesome5;
    requires org.kordamp.ikonli.fontawesome;
    requires org.kordamp.ikonli.foundation;
    requires org.kordamp.ikonli.icomoon;
    requires org.kordamp.ikonli.ionicons4;
    requires org.kordamp.ikonli.mapicons;
    requires org.kordamp.ikonli.material2;
    requires org.kordamp.ikonli.materialdesign2;
    requires org.kordamp.ikonli.materialdesign;
    requires org.kordamp.ikonli.octicons;
    requires org.kordamp.ikonli.runestroicons;
    requires org.kordamp.ikonli.unicons;
    requires org.reactivestreams;
    requires transitive javafx.base;
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires transitive javafx.graphics;
    requires transitive org.controlsfx.controls;
    requires transitive org.eclipse.jetty.logging;
    requires transitive org.hl7.komet.executor;
    requires transitive org.hl7.komet.preferences;
    requires transitive org.hl7.komet.terms;
    requires transitive org.hl7.tinkar.common;
    requires transitive org.hl7.tinkar.coordinate;
    requires transitive org.hl7.tinkar.dto;
    requires transitive org.hl7.tinkar.eclipse.collections;
    requires transitive org.hl7.tinkar.entity;
    requires transitive org.hl7.tinkar.terms;
    requires transitive org.kordamp.ikonli.javafx;
    requires transitive org.slf4j;

    opens org.hl7.komet.framework.graphics;
    exports org.hl7.komet.framework.temp;
    exports org.hl7.komet.framework.context;
    exports org.hl7.komet.framework.panel;
    exports org.hl7.komet.framework.propsheet;
    exports org.hl7.komet.framework.propsheet.editor to org.controlsfx.controls;
    exports org.hl7.komet.framework.docbook;

}