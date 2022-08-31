/*
 * #%L
 * elk-reasoner
 * 
 * $Id: ElkObjectIntersectionOf.java 297 2011-08-10 14:42:57Z mak@aifb.uni-karlsruhe.de $
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

import org.semanticweb.elk.owl.visitors.ElkObjectIntersectionOfVisitor;

/**
 * Corresponds to an <a href=
 * "http://www.w3.org/TR/owl2-syntax/#Intersection_of_Class_Expressions" >
 * Intersection of Class Expressions<a> in the OWL 2 specification.
 * 
 * @author Markus Kroetzsch
 */
public interface ElkObjectIntersectionOf extends ElkClassExpression {

	/**
	 * Get the list of class expressions that this expression refers to. The
	 * order of class expressions does not affect the semantics but it is
	 * relevant to the syntax of OWL.
	 * 
	 * @return list of class expressions
	 */
	public List<? extends ElkClassExpression> getClassExpressions();

	/**
	 * Accept an {@link ElkObjectIntersectionOfVisitor}.
	 * 
	 * @param visitor
	 *            the visitor that can work with this object type
	 * @return the output of the visitor
	 */
	public <O> O accept(ElkObjectIntersectionOfVisitor<O> visitor);

	/**
	 * A factory for creating instances
	 * 
	 * @author Yevgeny Kazakov
	 *
	 */
	interface Factory {

		/**
		 * Create an {@link ElkObjectIntersectionOf}.
		 * 
		 * @param first
		 *            the first {@link ElkClassExpression} for which the object
		 *            should be created
		 * @param second
		 *            the second {@link ElkClassExpression} for which the object
		 *            should be created
		 * @param other
		 *            other {@link ElkClassExpression}s for which the object
		 *            should be created
		 * @return an {@link ElkObjectIntersectionOf} corresponding to the input
		 */
		public ElkObjectIntersectionOf getObjectIntersectionOf(
				ElkClassExpression first,
				ElkClassExpression second,
				ElkClassExpression... other);

		/**
		 * Create an {@link ElkObjectIntersectionOf}.
		 * 
		 * @param members
		 *            the {@link ElkClassExpression}s for which the object
		 *            should be created
		 * @return an {@link ElkObjectIntersectionOf} corresponding to the input
		 */
		public ElkObjectIntersectionOf getObjectIntersectionOf(
				List<? extends ElkClassExpression> members);

	}

}
