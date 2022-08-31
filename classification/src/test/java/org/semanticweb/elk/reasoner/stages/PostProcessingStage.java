package org.semanticweb.elk.reasoner.stages;
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

import org.semanticweb.elk.exceptions.ElkException;

public interface PostProcessingStage {

	/**
	 * @return a string identifier of this stage
	 */
	public String getName();

	/**
	 * Performs the execution of this stage.
	 * 
	 * @throws ElkException
	 *             if execution was not successful
	 */
	public void execute() throws ElkException;

	/**
	 * Prints detailed information about the (progress) of this stage. This
	 * function can be used to print statistics after this stage is executed or
	 * interrupted.
	 */
	public void printInfo();

}
