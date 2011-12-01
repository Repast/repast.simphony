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

/**
 * An object used to hold parameters for use when executing graphs.
 * 
 * @see repast.simphony.space.graph.Traverser
 * @see repast.simphony.engine.graph.GraphExecutor#validateForExecution(GraphParams)
 * 
 * @author Jerry Vos
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 */
public class GraphParams<E> {
	private E previousNode;

	private E currentNode;

	private double previousDistance;

	/**
	 * Constructs this GraphParams object holding the specified values.
	 * 
	 * @param previousNode
	 *            the previous node
	 * @param currentNode
	 *            the current node
	 * @param previousDistance
	 *            the previous distance that has been traversed
	 */
	public GraphParams(E previousNode, E curNode, double previousDistance) {
		super();
		this.previousNode = previousNode;
		this.currentNode = curNode;
		this.previousDistance = previousDistance;
	}

	/**
	 * Retrieves the current node in the graph execution.
	 * 
	 * @return the current node in the graph execution
	 */
	public E getCurrentNode() {
		return currentNode;
	}

	/**
	 * Retrieves the previous node in the graph execution.
	 * 
	 * @return the previous node in the graph execution
	 */
	public E getPreviousNode() {
		return previousNode;
	}

	/**
	 * Retrieves the sum of the previous distances in the graph execution.
	 * 
	 * @return the sum of the previous distances in the graph execution
	 */
	public double getPreviousDistance() {
		return previousDistance;
	}
}
