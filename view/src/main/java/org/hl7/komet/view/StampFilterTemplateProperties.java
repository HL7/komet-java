package org.hl7.komet.view;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SetProperty;
import org.hl7.tinkar.coordinate.stamp.StateSet;
import org.hl7.tinkar.terms.ConceptFacade;

interface StampFilterTemplateProperties {

    default Property<?>[] getBaseProperties() {
        return new Property<?>[] {
                allowedStatesProperty(),
                moduleSpecificationsProperty(),
                excludedModuleSpecificationsProperty(),
                modulePriorityOrderProperty()
        };
    }

    default ObservableCoordinate<?>[] getEmbeddedCoordinates() {
        return new ObservableCoordinate<?>[]{

        };
    }

    /**
     *
     * @return a set of allowed status values to filter computation results.
     */
    ObjectProperty<StateSet> allowedStatesProperty();

    /**
     *
     * @return the specified modules property
     */
    SetProperty<ConceptFacade> moduleSpecificationsProperty();

    /**
     *
     * @return the specified modules property
     */
    SetProperty<ConceptFacade> excludedModuleSpecificationsProperty();

    /**
     * Module preference list property.
     *
     * @return the object property
     */
    ListProperty<ConceptFacade> modulePriorityOrderProperty();
}
