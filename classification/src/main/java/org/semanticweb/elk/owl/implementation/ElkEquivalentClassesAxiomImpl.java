/*
 * #%L
 * elk-reasoner
 * 
 * $Id: ElkEquivalentClassesAxiom.java 68 2011-06-04 21:49:01Z mak@aifb.uni-karlsruhe.de $
 * $HeadURL: https://elk-reasoner.googlecode.com/svn/trunk/elk-reasoner/src/main/java/org/semanticweb/elk/syntax/ElkEquivalentClassesAxiom.java $
 * %%
 * Copyright (C) 2011 Oxford University Computing Laboratory
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
 * @author Yevgeny Kazakov, Apr 8, 2011
 */
package org.semanticweb.elk.owl.implementation;

import java.util.List;

import org.semanticweb.elk.owl.interfaces.ElkClassExpression;
import org.semanticweb.elk.owl.interfaces.ElkEquivalentClassesAxiom;
import org.semanticweb.elk.owl.visitors.ElkAxiomVisitor;
import org.semanticweb.elk.owl.visitors.ElkClassAxiomVisitor;
import org.semanticweb.elk.owl.visitors.ElkEquivalentClassesAxiomVisitor;
import org.semanticweb.elk.owl.visitors.ElkObjectVisitor;

/**
 * Corresponds to an <a href=
 * "http://www.w3.org/TR/owl2-syntax/#Equivalent_Classes">Equivalent Class
 * Axiom<a> in the OWL 2 specification.
 * 
 * @author Yevgeny Kazakov
 * @author Markus Kroetzsch
 * 
 */
public class ElkEquivalentClassesAxiomImpl extends ElkClassExpressionListObject
		implements ElkEquivalentClassesAxiom {

	ElkEquivalentClassesAxiomImpl(
			List<? extends ElkClassExpression> equivalentClassExpressions) {
		super(equivalentClassExpressions);
	}

	@Override
	public <O> O accept(ElkClassAxiomVisitor<O> visitor) {
		return accept((ElkEquivalentClassesAxiomVisitor<O>) visitor);
	}

	@Override
	public <O> O accept(ElkAxiomVisitor<O> visitor) {
		return accept((ElkEquivalentClassesAxiomVisitor<O>) visitor);
	}

	@Override
	public <O> O accept(ElkObjectVisitor<O> visitor) {
		return accept((ElkEquivalentClassesAxiomVisitor<O>) visitor);
	}

	@Override
	public <O> O accept(ElkEquivalentClassesAxiomVisitor<O> visitor) {
		return visitor.visit(this);
	}

}
