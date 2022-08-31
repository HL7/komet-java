/**
 * 
 */
package org.semanticweb.elk.reasoner.saturation.inferences;

/*
 * #%L
 * ELK Reasoner
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2011 - 2013 Department of Computer Science, University of Oxford
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

import org.semanticweb.elk.owl.interfaces.ElkAxiom;
import org.semanticweb.elk.reasoner.indexing.model.IndexedComplexPropertyChain;
import org.semanticweb.elk.reasoner.indexing.model.IndexedContextRoot;
import org.semanticweb.elk.reasoner.indexing.model.IndexedObjectProperty;
import org.semanticweb.elk.reasoner.indexing.model.IndexedPropertyChain;
import org.semanticweb.elk.reasoner.indexing.model.IndexedSubObjectPropertyOfAxiom;
import org.semanticweb.elk.reasoner.saturation.conclusions.model.BackwardLink;
import org.semanticweb.elk.reasoner.saturation.conclusions.model.ForwardLink;
import org.semanticweb.elk.reasoner.saturation.conclusions.model.SubPropertyChain;
import org.semanticweb.elk.reasoner.tracing.Conclusion;
import org.semanticweb.elk.reasoner.tracing.Conclusion.Factory;

/**
 * A {@link ClassInference} producing a {@link BackwardLink} from a
 * {@link BackwardLink}, {@link SubPropertyChain}, {@link ForwardLink},
 * {@link SubPropertyChain}, and a {@link IndexedSubObjectPropertyOfAxiom}:<br>
 * 
 * <pre>
 *   (1)             (2)       (3)           (4)         (5)
 *  C ⊑ <∃R1>.[D]  R1 ⊑ R2  [D] ⊑ <∃P1>.E  P1 ⊑ P2  [R2P2 ⊑ R]
 * ⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯
 *                      C ⊑ <∃R>.[E]
 * </pre>
 * 
 * The parameters can be obtained as follows:<br>
 * 
 * C = {@link #getPremiseSource()} = {@link #getConclusionSource()}<br>
 * R1 = {@link #getPremiseBackwardRelation()}<br>
 * D = {@link #getOrigin()}<br>
 * P1 = {@link #getPremiseForwardChain()}<br>
 * E = {@link #getPremiseTarget()} = {@link #getDestination()}<br>
 * R2P2 = {@link #getComposition()} (from which R2 and P2 can be obtained)<br>
 * R = {@link #getConclusionRelation()}<br>
 * 
 * @author "Yevgeny Kazakov"
 */
public class BackwardLinkComposition extends AbstractBackwardLinkInference
		implements LinkComposition {

	private final IndexedContextRoot inferenceRoot_;

	private final IndexedObjectProperty backwardRelation_;

	private final IndexedPropertyChain forwardChain_;

	private final IndexedComplexPropertyChain composition_;

	/**
	 * The {@link ElkAxiom} that yields the super-property of the composition
	 */
	private final ElkAxiom reason_;

	public BackwardLinkComposition(IndexedContextRoot originRoot,
			IndexedObjectProperty backwardRelation,
			IndexedContextRoot inferenceRoot, IndexedPropertyChain forwardChain,
			IndexedContextRoot targetRoot,
			IndexedComplexPropertyChain composition,
			IndexedObjectProperty compositionSuperProperty, ElkAxiom reason) {
		super(targetRoot, compositionSuperProperty, originRoot);
		this.backwardRelation_ = backwardRelation;
		this.forwardChain_ = forwardChain;
		this.inferenceRoot_ = inferenceRoot;
		this.composition_ = composition;
		this.reason_ = reason;
	}

	@Override
	public IndexedContextRoot getOrigin() {
		return inferenceRoot_;
	}

	@Override
	public IndexedContextRoot getPremiseSource() {
		return getConclusionSource();
	}

	@Override
	public IndexedObjectProperty getPremiseBackwardRelation() {
		return backwardRelation_;
	}

	@Override
	public IndexedPropertyChain getPremiseForwardChain() {
		return forwardChain_;
	}

	@Override
	public IndexedContextRoot getPremiseTarget() {
		return getDestination();
	}

	@Override
	public IndexedComplexPropertyChain getComposition() {
		return composition_;
	}

	/**
	 * @return the {@link ElkAxiom} responsible for the fifth premise
	 */
	public ElkAxiom getReason() {
		return reason_;
	}

	@Override
	public BackwardLink getFirstPremise(BackwardLink.Factory factory) {
		return factory.getBackwardLink(getOrigin(), backwardRelation_,
				getTraceRoot());
	}

	@Override
	public SubPropertyChain getSecondPremise(SubPropertyChain.Factory factory) {
		return factory.getSubPropertyChain(backwardRelation_,
				composition_.getFirstProperty());
	}

	@Override
	public ForwardLink getThirdPremise(ForwardLink.Factory factory) {
		return factory.getForwardLink(getOrigin(), forwardChain_,
				getDestination());
	}

	@Override
	public SubPropertyChain getFourthPremise(SubPropertyChain.Factory factory) {
		return factory.getSubPropertyChain(forwardChain_,
				composition_.getSuffixChain());
	}

	public IndexedSubObjectPropertyOfAxiom getFifthPremise(
			IndexedSubObjectPropertyOfAxiom.Factory factory) {
		return factory.getIndexedSubObjectPropertyOfAxiom(reason_, composition_,
				getConclusionRelation());
	}

	@Override
	public int getPremiseCount() {
		return 5;
	}

	@Override
	public Conclusion getPremise(int index, Factory factory) {
		switch (index) {
		case 0:
			return getFirstPremise(factory);
		case 1:
			return getSecondPremise(factory);
		case 2:
			return getThirdPremise(factory);
		case 3:
			return getFourthPremise(factory);
		case 4:
			return getFifthPremise(factory);
		default:
			return failGetPremise(index);
		}
	}

	@Override
	public final <O> O accept(BackwardLinkInference.Visitor<O> visitor) {
		return visitor.visit(this);
	}

	@Override
	public final <O> O accept(LinkComposition.Visitor<O> visitor) {
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

		public O visit(BackwardLinkComposition inference);

	}

}
