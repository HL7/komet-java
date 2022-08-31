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
package org.semanticweb.elk.reasoner.saturation.conclusions.model;

import org.semanticweb.elk.reasoner.indexing.model.IndexedClassExpressionList;
import org.semanticweb.elk.reasoner.indexing.model.IndexedContextRoot;

/**
 * A {@link ClassConclusion} representing a concept inclusion and a concept
 * disjointness axiom.<br>
 * 
 * Notation:
 * 
 * <pre>
 * [C] ⊑ D|L   where L is a list of concepts containing D
 * </pre>
 * 
 * It is logically equivalent to axioms {@code SubClassOf(C D)} and
 * {@code DisjointClasses(L)}<br>
 * 
 * The parameters can be obtained as follows:<br>
 * 
 * C = {@link #getDestination()}<br>
 * L = {@link #getDisjointExpressions()}<br>
 * D = {@link #getPosition()} returns the position of D in
 * {@link IndexedClassExpressionList#getElements()} for L<br>
 * 
 * @see IndexedClassExpressionList#getElements()
 * 
 * @author Pavel Klinov
 * 
 *         pavel.klinov@uni-ulm.de
 * 
 * @author "Yevgeny Kazakov"
 */
public interface DisjointSubsumer extends ClassConclusion {

	public static final String NAME = "Disjoint Subsumer";

	/**
	 * @return the {@link IndexedClassExpressionList} to which the member
	 *         belongs
	 */
	public IndexedClassExpressionList getDisjointExpressions();

	/**
	 * @return the position of the {@link IndexedClassExpressionList} at which
	 *         the member occurs
	 */
	public int getPosition();

	public <O> O accept(Visitor<O> visitor);

	/**
	 * A factory for creating instances
	 * 
	 * @author Yevgeny Kazakov
	 *
	 */
	interface Factory {

		DisjointSubsumer getDisjointSubsumer(IndexedContextRoot root,
				IndexedClassExpressionList disjointExpressions, int position);

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

		public O visit(DisjointSubsumer conclusion);

	}

}
