package org.hl7.komet.framework.panel.semantic;

import org.hl7.komet.framework.panel.ComponentVersionIsFinalPanel;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.tinkar.entity.SemanticEntityVersion;

public class SemanticVersionPanel extends ComponentVersionIsFinalPanel<SemanticEntityVersion> {
    public SemanticVersionPanel(SemanticEntityVersion version, ViewProperties viewProperties) {
        super(version, viewProperties);
    }
}
