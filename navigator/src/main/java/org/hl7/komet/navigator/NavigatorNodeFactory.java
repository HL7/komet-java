package org.hl7.komet.navigator;

import com.google.auto.service.AutoService;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import org.hl7.komet.framework.ExplorationNode;
import org.hl7.komet.framework.NodeFactory;
import org.kordamp.ikonli.javafx.FontIcon;

@AutoService(NavigatorNode.class)
public class NavigatorNodeFactory implements NodeFactory {
    protected static final String STYLE_ID = NavigatorNode.STYLE_ID;
    protected static final String TITLE = NavigatorNode.TITLE;

    @Override
    public ExplorationNode create() {
        return new NavigatorNode();
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