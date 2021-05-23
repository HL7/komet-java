package org.hl7.komet.view;


import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SetProperty;
import org.hl7.tinkar.coordinate.logic.PremiseType;
import org.hl7.tinkar.coordinate.navigation.NavigationCoordinateRecord;
import org.hl7.tinkar.coordinate.navigation.NavigationCoordinateDelegate;
import org.hl7.tinkar.terms.PatternFacade;
import org.hl7.tinkar.terms.State;
import org.hl7.tinkar.terms.TinkarTerm;

public interface ObservableNavigationCoordinate
        extends ObservableCoordinate<NavigationCoordinateRecord>, NavigationCoordinateDelegate {

    SetProperty<PatternFacade> navigationPatternsProperty();

    /**
     *
     * @return a set of allowed status values to filter computation results.
     */
    SetProperty<State> vertexStatesProperty();

    ObjectProperty<Boolean> sortVerticesProperty();

    ListProperty<PatternFacade> verticesSortPatternListProperty();


    default Property<?>[] getBaseProperties() {
        return new Property<?>[] {
                navigationPatternsProperty(),
                vertexStatesProperty(),
                sortVerticesProperty(),
                verticesSortPatternListProperty()
         };
    }

    default ObservableCoordinate<?>[] getCompositeCoordinates() {
        return new ObservableCoordinate<?>[] {};
    }

    default void setPremiseType(PremiseType premiseType) {
        switch (premiseType) {
            case STATED:
                navigationPatternsProperty().clear();
                navigationPatternsProperty().add(TinkarTerm.EL_PLUS_PLUS_STATED_AXIOMS_PATTERN);
                break;
            case INFERRED:
                navigationPatternsProperty().clear();
                navigationPatternsProperty().add(TinkarTerm.EL_PLUS_PLUS_INFERRED_AXIOMS_PATTERN);
                break;
        }
    }

}
