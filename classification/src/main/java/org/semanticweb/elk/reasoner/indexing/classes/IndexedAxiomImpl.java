/*
 * #%L
 * ELK Reasoner
 * 
 * $Id$
 * $HeadURL$
 * %%
 * Copyright (C) 2011 Department of Computer Science, University of Oxford
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
package org.semanticweb.elk.reasoner.indexing.classes;

import org.semanticweb.elk.owl.interfaces.ElkAxiom;
import org.semanticweb.elk.reasoner.indexing.model.IndexedAxiom;
import org.semanticweb.elk.reasoner.indexing.model.IndexedObject;
import org.semanticweb.elk.reasoner.tracing.AbstractConclusion;
import org.semanticweb.elk.reasoner.tracing.Conclusion;

/**
 * Implements {@link IndexedAxiom}
 * 
 * @author "Yevgeny Kazakov"
 *
 * @param <A>
 *            the type of the {@link ElkAxiom} from which this axiom originates
 */
abstract class IndexedAxiomImpl<A extends ElkAxiom> extends AbstractConclusion
		implements
			IndexedAxiom {

	private final A originalAxiom_;

	IndexedAxiomImpl(A originalAxiom) {
		this.originalAxiom_ = originalAxiom;
	}

	@Override
	public A getOriginalAxiom() {
		return originalAxiom_;
	}

	@Override
	public final <O> O accept(IndexedObject.Visitor<O> visitor) {
		return accept((IndexedAxiom.Visitor<O>) visitor);
	}

	@Override
	public final <O> O accept(Conclusion.Visitor<O> visitor) {
		return accept((IndexedAxiom.Visitor<O>) visitor);
	}

}
