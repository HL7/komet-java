package org.hl7.komet.view.uncertain;

import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import org.hl7.komet.view.LongPropertyWithOverride;
import org.hl7.komet.view.ObjectPropertyWithOverride;
import org.hl7.tinkar.coordinate.stamp.StampPosition;
import org.hl7.tinkar.coordinate.stamp.StampPositionRecord;
import org.hl7.tinkar.terms.ConceptFacade;

public class ObservableStampPositionWithOverride
        extends ObservableStampPositionBase {

    //~--- constructors --------------------------------------------------------

    /**
     * Instantiates a new observable stamp position impl.
     *
     * @param stampPosition the stamp position
     */
    public ObservableStampPositionWithOverride(ObservableStampPosition stampPosition, String coordinateName) {
        super(stampPosition, coordinateName);
        if (stampPosition instanceof ObservableStampPositionWithOverride) {
            throw new IllegalStateException("Cannot override an overridden Coordinate. ");
        }
    }
    public ObservableStampPositionWithOverride(ObservableStampPosition stampPosition) {
        this(stampPosition, stampPosition.getName());
    }

    @Override
    public void setExceptOverrides(StampPositionRecord updatedCoordinate) {
        int pathConceptNid = updatedCoordinate.getPathForPositionNid();
        if (pathConceptProperty().isOverridden()) {
            pathConceptNid = pathConceptProperty().get().nid();
        }
        long time = updatedCoordinate.time();
        if (timeProperty().isOverridden()) {
            time = timeProperty().get();
        }
        setValue(StampPositionRecord.make(time, pathConceptNid));
    }

    @Override
    public ObjectPropertyWithOverride<ConceptFacade> pathConceptProperty() {
        return (ObjectPropertyWithOverride<ConceptFacade>) super.pathConceptProperty();
    }

    @Override
    public LongPropertyWithOverride timeProperty() {
        return (LongPropertyWithOverride) super.timeProperty();
    }

    protected ObjectProperty<ConceptFacade> makePathConceptProperty(StampPosition stampPosition) {
        ObservableStampPosition observableStampPosition = (ObservableStampPosition) stampPosition;
        return new ObjectPropertyWithOverride<>(observableStampPosition.pathConceptProperty(), this);
    }

    protected LongProperty makeTimeProperty(StampPosition stampPosition) {
        ObservableStampPosition observableStampPosition = (ObservableStampPosition) stampPosition;
        return new LongPropertyWithOverride(observableStampPosition.timeProperty(), this);
    }

    @Override
    public StampPositionRecord getOriginalValue() {
        return StampPositionRecord.make(timeProperty().getOriginalValue().longValue(), pathConceptProperty().getOriginalValue());
    }


    @Override
    protected StampPositionRecord baseCoordinateChangedListenersRemoved(ObservableValue<? extends StampPositionRecord> observable,
                                                                        StampPositionRecord oldValue, StampPositionRecord newValue) {
        if (!this.pathConceptProperty().isOverridden()) {
            this.pathConceptProperty().setValue(newValue.getPathForPositionConcept());
        }
        if (!this.timeProperty().isOverridden()) {
            this.timeProperty().set(newValue.time());
        }

        return StampPositionRecord.make(timeProperty().longValue(), pathConceptProperty().get().nid());
    }

}
