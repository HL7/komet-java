package org.hl7.komet.reasoner;

import au.csiro.ontology.classification.IReasoner;
import org.hl7.tinkar.common.service.TrackingCallable;

public class ComputeTaxonomyTask extends TrackingCallable<Void> {
    final IReasoner reasoner;

    public ComputeTaxonomyTask(IReasoner reasoner) {
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