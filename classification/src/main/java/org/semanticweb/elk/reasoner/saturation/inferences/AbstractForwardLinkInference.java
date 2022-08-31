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
import org.semanticweb.elk.reasoner.indexing.model.IndexedPropertyChain;
import org.semanticweb.elk.reasoner.saturation.conclusions.classes.ForwardLinkImpl;
import org.semanticweb.elk.reasoner.saturation.conclusions.model.ForwardLink;

abstract class AbstractForwardLinkInference<R extends IndexedPropertyChain>
		extends AbstractClassInference implements ForwardLinkInference {

	/**
	 * the {@link IndexedPropertyChain} in the existential restriction
	 * corresponding to this {@link ForwardLinkImpl}
	 */
	private final R forwardChain_;

	/**
	 * the {@link IndexedContextRoot} corresponding to the filler of the
	 * existential restriction corresponding to this {@link ForwardLinkImpl}
	 */
	private final IndexedContextRoot target_;

	public AbstractForwardLinkInference(IndexedContextRoot destination,
			R forwardChain, IndexedContextRoot target) {
		super(destination);
		this.forwardChain_ = forwardChain;
		this.target_ = target;
	}

	public R getChain() {
		return forwardChain_;
	}

	public IndexedContextRoot getTarget() {
		return target_;
	}

	/**
	 * @param factory
	 *            the factory for creating conclusions
	 * 
	 * @return the conclusion produced by this inference
	 */
	public final ForwardLink getConclusion(ForwardLink.Factory factory) {
		return factory.getForwardLink(getDestination(), getChain(),
				getTarget());
	}

	@Override
	public final <O> O accept(ClassInference.Visitor<O> visitor) {
		return accept((ForwardLinkInference.Visitor<O>) visitor);
	}

}
