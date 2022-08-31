package org.semanticweb.elk.reasoner.saturation.inferences;

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

import org.semanticweb.elk.reasoner.indexing.model.IndexedContextRoot;
import org.semanticweb.elk.reasoner.indexing.model.IndexedObjectProperty;

abstract class AbstractSubClassInference extends AbstractClassInference
		implements SubClassInference {

	private final IndexedObjectProperty subDestination_;

	public AbstractSubClassInference(IndexedContextRoot destination,
			IndexedObjectProperty subDestination) {
		super(destination);
		this.subDestination_ = subDestination;
	}

	@Override
	public final IndexedObjectProperty getSubDestination() {
		return this.subDestination_;
	}

	@Override
	public IndexedObjectProperty getTraceSubRoot() {
		return subDestination_;
	}

	@Override
	public final <O> O accept(ClassInference.Visitor<O> visitor) {
		return accept((SubClassInference.Visitor<O>) visitor);
	}

}
