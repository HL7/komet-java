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
package org.semanticweb.elk.reasoner.entailments.model;

import java.util.List;

import org.semanticweb.elk.owl.interfaces.ElkAxiom;

/**
 * An axiom was entailed because {@link OntologyInconsistency} was entailed.
 * <p>
 * The entailed axiom can be retrieved from {@link #getConclusion()} and the
 * reason, i.e. {@link OntologyInconsistency}, should be the only member of
 * {@link #getPremises()}.
 * 
 * @author Peter Skocovsky
 */
public interface OntologyInconsistencyEntailsAnyAxiom
		extends AxiomEntailmentInference<ElkAxiom> {

	@Override
	List<? extends OntologyInconsistency> getPremises();

	public static interface Visitor<O> {
		O visit(OntologyInconsistencyEntailsAnyAxiom ontologyInconsistencyEntailsAnyAxiom);
	}

}
