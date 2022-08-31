/*
 * #%L
 * ELK OWL Model Implementation
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
package org.semanticweb.elk.owl.iris;

/**
 * An implementation of {@link ElkPrefix}
 * 
 * @author Frantisek Simancik
 * @author "Yevgeny Kazakov"
 */
public class ElkPrefixImpl implements ElkPrefix {

	private final String name_;
	private final ElkFullIri iri_;

	public ElkPrefixImpl(String prefixName, ElkFullIri iri) {
		this.name_ = prefixName;
		this.iri_ = iri;
	}

	@Override
	public String getName() {
		return name_;
	}

	@Override
	public ElkFullIri getIri() {
		return iri_;
	}
}
