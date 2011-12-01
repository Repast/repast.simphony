/*$$
 * Copyright (c) 2007, Argonne National Laboratory
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with 
 * or without modification, are permitted provided that the following 
 * conditions are met:
 *
 *	 Redistributions of source code must retain the above copyright notice,
 *	 this list of conditions and the following disclaimer.
 *
 *	 Redistributions in binary form must reproduce the above copyright notice,
 *	 this list of conditions and the following disclaimer in the documentation
 *	 and/or other materials provided with the distribution.
 *
 * Neither the name of the Repast project nor the names the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE TRUSTEES OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *$$*/
package repast.simphony.engine.graph;

import repast.simphony.space.graph.Traverser;
import repast.simphony.util.collections.CollectionUtils;

import java.util.Iterator;

/**
 * A simple {@link repast.simphony.space.graph.Traverser} that will return elements from
 * an Iterator in the order they are returned by that iterator. This is useful
 * primarily for working with the {@link net.sf.cglib.core.CollectionUtils}
 * class.<p/>
 * 
 * Since the getDistance method of this class does not generally make sense for
 * a list, it will default to returning 1 or the defaultDistance value.<p/>
 * 
 * @see #setDefaultDistance(double)
 * 
 * @author Jerry Vos
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:35 $
 */
public class IterableTraverser<T> implements Traverser<T> {
	private Iterator<T> iterator;

	private static final Iterator NULL_ITERATOR = new Iterator() {
		public boolean hasNext() {
			return false;
		}

		public Object next() {
			return null;
		}

		public void remove() {
		}

	};

	private class ValueIterator implements Iterator<T> {
		public T value;

		public boolean hasNext() {
			return value != null;
		}

		public T next() {
			T valTmp = value;
			value = null;

			return valTmp;
		}

		public void remove() {
			throw new UnsupportedOperationException("Remove unsupported");
		}

	}

	private final ValueIterator valueIterator = new ValueIterator();

	private double defaultDistance = 1;

	/**
	 * Constructs this traverser based on the iterator returned by this
	 * iterable.<p/>
	 * 
	 * Same as: <code>IterableTraverser(iterable.iterator(), true)</code>
	 * 
	 * @see #IterableTraverser(Iterator, boolean)
	 * 
	 * @param iterable
	 *            the iterable who's iterator to use for traversing
	 */
	public IterableTraverser(Iterable<T> iterable) {
		this(iterable.iterator(), true);
	}

	/**
	 * Constructs this traverser based on the iterator returned by this
	 * iterable.<p/>
	 * 
	 * Same as: <code>IterableTraverser(iterable.iterator(), skipFirst)</code>
	 * 
	 * @see #IterableTraverser(Iterator, boolean)
	 * 
	 * @param iterable
	 *            the iterable who's iterator to use for traversing
	 * @param skipFirst
	 *            whether or not to skip the first value in the iterator, for
	 *            more information on this see
	 *            {@link #IterableTraverser(Iterator, boolean)}
	 */
	public IterableTraverser(Iterable<T> iterable, boolean skipFirst) {
		this(iterable.iterator(), skipFirst);
	}

	/**
	 * Constructs this traverser based on the iterator returned by this
	 * iterable.<p/>
	 * 
	 * Same as: <code>IterableTraverser(iterator, true)</code>
	 * 
	 * @see #IterableTraverser(Iterator, boolean)
	 * 
	 * @param iterator
	 *            the iterator to traverse
	 */
	public IterableTraverser(Iterator<T> iterator) {
		this(iterator, true);
	}

	/**
	 * Constructs this traverser based on the iterator returned by this
	 * iterable.<p/>
	 * 
	 * Based on the skipFirst value this traverser will (or will not) skip the
	 * first value returned by the iterator. This is useful because the
	 * scheduling and the {@link CollectionUtils} methods take in a root element
	 * that they start their execution on, and then they use this traverser. In
	 * these cases if you do not skip the first element you will have that
	 * element executed upon twice, once because it is the root, and once
	 * because it is the first element returned by the iterator.
	 * 
	 * @param iterator
	 *            the iterator to traverse
	 * @param skipFirst
	 *            whether or not to skip the first value in the iterator
	 */
	public IterableTraverser(Iterator<T> iterator, boolean skipFirst) {
		super();

		this.iterator = iterator;
		
		if (skipFirst) skipFirst();
	}

	
	private void skipFirst() {
		if (iterator.hasNext()) {
			iterator.next();
		}
	}
	
	/**
	 * Retrieves an iterator that will return the next object in the iterator.
	 * If there are no more objects in the iterator it will return an iterator
	 * that is empty (meaning it will always return false for it's hasNext).
	 * 
	 * @param previousNode
	 *            ignored
	 * @param currentNode
	 *            ignored
	 * @return an iterator as specified in the description
	 */
	@SuppressWarnings("unchecked")
	public Iterator<T> getSuccessors(T previousNode, T currentNode) {
		if (iterator.hasNext()) {
			valueIterator.value = iterator.next();
			return valueIterator;
		} else {
			return NULL_ITERATOR;
		}
	}

	/**
	 * Always returns the default distance of this instance.
	 * 
	 * @see #setDefaultDistance(double)
	 * 
	 * @param fromNode
	 *            ignored
	 * @param toNode
	 *            ignored
	 * @return the default distance
	 */
	public double getDistance(T fromNode, T toNode) {
		return defaultDistance;
	}

	/**
	 * Sets the value that will be returned by {@link #getDistance(T, T)}.
	 * 
	 * @param defaultDistance
	 *            the value to be returned by {@link #getDistance(T, T)}
	 */
	public void setDefaultDistance(double defaultDistance) {
		this.defaultDistance = defaultDistance;
	}
}
