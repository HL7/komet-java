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
package org.semanticweb.elk.reasoner.query;

import org.junit.runner.RunWith;
import org.semanticweb.elk.reasoner.ReasoningCorrectnessTestWithInterrupts;
import org.semanticweb.elk.reasoner.ReasoningTestWithOutputAndInterruptsDelegate;
import org.semanticweb.elk.testing.DiffableOutput;
import org.semanticweb.elk.testing.PolySuite;

@RunWith(PolySuite.class)
public abstract class BaseQueryTest<Q, O extends DiffableOutput<?, O>> extends
		ReasoningCorrectnessTestWithInterrupts<QueryTestInput<Q>, O, QueryTestManifest<Q, O>, ReasoningTestWithOutputAndInterruptsDelegate<O>> {

	public BaseQueryTest(final QueryTestManifest<Q, O> manifest,
			final ReasoningTestWithOutputAndInterruptsDelegate<O> delegate) {
		super(manifest, delegate);
	}

}
