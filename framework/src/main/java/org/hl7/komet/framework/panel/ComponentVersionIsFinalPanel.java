package org.hl7.komet.framework.panel;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.tinkar.entity.EntityVersion;

public abstract class ComponentVersionIsFinalPanel<V extends EntityVersion> {
    protected final BorderPane versionDetailsPane = new BorderPane();
    private final V version;
    private final ViewProperties viewProperties;

    public ComponentVersionIsFinalPanel(V version, ViewProperties viewProperties) {
        this.version = version;
        this.viewProperties = viewProperties;
        Node versionNode = makeCenterNode(version, viewProperties);
        BorderPane.setAlignment(versionNode, Pos.TOP_LEFT);
        this.versionDetailsPane.setCenter(versionNode);
        this.versionDetailsPane.setBottom(new StampPanel<V>(version, viewProperties));
    }

    protected abstract Node makeCenterNode(V version, ViewProperties viewProperties);

    public BorderPane getVersionDetailsPane() {
        return versionDetailsPane;
    }
}
