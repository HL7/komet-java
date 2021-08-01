package org.hl7.komet.framework.panel.pattern;

import javafx.scene.control.Label;
import org.hl7.komet.framework.PseudoClasses;
import org.hl7.komet.framework.panel.ComponentIsFinalPanel;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.tinkar.entity.PatternEntity;
import org.hl7.tinkar.entity.PatternEntityVersion;

public class PatternPanel<T extends PatternEntity> extends ComponentIsFinalPanel<T, PatternEntityVersion> {

    public PatternPanel(T patternEntity, ViewProperties viewProperties) {
        super(patternEntity, viewProperties);
        Label label = new Label("Pattern Panel: " + patternEntity.publicId().toString());
        label.setWrapText(true);
        this.getComponentPanelBox().getChildren().add(label);
        this.getComponentPanelBox().pseudoClassStateChanged(PseudoClasses.PATTERN_PSEUDO_CLASS, true);
        addSemanticReferences(patternEntity);
    }
}
