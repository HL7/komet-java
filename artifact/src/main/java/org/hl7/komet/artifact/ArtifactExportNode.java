package org.hl7.komet.artifact;

import javafx.scene.Node;
import javafx.scene.control.Label;
import org.hl7.komet.framework.ExplorationNodeAbstract;
import org.hl7.komet.preferences.KometPreferences;
import org.hl7.komet.framework.view.ViewProperties;

public class ArtifactExportNode extends ExplorationNodeAbstract {
    protected static final String STYLE_ID = "export-node";
    protected static final String TITLE = "Export Artifact";

    public ArtifactExportNode(ViewProperties viewProperties, KometPreferences nodePreferences) {
        super(viewProperties, nodePreferences);
    }

    @Override
    public Node getNode() {
        return new Label(titleProperty.getValue());
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
