package org.hl7.komet.executor;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import org.hl7.tinkar.common.service.TrackingCallable;
import org.hl7.tinkar.common.service.TrackingListener;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.function.Consumer;

public class TaskWrapper<V> extends Task<V> implements TrackingListener<V> {
    private static int maxCompletedTaskListSize = 200;



    public static <V> TaskWrapper<V> make(TrackingCallable<V> trackingCallable) {
        return new TaskWrapper<>(trackingCallable);
    }

    public static <V> TaskWrapper<V> make(TrackingCallable<V> trackingCallable, Consumer<V> appThreadConsumer) {
        return new TaskWrapper<>(trackingCallable, appThreadConsumer);
    }

    private final TrackingCallable<V> trackingCallable;
    private final Consumer<V> appThreadConsumer;

    private TaskWrapper(TrackingCallable<V> trackingCallable) {
        this.trackingCallable = trackingCallable;
        this.appThreadConsumer = null;
        this.updateProgress(-1, -1);
        this.trackingCallable.addListener(this);
    }

    private TaskWrapper(TrackingCallable<V> trackingCallable, Consumer<V> appThreadConsumer) {
        this.trackingCallable = trackingCallable;
        this.appThreadConsumer = appThreadConsumer;
        this.updateProgress(-1, -1);
        this.trackingCallable.addListener(this);
    }

    @Override
    protected void scheduled() {
        KometExecutor.pendingTasks.add(this);
    }

    @Override
    protected void running() {
        KometExecutor.pendingTasks.remove(this);
        KometExecutor.executingTasks.add(this);
    }

    @Override
    protected void succeeded() {
        KometExecutor.executingTasks.remove(this);
        handleRetention();
    }
    @Override
    protected void failed() {
        KometExecutor.executingTasks.remove(this);
        handleRetention();
        Platform.runLater(() -> showExceptionDialog());
    }
    // TODO: align/reuse exception dialog in framework...
    public void showExceptionDialog() {
        getException().printStackTrace();/*w ww. j a  va2s.  c  o m*/

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception during " + getTitle());
        alert.setHeaderText(getException().getLocalizedMessage());
        //alert.setContentText("DevLaunch has thrown an exception.");

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        getException().printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
    }
    private void handleRetention() {
        if (this.trackingCallable.retainWhenComplete()) {
            KometExecutor.completedTasks.add(this);
            if (KometExecutor.completedTasks.size() > maxCompletedTaskListSize) {
                KometExecutor.completedTasks.remove(0, 25);
            }
        }
    }

    @Override
    protected V call() throws Exception {
        V result = trackingCallable.call();
        if (appThreadConsumer != null) {
            Platform.runLater(() -> appThreadConsumer.accept(result));
        }
        return result;
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
