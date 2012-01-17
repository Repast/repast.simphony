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

import junit.framework.TestCase;
import junitx.util.PrivateAccessor;
import repast.simphony.util.collections.Pair;

/**
 * Tests for {@link repast.simphony.util.collections.Pair}.
 * 
 * @author Jerry Vos
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:26:02 $
 */
@SuppressWarnings("unchecked")
public class PairTest extends TestCase {
	Object first;
	Object second;
	Pair pair;

	/*
	 * Test method for 'repast.simphony.util.collections.Pair.Pair(X, Y)'
	 */
	public void testPair() throws NoSuchFieldException {
		first = "first";
		second = "second";

		pair = new Pair(first, second);

		assertSame(first, PrivateAccessor.getField(pair, "first"));
		assertSame(second, PrivateAccessor.getField(pair, "second"));
	}

	@Override
	protected void setUp() throws Exception {
		first = "first";
		second = "second";

		pair = new Pair(first, second);
	}

	/*
	 * Test method for 'repast.simphony.util.collections.Pair.equals(Object)'
	 */
	public void testEqualsObject() {
		assertFalse(pair.equals("asdf"));

		assertFalse(pair.equals(new Pair("asd", "fdsa")));
		assertFalse(pair.equals(new Pair("first", "fdsa")));

		assertTrue(pair.equals(new Pair("first", "second")));
		assertTrue(pair.equals(pair));
	}

	/*
	 * Test method for 'repast.simphony.util.collections.Pair.getFirst()'
	 */
	public void testGetFirst() throws NoSuchFieldException {
		assertSame(first, pair.getFirst());
	}

	/*
	 * Test method for 'repast.simphony.util.collections.Pair.getSecond()'
	 */
	public void testGetSecond() throws NoSuchFieldException {
		assertSame(second, pair.getSecond());
	}

	/*
	 * Test method for 'repast.simphony.util.collections.Pair.setFirst(X)'
	 */
	public void testSetFirst() throws NoSuchFieldException {
		pair.setFirst(second);

		assertSame(second, PrivateAccessor.getField(pair, "first"));
	}

	/*
	 * Test method for 'repast.simphony.util.collections.Pair.setSecond(Y)'
	 */
	public void testSetSecond() throws NoSuchFieldException {
		pair.setSecond(first);

		assertSame(first, PrivateAccessor.getField(pair, "second"));
	}

}
