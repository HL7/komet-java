package org.semanticweb.elk.reasoner.tracing;

import java.util.AbstractList;
import java.util.List;

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

/**
 * A skeleton implementation of {@link TracingInference}
 * 
 * @author Yevgeny Kazakov
 *
 */
public abstract class AbstractTracingInference implements TracingInference {

	private final static Conclusion.Factory CONCLUSION_FACTORY_ = ConclusionBaseFactory
			.getInstance();

	private final TracingInference.Visitor<Conclusion> CONCLUSION_GETTER_ = new TracingInferenceConclusionGetter(
			CONCLUSION_FACTORY_);

	public abstract int getPremiseCount();

	public abstract Conclusion getPremise(int index,
			Conclusion.Factory factory);

	/**
	 * @param factory
	 *            the factory for creating conclusions
	 * 
	 * @return the conclusion produced by this inference
	 */

	protected static <T> T failGetPremise(int index) {
		throw new IndexOutOfBoundsException("No premise with index: " + index);
	}

	protected void checkPremiseIndex(int index) {
		if (index < 0 || index >= getPremiseCount()) {
			failGetPremise(index);
		}
	}

	@Override
	public String getName() {
		return getClass().getName();
	}

	@Override
	public final Conclusion getConclusion() {
		return accept(CONCLUSION_GETTER_);
	}

	@Override
	public final List<? extends Conclusion> getPremises() {
		return new AbstractList<Conclusion>() {

			@Override
			public Conclusion get(int index) {
				return getPremise(index, CONCLUSION_FACTORY_);
			}

			@Override
			public int size() {
				return getPremiseCount();
			}

		};
	}

	@Override
	public String toString() {
		return TracingInferencePrinter.toString(this);
	}

}
