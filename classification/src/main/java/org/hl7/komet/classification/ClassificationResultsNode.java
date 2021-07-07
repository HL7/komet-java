package org.hl7.komet.classification;

import javafx.scene.Node;
import javafx.scene.control.Label;
import org.hl7.komet.framework.ExplorationNodeAbstract;
import org.hl7.komet.preferences.KometPreferences;
import org.hl7.komet.framework.view.ViewProperties;

public class ClassificationResultsNode extends ExplorationNodeAbstract {
    protected static final String STYLE_ID = "classification-results-node";
    protected static final String TITLE = "Classification Results";

    public ClassificationResultsNode(ViewProperties viewProperties, KometPreferences nodePreferences) {
        super(viewProperties, nodePreferences);
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
        return new Label(titleProperty.getValue());
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