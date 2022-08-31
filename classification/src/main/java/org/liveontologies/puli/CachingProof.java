/*-
 * #%L
 * Proof Utility Library
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2014 - 2017 Live Ontologies Project
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
package org.liveontologies.puli;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A {@link Proof} which caches the inferences returned by the input
 * {@link Proof} by {@link Proof#getInferences(Object)}. When this method is
 * called for the second time with the same input, the cached version is used.
 * 
 * @author Yevgeny Kazakov
 *
 * @param <I>
 *            the type of inferences provided by this proof
 */
public class CachingProof<I extends Inference<?>>
		extends DelegatingProof<I, Proof<? extends I>> {

	private final Map<Object, Collection<? extends I>> inferenceCache_ = new HashMap<Object, Collection<? extends I>>();

	public CachingProof(Proof<? extends I> delegate) {
		super(delegate);
	}

	@Override
	public Collection<? extends I> getInferences(Object conclusion) {
		Collection<? extends I> result = inferenceCache_.get(conclusion);
		if (result == null) {
			result = super.getInferences(conclusion);
			inferenceCache_.put(conclusion, result);
		}
		return result;
	}

}
