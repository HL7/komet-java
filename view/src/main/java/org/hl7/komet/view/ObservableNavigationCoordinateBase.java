package org.hl7.komet.view;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.SetChangeListener;
import org.hl7.tinkar.common.id.IntIds;
import org.hl7.tinkar.coordinate.navigation.NavigationCoordinate;
import org.hl7.tinkar.coordinate.navigation.NavigationCoordinateRecord;
import org.hl7.tinkar.coordinate.navigation.NavigationCoordinateDelegate;
import org.hl7.tinkar.coordinate.stamp.StateSet;
import org.hl7.tinkar.terms.PatternFacade;
import org.hl7.tinkar.terms.State;

public abstract class ObservableNavigationCoordinateBase
        extends ObservableCoordinateAbstract<NavigationCoordinateRecord>
        implements NavigationCoordinateDelegate, ObservableNavigationCoordinate {

    private final SimpleEqualityBasedSetProperty<PatternFacade> navigatorIdentifierConceptsProperty;
    private final SimpleEqualityBasedSetProperty<State> vertexStatesProperty;
    private final ObjectProperty<Boolean> sortVerticesProperty;
    private final ListProperty<PatternFacade> verticesSortPatternListProperty;

    /**
     * Note that if you don't declare a listener as final in this way, and just use method references, or
     * a direct lambda expression, you will not be able to remove the listener, since each method reference will create
     * a new object, and they won't compare equal using object identity.
     * https://stackoverflow.com/questions/42146360/how-do-i-remove-lambda-expressions-method-handles-that-are-used-as-listeners
     */
    private final SetChangeListener<PatternFacade> navigatorIdentifierConceptSetListener = this::navigationSetChanged;
    private final SetChangeListener<State> vertexStatesSetListener = this::vertexStateSetChanged;
    private final ChangeListener<Boolean> sortVerticesListener = this::sortVerticesListener;
    private final ListChangeListener<PatternFacade> verticesSortPatternListener = this::verticesSortPatternListener;

    public ObservableNavigationCoordinateBase(NavigationCoordinate navigationCoordinate, String coordinateName) {
        super(navigationCoordinate.toNavigationCoordinateRecord(), coordinateName);
        this.navigatorIdentifierConceptsProperty = makeNavigationPatternsProperty(navigationCoordinate);
        this.vertexStatesProperty = makeVertexStatesProperty(navigationCoordinate);
        this.sortVerticesProperty = makeSortVerticesProperty(navigationCoordinate);
        this.verticesSortPatternListProperty = makeVerticesSortPatternListProperty(navigationCoordinate);
        addListeners();
    }

    protected abstract SimpleEqualityBasedSetProperty<PatternFacade> makeNavigationPatternsProperty(NavigationCoordinate navigationCoordinate);
    protected abstract SimpleEqualityBasedSetProperty<State> makeVertexStatesProperty(NavigationCoordinate navigationCoordinate);
    protected abstract ObjectProperty<Boolean> makeSortVerticesProperty(NavigationCoordinate navigationCoordinate);
    protected abstract ListProperty<PatternFacade> makeVerticesSortPatternListProperty(NavigationCoordinate navigationCoordinate);


    @Override
    protected void addListeners() {
        this.navigatorIdentifierConceptsProperty.addListener(this.navigatorIdentifierConceptSetListener);
        this.vertexStatesProperty.addListener(this.vertexStatesSetListener);
        this.sortVerticesProperty.addListener(this.sortVerticesListener);
        this.verticesSortPatternListProperty.addListener(this.verticesSortPatternListener);
    }

    @Override
    protected void removeListeners() {
        this.navigatorIdentifierConceptsProperty.removeListener(this.navigatorIdentifierConceptSetListener);
        this.vertexStatesProperty.removeListener(this.vertexStatesSetListener);
        this.sortVerticesProperty.removeListener(this.sortVerticesListener);
        this.verticesSortPatternListProperty.removeListener(this.verticesSortPatternListener);
    }

    private void navigationSetChanged(SetChangeListener.Change<? extends PatternFacade> c) {
        this.setValue(getValue().withNavigationPatternNids(
                IntIds.set.of(c.getSet().stream().mapToInt(patternFacade -> patternFacade.nid()).toArray())));
    }

    private void vertexStateSetChanged(SetChangeListener.Change<? extends State> c) {
        this.setValue(getValue().withVertexStates(StateSet.of(c.getSet())));
    }

    private void sortVerticesListener(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
        this.setValue(getValue().withSortVertices(newValue));
    }


    private void verticesSortPatternListener(ListChangeListener.Change<? extends PatternFacade> c) {
        this.setValue(getValue().withVerticesSortPatternNidList(IntIds.list.of(
                c.getList().stream().mapToInt(patternFacade -> patternFacade.nid()).toArray())));
    }

    @Override
    public NavigationCoordinateRecord navigationCoordinate() {
        return getValue();
    }

    @Override
    public SimpleEqualityBasedSetProperty<PatternFacade> navigationPatternsProperty() {
        return navigatorIdentifierConceptsProperty;
    }

    @Override
    public SimpleEqualityBasedSetProperty<State> vertexStatesProperty() {
        return this.vertexStatesProperty;
    }

    @Override
    public ObjectProperty<Boolean> sortVerticesProperty() {
        return this.sortVerticesProperty;
    }

    @Override
    public ListProperty<PatternFacade> verticesSortPatternListProperty() {
        return this.verticesSortPatternListProperty;
    }
}