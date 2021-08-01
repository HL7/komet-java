package org.hl7.komet.framework.panel;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.tinkar.entity.EntityVersion;

public class ComponentVersionIsFinalPanel<V extends EntityVersion> {
    protected final BorderPane versionDetailsPane = new BorderPane();
    private final V version;
    private final ViewProperties viewProperties;

    public ComponentVersionIsFinalPanel(V version, ViewProperties viewProperties) {
        this.version = version;
        this.viewProperties = viewProperties;
        // TODO: change to more than label with toString()
        Label label = new Label(version.toString());
        label.setWrapText(true);
        BorderPane.setAlignment(label, Pos.TOP_LEFT);
        this.versionDetailsPane.setCenter(label);
    }

    public BorderPane getVersionDetailsPane() {
        return versionDetailsPane;
    }
}
