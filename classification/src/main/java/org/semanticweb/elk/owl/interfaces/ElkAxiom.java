/*
 * #%L
 * elk-reasoner
 * 
 * $Id$
 * $HeadURL$
 * %%
 * Copyright (C) 2011 Oxford University Computing Laboratory
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
/**
 * @author Markus Kroetzsch, Aug 8, 2011
 */
package org.semanticweb.elk.owl.interfaces;

import org.semanticweb.elk.owl.visitors.ElkAxiomVisitor;

/**
 * Corresponds to an <a href= "http://www.w3.org/TR/owl2-syntax/#Axioms">axiom
 * <a> in the OWL 2 specification.
 * 
 * @author Markus Kroetzsch
 */
public interface ElkAxiom extends ElkObject {

	// TODO getAnnotation();

	/**
	 * Accept an {@link ElkAxiomVisitor}.
	 * 
	 * @param visitor
	 *            the visitor that can work with this object type
	 * @return the output of the visitor
	 */
	public abstract <O> O accept(ElkAxiomVisitor<O> visitor);

	/**
	 * A factory for creating instances
	 * 
	 * @author Yevgeny Kazakov
	 *
	 */
	interface Factory
			extends ElkAnnotationAxiom.Factory, ElkAssertionAxiom.Factory,
			ElkClassAxiom.Factory, ElkDataPropertyAxiom.Factory,
			ElkDatatypeDefinitionAxiom.Factory, ElkDeclarationAxiom.Factory,
			ElkHasKeyAxiom.Factory, ElkObjectPropertyAxiom.Factory,
			ElkPropertyAxiom.Factory, ElkSWRLRule.Factory {

		// combined interface

	}

}
