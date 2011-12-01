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

import junit.framework.TestCase;
import junitx.util.PrivateAccessor;

/**
 * Tests for {@link repast.simphony.engine.graph.GraphParams}.
 *
 * @author Jerry Vos
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:26:02 $
 */
public class GraphParamsTest extends TestCase {
	GraphParams graphParams;
	private Object prevNode;
	private Object curNode;
	private double prevDist;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void setUp() throws Exception {
		prevNode = new Object();
		curNode = new Object();
		prevDist = 123.345;
		
		graphParams  = new GraphParams(prevNode, curNode, prevDist);
	}
	
	/*
	 * Test method for 'repast.simphony.engine.graph.GraphParams.GraphParams(E, E,
	 * double)'
	 */
	@SuppressWarnings("unchecked")
	public void testGraphParams() throws NoSuchFieldException {
		prevNode = new Object();
		curNode = new Object();
		prevDist = 123.345;
		
		graphParams  = new GraphParams(prevNode, curNode, prevDist);
		
		assertSame(prevNode, PrivateAccessor.getField(graphParams, "previousNode"));
		assertSame(curNode, PrivateAccessor.getField(graphParams, "currentNode"));
		assertEquals(prevDist, PrivateAccessor.getField(graphParams, "previousDistance"));
	}

	/*
	 * Test method for 'repast.simphony.engine.graph.GraphParams.getCurrentNode()'
	 */
	@SuppressWarnings("unchecked")
	public void testGetCurrentNode() {
		assertSame(curNode, graphParams.getCurrentNode());
	}

	/*
	 * Test method for 'repast.simphony.engine.graph.GraphParams.getPreviousNode()'
	 */
	@SuppressWarnings("unchecked")
	public void testGetPreviousNode() {
		assertSame(prevNode, graphParams.getPreviousNode());
	}

	/*
	 * Test method for 'repast.simphony.engine.graph.GraphParams.getPreviousDistance()'
	 */
	@SuppressWarnings("unchecked")
	public void testGetPreviousDistance() {
		assertEquals(prevDist, graphParams.getPreviousDistance());
	}

}
