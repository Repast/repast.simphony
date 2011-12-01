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
package repast.simphony.engine.environment;

import junitx.util.PrivateAccessor;

import org.jmock.MockObjectTestCase;

import repast.simphony.engine.environment.DefaultScheduleRegistry;
import repast.simphony.engine.environment.GUIRegistry;
import repast.simphony.engine.environment.RunInfo;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.environment.ScheduleRegistry;

/**
 * Tests for {@link repast.simphony.engine.environment.RunState}.
 * 
 * @author Jerry Vos
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:35 $
 */
public class RunStateTest extends MockObjectTestCase {
	RunState runState;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void setUp() throws Exception {
		runState = new RunState(null);
	}
	
	/*
	 * Test method for 'repast.simphony.engine.engine.RunState.RunState(RunInfo<E>,
	 * ScheduleRegistry, LoggingRegistry)'
	 */
	@SuppressWarnings("unchecked")
	public void testRunStateRunInfoOfEScheduleRegistryLoggingRegistry() throws NoSuchFieldException {
		RunInfo runInfo = new RunInfo(null, 0, 0);
		ScheduleRegistry schedReg = (ScheduleRegistry) mock(ScheduleRegistry.class).proxy();
		
		GUIRegistry guiReg = (GUIRegistry) mock(GUIRegistry.class).proxy();
		RunState state = new RunState(runInfo, schedReg, guiReg);
		assertSame(runInfo, PrivateAccessor.getField(state, "runInfo"));
		assertSame(schedReg, PrivateAccessor.getField(state, "scheduleRegistry"));

	}

	/*
	 * Test method for 'repast.simphony.engine.engine.RunState.RunState(RunInfo<E>)'
	 */
	@SuppressWarnings("unchecked")
	public void testRunStateRunInfoOfE() throws NoSuchFieldException {
		RunInfo runInfo = new RunInfo(null, 0, 0);
		
		RunState state = new RunState(runInfo);
		
		assertSame(runInfo, PrivateAccessor.getField(state, "runInfo"));
		// test against docs
		assertTrue(PrivateAccessor.getField(state, "scheduleRegistry") instanceof DefaultScheduleRegistry);
	}

	/*
	 * Test method for 'repast.simphony.engine.engine.RunState.RunState(RunInfo<E>)'
	 */
	@SuppressWarnings("unchecked")
	public void testRunState() throws NoSuchFieldException {
		RunState state = new RunState();
		
		// test against docs
		assertTrue(PrivateAccessor.getField(state, "scheduleRegistry") instanceof DefaultScheduleRegistry);
	}
	

	/*
	 * Test method for
	 * 'repast.simphony.engine.engine.RunState.setLoggingRegistry(LoggingRegistry)'
	 */
	public void testSetGUIRegistry() throws NoSuchFieldException {
		GUIRegistry guiReg = (GUIRegistry) mock(GUIRegistry.class).proxy();
		
		runState.setGUIRegistry(guiReg);
		assertSame(guiReg, PrivateAccessor.getField(runState, "guiRegistry"));
	}

	/*
	 * Test method for 'repast.simphony.engine.engine.RunState.getLoggingRegistry()'
	 */
	@SuppressWarnings("unchecked")
	public void testGetGUIRegistry() throws NoSuchFieldException {
		GUIRegistry guiReg = (GUIRegistry) mock(GUIRegistry.class).proxy();
		
		PrivateAccessor.setField(runState, "guiRegistry", guiReg);
		assertSame(guiReg, runState.getGUIRegistry());
	}

	/*
	 * Test method for 'repast.simphony.engine.engine.RunState.getRunInfo()'
	 */
	@SuppressWarnings("unchecked")
	public void testGetRunInfo() throws NoSuchFieldException {
		RunInfo runInfo = new RunInfo(null, 0, 0);
		
		PrivateAccessor.setField(runState, "runInfo", runInfo);
		assertSame(runInfo, runState.getRunInfo());
	}

	/*
	 * Test method for 'repast.simphony.engine.engine.RunState.setRunInfo(RunInfo<E>)'
	 */
	@SuppressWarnings("unchecked")
	public void testSetRunInfo() throws NoSuchFieldException {
		RunInfo runInfo = new RunInfo(null, 0, 0);
		
		runState.setRunInfo(runInfo);
		assertSame(runInfo, PrivateAccessor.getField(runState, "runInfo"));
	}

	/*
	 * Test method for 'repast.simphony.engine.engine.RunState.getScheduleRegistry()'
	 */
	@SuppressWarnings("unchecked")
	public void testGetScheduleRegistry() throws NoSuchFieldException {
		ScheduleRegistry schedReg = (ScheduleRegistry) mock(ScheduleRegistry.class).proxy();
		
		PrivateAccessor.setField(runState, "scheduleRegistry", schedReg);
		
		assertSame(schedReg, runState.getScheduleRegistry());
	}

	/*
	 * Test method for
	 * 'repast.simphony.engine.engine.RunState.setScheduleRegistry(ScheduleRegistry)'
	 */
	public void testSetScheduleRegistry() throws NoSuchFieldException {
		ScheduleRegistry schedReg = (ScheduleRegistry) mock(ScheduleRegistry.class).proxy();
		
		runState.setScheduleRegistry(schedReg);
		assertSame(schedReg, PrivateAccessor.getField(runState, "scheduleRegistry"));
	}
}
