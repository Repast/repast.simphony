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

import java.util.ArrayList;

import junitx.util.PrivateAccessor;

import org.jmock.cglib.MockObjectTestCase;

import repast.simphony.TestUtils;
import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.graph.NetworkFactoryFinder;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.schedule.ISchedulableActionFactory;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.space.graph.Network;

/**
 * Tests for {@link repast.simphony.engine.graph.DefaultGraphSchedule}.
 * 
 * @author Jerry Vos
 */
public class DefaultGraphScheduleTest extends MockObjectTestCase {

	private DefaultGraphSchedule graphSchedule;

	private ISchedulableActionFactory factory;

	@Override
	protected void setUp() throws Exception {
		RunState.init(null, null, null);
		factory = (ISchedulableActionFactory) mock(
				ISchedulableActionFactory.class).proxy();
		graphSchedule = new DefaultGraphSchedule(factory);
	}

	/*
	 * Test method for
	 * 'repast.simphony.engine.graph.DefaultGraphSchedule.DefaultGraphSchedule()'
	 */
	public void testDefaultGraphSchedule() {
		new DefaultGraphSchedule();
	}

	/*
	 * Test method for
	 * 'repast.simphony.engine.graph.DefaultGraphSchedule.DefaultGraphSchedule(ISchedulableActionFactory)'
	 */
	public void testDefaultGraphScheduleISchedulableActionFactory()
			throws NoSuchFieldException {
		graphSchedule = new DefaultGraphSchedule(factory);
		assertSame(factory, PrivateAccessor.getField(graphSchedule,
				"actionFactory"));
	}

	/*
	 * Test method for
	 * 'repast.simphony.engine.graph.DefaultGraphSchedule.schedule(ScheduleParameters,
	 * Object, GraphExecutor<?>)'
	 */
	@SuppressWarnings("unchecked")
	public void testScheduleScheduleParametersObjectGraphExecutorOfQ() {
		DefaultContext context = new DefaultContext();

		// build a graph of the form:
		// |-->c
		// a->b|
		// ^ |-->d-->e
		// |-----------|
		Object a = "a";
		Object b = "b";
		Object c = "c";
		Object d = "d";
		Object e = "e";

		context.add(a);
		context.add(b);
		context.add(c);
		context.add(d);
		context.add(e);

		Network topology = NetworkFactoryFinder.createNetworkFactory(null).createNetwork(
						"foo", context, true);
		topology.addEdge(a, b);
		topology.addEdge(b, c);
		topology.addEdge(b, d);
		topology.addEdge(d, e);
		topology.addEdge(e, a, 2.5);

		ArrayList expectedOrder = new ArrayList();

		expectedOrder.add(a);
		expectedOrder.add(b);
		expectedOrder.add(c);
		expectedOrder.add(d);
		expectedOrder.add(e);
		expectedOrder.add(a);

		ArrayList expectedTimes = new ArrayList();
		expectedTimes.add(1.0);
		expectedTimes.add(2.0);
		expectedTimes.add(3.0);
		expectedTimes.add(3.0);
		expectedTimes.add(4.0);
		expectedTimes.add(6.5);

		final ArrayList resultantOrder = new ArrayList();
		final ArrayList resultantTimes = new ArrayList();

		final DefaultGraphSchedule schedule = new DefaultGraphSchedule();
		ScheduleParameters start = ScheduleParameters.createOneTime(1.0);
		schedule.schedule(start, a, new NetworkTopologyExecutor(schedule,
				start, new NetworkTraverser(topology), new Executor<Object>() {
					public void execute(Object toExecuteOn) {
						resultantOrder.add(toExecuteOn);
						resultantTimes.add(schedule.getTickCount());
						// System.out.println(schedule.getTickCount());
					}
				}, topology));

		for (int i = 0; i < 5; i++)
			schedule.execute();

		assertEquals(6.5, schedule.getTickCount());

		assertTrue(TestUtils.collectionsContentsEqual(expectedOrder,
				resultantOrder));
		assertTrue(TestUtils.collectionsContentsEqual(expectedTimes,
				resultantTimes));
	}

	/*
	 * Test method for
	 * 'repast.simphony.engine.graph.DefaultGraphSchedule.schedule(ScheduleParameters,
	 * Object, NetworkTopology, Executor<Object>)'
	 */
	@SuppressWarnings("unchecked")
	public void testScheduleScheduleParametersObjectNetworkTopologyExecutorOfObject() {
		DefaultContext context = new DefaultContext();

		// build a graph of the form:
		// |-->c
		// a->b-|
		// ^ ^ |-->d-->e
		// | -----|
		// |-----------|

		final Object a = "a";
		final Object b = "b";
		final Object c = "c";
		final Object d = "d";
		final Object e = "e";

		context.add(a);
		context.add(b);
		context.add(c);
		context.add(d);
		context.add(e);

		final Network topology = NetworkFactoryFinder.createNetworkFactory(null).createNetwork(
						"foo", context, true);
		topology.addEdge(a, b);
		topology.addEdge(b, c);
		topology.addEdge(b, d);
		topology.addEdge(d, b);
		topology.addEdge(d, e);
		topology.addEdge(e, a, 2.5);

		ArrayList expectedOrder = new ArrayList();

		expectedOrder.add(a);
		expectedOrder.add(b);
		expectedOrder.add(c);
		expectedOrder.add(d);
		expectedOrder.add(e);
		expectedOrder.add(a);
		expectedOrder.add(b);

		ArrayList expectedTimes = new ArrayList();
		expectedTimes.add(1.0);
		expectedTimes.add(2.0);
		expectedTimes.add(3.0);
		expectedTimes.add(3.0);
		expectedTimes.add(4.0);
		expectedTimes.add(6.5);
		expectedTimes.add(7.5);

		final ArrayList resultantOrder = new ArrayList();
		final ArrayList resultantTimes = new ArrayList();

		final DefaultGraphSchedule schedule = new DefaultGraphSchedule();
		ScheduleParameters start = ScheduleParameters.createOneTime(1.0);
		schedule.schedule(start, a, topology, new Executor<Object>() {
			public void execute(Object toExecuteOn) {
				// remove the edge a->b when we are at the beginning again
				if (toExecuteOn == a && resultantOrder.contains(a)) {
					topology.removeEdge(topology.getEdge(b, d));
				}
				resultantOrder.add(toExecuteOn);
				resultantTimes.add(schedule.getTickCount());
			}
		});

		schedule.schedule(ScheduleParameters.createOneTime(8), new Object() {
			public void removeEdge(Object source, Object target) {
				topology.removeEdge(topology.getEdge(source, target));
			}
		}, "removeEdge", b, c);

		while (schedule.getActionCount() > 0) {
			schedule.execute();
		}

		assertEquals(8.5, schedule.getTickCount());

		assertTrue(TestUtils.collectionsContentsEqual(expectedOrder,
				resultantOrder));
		assertTrue(TestUtils.collectionsContentsEqual(expectedTimes,
				resultantTimes));
	}

}
