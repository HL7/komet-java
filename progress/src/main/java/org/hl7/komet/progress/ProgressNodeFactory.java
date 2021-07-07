package org.hl7.komet.progress;

import com.google.auto.service.AutoService;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import org.hl7.komet.framework.KometNode;
import org.hl7.komet.framework.KometNodeFactory;
import org.hl7.komet.preferences.KometPreferences;
import org.hl7.komet.framework.view.ObservableViewNoOverride;
import org.kordamp.ikonli.javafx.FontIcon;

@AutoService(KometNodeFactory.class)
public class ProgressNodeFactory implements KometNodeFactory {

    @Override
    public void addDefaultNodePreferences(KometPreferences nodePreferences) {

    }

    @Override
    public KometNode create(ObservableViewNoOverride windowView, KometPreferences nodePreferences) {
        return new ProgressNode(windowView.makeOverridableViewProperties(), nodePreferences);
    }

    @Override
    public String getStyleId() {
        return null;
    }

    @Override
    public Class<? extends KometNode> kometNodeClass() {
        return ProgressNode.class;
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
