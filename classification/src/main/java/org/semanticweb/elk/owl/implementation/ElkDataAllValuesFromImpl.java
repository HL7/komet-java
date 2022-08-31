/*
 * #%L
 * ELK Reasoner
 * 
 * $Id$
 * $HeadURL$
 * %%
 * Copyright (C) 2011 Department of Computer Science, University of Oxford
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
package org.semanticweb.elk.owl.implementation;

import java.util.List;

import org.semanticweb.elk.owl.interfaces.ElkDataAllValuesFrom;
import org.semanticweb.elk.owl.interfaces.ElkDataPropertyExpression;
import org.semanticweb.elk.owl.interfaces.ElkDataRange;
import org.semanticweb.elk.owl.visitors.ElkDataAllValuesFromVisitor;
import org.semanticweb.elk.owl.visitors.ElkDataPropertyListRestrictionQualifiedVisitor;

/**
 * Implementation of {@link ElkDataAllValuesFrom}.
 * 
 * @author Markus Kroetzsch
 * @author "Yevgeny Kazakov"
 * 
 */
public class ElkDataAllValuesFromImpl extends
		ElkDataPropertyListRestrictionQualifiedImpl implements
		ElkDataAllValuesFrom {

	ElkDataAllValuesFromImpl(
			List<? extends ElkDataPropertyExpression> properties,
			ElkDataRange range) {
		super(properties, range);
	}

	@Override
	public <O> O accept(
			ElkDataPropertyListRestrictionQualifiedVisitor<O> visitor) {
		return accept((ElkDataAllValuesFromVisitor<O>) visitor);
	}

	@Override
	public <O> O accept(ElkDataAllValuesFromVisitor<O> visitor) {
		return visitor.visit(this);
	}

}
