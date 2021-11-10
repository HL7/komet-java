package org.hl7.komet.framework.panel.semantic;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Node;
import org.eclipse.collections.api.list.ImmutableList;
import org.hl7.komet.framework.PseudoClasses;
import org.hl7.komet.framework.panel.ComponentVersionIsFinalPanel;
import org.hl7.komet.framework.propsheet.KometPropertySheet;
import org.hl7.komet.framework.propsheet.SheetItem;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.tinkar.common.id.IntIdCollection;
import org.hl7.tinkar.coordinate.stamp.calculator.Latest;
import org.hl7.tinkar.entity.Field;
import org.hl7.tinkar.entity.PatternEntityVersion;
import org.hl7.tinkar.entity.SemanticEntityVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hl7.tinkar.component.FieldDataType.COMPONENT_ID_LIST;
import static org.hl7.tinkar.component.FieldDataType.COMPONENT_ID_SET;

public class SemanticVersionPanel extends ComponentVersionIsFinalPanel<SemanticEntityVersion> {
    private static final Logger LOG = LoggerFactory.getLogger(SemanticVersionPanel.class);

    public SemanticVersionPanel(SemanticEntityVersion version, ViewProperties viewProperties) {
        super(version, viewProperties);
    }

    @Override
    protected Node makeCenterNode(SemanticEntityVersion version, ViewProperties viewProperties) {
        KometPropertySheet propertySheet = new KometPropertySheet(viewProperties);
        propertySheet.pseudoClassStateChanged(PseudoClasses.UNCOMMITTED_PSEUDO_CLASS, version.uncommitted());
        Latest<PatternEntityVersion> latestPatternEntityVersion = viewProperties.calculator().latestPatternEntityVersion(version.patternNid());
        latestPatternEntityVersion.ifPresent(patternEntityVersion -> {
            ImmutableList<Field> fields = version.fields(patternEntityVersion);
            if (fields.isEmpty()) {
                collapsiblePane.setExpanded(false);
                collapsiblePane.setContent(null);
            } else for (Field field : fields) {
                if (field.fieldDataType() == COMPONENT_ID_SET ||
                        field.fieldDataType() == COMPONENT_ID_LIST) {
                    if (field.value() instanceof IntIdCollection intIdCollection) {
                        if (intIdCollection.size() > 30) {
                            LOG.info("Suppressing field because number of components is to large. ");
                            SimpleStringProperty simpleStringProperty = new SimpleStringProperty(this,
                                    viewProperties.calculator().getPreferredDescriptionTextWithFallbackOrNid(field.meaningNid()),
                                    "Suppressing field because number of components is to large. "
                            );
                            propertySheet.getItems().add(SheetItem.make(simpleStringProperty));
                        } else {
                            propertySheet.getItems().add(SheetItem.make(field, version, viewProperties));
                        }
                    }
                } else {
                    propertySheet.getItems().add(SheetItem.make(field, version, viewProperties));
                }
            }
        });
        return propertySheet;
    }
}
