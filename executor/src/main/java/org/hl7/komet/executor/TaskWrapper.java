package org.hl7.komet.executor;

import javafx.concurrent.Task;
import org.hl7.tinkar.common.service.TrackingCallable;
import org.hl7.tinkar.common.service.TrackingListener;

public class TaskWrapper<V> extends Task<V> implements TrackingListener<V> {
    private final TrackingCallable<V> trackingCallable;
    public TaskWrapper(TrackingCallable<V> trackingCallable) {
        this.trackingCallable = trackingCallable;
        this.trackingCallable.addListener(this);
    }

    @Override
    protected V call() throws Exception {
        return trackingCallable.call();
    }

    @Override
    public void updateProgress(double workDone, double max) {
        super.updateProgress(workDone, max);
    }

    @Override
    public void updateMessage(String message) {
        super.updateMessage(message);
    }

    @Override
    public void updateTitle(String title) {
        super.updateTitle(title);
    }

    @Override
    public void updateValue(V result) {
        super.updateValue(result);
    }

    @Override
    protected void cancelled() {
        this.trackingCallable.cancel();
    }
}
