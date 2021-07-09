package org.hl7.komet.navigator;


import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import org.hl7.komet.framework.ExplorationNodeAbstract;
import org.hl7.komet.preferences.KometPreferences;
import org.hl7.komet.framework.view.ViewProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class GraphNavigatorNode extends ExplorationNodeAbstract {
    private static final Logger LOG = LoggerFactory.getLogger(GraphNavigatorNode.class);
    protected static final String STYLE_ID = "navigator-node";
    protected static final String TITLE = "Navigator";

    final AnchorPane root;
    final MultiParentGraphViewController controller;

    public GraphNavigatorNode(ViewProperties viewProperties, KometPreferences nodePreferences) {
        super(viewProperties, nodePreferences);
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/hl7/komet/navigator/GraphView.fxml"));
            this.root = loader.load();
            this.controller = loader.getController();
            this.controller.setProperties(this, viewProperties, nodePreferences);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
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
    public void revertPreferences() {

    }

    @Override
    public void savePreferences() {

    }

    @Override
    public Node getNode() {
        return root;
    }
    @Override
    public void close() {
        // nothing to do...
    }

    @Override
    public boolean canClose() {
        return true;
    }
}
