package org.hl7.komet.reasoner.elk;

import org.hl7.komet.reasoner.AxiomData;
import org.hl7.komet.reasoner.ClassifierResults;
import org.hl7.tinkar.common.service.TinkExecutor;
import org.hl7.tinkar.common.service.TrackingCallable;
import org.hl7.tinkar.coordinate.view.calculator.ViewCalculator;
import org.hl7.tinkar.terms.PatternFacade;
import org.semanticweb.elk.owl.interfaces.ElkAxiom;
import org.semanticweb.elk.owl.interfaces.ElkClass;
import org.semanticweb.elk.reasoner.taxonomy.model.Taxonomy;

import java.util.concurrent.Future;
import java.util.function.Consumer;

public class RunElkReasonerTask extends TrackingCallable<AxiomData> {
    final ViewCalculator viewCalculator;
    final PatternFacade statedAxiomPattern;
    final PatternFacade inferredAxiomPattern;

    final Consumer<ClassifierResults> classifierResultsConsumer;

    public RunElkReasonerTask(ViewCalculator viewCalculator, PatternFacade statedAxiomPattern,
                                    PatternFacade inferredAxiomPattern,
                                    Consumer<ClassifierResults> classifierResultsConsumer) {
        super(true, true);
        this.viewCalculator = viewCalculator;
        this.statedAxiomPattern = statedAxiomPattern;
        this.inferredAxiomPattern = inferredAxiomPattern;
        this.classifierResultsConsumer = classifierResultsConsumer;
        updateTitle("Running reasoner: " + viewCalculator.getPreferredDescriptionTextWithFallbackOrNid(statedAxiomPattern));
        updateProgress(0, 3);
    }

    @Override
    protected AxiomData compute() throws Exception {
        final int maxWork = 4;
        int workDone = 1;
        ExtractLoadReasonWithElkTask extractSnoRocketAxiomsTask = new ExtractLoadReasonWithElkTask(this.viewCalculator, this.statedAxiomPattern);
        updateMessage("Step " + workDone +
                ": " + viewCalculator.getPreferredDescriptionTextWithFallbackOrNid(statedAxiomPattern));
        Future<AxiomData<ElkAxiom>> axiomDataFuture = TinkExecutor.threadPool().submit(extractSnoRocketAxiomsTask);
        AxiomData axiomData = axiomDataFuture.get();
        updateProgress(workDone++, maxWork);
        updateMessage("Step " + workDone +
                ": Loading axioms into reasoner");
        LoadAndClassifyElkAxiomsTask loadElkAxiomsTask = new LoadAndClassifyElkAxiomsTask(axiomData);
        Future<Taxonomy<ElkClass>> taxonomyFuture = TinkExecutor.threadPool().submit(loadElkAxiomsTask);
        Taxonomy<ElkClass> taxonomy = taxonomyFuture.get();
        updateProgress(workDone++, maxWork);
        updateMessage("Step " + workDone +
                ": Computing taxonomy");
        ProcessElkResultsTask processResultsTask = new ProcessElkResultsTask(taxonomy, this.viewCalculator, this.inferredAxiomPattern,
                axiomData);
        Future<ClassifierResults> processResultsFuture = TinkExecutor.threadPool().submit(processResultsTask);
        ClassifierResults classifierResults = processResultsFuture.get();

        updateProgress(workDone++, maxWork);
        classifierResultsConsumer.accept(classifierResults);

        updateMessage("Reasoner run complete in " + durationString());
        return axiomData;
    }
}