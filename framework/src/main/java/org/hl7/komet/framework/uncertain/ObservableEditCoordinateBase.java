package org.hl7.komet.framework.uncertain;


import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.hl7.komet.framework.view.ObservableCoordinateAbstract;
import org.hl7.komet.framework.view.SimpleEqualityBasedObjectProperty;
import org.hl7.tinkar.coordinate.edit.EditCoordinate;
import org.hl7.tinkar.coordinate.edit.EditCoordinateImmutable;
import org.hl7.tinkar.terms.ConceptFacade;

public abstract class ObservableEditCoordinateBase extends ObservableCoordinateAbstract<EditCoordinateImmutable>
        implements ObservableEditCoordinate {
    /**
     * The author property.
     */
    private final SimpleEqualityBasedObjectProperty<ConceptFacade> authorForChangesProperty;

    /**
     * The default module property.
     */
    private final SimpleEqualityBasedObjectProperty<ConceptFacade> defaultModuleProperty;

    /**
     * The promotion module property.
     */
    private final SimpleEqualityBasedObjectProperty<ConceptFacade> destinationModuleProperty;

    /**
     * The path property.
     */
    private final SimpleEqualityBasedObjectProperty<ConceptFacade> defaultPathProperty;

    /**
     * The path property.
     */
    private final SimpleEqualityBasedObjectProperty<ConceptFacade> promotionPathProperty;

    /**
     * Note that if you don't declare a listener as final in this way, and just use method references, or
     * a direct lambda expression, you will not be able to remove the listener, since each method reference will create
     * a new object, and they won't compare equal using object identity.
     * https://stackoverflow.com/questions/42146360/how-do-i-remove-lambda-expressions-method-handles-that-are-used-as-listeners
     */
    private final ChangeListener<ConceptFacade> authorForChangesListener = this::authorForChangesConceptChanged;
    private final ChangeListener<ConceptFacade> defaultModuleListener = this::defaultModuleConceptChanged;
    private final ChangeListener<ConceptFacade> destinationModuleListener = this::destinationModuleConceptChanged;
    private final ChangeListener<ConceptFacade> defaultPathListener = this::defaultPathConceptChanged;
    private final ChangeListener<ConceptFacade> promotionPathListener = this::promotionPathConceptChanged;

    //~--- constructors --------------------------------------------------------

    /**
     * Instantiates a new observable edit coordinate impl.
     *
     * @param editCoordinate the edit coordinate
     */
    public ObservableEditCoordinateBase(EditCoordinate editCoordinate, String coordinateName) {
        super(editCoordinate.toEditCoordinateImmutable(), "Edit coordinate");
        this.authorForChangesProperty = makeAuthorForChangesProperty(editCoordinate);
        this.defaultModuleProperty = makeDefaultModuleProperty(editCoordinate);
        this.destinationModuleProperty = makeDestinationModuleProperty(editCoordinate);
        this.defaultPathProperty = makeDefaultPathProperty(editCoordinate);
        this.promotionPathProperty = makePromotionPathProperty(editCoordinate);
        addListeners();
    }

    protected abstract SimpleEqualityBasedObjectProperty<ConceptFacade> makeAuthorForChangesProperty(EditCoordinate editCoordinate);

    protected abstract SimpleEqualityBasedObjectProperty<ConceptFacade> makeDefaultModuleProperty(EditCoordinate editCoordinate);

    protected abstract SimpleEqualityBasedObjectProperty<ConceptFacade> makeDestinationModuleProperty(EditCoordinate editCoordinate);

    protected abstract SimpleEqualityBasedObjectProperty<ConceptFacade> makeDefaultPathProperty(EditCoordinate editCoordinate);

    protected abstract SimpleEqualityBasedObjectProperty<ConceptFacade> makePromotionPathProperty(EditCoordinate editCoordinate);

    protected void removeListeners() {
        this.authorForChangesProperty.removeListener(this.authorForChangesListener);
        this.defaultModuleProperty.removeListener(this.defaultModuleListener);
        this.destinationModuleProperty.removeListener(this.destinationModuleListener);
        this.defaultPathProperty.removeListener(this.defaultPathListener);
        this.promotionPathProperty.removeListener(this.promotionPathListener);
    }

    protected void addListeners() {
        this.authorForChangesProperty.addListener(this.authorForChangesListener);
        this.defaultModuleProperty.addListener(this.defaultModuleListener);
        this.destinationModuleProperty.addListener(this.destinationModuleListener);
        this.defaultPathProperty.addListener(this.defaultPathListener);
        this.promotionPathProperty.addListener(this.promotionPathListener);
    }

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "ObservableEditCoordinateImpl{" + this.getValue().toString() + '}';
    }

    private void promotionPathConceptChanged(ObservableValue<? extends ConceptFacade> observable,
                                             ConceptFacade old,
                                             ConceptFacade newPathConcept) {
        this.setValue(EditCoordinateImmutable.make(getAuthorNidForChanges(),
                getDefaultModuleNid(),
                getDestinationModuleNid(),
                getDefaultPathNid(),
                newPathConcept.nid()
        ));
    }


    private void defaultPathConceptChanged(ObservableValue<? extends ConceptFacade> observable,
                                           ConceptFacade old,
                                           ConceptFacade newPathConcept) {
        this.setValue(EditCoordinateImmutable.make(getAuthorNidForChanges(),
                getDefaultModuleNid(),
                getDestinationModuleNid(),
                newPathConcept.nid(),
                getPromotionPathNid()
        ));
    }

    private void authorForChangesConceptChanged(ObservableValue<? extends ConceptFacade> observable,
                                                ConceptFacade oldAuthorConcept,
                                                ConceptFacade newAuthorConcept) {
        this.setValue(EditCoordinateImmutable.make(newAuthorConcept.nid(),
                getDefaultModuleNid(),
                getDestinationModuleNid(),
                getDefaultModuleNid(),
                getPromotionPathNid()
        ));
    }

    private void defaultModuleConceptChanged(ObservableValue<? extends ConceptFacade> observable,
                                             ConceptFacade old,
                                             ConceptFacade newModuleConcept) {
        this.setValue(EditCoordinateImmutable.make(getAuthorNidForChanges(),
                newModuleConcept.nid(),
                getDestinationModuleNid(),
                getDefaultModuleNid(),
                getPromotionPathNid()
        ));
    }

    private void destinationModuleConceptChanged(ObservableValue<? extends ConceptFacade> observable,
                                                 ConceptFacade old,
                                                 ConceptFacade newModuleConcept) {
        this.setValue(EditCoordinateImmutable.make(getAuthorNidForChanges(),
                getDefaultModuleNid(),
                newModuleConcept.nid(),
                getDefaultModuleNid(),
                getPromotionPathNid()
        ));
    }

    @Override
    public ObjectProperty<ConceptFacade> authorForChangesProperty() {
        return this.authorForChangesProperty;
    }

    @Override
    public ObjectProperty<ConceptFacade> defaultModuleProperty() {
        return this.defaultModuleProperty;
    }

    @Override
    public ObjectProperty<ConceptFacade> destinationModuleProperty() {
        return this.destinationModuleProperty;
    }

    @Override
    public ObjectProperty<ConceptFacade> defaultPathProperty() {
        return this.defaultPathProperty;
    }

    @Override
    public ObjectProperty<ConceptFacade> promotionPathProperty() {
        return this.promotionPathProperty;
    }

    @Override
    public EditCoordinate getEditCoordinate() {
        return this.getValue();
    }

}
