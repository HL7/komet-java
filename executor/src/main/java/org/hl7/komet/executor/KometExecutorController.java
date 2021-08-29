package org.hl7.komet.executor;

import com.google.auto.service.AutoService;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import org.hl7.tinkar.common.service.CachingService;
import org.hl7.tinkar.common.service.ExecutorController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicReference;

@AutoService({ExecutorController.class, CachingService.class, TaskLists.class})
public class KometExecutorController implements ExecutorController, CachingService, TaskLists {
    private static final Logger LOG = LoggerFactory.getLogger(KometExecutorController.class);
    private AtomicReference<KometExecutor> providerReference = new AtomicReference<>();

    @Override
    public ObservableList<Task<?>> pendingTasks() {
        if (providerReference.get() == null) {
            return KometExecutor.pendingTasks;
        }
        return null;
    }

    @Override
    public ObservableList<Task<?>> executingTasks() {
        if (providerReference.get() == null) {
            return KometExecutor.executingTasks;
        }
        return null;
    }

    @Override
    public ObservableList<Task<?>> completedTasks() {
        if (providerReference.get() == null) {
            return KometExecutor.completedTasks;
        }
        return null;
    }

    @Override
    public void reset() {
        stop();
    }

    @Override
    public KometExecutor create() {
        if (providerReference.get() == null) {
            providerReference.updateAndGet(executorProvider -> {
                if (executorProvider != null) {
                    return executorProvider;
                }
                return new KometExecutor();
            });
            providerReference.get().start();
        }
        return providerReference.get();
    }

    @Override
    public void stop() {
        providerReference.updateAndGet(executorProvider -> {
            if (executorProvider != null) {
                executorProvider.stop();
            }
            return null;
        });
    }
}
