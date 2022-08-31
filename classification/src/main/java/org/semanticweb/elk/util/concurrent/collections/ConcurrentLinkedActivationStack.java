package org.semanticweb.elk.util.concurrent.collections;

/*
 * #%L
 * ELK Utilities for Concurrency
 * $Id:$
 * $HeadURL:$
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

import java.util.concurrent.atomic.AtomicReference;

/**
 * A thread-safe implementation of stack based on the non-blocking Treiber's
 * Algorithm (Treiber, 1986). The implementation allows to check when the pushed
 * element is the first element in the stack. This stack does not allow storing
 * {@code null} values.
 * 
 * @author "Yevgeny Kazakov"
 * 
 * @param <E>
 *            the type of elements in the stack
 */
public class ConcurrentLinkedActivationStack<E> implements ActivationStack<E> {

	private final AtomicReference<Node<E>> top_ = new AtomicReference<Node<E>>();

	/**
	 * a special dummy node used to mark the end of the stack after it has been
	 * activated
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Node<?> dummyNode = new Node(null);

	@Override
	@SuppressWarnings("unchecked")
	public boolean push(E element) {
		if (element == null)
			throw new IllegalArgumentException(
					"Elements in the stack cannot be null");
		Node<E> newHead = new Node<E>(element);
		Node<E> oldHead;
		for (;;) {
			oldHead = top_.get();
			if (oldHead == null)
				newHead.next = (Node<E>) dummyNode;
			else
				newHead.next = oldHead;
			if (top_.compareAndSet(oldHead, newHead)) {
				if (oldHead == null)
					return true;
				// else
				return false;
			}
		}
	}

	@Override
	public E peek() {
		Node<E> head = top_.get();
		if (head == null)
			return null;
		return head.item;
	}

	@Override
	public E pop() {
		for (;;) {
			Node<E> oldHead = top_.get();
			Node<E> newHead;
			if (oldHead == null)
				return null;
			newHead = oldHead.next;
			if (top_.compareAndSet(oldHead, newHead))
				return oldHead.item;
		}
	}

	private static class Node<T> {
		public final T item;
		public Node<T> next;

		public Node(T item) {
			this.item = item;
		}
	}

}
