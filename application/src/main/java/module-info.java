import org.hl7.komet.framework.KometNodeFactory;
import org.hl7.komet.framework.concurrent.TaskListsService;
import org.hl7.tinkar.common.service.DataServiceController;
import org.hl7.tinkar.common.service.DefaultDescriptionForNidService;
import org.hl7.tinkar.common.service.PublicIdService;
import org.hl7.tinkar.entity.EntityService;
import org.hl7.tinkar.entity.StampService;

module org.hl7.komet.application {

    exports org.hl7.komet.app to javafx.graphics;
    opens org.hl7.komet.app to javafx.fxml;

    // Not happy that I have to specify these here... Can't dynamically add modules?
    requires org.hl7.tinkar.provider.spinedarray;
    requires org.hl7.tinkar.provider.mvstore;
    requires org.hl7.tinkar.provider.ephemeral;
    // End not happy...

    requires javafx.controls;
    requires javafx.fxml;
    requires nsmenufx;
    requires org.controlsfx.controls;
    requires org.hl7.komet.classification;
    requires org.hl7.komet.details;
    requires org.hl7.komet.executor;
    requires org.hl7.komet.framework;
    requires org.hl7.komet.list;
    requires org.hl7.komet.navigator;
    requires org.hl7.komet.preferences;
    requires org.hl7.komet.progress;
    requires org.hl7.komet.search;
    requires org.hl7.tinkar.common;
    requires org.hl7.tinkar.entity;
    requires org.hl7.tinkar.provider.entity;
    requires org.hl7.tinkar.terms;
    requires org.kordamp.ikonli.javafx;

    uses DataServiceController;
    uses DefaultDescriptionForNidService;
    uses EntityService;
    uses KometNodeFactory;
    uses PublicIdService;
    uses StampService;
    uses TaskListsService;

    // For ScenicView...
    //requires org.scenicview.scenicview;
}