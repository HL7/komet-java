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
import org.semanticweb.elk.reasoner.indexing.model.IndexedObjectHasSelf;
import org.semanticweb.elk.reasoner.saturation.conclusions.model.BackwardLink;
import org.semanticweb.elk.reasoner.saturation.conclusions.model.ForwardLink;
import org.semanticweb.elk.reasoner.saturation.context.ContextPremises;
import org.semanticweb.elk.reasoner.saturation.inferences.SubClassInclusionObjectHasSelfPropertyRange;
import org.semanticweb.elk.reasoner.saturation.rules.ClassInferenceProducer;

/**
 * A {@link SubsumerDecompositionRule} that processes
 * {@link IndexedObjectHasSelf} and produces the corresponding reflexive
 * {@link ForwardLink} or {@link BackwardLink}.
 * 
 * @author "Yevgeny Kazakov"
 */
public class IndexedObjectHasSelfDecomposition extends
		AbstractSubsumerDecompositionRule<IndexedObjectHasSelf> {

	public static final String NAME = "IndexedObjectHasSelf Decomposition";

	private static SubsumerDecompositionRule<IndexedObjectHasSelf> INSTANCE_ = new IndexedObjectHasSelfDecomposition();

	public static SubsumerDecompositionRule<IndexedObjectHasSelf> getInstance() {
		return INSTANCE_;
	}

	@Override
	public String toString() {
		return NAME;
	}

	@Override
	public void apply(IndexedObjectHasSelf premise, ContextPremises premises,
			ClassInferenceProducer producer) {
		IndexedObjectHasSelf.Helper.produceDecomposedExistentialLink(producer,
				premises.getRoot(), premise);
		for (IndexedClassExpression range : premise.getProperty()
				.getSaturated().getRanges()) {
			producer.produce(new SubClassInclusionObjectHasSelfPropertyRange(premises
					.getRoot(), premise, range));
		}
	}

	@Override
	public boolean isTracingRule() {
		return true;
	}

	@Override
	public void accept(SubsumerDecompositionRuleVisitor<?> visitor,
			IndexedObjectHasSelf premise, ContextPremises premises,
			ClassInferenceProducer producer) {
		visitor.visit(this, premise, premises, producer);
	}

}
