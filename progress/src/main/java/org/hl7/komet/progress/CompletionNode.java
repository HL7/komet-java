package org.hl7.komet.progress;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.concurrent.Task;
import javafx.scene.Node;
import org.controlsfx.control.TaskProgressView;
import org.eclipse.collections.api.list.ImmutableList;
import org.hl7.komet.framework.ExplorationNodeAbstract;
import org.hl7.komet.framework.concurrent.TaskListsService;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.komet.preferences.KometPreferences;
import org.hl7.tinkar.terms.EntityFacade;
import org.kordamp.ikonli.javafx.FontIcon;

public class CompletionNode extends ExplorationNodeAbstract {

    protected static final String STYLE_ID = "completion-node";
    protected static final String TITLE = "Completions";

    TaskProgressView<Task<?>> progressView = new TaskProgressView<>();
    TaskListsService taskLists = TaskListsService.get();

    {
        progressView.setRetainTasks(true);
        CompletionViewSkin skin = new CompletionViewSkin<>(progressView, taskLists.completedTasks());
        progressView.setSkin(skin);
        Bindings.bindContent(progressView.getTasks(), taskLists.completedTasks());
    }

    public CompletionNode(ViewProperties viewProperties, KometPreferences nodePreferences) {
        super(viewProperties, nodePreferences);
    }

    @Override
    public String getDefaultTitle() {
        return TITLE;
    }

    @Override
    public void handleActivity(ImmutableList<EntityFacade> entities) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getStyleId() {
        return STYLE_ID;
    }

    @Override
    protected void saveAdditionalPreferences() {

    }

    private Node getTitleGraphic() {
        FontIcon icon = new FontIcon();
        icon.setIconLiteral("mdi2f-flag-checkered:16:white");
        icon.setId(getStyleId());
        return icon;
    }

    public void removeTask(Task<?> task) {
        Platform.runLater(() -> taskLists.completedTasks().remove(task));
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
    public void revertPreferences() {

    }

    @Override
    public Class factoryClass() {
        return CompletionNodeFactory.class;
    }
}
