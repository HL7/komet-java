package sh.komet.app;

import javafx.beans.property.SimpleObjectProperty;

public enum AppState {
    STARTING,
    SELECT_DATA_SOURCE,
    SELECTED_DATA_SOURCE,
    LOADING_DATA_SOURCE,
    RUNNING,
    SHUTDOWN;

}
