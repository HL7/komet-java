package org.hl7.komet.reasoner.elk;

import au.csiro.ontology.classification.IReasoner;
import org.hl7.komet.reasoner.AxiomData;
import org.hl7.komet.reasoner.ClassifierResults;
import org.hl7.tinkar.common.service.TrackingCallable;
import org.hl7.tinkar.coordinate.view.calculator.ViewCalculator;
import org.hl7.tinkar.terms.PatternFacade;
import org.semanticweb.elk.owl.interfaces.ElkClass;
import org.semanticweb.elk.reasoner.taxonomy.model.Taxonomy;

public class ProcessElkResultsTask extends TrackingCallable<ClassifierResults> {
    public ProcessElkResultsTask(Taxonomy<ElkClass> taxonomy, ViewCalculator viewCalculator, PatternFacade inferredAxiomPattern,
                                 AxiomData axiomData) {

    }

    @Override
    protected ClassifierResults compute() throws Exception {
        return null;
    }
}
