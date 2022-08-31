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

import org.semanticweb.elk.owl.interfaces.ElkEquivalentClassesAxiom;

/**
 * An {@link ElkEquivalentClassesAxiomSubClassConversion} that can be modified
 * as a result of updating the {@link ModifiableOntologyIndex} where this object
 * is stored.
 * 
 * @author "Yevgeny Kazakov"
 */
public interface ModifiableElkEquivalentClassesAxiomSubClassConversion
		extends
			ElkEquivalentClassesAxiomSubClassConversion,
			ModifiableIndexedSubClassOfAxiomInference {

	/**
	 * A factory for creating instances
	 * 
	 * @author Yevgeny Kazakov
	 *
	 */
	interface Factory {
		
		ModifiableElkEquivalentClassesAxiomSubClassConversion getElkEquivalentClassesAxiomSubClassConversion(
				ElkEquivalentClassesAxiom originalAxiom, int subClassPosition,
				int superClassPosition,
				ModifiableIndexedClassExpression subClass,
				ModifiableIndexedClassExpression superClass);
		
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

		O visit(ModifiableElkEquivalentClassesAxiomSubClassConversion inference);

	}


}
