import org.hl7.komet.framework.NodeFactory;
import org.hl7.komet.executor.TaskLists;
import org.hl7.tinkar.common.service.DataServiceController;

module org.hl7.komet.application {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires org.hl7.komet.details;
    requires org.hl7.komet.framework;
    requires org.hl7.komet.graphics;
    requires org.hl7.komet.navigator;
    requires org.hl7.komet.progress;
    requires org.hl7.komet.search;
    requires org.hl7.komet.tabs;
    requires org.hl7.tinkar.common;
    requires org.hl7.tinkar.entity;
    requires org.hl7.tinkar.terms;
    requires org.kordamp.ikonli.javafx;
    requires org.hl7.komet.view;
    requires org.hl7.komet.executor;

    uses DataServiceController;
    uses TaskLists;
    uses NodeFactory;

    opens sh.komet.app to javafx.fxml;

    exports sh.komet.app to javafx.graphics;
    exports sh.komet.app.propsheet.editor to org.controlsfx.controls;
}