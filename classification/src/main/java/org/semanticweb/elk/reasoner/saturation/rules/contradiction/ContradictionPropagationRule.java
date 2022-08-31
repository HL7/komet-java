package org.semanticweb.elk.reasoner.saturation.rules.contradiction;

/*
 * #%L
 * ELK Reasoner
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2011 - 2014 Department of Computer Science, University of Oxford
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.Map;

import org.semanticweb.elk.reasoner.indexing.model.IndexedContextRoot;
import org.semanticweb.elk.reasoner.indexing.model.IndexedObjectProperty;
import org.semanticweb.elk.reasoner.saturation.conclusions.model.BackwardLink;
import org.semanticweb.elk.reasoner.saturation.conclusions.model.ClassInconsistency;
import org.semanticweb.elk.reasoner.saturation.context.ContextPremises;
import org.semanticweb.elk.reasoner.saturation.context.SubContextPremises;
import org.semanticweb.elk.reasoner.saturation.inferences.ClassInconsistencyPropagated;
import org.semanticweb.elk.reasoner.saturation.rules.ClassInferenceProducer;

/**
 * A {@link ContradictionRule} applied when processing {@link ClassInconsistency}
 * producing {@link ClassInconsistency} in all contexts linked by non-reflexive
 * {@link BackwardLink}s in the {@code ContextPremises} (i.e.,
 * {@link BackwardLink}s such that {@link BackwardLink#getTraceRoot()} is
 * different from {@link ContextPremises#getRoot()}.
 * 
 * @author "Yevgeny Kazakov"
 * 
 */
public class ContradictionPropagationRule extends AbstractContradictionRule {

	private static final ContradictionPropagationRule INSTANCE_ = new ContradictionPropagationRule();

	public static final String NAME = "Contradiction Propagation over Backward Links";

	private ContradictionPropagationRule() {
		// do not allow creation of instances outside of this class
	}

	public static ContradictionPropagationRule getInstance() {
		return INSTANCE_;
	}

	@Override
	public String toString() {
		return NAME;
	}

	@Override
	public void apply(ClassInconsistency premise, ContextPremises premises,
			ClassInferenceProducer producer) {
		final Map<IndexedObjectProperty, ? extends SubContextPremises> subPremises = premises
				.getSubContextPremisesByObjectProperty();
		// no need to propagate over reflexive links
		for (IndexedObjectProperty propRelation : subPremises.keySet()) {
			for (IndexedContextRoot target : subPremises.get(propRelation)
					.getLinkedRoots()) {
				// producer.produce(target, premise);
				producer.produce(new ClassInconsistencyPropagated(
						premises.getRoot(), propRelation, target));
			}
		}
	}

	@Override
	public boolean isTracingRule() {
		return false;
	}

	@Override
	public void accept(ContradictionRuleVisitor<?> visitor,
			ClassInconsistency premise, ContextPremises premises,
			ClassInferenceProducer producer) {
		visitor.visit(this, premise, premises, producer);
	}

}