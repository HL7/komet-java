package org.hl7.komet.framework.panel.semantic;

import javafx.scene.Node;
import org.hl7.komet.framework.panel.ComponentVersionIsFinalPanel;
import org.hl7.komet.framework.propsheet.KometPropertySheet;
import org.hl7.komet.framework.propsheet.SheetItem;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.tinkar.coordinate.stamp.calculator.Latest;
import org.hl7.tinkar.entity.Field;
import org.hl7.tinkar.entity.PatternEntityVersion;
import org.hl7.tinkar.entity.SemanticEntityVersion;

public class SemanticVersionPanel extends ComponentVersionIsFinalPanel<SemanticEntityVersion> {
    public SemanticVersionPanel(SemanticEntityVersion version, ViewProperties viewProperties) {
        super(version, viewProperties);
    }

    @Override
    protected Node makeCenterNode(SemanticEntityVersion version, ViewProperties viewProperties) {
        KometPropertySheet propertySheet = new KometPropertySheet(viewProperties);
        Latest<PatternEntityVersion> latestPatternEntityVersion = viewProperties.calculator().latestPatternEntityVersion(version.patternNid());
        latestPatternEntityVersion.ifPresent(patternEntityVersion -> {
            for (Field field : version.fields(patternEntityVersion)) {
                propertySheet.getItems().add(SheetItem.make(field, viewProperties));
            }
        });

        return propertySheet;
    }
}
