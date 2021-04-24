import org.hl7.komet.executor.KometExecutorController;
import org.hl7.tinkar.common.service.CachingService;
import org.hl7.tinkar.common.service.ExecutorController;
import org.hl7.komet.framework.TaskLists;

module org.hl7.komet.executor {
    requires transitive org.hl7.tinkar.common;

    requires javafx.graphics;
    requires org.hl7.komet.framework;

    provides TaskLists
            with KometExecutorController;

    provides ExecutorController
            with KometExecutorController;

    provides CachingService
            with KometExecutorController;
}