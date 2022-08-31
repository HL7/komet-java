package org.semanticweb.elk.reasoner.indexing.model;

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

import org.semanticweb.elk.owl.interfaces.ElkAxiom;

/**
 * An {@link IndexedPropertyChain} that can be modified as a result of updating
 * the {@link ModifiableOntologyIndex} where this object is stored.
 * 
 * @author "Yevgeny Kazakov"
 *
 */
public interface ModifiableIndexedPropertyChain extends
		ModifiableIndexedSubObject, IndexedPropertyChain,
		Comparable<ModifiableIndexedPropertyChain> {

	/**
	 * Adds the given {@link IndexedObjectProperty} as a super-role of this
	 * {@link IndexedPropertyChain}
	 * 
	 * @param superObjectProperty
	 *            the {@link IndexedObjectProperty} to be added
	 * @param reason
	 *            the {@link ElkAxiom} responsible for the super-property
	 * @return {@code true} if the operation is successful or {@code false}
	 *         otherwise; if {@code false} is returned, this
	 *         {@link IndexedPropertyChain} does not change
	 */
	boolean addToldSuperObjectProperty(
			IndexedObjectProperty superObjectProperty, ElkAxiom reason);

	/**
	 * Removes the given {@link IndexedObjectProperty} from super-roles of this
	 * {@link IndexedPropertyChain}
	 * 
	 * @param superObjectProperty
	 *            the {@link IndexedObjectProperty} to be removed
	 * @param reason
	 *            the {@link ElkAxiom} responsible for the super-property
	 * @return {@code true} if the operation is successful or {@code false}
	 *         otherwise; if {@code false} is returned, this
	 *         {@link IndexedPropertyChain} does not change
	 */
	boolean removeToldSuperObjectProperty(
			IndexedObjectProperty superObjectProperty, ElkAxiom reason);

	/**
	 * Adds the given {@link IndexedComplexPropertyChain} to the list of
	 * {@link IndexedComplexPropertyChain} that contains this
	 * {@link IndexedPropertyChain} in the right-hand-side
	 * 
	 * @param chain
	 *            the {@link IndexedComplexPropertyChain} to be added
	 * @return {@code true} if the operation is successful or {@code false}
	 *         otherwise; if {@code false} is returned, this
	 *         {@link IndexedPropertyChain} does not change
	 */
	boolean addRightChain(IndexedComplexPropertyChain chain);

	/**
	 * Adds the given {@link IndexedComplexPropertyChain} from the list of
	 * {@link IndexedComplexPropertyChain} that contain this
	 * {@link IndexedPropertyChain} in the right-hand-side
	 * 
	 * @param chain
	 *            the {@link IndexedComplexPropertyChain} to be removed
	 * @return {@code true} if the operation is successful or {@code false}
	 *         otherwise; if {@code false} is returned, this
	 *         {@link IndexedPropertyChain} does not change
	 */
	boolean removeRightChain(IndexedComplexPropertyChain chain);
	
	/**
	 * A factory for creating instances
	 * 
	 * @author Yevgeny Kazakov
	 *
	 */
	interface Factory
			extends
				ModifiableIndexedComplexPropertyChain.Factory,
				ModifiableIndexedObjectProperty.Factory {

		// combined interface

	}

}
