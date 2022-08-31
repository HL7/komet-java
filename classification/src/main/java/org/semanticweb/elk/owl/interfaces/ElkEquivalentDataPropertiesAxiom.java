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
/**
 * @author Markus Kroetzsch, Aug 8, 2011
 */
package org.semanticweb.elk.owl.interfaces;

import java.util.List;

import org.semanticweb.elk.owl.visitors.ElkEquivalentDataPropertiesAxiomVisitor;

/**
 * Corresponds to an
 * <a href= "http://www.w3.org/TR/owl2-syntax/#Equivalent_Data_Properties">
 * Equivalent Data Properties Axiom<a> in the OWL 2 specification.
 * 
 * @author Markus Kroetzsch
 */
public interface ElkEquivalentDataPropertiesAxiom extends ElkDataPropertyAxiom {

	/**
	 * Get the list of equivalent data property expressions that this axiom
	 * refers to. The order of data property expressions does not affect the
	 * semantics but it is relevant to the syntax of OWL.
	 * 
	 * @return list of equivalent data property expressions
	 */
	public List<? extends ElkDataPropertyExpression> getDataPropertyExpressions();

	/**
	 * Accept an {@link ElkEquivalentDataPropertiesAxiomVisitor}.
	 * 
	 * @param visitor
	 *            the visitor that can work with this axiom type
	 * @return the output of the visitor
	 */
	public abstract <O> O accept(
			ElkEquivalentDataPropertiesAxiomVisitor<O> visitor);

	/**
	 * A factory for creating instances
	 * 
	 * @author Yevgeny Kazakov
	 *
	 */
	interface Factory {

		/**
		 * Create an {@link ElkEquivalentDataPropertiesAxiom}.
		 * 
		 * @param first
		 *            the fist equivalent {@link ElkDataPropertyExpression} for
		 *            which the axiom should be created
		 * @param second
		 *            the second equivalent {@link ElkDataPropertyExpression}
		 *            for which the axiom should be created
		 * @param other
		 *            other equivalent {@link ElkDataPropertyExpression}s for
		 *            which the axiom should be created
		 * @return an {@link ElkEquivalentDataPropertiesAxiom} corresponding to
		 *         the input
		 */
		public ElkEquivalentDataPropertiesAxiom getEquivalentDataPropertiesAxiom(
				ElkDataPropertyExpression first,
				ElkDataPropertyExpression second,
				ElkDataPropertyExpression... other);

		/**
		 * Create an {@link ElkEquivalentDataPropertiesAxiom}.
		 * 
		 * @param equivalentDataPropertyExpressions
		 *            the equivalent {@link ElkDataPropertyExpression}s for
		 *            which the axiom should be created
		 * @return an {@link ElkEquivalentDataPropertiesAxiom} corresponding to
		 *         the input
		 */
		public ElkEquivalentDataPropertiesAxiom getEquivalentDataPropertiesAxiom(
				List<? extends ElkDataPropertyExpression> equivalentDataPropertyExpressions);
	}

}
