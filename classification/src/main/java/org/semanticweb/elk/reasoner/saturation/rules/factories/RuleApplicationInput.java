/**
 * 
 */
package org.semanticweb.elk.reasoner.saturation.rules.factories;
/*
 * #%L
 * ELK Reasoner
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2011 - 2014 Department of Computer Science, University of Oxford
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

import org.semanticweb.elk.reasoner.indexing.model.IndexedContextRoot;

/**
 * Represents the input taken by {@link RuleApplicationFactory}'s processing
 * engines. Always has a context's root but may be extended with other
 * information.
 * 
 * @author Pavel Klinov
 * 
 *         pavel.klinov@uni-ulm.de
 */
public class RuleApplicationInput {

	private final IndexedContextRoot root_;
	
	public RuleApplicationInput(IndexedContextRoot root) {
		root_ = root;
	}
	
	public IndexedContextRoot getRoot() {
		return root_;
	}

	@Override
	public String toString() {
		return root_.toString();
	}
	
}
