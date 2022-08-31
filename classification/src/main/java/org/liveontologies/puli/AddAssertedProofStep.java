/*-
 * #%L
 * Proof Utility Library
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2014 - 2017 Live Ontologies Project
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
package org.liveontologies.puli;

import java.util.Set;

import java.util.Objects;

class AddAssertedProofStep<C> extends ConvertedProofStep<C> {

	private final Set<? extends C> assertedAxioms_;

	AddAssertedProofStep(ProofStep<C> delegate,
			Set<? extends C> assertedAxioms) {
		super(delegate);
		Objects.requireNonNull(assertedAxioms);
		this.assertedAxioms_ = assertedAxioms;
	}

	@Override
	protected ConvertedProofNode<C> convert(ProofNode<C> node) {
		return new AddAssertedProofNode<C>(node, assertedAxioms_);
	}

}
