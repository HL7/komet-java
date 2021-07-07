package org.hl7.komet.framework.view;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.hl7.tinkar.coordinate.language.LanguageCoordinateRecord;
import org.hl7.tinkar.coordinate.logic.LogicCoordinateRecord;
import org.hl7.tinkar.coordinate.navigation.NavigationCoordinateRecord;
import org.hl7.tinkar.coordinate.stamp.StampCoordinateRecord;
import org.hl7.tinkar.coordinate.stamp.StateSet;
import org.hl7.tinkar.coordinate.view.ViewCoordinate;
import org.hl7.tinkar.coordinate.view.ViewCoordinateRecord;
import org.hl7.tinkar.coordinate.view.calculator.ViewCalculator;
import org.hl7.tinkar.coordinate.view.calculator.ViewCalculatorWithCache;
import org.hl7.tinkar.terms.ConceptFacade;

public abstract class ObservableViewBase
        extends ObservableCoordinateAbstract<ViewCoordinateRecord>
        implements ObservableView {

    protected final SimpleBooleanProperty listening = new SimpleBooleanProperty(this, "Listening for changes", false);

    protected final ObservableStampCoordinateBase stampCoordinateObservable;

    protected final ListProperty<ObservableLanguageCoordinateBase> languageCoordinates;

    protected final ObservableNavigationCoordinateBase navigationCoordinateObservable;

    protected final ObservableLogicCoordinateBase logicCoordinateObservable;
    /**
     * Note that if you don't declare a listener as final in this way, and just use method references, or
     * a direct lambda expression, you will not be able to remove the listener, since each method reference will create
     * a new object, and they won't compare equal using object identity.
     * https://stackoverflow.com/questions/42146360/how-do-i-remove-lambda-expressions-method-handles-that-are-used-as-listeners
     */
    private final ChangeListener<StampCoordinateRecord> stampChangeListener = this::stampChanged;
    private final ChangeListener<NavigationCoordinateRecord> navigationChangedListener = this::navigationChanged;
    private final ListChangeListener<ObservableLanguageCoordinateBase> languageCoordinateListener = this::languageChanged;
    private final ChangeListener<LogicCoordinateRecord> logicCoordinateListener = this::logicChanged;

    private ViewCalculator viewCalculator;

    //~--- constructors --------------------------------------------------------
    public ObservableViewBase(ViewCoordinate viewRecord, String name) {
        super(viewRecord.toViewCoordinateRecord(), name);
        this.stampCoordinateObservable = makeStampCoordinateObservable(viewRecord);
        this.navigationCoordinateObservable = makeNavigationCoordinateObservable(viewRecord);
        this.languageCoordinates = makeLanguageCoordinateListProperty(viewRecord);
        this.logicCoordinateObservable = makeLogicCoordinateObservable(viewRecord);
        addListeners();
        this.viewCalculator = ViewCalculatorWithCache.getCalculator(viewRecord.toViewCoordinateRecord());
        addListener((observable, oldValue, newValue) -> {
            this.viewCalculator = ViewCalculatorWithCache.getCalculator(newValue);
        });
    }

    /**
     * Instantiates a new observable taxonomy coordinate impl.
     *
     * @param viewRecord the taxonomy coordinate
     */
    public ObservableViewBase(ViewCoordinate viewRecord) {
        this(viewRecord, "View");
    }

    @Override
    public ViewCalculator calculator() {
        return ViewCalculatorWithCache.getCalculator(getValue());
    }

    protected abstract ObservableStampCoordinateBase makeStampCoordinateObservable(ViewCoordinate viewRecord);

    @Override
    public ImmutableList<LanguageCoordinateRecord> languageCoordinateList() {
        return getValue().languageCoordinateList();
    }

    @Override
    public ViewCoordinateRecord toViewCoordinateRecord() {
        return getValue();
    }

    @Override
    public void setViewPath(ConceptFacade pathConcept) {
        this.removeListeners();
        StampCoordinateRecord newStampCoordinate = stampCoordinate().getValue().withPath(pathConcept);

        this.stampCoordinate().pathConceptProperty().set(pathConcept);
        MutableList<LanguageCoordinateRecord> languageCoordinateRecords = Lists.mutable.empty();
        languageCoordinates.forEach(observableLanguageCoordinateBase -> languageCoordinateRecords.add(observableLanguageCoordinateBase.getValue()));
        ViewCoordinateRecord viewRecord = new ViewCoordinateRecord(
                this.stampCoordinate().toStampCoordinateRecord(),
                languageCoordinateRecords.toImmutable(),
                this.logicCoordinate().toLogicCoordinateRecord(),
                this.navigationCoordinate().toNavigationCoordinateRecord());
        this.addListeners();
        this.setValue(viewRecord);
    }

    @Override
    public void setAllowedStates(StateSet stateSet) {
        ViewCoordinateRecord newView = getValue().withStampCoordinate(stampCoordinate().getValue().withAllowedStates(stateSet));
        newView = newView.withNavigationCoordinate(navigationCoordinate().getValue().withVertexStates(stateSet));

        this.setValue(newView);
    }

    @Override
    protected void addListeners() {
        this.stampCoordinateObservable.addListener(this.stampChangeListener);
        this.navigationCoordinateObservable.addListener(this.navigationChangedListener);
        this.languageCoordinates.addListener(this.languageCoordinateListener);
        this.logicCoordinateObservable.addListener(this.logicCoordinateListener);
        listening.set(true);
    }

    @Override
    protected void removeListeners() {
        this.stampCoordinateObservable.removeListener(this.stampChangeListener);
        this.navigationCoordinateObservable.removeListener(this.navigationChangedListener);
        this.languageCoordinates.removeListener(this.languageCoordinateListener);
        this.logicCoordinateObservable.removeListener(this.logicCoordinateListener);
        listening.set(false);
    }

    protected abstract ObservableNavigationCoordinateBase makeNavigationCoordinateObservable(ViewCoordinate viewRecord);

    protected abstract ListProperty<ObservableLanguageCoordinateBase> makeLanguageCoordinateListProperty(ViewCoordinate viewRecord);

    protected abstract ObservableLogicCoordinateBase makeLogicCoordinateObservable(ViewCoordinate viewRecord);

    //~--- methods -------------------------------------------------------------


    private void languageChanged(ListChangeListener.Change<? extends ObservableLanguageCoordinateBase> c) {
        MutableList<LanguageCoordinateRecord> languageRecordList = Lists.mutable.empty();
        c.getList().forEach(observableLanguageCoordinateBase -> languageRecordList.add(observableLanguageCoordinateBase.getValue()));
        this.setValue(this.getValue().withLanguageCoordinateList(languageRecordList.toImmutable()));
    }

    private void stampChanged(ObservableValue<? extends StampCoordinateRecord> observable,
                              StampCoordinateRecord oldValue,
                              StampCoordinateRecord newValue) {
        this.setValue(getValue().withStampCoordinate(newValue.toStampCoordinateRecord()));
    }

    private void navigationChanged(ObservableValue<? extends NavigationCoordinateRecord> observable,
                                   NavigationCoordinateRecord oldValue,
                                   NavigationCoordinateRecord newValue) {
        this.setValue(getValue().withNavigationCoordinate(newValue.toNavigationCoordinateRecord()));
    }

    private void logicChanged(ObservableValue<? extends LogicCoordinateRecord> observable,
                              LogicCoordinateRecord oldValue,
                              LogicCoordinateRecord newValue) {
        this.setValue(getValue().withLogicCoordinate(newValue.toLogicCoordinateRecord()));
    }

    @Override
    public ObservableLogicCoordinate logicCoordinate() {
        return this.logicCoordinateObservable;
    }

    @Override
    public ObservableNavigationCoordinate navigationCoordinate() {
        return this.navigationCoordinateObservable;
    }

    @Override
    public ListProperty<ObservableLanguageCoordinateBase> languageCoordinates() {
        return this.languageCoordinates;
    }

    public ViewCoordinateRecord toViewRecord() {
        return this.getValue();
    }

    @Override
    public ObservableStampCoordinate stampCoordinate() {
        return this.stampCoordinateObservable;
    }

    @Override
    public ViewCoordinate getViewCoordinate() {
        return this;
    }

    @Override
    public Iterable<ObservableLanguageCoordinateBase> languageCoordinateIterable() {
        return this.languageCoordinates;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{\n" +
                getValue().toString() +
                "\n}";
    }
}