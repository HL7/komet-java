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
package org.semanticweb.elk.reasoner.taxonomy;

import java.util.Collections;
import java.util.Set;

import org.semanticweb.elk.owl.interfaces.ElkEntity;
import org.semanticweb.elk.reasoner.taxonomy.model.InstanceNode;
import org.semanticweb.elk.reasoner.taxonomy.model.InstanceTaxonomy;
import org.semanticweb.elk.reasoner.taxonomy.model.TypeNode;

/**
 * A fresh InstanceNode containing an object that does not occur in a taxonomy.
 * Such nodes are returned to queries when FreshEntityPolicy is set to ALLOW.
 * 
 * @author Frantisek Simancik
 * @author "Yevgeny Kazakov"
 * @author Peter Skocovsky
 * 
 * @param <T>
 *            the types of objects in this node
 * @param <I>
 *            the type of instances in this node
 */
public class FreshInstanceNode<T extends ElkEntity, I extends ElkEntity>
		extends FreshNode<I> implements InstanceNode<T, I> {

	protected InstanceTaxonomy<T, I> taxonomy;

	public FreshInstanceNode(I member, InstanceTaxonomy<T, I> taxonomy) {
		super(member);
		this.taxonomy = taxonomy;
	}

	@Override
	public InstanceTaxonomy<T, I> getTaxonomy() {
		return taxonomy;
	}

	@Override
	public Set<? extends TypeNode<T, I>> getDirectTypeNodes() {
		return Collections.singleton(taxonomy.getTopNode());
	}

	@Override
	public Set<? extends TypeNode<T, I>> getAllTypeNodes() {
		return Collections.singleton(taxonomy.getTopNode());
	}

}
