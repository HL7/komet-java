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

import org.semanticweb.elk.owl.interfaces.ElkClassExpression;
import org.semanticweb.elk.owl.interfaces.ElkObjectComplementOf;
import org.semanticweb.elk.owl.visitors.ElkClassExpressionVisitor;
import org.semanticweb.elk.owl.visitors.ElkObjectComplementOfVisitor;
import org.semanticweb.elk.owl.visitors.ElkObjectVisitor;

/**
 * ELK implementation of ElkObjectComplementOf.
 * 
 * @author Markus Kroetzsch
 * 
 */
public class ElkObjectComplementOfImpl extends ElkObjectImpl implements
		ElkObjectComplementOf {

	private final ElkClassExpression classExpression_;

	ElkObjectComplementOfImpl(ElkClassExpression negated) {
		this.classExpression_ = negated;
	}

	@Override
	public ElkClassExpression getClassExpression() {
		return classExpression_;
	}

	@Override
	public <O> O accept(ElkClassExpressionVisitor<O> visitor) {
		return accept((ElkObjectComplementOfVisitor<O>) visitor);
	}

	@Override
	public <O> O accept(ElkObjectVisitor<O> visitor) {
		return accept((ElkObjectComplementOfVisitor<O>) visitor);
	}

	@Override
	public <O> O accept(ElkObjectComplementOfVisitor<O> visitor) {
		return visitor.visit(this);
	}

}
