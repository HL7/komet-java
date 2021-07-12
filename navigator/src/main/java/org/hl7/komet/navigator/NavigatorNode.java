package org.hl7.komet.navigator;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import org.hl7.komet.framework.ExplorationNodeAbstract;
import org.hl7.komet.framework.TopPanelFactory;
import org.hl7.komet.preferences.KometPreferences;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.tinkar.common.service.Executor;
import org.hl7.tinkar.coordinate.view.calculator.ViewCalculatorWithCache;

public class NavigatorNode extends ExplorationNodeAbstract {
    protected static final String STYLE_ID = "navigator-node";
    protected static final String TITLE = "Navigator";


    private final BorderPane navigatorPane = new BorderPane();

    public NavigatorNode(ViewProperties viewProperties, KometPreferences nodePreferences) {
        super(viewProperties, nodePreferences);
        this.navigatorPane.setCenter(new Label(titleProperty.getValue()));
        ViewCalculatorWithCache viewCalculator =
                ViewCalculatorWithCache.getCalculator(getViewProperties().nodeView().getValue());
        Node topPanel = TopPanelFactory.make(viewCalculator,
                getViewProperties().nodeView(), activityStreamKeyProperty(), optionForActivityStreamKeyProperty);
        Platform.runLater(() -> this.navigatorPane.setTop(topPanel));

    }

    @Override
    public Node getNode() {
        return navigatorPane;
    }

    @Override
    public String getStyleId() {
        return STYLE_ID;
    }

    @Override
    public String getDefaultTitle() {
        return TITLE;
    }

    @Override
    public void close() {

    }

    @Override
    public boolean canClose() {
        return false;
    }

    @Override
    public void savePreferences() {

    }

    @Override
    public void revertPreferences() {

    }
}