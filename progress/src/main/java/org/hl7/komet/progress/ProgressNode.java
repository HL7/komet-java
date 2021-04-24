package org.hl7.komet.progress;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.scene.Node;
import org.controlsfx.control.TaskProgressView;
import org.hl7.komet.framework.ActivityFeed;
import org.hl7.komet.framework.ExplorationNode;
import org.hl7.komet.framework.TaskLists;
import org.hl7.komet.framework.ViewProperties;

import java.util.Optional;
import java.util.ServiceLoader;

public class ProgressNode implements ExplorationNode {

    TaskProgressView<Task<?>> progressView = new TaskProgressView<>();
    Optional<TaskLists> optionalTaskLists = ServiceLoader.load(TaskLists.class).findFirst();
    {
        progressView.setRetainTasks(false);
        optionalTaskLists.ifPresent(taskLists -> {
            Bindings.bindContent(progressView.getTasks(), taskLists.executingTasks());
        });
    }

    @Override
    public ReadOnlyProperty<String> getTitle() {
        return null;
    }

    @Override
    public Optional<Node> getTitleNode() {
        return Optional.empty();
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
    public ActivityFeed getActivityFeed() {
        return null;
    }

    @Override
    public SimpleObjectProperty<ActivityFeed> activityFeedProperty() {
        return null;
    }

    @Override
    public Node getNode() {
        return null;
    }

    @Override
    public ObjectProperty<Node> getMenuIconProperty() {
        return null;
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

    @Override
    public Node getMenuIconGraphic() {
        return null;
    }
}
