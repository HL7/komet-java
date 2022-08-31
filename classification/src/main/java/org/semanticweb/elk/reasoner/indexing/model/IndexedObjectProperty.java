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

package org.semanticweb.elk.reasoner.indexing.model;

import java.util.ArrayList;
import java.util.Collection;

import org.semanticweb.elk.owl.interfaces.ElkAxiom;
import org.semanticweb.elk.owl.interfaces.ElkObjectProperty;
import org.semanticweb.elk.owl.interfaces.ElkObjectPropertyRangeAxiom;
import org.semanticweb.elk.owl.interfaces.ElkSubObjectPropertyExpression;
import org.semanticweb.elk.owl.interfaces.ElkSubObjectPropertyOfAxiom;

/**
 * An {@link IndexedPropertyChain} constructed from an {@link ElkObjectProperty}
 * .<br>
 * 
 * Notation:
 * 
 * <pre>
 * R
 * </pre>
 * 
 * The parameters can be obtained as follows:<br>
 * 
 * R = {@link #getElkEntity()}<br>
 */
public interface IndexedObjectProperty
		extends
			IndexedPropertyChain,
			IndexedEntity {

	/**
	 * @return The {@link ElkObjectProperty} represented by this
	 *         {@link IndexedObjectProperty}.
	 */
	@Override
	ElkObjectProperty getElkEntity();

	/**
	 * @return The representations of all {@link ElkSubObjectPropertyExpression}
	 *         s occurring in {@link ElkSubObjectPropertyOfAxiom}, where the
	 *         super property {@link ElkObjectProperty} is represented by this
	 *         {@link IndexedObjectProperty}
	 * 
	 * @see ElkSubObjectPropertyOfAxiom#getSubObjectPropertyExpression()
	 * @see ElkSubObjectPropertyOfAxiom#getSuperObjectPropertyExpression()
	 * @see IndexedPropertyChain#getToldSuperProperties()
	 */
	ArrayList<IndexedPropertyChain> getToldSubChains();

	/**
	 * @return The {@link ElkAxiom}s responsible for the respective told sub
	 *         properties returned by {@link #getToldSubChains()}
	 * 
	 * @see IndexedPropertyChain#getToldSuperPropertiesReasons()
	 */
	ArrayList<ElkAxiom> getToldSubChainsReasons();

	/**
	 * @return The representation of ranges for all
	 *         {@link ElkObjectPropertyRangeAxiom} with the property represented
	 *         by this {@link IndexedObjectProperty}
	 * 
	 * @see ElkObjectPropertyRangeAxiom#getRange()
	 * @see ElkObjectPropertyRangeAxiom#getProperty()
	 */
	ArrayList<IndexedClassExpression> getToldRanges();

	/**
	 * @return The {@link ElkAxiom}s responsible for the respective told ranges
	 *         returned by {@link #getToldRanges()}
	 */
	ArrayList<ElkAxiom> getToldRangesReasons();

	/**
	 * @return All {@link IndexedComplexPropertyChain}s in which this
	 *         {@link IndexedObjectProperty} is a left property
	 * 
	 * @see {@link IndexedComplexPropertyChain#getFirstProperty()}
	 */
	Collection<IndexedComplexPropertyChain> getLeftChains();

	/**
	 * The visitor pattern for instances
	 * 
	 * @author Yevgeny Kazakov
	 *
	 * @param <O>
	 *            the type of the output
	 */
	interface Visitor<O> {

		O visit(IndexedObjectProperty element);

	}

}
