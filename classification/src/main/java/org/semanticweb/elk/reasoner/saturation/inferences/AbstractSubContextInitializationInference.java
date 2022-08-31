package org.semanticweb.elk.reasoner.saturation.inferences;

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

import org.semanticweb.elk.reasoner.indexing.model.IndexedContextRoot;
import org.semanticweb.elk.reasoner.indexing.model.IndexedObjectProperty;
import org.semanticweb.elk.reasoner.saturation.conclusions.model.SubContextInitialization;

abstract class AbstractSubContextInitializationInference extends
		AbstractSubClassInference implements SubContextInitializationInference {

	protected AbstractSubContextInitializationInference(
			IndexedContextRoot destination,
			IndexedObjectProperty subDestination) {
		super(destination, subDestination);
	}

	/**
	 * @param factory
	 *            the factory for creating conclusions
	 * 
	 * @return the conclusion produced by this inference
	 */
	public final SubContextInitialization getConclusion(
			SubContextInitialization.Factory factory) {
		return factory.getSubContextInitialization(getDestination(),
				getSubDestination());
	}

	@Override
	public final <O> O accept(SubClassInference.Visitor<O> visitor) {
		return accept((SubContextInitializationInference.Visitor<O>) visitor);
	}

}
