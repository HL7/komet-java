package org.semanticweb.elk.reasoner.saturation.rules;

/*
 * #%L
 * ELK Reasoner
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2011 - 2016 Department of Computer Science, University of Oxford
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

import org.semanticweb.elk.reasoner.saturation.context.ContextPremises;

/**
 * A skeleton implementation of {@code Rule}
 * 
 * @author Yevgeny Kazakov
 *
 * @param <P>
 *            the type of premises to which the rule can be applied
 */
public abstract class AbstractRule<P> implements Rule<P> {
	
	@Override
	public void applyTracing(P premise, ContextPremises premises,
			ClassInferenceProducer producer) {
		// by default apply normally
		apply(premise, premises, producer);
	}

}
