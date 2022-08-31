/*
 * #%L
 * ELK Utilities for Concurrency
 * 
 * $Id$
 * $HeadURL$
 * %%
 * Copyright (C) 2011 - 2012 Department of Computer Science, University of Oxford
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
package org.semanticweb.elk.util.concurrent.computation;

/**
 * A listener to be used with {@link InputProcessor} that can be used to perform
 * actions when a job is processed.
 * 
 * @author "Yevgeny Kazakov"
 * 
 * @param <J>
 *            the type of the jobs used in the job processor
 */
public interface InputProcessorListenerNotifyFinishedJob<J> {

	/**
	 * This function is called after the input processor detects when the job is
	 * fully processed. When the submitted job is processed, this method will be
	 * guaranteed to be called with this job as an argument. If
	 * {@link InputProcessor#submit(Object)} is called followed with
	 * {@link InputProcessor#process()}, it is guaranteed that
	 * {@link #notifyFinished(Object)} will be called (perhaps from some other
	 * thread) before no instance of {@link InputProcessor#process()} is
	 * running.
	 * 
	 * @param job
	 *            the job that has been processed
	 * @throws InterruptedException
	 *             if interrupted during the notification
	 */
	public void notifyFinished(J job) throws InterruptedException;

}
