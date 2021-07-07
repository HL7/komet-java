package org.hl7.komet.framework.view;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import org.hl7.komet.terms.KometTerm;
import org.hl7.tinkar.coordinate.logic.LogicCoordinate;
import org.hl7.tinkar.coordinate.logic.LogicCoordinateRecord;
import org.hl7.tinkar.terms.ConceptFacade;
import org.hl7.tinkar.terms.PatternFacade;
import org.hl7.tinkar.terms.PatternProxy;

public class ObservableLogicCoordinateNoOverride extends ObservableLogicCoordinateBase  {

    public ObservableLogicCoordinateNoOverride(LogicCoordinate logicCoordinate, String coordinateName) {
        super(logicCoordinate, coordinateName);
    }

    public ObservableLogicCoordinateNoOverride(LogicCoordinate logicCoordinate) {
        super(logicCoordinate, "Logic coordinate");
    }

    @Override
    protected ObjectProperty<ConceptFacade> makeClassifierProperty(LogicCoordinate logicCoordinate) {
        return new SimpleEqualityBasedObjectProperty(this,
                KometTerm.CLASSIFIER_FOR_LOGIC_COORDINATE.toXmlFragment(),
                logicCoordinate.classifier());
    }

    @Override
    public void setExceptOverrides(LogicCoordinateRecord updatedCoordinate) {
        setValue(updatedCoordinate);
    }

    @Override
    protected ObjectProperty<PatternFacade> makeConceptMemberPattern(LogicCoordinate logicCoordinate) {
        return new SimpleEqualityBasedObjectProperty(this,
                KometTerm.CONCEPT_ASSEMBLAGE_FOR_LOGIC_COORDINATE.toXmlFragment(),
                PatternProxy.make(logicCoordinate.conceptMemberPatternNid()));
    }

    @Override
    protected ObjectProperty<ConceptFacade> makeDescriptionLogicProfileProperty(LogicCoordinate logicCoordinate) {
        return new SimpleEqualityBasedObjectProperty(this,
                KometTerm.DESCRIPTION_LOGIC_PROFILE_FOR_LOGIC_COORDINATE.toXmlFragment(),
                logicCoordinate.descriptionLogicProfile());
    }

    @Override
    protected ObjectProperty<PatternFacade> makeInferredAxiomPatternProperty(LogicCoordinate logicCoordinate) {
        return new SimpleEqualityBasedObjectProperty(this,
                KometTerm.INFERRED_ASSEMBLAGE_FOR_LOGIC_COORDINATE.toXmlFragment(),
                logicCoordinate.inferredAxiomsPattern());
    }

    @Override
    protected ObjectProperty<PatternFacade> makeStatedAxiomPatternProperty(LogicCoordinate logicCoordinate) {
        return new SimpleEqualityBasedObjectProperty(this,
                KometTerm.STATED_ASSEMBLAGE_FOR_LOGIC_COORDINATE.toXmlFragment(),
                logicCoordinate.statedAxiomsPattern());
    }

    @Override
    protected ObjectProperty<PatternFacade> makeInferredNavigationPatternProperty(LogicCoordinate logicCoordinate) {
        return new SimpleEqualityBasedObjectProperty(this,
                "Inferred navigation pattern (fix)",
                logicCoordinate.inferredNavigationPattern());
    }

    @Override
    protected ObjectProperty<PatternFacade> makeStatedNavigationPatternProperty(LogicCoordinate logicCoordinate) {
        return new SimpleEqualityBasedObjectProperty(this,
                "Stated navigation pattern (fix)",
                logicCoordinate.statedNavigationPattern());
    }

    @Override
    protected ObjectProperty<ConceptFacade> makeRootConceptProperty(LogicCoordinate logicCoordinate) {
        return new SimpleEqualityBasedObjectProperty(this,
                KometTerm.ROOT_FOR_LOGIC_COORDINATE.toXmlFragment(),
                logicCoordinate.root());
    }

    @Override
    protected LogicCoordinateRecord baseCoordinateChangedListenersRemoved(ObservableValue<? extends LogicCoordinateRecord> observable,
                                                                          LogicCoordinateRecord oldValue,
                                                                          LogicCoordinateRecord newValue) {
        this.classifierProperty().setValue(newValue.classifier());
        this.conceptMemberPatternProperty().setValue(newValue.conceptMemberPattern());
        this.descriptionLogicProfileProperty().setValue(newValue.descriptionLogicProfile());
        this.inferredAxiomsPatternProperty().setValue(newValue.inferredAxiomsPattern());
        this.statedAxiomsPatternProperty().setValue(newValue.statedAxiomsPattern());
        this.inferredNavigationPatternProperty().setValue(newValue.inferredNavigationPattern());
        this.statedNavigationPatternProperty().setValue(newValue.statedNavigationPattern());
        this.rootConceptProperty().setValue(newValue.root());
        return newValue;
    }

    @Override
    public LogicCoordinateRecord getOriginalValue() {
        return getValue();
    }
}
