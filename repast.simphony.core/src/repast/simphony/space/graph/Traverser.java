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
package repast.simphony.space.graph;

import java.util.Iterator;

import repast.simphony.engine.graph.GraphParams;

/**
 * Interface used by the graph scheduling utilities to traverse the nodes in a
 * graph. This interface provides the nodes that will be ran, and the distance
 * between nodes (for determining the order of execution).
 * 
 * @see repast.simphony.engine.graph.NetworkTraverser
 * 
 * @author Jerry Vos
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 */
public interface Traverser<E> {
	/**
	 * Retrieves the next set of nodes in the graph based on the given
	 * GraphParams. In a tree this would be the child nodes of the current node.
	 * 
	 * @see GraphParams
	 * 
	 * @param params
	 *            a parameter object that contains information on the previous
	 *            and current graph traversal
	 * 
	 * @return An iterator that will return the next nodes to visit in the graph
	 *         traversal.
	 */
	Iterator<E> getSuccessors(E previousNode, E currentNode);

	/**
	 * Retrieves the distance between the current and previous node in the
	 * GraphParams object. This generally will be used to determine what time to
	 * schedule the current node at.
	 * 
	 * @param params
	 *            the object containing the previous and current node
	 * @return the distance between the nodes
	 */
	double getDistance(E fromNode, E toNode);
}