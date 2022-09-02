package org.hl7.komet.reasoner.sorocket;

import au.csiro.ontology.classification.IReasoner;
import org.hl7.tinkar.common.service.TrackingCallable;

public class ComputeSnorocketInterencesTask extends TrackingCallable<Void> {
    final IReasoner reasoner;

    public ComputeSnorocketInterencesTask(IReasoner reasoner) {
        super(false, true);
        this.reasoner = reasoner;
        updateTitle("Computing taxonomy. ");
    }

    @Override
    protected Void compute() throws Exception {
        this.reasoner.classify(this);
        updateMessage("Computed taxonomy in " + durationString());
        return null;
    }
}