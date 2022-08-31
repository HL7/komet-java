/*-
 * #%L
 * ELK Reasoner Core
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
package org.semanticweb.elk.reasoner.stages;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.liveontologies.puli.Proof;
import org.liveontologies.puli.Proofs;
import org.semanticweb.elk.loading.AbstractEntailmentQueryLoader;
import org.semanticweb.elk.loading.ElkLoadingException;
import org.semanticweb.elk.loading.EntailmentQueryLoader;
import org.semanticweb.elk.owl.interfaces.ElkAxiom;
import org.semanticweb.elk.owl.visitors.ElkAxiomVisitor;
import org.semanticweb.elk.reasoner.completeness.Feature;
import org.semanticweb.elk.reasoner.completeness.IncompletenessManager;
import org.semanticweb.elk.reasoner.completeness.IncompletenessMonitor;
import org.semanticweb.elk.reasoner.completeness.OccurrenceListener;
import org.semanticweb.elk.reasoner.completeness.OccurrenceRegistry;
import org.semanticweb.elk.reasoner.completeness.OccurrencesInEntailmentQuery;
import org.semanticweb.elk.reasoner.config.ReasonerConfiguration;
import org.semanticweb.elk.reasoner.consistency.ConsistencyCheckingState;
import org.semanticweb.elk.reasoner.entailments.InconsistencyProofWrapper;
import org.semanticweb.elk.reasoner.entailments.model.Entailment;
import org.semanticweb.elk.reasoner.entailments.model.EntailmentInference;
import org.semanticweb.elk.reasoner.indexing.model.IndexedContextRoot;
import org.semanticweb.elk.reasoner.query.ElkQueryException;
import org.semanticweb.elk.reasoner.query.IndexedEntailmentQuery;
import org.semanticweb.elk.reasoner.query.VerifiableQueryResult;
import org.semanticweb.elk.reasoner.saturation.SaturationState;
import org.semanticweb.elk.reasoner.saturation.conclusions.model.SaturationConclusion;
import org.semanticweb.elk.reasoner.saturation.context.Context;
import org.semanticweb.elk.util.collections.ArrayHashMap;
import org.semanticweb.elk.util.collections.ArrayHashSet;
import org.semanticweb.elk.util.collections.Condition;
import org.semanticweb.elk.util.collections.Evictor;
import org.semanticweb.elk.util.collections.Operations;
import org.semanticweb.elk.util.concurrent.computation.InterruptMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Predicate;

/**
 * Keeps track of axioms that were queried for entailment.
 * 
 * @author Peter Skocovsky
 */
public class EntailmentQueryState implements EntailmentQueryLoader.Factory {

	private static final Logger LOGGER_ = LoggerFactory
			.getLogger(EntailmentQueryState.class);

	/**
	 * Maps axioms that were queried to the states of their query.
	 */
	private final Map<ElkAxiom, QueryState> queried_ = new ConcurrentHashMap<ElkAxiom, QueryState>();

	/**
	 * Contains axioms that were queried but not loaded.
	 */
	private final Queue<ElkAxiom> toLoad_ = new ConcurrentLinkedQueue<ElkAxiom>();

	/**
	 * Manages eviction from {@link #queried_}.
	 */
	private final Evictor<QueryState> queriedEvictor_;

	/**
	 * The axioms that were registered by the last call.
	 */
	private final Set<ElkAxiom> lastQueries_ = new ArrayHashSet<ElkAxiom>();

	/**
	 * A manager to keep track of incompleteness for queries
	 */
	private final IncompletenessManager incompletenessManager_;

	/**
	 * State of the query of a particular axiom. There are two forbidden states:
	 * <ul>
	 * <li>when an axiom does not have a state, it must not be loaded,
	 * <li>when {@link #isLoaded} is {@code true} then {@link #indexed} is not
	 * {@code null},
	 * </ul>
	 * 
	 * @author Peter Skocovsky
	 */
	private class QueryState implements VerifiableQueryResult {

		/**
		 * The query for which the entailment is proved
		 */
		private final ElkAxiom query_;

		/**
		 * Whether the queried axiom was loaded (whether it was attempted to
		 * index it). If this is {@code false}, then {@link #indexed} must be
		 * {@code null}.
		 */
		boolean isLoaded = false;
		/**
		 * Results of indexing of the entailment query. If this is not
		 * {@code null}, then {@link #isLoaded} must be {@code true}.
		 */
		IndexedEntailmentQuery<? extends Entailment> indexed = null;
		/**
		 * Whether the query is locked. While this is true and {@link #indexed}
		 * is not {@code null}, the query results must be available.
		 */
		int lockedCount = 0;
		/**
		 * How many times an {@link Feature} occurs in the query.
		 */
		OccurrenceRegistry occurrences = new OccurrenceRegistry();

		/**
		 * An {@link IncompletenessMonitor} for entailment of this query
		 */
		private final IncompletenessMonitor queryIncompletenessMonitor_;

		QueryState(ElkAxiom query) {
			this.query_ = query;
			this.queryIncompletenessMonitor_ = incompletenessManager_
					.getQueryMonitor(
							new OccurrencesInEntailmentQuery(query,
									occurrences));
		}

		@Override
		public ElkAxiom getQuery() {
			return this.query_;
		}

		@Override
		public Entailment getEntailment() throws ElkQueryException {
			if (indexed == null) {
				// query could not be indexed and therefore proved
				return null;
			}
			// else
			return indexed.getQuery();
		}

		@Override
		public boolean entailmentProved() throws ElkQueryException {
			if (indexed == null) {
				return false;
			}
			// else
			// TODO: cache result?
			return Proofs.isDerivable(getEvidence(true), indexed.getQuery());
		}

		@Override
		public IncompletenessMonitor getIncompletenessMonitor() {
			return queryIncompletenessMonitor_;
		}

		@SuppressWarnings("unchecked")
		@Override
		public Proof<EntailmentInference> getEvidence(final boolean onlyOne)
				throws ElkQueryException {
			if (indexed == null) {
				// not indexed => no proof
				return Proofs.EMPTY_PROOF;
			}
			// else

			final Proof<EntailmentInference> inconsistencyEvidence = new InconsistencyProofWrapper(
					consistencyCheckingState_.getEvidence(onlyOne));

			if (consistencyCheckingState_.isInconsistent() && onlyOne) {
				return inconsistencyEvidence;
			}
			// else

			final Proof<EntailmentInference> entailmentEvidence = indexed
					.getEvidence(onlyOne, saturationState_, conclusionFactory_);

			return Proofs.union(inconsistencyEvidence, entailmentEvidence);
		}

		public synchronized boolean lock() {
			final boolean wasLocked = isLocked();
			lockedCount++;
			return wasLocked != isLocked();
		}

		/**
		 * @return {@code true} iff the lock is locked.
		 */
		public synchronized boolean isLocked() {
			return lockedCount > 0;
		}

		@Override
		public synchronized boolean unlock() {
			if (!isLocked()) {
				return false;
			}
			// else
			lockedCount--;
			return !isLocked();
		}

	}

	private final SaturationState<? extends Context> saturationState_;

	private final ConsistencyCheckingState consistencyCheckingState_;

	private final SaturationConclusion.Factory conclusionFactory_;

	public <C extends Context> EntailmentQueryState(
			final ReasonerConfiguration config,
			final SaturationState<C> saturationState,
			final ConsistencyCheckingState consistencyCheckingState,
			final SaturationConclusion.Factory factory,
			final IncompletenessManager incompletenessManager) {
		this.saturationState_ = saturationState;
		this.consistencyCheckingState_ = consistencyCheckingState;
		this.conclusionFactory_ = factory;
		final Object builder = config
				.getParameter(ReasonerConfiguration.ENTAILMENT_QUERY_EVICTOR);
		LOGGER_.debug("{} = {}", ReasonerConfiguration.ENTAILMENT_QUERY_EVICTOR,
				builder);
		this.queriedEvictor_ = ((Evictor.Builder) builder).build();
		this.incompletenessManager_ = incompletenessManager;
	}

	/**
	 * Registers the supplied axioms for querying. If all necessary stages are
	 * run after calling this method for some axioms, then
	 * {@link #isEntailed(Iterable)} for any of these axioms will not throw
	 * {@link ElkQueryException}.
	 * 
	 * @param axioms
	 */
	void registerQueries(final Iterable<? extends ElkAxiom> axioms) {
		lastQueries_.clear();
		for (final ElkAxiom axiom : axioms) {

			LOGGER_.trace("entailment query registered {}", axiom);

			lastQueries_.add(axiom);

			QueryState state = queried_.get(axiom);
			if (state == null) {
				state = new QueryState(axiom);
				queried_.put(axiom, state);
				toLoad_.add(axiom);
			}
			queriedEvictor_.add(state);
		}
	}

	public OccurrenceListener getOccurrenceListener() {
		return occurrenceListener_;
	}

	@Override
	public EntailmentQueryLoader getQueryLoader(
			final InterruptMonitor interrupter) {
		return new Loader(interrupter);
	}

	private final QueryOccurrenceListener occurrenceListener_ = new QueryOccurrenceListener();

	private class QueryOccurrenceListener implements OccurrenceListener {

		// Indexing state.
		private QueryState beingLoaded_ = null;

		@Override
		public void occurrenceChanged(Feature occurrence, int increment) {
			beingLoaded_.occurrences.occurrenceChanged(occurrence, increment);
		}

	}

	private class Loader extends AbstractEntailmentQueryLoader {

		public Loader(final InterruptMonitor interrupter) {
			super(interrupter);
		}

		@Override
		public void load(
				final ElkAxiomVisitor<IndexedEntailmentQuery<? extends Entailment>> inserter,
				final ElkAxiomVisitor<IndexedEntailmentQuery<? extends Entailment>> deleter)
				throws ElkLoadingException {

			// First evict and unload.
			final Iterator<QueryState> evicted = queriedEvictor_
					.evict(doNotEvict_);
			while (evicted.hasNext()) {
				final QueryState state = evicted.next();
				queried_.remove(state.getQuery());
				if (state.isLoaded) {
					occurrenceListener_.beingLoaded_ = state;
					state.getQuery().accept(deleter);
					state.indexed = null;
					state.isLoaded = false;
				}
			}

			/*
			 * Load all registered queries that are not loaded and assign
			 * state.indexed if successful.
			 */
			for (ElkAxiom axiom; (axiom = toLoad_.poll()) != null;) {
				final QueryState state = queried_.get(axiom);
				occurrenceListener_.beingLoaded_ = state;
				state.indexed = axiom.accept(inserter);
				state.isLoaded = true;

				if (isInterrupted()) {
					return;
				}
			}

		}

		@Override
		public boolean isLoadingFinished() {
			return toLoad_.isEmpty();
		}

	}

	private final Predicate<QueryState> doNotEvict_ = new Predicate<QueryState>() {
		@Override
		public boolean test(final QueryState state) {
			return state.isLocked() || lastQueries_.contains(state.getQuery());
		}
	};

	private final Condition<IndexedContextRoot> IS_NOT_SATURATED = new Condition<IndexedContextRoot>() {
		@Override
		public boolean holds(final IndexedContextRoot root) {
			final Context context = saturationState_.getContext(root);
			return context == null || !context.isInitialized()
					|| !context.isSaturated();
		}
	};

	private final Operations.Transformation<QueryState, Iterable<? extends IndexedContextRoot>> POSITIVELY_INDEXED = new Operations.Transformation<QueryState, Iterable<? extends IndexedContextRoot>>() {
		@Override
		public Iterable<? extends IndexedContextRoot> transform(
				final QueryState state) {
			if (state.indexed == null) {
				return null;
			}
			// else
			final Collection<? extends IndexedContextRoot> roots = state.indexed
					.getPositivelyIndexed();
			return Operations.filter(roots, IS_NOT_SATURATED);
		}
	};

	/**
	 * @return {@link IndexedContextRoot} that are needed to answer the
	 *         registered entailment queries and not saturated.
	 */
	Collection<IndexedContextRoot> getNotSaturatedPositivelyIndexedRoots() {
		int sizeUpper = 0;
		for (final QueryState state : queried_.values()) {
			if (state.indexed != null) {
				sizeUpper += state.indexed.getPositivelyIndexed().size();
			}
		}
		final Iterable<IndexedContextRoot> result = Operations
				.concat(Operations.map(queried_.values(), POSITIVELY_INDEXED));
		return Operations.getCollection(result, sizeUpper);
	}

	/**
	 * Decides whether the supplied {@code axioms} are entailed. If some of the
	 * supplied axioms was not registered by {@link #registerQueries(Iterable)}.
	 * 
	 * @param axioms
	 *            Entailment of what axioms is queried.
	 * @return A map from each queried axiom to the result of entailment query
	 *         for that axiom.
	 * @throws ElkQueryException
	 *             When some of the axioms was not registered by
	 *             {@link #registerQueries(Iterable)}.
	 */
	Map<ElkAxiom, VerifiableQueryResult> isEntailed(
			final Iterable<? extends ElkAxiom> axioms)
			throws ElkQueryException {

		final Map<ElkAxiom, VerifiableQueryResult> results = new ArrayHashMap<ElkAxiom, VerifiableQueryResult>();

		for (final ElkAxiom axiom : axioms) {
			final QueryState state = queried_.get(axiom);
			if (state == null) {
				throw new ElkQueryException(
						"Query was not registered: " + axiom);
			}
			// else
			state.lock();
			results.put(axiom, state);
		}

		return results;
	}

}
