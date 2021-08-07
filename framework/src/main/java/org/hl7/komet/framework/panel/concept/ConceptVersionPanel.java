package org.hl7.komet.framework.panel.concept;

import javafx.scene.Node;
import javafx.scene.control.Label;
import org.hl7.komet.framework.panel.ComponentVersionIsFinalPanel;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.tinkar.entity.ConceptEntityVersion;

public class ConceptVersionPanel extends ComponentVersionIsFinalPanel<ConceptEntityVersion> {
    public ConceptVersionPanel(ConceptEntityVersion version, ViewProperties viewProperties) {
        super(version, viewProperties);
    }

    @Override
    protected Node makeCenterNode(ConceptEntityVersion version, ViewProperties viewProperties) {
        // Nothing to add beyond stamp already added.
        return new Label();
    }
}