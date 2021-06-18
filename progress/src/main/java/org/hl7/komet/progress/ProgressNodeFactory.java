package org.hl7.komet.progress;

import com.google.auto.service.AutoService;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import org.hl7.komet.framework.ExplorationNode;
import org.hl7.komet.framework.NodeFactory;
import org.hl7.komet.view.ObservableViewNoOverride;
import org.hl7.komet.view.ViewProperties;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.concurrent.atomic.AtomicReference;

@AutoService(NodeFactory.class)
public class ProgressNodeFactory implements NodeFactory {
    @Override
    public ExplorationNode create(AtomicReference<ObservableViewNoOverride> windowViewReference) {
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
