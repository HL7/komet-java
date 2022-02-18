package org.hl7.komet.classification;

import au.csiro.ontology.classification.IReasoner;
import org.hl7.tinkar.common.service.TinkExecutor;
import org.hl7.tinkar.common.service.TrackingCallable;
import org.hl7.tinkar.coordinate.view.calculator.ViewCalculator;
import org.hl7.tinkar.terms.PatternFacade;

import java.util.concurrent.Future;

public class RunReasonerTask extends TrackingCallable<AxiomData> {
    final ViewCalculator viewCalculator;
    final PatternFacade axiomPattern;

    public RunReasonerTask(ViewCalculator viewCalculator, PatternFacade axiomPattern) {
        super(true, true);
        this.viewCalculator = viewCalculator;
        this.axiomPattern = axiomPattern;
        updateTitle("Running reasoner: " + viewCalculator.getPreferredDescriptionTextWithFallbackOrNid(axiomPattern));
        updateProgress(0, 3);
    }

    @Override
    protected AxiomData compute() throws Exception {
        ExtractAxiomsTask extractAxiomsTask = new ExtractAxiomsTask(this.viewCalculator, this.axiomPattern);
        updateMessage("Step 1: " + viewCalculator.getPreferredDescriptionTextWithFallbackOrNid(axiomPattern));
        Future<AxiomData> axiomDataFuture = TinkExecutor.threadPool().submit(extractAxiomsTask);
        AxiomData axiomData = axiomDataFuture.get();
        updateProgress(1, 3);
        updateMessage("Step 2: Loading axioms into reasoner");
        LoadAxiomsTask loadAxiomsTask = new LoadAxiomsTask(axiomData);
        Future<IReasoner> reasonerFuture = TinkExecutor.threadPool().submit(loadAxiomsTask);
        IReasoner reasoner = reasonerFuture.get();
        updateProgress(2, 3);
        updateMessage("Step 3: Classifying axioms");
        ClassifyTask classifyTask = new ClassifyTask(reasoner);
        Future<Void> classifyFuture = TinkExecutor.threadPool().submit(classifyTask);
        classifyFuture.get();
        updateProgress(3, 3);
        updateMessage("Reasoner complete in " + durationString());
        return axiomData;
    }
}
