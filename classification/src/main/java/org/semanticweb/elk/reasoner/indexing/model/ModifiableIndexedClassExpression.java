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

import org.semanticweb.elk.reasoner.saturation.rules.subsumers.ChainableSubsumerRule;
import org.semanticweb.elk.util.collections.chains.Chain;

/**
 * An {@link IndexedClassExpression} that can be modified as a result of
 * updating the {@link ModifiableOntologyIndex} where this object is stored.
 * 
 * @author "Yevgeny Kazakov"
 *
 */
public interface ModifiableIndexedClassExpression extends
		ModifiableIndexedSubObject, IndexedClassExpression {

	/**
	 * @return the {@link Chain} view of all composition rules assigned to this
	 *         {@link IndexedClassExpression}; this is always not {@code null}.
	 *         This method can be used for convenient search and modification
	 *         (addition and deletion) of the rules using the methods of the
	 *         {@link Chain} interface without worrying about {@code null}
	 *         values.
	 */
	Chain<ChainableSubsumerRule> getCompositionRuleChain();
	
	/**
	 * A factory for creating instances
	 * 
	 * @author Yevgeny Kazakov
	 *
	 */
	interface Factory
			extends
				ModifiableIndexedClassEntity.Factory,
				ModifiableIndexedDataHasValue.Factory,
				ModifiableIndexedIndividual.Factory,
				ModifiableIndexedObjectComplementOf.Factory,
				ModifiableIndexedObjectHasSelf.Factory,
				ModifiableIndexedObjectIntersectionOf.Factory,
				ModifiableIndexedObjectSomeValuesFrom.Factory,
				ModifiableIndexedObjectUnionOf.Factory {

		// combined interface

	}

}
