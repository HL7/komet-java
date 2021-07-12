package org.hl7.komet.search;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import org.hl7.komet.framework.ExplorationNodeAbstract;
import org.hl7.komet.framework.TopPanelFactory;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.komet.preferences.KometPreferences;
import org.hl7.tinkar.coordinate.view.calculator.ViewCalculatorWithCache;

public class SearchNode extends ExplorationNodeAbstract {
    protected static final String STYLE_ID = "search-node";
    protected static final String TITLE = "Search";

    private final BorderPane searchPane = new BorderPane();

    public SearchNode(ViewProperties viewProperties, KometPreferences nodePreferences) {
        super(viewProperties, nodePreferences);
             // TODO makeOverridableViewProperties should accept node preferences, and accept saved overrides

            Platform.runLater(() -> {
                this.searchPane.setCenter(new Label(titleProperty.getValue()));
                ViewCalculatorWithCache viewCalculator =
                        ViewCalculatorWithCache.getCalculator(viewProperties.nodeView().getValue());
                Node topPanel = TopPanelFactory.make(viewCalculator,
                        viewProperties.nodeView(), activityStreamKeyProperty(), optionForActivityStreamKeyProperty());
                this.searchPane.setTop(topPanel);
            });
    }

    @Override
    public Node getNode() {
        return searchPane;
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