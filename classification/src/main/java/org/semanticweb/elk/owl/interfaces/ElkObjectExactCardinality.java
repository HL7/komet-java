package org.semanticweb.elk.owl.interfaces;
/*
 * #%L
 * ELK OWL Object Interfaces
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2011 - 2014 Department of Computer Science, University of Oxford
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

import org.semanticweb.elk.owl.visitors.ElkObjectExactCardinalityVisitor;

/**
 * A common interface for qualified and unqualified min cardinality restrictions
 * on objects.
 * 
 * @author "Yevgeny Kazakov"
 *
 */
public interface ElkObjectExactCardinality
		extends ElkCardinalityRestriction<ElkObjectPropertyExpression> {

	/**
	 * Accept an {@link ElkObjectExactCardinalityVisitor}.
	 * 
	 * @param visitor
	 *            the visitor that can work with this object type
	 * @return the output of the visitor
	 */
	public <O> O accept(ElkObjectExactCardinalityVisitor<O> visitor);

	/**
	 * A factory for creating instances
	 * 
	 * @author Yevgeny Kazakov
	 *
	 */
	interface Factory extends ElkObjectExactCardinalityQualified.Factory,
			ElkObjectExactCardinalityUnqualified.Factory {

		// combined interface

	}
}
