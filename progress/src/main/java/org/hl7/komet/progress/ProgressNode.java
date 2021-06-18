package org.hl7.komet.progress;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import org.controlsfx.control.TaskProgressView;
import org.hl7.komet.executor.TaskLists;
import org.hl7.komet.framework.ActivityStream;
import org.hl7.komet.framework.ExplorationNode;
import org.hl7.komet.view.ViewProperties;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Optional;
import java.util.ServiceLoader;

public class ProgressNode implements ExplorationNode {

    SimpleStringProperty title = new SimpleStringProperty("Activity");
    final Node activityGraphic = getTitleGraphic();
    final RotateTransition rotation = new RotateTransition(Duration.seconds(1.5), activityGraphic);
    {
        rotation.setCycleCount(Animation.INDEFINITE);
        rotation.setByAngle(360);
        rotation.setInterpolator(Interpolator.LINEAR);
        activityGraphic.setTranslateZ(activityGraphic.getBoundsInLocal().getWidth() / 2.0);
        activityGraphic.setRotationAxis(Rotate.Z_AXIS);
    }

    TaskProgressView<Task<?>> progressView = new TaskProgressView<>();
    Optional<TaskLists> optionalTaskLists = ServiceLoader.load(TaskLists.class).findFirst();
    {
        progressView.setRetainTasks(true);
        optionalTaskLists.ifPresent(taskLists -> {
            ProgressViewSkin skin = new ProgressViewSkin<>(progressView);
            progressView.setSkin(skin);
            Bindings.bindContent(progressView.getTasks(), taskLists.executingTasks());
        });
        progressView.getTasks().addListener(this::listInvalidated);
        if (progressView.getTasks().isEmpty()) {
            rotation.stop();
        } else {
            rotation.play();
        }
    }

    private static Node getTitleGraphic() {
        FontIcon icon = new FontIcon();
        icon.setIconLiteral("icm-spinner9:12:white");
        icon.setId("activity-node");
        return icon;
    }

    private void listInvalidated(Observable list) {
        if (progressView.getTasks().isEmpty()) {
            rotation.stop();
        } else {
            rotation.play();
        }
    }
    @Override
    public ReadOnlyProperty<String> getTitle() {
        return title;
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
