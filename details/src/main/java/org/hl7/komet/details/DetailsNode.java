package org.hl7.komet.details;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import org.hl7.komet.framework.ActivityStream;
import org.hl7.komet.framework.ExplorationNode;
import org.hl7.komet.framework.TopPanelFactory;
import org.hl7.komet.view.ObservableViewNoOverride;
import org.hl7.komet.view.ViewProperties;
import org.hl7.tinkar.common.service.Executor;
import org.hl7.tinkar.coordinate.view.calculator.ViewCalculatorWithCache;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.concurrent.atomic.AtomicReference;

public class DetailsNode implements ExplorationNode {
    protected static final String STYLE_ID = "concept-details-node";
    protected static final String TITLE = "Details";


    private final BorderPane detailsPane = new BorderPane();
    AtomicReference<ViewProperties> viewPropertiesReference = new AtomicReference<>();

    public DetailsNode(AtomicReference<ObservableViewNoOverride> windowViewReference) {
        this.detailsPane.setCenter(new Label(titleProperty.getValue()));

        Executor.afterDataLoadThreadPool().execute(() -> {
            Platform.runLater(() -> {
                ObservableViewNoOverride windowView = windowViewReference.get();
                viewPropertiesReference.set(windowView.makeOverridableViewProperties());
                ViewProperties viewProperties = viewPropertiesReference.get();
                ViewCalculatorWithCache viewCalculator =
                        ViewCalculatorWithCache.getCalculator(viewProperties.overridableView().getValue());
                Node topPanel = TopPanelFactory.make(viewCalculator,
                        viewProperties.overridableView());
                Platform.runLater(() -> this.detailsPane.setTop(topPanel));
            });
        });

    }

    SimpleStringProperty titleProperty = new SimpleStringProperty(TITLE);
    Label titleNode = new Label("", new FontIcon());
    {
        titleNode.setId(STYLE_ID);
        titleProperty.addListener((observable, oldValue, newValue) -> {
            titleNode.setText(newValue);
        });
    }

    @Override
    public ReadOnlyProperty<String> getTitle() {
        return titleProperty;
    }

    @Override
    public Node getNode() {
        return this.detailsPane;
    }

    @Override
    public Node getTitleNode() {
        return titleNode;
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