package org.hl7.komet.framework.panel.pattern;

import javafx.scene.Node;
import org.controlsfx.control.PropertySheet;
import org.hl7.komet.framework.panel.ComponentVersionIsFinalPanel;
import org.hl7.komet.framework.propsheet.KometPropertySheet;
import org.hl7.komet.framework.propsheet.SheetItem;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.tinkar.common.util.text.NaturalOrder;
import org.hl7.tinkar.component.FieldDataType;
import org.hl7.tinkar.entity.FieldDefinitionForEntity;
import org.hl7.tinkar.entity.FieldRecord;
import org.hl7.tinkar.entity.PatternEntityVersion;
import org.hl7.tinkar.terms.EntityProxy;
import org.hl7.tinkar.terms.TinkarTerm;

public class PatternVersionPanel extends ComponentVersionIsFinalPanel<PatternEntityVersion> {

    public static final String REFERENCED_COMPONENT = "Referenced component";

    public PatternVersionPanel(PatternEntityVersion version, ViewProperties viewProperties) {
        super(version, viewProperties);
    }

    @Override
    protected Node makeCenterNode(PatternEntityVersion version, ViewProperties viewProperties) {
        KometPropertySheet propertySheet = new KometPropertySheet(viewProperties);
        // Referenced component meaning:
        // TODO Referenced component meaning concept.
        FieldRecord referencedComponentMeaningField = new FieldRecord(EntityProxy.Concept.make(version.referencedComponentMeaningNid()),
                TinkarTerm.MEANING.nid(), TinkarTerm.MEANING.nid(), FieldDataType.CONCEPT);
        propertySheet.getItems().add(SheetItem.make(referencedComponentMeaningField, REFERENCED_COMPONENT, viewProperties));
        // Referenced component purpose:
        // TODO Referenced component purpose concept.
        FieldRecord referencedComponentPurposeField = new FieldRecord(EntityProxy.Concept.make(version.referencedComponentPurposeNid()),
                TinkarTerm.PURPOSE.nid(), TinkarTerm.PURPOSE.nid(), FieldDataType.CONCEPT);
        propertySheet.getItems().add(SheetItem.make(referencedComponentPurposeField, REFERENCED_COMPONENT, viewProperties));
        // Add the field definitions.

        int i = 1;
        for (FieldDefinitionForEntity fieldDef : version.fieldDefinitions()) {
            String categoryName = "Field " + i + ": " + viewProperties.calculator().getPreferredDescriptionTextWithFallbackOrNid(fieldDef.meaning());

            FieldRecord fieldDataTypeField = new FieldRecord(EntityProxy.Concept.make(fieldDef.dataType()),
                    TinkarTerm.SEMANTIC_FIELD_TYPE.nid(), TinkarTerm.SEMANTIC_FIELD_TYPE.nid(), FieldDataType.CONCEPT);
            propertySheet.getItems().add(SheetItem.make(fieldDataTypeField, categoryName, viewProperties));
            FieldRecord fieldPurposeField = new FieldRecord(EntityProxy.Concept.make(fieldDef.purpose()),
                    TinkarTerm.PURPOSE.nid(), TinkarTerm.PURPOSE.nid(), FieldDataType.CONCEPT);
            propertySheet.getItems().add(SheetItem.make(fieldPurposeField, categoryName, viewProperties));
            FieldRecord fieldMeaningField = new FieldRecord(EntityProxy.Concept.make(fieldDef.meaning()),
                    TinkarTerm.MEANING.nid(), TinkarTerm.MEANING.nid(), FieldDataType.CONCEPT);
            propertySheet.getItems().add(SheetItem.make(fieldMeaningField, categoryName, viewProperties));
            i++;
        }
        propertySheet.setMode(PropertySheet.Mode.CATEGORY);
        propertySheet.setCategoryComparator((o1, o2) -> {
            if (o1.equals(o2)) {
                return 0;
            }
            if (o1.equals(REFERENCED_COMPONENT)) {
                return -1;
            }
            if (o2.equals(REFERENCED_COMPONENT)) {
                return 1;
            }
            return NaturalOrder.compareStrings(o1, o2);
        });
        return propertySheet;
    }
}