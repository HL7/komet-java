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

import org.semanticweb.elk.reasoner.saturation.inferences.ClassInference;

/**
 * A {@link ClassInferenceProducer} that combines two given
 * {@link ClassInferenceProducer}: all methods are executed first for the first
 * {@link ClassInferenceProducer} and then for the second.
 * 
 * @author "Yevgeny Kazakov"
 * 
 */
public class CombinedConclusionProducer implements ClassInferenceProducer {

	private final ClassInferenceProducer firstProducer_;

	private final ClassInferenceProducer secondProducer_;

	public CombinedConclusionProducer(ClassInferenceProducer firstProducer,
			ClassInferenceProducer secondProducer) {
		this.firstProducer_ = firstProducer;
		this.secondProducer_ = secondProducer;
	}

	@Override
	public void produce(ClassInference inference) {
		firstProducer_.produce(inference);
		secondProducer_.produce(inference);
	}

}
