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

import junitx.util.PrivateAccessor;
import org.apache.commons.collections15.Predicate;
import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;
import repast.simphony.util.collections.FilteredIterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
* Tests for {@link repast.simphony.util.collections.FilteredIterator}.
*
* @author Jerry Vos
* @version $Revision: 1.1 $ $Date: 2005/12/21 22:26:02 $
*/
@SuppressWarnings("unchecked")
public class FilteredIteratorTest extends MockObjectTestCase {
	Mock mockIterator;
	Mock mockRule;

	FilteredIterator iterator;

	@Override
	protected void setUp() throws Exception {
		mockIterator = mock(Iterator.class);
		mockRule = mock(Predicate.class);

		iterator = new FilteredIterator((Iterator) mockIterator.proxy(), (Predicate) mockRule.proxy());
	}

	/*
	   * Test method for 'repast.simphony.util.collections.FilteredIterator.FilteredIterator(Iterator<X>, Rule<X>)'
	   */
	public void testFilteredIterator() throws NoSuchFieldException {
		mockIterator = mock(Iterator.class);
		mockRule = mock(Predicate.class);

		iterator = new FilteredIterator((Iterator) mockIterator.proxy(), (Predicate) mockRule.proxy());

		assertSame(mockIterator.proxy(), PrivateAccessor.getField(iterator, "innerIterator"));
		assertSame(mockRule.proxy(), PrivateAccessor.getField(iterator, "predicate"));

		assertNull(PrivateAccessor.getField(iterator, "cached"));
	}

	/*
	 * Test method for 'repast.simphony.util.collections.FilteredIterator.next()'
	 */
	public void testNext1() {
		mockIterator.stubs().method("hasNext").will(returnValue(false));

		try {
			iterator.next();
			fail();
		} catch (NoSuchElementException ex) {
			// had no elements to grab
		}
	}

	/*
	 * Test method for 'repast.simphony.util.collections.FilteredIterator.next()'
	 */
	public void testNext2() throws NoSuchFieldException {
		PrivateAccessor.setField(iterator, "innerIterator", null);

		try {
			iterator.next();
			fail();
		} catch (NoSuchElementException ex) {
			// had no iterator to look at
		}
	}

	/*
	 * Test method for 'repast.simphony.util.collections.FilteredIterator.next()'
	 */
	public void testNext3() throws NoSuchFieldException {
		Object expected = "expected";
		PrivateAccessor.setField(iterator, "cached", expected);

		assertSame(expected, iterator.next());
	}

	/*
	 * Test method for 'repast.simphony.util.collections.FilteredIterator.next()'
	 */
	public void testNext4() throws NoSuchFieldException {
		Object expected = "expected";

		mockIterator.stubs().method("hasNext").will(returnValue(true));
		mockIterator.stubs().method("next").will(returnValue(expected));

		mockRule.stubs().method("evaluate").will(returnValue(true));

		assertSame(expected, iterator.next());
	}

	/*
	 * Test method for 'repast.simphony.util.collections.FilteredIterator.next()'
	 */
	public void testNext5() throws NoSuchFieldException {
		iterator = new FilteredIterator((Iterator) mockIterator.proxy(), (Predicate) mockRule.proxy(), false);

		mockIterator.stubs().method("hasNext").will(returnValue(true));
		mockIterator.stubs().method("next").will(returnValue(new Object()));

		mockRule.stubs().method("evaluate").will(returnValue(false));

		try {
			iterator.next();
			fail();
		} catch (NoSuchElementException ex) {
			// shouldn't have been able to grab a valid element
		}
	}

	/*
	 * Test method for 'repast.simphony.util.collections.FilteredIterator.hasNext()'
	 */
	public void testHasNext1() {
		mockIterator.stubs().method("hasNext").will(returnValue(false));

		assertFalse(iterator.hasNext());
	}

	/*
	   * Test method for 'repast.simphony.util.collections.FilteredIterator.hasNext()'
	   */
	public void testHasNext2() {
		iterator = new FilteredIterator((Iterator) mockIterator.proxy(), (Predicate) mockRule.proxy(), false);

		mockIterator.stubs().method("hasNext").will(returnValue(true));
		mockIterator.stubs().method("next").will(returnValue(new Object()));
		mockRule.stubs().method("evaluate").will(returnValue(false));
		assertFalse(iterator.hasNext());
	}

	/*
	   * Test method for 'repast.simphony.util.collections.FilteredIterator.hasNext()'
	   */
	public void testHasNext4() {
		mockIterator.stubs().method("hasNext").will(
				onConsecutiveCalls(returnValue(true), returnValue(true),
						returnValue(true), returnValue(false)));
		mockIterator.stubs().method("next").will(returnValue(new Object()));
		mockRule.stubs().method("evaluate").will(returnValue(false));
		assertFalse(iterator.hasNext());
	}

	/*
	 * Test method for 'repast.simphony.util.collections.FilteredIterator.hasNext()'
	 */
	public void testHasNext5() {
		// this results in a cached node
		mockIterator.stubs().method("hasNext").will(returnValue(true));
		mockIterator.stubs().method("next").will(returnValue(new Object()));
		mockRule.stubs().method("evaluate").will(returnValue(true));
		assertTrue(iterator.hasNext());

		assertTrue(iterator.hasNext());
	}

	/*
	 * Test method for 'repast.simphony.util.collections.FilteredIterator.hasNext()'
	 */
	public void testHasNext6() {
		// this results in a cached node
		mockIterator.stubs().method("hasNext").will(returnValue(false));
		assertFalse(iterator.hasNext());
	}

	/*
	 * Test method for 'repast.simphony.util.collections.FilteredIterator.hasNext()'
	 */
	public void testHasNext7() throws NoSuchFieldException {
		// this results in a cached node
		PrivateAccessor.setField(iterator, "innerIterator", null);
		assertFalse(iterator.hasNext());
	}

	/*
	 * Test method for 'repast.simphony.util.collections.FilteredIterator.remove()'
	 */
	public void testRemove() {
		try {
			iterator.remove();
			fail();
		} catch (UnsupportedOperationException ex) {
			// ok, worked
		}
	}

}
