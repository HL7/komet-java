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

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.semanticweb.elk.ElkTestUtils;
import org.semanticweb.elk.reasoner.ElkClassTaxonomyTestOutput;
import org.semanticweb.elk.reasoner.SimpleManifestCreator;
import org.semanticweb.elk.testing.ConfigurationUtils;
import org.semanticweb.elk.testing.DiffableOutput;
import org.semanticweb.elk.testing.PolySuite;
import org.semanticweb.elk.testing.PolySuite.Config;
import org.semanticweb.elk.testing.PolySuite.Configuration;
import org.semanticweb.elk.testing.TestManifest;
import org.semanticweb.elk.testing.UrlTestInput;

/**
 * Implements the correctness check based on comparing expected and obtained
 * class taxonomies. Subclasses still need to provide methods to load changes
 * 
 * @author Pavel Klinov
 * 
 *         pavel.klinov@uni-ulm.de
 * @author Peter Skocovsky
 * @author Yevgeny Kazakov
 * @param <A> 
 */
@RunWith(PolySuite.class)
public abstract class BaseIncrementalClassificationCorrectnessTest<A> extends
		IncrementalReasoningCorrectnessTestWithInterrupts<UrlTestInput, A, ElkClassTaxonomyTestOutput, IncrementalReasoningTestWithInterruptsDelegate<A, ElkClassTaxonomyTestOutput>> {

	public BaseIncrementalClassificationCorrectnessTest(
			final TestManifest<UrlTestInput> testManifest,
			final IncrementalReasoningTestWithInterruptsDelegate<A, ElkClassTaxonomyTestOutput> testDelegate) {
		super(testManifest, testDelegate);
	}

	@Override
	protected void correctnessCheck(final ElkClassTaxonomyTestOutput actualOutput,
			final ElkClassTaxonomyTestOutput expectedOutput) throws Exception {

		boolean actualContainsAllExpected = actualOutput
				.containsAllElementsOf(expectedOutput);
		boolean expectedContainsAllActual = expectedOutput
				.containsAllElementsOf(actualOutput);
		if (actualContainsAllExpected && expectedContainsAllActual) {
			return;
		}
		// else
		final StringBuilder message = new StringBuilder(
				"Actual output is not equal to the expected output:");
		if (!actualContainsAllExpected) {
			reportMissing(actualOutput, expectedOutput, "< ", message);
		}
		if (!expectedContainsAllActual) {
			reportMissing(expectedOutput, actualOutput, "> ", message);
		}
		Assert.fail(message.toString());
	}

	private static <E, O extends DiffableOutput<E, O>> void reportMissing(
			O first, O second, final String prefix,
			final StringBuilder message) {
		first.reportMissingElementsOf(second, new DiffableOutput.Listener<E>() {

			@Override
			public void missing(E element) {
				message.append('\n');
				message.append(prefix);
				message.append(element);
			}
		});

	}

	@Config
	public static Configuration getConfig()
			throws URISyntaxException, IOException {
		return ConfigurationUtils.loadFileBasedTestConfiguration(
				ElkTestUtils.TEST_INPUT_LOCATION,
				IncrementalClassificationCorrectnessTest.class,
				SimpleManifestCreator.INSTANCE, "owl");
	}

}
