package org.hl7.komet.framework.panel;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import org.hl7.tinkar.entity.*;

public class ComponentPanel<T extends Entity<? extends EntityVersion>>
        extends ComponentPanelAbstract<T>
        implements ChangeListener<T> {

    private final ObservableValue<T> componentProperty;

    private final WeakChangeListener<T> weakComponentChangedListener = new WeakChangeListener(this);

    public ComponentPanel(ObservableValue<T> componentProperty) {
        this.componentProperty = componentProperty;
        this.componentProperty.addListener(this.weakComponentChangedListener);
        Platform.runLater(() -> changed(this.componentProperty, null, this.componentProperty.getValue()));
    }

    @Override
    public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
        getComponentPanelBox().getChildren().clear();
        if (newValue != null) {
            getComponentPanelBox().getChildren().add(makeComponentPanel(newValue).getDetailPane());
        }
    }
}
