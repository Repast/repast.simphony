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
package repast.simphony;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

import junit.framework.TestCase;

/**
 * Some useful testing routines
 * 
 * @author Jerry Vos
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:35 $
 */
public class TestUtils extends TestCase {
	public TestUtils() {
		super();
	}

	/**
	 * TODO: update this javadoc Equivalent to col1.containsAll(col2) &&
	 * col1.size() == col2.size()
	 * 
	 * @param col1
	 *            a collection to test
	 * @param col2
	 *            another collection to test
	 * 
	 * @return col1.containsAll(col2) && col1.size() == col2.size()
	 */
	@SuppressWarnings("unchecked")
	public static boolean collectionsContentsEqual(Iterable col1, Iterable col2) {
		return collectionsContentsEqual(col1.iterator(), col2.iterator());
	}

	/**
	 * TODO: update this javadoc Equivalent to col1.containsAll(col2) &&
	 * col1.size() == col2.size()
	 * 
	 * @param col1
	 *            a collection to test
	 * @param col2
	 *            another collection to test
	 * 
	 * @return col1.containsAll(col2) && col1.size() == col2.size()
	 */
	@SuppressWarnings("unchecked")
	public static boolean collectionsContentsEqual(Iterator iter1,
			Iterator iter2) {
		ArrayList list1 = new ArrayList();
		ArrayList list2 = new ArrayList();

		for (; iter1.hasNext();) {
			list1.add(iter1.next());
		}

		for (; iter2.hasNext();) {
			list2.add(iter2.next());
		}

		return list1.size() == list2.size() && list1.containsAll(list2);
	}

	/**
	 * Checks if the contents of two Iterators are equal by making sure the
	 * Iterators have the same number of elements and the elements are in the
	 * same order. The objects are compared with the .equals method
	 * 
	 * @param iter1
	 *            an Iterator to test
	 * @param iter2
	 *            another Iterator to test
	 * 
	 * @return if the iterators are equal according to this method's description
	 */
	@SuppressWarnings("unchecked")
	public static boolean collectionsContentsEqualOrdered(Iterator iter1,
			Iterator iter2) {

		try {
			while (iter1.hasNext()) {
				if (!iter1.next().equals(iter2.next())) {
					return false;
				}
			}

			return !iter2.hasNext();
		} catch (NoSuchElementException ex) {
			return false;
		}
	}
	
	/**
	 * Checks if the contents of two iterables are equal by making sure the
	 * iterables have the same number of elements and the elements are in the
	 * same order. The objects are compared with the .equals method. <p/>
	 * 
	 * Same as:
	 * <code>collectionsContentsEqualOrdered(iterable1.iterator(), iterable2.iterator());</code>
	 * 
	 * @param iter1
	 *            an Iteratable to test
	 * @param iter2
	 *            another Iteratable to test
	 * 
	 * @return if the Iteratables are equal according to this method's description
	 */
	@SuppressWarnings("unchecked")
	public static boolean collectionsContentsEqualOrdered(Iterable iterable1,
			Iterable iterable2) {
		return collectionsContentsEqualOrdered(iterable1.iterator(), iterable2.iterator());
	}

	@SuppressWarnings("unchecked")
	public void testSelf() {
		HashSet foo = new HashSet();
		ArrayList<Object> bar = new ArrayList();

		foo.add("String1");

		assertFalse(collectionsContentsEqual(foo, bar));

		bar.add("String1");

		assertTrue(collectionsContentsEqual(foo, bar));

		Object tmp = new Object();
		foo.add(tmp);

		assertFalse(collectionsContentsEqual(foo, bar));

		bar.add(tmp);

		assertTrue(collectionsContentsEqual(foo, bar));

		foo = new HashSet();
		bar = new ArrayList();

		tmp = new Object();

		foo.add("String1");
		foo.add(tmp);

		bar.add(tmp);
		bar.add("String1");

		assertTrue(collectionsContentsEqual(foo, bar));
	}
}
