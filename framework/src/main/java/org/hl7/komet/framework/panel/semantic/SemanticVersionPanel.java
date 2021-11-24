package org.hl7.komet.framework.panel.semantic;

import javafx.scene.Node;
import org.eclipse.collections.api.list.ImmutableList;
import org.hl7.komet.framework.PseudoClasses;
import org.hl7.komet.framework.observable.ObservableField;
import org.hl7.komet.framework.observable.ObservableSemanticVersion;
import org.hl7.komet.framework.panel.ComponentVersionIsFinalPanel;
import org.hl7.komet.framework.propsheet.KometPropertySheet;
import org.hl7.komet.framework.propsheet.SheetItem;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.tinkar.coordinate.stamp.calculator.Latest;
import org.hl7.tinkar.entity.PatternEntityVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SemanticVersionPanel extends ComponentVersionIsFinalPanel<ObservableSemanticVersion> {
    private static final Logger LOG = LoggerFactory.getLogger(SemanticVersionPanel.class);

    public SemanticVersionPanel(ObservableSemanticVersion version, ViewProperties viewProperties) {
        super(version, viewProperties);
    }

    @Override
    protected Node makeCenterNode(ObservableSemanticVersion version, ViewProperties viewProperties) {
        KometPropertySheet propertySheet = new KometPropertySheet(viewProperties);
        propertySheet.pseudoClassStateChanged(PseudoClasses.UNCOMMITTED_PSEUDO_CLASS, version.uncommitted());
        Latest<PatternEntityVersion> latestPatternEntityVersion = viewProperties.calculator().latestPatternEntityVersion(version.patternNid());
        latestPatternEntityVersion.ifPresent(patternEntityVersion -> {
            ImmutableList<ObservableField> fields = version.fields(patternEntityVersion);
            if (fields.isEmpty()) {
                collapsiblePane.setExpanded(false);
                collapsiblePane.setContent(null);
            } else for (ObservableField field : fields) {
                propertySheet.getItems().add(SheetItem.make(field, version, viewProperties));
            }
        });
        return propertySheet;
    }
}
