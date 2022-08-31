package org.semanticweb.elk.reasoner.saturation.conclusions.classes;

/*
 * #%L
 * ELK Reasoner
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2011 - 2015 Department of Computer Science, University of Oxford
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

import org.semanticweb.elk.Reference;
import org.semanticweb.elk.reasoner.saturation.SaturationState;
import org.semanticweb.elk.reasoner.saturation.context.Context;

/**
 * A {@link Reference} to a {@link Context} within the given
 * {@link SaturationState} that has the same root as the {@link Context} from
 * the given {@link Reference}.
 * 
 * @author Yevgeny Kazakov
 *
 */
public class RelativizedContextReference implements Reference<Context> {

	/**
	 * The reference to a {@link Context} to be relativized
	 */
	private final Reference<? extends Context> mainContextRef_;

	/**
	 * the {@link SaturationState} used to relativize {@link Context}s
	 */
	private final SaturationState<?> state_;

	public RelativizedContextReference(Reference<? extends Context> contextReference,
			SaturationState<?> state) {
		this.mainContextRef_ = contextReference;
		this.state_ = state;
	}

	@Override
	public Context get() {
		return state_.getContext(mainContextRef_.get().getRoot());
	}

}
