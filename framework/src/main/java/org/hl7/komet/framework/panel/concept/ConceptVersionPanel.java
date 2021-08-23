package org.hl7.komet.framework.panel.concept;

import javafx.scene.Node;
import org.hl7.komet.framework.panel.ComponentVersionIsFinalPanel;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.tinkar.entity.ConceptEntityVersion;

public class ConceptVersionPanel extends ComponentVersionIsFinalPanel<ConceptEntityVersion> {
    public ConceptVersionPanel(ConceptEntityVersion version, ViewProperties viewProperties) {
        super(version, viewProperties);
        collapsiblePane.setExpanded(false);
        collapsiblePane.setContent(null);
    }

    @Override
    protected Node makeCenterNode(ConceptEntityVersion version, ViewProperties viewProperties) {
        return null;
    }
}