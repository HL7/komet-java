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
package org.semanticweb.elk.reasoner.taxonomy.impl;

import org.semanticweb.elk.reasoner.taxonomy.model.NodeFactory;
import org.semanticweb.elk.reasoner.taxonomy.model.NodeStore;

/**
 * Generic node store that can be modified.
 * 
 * @author Peter Skocovsky
 *
 * @param <T>
 *            The type of members of the nodes in this store.
 * @param <N>
 *            The type of nodes in this store.
 */
public interface UpdateableNodeStore<T, N extends UpdateableNode<T>>
		extends NodeStore<T, N> {

	/**
	 * Returns the node that contains the members provided in arguments. If such
	 * a node is not in this store, it is created using the provided factory and
	 * inserted into this store.
	 * 
	 * @param members
	 *            The members of the returned node.
	 * @param size
	 *            The number of the members.
	 * @param factory
	 *            The factory creating nodes that can be stored in this store.
	 * @return The node containing the provided members.
	 */
	N getCreateNode(Iterable<? extends T> members, int size,
			NodeFactory<T, N> factory);

	/**
	 * Removes the node containing the specified member from the taxonomy.
	 * 
	 * @param member
	 *            The member whose node should be removed.
	 * @return <code>true</code> if and only if some node was removed.
	 */
	boolean removeNode(T member);

}
