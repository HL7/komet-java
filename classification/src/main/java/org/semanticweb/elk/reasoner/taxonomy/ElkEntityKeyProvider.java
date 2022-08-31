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

import org.semanticweb.elk.owl.interfaces.ElkEntity;
import org.semanticweb.elk.reasoner.taxonomy.model.KeyProvider;

/**
 * {@link KeyProvider} for {@link ElkEntity}.
 * 
 * @author Peter Skocovsky
 */
public class ElkEntityKeyProvider implements KeyProvider<ElkEntity> {

	/**
	 * The instance of this class.
	 */
	public static final ElkEntityKeyProvider INSTANCE = new ElkEntityKeyProvider();

	@Override
	public Object getKey(final ElkEntity arg) {
		return arg.getIri();
	}

}
