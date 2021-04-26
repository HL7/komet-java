package org.hl7.komet.progress;

import com.google.auto.service.AutoService;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import org.hl7.komet.framework.ExplorationNode;
import org.hl7.komet.framework.NodeFactory;
import org.kordamp.ikonli.javafx.FontIcon;

@AutoService(NodeFactory.class)
public class ProgressNodeFactory implements NodeFactory {
    @Override
    public ExplorationNode create() {
        return new ProgressNode();
    }

    @Override
    public String getMenuText() {
        return "Activity";
    }

    @Override
    public Node getMenuGraphic() {
        FontIcon icon = new FontIcon();
        Label iconLabel = new Label("", icon);
        iconLabel.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        iconLabel.setId("activity-node");
        return iconLabel;
    }


}
