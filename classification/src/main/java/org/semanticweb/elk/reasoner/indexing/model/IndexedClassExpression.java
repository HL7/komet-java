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

import org.semanticweb.elk.owl.interfaces.ElkClassExpression;
import org.semanticweb.elk.reasoner.saturation.rules.LinkRule;
import org.semanticweb.elk.reasoner.saturation.rules.subsumers.LinkedSubsumerRule;

/**
 * Represents occurrences of an {@link ElkClassExpression} in an ontology.
 * 
 * @author "Frantisek Simancik"
 * @author "Markus Kroetzsch"
 * @author "Yevgeny Kazakov"
 * @author Pavel Klinov
 */
public interface IndexedClassExpression extends IndexedContextRoot {

	/**
	 * @return the first composition rule assigned to this
	 *         {@link IndexedClassExpression}, or {@code null} if there no such
	 *         rules; all other rules can be obtained by traversing over
	 *         {@link LinkRule#next()}
	 */
	LinkedSubsumerRule getCompositionRuleHead();

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
				IndexedDataHasValue.Visitor<O>,
				IndexedObjectComplementOf.Visitor<O>,
				IndexedObjectHasSelf.Visitor<O>,
				IndexedObjectIntersectionOf.Visitor<O>,
				IndexedObjectSomeValuesFrom.Visitor<O>,
				IndexedObjectUnionOf.Visitor<O> {

		// combined interface

	}
	
	<O> O accept(Visitor<O> visitor);

}
