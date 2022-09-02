package org.hl7.komet.reasoner.sorocket;

import au.csiro.ontology.classification.IReasoner;
import org.hl7.komet.reasoner.AxiomData;
import org.hl7.komet.reasoner.ClassifierResults;
import org.hl7.komet.reasoner.sorocket.ComputeSnorocketInterencesTask;
import org.hl7.komet.reasoner.sorocket.ExtractSnoRocketAxiomsTask;
import org.hl7.komet.reasoner.sorocket.LoadSnoRocketAxiomsTask;
import org.hl7.komet.reasoner.sorocket.ProcessSnoRocketResultsTask;
import org.hl7.tinkar.common.service.TinkExecutor;
import org.hl7.tinkar.common.service.TrackingCallable;
import org.hl7.tinkar.coordinate.view.calculator.ViewCalculator;
import org.hl7.tinkar.terms.PatternFacade;

import java.util.concurrent.Future;
import java.util.function.Consumer;

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
public class RunSnoRocketReasonerTask extends TrackingCallable<AxiomData> {
    final ViewCalculator viewCalculator;
    final PatternFacade statedAxiomPattern;
    final PatternFacade inferredAxiomPattern;

    final Consumer<ClassifierResults> classifierResultsConsumer;

    public RunSnoRocketReasonerTask(ViewCalculator viewCalculator, PatternFacade statedAxiomPattern,
                                    PatternFacade inferredAxiomPattern,
                                    Consumer<ClassifierResults> classifierResultsConsumer) {
        super(true, true);
        this.viewCalculator = viewCalculator;
        this.statedAxiomPattern = statedAxiomPattern;
        this.inferredAxiomPattern = inferredAxiomPattern;
        this.classifierResultsConsumer = classifierResultsConsumer;
        updateTitle("Running reasoner: " + viewCalculator.getPreferredDescriptionTextWithFallbackOrNid(statedAxiomPattern));
        updateProgress(0, 4);
    }

    @Override
    protected AxiomData compute() throws Exception {
        final int maxWork = 4;
        int workDone = 1;
        ExtractSnoRocketAxiomsTask extractSnoRocketAxiomsTask = new ExtractSnoRocketAxiomsTask(this.viewCalculator, this.statedAxiomPattern);
        updateMessage("Step " + workDone +
                ": " + viewCalculator.getPreferredDescriptionTextWithFallbackOrNid(statedAxiomPattern));
        Future<AxiomData> axiomDataFuture = TinkExecutor.threadPool().submit(extractSnoRocketAxiomsTask);
        AxiomData axiomData = axiomDataFuture.get();
        updateProgress(workDone++, maxWork);
        updateMessage("Step " + workDone +
                ": Loading axioms into reasoner");
        LoadSnoRocketAxiomsTask loadSnoRocketAxiomsTask = new LoadSnoRocketAxiomsTask(axiomData);
        Future<IReasoner> reasonerFuture = TinkExecutor.threadPool().submit(loadSnoRocketAxiomsTask);
        IReasoner reasoner = reasonerFuture.get();
        updateProgress(workDone++, maxWork);
        updateMessage("Step " + workDone +
                ": Computing taxonomy");
        ComputeSnorocketInterencesTask classifyTask = new ComputeSnorocketInterencesTask(reasoner);
        Future<Void> classifyFuture = TinkExecutor.threadPool().submit(classifyTask);
        classifyFuture.get();
        updateProgress(workDone++, maxWork);
        updateMessage("Step " + workDone +
                ": Processing results");
        ProcessSnoRocketResultsTask processResultsTask = new ProcessSnoRocketResultsTask(reasoner, this.viewCalculator, this.inferredAxiomPattern,
                axiomData);
        Future<ClassifierResults> processResultsFuture = TinkExecutor.threadPool().submit(processResultsTask);
        ClassifierResults classifierResults = processResultsFuture.get();

        updateProgress(workDone++, maxWork);
        classifierResultsConsumer.accept(classifierResults);

        updateMessage("Reasoner run complete in " + durationString());
        return axiomData;
    }
}
