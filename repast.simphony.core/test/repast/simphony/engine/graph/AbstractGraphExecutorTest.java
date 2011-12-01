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
import org.jmock.MockObjectTestCase;

import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.space.graph.Traverser;

/**
 * Tests for {@link repast.simphony.engine.graph.AbstractGraphExecutor}.
 * 
 * @author Jerry Vos
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:26:02 $
 */
public class AbstractGraphExecutorTest extends MockObjectTestCase {
	AbstractGraphExecutor executor;
	Mock mockSchedule;
	Mock mockTraverser;
	Mock mockExecutor;
	ScheduleParameters params;

	class MockAbstractGraphExecutor extends AbstractGraphExecutor<Object> {
		public MockAbstractGraphExecutor(ISchedule schedule,
				ScheduleParameters baseParams, Traverser<Object> visitor,
				Executor<Object> nodeExecutor) {
			super(schedule, baseParams, visitor, nodeExecutor);
		}

		public boolean validateForExecution(GraphParams<Object> params) {
			return true;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void setUp() throws Exception {
		mockSchedule = mock(ISchedule.class);
		mockTraverser = mock(Traverser.class);
		mockExecutor = mock(Executor.class);
		params = ScheduleParameters.createOneTime(1);

		executor = new MockAbstractGraphExecutor((ISchedule) mockSchedule
				.proxy(), params, (Traverser) mockTraverser.proxy(),
				(Executor) mockExecutor.proxy());
	}

	/*
	 * Test method for
	 * 'repast.simphony.engine.graph.AbstractGraphExecutor.AbstractGraphExecutor(ISchedule,
	 * ScheduleParameters, Visitor<E>, Executor<E>)'
	 */
	public void testAbstractGraphExecutor() throws NoSuchFieldException {
		assertSame(mockSchedule.proxy(), PrivateAccessor.getField(executor,
				"schedule"));
		assertSame(mockTraverser.proxy(), PrivateAccessor.getField(executor,
				"traverser"));
		assertSame(mockExecutor.proxy(), PrivateAccessor.getField(executor,
				"nodeExecutor"));
		assertSame(params, PrivateAccessor.getField(executor,
				"baseParams"));
	}

}
