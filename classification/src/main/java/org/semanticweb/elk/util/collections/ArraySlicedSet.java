/*
 * #%L
 * elk-reasoner
 * 
 * $Id$
 * $HeadURL$
 * %%
 * Copyright (C) 2011 Oxford University Computing Laboratory
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
/**
 * @author Yevgeny Kazakov, May 17, 2011
 */
package org.semanticweb.elk.util.collections;

import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Set;

/**
 * A compact representation of several sets (called "slices") that can share
 * many elements. The representation is backed by arrays that store the elements
 * and numerical masks that encode to which sets those elements belong to. The
 * membership is checked using hash values of elements with linear probing to
 * resolve hash collisions: see [1] p.526. Parts of the code are inspired by the
 * implementation of {@link java.util.HashMap}.
 * 
 * [1] Donald E. Knuth, The Art of Computer Programming, Volume 3, Sorting and
 * Searching, Second Edition
 * 
 * @author Yevgeny Kazakov
 * @param <E>
 *            the type of the elements in the sets
 * 
 */
public class ArraySlicedSet<E> {

	/**
	 * the maximal number of slices that can be stored in a
	 * {@link ArraySlicedSet}
	 */
	public static int MAX_SLICES = 32;

	/**
	 * The table for the elements; the length MUST always be a power of two.
	 */
	transient E[] data;

	/**
	 * The table of masks that encode the membership of elements in slices; the
	 * length MUST be a power of two.
	 */
	transient int[] masks;

	/**
	 * The number of elements contained in the respective slice
	 */
	transient int[] sizes;

	/**
	 * The number of non-{@code null} elements in {@link #data}
	 */
	transient int occupied = 0;

	/**
	 * The upper rounded logarithm for the number of slices; it is used to
	 * efficiently compute membership of elements
	 */
	private final byte logs;

	/**
	 * Bitwise masks consisting of binary exponential number of 1s.
	 */
	private static int[] MSK_ = { 1, (1 << 2) - 1, (1 << 4) - 1, (1 << 8) - 1,
			(1 << 16) - 1, ~0 };

	@SuppressWarnings("unchecked")
	public ArraySlicedSet(int slices, int initialCapacity) {
		if (slices <= 0 || slices > MAX_SLICES)
			throw new IllegalArgumentException(
					"The nuber of slices should be between 1 and " + MAX_SLICES
							+ ": " + slices);
		int capacity = LinearProbing.getInitialCapacity(initialCapacity);
		this.data = (E[]) new Object[capacity];
		this.logs = (byte) upperLog(slices);
		this.masks = new int[getMaskCapacity(logs, capacity)];
		this.sizes = new int[slices];
		initSizes();
	}

	public ArraySlicedSet(int slices) {
		this(slices, LinearProbing.DEFAULT_INITIAL_CAPACITY);
	}

	static int upperLog(int n) {
		int log = 0;
		int exp = 1;
		while (exp < n) {
			log++;
			exp <<= 1;
		}
		return log;
	}

	void initSizes() {
		for (int i = 0; i < sizes.length; i++)
			this.sizes[i] = 0;
	}

	/**
	 * Computes the capacity of the mask table given the capacity of the table
	 * using the property that one integer can represent 32 set membership flags
	 * (as bits).
	 * 
	 * @param sl
	 *            the logarithm of the number of slices
	 * @param capacity
	 * @return
	 */
	static int getMaskCapacity(byte sl, int capacity) {
		int result = capacity >> (5 - sl);
		if (result == 0)
			result = 1;
		return result;
	}

	/**
	 * Returns the bit fragment that encodes the membership of an element for
	 * the given position in slices. The returned fragment is an integer whose
	 * lower bits correspond to whether an element occurs in the slice or not
	 * (the lowest bit corresponds to the membership in slice = 0, next bit in
	 * slice = 1, etc)
	 * 
	 * @param sl
	 *            the logarithm of the number of slices
	 * @param masks
	 *            the table storing all masks encoding the membership of
	 *            elements in slices
	 * @param pos
	 *            the position of an element in the table for which the fragment
	 *            should be returned
	 * @return the integer whose bits encode membership of the element at the
	 *         given position in slices
	 */
	static int getFragment(byte sl, int[] masks, int pos) {
		/*
		 * one element of the masks table stores fragments for 32/(2^sl)
		 * elements; since it is a power of 2, we can divide on this value and
		 * compute the remainder efficiently
		 */
		int shift = (5 - sl);
		int p = pos >> shift; // = pos / (32/(2^sl))
		int r = (p << shift) ^ pos; // the remainder after the devision
		// extract the r-th fragment of the length 2^sl
		return (masks[p] >> (r << sl)) & MSK_[sl];
	}

	/**
	 * Changes the fragment that corresponds to the encoding of membership of an
	 * element at the given position. The change is specified by the bits of the
	 * last parameter: bit 1 means the corresponding value of the fragment
	 * should flip, bit 0 means it should stay the same.
	 * 
	 * @param sl
	 *            the logarithm of the number of slices
	 * @param masks
	 *            the table storing all masks encoding the membership of
	 *            elements in slices
	 * @param pos
	 *            the position of an element in the table for which the fragment
	 *            should be changed
	 * @param diff
	 *            the sequence of bits that describes the change; the new
	 *            fragment is obtained by applying bitwise XOR operation
	 */
	static void changeFragment(byte sl, int[] masks, int pos, int diff) {
		int shift = (5 - sl);
		int p = pos >> shift;
		int r = (p << shift) ^ pos;
		masks[p] ^= diff << (r << sl);
	}

	/**
	 * @param s
	 *            the slice id
	 * @return the number of elements currently stored in the provided slice
	 */
	public int size(int s) {
		return sizes[s];
	}

	/**
	 * @param s
	 *            the slice id
	 * @return {@code true} if the given slice does not contain any elements and
	 *         {@code false} otherwise
	 */
	public boolean isEmpty(int s) {
		return sizes[s] == 0;
	}

	private static <E> int addMask(byte sl, E[] data, int[] masks, E e, int mask) {
		int pos = LinearProbing.getPosition(data, e);
		int oldMask = getFragment(sl, masks, pos);
		if (data[pos] == null) {
			data[pos] = e;
			changeFragment(sl, masks, pos, oldMask ^ mask);
			return 0;
		}
		// else
		int newMask = (oldMask | mask);
		if (newMask != oldMask)
			changeFragment(sl, masks, pos, oldMask ^ newMask);
		return oldMask;
	}

	private static <E> void remove(byte sl, E[] data, int[] masks, int pos) {
		int oldFragment = getFragment(sl, masks, pos);
		for (;;) {
			int next = LinearProbing.getMovedPosition(data, pos);
			E moved = data[pos] = data[next];
			int newFragment = getFragment(sl, masks, next);
			changeFragment(sl, masks, pos, oldFragment ^ newFragment);
			if (moved == null)
				return;
			// else
			pos = next;
			oldFragment = newFragment;
		}
	}

	private static <E> int removeMask(byte sl, E[] data, int[] masks, Object o,
			int mask) {
		int pos = LinearProbing.getPosition(data, o);
		if (data[pos] == null)
			return 0;
		// else
		int oldFragment = getFragment(sl, masks, pos);
		int newFragment = oldFragment & (~mask);
		if (newFragment == 0)
			remove(sl, data, masks, pos);
		else
			changeFragment(sl, masks, pos, oldFragment ^ newFragment);
		return oldFragment;
	}

	/**
	 * Increasing the capacity of the table
	 */
	private void enlarge() {
		int oldCapacity = data.length;
		if (oldCapacity == LinearProbing.MAXIMUM_CAPACITY)
			throw new IllegalArgumentException(
					"The set cannot grow beyond the capacity: "
							+ LinearProbing.MAXIMUM_CAPACITY);
		E[] oldData = data;
		int[] oldMasks = masks;
		int newCapacity = oldCapacity << 1;
		@SuppressWarnings("unchecked")
		E[] newData = (E[]) new Object[newCapacity];
		int[] newMasks = new int[getMaskCapacity(logs, newCapacity)];
		for (int i = 0; i < oldCapacity; i++) {
			E e = oldData[i];
			if (e != null)
				addMask(logs, newData, newMasks, e,
						getFragment(logs, oldMasks, i));
		}
		this.data = newData;
		this.masks = newMasks;
	}

	/**
	 * Decreasing the capacity of the table
	 */
	private void shrink() {
		int oldCapacity = data.length;
		if (oldCapacity == 1)
			return;
		E[] oldData = data;
		int[] oldMasks = masks;
		int newCapacity = oldCapacity >> 1;
		@SuppressWarnings("unchecked")
		E[] newData = (E[]) new Object[newCapacity];
		int[] newMasks = new int[getMaskCapacity(logs, newCapacity)];
		for (int i = 0; i < oldCapacity; i++) {
			E e = oldData[i];
			if (e != null)
				addMask(logs, newData, newMasks, e,
						getFragment(logs, oldMasks, i));
		}
		this.data = newData;
		this.masks = newMasks;
	}

	public boolean contains(int s, Object o) {
		if (o == null)
			throw new NullPointerException();
		// to avoid problems in the middle of resizing, we copy data and masks
		// when they have the same size
		E[] d;
		int[] m;
		for (;;) {
			d = this.data;
			m = this.masks;
			if (m.length == getMaskCapacity(logs, d.length))
				break;
		}
		int pos = LinearProbing.getPosition(d, o);
		if (d[pos] == null)
			return false;
		// else
		int mask = 1 << s;
		return ((getFragment(logs, m, pos) & mask) == mask);
	}

	/**
	 * Inserts a given element into the given slice
	 * 
	 * @param s
	 *            the slice id
	 * @param e
	 *            the elements to be inserted in to the given slice
	 * @return {@code true} if the given element did not occur in the given
	 *         slice and thus was inserted. Otherwise {@code false} is returned
	 *         and nothing is modified.
	 */
	public boolean add(int s, E e) {
		if (e == null)
			throw new NullPointerException();
		int mask = (1 << s);
		int oldMask = addMask(logs, data, masks, e, mask);
		int newMask = oldMask | mask;
		if (newMask == oldMask)
			return false;
		else if (oldMask == 0
				&& ++occupied == LinearProbing.getUpperSize(data.length))
			enlarge();
		sizes[s]++;
		return true;
	}

	/**
	 * Removes the given object from the given slice
	 * 
	 * @param s
	 *            the slice id
	 * @param o
	 *            the object that should be removed from the given slice
	 * @return {@code true} if the given object is equal to some element of the
	 *         given slice; this element will be removed from the slice. If
	 *         there is no such an object, {@code false} is returned and nothing
	 *         is modified
	 */
	public boolean remove(int s, Object o) {
		if (o == null)
			throw new NullPointerException();
		int mask = 1 << s;
		int oldMask = removeMask(logs, data, masks, o, mask);
		int newMask = oldMask & ~mask;
		if (newMask == oldMask)
			return false;
		// else
		if (newMask == 0
				&& --occupied == LinearProbing.getLowerSize(data.length))
			shrink();
		sizes[s]--;
		return true;
	}

	/**
	 * Removes all element of the given {@link Collection} from the given slice
	 * 
	 * @param s
	 *            the slice id
	 * @param c
	 *            the collection whose elements should be removed
	 * @return {@code true} if at least one element is removed from the slice.
	 *         An element is removed if an equal element is present in the given
	 *         collection. If no elements are removed, {@code false} is returned
	 *         and nothing is modified.
	 */
	public boolean removeAll(int s, Collection<?> c) {
		boolean modified = false;
		for (Object o : c) {
			modified |= remove(s, o);
		}
		return modified;
	}

	/**
	 * Clears all slices of this {@link ArraySlicedSet}. After calling this
	 * methods, all slices are empty.
	 */
	@SuppressWarnings("unchecked")
	public void clear() {
		int capacity = data.length >> 2;
		if (capacity == 0)
			capacity = 1;
		initSizes();
		this.data = (E[]) new Object[capacity];
		this.masks = new int[getMaskCapacity(logs, capacity)];
	}

	/**
	 * @param s
	 *            the slice id
	 * @return the set corresponding to this slice backed by this
	 *         {@link ArraySlicedSet}; it can be modified
	 */
	public Set<E> getSlice(int s) {
		return new Slice(s);
	}

	private class Slice extends AbstractSet<E> implements Set<E> {

		/**
		 * the slice which is viewed by this set
		 */
		final byte s;

		Slice(int s) {
			if (s > sizes.length || s < 0)
				throw new IllegalArgumentException("Slice should be in [0;"
						+ sizes.length + "]: " + s);
			this.s = (byte) s;
		}

		@Override
		public int size() {
			return ArraySlicedSet.this.size(s);
		}

		@Override
		public boolean isEmpty() {
			return ArraySlicedSet.this.isEmpty(s);
		}

		@Override
		public boolean contains(Object o) {
			return ArraySlicedSet.this.contains(s, o);
		}

		@Override
		public boolean add(E e) {
			return ArraySlicedSet.this.add(s, e);
		}

		@Override
		public boolean remove(Object o) {
			return ArraySlicedSet.this.remove(s, o);
		}

		@Override
		public boolean removeAll(Collection<?> c) {
			return ArraySlicedSet.this.removeAll(s, c);
		}

		@Override
		public Iterator<E> iterator() {
			return new ElementIterator(s);
		}

		@Override
		public String toString() {
			return Arrays.toString(toArray());
		}

	}

	private class ElementIterator extends LinearProbingIterator<E, E> {

		final int s;
		final int mask;

		ElementIterator(int s) {
			super(data, sizes[s]);
			this.s = s;
			mask = (1 << s);
			init();
		}

		@Override
		void checkSize(int expectedSize) {
			if (expectedSize != sizes[s])
				throw new ConcurrentModificationException();
		}

		@Override
		boolean isOccupied(int pos) {
			return (dataSnapshot[pos] != null && (getFragment(logs, masks, pos) & mask) == mask);
		}

		@Override
		void remove(int pos) {
			int oldMask = getFragment(logs, masks, pos);
			int newMask = oldMask & (~mask);
			if (newMask == 0) {
				ArraySlicedSet.remove(logs, data, masks, pos);
				occupied--;
			} else
				changeFragment(logs, masks, pos, oldMask ^ newMask);
			sizes[s]--;
		}

		@Override
		E getValue(E element, int pos) {
			return element;
		}
	}
}
