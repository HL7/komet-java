/*
 * #%L
 * ELK OWL Model Implementation
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
/**
 * 
 */
package org.semanticweb.elk.owl.implementation;

import java.util.List;

import org.semanticweb.elk.owl.interfaces.ElkDataPropertyExpression;
import org.semanticweb.elk.owl.interfaces.ElkDataPropertyListRestrictionQualified;
import org.semanticweb.elk.owl.interfaces.ElkDataRange;
import org.semanticweb.elk.owl.visitors.ElkClassExpressionVisitor;
import org.semanticweb.elk.owl.visitors.ElkDataPropertyListRestrictionQualifiedVisitor;
import org.semanticweb.elk.owl.visitors.ElkObjectVisitor;

/**
 * Implementation of {@link ElkDataPropertyListRestrictionQualified}
 * 
 * @author Pavel Klinov
 * 
 *         pavel.klinov@uni-ulm.de
 * 
 */
public abstract class ElkDataPropertyListRestrictionQualifiedImpl implements
		ElkDataPropertyListRestrictionQualified {

	private final List<? extends ElkDataPropertyExpression> dataProperties_;
	private final ElkDataRange dataRange_;

	ElkDataPropertyListRestrictionQualifiedImpl(
			List<? extends ElkDataPropertyExpression> dataProps,
			ElkDataRange dataRange) {
		this.dataProperties_ = dataProps;
		this.dataRange_ = dataRange;
	}

	@Override
	public List<? extends ElkDataPropertyExpression> getDataPropertyExpressions() {
		return dataProperties_;
	}

	@Override
	public ElkDataRange getDataRange() {
		return dataRange_;
	}

	@Override
	public <O> O accept(ElkObjectVisitor<O> visitor) {
		return accept((ElkDataPropertyListRestrictionQualifiedVisitor<O>) visitor);
	}

	@Override
	public <O> O accept(ElkClassExpressionVisitor<O> visitor) {
		return accept((ElkDataPropertyListRestrictionQualifiedVisitor<O>) visitor);
	}
}