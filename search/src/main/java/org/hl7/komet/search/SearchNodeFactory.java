package org.hl7.komet.search;

import com.google.auto.service.AutoService;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import org.hl7.komet.framework.ExplorationNode;
import org.hl7.komet.framework.NodeFactory;
import org.kordamp.ikonli.javafx.FontIcon;

@AutoService(NodeFactory.class)
public class SearchNodeFactory implements NodeFactory {
    protected static final String STYLE_ID = SearchNode.STYLE_ID;
    protected static final String TITLE = SearchNode.TITLE;

    @Override
    public ExplorationNode create() {
        return new SearchNode();
    }

    @Override
    public String getMenuText() {
        return TITLE;
    }

    @Override
    public Node getMenuGraphic() {
        FontIcon icon = new FontIcon();
        Label iconLabel = new Label("", icon);
        iconLabel.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        iconLabel.setId(STYLE_ID);
        return iconLabel;
    }
}