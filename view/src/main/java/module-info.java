module org.hl7.komet.view {
    requires transitive javafx.base;
    requires transitive org.hl7.tinkar.coordinate;

    requires java.logging;
    requires org.hl7.tinkar.eclipse.collections;
    requires org.hl7.tinkar.terms;
    requires org.hl7.tinkar.common;
    requires org.hl7.komet.terms;

    exports org.hl7.komet.view;
    exports org.hl7.komet.view.uncertain;

}