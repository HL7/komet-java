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
package org.semanticweb.elk.reasoner.taxonomy.impl;

import java.util.Set;

import org.semanticweb.elk.owl.interfaces.ElkEntity;
import org.semanticweb.elk.reasoner.taxonomy.model.GenericTaxonomyNode;
import org.semanticweb.elk.reasoner.taxonomy.model.NonBottomTaxonomyNode;

/**
 * Updateable generic taxonomy node that can be related with other nodes.
 * 
 * @author Pavel Klinov
 * 
 *         pavel.klinov@uni-ulm.de
 * @author Peter Skocovsky
 *
 * @param <T>
 *            The type of members of this nodes.
 * @param <N>
 *            The immutable type of nodes with which this node may be
 *            associated.
 * @param <UN>
 *            The mutable type of nodes with which this node may be associated.
 */
public interface UpdateableTaxonomyNode<
				T extends ElkEntity,
				N extends GenericTaxonomyNode<T, N>,
				UN extends UpdateableTaxonomyNode<T, N, UN>
		>
		extends UpdateableNode<T>, NonBottomTaxonomyNode<T> {

	@Override
	Set<? extends UN> getDirectNonBottomSuperNodes();
	
	@Override
	Set<? extends UN> getDirectNonBottomSubNodes();
	
	/**
	 * Associates this node with its direct super-node.
	 * 
	 * @param superNode
	 *            The super-node with which this node should be associated.
	 */
	void addDirectSuperNode(UN superNode);

	/**
	 * Associates this node with its direct sub-node.
	 * 
	 * @param subNode
	 *            The sub-node with which this node should be associated.
	 */
	void addDirectSubNode(UN subNode);

	/**
	 * Deletes the association between this node and the specified sub-node.
	 * 
	 * @param subNode
	 *            The sub-node with which this node should not be associated.
	 * @return <code>true</code> if and only if this node changed.
	 */
	boolean removeDirectSubNode(UN subNode);

	/**
	 * Deletes the association between this node and the specified super-node.
	 * 
	 * @param superNode
	 *            The super-node with which this node should not be associated.
	 * @return <code>true</code> if and only if this node changed.
	 */
	boolean removeDirectSuperNode(UN superNode);

}
