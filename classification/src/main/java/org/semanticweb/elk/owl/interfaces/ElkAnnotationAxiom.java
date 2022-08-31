/*
 * #%L
 * ELK OWL Object Interfaces
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
package org.semanticweb.elk.owl.interfaces;

import org.semanticweb.elk.owl.visitors.ElkAnnotationAxiomVisitor;

/**
 * Corresponds to an
 * <a href= "http://www.w3.org/TR/owl2-syntax/#Annotation_Axioms">Annotation
 * Axiom<a> in the OWL 2 specification. Annotation axioms do not have any
 * logical meaning and can be safely ignored during reasoning. A (dummy)
 * implementation of annotation axioms is required to handle them more
 * gracefully during loading of OWL 2 ontologies.
 * 
 * @author "Yevgeny Kazakov"
 * 
 */
public interface ElkAnnotationAxiom extends ElkAxiom {

	/**
	 * Accept an {@link ElkAnnotationAxiomVisitor}.
	 * 
	 * @param visitor
	 *            the visitor that can work with this axiom type
	 * @return the output of the visitor
	 */
	public <O> O accept(ElkAnnotationAxiomVisitor<O> visitor);

	/**
	 * A factory for creating instances
	 * 
	 * @author Yevgeny Kazakov
	 *
	 */
	interface Factory extends ElkAnnotationAssertionAxiom.Factory,
			ElkAnnotationPropertyDomainAxiom.Factory,
			ElkAnnotationPropertyRangeAxiom.Factory,
			ElkSubAnnotationPropertyOfAxiom.Factory {

		// combined interface

	}

}
