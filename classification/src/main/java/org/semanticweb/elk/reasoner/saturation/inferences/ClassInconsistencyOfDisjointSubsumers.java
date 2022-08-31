/*
 * #%L
 * ELK Reasoner
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2011 - 2014 Department of Computer Science, University of Oxford
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
package org.semanticweb.elk.reasoner.saturation.inferences;

import org.semanticweb.elk.reasoner.indexing.model.IndexedClassExpression;
import org.semanticweb.elk.reasoner.indexing.model.IndexedClassExpressionList;
import org.semanticweb.elk.reasoner.indexing.model.IndexedContextRoot;
import org.semanticweb.elk.reasoner.saturation.conclusions.model.ClassInconsistency;
import org.semanticweb.elk.reasoner.saturation.conclusions.model.DisjointSubsumer;
import org.semanticweb.elk.reasoner.tracing.Conclusion;
import org.semanticweb.elk.reasoner.tracing.Conclusion.Factory;

/**
 * A {@link ClassInference} producing a {@link ClassInconsistency} from two
 * {@link DisjointSubsumer}s with different disjoint expressions of the same
 * disjoint expression list:<br>
 * 
 * <pre>
 *     (1)         (2)
 *  [C] ⊑ D1:L  [C] ⊑ D2:L
 * ⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯
 *        [C] ⊑ 0
 * </pre>
 * 
 * The parameters can be obtained as follows:<br>
 * 
 * C = {@link #getOrigin()}={@link #getDestination()}<br>
 * L = {@link #getDisjointExpressions()}<br>
 * D1 = {@link #getFirstDisjointPosition()} gives the position of D1 in
 * {@link IndexedClassExpressionList#getElements()} of L<br>
 * D2 = {@link #getSecondDisjointPosition()} gives the position of D2 in
 * {@link IndexedClassExpressionList#getElements()} of L<br>
 * 
 * @see DisjointSubsumer#getDisjointExpressions()
 * 
 * @author Pavel Klinov
 * 
 *         pavel.klinov@uni-ulm.de
 * 
 * @author Yevgeny Kazakov
 */
public class ClassInconsistencyOfDisjointSubsumers
		extends AbstractClassInconsistencyInference {

	/**
	 * The disjoint {@link IndexedClassExpression}s that cause the contradiction
	 */
	private final IndexedClassExpressionList disjointExpressions_;

	/**
	 * The positions of subsumers that violate the disjointness axiom; these
	 * positions must be different
	 */
	private final Integer firstPosition_, secondPosition_;

	public ClassInconsistencyOfDisjointSubsumers(DisjointSubsumer premise,
			Integer otherPos) {
		super(premise.getDestination());
		this.disjointExpressions_ = premise.getDisjointExpressions();
		this.firstPosition_ = premise.getPosition();
		this.secondPosition_ = otherPos;
	}

	@Override
	public IndexedContextRoot getOrigin() {
		return getDestination();
	}

	public IndexedClassExpressionList getDisjointExpressions() {
		return disjointExpressions_;
	}

	public int getFirstDisjointPosition() {
		return firstPosition_;
	}

	public int getSecondDisjointPosition() {
		return secondPosition_;
	}

	public DisjointSubsumer getFirstPremise(DisjointSubsumer.Factory factory) {
		return factory.getDisjointSubsumer(getOrigin(), disjointExpressions_,
				firstPosition_);
	}

	public DisjointSubsumer getSecondPremise(DisjointSubsumer.Factory factory) {
		return factory.getDisjointSubsumer(getOrigin(), disjointExpressions_,
				secondPosition_);
	}

	@Override
	public int getPremiseCount() {
		return 2;
	}

	@Override
	public Conclusion getPremise(int index, Factory factory) {
		switch (index) {
		case 0:
			return getFirstPremise(factory);
		case 1:
			return getSecondPremise(factory);
		default:
			return failGetPremise(index);
		}
	}

	@Override
	public final <O> O accept(ClassInconsistencyInference.Visitor<O> visitor) {
		return visitor.visit(this);
	}

	/**
	 * Visitor pattern for instances
	 * 
	 * @author Yevgeny Kazakov
	 *
	 * @param <O>
	 *            the type of the output
	 */
	public static interface Visitor<O> {

		public O visit(ClassInconsistencyOfDisjointSubsumers inference);

	}

}
