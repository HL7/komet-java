package org.semanticweb.elk.reasoner.saturation.conclusions.classes;

/*
 * #%L
 * ELK Reasoner
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2011 - 2015 Department of Computer Science, University of Oxford
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

import org.semanticweb.elk.reasoner.saturation.conclusions.model.ClassConclusion;
import org.semanticweb.elk.reasoner.saturation.conclusions.model.ContextInitialization;
import org.semanticweb.elk.reasoner.saturation.conclusions.model.ClassInconsistency;
import org.semanticweb.elk.reasoner.saturation.conclusions.model.DisjointSubsumer;
import org.semanticweb.elk.reasoner.saturation.conclusions.model.ForwardLink;
import org.semanticweb.elk.reasoner.saturation.conclusions.model.SubClassConclusion;
import org.semanticweb.elk.reasoner.saturation.conclusions.model.SubClassInclusionComposed;
import org.semanticweb.elk.reasoner.saturation.conclusions.model.SubClassInclusionDecomposed;

/**
 * A {@link ClassConclusion.Visitor} that always returns {@code null}. Can be
 * used as prototype to implement other visitors by overriding the corresponding
 * (default) methods.
 * 
 * @author "Yevgeny Kazakov"
 * 
 * @param <O>
 *            the type of output parameter with which this visitor works
 */
public class DummyClassConclusionVisitor<O>
		extends
			DummySubClassConclusionVisitor<O>
		implements
			ClassConclusion.Visitor<O> {

	/**
	 * The default implementation of all methods
	 * 
	 * @param conclusion
	 * @return
	 */
	protected O defaultVisit(ClassConclusion conclusion) {
		return null;
	}

	@Override
	protected O defaultVisit(SubClassConclusion subConclusion) {
		return defaultVisit((ClassConclusion) subConclusion);
	}

	@Override
	public O visit(SubClassInclusionComposed conclusion) {
		return defaultVisit(conclusion);
	}

	@Override
	public O visit(ContextInitialization conclusion) {
		return defaultVisit(conclusion);
	}

	@Override
	public O visit(ClassInconsistency conclusion) {
		return defaultVisit(conclusion);
	}

	@Override
	public O visit(SubClassInclusionDecomposed conclusion) {
		return defaultVisit(conclusion);
	}

	@Override
	public O visit(DisjointSubsumer conclusion) {
		return defaultVisit(conclusion);
	}

	@Override
	public O visit(ForwardLink conclusion) {
		return defaultVisit(conclusion);
	}

}
