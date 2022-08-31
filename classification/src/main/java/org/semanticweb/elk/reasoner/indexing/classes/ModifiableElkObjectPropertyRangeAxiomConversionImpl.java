package org.semanticweb.elk.reasoner.indexing.classes;

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

import org.semanticweb.elk.owl.interfaces.ElkObjectPropertyRangeAxiom;
import org.semanticweb.elk.reasoner.indexing.model.IndexedObjectPropertyRangeAxiomInference;
import org.semanticweb.elk.reasoner.indexing.model.ModifiableElkObjectPropertyRangeAxiomConversion;
import org.semanticweb.elk.reasoner.indexing.model.ModifiableIndexedClassExpression;
import org.semanticweb.elk.reasoner.indexing.model.ModifiableIndexedObjectProperty;
import org.semanticweb.elk.reasoner.indexing.model.ModifiableIndexedObjectPropertyRangeAxiomInference;

/**
 * Implements {@link ModifiableElkObjectPropertyRangeAxiomConversion}
 * 
 * @author "Yevgeny Kazakov"
 */
class ModifiableElkObjectPropertyRangeAxiomConversionImpl extends
		AbstractModifiableIndexedObjectPropertyRangeAxiomInference<ElkObjectPropertyRangeAxiom>
		implements ModifiableElkObjectPropertyRangeAxiomConversion {

	ModifiableElkObjectPropertyRangeAxiomConversionImpl(
			ElkObjectPropertyRangeAxiom originalAxiom,
			ModifiableIndexedObjectProperty property,
			ModifiableIndexedClassExpression range) {
		super(originalAxiom, property, range);
	}

	@Override
	public final <O> O accept(
			IndexedObjectPropertyRangeAxiomInference.Visitor<O> visitor) {
		return visitor.visit(this);
	}

	@Override
	public final <O> O accept(
			ModifiableIndexedObjectPropertyRangeAxiomInference.Visitor<O> visitor) {
		return visitor.visit(this);
	}

}
