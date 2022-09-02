/*
 * #%L
 * elk-reasoner
 * 
 * $Id: ElkTransitiveObjectPropertyAxiom.java 295 2011-08-10 11:43:29Z mak@aifb.uni-karlsruhe.de $
 * $HeadURL: https://elk-reasoner.googlecode.com/svn/trunk/elk-reasoner/src/main/java/org/semanticweb/elk/syntax/interfaces/ElkTransitiveObjectPropertyAxiom.java $
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

import org.semanticweb.elk.owl.visitors.ElkIrreflexiveObjectPropertyAxiomVisitor;

/**
 * Corresponds to a
 * <a href= "http://www.w3.org/TR/owl2-syntax/#Irreflexive_Object_Properties">
 * Irreflexive Object Property Axiom<a> in the OWL 2 specification.
 * 
 * @author Markus Kroetzsch
 * @author "Yevgeny Kazakov"
 */
public interface ElkIrreflexiveObjectPropertyAxiom extends
		ElkObjectPropertyAxiom, ElkPropertyAxiom<ElkObjectPropertyExpression> {

	/**
	 * Accept an {@link ElkIrreflexiveObjectPropertyAxiomVisitor}.
	 * 
	 * @param visitor
	 *            the visitor that can work with this axiom type
	 * @return the output of the visitor
	 */
	public abstract <O> O accept(
			ElkIrreflexiveObjectPropertyAxiomVisitor<O> visitor);

	/**
	 * A factory for creating instances
	 * 
	 * @author Yevgeny Kazakov
	 *
	 */
	interface Factory {

		/**
		 * Create an {@link ElkIrreflexiveObjectPropertyAxiom}.
		 * 
		 * @param property
		 *            the irreflexive {@link ElkObjectPropertyExpression} for
		 *            which the axiom should be created
		 * @return an {@link ElkIrreflexiveObjectPropertyAxiom} corresponding to
		 *         the input
		 */
		public ElkIrreflexiveObjectPropertyAxiom getIrreflexiveObjectPropertyAxiom(
				ElkObjectPropertyExpression property);

	}

}