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
package org.liveontologies.puli.pinpointing;

/**
 * Compares the priorities according on their natural ordering.
 * 
 * @author Peter Skocovsky
 *
 * @param <T>
 *            The type of the compared objects.
 * @param <P>
 *            The type of their priorities.
 */
public abstract class NaturalPriorityComparator<T, P extends Comparable<P>>
		implements PriorityComparator<T, P> {

	@Override
	public int compare(final P o1, final P o2) {
		return o1.compareTo(o2);
	}

}
