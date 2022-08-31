package org.semanticweb.elk.reasoner.saturation.rules.subcontextinit;

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

import org.semanticweb.elk.reasoner.indexing.model.IndexedObjectSomeValuesFrom;
import org.semanticweb.elk.reasoner.saturation.conclusions.model.Propagation;
import org.semanticweb.elk.reasoner.saturation.conclusions.model.SubContextInitialization;
import org.semanticweb.elk.reasoner.saturation.context.ContextPremises;
import org.semanticweb.elk.reasoner.saturation.context.SubContext;
import org.semanticweb.elk.reasoner.saturation.rules.ClassInferenceProducer;

/**
 * 
 * A {@link SubContextInitRule} generating {@link Propagation}s when
 * initializing {@link SubContext}s
 * 
 * @author "Yevgeny Kazakov"
 */
public class PropagationInitializationRule extends AbstractSubContextInitRule {

	public static final String NAME = "Propagations For SubContext";

	private static final PropagationInitializationRule INSTANCE_ = new PropagationInitializationRule();

	public static PropagationInitializationRule getInstance() {
		return INSTANCE_;
	}

	@Override
	public String toString() {
		return NAME;
	}

	@Override
	public void apply(SubContextInitialization premise,
			ContextPremises premises, ClassInferenceProducer producer) {
		IndexedObjectSomeValuesFrom.Helper.generatePropagations(
				premise.getSubDestination(), premises, producer);
	}

	@Override
	public boolean isTracingRule() {
		return true;
	}

	@Override
	public void accept(SubContextInitRuleVisitor<?> visitor,
			SubContextInitialization premise, ContextPremises premises,
			ClassInferenceProducer producer) {
		visitor.visit(this, premise, premises, producer);
	}

}
