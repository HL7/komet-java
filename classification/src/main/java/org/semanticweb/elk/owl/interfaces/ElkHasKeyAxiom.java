/*
 * #%L
 * ELK OWL Object Interfaces
 * 
 * $Id$
 * $HeadURL$
 * %%
 * Copyright (C) 2011 - 2012 Department of Computer Science, University of Oxford
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
 * 
 */
package org.semanticweb.elk.owl.interfaces;

import java.util.List;

import org.semanticweb.elk.owl.visitors.ElkHasKeyAxiomVisitor;

/**
 * Corresponds to a <a href= "http://www.w3.org/TR/owl2-syntax/#Keys">Keys<a> in
 * the OWL 2 specification.
 * 
 * @author Pavel Klinov
 *
 *         pavel.klinov@uni-ulm.de
 *
 */
public interface ElkHasKeyAxiom extends ElkAxiom {

	public ElkClassExpression getClassExpression();

	public List<? extends ElkObjectPropertyExpression> getObjectPropertyExpressions();

	public List<? extends ElkDataPropertyExpression> getDataPropertyExpressions();

	/**
	 * Accept an {@link ElkHasKeyAxiomVisitor}.
	 * 
	 * @param visitor
	 *            the visitor that can work with this axiom type
	 * @return the output of the visitor
	 */
	public <O> O accept(ElkHasKeyAxiomVisitor<O> visitor);

	/**
	 * A factory for creating instances
	 * 
	 * @author Yevgeny Kazakov
	 *
	 */
	interface Factory {

		/**
		 * Create an {@link ElkHasKeyAxiom}
		 * 
		 * @param object
		 *            the {@link ElkClassExpression} for which the axiom should
		 *            be created
		 * @param objectPropertyKeys
		 *            the {@link ElkObjectPropertyExpression}s for which the
		 *            axiom should be created
		 * @param dataPropertyKeys
		 *            the {@link ElkDataPropertyExpression}s for which the axiom
		 *            should be created
		 * @return an {@link ElkHasKeyAxiom} corresponding to the input
		 */
		public ElkHasKeyAxiom getHasKeyAxiom(ElkClassExpression object,
				List<? extends ElkObjectPropertyExpression> objectPropertyKeys,
				List<? extends ElkDataPropertyExpression> dataPropertyKeys);

	}
}
