/*
 * #%L
 * ELK Reasoner
 * 
 * $Id$
 * $HeadURL$
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
package org.semanticweb.elk.reasoner.saturation.conclusions.classes;

import org.semanticweb.elk.reasoner.saturation.conclusions.model.BackwardLink;
import org.semanticweb.elk.reasoner.saturation.conclusions.model.ClassConclusion;
import org.semanticweb.elk.reasoner.saturation.conclusions.model.SubClassInclusionComposed;
import org.semanticweb.elk.reasoner.saturation.conclusions.model.ContextInitialization;
import org.semanticweb.elk.reasoner.saturation.conclusions.model.ClassInconsistency;
import org.semanticweb.elk.reasoner.saturation.conclusions.model.SubClassInclusionDecomposed;
import org.semanticweb.elk.reasoner.saturation.conclusions.model.DisjointSubsumer;
import org.semanticweb.elk.reasoner.saturation.conclusions.model.ForwardLink;
import org.semanticweb.elk.reasoner.saturation.conclusions.model.Propagation;
import org.semanticweb.elk.reasoner.saturation.conclusions.model.SubContextInitialization;

/**
 * An object which can be used to measure the time spent on processing
 * {@link ClassConclusion}s using {@link ClassConclusion.Visitor}.
 * 
 * @author "Yevgeny Kazakov"
 * 
 */
public class ClassConclusionTimer {

	/**
	 * timer for {@link BackwardLink}s
	 */
	long timeBackwardLinks;

	/**
	 * timer for {@link ContextInitialization}s
	 */
	long timeContextInitializations;

	/**
	 * timer for {@link ClassInconsistency}s
	 */
	long timeContradictions;

	/**
	 * timer for {@link DisjointSubsumer}s
	 */
	long timeDisjointSubsumers;

	/**
	 * timer for {@link ForwardLink}s
	 */
	long timeForwardLinks;

	/**
	 * timer for {@link SubClassInclusionDecomposed}s
	 */
	long timeDecomposedSubsumers;

	/**
	 * timer for {@link SubClassInclusionComposed}s
	 */
	long timeComposedSubsumers;

	/**
	 * timer for {@link Propagation}s
	 */
	long timePropagations;

	/**
	 * timer for {@link SubContextInitialization}s
	 */
	long timeSubContextInitializations;

	public long getTimeBackwardLinks() {
		return timeBackwardLinks;
	}

	public long getTimeContradictions() {
		return timeContradictions;
	}

	public long getTimeContextInitializations() {
		return timeContextInitializations;
	}

	public long getTimeDisjointnessAxioms() {
		return timeDisjointSubsumers;
	}

	public long getTimeForwardLinks() {
		return timeForwardLinks;
	}

	public long getTimeComposedSubsumers() {
		return timeComposedSubsumers;
	}

	public long getTimeDecomposedSubsumers() {
		return timeDecomposedSubsumers;
	}

	public long getTimePropagations() {
		return timePropagations;
	}

	public long getSubContextInitializations() {
		return timeSubContextInitializations;
	}

	public long getTotalTime() {
		return timeComposedSubsumers + timeDecomposedSubsumers
				+ timeBackwardLinks + timeForwardLinks + timePropagations
				+ timeContradictions + timeDisjointSubsumers
				+ timeContextInitializations + timeSubContextInitializations;
	}

	/**
	 * Adds all timers of the argument to the corresponding counters of this
	 * object. The timers should not be directly modified other than using this
	 * method during this operation. The timers in the argument will be reseted
	 * after this operation.
	 * 
	 * @param timer
	 *            the object which counters should be added
	 */
	public synchronized void add(ClassConclusionTimer timer) {
		this.timeComposedSubsumers += timer.timeComposedSubsumers;
		this.timeDecomposedSubsumers += timer.timeDecomposedSubsumers;
		this.timeBackwardLinks += timer.timeBackwardLinks;
		this.timeForwardLinks += timer.timeForwardLinks;
		this.timeContradictions += timer.timeContradictions;
		this.timePropagations += timer.timePropagations;
		this.timeDisjointSubsumers += timer.timeDisjointSubsumers;
		this.timeContextInitializations += timer.timeContextInitializations;
		this.timeSubContextInitializations += timer.timeSubContextInitializations;
	}

	/**
	 * Reset all times to zero.
	 */
	public void reset() {
		timeComposedSubsumers = 0;
		timeDecomposedSubsumers = 0;
		timeBackwardLinks = 0;
		timeForwardLinks = 0;
		timeContradictions = 0;
		timePropagations = 0;
		timeDisjointSubsumers = 0;
		timeContextInitializations = 0;
		timeSubContextInitializations = 0;
	}

}
