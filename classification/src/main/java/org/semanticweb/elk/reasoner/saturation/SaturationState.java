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

import java.util.Collection;

import org.semanticweb.elk.reasoner.indexing.model.IndexedContextRoot;
import org.semanticweb.elk.reasoner.indexing.model.OntologyIndex;
import org.semanticweb.elk.reasoner.saturation.context.Context;

/**
 * Represents the state of saturation containing information about
 * {@link Context}s and their assignment to {@link IndexedContextRoot}
 * 
 * @author Pavel Klinov
 * 
 *         pavel.klinov@uni-ulm.de
 * 
 * @author "Yevgeny Kazakov"
 *
 * @param <C>
 *            the type of contexts maintained by this {@link SaturationState}
 */
public interface SaturationState<C extends Context> {

	/**
	 * @return the unmodifiable {@link Collection} of {@link Context} stored in
	 *         this {@link SaturationState}
	 */
	public Collection<C> getContexts();

	/**
	 * @param root
	 *            the {@link IndexedContextRoot} for which to find the
	 *            {@link Context}
	 * @return the {@link Context} in this {@link SaturationState} with the
	 *         given {@link IndexedContextRoot} or {@code null} if there exists
	 *         no such {@link Context}. There can be at most one {@link Context}
	 *         for any given {@link IndexedContextRoot}.
	 * @see Context#getRoot()
	 */
	public C getContext(IndexedContextRoot root);

	/**
	 * @return the {@link OntologyIndex} associated with this
	 *         {@link SaturationState}
	 */
	public OntologyIndex getOntologyIndex();

	/**
	 * @return the unmodifiable {@link Collection} of {@link Context}s in this
	 *         {@link SaturationState} that are not saturated, i.e., for which
	 *         {@link Context#isSaturated()} returns {@code false}
	 */
	public Collection<C> getNotSaturatedContexts();

	/**
	 * @return the value of the counter that is incremented right after a
	 *         context becomes marked as non-saturated, e.g., as the result of
	 *         calling
	 *         {@link SaturationStateWriter#markAsNotSaturated(IndexedContextRoot)}.
	 *         The value never decreases and is never greater than the value of
	 *         #getContextSetSaturatedCount().
	 */
	int getContextMarkNonSaturatedCount();

	/**
	 * @return the value of the counter that is incremented right after a
	 *         context becomes marked as saturated, e.g., as the result of
	 *         calling {@link #setContextsSaturated(int)}. The value never
	 *         decreases and is never smaller than the value of
	 *         #getContextMarkNonSaturatedCount().
	 */
	int getContextSetSaturatedCount();

	/**
	 * Marks non-saturated contexts of this {@link SaturationState} as saturated
	 * until the value of {@link #getContextSetSaturatedCount()} reaches the
	 * given limit or all contexts are marked as saturated. The {@link Context}s
	 * are set in the order in which they become non-saturated or created.
	 *
	 * @param saturatedContextLimit
	 *            the limit on the value {@link #getContextSetSaturatedCount()}
	 *            that can be achieved after calling this method. If this method
	 *            is called concurrently from multiple threads, the maximal of
	 *            the set limits apply.
	 * 
	 * @see Context#isSaturated()
	 */
	public void setContextsSaturated(int saturatedContextLimit);

	/**
	 * @param contextModificationListener
	 * @return a new {@link SaturationStateWriter} that can only modify
	 *         {@link Context}s in this {@link SaturationState}, but cannot
	 *         create new ones. When a {@link Context} is modified by the
	 *         {@link SaturationStateWriter}, it becomes not saturated according
	 *         to {@link Context#isSaturated()} if it was not already so.
	 *         Whenever a {@link Context} becomes not saturated using this
	 *         {@link SaturationStateWriter}, the provided
	 *         {@link ContextModificationListener} is called. The returned
	 *         {@link SaturationStateWriter} is not thread safe and should not
	 *         be used from more than one thread.
	 * 
	 * @see #getContextModifyingWriter()
	 * @see #getContextCreatingWriter(ContextCreationListener,
	 *      ContextModificationListener)
	 * @see Context#isSaturated()
	 */
	public SaturationStateWriter<C> getContextModifyingWriter(
			ContextModificationListener contextModificationListener);

	/**
	 * @return a new {@link SaturationStateWriter} for this
	 *         {@link SaturationState} that can modify but cannot create new
	 *         {@link Context}s. The returned {@link SaturationStateWriter} is
	 *         not thread safe and should not be used from more than one thread.
	 * 
	 * @see #getContextModifyingWriter(ContextModificationListener)
	 * @see #getContextCreatingWriter()
	 */
	public SaturationStateWriter<C> getContextModifyingWriter();

	/**
	 * @param contextCreationListener
	 * @param contextModificationListener
	 * @return a new {@link SaturationStateWriter} that can modify as well as
	 *         create new {@link Context}s in this {@link SaturationState}. When
	 *         a {@link Context} is modified by the
	 *         {@link SaturationStateWriter}, it becomes not saturated according
	 *         to {@link Context#isSaturated()} if it was not already so.
	 *         Whenever a {@link Context} is created using this
	 *         {@link SaturationStateWriter} the provided
	 *         {@link ContextCreationListener} is called. Whenever a
	 *         {@link Context} becomes not saturated using this
	 *         {@link SaturationStateWriter}, the provided
	 *         {@link ContextModificationListener} is called. The returned
	 *         {@link SaturationStateWriter} is not thread safe and should not
	 *         be used from more than one thread.
	 * 
	 * @see #getContextCreatingWriter()
	 * @see #getContextModifyingWriter(ContextModificationListener)
	 */
	public ContextCreatingSaturationStateWriter<C> getContextCreatingWriter(
			ContextCreationListener contextCreationListener,
			ContextModificationListener contextModificationListener);

	/**
	 * @return a new {@link SaturationStateWriter} for this
	 *         {@link SaturationState} that can modify and create new
	 *         {@link Context}s. The returned {@link SaturationStateWriter} is
	 *         not thread safe and should not be used from more than one thread.
	 * 
	 * @see #getContextCreatingWriter(ContextCreationListener,
	 *      ContextModificationListener)
	 * @see #getContextModifyingWriter()
	 */
	public ContextCreatingSaturationStateWriter<C> getContextCreatingWriter();

	/**
	 * Registers a given {@link ChangeListener} with this
	 * {@link SaturationState}
	 * 
	 * @param listener
	 * @return {@code true} if the operation was successful and {@code false}
	 *         otherwise; if {@code false} is return, the listener was not
	 *         registered
	 */
	public boolean addListener(ChangeListener<C> listener);

	/**
	 * Removes a given {@link ChangeListener} from this {@link SaturationState}
	 * 
	 * @param listener
	 * @return {@code true} if the operation was successful and {@code false}
	 *         otherwise; if {@code false} is return, the listener was not
	 *         removed
	 */
	public boolean removeListener(ChangeListener<C> listener);

	/**
	 * The listener for changes in {@link SaturationState}
	 * 
	 * @author Yevgeny Kazakov
	 * 
	 * @param <C>
	 *            the type of contexts maintained by the {@link SaturationState}
	 */
	public interface ChangeListener<C extends Context> {

		/**
		 * Is triggered immediately after a give context is added to the
		 * {@link SaturationState}, i.e., it appears
		 * {@link SaturationState#getContexts()}.
		 * 
		 * @param context
		 */
		void contextAddition(C context);

		/**
		 * Is triggered immediately after all contexts are removed from the
		 * {@link SaturationState}, i.e., {@link SaturationState#getContexts()}
		 * becomes empty.
		 */
		void contextsClear();

		/**
		 * Is triggered immediately after the given context is marked as
		 * saturated, i.e., it disappears from
		 * {@link SaturationState#getNotSaturatedContexts()}.
		 * 
		 * @param context
		 */
		void contextMarkSaturated(C context);

		/**
		 * Is triggered immediately after the given context is marked as
		 * non-saturated, i.e., it appears in
		 * {@link SaturationState#getNotSaturatedContexts()}.
		 * 
		 * @param context
		 */
		void contextMarkNonSaturated(C context);

	}

}
