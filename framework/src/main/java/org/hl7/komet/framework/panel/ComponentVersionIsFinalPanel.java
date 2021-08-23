package org.hl7.komet.framework.panel;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.tinkar.common.util.time.DateTimeUtil;
import org.hl7.tinkar.entity.EntityVersion;
import org.hl7.tinkar.entity.StampEntity;

public abstract class ComponentVersionIsFinalPanel<V extends EntityVersion> {
    protected final BorderPane versionDetailsPane = new BorderPane();
    protected final TitledPane collapsiblePane = new TitledPane("version", versionDetailsPane);
    private final V version;
    private final ViewProperties viewProperties;

    public ComponentVersionIsFinalPanel(V version, ViewProperties viewProperties) {
        this.version = version;
        this.viewProperties = viewProperties;
        Node versionNode = makeCenterNode(version, viewProperties);
        if (versionNode != null) {
            BorderPane.setAlignment(versionNode, Pos.TOP_LEFT);
        }
        this.versionDetailsPane.setCenter(versionNode);
        //this.versionDetailsPane.setBottom(new StampPanel<V>(version, viewProperties));
        StampEntity stampEntity = version.stamp();
        this.collapsiblePane.setText(stampEntity.state() + " as of " + DateTimeUtil.format(stampEntity.time()) +
                " on " + viewProperties.calculator().getPreferredDescriptionTextWithFallbackOrNid(stampEntity.pathNid()) +
                " in " + viewProperties.calculator().getPreferredDescriptionTextWithFallbackOrNid(stampEntity.moduleNid()) +
                " by " + viewProperties.calculator().getPreferredDescriptionTextWithFallbackOrNid(stampEntity.authorNid()));
    }

    protected abstract Node makeCenterNode(V version, ViewProperties viewProperties);

    public TitledPane getVersionDetailsPane() {
        return collapsiblePane;
    }
}
