package org.hl7.komet.framework.panel.pattern;

import javafx.scene.control.Label;
import org.hl7.komet.framework.PseudoClasses;
import org.hl7.komet.framework.panel.ComponentPanelAbstract;
import org.hl7.tinkar.entity.PatternEntity;

public class PatternPanel<T extends PatternEntity> extends ComponentPanelAbstract<T> {
    private final PatternEntity patternEntity;
    public PatternPanel(PatternEntity patternEntity) {
        this.patternEntity = patternEntity;
        this.getComponentPanelBox().getChildren().add(new Label(patternEntity.toString()));
        this.getComponentPanelBox().pseudoClassStateChanged(PseudoClasses.PATTERN_PSEUDO_CLASS, true);
        addSemanticReferences(patternEntity);
    }
}
