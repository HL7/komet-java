package org.hl7.komet.details;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import org.hl7.komet.framework.ExplorationNodeAbstract;
import org.hl7.komet.framework.KometNode;
import org.hl7.komet.framework.TopPanelFactory;
import org.hl7.komet.preferences.KometPreferences;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.tinkar.coordinate.view.calculator.ViewCalculatorWithCache;

public class DetailsNode extends ExplorationNodeAbstract {
    protected static final String STYLE_ID = "concept-details-node";
    protected static final String TITLE = "Details";

    enum PreferenceKey {
        TEST
    }

    protected static void addDefaultNodePreferences(KometPreferences nodePreferences) {
        nodePreferences.put(PreferenceKey.TEST, "test");
        nodePreferences.get(KometNode.PreferenceKey.PARENT_ALERT_STREAM_KEY);
    }

    private final BorderPane detailsPane = new BorderPane();

    public DetailsNode(ViewProperties viewProperties, KometPreferences nodePreferences) {
        super(viewProperties, nodePreferences);

        Platform.runLater(() -> {
            this.detailsPane.setCenter(new Label(titleProperty.getValue()));
            ViewCalculatorWithCache viewCalculator =
                    ViewCalculatorWithCache.getCalculator(viewProperties.nodeView().getValue());
            Node topPanel = TopPanelFactory.make(viewCalculator,
                    viewProperties.nodeView(), activityStreamKeyProperty, optionForActivityStreamKeyProperty);
            this.detailsPane.setTop(topPanel);
        });
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
    public Node getNode() {
        return this.detailsPane;
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