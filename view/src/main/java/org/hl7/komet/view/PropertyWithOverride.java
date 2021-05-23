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
        Optional<EntityFacade> entityFacadeOptional = ProxyFactory.fromXmlFragmentOptional(getName());
        Optional<String> optionalDescription;
        if (entityFacadeOptional.isPresent()) {
            optionalDescription = viewCalculator.getRegularDescriptionText(entityFacadeOptional.get());
        } else {
            optionalDescription = Optional.empty();
        }
        String name;
         if (optionalDescription.isPresent()) {
            name = optionalDescription.get();
        } else {
            name = getName();
        }
        if (isOverridden()) {
            return name + " with override";
        }
        return name;
    }
}
