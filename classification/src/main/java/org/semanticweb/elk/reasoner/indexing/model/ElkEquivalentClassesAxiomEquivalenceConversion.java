package org.semanticweb.elk.reasoner.indexing.model;

/*
 * #%L
 * ELK Reasoner
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2011 - 2016 Department of Computer Science, University of Oxford
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

import org.semanticweb.elk.owl.interfaces.ElkClassExpression;
import org.semanticweb.elk.owl.interfaces.ElkEquivalentClassesAxiom;

/**
 * Represents a transformation of an {@link ElkEquivalentClassesAxiom} to an
 * {@link IndexedEquivalentClassesAxiom} with two members representing the
 * corresponding members of the {@link ElkEquivalentClassesAxiom}.
 * 
 * @see ElkEquivalentClassesAxiom#getClassExpressions()
 * 
 * @author Yevgeny Kazakov
 */
public interface ElkEquivalentClassesAxiomEquivalenceConversion
		extends IndexedEquivalentClassesAxiomInference {

	@Override
	ElkEquivalentClassesAxiom getOriginalAxiom();

	/**
	 * @return the position of the {@link ElkClassExpression} in the member list
	 *         of the {@link ElkEquivalentClassesAxiom} that is converted to the
	 *         first member of the resulting
	 *         {@link IndexedEquivalentClassesAxiom}
	 * 
	 * @see ElkEquivalentClassesAxiom#getClassExpressions()
	 * @see IndexedEquivalentClassesAxiom#getFirstMember()
	 */
	int getFirstMemberPosition();

	/**
	 * @return the position of the {@link ElkClassExpression} in the member list
	 *         of the {@link ElkEquivalentClassesAxiom} that is converted to the
	 *         second member of the resulting
	 *         {@link IndexedEquivalentClassesAxiom}
	 * 
	 * @see ElkEquivalentClassesAxiom#getClassExpressions()
	 * @see IndexedEquivalentClassesAxiom#getSecondMember()
	 */
	int getSecondMemberPosition();

	/**
	 * The visitor pattern for instances
	 * 
	 * @author Yevgeny Kazakov
	 *
	 * @param <O>
	 *            the type of the output
	 */
	interface Visitor<O> {

		O visit(ElkEquivalentClassesAxiomEquivalenceConversion inference);

	}

}
