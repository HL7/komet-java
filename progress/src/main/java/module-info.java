import org.hl7.komet.framework.KometNodeFactory;
import org.hl7.komet.executor.TaskLists;
import org.hl7.komet.progress.CompletionNodeFactory;
import org.hl7.komet.progress.ProgressNodeFactory;

module org.hl7.komet.progress {
    exports org.hl7.komet.progress;
    requires static org.hl7.tinkar.autoservice;
    requires org.hl7.komet.framework;
    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.core;
    requires org.hl7.komet.executor;
    requires org.hl7.komet.preferences;
    requires org.kordamp.ikonli.javafx;

    provides KometNodeFactory
            with ProgressNodeFactory, CompletionNodeFactory;

    uses TaskLists;
}