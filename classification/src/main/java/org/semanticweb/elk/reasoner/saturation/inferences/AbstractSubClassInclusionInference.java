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

import org.semanticweb.elk.reasoner.indexing.model.IndexedClassExpression;
import org.semanticweb.elk.reasoner.indexing.model.IndexedContextRoot;

abstract class AbstractSubClassInclusionInference<S extends IndexedClassExpression>
		extends AbstractClassInference implements SubClassInclusionInference {

	private final S subsumer_;

	public AbstractSubClassInclusionInference(IndexedContextRoot subExpression,
			S superExpression) {
		super(subExpression);
		this.subsumer_ = superExpression;
	}

	public S getSubsumer() {
		return subsumer_;
	}

	public final S getConclusionSubsumer() {
		return getSubsumer();
	}

	@Override
	public final <O> O accept(ClassInference.Visitor<O> visitor) {
		return accept((SubClassInclusionInference.Visitor<O>) visitor);
	}

}
