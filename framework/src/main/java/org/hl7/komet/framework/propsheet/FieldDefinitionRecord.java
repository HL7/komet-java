package org.hl7.komet.framework.propsheet;

import javafx.beans.property.SimpleObjectProperty;
import org.hl7.tinkar.entity.PatternEntityVersion;

public record FieldDefinitionRecord(SimpleObjectProperty valueProperty, String propertyDescription,
                                    String propertyName,
                                    PatternEntityVersion enclosingPatternVersion) {
}
