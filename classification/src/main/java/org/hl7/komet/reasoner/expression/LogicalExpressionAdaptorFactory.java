package org.hl7.komet.reasoner.expression;

import org.hl7.tinkar.component.graph.Graph;
import org.hl7.tinkar.component.graph.GraphAdaptorFactory;
import org.hl7.tinkar.entity.graph.DiTreeEntity;

public class LogicalExpressionAdaptorFactory implements GraphAdaptorFactory<LogicalExpression> {

    @Override
    public LogicalExpression adapt(Graph graph) {
        return new LogicalExpression((DiTreeEntity) graph);
    }


}
