package org.liveontologies.puli;

/*-
 * #%L
 * Proof Utility Library
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2014 - 2021 Live Ontologies Project
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

import java.util.HashSet;
import java.util.Set;

import java.util.Objects;

public class AxiomPinpointingInferenceBuilder<C, A, B extends AxiomPinpointingInferenceBuilder<C, A, B>>
		extends InferenceBuilder<C, B> {

	private final Set<A> axioms_ = new HashSet<A>();

	protected AxiomPinpointingInferenceBuilder(String name) {
		super(name);
	}

	Set<A> getAxioms() {
		return axioms_;
	}

	public B axiom(final A axiom) {
		Objects.requireNonNull(axiom);
		axioms_.add(axiom);
		return getBuilder();
	}

	@Override
	AxiomPinpointingInference<C, A> build() {
		return new BaseAxiomPinpointingInference<C, A>(getName(),
				getConclusion(), getPremises(), getAxioms());
	}

}
