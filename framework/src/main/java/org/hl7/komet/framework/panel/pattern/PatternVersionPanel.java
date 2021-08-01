package org.hl7.komet.framework.panel.pattern;

import org.hl7.komet.framework.panel.ComponentVersionIsFinalPanel;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.tinkar.entity.PatternEntityVersion;

public class PatternVersionPanel extends ComponentVersionIsFinalPanel<PatternEntityVersion> {
    public PatternVersionPanel(PatternEntityVersion version, ViewProperties viewProperties) {
        super(version, viewProperties);
    }
}