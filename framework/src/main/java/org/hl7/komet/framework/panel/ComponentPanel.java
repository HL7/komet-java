package org.hl7.komet.framework.panel;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.scene.control.ScrollPane;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.tinkar.terms.EntityFacade;

import java.util.Optional;

public class ComponentPanel
        extends ComponentPanelAbstract
        implements ChangeListener<EntityFacade> {

    private final ScrollPane scrollPane = new ScrollPane(componentPanelBox);
    private final SimpleObjectProperty<EntityFacade> componentProperty;
    private final WeakChangeListener<EntityFacade> weakComponentChangedListener = new WeakChangeListener(this);

    {
        this.scrollPane.setFitToWidth(true);
        this.scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        this.componentDetailPane.setCenter(this.scrollPane);
    }

    public ComponentPanel(SimpleObjectProperty<EntityFacade> componentProperty, ViewProperties viewProperties) {
        super(viewProperties);
        this.componentProperty = componentProperty;
        this.componentProperty.addListener(this.weakComponentChangedListener);
        Platform.runLater(() -> changed(this.componentProperty, null, this.componentProperty.getValue()));
    }

    @Override
    public void changed(ObservableValue<? extends EntityFacade> observable, EntityFacade oldValue, EntityFacade newValue) {
        getComponentPanelBox().getChildren().clear();
        if (newValue != null) {
            getComponentPanelBox().getChildren().add(makeComponentPanel(newValue, componentProperty).getComponentDetailPane());
        }
    }

    @Override
    public <C extends EntityFacade> Optional<C> getComponent() {
        return Optional.ofNullable((C) componentProperty.getValue());
    }
}
