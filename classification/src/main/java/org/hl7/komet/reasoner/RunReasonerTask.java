package org.hl7.komet.reasoner;

import au.csiro.ontology.classification.IReasoner;
import org.hl7.tinkar.common.service.TinkExecutor;
import org.hl7.tinkar.common.service.TrackingCallable;
import org.hl7.tinkar.coordinate.view.calculator.ViewCalculator;
import org.hl7.tinkar.terms.PatternFacade;

import java.util.concurrent.Future;

/**
 * Reasoning Tasks
 * <p>
 * Reasoning tasks are: axiom consistency, class satisfiability, classification
 * <p>
 * Consistency: this is the task of ensuring that the set of axioms is free of contradictions. For example, consider the following two facts: 1) all birds can fly and 2) penguins are birds which cannot fly. This would trigger an inconsistency because if a penguin is a bird then it can fly but another fact is stating otherwise.
 * <p>
 * Class Satisfiability: this is the task of determining whether it is possible for a class to have instances without causing inconsistency. For example, if we say that students can either be good OR bad, then the class GoodAndBadStudent would be unsatisfiable (i.e., it cannot have instances if the set of axioms are to be consistent). Typically, we are interested in modeling classes that can have at least one instance; therefore, having unsatisfiable classes usually suggests a modeling error.
 * <p>
 * Compute taxonomy (aka Classification): this is the task of determining the subclass relationships between classes in order to complete the class hierarchy. For example, .. (left for the reader :P)
 */
public class RunReasonerTask extends TrackingCallable<AxiomData> {
    final ViewCalculator viewCalculator;
    final PatternFacade statedAxiomPattern;
    final PatternFacade inferredAxiomPattern;

    public RunReasonerTask(ViewCalculator viewCalculator, PatternFacade statedAxiomPattern,
                           PatternFacade inferredAxiomPattern) {
        super(true, true);
        this.viewCalculator = viewCalculator;
        this.statedAxiomPattern = statedAxiomPattern;
        this.inferredAxiomPattern = inferredAxiomPattern;
        updateTitle("Running reasoner: " + viewCalculator.getPreferredDescriptionTextWithFallbackOrNid(statedAxiomPattern));
        updateProgress(0, 4);
    }

    @Override
    protected AxiomData compute() throws Exception {
        final int maxWork = 4;
        int workDone = 1;
        ExtractAxiomsTask extractAxiomsTask = new ExtractAxiomsTask(this.viewCalculator, this.statedAxiomPattern);
        updateMessage("Step " + workDone +
                ": " + viewCalculator.getPreferredDescriptionTextWithFallbackOrNid(statedAxiomPattern));
        Future<AxiomData> axiomDataFuture = TinkExecutor.threadPool().submit(extractAxiomsTask);
        AxiomData axiomData = axiomDataFuture.get();
        updateProgress(workDone++, maxWork);
        updateMessage("Step " + workDone +
                ": Loading axioms into reasoner");
        LoadAxiomsTask loadAxiomsTask = new LoadAxiomsTask(axiomData);
        Future<IReasoner> reasonerFuture = TinkExecutor.threadPool().submit(loadAxiomsTask);
        IReasoner reasoner = reasonerFuture.get();
        updateProgress(workDone++, maxWork);
        updateMessage("Step " + workDone +
                ": Computing taxonomy");
        ComputeTaxonomyTask classifyTask = new ComputeTaxonomyTask(reasoner);
        Future<Void> classifyFuture = TinkExecutor.threadPool().submit(classifyTask);
        classifyFuture.get();
        updateProgress(workDone++, maxWork);
        updateMessage("Step " + workDone +
                ": Processing results");
        ProcessResultsTask processResultsTask = new ProcessResultsTask(reasoner, this.viewCalculator, this.inferredAxiomPattern,
                axiomData);
        Future<Void> processResultsFuture = TinkExecutor.threadPool().submit(processResultsTask);
        processResultsFuture.get();
        updateProgress(workDone++, maxWork);

        updateMessage("Reasoner run complete in " + durationString());
        return axiomData;
    }
}