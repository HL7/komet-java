package org.hl7.komet.progress;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.scene.Node;
import org.controlsfx.control.TaskProgressView;
import org.hl7.komet.framework.ActivityStream;
import org.hl7.komet.framework.ExplorationNode;
import org.hl7.komet.executor.TaskLists;
import org.hl7.komet.view.ViewProperties;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Optional;
import java.util.ServiceLoader;

public class CompletionNode implements ExplorationNode {

    SimpleStringProperty title = new SimpleStringProperty("Completions");
    final Node activityGraphic = getTitleGraphic();

    TaskProgressView<Task<?>> progressView = new TaskProgressView<>();
    Optional<TaskLists> optionalTaskLists = ServiceLoader.load(TaskLists.class).findFirst();
    {
        progressView.setRetainTasks(true);
        optionalTaskLists.ifPresent(taskLists -> {
            CompletionViewSkin skin = new CompletionViewSkin<>(progressView, taskLists.completedTasks());
            progressView.setSkin(skin);
            Bindings.bindContent(progressView.getTasks(), taskLists.completedTasks());
        });
    }

    private static Node getTitleGraphic() {
        FontIcon icon = new FontIcon();
        icon.setIconLiteral("mdi2f-flag-checkered:16:white");
        icon.setId("completion-node");
        return icon;
    }
    @Override
    public ReadOnlyProperty<String> getTitle() {
        return title;
    }

    public void removeTask(Task<?> task) {
        Platform.runLater(() -> optionalTaskLists.get().completedTasks().remove(task));
    }


    @Override
    public Node getTitleNode() {
        return activityGraphic;
    }

    @Override
    public ReadOnlyProperty<String> getToolTip() {
        return null;
    }

    @Override
    public ViewProperties getViewProperties() {
        return null;
    }

    @Override
    public ActivityStream getActivityFeed() {
        return null;
    }

    @Override
    public SimpleObjectProperty<ActivityStream> activityFeedProperty() {
        return null;
    }

    @Override
    public Node getNode() {
        return progressView;
    }

    @Override
    public void close() {

    }

    @Override
    public boolean canClose() {
        return false;
    }

    @Override
    public void setNodeSelectionMethod(Runnable nodeSelectionMethod) {

    }

    @Override
    public void savePreferences() {

    }

    @Override
    public void revertPreferences() {

    }
}
