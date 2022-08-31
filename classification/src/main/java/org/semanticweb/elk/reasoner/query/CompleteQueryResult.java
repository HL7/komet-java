package org.semanticweb.elk.reasoner.query;

/*-
 * #%L
 * ELK Reasoner Core
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2011 - 2020 Department of Computer Science, University of Oxford
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
import org.semanticweb.elk.reasoner.completeness.Incompleteness;
import org.semanticweb.elk.reasoner.completeness.IncompletenessMonitor;

/**
 * A complete {@link QueryResult} whose {@link IncompletenessMonitor} always
 * returns {@code false}
 * {@link IncompletenessMonitor#isIncompletenessDetected()}
 * 
 * @author Yevgeny Kazakov
 *
 */
public class CompleteQueryResult implements QueryResult {

	private final ElkAxiom query_;

	private final boolean isEntailed_;

	public CompleteQueryResult(ElkAxiom query, boolean isEntailed) {
		this.query_ = query;
		this.isEntailed_ = isEntailed;
	}

	@Override
	public ElkAxiom getQuery() {
		return query_;
	}

	@Override
	public boolean entailmentProved() throws ElkQueryException {
		return isEntailed_;
	}

	@Override
	public IncompletenessMonitor getIncompletenessMonitor() {
		return Incompleteness.getNoIncompletenessMonitor();
	}

	@Override
	public boolean unlock() {
		return true;
	}

}
