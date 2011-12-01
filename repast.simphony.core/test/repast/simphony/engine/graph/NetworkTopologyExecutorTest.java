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

import junitx.util.PrivateAccessor;

import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;

import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.Traverser;

/**
* Tests for {@link repast.simphony.engine.graph.NetworkTopologyExecutor}.
*
* @author Jerry Vos
* @version $Revision: 1.1 $ $Date: 2005/12/21 22:26:02 $
*/
public class NetworkTopologyExecutorTest extends MockObjectTestCase {

	/*
	 * Test method for
	 * 'repast.simphony.engine.graph.NetworkTopologyExecutor.NetworkTopologyExecutor(ISchedule,
	 * ScheduleParameters, Traverser<Object>, Executor<Object>,
	 * NetworkTopology)'
	 */
	public void testNetworkTopologyExecutorIScheduleScheduleParametersTraverserOfObjectExecutorOfObjectNetworkTopology() throws NoSuchFieldException {
		Mock mockSchedule = mock(ISchedule.class);
		Mock mockExecutor = mock(Executor.class);
		Mock mockTraverser = mock(Traverser.class);
		Mock mockTopology = mock(Network.class);

		ScheduleParameters schedParams = ScheduleParameters.createOneTime(9.98);

		NetworkTopologyExecutor executor = new NetworkTopologyExecutor(
				(ISchedule) mockSchedule.proxy(), schedParams,
				(Traverser) mockTraverser.proxy(), (Executor) mockExecutor
						.proxy(), (Network) mockTopology.proxy());

		assertSame(mockSchedule.proxy(), PrivateAccessor.getField(executor,
				"schedule"));
		assertSame(mockExecutor.proxy(), PrivateAccessor.getField(executor,
				"nodeExecutor"));
		assertSame(mockTraverser.proxy(), PrivateAccessor.getField(executor,
				"traverser"));
		assertSame(mockTopology.proxy(), PrivateAccessor.getField(executor,
				"topology"));
		assertSame(schedParams, PrivateAccessor
				.getField(executor, "baseParams"));
	}
	/*
	 * Test method for
	 * 'repast.simphony.engine.graph.NetworkTopologyExecutor.NetworkTopologyExecutor(ISchedule,
	 * ScheduleParameters, Executor<Object>, NetworkTopology)'
	 */
	@SuppressWarnings("unchecked")
	public void testNetworkTopologyExecutorIScheduleScheduleParametersExecutorOfObjectNetworkTopology() throws NoSuchFieldException {
		Mock mockSchedule = mock(ISchedule.class);
		Mock mockExecutor = mock(Executor.class);
		Mock mockTopology = mock(Network.class);
		
		ScheduleParameters schedParams = ScheduleParameters.createOneTime(9.98);
		
		NetworkTopologyExecutor executor = new NetworkTopologyExecutor(
				(ISchedule) mockSchedule.proxy(), schedParams,
				(Executor) mockExecutor.proxy(), (Network) mockTopology
						.proxy());
		
		assertSame(mockSchedule.proxy(), PrivateAccessor.getField(executor, "schedule"));
		assertSame(mockExecutor.proxy(), PrivateAccessor.getField(executor, "nodeExecutor"));
		assertTrue(isA(NetworkTraverser.class).eval(
				PrivateAccessor.getField(executor, "traverser")));
		assertSame(mockTopology.proxy(), PrivateAccessor.getField(executor, "topology"));
		assertSame(schedParams, PrivateAccessor.getField(executor, "baseParams"));
	}

	/*
	 * Test method for
	 * 'repast.simphony.engine.graph.NetworkTopologyExecutor.validateForExecution(GraphParams<Object>)'
	 */
	@SuppressWarnings("unchecked")
	public void testValidateForExecution() throws NoSuchFieldException {
		Mock mockSchedule = mock(ISchedule.class);
		Mock mockExecutor = mock(Executor.class);
		Mock mockTraverser = mock(Traverser.class);
		Mock mockTopology = mock(Network.class);
		
		ScheduleParameters schedParams = ScheduleParameters.createOneTime(9.98);
		
		NetworkTopologyExecutor executor = new NetworkTopologyExecutor(
				(ISchedule) mockSchedule.proxy(), schedParams,
				(Traverser) mockTraverser.proxy(), (Executor) mockExecutor
						.proxy(), (Network) mockTopology.proxy());
		
		assertSame(mockSchedule.proxy(), PrivateAccessor.getField(executor, "schedule"));
		assertSame(mockExecutor.proxy(), PrivateAccessor.getField(executor, "nodeExecutor"));
		assertSame(mockTraverser.proxy(), PrivateAccessor.getField(executor, "traverser"));
		assertSame(mockTopology.proxy(), PrivateAccessor.getField(executor, "topology"));
		assertSame(schedParams, PrivateAccessor.getField(executor, "baseParams"));
		
		mockTopology.stubs().method("isAdjacent").will(returnValue(true));
		assertTrue(executor.validateForExecution(new GraphParams(null, null, 0)));
		
		mockTopology.stubs().method("isAdjacent").will(returnValue(false));
		assertFalse(executor.validateForExecution(new GraphParams(null, null, 0)));
		
	}

}
