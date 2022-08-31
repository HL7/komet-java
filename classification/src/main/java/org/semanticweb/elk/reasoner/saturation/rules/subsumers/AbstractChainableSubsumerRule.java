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
import org.semanticweb.elk.reasoner.saturation.context.ContextPremises;
import org.semanticweb.elk.reasoner.saturation.rules.ClassInferenceProducer;
import org.semanticweb.elk.reasoner.saturation.rules.RuleVisitor;
import org.semanticweb.elk.util.collections.chains.ModifiableLinkImpl;

/**
 * A skeleton implementation of {@link ChainableSubsumerRule}
 * 
 * @author "Yevgeny Kazakov"
 * 
 */
abstract class AbstractChainableSubsumerRule extends
		ModifiableLinkImpl<ChainableSubsumerRule> implements
		ChainableSubsumerRule {

	AbstractChainableSubsumerRule(ChainableSubsumerRule tail) {
		super(tail);
	}
	
	@Override
	public void applyTracing(IndexedClassExpression premise,
			ContextPremises premises, ClassInferenceProducer producer) {
		// by default apply normally
		apply(premise, premises, producer);
	}

	@Override
	public void accept(RuleVisitor<?> visitor, IndexedClassExpression premise,
			ContextPremises premises, ClassInferenceProducer producer) {
		accept((SubsumerRuleVisitor<?>) visitor, premise, premises, producer);
	}

	@Override
	public void accept(SubsumerRuleVisitor<?> visitor,
			IndexedClassExpression premise, ContextPremises premises,
			ClassInferenceProducer producer) {
		accept((LinkedSubsumerRuleVisitor<?>) visitor, premise, premises,
				producer);
	}

}
