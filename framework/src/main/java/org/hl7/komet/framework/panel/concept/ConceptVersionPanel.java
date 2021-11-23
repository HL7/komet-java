package org.hl7.komet.framework.panel.concept;

import javafx.scene.Node;
import org.hl7.komet.framework.PseudoClasses;
import org.hl7.komet.framework.observable.ObservableConceptVersion;
import org.hl7.komet.framework.panel.ComponentVersionIsFinalPanel;
import org.hl7.komet.framework.view.ViewProperties;

public class ConceptVersionPanel extends ComponentVersionIsFinalPanel<ObservableConceptVersion> {
    public ConceptVersionPanel(ObservableConceptVersion version, ViewProperties viewProperties) {
        super(version, viewProperties);
        collapsiblePane.setExpanded(false);
        collapsiblePane.setContent(null);
    }

    @Override
    protected Node makeCenterNode(ObservableConceptVersion version, ViewProperties viewProperties) {
        versionDetailsPane.pseudoClassStateChanged(PseudoClasses.UNCOMMITTED_PSEUDO_CLASS, version.uncommitted());
        return null;
    }
}