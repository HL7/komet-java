/*
 * #%L
 * elk-reasoner
 * 
 * $Id$
 * $HeadURL$
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
package org.semanticweb.elk.owl.visitors;

import org.semanticweb.elk.owl.interfaces.ElkAxiom;

/**
 * Visitor pattern interface for instances of {@link ElkAxiom}.
 * 
 * @author Markus Kroetzsch
 * @author Frantisek Simancik
 * @author "Yevgeny Kazakov"
 * 
 * @param <O>
 *            the type of the output of this visitor
 */
public interface ElkAxiomVisitor<O> extends ElkAnnotationAxiomVisitor<O>,
		ElkAssertionAxiomVisitor<O>, ElkClassAxiomVisitor<O>,
		ElkDataPropertyAxiomVisitor<O>, ElkDatatypeDefinitionAxiomVisitor<O>,
		ElkDeclarationAxiomVisitor<O>, ElkHasKeyAxiomVisitor<O>,
		ElkObjectPropertyAxiomVisitor<O>, ElkPropertyAxiomVisitor<O>,
		ElkSWRLRuleVisitor<O> {

	// combined visitor
}
