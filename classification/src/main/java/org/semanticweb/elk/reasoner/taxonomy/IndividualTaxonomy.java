/*
 * #%L
 * ELK Reasoner
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2011 - 2016 Department of Computer Science, University of Oxford
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

import java.util.Set;

import org.semanticweb.elk.owl.interfaces.ElkClass;
import org.semanticweb.elk.owl.interfaces.ElkEntity;
import org.semanticweb.elk.owl.interfaces.ElkNamedIndividual;
import org.semanticweb.elk.owl.predefined.PredefinedElkClassFactory;
import org.semanticweb.elk.reasoner.taxonomy.impl.AbstractDistinctBottomTaxonomy;
import org.semanticweb.elk.reasoner.taxonomy.impl.AbstractUpdateableGenericInstanceTaxonomy;
import org.semanticweb.elk.reasoner.taxonomy.impl.BottomGenericTypeNode;
import org.semanticweb.elk.reasoner.taxonomy.impl.ConcurrentNodeStore;
import org.semanticweb.elk.reasoner.taxonomy.impl.IndividualNode;
import org.semanticweb.elk.reasoner.taxonomy.impl.NonBottomGenericTypeNode;
import org.semanticweb.elk.reasoner.taxonomy.model.ComparatorKeyProvider;
import org.semanticweb.elk.reasoner.taxonomy.model.GenericInstanceNode;
import org.semanticweb.elk.reasoner.taxonomy.model.GenericTypeNode;
import org.semanticweb.elk.reasoner.taxonomy.model.InstanceTaxonomy;
import org.semanticweb.elk.reasoner.taxonomy.model.TaxonomyNodeFactory;

/**
 * Instance taxonomy that is suitable for concurrent processing. Taxonomy
 * objects are only constructed for consistent ontologies, and some consequences
 * of this are hardcoded here.
 * <p>
 * This class extends an implementation of a class taxonomy, so it may be
 * suboptimal for use with ontologies with no individuals.
 * 
 * @author Peter Skocovsky
 */
public class IndividualTaxonomy
		extends AbstractUpdateableGenericInstanceTaxonomy<
				ElkClass,
				ElkNamedIndividual,
				GenericTypeNode.Projection<ElkClass, ElkNamedIndividual>,
				GenericInstanceNode.Projection<ElkClass, ElkNamedIndividual>,
				NonBottomGenericTypeNode.Projection<ElkClass, ElkNamedIndividual>,
				IndividualNode.Projection2<ElkClass, ElkNamedIndividual>
		> {

	private final GenericTypeNode.Projection<ElkClass, ElkNamedIndividual> bottomNode_;
	
	public IndividualTaxonomy(final PredefinedElkClassFactory elkFactory,
			final ComparatorKeyProvider<ElkEntity> classKeyProvider,
			final ComparatorKeyProvider<ElkNamedIndividual> instanceKeyProvider) {
		super(
				new ConcurrentNodeStore<
						ElkClass,
						NonBottomGenericTypeNode.Projection<ElkClass, ElkNamedIndividual>
				>(classKeyProvider),
				new TaxonomyNodeFactory<
						ElkClass,
						NonBottomGenericTypeNode.Projection<ElkClass, ElkNamedIndividual>,
						AbstractDistinctBottomTaxonomy<
								ElkClass,
								GenericTypeNode.Projection<ElkClass, ElkNamedIndividual>,
								NonBottomGenericTypeNode.Projection<ElkClass, ElkNamedIndividual>
						>
				>() {
					@Override
					public NonBottomGenericTypeNode.Projection<ElkClass, ElkNamedIndividual> createNode(
							final Iterable<? extends ElkClass> members, final int size,
							final AbstractDistinctBottomTaxonomy<
									ElkClass,
									GenericTypeNode.Projection<ElkClass, ElkNamedIndividual>,
									NonBottomGenericTypeNode.Projection<ElkClass, ElkNamedIndividual>
							> taxonomy) {
						return new NonBottomGenericTypeNode.Projection<ElkClass, ElkNamedIndividual>(
								taxonomy, members, size);
					}
				},
				new ConcurrentNodeStore<
						ElkNamedIndividual,
						IndividualNode.Projection2<ElkClass, ElkNamedIndividual>
				>(instanceKeyProvider),
				new TaxonomyNodeFactory<
						ElkNamedIndividual,
						IndividualNode.Projection2<ElkClass, ElkNamedIndividual>,
						InstanceTaxonomy<ElkClass, ElkNamedIndividual>
				>() {
					@Override
					public IndividualNode.Projection2<ElkClass, ElkNamedIndividual> createNode(
							final Iterable<? extends ElkNamedIndividual> members, final int size,
							final InstanceTaxonomy<ElkClass, ElkNamedIndividual> taxonomy) {
						return new IndividualNode.Projection2<ElkClass, ElkNamedIndividual>(
								taxonomy, members, size);
					}
				},
				elkFactory.getOwlThing());
		this.bottomNode_ = new BottomGenericTypeNode.Projection<ElkClass, ElkNamedIndividual>(
				this, elkFactory.getOwlNothing());
	}

	@Override
	public GenericTypeNode.Projection<ElkClass, ElkNamedIndividual> getBottomNode() {
		return bottomNode_;
	}

	@Override
	protected Set<? extends GenericTypeNode.Projection<ElkClass, ElkNamedIndividual>> toTaxonomyNodes(
			final Set<? extends NonBottomGenericTypeNode.Projection<ElkClass, ElkNamedIndividual>> nodes) {
		return nodes;
	}

}
