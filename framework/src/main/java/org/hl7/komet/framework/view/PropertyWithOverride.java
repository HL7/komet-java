package org.hl7.komet.framework.view;

import javafx.beans.property.Property;
import org.hl7.tinkar.coordinate.view.calculator.ViewCalculator;

public interface PropertyWithOverride<T> extends Property<T> {
    boolean isOverridden();

    void removeOverride();

    Property<T> overriddenProperty();

    default T getOriginalValue() {
        return overriddenProperty().getValue();
    }

    default String getOverrideName(ViewCalculator viewCalculator) {
        return viewCalculator.toPreferredEntityStringOrInputString(getName());
    }
}
