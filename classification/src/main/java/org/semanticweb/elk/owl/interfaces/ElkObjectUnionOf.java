/*
 * #%L
 * elk-reasoner
 * 
 * $Id: ElkObjectIntersectionOf.java 295 2011-08-10 11:43:29Z mak@aifb.uni-karlsruhe.de $
 * $HeadURL: https://elk-reasoner.googlecode.com/svn/trunk/elk-reasoner/src/main/java/org/semanticweb/elk/syntax/interfaces/ElkObjectIntersectionOf.java $
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
 * @author Markus Kroetzsch, Aug 8, 2011
 */
package org.semanticweb.elk.owl.interfaces;

import java.util.List;

import org.semanticweb.elk.owl.visitors.ElkObjectUnionOfVisitor;

/**
 * Corresponds to a
 * <a href= "http://www.w3.org/TR/owl2-syntax/#Union_of_Class_Expressions" >
 * Union of Class Expressions<a> in the OWL 2 specification.
 * 
 * @author Markus Kroetzsch
 */
public interface ElkObjectUnionOf extends ElkClassExpression {

	/**
	 * Get the list of class expressions that this expression refers to. The
	 * order of class expressions does not affect the semantics but it is
	 * relevant to the syntax of OWL.
	 * 
	 * @return list of class expressions
	 */
	public List<? extends ElkClassExpression> getClassExpressions();

	/**
	 * Accept an {@link ElkObjectUnionOfVisitor}.
	 * 
	 * @param visitor
	 *            the visitor that can work with this object type
	 * @return the output of the visitor
	 */
	public <O> O accept(ElkObjectUnionOfVisitor<O> visitor);

	/**
	 * A factory for creating instances
	 * 
	 * @author Yevgeny Kazakov
	 *
	 */
	interface Factory {

		/**
		 * Create an {@link ElkObjectUnionOf}.
		 * 
		 * @param first
		 *            the first {@link ElkClassExpression} of the union for
		 *            which the object should be created
		 * @param second
		 *            the second {@link ElkClassExpression} of the union for
		 *            which the object should be created
		 * @param other
		 *            other {@link ElkClassExpression}s of the union for which
		 *            the object should be created
		 * @return an {@link ElkObjectUnionOf} corresponding to the input
		 */
		public ElkObjectUnionOf getObjectUnionOf(
				ElkClassExpression first,
				ElkClassExpression second,
				ElkClassExpression... other);

		/**
		 * Create an {@link ElkObjectUnionOf}.
		 * 
		 * @param members
		 *            the {@link ElkClassExpression}s for which the object
		 *            should be created
		 * @return an {@link ElkObjectUnionOf} corresponding to the input
		 */
		public ElkObjectUnionOf getObjectUnionOf(
				List<? extends ElkClassExpression> members);
	}

}
