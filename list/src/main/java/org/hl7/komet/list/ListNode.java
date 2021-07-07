package org.hl7.komet.list;

import javafx.scene.Node;
import javafx.scene.control.Label;
import org.hl7.komet.framework.ExplorationNodeAbstract;
import org.hl7.komet.preferences.KometPreferences;
import org.hl7.komet.framework.view.ViewProperties;

public class ListNode extends ExplorationNodeAbstract {
    protected static final String STYLE_ID = "list-node";
    protected static final String TITLE = "List Manager";

    public ListNode(ViewProperties viewProperties, KometPreferences nodePreferences) {
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