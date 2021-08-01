package org.hl7.komet.framework.panel;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.scene.control.ScrollPane;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.tinkar.terms.EntityFacade;

import java.util.Optional;

public class ComponentPanel<C extends EntityFacade>
        extends ComponentPanelAbstract<C>
        implements ChangeListener<C> {

    private final ScrollPane scrollPane = new ScrollPane(componentPanelBox);
    private final ObservableValue<C> componentProperty;
    private final WeakChangeListener<C> weakComponentChangedListener = new WeakChangeListener(this);

    {
        this.scrollPane.setFitToWidth(true);
        this.scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        this.componentDetailPane.setCenter(this.scrollPane);
    }

    public ComponentPanel(ObservableValue<C> componentProperty, ViewProperties viewProperties) {
        super(viewProperties);
        this.componentProperty = componentProperty;
        this.componentProperty.addListener(this.weakComponentChangedListener);
        Platform.runLater(() -> changed(this.componentProperty, null, this.componentProperty.getValue()));
    }

    @Override
    public void changed(ObservableValue<? extends C> observable, C oldValue, C newValue) {
        getComponentPanelBox().getChildren().clear();
        if (newValue != null) {
            getComponentPanelBox().getChildren().add(makeComponentPanel(newValue).getComponentDetailPane());
        }
    }

    @Override
    public Optional<C> getComponent() {
        return Optional.ofNullable(componentProperty.getValue());
    }
}
