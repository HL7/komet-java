/*
 * #%L
 * ELK OWL API Binding
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
package org.semanticweb.elk.reasoner.query;

import java.util.Map;

import org.semanticweb.elk.exceptions.ElkException;
import org.semanticweb.elk.owl.interfaces.ElkClass;
import org.semanticweb.elk.owl.interfaces.ElkClassExpression;
import org.semanticweb.elk.owl.iris.ElkIri;
import org.semanticweb.elk.reasoner.Reasoner;
import org.semanticweb.elk.reasoner.completeness.IncompleteResult;
import org.semanticweb.elk.reasoner.taxonomy.model.Node;

/**
 * ensures that the test results can be compared with {@link #equals(Object)}
 * 
 * @author Peter Skocovsky
 * @author Yevgeny Kazakov
 */
public class ElkEquivalentClassesTestOutput extends
		EquivalentEntitiesTestOutput<ElkClass, ElkEquivalentClassesTestOutput> {

	public ElkEquivalentClassesTestOutput(
			IncompleteResult<? extends Node<ElkClass>> incompleteMembers) {
		super(incompleteMembers.map(n -> toMap(n)));
	}

	public ElkEquivalentClassesTestOutput(
			Map<ElkIri, ? extends ElkClass> members) {
		super(members);
	}

	public ElkEquivalentClassesTestOutput(Reasoner reasoner,
			ElkClassExpression query) throws ElkException {
		this(reasoner.getEquivalentClassesQuitely(query));
	}

}
