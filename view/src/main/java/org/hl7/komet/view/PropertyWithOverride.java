package org.hl7.komet.view;

import javafx.beans.property.Property;
import org.hl7.tinkar.coordinate.view.ViewCoordinate;
import org.hl7.tinkar.coordinate.view.calculator.ViewCalculator;
import org.hl7.tinkar.terms.ConceptProxy;
import org.hl7.tinkar.terms.EntityFacade;
import org.hl7.tinkar.terms.ProxyFactory;

import java.util.Optional;

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
