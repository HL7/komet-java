package org.hl7.komet.reasoner.elk;

import org.hl7.komet.reasoner.AxiomData;
import org.hl7.tinkar.common.alert.AlertStreams;
import org.hl7.tinkar.common.service.TrackingCallable;
import org.semanticweb.elk.exceptions.ElkException;
import org.semanticweb.elk.loading.AxiomLoader;
import org.semanticweb.elk.loading.ElkLoadingException;
import org.semanticweb.elk.owl.interfaces.ElkAxiom;
import org.semanticweb.elk.owl.interfaces.ElkClass;
import org.semanticweb.elk.owl.interfaces.ElkEntity;
import org.semanticweb.elk.owl.visitors.ElkAxiomProcessor;
import org.semanticweb.elk.reasoner.Reasoner;
import org.semanticweb.elk.reasoner.ReasonerFactory;
import org.semanticweb.elk.reasoner.completeness.IncompleteResult;
import org.semanticweb.elk.reasoner.completeness.Incompleteness;
import org.semanticweb.elk.reasoner.completeness.IncompletenessMonitor;
import org.semanticweb.elk.reasoner.config.ReasonerConfiguration;
import org.semanticweb.elk.reasoner.taxonomy.model.Taxonomy;
import org.semanticweb.elk.util.concurrent.computation.InterruptMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LoadAndClassifyElkAxiomsTask extends TrackingCallable<Taxonomy<ElkClass>> implements AxiomLoader.Factory {
    private static final Logger LOG = LoggerFactory.getLogger(LoadAndClassifyElkAxiomsTask.class);
    final AxiomData<ElkAxiom> axiomData;

    public LoadAndClassifyElkAxiomsTask(AxiomData<ElkAxiom> axiomData) {
        super(true, true);
        this.axiomData = axiomData;
        updateTitle("Loading axioms into ELK reasoner. ");
    }

    @Override
    protected Taxonomy<ElkClass> compute() throws Exception {
        try {
            int axiomCount = this.axiomData.processedSemantics.get();
            updateProgress(0, axiomCount);

            // create reasoner
            ReasonerFactory reasoningFactory = new ReasonerFactory();

            ReasonerConfiguration configuration = ReasonerConfiguration.getConfiguration();
            Reasoner reasoner = reasoningFactory.createReasoner(this, configuration);

            reasoner.ensureLoading();

            // Classify the ontology.

            IncompleteResult<? extends Taxonomy<ElkClass>> taxonomyResult = reasoner.getTaxonomy();
            IncompletenessMonitor incompletenessMonitor = taxonomyResult.getIncompletenessMonitor();
            incompletenessMonitor.isIncompletenessDetected();
            incompletenessMonitor.logStatus(LOG);
            Taxonomy<ElkClass> taxonomy = Incompleteness.getValue(taxonomyResult);

            LOG.info("getTaxonomyQuietly complete: " + taxonomy.getTopNode());
            updateMessage("Load in " + durationString());
            return taxonomy;
        } catch (ElkException e) {
            AlertStreams.dispatchToRoot(e);
            throw e;
        }
    }

    @Override
    public AxiomLoader getAxiomLoader(InterruptMonitor interrupter) {
        return new ElkLoader();
    }

    private class ElkLoader implements AxiomLoader {
        private boolean started = false;
        private volatile boolean finished = false;
        /**
         * the exception created if something goes wrong
         */
        protected volatile Exception exception;

        @Override
        public void load(ElkAxiomProcessor axiomInserter, ElkAxiomProcessor axiomDeleter) throws ElkLoadingException {
            if (finished)
                return;

            if (!started) {
                started = true;
            }
            for (ElkAxiom elkAxiom : axiomData.axiomsSet) {
                if (isInterrupted()) {
                    break;
                }
                axiomInserter.visit(elkAxiom);
            }

            if (exception != null) {
                throw new ElkLoadingException(exception);
            }
        }

        @Override
        public boolean isLoadingFinished() {
            return finished;
        }

        @Override
        public void dispose() {

        }

        @Override
        public boolean isInterrupted() {
            return false;
        }
    }
}
