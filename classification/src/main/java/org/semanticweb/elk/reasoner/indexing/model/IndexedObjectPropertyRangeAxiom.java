/*
 * #%L
 * ELK Reasoner
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

package org.semanticweb.elk.reasoner.indexing.model;

import org.semanticweb.elk.owl.interfaces.ElkAxiom;
import org.semanticweb.elk.owl.interfaces.ElkObjectPropertyRangeAxiom;

/**
 * An {@link IndexedAxiom} constructed from an {@link IndexedObjectProperty} and
 * {@link IndexedClassExpression}.<br>
 * 
 * Notation:
 * 
 * <pre>
 * [Range(R,C)]
 * </pre>
 * 
 * It is logically equivalent to the OWL axiom {@code ObjectPropertyRange(R C)}
 * <br>
 * 
 * The parameters can be obtained as follows:<br>
 * 
 * R = {@link #getProperty()}<br>
 * D = {@link #getRange()}<br>
 * 
 * @author "Yevgeny Kazakov"
 */
public interface IndexedObjectPropertyRangeAxiom extends IndexedAxiom {

	/**
	 * @return the {@link IndexedObjectProperty} representing the property of
	 *         the {@link ElkObjectPropertyRangeAxiom} represented by this
	 *         {@link IndexedObjectPropertyRangeAxiom}
	 * 
	 * @see ElkObjectPropertyRangeAxiom#getProperty()
	 */
	IndexedObjectProperty getProperty();

	/**
	 * @return the {@link IndexedClassExpression} representing the range of the
	 *         {@link ElkObjectPropertyRangeAxiom} represented by this
	 *         {@link IndexedObjectPropertyRangeAxiom}
	 * 
	 * @see ElkObjectPropertyRangeAxiom#getRange()
	 */
	IndexedClassExpression getRange();

	/**
	 * A factory for creating instances
	 * 
	 * @author Yevgeny Kazakov
	 *
	 */
	interface Factory {

		IndexedObjectPropertyRangeAxiom getIndexedObjectPropertyRangeAxiom(
				ElkAxiom originalAxiom, IndexedObjectProperty property,
				IndexedClassExpression range);

	}

	/**
	 * The visitor pattern for instances
	 * 
	 * @author Yevgeny Kazakov
	 *
	 * @param <O>
	 *            the type of the output
	 */
	interface Visitor<O> {

		O visit(IndexedObjectPropertyRangeAxiom axiom);

	}

}
