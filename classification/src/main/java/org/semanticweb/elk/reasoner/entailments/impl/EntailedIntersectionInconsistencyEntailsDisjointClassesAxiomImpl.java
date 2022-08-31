/*-
 * #%L
 * ELK Reasoner Core
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
package org.semanticweb.elk.reasoner.entailments.impl;

import java.util.Arrays;
import java.util.List;

import org.semanticweb.elk.owl.interfaces.ElkDisjointClassesAxiom;
import org.semanticweb.elk.reasoner.entailments.model.EntailedIntersectionInconsistencyEntailsDisjointClassesAxiom;
import org.semanticweb.elk.reasoner.entailments.model.DisjointClassesAxiomEntailment;
import org.semanticweb.elk.reasoner.entailments.model.EntailmentInference;
import org.semanticweb.elk.reasoner.entailments.model.SubClassOfAxiomEntailment;

public class EntailedIntersectionInconsistencyEntailsDisjointClassesAxiomImpl
		extends
		AbstractAxiomEntailmentInference<ElkDisjointClassesAxiom, DisjointClassesAxiomEntailment>
		implements EntailedIntersectionInconsistencyEntailsDisjointClassesAxiom {

	private final List<? extends SubClassOfAxiomEntailment> premises_;

	public EntailedIntersectionInconsistencyEntailsDisjointClassesAxiomImpl(
			final DisjointClassesAxiomEntailment conclusion,
			final List<? extends SubClassOfAxiomEntailment> premises) {
		super(conclusion);
		this.premises_ = premises;
	}

	public EntailedIntersectionInconsistencyEntailsDisjointClassesAxiomImpl(
			final DisjointClassesAxiomEntailment conclusion,
			final SubClassOfAxiomEntailment... premises) {
		this(conclusion, Arrays.asList(premises));
	}

	@Override
	public List<? extends SubClassOfAxiomEntailment> getPremises() {
		return premises_;
	}

	@Override
	public <O> O accept(final EntailmentInference.Visitor<O> visitor) {
		return visitor.visit(this);
	}

}
