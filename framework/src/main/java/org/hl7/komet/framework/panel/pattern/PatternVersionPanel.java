package org.hl7.komet.framework.panel.pattern;

import javafx.scene.Node;
import org.controlsfx.control.PropertySheet;
import org.hl7.komet.framework.panel.ComponentVersionIsFinalPanel;
import org.hl7.komet.framework.propsheet.FieldDefinitionRecord;
import org.hl7.komet.framework.propsheet.KometPropertySheet;
import org.hl7.komet.framework.propsheet.SheetItem;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.tinkar.common.util.text.NaturalOrder;
import org.hl7.tinkar.entity.FieldDefinitionForEntity;
import org.hl7.tinkar.entity.PatternEntityVersion;
import org.hl7.tinkar.terms.EntityProxy;

public class PatternVersionPanel extends ComponentVersionIsFinalPanel<PatternEntityVersion> {

    public static final String REFERENCED_COMPONENT = "Referenced component";

    public PatternVersionPanel(PatternEntityVersion version, ViewProperties viewProperties) {
        super(version, viewProperties);
    }

    @Override
    protected Node makeCenterNode(PatternEntityVersion version, ViewProperties viewProperties) {
        KometPropertySheet propertySheet = new KometPropertySheet(viewProperties);
        // Referenced component meaning:
        // TODO Referenced component meaning concept & definition.
        FieldDefinitionRecord referencedComponentMeaningField = new FieldDefinitionRecord(EntityProxy.Concept.make(version.referencedComponentMeaningNid()),
                "The meaning of the referenced component of a semantic with this pattern",
                "Referenced component meaning", version);
        propertySheet.getItems().add(SheetItem.make(referencedComponentMeaningField, REFERENCED_COMPONENT, viewProperties));
        // Referenced component purpose:
        // TODO Referenced component purpose concept & definition.
        FieldDefinitionRecord referencedComponentPurposeField = new FieldDefinitionRecord(EntityProxy.Concept.make(version.referencedComponentPurposeNid()),
                "The purpose served by the referenced component of a semantic with this pattern",
                "Referenced component purpose ", version);
        propertySheet.getItems().add(SheetItem.make(referencedComponentPurposeField, REFERENCED_COMPONENT, viewProperties));
        // Add the field definitions.

        int i = 1;
        for (FieldDefinitionForEntity fieldDef : version.fieldDefinitions()) {
            String categoryName = "Field " + i + ": " + viewProperties.calculator().getPreferredDescriptionTextWithFallbackOrNid(fieldDef.meaning());

            FieldDefinitionRecord fieldDataTypeField = new FieldDefinitionRecord(EntityProxy.Concept.make(fieldDef.dataTypeNid()),
                    "Specify the data type of this field for semantics of this pattern",
                    "Data type", version);
            propertySheet.getItems().add(SheetItem.make(fieldDataTypeField, categoryName, viewProperties));
            FieldDefinitionRecord fieldPurposeField = new FieldDefinitionRecord(EntityProxy.Concept.make(fieldDef.purposeNid()),
                    "Specify the purpose of this field for semantics of this pattern",
                    "Purpose", version);
            propertySheet.getItems().add(SheetItem.make(fieldPurposeField, categoryName, viewProperties));
            FieldDefinitionRecord fieldMeaningField = new FieldDefinitionRecord(EntityProxy.Concept.make(fieldDef.meaningNid()),
                    "Specify the meaning of this field for semantics of this pattern",
                    "Meaning", version);
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