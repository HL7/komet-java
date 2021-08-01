package org.hl7.komet.framework.panel.concept;

import org.hl7.komet.framework.panel.ComponentVersionIsFinalPanel;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.tinkar.entity.ConceptEntityVersion;

public class ConceptVersionPanel extends ComponentVersionIsFinalPanel<ConceptEntityVersion> {
    public ConceptVersionPanel(ConceptEntityVersion version, ViewProperties viewProperties) {
        super(version, viewProperties);
    }
}