package org.hl7.komet.view;

import javafx.beans.value.ObservableValue;
import org.eclipse.collections.api.set.ImmutableSet;
import org.hl7.tinkar.common.id.IntIdList;
import org.hl7.tinkar.common.id.IntIdSet;
import org.hl7.tinkar.common.id.IntIds;
import org.hl7.tinkar.coordinate.stamp.StampCoordinateRecord;
import org.hl7.tinkar.coordinate.stamp.StampCoordinate;
import org.hl7.tinkar.coordinate.stamp.StampPositionRecord;
import org.hl7.tinkar.coordinate.stamp.StateSet;
import org.hl7.tinkar.terms.ConceptFacade;
import org.hl7.tinkar.terms.ConceptProxy;

public class ObservableStampCoordinateWithOverride extends ObservableStampCoordinateBase {

    public ObservableStampCoordinateWithOverride(ObservableStampCoordinate stampFilter) {
        this(stampFilter, stampFilter.getName());
    }

    public ObservableStampCoordinateWithOverride(ObservableStampCoordinate stampFilter, String coordinateName) {
        super(stampFilter, coordinateName);
        if (stampFilter instanceof ObservableStampCoordinateWithOverride) {
            throw new IllegalStateException("Cannot override an overridden Coordinate. ");
        }
    }

    @Override
    public LongPropertyWithOverride timeProperty() {
        return (LongPropertyWithOverride) super.timeProperty();
    }

    @Override
    public ObjectPropertyWithOverride<ConceptFacade> pathConceptProperty() {
        return (ObjectPropertyWithOverride) super.pathConceptProperty();
    }

    @Override
    public SetPropertyWithOverride<ConceptFacade> moduleSpecificationsProperty() {
        return (SetPropertyWithOverride) super.moduleSpecificationsProperty();
    }

    @Override
    public SetPropertyWithOverride<ConceptFacade> excludedModuleSpecificationsProperty() {
        return (SetPropertyWithOverride) super.excludedModuleSpecificationsProperty();
    }

    @Override
    public ListPropertyWithOverride<ConceptFacade> modulePriorityOrderProperty() {
        return (ListPropertyWithOverride) super.modulePriorityOrderProperty();
    }

    @Override
    public ObjectPropertyWithOverride<StateSet> allowedStatusProperty() {
        return (ObjectPropertyWithOverride) super.allowedStatusProperty();
    }

    @Override
    public void setExceptOverrides(StampCoordinateRecord updatedCoordinate) {
        if (this.hasOverrides()) {
            long time = updatedCoordinate.time();
            if (timeProperty().isOverridden()) {
                time = time();
            }
            int pathConceptNid = updatedCoordinate.pathNidForFilter();
            if (pathConceptProperty().isOverridden()) {
                pathConceptNid = pathConceptProperty().get().nid();
            }
            IntIdSet moduleSpecificationNids = updatedCoordinate.moduleNids();
            if (moduleSpecificationsProperty().isOverridden()) {
                moduleSpecificationNids = moduleNids();
            }
            IntIdSet moduleExclusionNids = updatedCoordinate.excludedModuleNids();
            if (excludedModuleSpecificationsProperty().isOverridden()) {
                moduleExclusionNids = excludedModuleNids();
            }
            IntIdList modulePriorityOrder = updatedCoordinate.modulePriorityNidList();
            if (modulePriorityOrderProperty().isOverridden()) {
                modulePriorityOrder = modulePriorityNidList();
            }
            StateSet StateSet = updatedCoordinate.allowedStates();
            if (allowedStatusProperty().isOverridden()) {
                StateSet = allowedStates();
            }
            setValue(StampCoordinateRecord.make(StateSet,
                    StampPositionRecord.make(time, pathConceptNid),
                    moduleSpecificationNids,
                    moduleExclusionNids,
                    modulePriorityOrder));

        } else {
            setValue(updatedCoordinate);
        }
    }

    @Override
    protected ListPropertyWithOverride<ConceptFacade> makeModulePriorityOrderProperty(StampCoordinate stampCoordinate) {
        ObservableStampCoordinate observableStampFilter = (ObservableStampCoordinate) stampCoordinate;
        return new ListPropertyWithOverride<>(observableStampFilter.modulePriorityOrderProperty(), this);
    }

    @Override
    protected ObjectPropertyWithOverride makeAllowedStatusProperty(StampCoordinate stampCoordinate) {
        ObservableStampCoordinate observableStampFilter = (ObservableStampCoordinate) stampCoordinate;
        return new ObjectPropertyWithOverride<>(observableStampFilter.allowedStatusProperty(), this);
    }

    @Override
    protected SetPropertyWithOverride<ConceptFacade> makeExcludedModuleSpecificationsProperty(StampCoordinate stampCoordinate) {
        ObservableStampCoordinate observableStampFilter = (ObservableStampCoordinate) stampCoordinate;
        return new SetPropertyWithOverride<>(observableStampFilter.excludedModuleSpecificationsProperty(), this);
    }

    @Override
    protected SetPropertyWithOverride<ConceptFacade> makeModuleSpecificationsProperty(StampCoordinate stampCoordinate) {
        ObservableStampCoordinate observableStampFilter = (ObservableStampCoordinate) stampCoordinate;
        return new SetPropertyWithOverride<>(observableStampFilter.moduleSpecificationsProperty(), this);
    }

    @Override
    protected LongPropertyWithOverride makeTimeProperty(StampCoordinate stampCoordinate) {
        ObservableStampCoordinate observableStampFilter = (ObservableStampCoordinate) stampCoordinate;
        return new LongPropertyWithOverride(observableStampFilter.timeProperty(), this);
    }

    @Override
    protected ObjectPropertyWithOverride<ConceptFacade> makePathConceptProperty(StampCoordinate stampCoordinate) {
        ObservableStampCoordinate observableStampFilter = (ObservableStampCoordinate) stampCoordinate;
        return new ObjectPropertyWithOverride<>(observableStampFilter.pathConceptProperty(), this);
    }

    @Override
    public StampCoordinateRecord getOriginalValue() {
        return StampCoordinateRecord.make(allowedStatusProperty().getOriginalValue(),
                StampPositionRecord.make(timeProperty().getOriginalValue().longValue(),
                        pathConceptProperty().getOriginalValue()),
                IntIds.set.of(moduleSpecificationsProperty().getOriginalValue().stream().mapToInt(value -> value.nid()).toArray()),
                IntIds.set.of(excludedModuleSpecificationsProperty().getOriginalValue().stream().mapToInt(value -> value.nid()).toArray()),
                IntIds.list.of(modulePriorityOrderProperty().getOriginalValue().stream().mapToInt(value -> value.nid()).toArray()));
    }


    @Override
    protected StampCoordinateRecord baseCoordinateChangedListenersRemoved(
            ObservableValue<? extends StampCoordinateRecord> observable,
            StampCoordinateRecord oldValue, StampCoordinateRecord newValue) {
        if (!this.pathConceptProperty().isOverridden()) {
            this.pathConceptProperty().setValue(newValue.pathForFilter());
        }

        if (!this.timeProperty().isOverridden()) {
            this.timeProperty().set(newValue.stampPosition().time());
        }

        if (!this.modulePriorityOrderProperty().isOverridden()) {
            this.modulePriorityOrderProperty().setAll(newValue.modulePriorityNidList().map(nid -> ConceptProxy.make(nid)).castToList());
        }

        if (!this.allowedStatusProperty().isOverridden()) {
            if (newValue.allowedStates() != this.allowedStatusProperty().get()) {
                this.allowedStatusProperty().setValue(newValue.allowedStates());
            }
        }

        if (!this.excludedModuleSpecificationsProperty().isOverridden()) {
            ImmutableSet<ConceptFacade> excludedModuleSet = newValue.moduleNids().map(nid -> ConceptProxy.make(nid));
            if (!excludedModuleSet.equals(this.excludedModuleSpecificationsProperty().get())) {
                this.excludedModuleSpecificationsProperty().setAll(excludedModuleSet.castToSet());
            }
        }
        if (!this.moduleSpecificationsProperty().isOverridden()) {
            ImmutableSet<ConceptFacade> moduleSet = newValue.moduleNids().map(nid -> ConceptProxy.make(nid));
            if (!moduleSet.equals(this.moduleSpecificationsProperty().get())) {
                this.moduleSpecificationsProperty().setAll(moduleSet.castToSet());
            }
        }
        return StampCoordinateRecord.make(allowedStatusProperty().get(),
                StampPositionRecord.make(timeProperty().get(),
                        pathConceptProperty().get().nid()),
                IntIds.set.of(moduleSpecificationsProperty().stream().mapToInt(value -> value.nid()).toArray()),
                IntIds.set.of(excludedModuleSpecificationsProperty().stream().mapToInt(value -> value.nid()).toArray()),
                IntIds.list.of(modulePriorityOrderProperty().getOriginalValue().stream().mapToInt(value -> value.nid()).toArray()));
    }

}
