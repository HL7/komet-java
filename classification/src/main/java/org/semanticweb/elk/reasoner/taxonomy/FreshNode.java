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

import java.util.Iterator;

import org.semanticweb.elk.owl.interfaces.ElkEntity;
import org.semanticweb.elk.reasoner.taxonomy.model.ComparatorKeyProvider;
import org.semanticweb.elk.reasoner.taxonomy.model.Node;
import org.semanticweb.elk.util.collections.Operations;

/**
 * A fresh Node containing an object that does not occur in a taxonomy. Such
 * nodes are returned to queries when FreshEntityPolicy is set to ALLOW.
 * 
 * @author Frantisek Simancik
 * @author "Yevgeny Kazakov"
 * @author Peter Skocovsky
 * 
 * @param <T>
 *            the type of objects in this node
 */
public class FreshNode<T extends ElkEntity> implements Node<T> {

	private final T member_;

	public FreshNode(T member) {
		this.member_ = member;
	}
	
	public T getMember() {
		return member_;
	}

	@Override
	public ComparatorKeyProvider<ElkEntity> getKeyProvider() {
		return null;
	}
	
	@Override
	public Iterator<T> iterator() {
		return Operations.singletonIterator(member_);
	}
	
	@Override
	public boolean contains(T member) {
		return member == null
				? this.member_ == null
				: member.getIri().equals(this.member_.getIri());
	}
	
	@Override
	public int size() {
		return 1;
	}
	
	@Override
	public T getCanonicalMember() {
		return member_;
	}

}
