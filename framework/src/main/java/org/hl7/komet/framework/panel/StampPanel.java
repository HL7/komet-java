package org.hl7.komet.framework.panel;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.hl7.komet.framework.StyleClasses;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.tinkar.common.util.time.DateTimeUtil;
import org.hl7.tinkar.entity.EntityVersion;
import org.hl7.tinkar.entity.StampEntity;

public class StampPanel<V extends EntityVersion> extends HBox {
    final StampEntity stampEntity;

    public StampPanel(V entityVersion, ViewProperties viewProperties) {
        super(4);
        this.stampEntity = entityVersion.stamp();

        Label stateLabel = new Label(stampEntity.state().toString());
        stateLabel.getStyleClass().add(StyleClasses.STAMP_STATUS_PANEL.toString());
        getChildren().add(stateLabel);

        Label timeLabel = new Label(DateTimeUtil.format(this.stampEntity.time()));
        timeLabel.getStyleClass().add(StyleClasses.STAMP_TIME_PANEL.toString());
        getChildren().add(timeLabel);

        Label authorLabel = new Label(viewProperties.calculator().getDescriptionTextOrNid(this.stampEntity.authorNid()));
        authorLabel.getStyleClass().add(StyleClasses.STAMP_AUTHOR_PANEL.toString());
        getChildren().add(authorLabel);

        Label moduleLabel = new Label(viewProperties.calculator().getDescriptionTextOrNid(this.stampEntity.moduleNid()));
        moduleLabel.getStyleClass().add(StyleClasses.STAMP_MODULE_PANEL.toString());
        getChildren().add(moduleLabel);

        Label pathLabel = new Label(viewProperties.calculator().getDescriptionTextOrNid(this.stampEntity.pathNid()));
        pathLabel.getStyleClass().add(StyleClasses.STAMP_PATH_PANEL.toString());
        getChildren().add(pathLabel);

        this.getStyleClass().add(StyleClasses.STAMP_PANEL.toString());
    }
}
