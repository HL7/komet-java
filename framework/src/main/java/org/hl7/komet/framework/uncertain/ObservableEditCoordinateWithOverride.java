package org.hl7.komet.framework.uncertain;


import javafx.beans.value.ObservableValue;
import org.hl7.komet.framework.view.ObjectPropertyWithOverride;
import org.hl7.komet.framework.view.SimpleEqualityBasedObjectProperty;
import org.hl7.tinkar.coordinate.edit.EditCoordinate;
import org.hl7.tinkar.coordinate.edit.EditCoordinateImmutable;
import org.hl7.tinkar.terms.ConceptFacade;

public class ObservableEditCoordinateWithOverride
        extends ObservableEditCoordinateBase {

    //~--- constructors --------------------------------------------------------

    public ObservableEditCoordinateWithOverride(ObservableEditCoordinate editCoordinate) {
        this(editCoordinate, editCoordinate.getName());
    }

    /**
     * Instantiates a new observable edit coordinate impl.
     *
     * @param editCoordinate the edit coordinate
     */
    public ObservableEditCoordinateWithOverride(ObservableEditCoordinate editCoordinate, String coordinateName) {
        super(editCoordinate, coordinateName);
        if (editCoordinate instanceof ObservableEditCoordinateWithOverride) {
            throw new IllegalStateException("Cannot override an overridden Coordinate. ");
        }

    }

    @Override
    protected EditCoordinateImmutable baseCoordinateChangedListenersRemoved(ObservableValue<? extends EditCoordinateImmutable> observable, EditCoordinateImmutable oldValue, EditCoordinateImmutable newValue) {
        if (!this.authorForChangesProperty().isOverridden()) {
            this.authorForChangesProperty().setValue(newValue.getAuthorForChanges());
        }
        if (!this.defaultModuleProperty().isOverridden()) {
            this.defaultModuleProperty().setValue(newValue.getDefaultModule());
        }
        if (!this.destinationModuleProperty().isOverridden()) {
            this.destinationModuleProperty().setValue(newValue.getDestinationModule());
        }
        if (!this.defaultPathProperty().isOverridden()) {
            this.defaultPathProperty().setValue(newValue.getPromotionPath());
        }
        if (!this.promotionPathProperty().isOverridden()) {
            this.promotionPathProperty().setValue(newValue.getPromotionPath());
        }
        /*
int authorNid, int defaultModuleNid, int promotionPathNid, int destinationModuleNid
         */
        return EditCoordinateImmutable.make(this.authorForChangesProperty().get().nid(),
                this.defaultModuleProperty().get().nid(),
                this.destinationModuleProperty().get().nid(),
                this.defaultPathProperty().get().nid(),
                this.promotionPathProperty().get().nid()
        );
    }

    @Override
    public ObjectPropertyWithOverride<ConceptFacade> defaultPathProperty() {
        return (ObjectPropertyWithOverride<ConceptFacade>) super.promotionPathProperty();
    }

    @Override
    public void setExceptOverrides(EditCoordinateImmutable updatedCoordinate) {
        if (hasOverrides()) {
            ConceptFacade author = updatedCoordinate.getAuthorForChanges();
            if (authorForChangesProperty().isOverridden()) {
                author = authorForChangesProperty().get();
            }
            ;
            ConceptFacade defaultModule = updatedCoordinate.getDefaultModule();
            if (defaultModuleProperty().isOverridden()) {
                defaultModule = defaultModuleProperty().get();
            }
            ;
            ConceptFacade defaultPath = updatedCoordinate.getDefaultPath();
            if (defaultPathProperty().isOverridden()) {
                defaultPath = promotionPathProperty().get();
            }
            ;
            ConceptFacade promotionPath = updatedCoordinate.getPromotionPath();
            if (promotionPathProperty().isOverridden()) {
                promotionPath = promotionPathProperty().get();
            }
            ;
            ConceptFacade destinationModule = updatedCoordinate.getDestinationModule();
            if (destinationModuleProperty().isOverridden()) {
                destinationModule = destinationModuleProperty().get();
            }
            ;
            setValue(EditCoordinateImmutable.make(author, defaultModule, promotionPath, defaultPath, destinationModule));
        } else {
            setValue(updatedCoordinate);
        }
    }

    @Override
    public EditCoordinateImmutable getOriginalValue() {
        return EditCoordinateImmutable.make(authorForChangesProperty().getOriginalValue(),
                defaultModuleProperty().getOriginalValue(),
                destinationModuleProperty().getOriginalValue(),
                defaultPathProperty().getOriginalValue(),
                promotionPathProperty().getOriginalValue()
        );
    }

    @Override
    protected SimpleEqualityBasedObjectProperty<ConceptFacade> makeAuthorForChangesProperty(EditCoordinate editCoordinate) {
        ObservableEditCoordinate observableEditCoordinate = (ObservableEditCoordinate) editCoordinate;
        return new ObjectPropertyWithOverride<>(observableEditCoordinate.authorForChangesProperty(), this);
    }

    @Override
    protected SimpleEqualityBasedObjectProperty<ConceptFacade> makeDefaultModuleProperty(EditCoordinate editCoordinate) {
        ObservableEditCoordinate observableEditCoordinate = (ObservableEditCoordinate) editCoordinate;
        return new ObjectPropertyWithOverride<>(observableEditCoordinate.defaultModuleProperty(), this);
    }

    @Override
    protected SimpleEqualityBasedObjectProperty<ConceptFacade> makeDestinationModuleProperty(EditCoordinate editCoordinate) {
        ObservableEditCoordinate observableEditCoordinate = (ObservableEditCoordinate) editCoordinate;
        return new ObjectPropertyWithOverride<>(observableEditCoordinate.destinationModuleProperty(), this);
    }

    @Override
    protected SimpleEqualityBasedObjectProperty<ConceptFacade> makeDefaultPathProperty(EditCoordinate editCoordinate) {
        ObservableEditCoordinate observableEditCoordinate = (ObservableEditCoordinate) editCoordinate;
        return new ObjectPropertyWithOverride<>(observableEditCoordinate.defaultPathProperty(), this);
    }

    @Override
    protected SimpleEqualityBasedObjectProperty<ConceptFacade> makePromotionPathProperty(EditCoordinate editCoordinate) {
        ObservableEditCoordinate observableEditCoordinate = (ObservableEditCoordinate) editCoordinate;
        return new ObjectPropertyWithOverride<>(observableEditCoordinate.promotionPathProperty(), this);
    }

    @Override
    public ObjectPropertyWithOverride<ConceptFacade> authorForChangesProperty() {
        return (ObjectPropertyWithOverride<ConceptFacade>) super.authorForChangesProperty();
    }

    @Override
    public ObjectPropertyWithOverride<ConceptFacade> defaultModuleProperty() {
        return (ObjectPropertyWithOverride<ConceptFacade>) super.defaultModuleProperty();
    }

    @Override
    public ObjectPropertyWithOverride<ConceptFacade> destinationModuleProperty() {
        return (ObjectPropertyWithOverride<ConceptFacade>) super.destinationModuleProperty();
    }

    @Override
    public ObjectPropertyWithOverride<ConceptFacade> promotionPathProperty() {
        return (ObjectPropertyWithOverride<ConceptFacade>) super.promotionPathProperty();
    }
}

