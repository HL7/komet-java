package org.semanticweb.elk.reasoner.saturation.rules.subsumers;

/*
 * #%L
 * ELK Reasoner
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2011 - 2015 Department of Computer Science, University of Oxford
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

import org.semanticweb.elk.reasoner.indexing.model.IndexedClassExpression;
import org.semanticweb.elk.reasoner.indexing.model.IndexedObjectComplementOf;
import org.semanticweb.elk.reasoner.saturation.conclusions.model.ClassInconsistency;
import org.semanticweb.elk.reasoner.saturation.context.ContextPremises;
import org.semanticweb.elk.reasoner.saturation.inferences.ClassInconsistencyOfObjectComplementOf;
import org.semanticweb.elk.reasoner.saturation.rules.ClassInferenceProducer;

/**
 * A {@link SubsumerDecompositionRule} producing {@link ClassInconsistency} when
 * processing the {@link IndexedObjectComplementOf} which negation
 * {@link IndexedClassExpression} is contained in the {@code Context}
 * 
 * @see IndexedObjectComplementOf#getNegated()
 * @see ContradictionFromNegationRule
 * 
 * @author "Yevgeny Kazakov"
 * 
 */
public class IndexedObjectComplementOfDecomposition extends
		AbstractSubsumerDecompositionRule<IndexedObjectComplementOf> {

	public static final String NAME = "IndexedObjectComplementOf Decomposition";

	private static IndexedObjectComplementOfDecomposition INSTANCE_ = new IndexedObjectComplementOfDecomposition();

	public static IndexedObjectComplementOfDecomposition getInstance() {
		return INSTANCE_;
	}

	@Override
	public String toString() {
		return NAME;
	}

	@Override
	public void apply(IndexedObjectComplementOf premise,
			ContextPremises premises, ClassInferenceProducer producer) {
		if (premises.getComposedSubsumers().contains(premise.getNegated())) {
			producer.produce(new ClassInconsistencyOfObjectComplementOf(premises.getRoot(),
					premise));
		}
	}

	@Override
	public boolean isTracingRule() {
		return true;
	}

	@Override
	public void accept(SubsumerDecompositionRuleVisitor<?> visitor,
			IndexedObjectComplementOf premise, ContextPremises premises,
			ClassInferenceProducer producer) {
		visitor.visit(this, premise, premises, producer);
	}

}
