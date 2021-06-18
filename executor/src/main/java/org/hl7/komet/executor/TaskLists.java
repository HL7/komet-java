package org.hl7.komet.executor;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public interface TaskLists {
    ObservableList<Task<?>> pendingTasks();
    ObservableList<Task<?>> executingTasks();
    ObservableList<Task<?>> completedTasks();
}
