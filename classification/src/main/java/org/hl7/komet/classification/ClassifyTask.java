package org.hl7.komet.classification;

import au.csiro.ontology.classification.IReasoner;
import org.hl7.tinkar.common.service.TrackingCallable;

public class ClassifyTask extends TrackingCallable<Void> {
    final IReasoner reasoner;

    public ClassifyTask(IReasoner reasoner) {
        super(false, true);
        this.reasoner = reasoner;
        updateTitle("Classifying axioms. ");
    }

    @Override
    protected Void compute() throws Exception {
        this.reasoner.classify(this);
        updateMessage("Classify in " + durationString());
        return null;
    }
}