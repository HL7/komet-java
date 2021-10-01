import org.hl7.komet.executor.KometExecutorController;
import org.hl7.komet.executor.TaskLists;
import org.hl7.tinkar.common.service.CachingService;
import org.hl7.tinkar.common.service.ExecutorController;

module org.hl7.komet.executor {
    exports org.hl7.komet.executor;
    requires transitive org.hl7.tinkar.common;

    requires transitive javafx.base;
    requires transitive javafx.graphics;
    requires transitive javafx.controls;

    provides TaskLists
            with KometExecutorController;

    provides ExecutorController
            with KometExecutorController;

    provides CachingService
            with KometExecutorController;
}