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
package org.semanticweb.elk.reasoner.incremental;

import java.util.Collection;

import org.semanticweb.elk.reasoner.ReasoningTestDelegate;
import org.semanticweb.elk.util.logging.LogLevel;
import org.slf4j.Logger;

/**
 * Test delegate for incremental tests.
 * 
 * @author Peter Skocovsky
 *
 * @param <A>
 *            The type of axioms that are added or removed from the input
 *            ontology.
 * @param <O>
 *            The type of test output.
 */
public interface IncrementalReasoningTestDelegate<A, O>
		extends ReasoningTestDelegate<O> {

	/**
	 * Called at the beginning of the test. Loads test input and selects axioms
	 * that may be added or removed from the input ontology.
	 * 
	 * @return A collection of changing axioms.
	 * @throws Exception
	 */
	Collection<A> load() throws Exception;

	/**
	 * Called at the beginning of the incremental test, but after
	 * {@link #load()}.
	 * 
	 * @throws Exception
	 */
	void initIncremental() throws Exception;

	/**
	 * Applies the supplied changes.
	 * 
	 * @param changes
	 *            Axioms that are added or removed.
	 * @param type
	 *            Whether the axioms are added or removed.
	 */
	void applyChanges(Iterable<A> changes, IncrementalChangeType type);

	void dumpChangeToLog(A change, Logger logger, LogLevel level);

	/**
	 * Returns the expected output.
	 * 
	 * @return the expected output.
	 * @throws Exception
	 */
	O getExpectedOutput() throws Exception;

}
