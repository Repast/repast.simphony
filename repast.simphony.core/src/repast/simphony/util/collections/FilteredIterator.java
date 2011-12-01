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
package repast.simphony.util.collections;


import org.apache.commons.collections15.Predicate;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An iterator that decorates another iterator with filtering capabilities based
 * on a specified rule. This allows for early stopping of iteration on the first
 * invalid entry or full traversal.<p/>
 * 
 * This does <em>not</em> support the remove operation.
 * 
 * @author Jerry Vos
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:35 $
 */
public class FilteredIterator<X> implements Iterator<X>, Iterable<X> {
	private Predicate<X> predicate;

	private Iterator<X> innerIterator;

	private X cached = null;

	private boolean scanAll;

	// TODO: decide whether or not to just delete this
	// TODO: update java docs if keeping

	/**
	 * Constructs this iterator working on the specified iterator with the
	 * specified predicate, and searching through all the iterator.
	 * 
	 * @param innerIterator
	 *            the iterator to add the functionality to
	 * @param predicate
	 *            the predicate that determines the validity of objects (the
	 *            filtering rule)
	 */
	public FilteredIterator(Iterator<X> innerIterator,
	                        Predicate<X> predicate) {
		this(innerIterator, predicate, true);
	}

	/**
	 * Constructs this iterator working on the specified iterator with the
	 * specified predicate.
	 * 
	 * @param innerIterator
	 *            the iterator to add the functionality to
	 * @param predicate
	 *            the predicate that determines the validity of objects (the
	 *            filtering rule)
	 */
	public FilteredIterator(Iterator<X> innerIterator,
	                       Predicate<X> predicate, boolean scanAll) {
		this.predicate = predicate;
		this.innerIterator = innerIterator;
		this.scanAll = scanAll;
	}

	/**
	 * Returns the next element in the iteration. Calling this method repeatedly
	 * until the {@link #hasNext()} method returns false will return each
	 * element in the underlying collection exactly once.
	 * 
	 * @return the next element in the iteration.
	 * @exception NoSuchElementException
	 *                iteration has no more elements.
	 */
	public X next() {
		if (innerIterator == null) {
			cached = null;
			throw new NoSuchElementException("No more elements in the Iterator");
		} else if (cached == null && !innerIterator.hasNext()) {
			throw new NoSuchElementException("No more elements in the Iterator");
		}

		if (!cache()) {
			cached = null;
			throw new NoSuchElementException("No more elements in the Iterator");
		}

		X tmp = cached;

		cached = null;

		return tmp;
	}

	/**
	 * Caches the next available object. This will continue attempting to cache
	 * an object until it runs out of objects to check for (by hitting the end
	 * of the iterator or seeing RuleResult.INVALID_CONTINUE.
	 * 
	 * @return the final result of the attempts to cache an object
	 */
	private boolean cache() {
		if (cached != null) {
			return true;
		}
		boolean result = false;

		do {
			if (!innerIterator.hasNext()) {
				result = false;
				break;
			}
			cached = innerIterator.next();
			result = predicate.evaluate(cached);
		} while (!result && scanAll);

		if (!result) {
			cached = null;
		}

		return result;
	}

	/**
	 * Returns <tt>true</tt> if the iteration has more elements. (In other
	 * words, returns <tt>true</tt> if <tt>next</tt> would return an element
	 * rather than throwing an exception.)
	 * 
	 * @return <tt>true</tt> if the iterator has more elements.
	 */
	public boolean hasNext() {
		if (cached != null) {
			return true;
		} else if (innerIterator == null || !innerIterator.hasNext()) {
			return false;
		}

		return cache();
	}

	/**
	 * Returns an iterator over a set of elements of type T.
	 *
	 * @return an Iterator.
	 */
	public Iterator<X> iterator() {
		return this;
	}

	/**
	 * Unsupported, throws an UnsupportedOperationException.
	 */
	public void remove() {
		throw new UnsupportedOperationException("Remove unsupported.");
	}
}
