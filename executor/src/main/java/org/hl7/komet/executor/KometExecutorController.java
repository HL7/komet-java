package org.hl7.komet.executor;

import com.google.auto.service.AutoService;
import org.hl7.tinkar.common.service.CachingService;
import org.hl7.tinkar.common.service.ExecutorController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicReference;

@AutoService({ExecutorController.class, CachingService.class})
public class KometExecutorController implements ExecutorController, CachingService {
    private static final Logger LOG = LoggerFactory.getLogger(KometExecutorController.class);
    private static AlertDialogSubscriber alertDialogSubscriber;
    private AtomicReference<KometExecutorProvider> providerReference = new AtomicReference<>();

    @Override
    public void reset() {
        stop();
    }

    @Override
    public KometExecutorProvider create() {
        if (providerReference.get() == null) {
            providerReference.updateAndGet(executorProvider -> {
                if (executorProvider != null) {
                    return executorProvider;
                }
                KometExecutorController.alertDialogSubscriber = new AlertDialogSubscriber();
                return new KometExecutorProvider();
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
