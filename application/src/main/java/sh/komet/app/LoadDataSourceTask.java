package sh.komet.app;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import org.hl7.tinkar.common.service.PrimitiveData;
import org.hl7.tinkar.common.service.TrackingCallable;

import static sh.komet.app.AppState.COMPUTE_GUI_PREREQUISITES;

public class LoadDataSourceTask extends TrackingCallable<Void> {
    final SimpleObjectProperty<AppState> state;

    public LoadDataSourceTask(SimpleObjectProperty<AppState> state) {
        super(false, true);
        this.state = state;
        updateTitle("Loading Data Source");
        updateMessage("Starting " + PrimitiveData.getController().controllerName());
        updateProgress(-1, -1);
    }

    @Override
    protected Void compute() throws Exception {
        try {
            PrimitiveData.start();
            Platform.runLater(() -> state.set(COMPUTE_GUI_PREREQUISITES));
            return null;
        } finally {
            updateTitle("Finished " + PrimitiveData.getController().controllerName());
            updateMessage("In " + durationString());
        }
    }
}
