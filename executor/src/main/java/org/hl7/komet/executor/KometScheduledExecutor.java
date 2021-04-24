package org.hl7.komet.executor;

import org.hl7.tinkar.common.service.TrackingCallable;

import java.util.concurrent.*;

public class KometScheduledExecutor extends ScheduledThreadPoolExecutor {
    public KometScheduledExecutor(int corePoolSize) {
        super(corePoolSize);
    }

    public KometScheduledExecutor(int corePoolSize, ThreadFactory threadFactory) {
        super(corePoolSize, threadFactory);
    }

    public KometScheduledExecutor(int corePoolSize, RejectedExecutionHandler handler) {
        super(corePoolSize, handler);
    }

    public KometScheduledExecutor(int corePoolSize, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, threadFactory, handler);
    }

    @Override
    protected <V> RunnableScheduledFuture<V> decorateTask(Runnable runnable, RunnableScheduledFuture<V> task) {
        if (runnable instanceof TrackingCallable) {

        }
        return super.decorateTask(runnable, task);
    }

    @Override
    protected <V> RunnableScheduledFuture<V> decorateTask(Callable<V> callable, RunnableScheduledFuture<V> task) {
        if (callable instanceof TrackingCallable) {

        }
        return super.decorateTask(callable, task);
    }
}
