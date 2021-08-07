package org.hl7.komet.framework.panel;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.tinkar.common.util.time.DateTimeUtil;
import org.hl7.tinkar.entity.EntityVersion;
import org.hl7.tinkar.entity.StampEntity;

public class StampPanel<V extends EntityVersion> extends HBox {
    final StampEntity stampEntity;

    public StampPanel(V entityVersion, ViewProperties viewProperties) {
        super(4);
        this.stampEntity = entityVersion.stamp();
        getChildren().add(new Label(stampEntity.state().toString()));
        getChildren().add(new Label(DateTimeUtil.format(this.stampEntity.time())));
        getChildren().add(new Label(viewProperties.calculator().getDescriptionTextOrNid(this.stampEntity.authorNid())));
        getChildren().add(new Label(viewProperties.calculator().getDescriptionTextOrNid(this.stampEntity.moduleNid())));
        getChildren().add(new Label(viewProperties.calculator().getDescriptionTextOrNid(this.stampEntity.pathNid())));
    }
}
