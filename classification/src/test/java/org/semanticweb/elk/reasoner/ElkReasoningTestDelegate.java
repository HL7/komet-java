/*
 * #%L
 * ELK Command Line Interface
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
package org.semanticweb.elk.reasoner;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collections;
import java.util.Map;
import java.util.Random;

import org.eclipse.collections.api.factory.Maps;
import org.eclipse.collections.api.map.ImmutableMap;
import org.semanticweb.elk.RandomSeedProvider;
import org.semanticweb.elk.reasoner.config.ReasonerConfiguration;
import org.semanticweb.elk.reasoner.stages.ElkInterruptedException;
import org.semanticweb.elk.testing.TestManifest;
import org.semanticweb.elk.testing.UrlTestInput;
import org.semanticweb.elk.util.concurrent.computation.RandomInterruptMonitor;

public abstract class ElkReasoningTestDelegate<O>
		extends AbstractReasoningTestWithInterruptsDelegate<O>
		implements ReasoningTestWithOutputAndInterruptsDelegate<O> {

	private Reasoner reasoner_;

	public ElkReasoningTestDelegate(
			final TestManifest<? extends UrlTestInput> manifest) {
		super(manifest);
	}

	public Reasoner getReasoner() {
		return reasoner_;
	}

	@Override
	public void initWithOutput() throws Exception {
		final ReasonerConfiguration config = ReasonerConfiguration
				.getConfiguration();
		config.setParameters(additionalConfigWithOutput());
		reasoner_ = TestReasonerUtils.createTestReasoner(
				getManifest().getInput().getUrl().openStream(), config);
	}

	/**
	 * Augments the configuration with the returned key-value pairs before
	 * {@link #initWithOutput()}.
	 * 
	 * @return The additional configuration.
	 */
	@SuppressWarnings("static-method")
	protected Map<String, String> additionalConfigWithOutput() {
		return (Map<String, String>) Maps.immutable.empty();
	}

	@Override
	public void initWithInterrupts() throws Exception {
		final ReasonerConfiguration config = ReasonerConfiguration
				.getConfiguration();
		config.setParameters(additionalConfigWithInterrupts());
		final Random random = new Random(RandomSeedProvider.VALUE);
		reasoner_ = TestReasonerUtils.createTestReasoner(
				getManifest().getInput().getUrl().openStream(),
				new TestReasonerInterrupter(new RandomInterruptMonitor(random,
						getInterruptionChance(),
						getInterruptionIntervalNanos())),
				config);
	}

	/**
	 * Augments the configuration with the returned key-value pairs before
	 * {@link #initWithInterrupts()}.
	 * 
	 * @return The additional configuration.
	 */
	@SuppressWarnings("static-method")
	protected Map<String, String> additionalConfigWithInterrupts() {
		return Collections.emptyMap();
	}

	@Override
	public Class<? extends Exception> getInterruptionExceptionClass() {
		return ElkInterruptedException.class;
	}

	@Override
	public void before() throws Exception {
		// Empty.
	}

	@Override
	public void after() {
		try {
			assertTrue(reasoner_.shutdown());
		} catch (InterruptedException e) {
			fail();
		}
	}

}
