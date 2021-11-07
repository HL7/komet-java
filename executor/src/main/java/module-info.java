import org.hl7.komet.executor.AlertDialogSubscriber;
import org.hl7.komet.executor.KometExecutorController;
import org.hl7.komet.executor.TaskListsProvider;
import org.hl7.komet.framework.concurrent.TaskListsService;
import org.hl7.tinkar.common.alert.AlertReportingService;
import org.hl7.tinkar.common.service.CachingService;
import org.hl7.tinkar.common.service.ExecutorController;

module org.hl7.komet.executor {

    exports org.hl7.komet.executor;
    provides AlertReportingService with AlertDialogSubscriber;
    provides CachingService with KometExecutorController;
    provides ExecutorController with KometExecutorController;
    provides TaskListsService with TaskListsProvider;
    requires transitive javafx.base;
    requires transitive javafx.controls;
    requires transitive javafx.graphics;
    requires transitive org.hl7.komet.framework;
    requires transitive org.hl7.tinkar.common;
    uses TaskListsService;
}