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
package org.liveontologies.puli.pinpointing.input.justifications;

import org.liveontologies.puli.pinpointing.BaseAxiomPinpointingTestInput;
import org.liveontologies.puli.pinpointing.input.SeveralCycles;

public class SeveralCyclesJustifications extends BaseAxiomPinpointingTestInput {

	@Override
	protected void build() {
		input(new SeveralCycles());

		justification(1, 4, 5, 6, 8, 10, 11);
		justification(8, 1, 9);
		justification(1, 9, 4, 5);
	}

}
