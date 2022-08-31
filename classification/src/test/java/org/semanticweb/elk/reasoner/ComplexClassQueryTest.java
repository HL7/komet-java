/*
 * #%L
 * ELK Reasoner
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2011 - 2013 Department of Computer Science, University of Oxford
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
package org.semanticweb.elk.reasoner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.elk.exceptions.ElkException;
import org.semanticweb.elk.loading.TestLoader;
import org.semanticweb.elk.owl.interfaces.ElkClass;
import org.semanticweb.elk.owl.interfaces.ElkClassExpression;
import org.semanticweb.elk.owl.interfaces.ElkObject;
import org.semanticweb.elk.owl.interfaces.ElkObjectProperty;
import org.semanticweb.elk.owl.iris.ElkFullIri;
import org.semanticweb.elk.owl.managers.ElkObjectEntityRecyclingFactory;
import org.semanticweb.elk.reasoner.completeness.TestIncompleteness;
import org.semanticweb.elk.reasoner.taxonomy.model.Node;

public class ComplexClassQueryTest {

	final ElkObject.Factory objectFactory = new ElkObjectEntityRecyclingFactory();

	private TestLoader loader;
	private Reasoner reasoner;

	@Before
	public void initReasoner() {
		loader = new TestLoader();
		reasoner = TestReasonerUtils.createTestReasoner(loader);
	}

	@Test
	public void testSimpleSubsumption() throws ElkException {
		ElkClass A = objectFactory.getClass(new ElkFullIri(":A"));
		ElkClass B = objectFactory.getClass(new ElkFullIri(":B"));
		loader.add(objectFactory.getSubClassOfAxiom(A, B));
		assertFalse(isSatisfiable(objectFactory.getObjectIntersectionOf(A,
				objectFactory.getObjectComplementOf(B))));
		assertTrue(isSatisfiable(objectFactory.getObjectIntersectionOf(B,
				objectFactory.getObjectComplementOf(A))));
	}

	@Test
	public void testSatisfiabilityExistential() throws ElkException {
		ElkClass A = objectFactory.getClass(new ElkFullIri(":A"));
		ElkClass B = objectFactory.getClass(new ElkFullIri(":B"));
		loader.add(objectFactory.getSubClassOfAxiom(A,
				objectFactory.getOwlNothing()));
		ElkObjectProperty R = objectFactory
				.getObjectProperty(new ElkFullIri(":R"));
		assertFalse(isSatisfiable(objectFactory.getObjectSomeValuesFrom(R, A)));
		assertTrue(isSatisfiable(objectFactory.getObjectSomeValuesFrom(R, B)));
	}

	@Test
	public void testSatisfiabilityExistentialSubsumption() throws ElkException {
		ElkClass A = objectFactory.getClass(new ElkFullIri(":A"));
		ElkClass B = objectFactory.getClass(new ElkFullIri(":B"));
		loader.add(objectFactory.getSubClassOfAxiom(A, B));
		ElkObjectProperty R = objectFactory
				.getObjectProperty(new ElkFullIri(":R"));
		assertFalse(isSatisfiable(objectFactory.getObjectIntersectionOf(
				objectFactory.getObjectSomeValuesFrom(R, A),
				objectFactory.getObjectComplementOf(
						objectFactory.getObjectSomeValuesFrom(R, B)))));
		assertTrue(isSatisfiable(objectFactory.getObjectIntersectionOf(
				objectFactory.getObjectSomeValuesFrom(R, B),
				objectFactory.getObjectComplementOf(
						objectFactory.getObjectSomeValuesFrom(R, A)))));
	}

	@Test
	public void testSupSubClassConjunction() throws ElkException {
		ElkClass A = objectFactory.getClass(new ElkFullIri(":A"));
		ElkClass B = objectFactory.getClass(new ElkFullIri(":B"));
		ElkClass C = objectFactory.getClass(new ElkFullIri(":C"));
		loader.add(objectFactory.getSubClassOfAxiom(A, B))
				.add((objectFactory.getSubClassOfAxiom(B, C)));

		Set<? extends Node<ElkClass>> superClasses = getSuperClasses(
				objectFactory.getObjectIntersectionOf(B, C), true);
		assertEquals(1, superClasses.size());
		for (Node<ElkClass> node : superClasses) {
			assertTrue(node.contains(C));
		}

		Set<? extends Node<ElkClass>> subClasses = getSubClasses(
				objectFactory.getObjectIntersectionOf(B, C), true);
		assertEquals(1, subClasses.size());
		for (Node<ElkClass> node : subClasses) {
			assertTrue(node.contains(A));
		}

	}

	@Test
	public void testEquivalentClasses() throws ElkException {
		// empty ontology, query for conjunction
		ElkClass A = objectFactory.getClass(new ElkFullIri(":A"));
		ElkClass B = objectFactory.getClass(new ElkFullIri(":B"));
		ElkClassExpression queryExpression = objectFactory
				.getObjectIntersectionOf(A, B);
		assertEquals(0, getEquivalentClasses(queryExpression).size());
		// the following has reproduced Issue 23:
		assertEquals(0, getEquivalentClasses(queryExpression).size());
	}

	/**
	 * This test is supposed to reproduce error where a partially supported
	 * complex class was considered to be equivalent to {@code owl:Thing} by
	 * transitive reduction, because the class and its subsumer
	 * {@code owl:Thing} had the same number of subsumers.
	 * 
	 * @throws ElkException
	 */
	@Test
	public void testPartiallySupportedExpression() throws ElkException {
		final ElkClass A = objectFactory.getClass(new ElkFullIri(":A"));
		final ElkClass B = objectFactory.getClass(new ElkFullIri(":B"));
		final ElkClass C = objectFactory.getClass(new ElkFullIri(":C"));
		final ElkClass D = objectFactory.getClass(new ElkFullIri(":D"));
		final ElkClass top = objectFactory.getOwlThing();
		final ElkClassExpression query = objectFactory.getObjectUnionOf(A, B);

		loader.add(objectFactory.getSubClassOfAxiom(A, C));
		loader.add(objectFactory.getSubClassOfAxiom(B, C));
		// negative occurrence of top, so that it is added to composed subsumers
		loader.add(objectFactory.getSubClassOfAxiom(top, D));

		final Set<? extends Node<ElkClass>> subClasses = getSubClasses(query,
				true);
		assertEquals(2, subClasses.size());
		for (final Node<ElkClass> node : subClasses) {
			assertEquals(1, node.size());
			assertTrue(node.contains(A) != node.contains(B));
		}
	}

	boolean isSatisfiable(ElkClassExpression expression) throws ElkException {
		return TestIncompleteness.getValue(reasoner.isSatisfiable(expression));
	}

	Set<? extends Node<ElkClass>> getSuperClasses(
			ElkClassExpression classExpression, boolean direct)
			throws ElkException {
		return TestIncompleteness
				.getValue(reasoner.getSuperClasses(classExpression, direct));
	}

	Set<? extends Node<ElkClass>> getSubClasses(
			ElkClassExpression classExpression, boolean direct)
			throws ElkException {
		return TestIncompleteness
				.getValue(reasoner.getSubClasses(classExpression, direct));
	}

	Node<ElkClass> getEquivalentClasses(ElkClassExpression classExpression)
			throws ElkException {
		return TestIncompleteness
				.getValue(reasoner.getEquivalentClasses(classExpression));
	}

}
