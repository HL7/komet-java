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
import org.semanticweb.elk.reasoner.taxonomy.model.Taxonomy;
import org.semanticweb.elk.reasoner.taxonomy.model.TaxonomyNode;

/**
 * A fresh TaxonomyNode containing an object that does not occur in a taxonomy.
 * Such nodes are returned to queries when FreshEntityPolicy is set to ALLOW.
 * 
 * @author Frantisek Simancik
 * @author "Yevgeny Kazakov"
 * @author Peter Skocovsky
 * 
 * @param <T>
 *            the type of objects in this node
 */
public class FreshTaxonomyNode<T extends ElkEntity> extends FreshNode<T>
		implements TaxonomyNode<T> {

	protected final Taxonomy<T> taxonomy;

	public FreshTaxonomyNode(T member, Taxonomy<T> taxonomy) {
		super(member);
		this.taxonomy = taxonomy;
	}

	@Override
	public Taxonomy<T> getTaxonomy() {
		return taxonomy;
	}

	@Override
	public Set<? extends TaxonomyNode<T>> getDirectSuperNodes() {
		return Collections.singleton(taxonomy.getTopNode());
	}

	@Override
	public Set<? extends TaxonomyNode<T>> getAllSuperNodes() {
		return Collections.singleton(taxonomy.getTopNode());
	}

	@Override
	public Set<? extends TaxonomyNode<T>> getDirectSubNodes() {
		return Collections.singleton(taxonomy.getBottomNode());
	}

	@Override
	public Set<? extends TaxonomyNode<T>> getAllSubNodes() {
		return Collections.singleton(taxonomy.getBottomNode());
	}

}
