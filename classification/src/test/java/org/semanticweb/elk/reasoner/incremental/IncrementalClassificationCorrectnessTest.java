/*
 * #%L
 * ELK Reasoner
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2011 - 2012 Department of Computer Science, University of Oxford
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

import org.junit.runner.RunWith;
import org.semanticweb.elk.owl.interfaces.ElkAxiom;
import org.semanticweb.elk.reasoner.ElkClassTaxonomyTestOutput;
import org.semanticweb.elk.testing.PolySuite;
import org.semanticweb.elk.testing.TestManifest;
import org.semanticweb.elk.testing.UrlTestInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Pavel Klinov
 * 
 *         pavel.klinov@uni-ulm.de
 */
@RunWith(PolySuite.class)
public class IncrementalClassificationCorrectnessTest
		extends BaseIncrementalClassificationCorrectnessTest<ElkAxiom> {

	private static final Logger LOGGER_ = LoggerFactory
			.getLogger(IncrementalClassificationCorrectnessTest.class);

	public IncrementalClassificationCorrectnessTest(
			final TestManifest<UrlTestInput> testManifest) {
		super(testManifest,
				new ElkIncrementalReasoningTestDelegate<ElkClassTaxonomyTestOutput>(
						testManifest) {

					@Override
					public ElkClassTaxonomyTestOutput getExpectedOutput()
							throws Exception {
						LOGGER_.trace(
								"======= Computing Expected Taxonomy =======");
						return new ElkClassTaxonomyTestOutput(
								getStandardReasoner().getTaxonomyQuietly());
					}

					@Override
					public ElkClassTaxonomyTestOutput getActualOutput()
							throws Exception {
						LOGGER_.trace(
								"======= Computing Incremental Taxonomy =======");
						return new ElkClassTaxonomyTestOutput(
								getStandardReasoner().getTaxonomyQuietly());
					}

				});
	}

}
