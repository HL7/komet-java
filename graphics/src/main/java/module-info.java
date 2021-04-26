module org.hl7.komet.graphics {
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires transitive org.kordamp.ikonli.javafx;

    requires transitive org.kordamp.ikonli.foundation;
    requires transitive org.kordamp.ikonli.fontawesome;
    requires transitive org.kordamp.ikonli.icomoon;
    requires transitive org.kordamp.ikonli.ionicons4;
    requires transitive org.kordamp.ikonli.mapicons;
    requires transitive org.kordamp.ikonli.material2;
    requires transitive org.kordamp.ikonli.materialdesign2;
    requires transitive org.kordamp.ikonli.materialdesign;
    requires transitive org.kordamp.ikonli.runestroicons;
    requires transitive org.kordamp.ikonli.unicons;

    opens org.hl7.komet.graphics;

    exports org.hl7.komet.graphics;
}