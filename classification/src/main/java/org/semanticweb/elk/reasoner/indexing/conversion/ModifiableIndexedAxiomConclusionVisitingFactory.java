package org.semanticweb.elk.reasoner.indexing.conversion;

/*-
 * #%L
 * ELK Reasoner Core
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2011 - 2017 Department of Computer Science, University of Oxford
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

import org.liveontologies.puli.Producer;
import org.semanticweb.elk.reasoner.indexing.classes.ModifiableIndexedAxiomInferenceBaseFactory;
import org.semanticweb.elk.reasoner.indexing.classes.ModifiableIndexedAxiomInferenceConclusionVisitor;
import org.semanticweb.elk.reasoner.indexing.classes.ModifiableIndexedAxiomInferenceDelegatingFactory;
import org.semanticweb.elk.reasoner.indexing.model.IndexedAxiomInference;
import org.semanticweb.elk.reasoner.indexing.model.ModifiableIndexedAxiomInference;
import org.semanticweb.elk.reasoner.indexing.model.ModifiableIndexedObject;

/**
 * A {@link ModifiableIndexedAxiomInference} that, in addition to creating
 * inferences, also creates their conclusions using the provided
 * {@link ModifiableIndexedObject.Factory}
 * 
 * @author Yevgeny Kazakov
 */
class ModifiableIndexedAxiomInferenceConclusionVisitingFactory
		extends ModifiableIndexedAxiomInferenceDelegatingFactory {

	private final Producer<? super IndexedAxiomInference> inferenceProducer_;

	private final ModifiableIndexedAxiomInference.Visitor<Void> inferenceVisitor_;

	ModifiableIndexedAxiomInferenceConclusionVisitingFactory(
			Producer<? super IndexedAxiomInference> inferenceProducer,
			ModifiableIndexedObject.Factory conclusionFactory) {
		super(new ModifiableIndexedAxiomInferenceBaseFactory());
		this.inferenceProducer_ = inferenceProducer;
		this.inferenceVisitor_ = new ModifiableIndexedAxiomInferenceConclusionVisitor<Void>(
				conclusionFactory);
	}

	@Override
	protected <T extends ModifiableIndexedAxiomInference> T filter(T input) {
		inferenceProducer_.produce(input);
		input.accept(inferenceVisitor_);
		return input;
	}

}
