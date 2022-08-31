/**
 * 
 */
package org.semanticweb.elk.reasoner.saturation;

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

import org.semanticweb.elk.reasoner.incremental.IncrementalProcessingStatistics;
import org.semanticweb.elk.reasoner.saturation.conclusions.classes.ClassConclusionStatistics;
import org.semanticweb.elk.reasoner.saturation.context.ContextStatistics;
import org.semanticweb.elk.reasoner.saturation.rules.RuleStatistics;
import org.semanticweb.elk.util.logging.LogLevel;
import org.slf4j.Logger;

/**
 * @author Pavel Klinov
 * 
 *         pavel.klinov@uni-ulm.de
 */
public class SaturationStatistics {

	private final ClassConclusionStatistics conclusionsStatistics_ = new ClassConclusionStatistics();

	private final RuleStatistics ruleStatistics_ = new RuleStatistics();

	private final ContextStatistics contextStatistics_ = new ContextStatistics();

	private final IncrementalProcessingStatistics processingStatistics_ = new IncrementalProcessingStatistics();

	public void startMeasurements() {
		conclusionsStatistics_.startMeasurements();
		ruleStatistics_.startMeasurements();
		conclusionsStatistics_.startMeasurements();
		processingStatistics_.startMeasurements();
	}

	public void reset() {
		conclusionsStatistics_.reset();
		ruleStatistics_.reset();
		contextStatistics_.reset();
		processingStatistics_.reset();
	}

	public synchronized void add(SaturationStatistics statistics) {
		this.conclusionsStatistics_.add(statistics.conclusionsStatistics_);
		this.ruleStatistics_.add(statistics.ruleStatistics_);
		this.contextStatistics_.add(statistics.contextStatistics_);
		this.processingStatistics_.add(statistics.processingStatistics_);
	}

	public void check(Logger logger) {
		contextStatistics_.check(logger);
		conclusionsStatistics_.check(logger);
	}

	public void print(Logger logger) {
		if (!logger.isDebugEnabled()) {
			return;
		}

		conclusionsStatistics_.print(logger);
		ruleStatistics_.print(logger);
		contextStatistics_.print(logger, LogLevel.DEBUG);
		processingStatistics_.print(logger, LogLevel.DEBUG);
	}

	public RuleStatistics getRuleStatistics() {
		return ruleStatistics_;
	}

	public ClassConclusionStatistics getConclusionStatistics() {
		return conclusionsStatistics_;
	}

	public ContextStatistics getContextStatistics() {
		return contextStatistics_;
	}

	public IncrementalProcessingStatistics getIncrementalProcessingStatistics() {
		return processingStatistics_;
	}
}
