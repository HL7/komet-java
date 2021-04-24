package org.hl7.komet.executor;

import javafx.application.Platform;
import javafx.concurrent.Task;
import org.hl7.tinkar.common.service.TrackingCallable;
import org.hl7.tinkar.common.util.thread.ThreadPoolExecutorFixed;

import java.util.concurrent.*;

public class KometThreadPoolExecutor extends ThreadPoolExecutorFixed {
    public KometThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public KometThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public KometThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public KometThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
        if (runnable instanceof TrackingCallable trackingCallable) {
            return new TaskWrapper(trackingCallable);
        }
        return super.newTaskFor(runnable, value);
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        if (callable instanceof TrackingCallable trackingCallable) {
            TaskWrapper<T> taskWrapper = new TaskWrapper(trackingCallable);
            Platform.runLater(() -> KometExecutor.executingTasks.add(taskWrapper));
            return taskWrapper;
        }
        return super.newTaskFor(callable);
    }
}
