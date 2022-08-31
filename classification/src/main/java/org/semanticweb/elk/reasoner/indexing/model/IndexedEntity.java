package org.semanticweb.elk.reasoner.indexing.model;

/*
 * #%L
 * ELK Reasoner
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2011 - 2015 Department of Computer Science, University of Oxford
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

import org.semanticweb.elk.owl.interfaces.ElkEntity;

/**
 * Represents all occurrences of an {@link ElkEntity} in an ontology.
 * 
 * @author "Yevgeny Kazakov"
 *
 */
public interface IndexedEntity extends IndexedObject {

	/**
	 * @return The {@link ElkEntity} represented by this {@link IndexedEntity}
	 */
	ElkEntity getElkEntity();

	/**
	 * @return {@code true} if this {@link IndexedClassExpression} occurs in the
	 *         ontology
	 */
	boolean occurs();

	String printOccurrenceNumbers();
	
	/**
	 * The visitor pattern for instances
	 * 
	 * @author Yevgeny Kazakov
	 *
	 * @param <O>
	 *            the type of the output
	 */
	interface Visitor<O>
			extends
				IndexedClassEntity.Visitor<O>,
				IndexedObjectProperty.Visitor<O> {

		// combined interface

	}
	
	<O> O accept(IndexedEntity.Visitor<O> visitor);

}
