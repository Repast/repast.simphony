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

import repast.simphony.engine.schedule.IAction;
import repast.simphony.engine.schedule.ISchedule;

import java.util.ArrayList;


/**
 * Default implementation of a ScheduleRegistry.
 * 
 * @see repast.simphony.engine.environment.ScheduleRegistry
 * 
 * @author Jerry Vos
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 */
public class DefaultScheduleRegistry implements ScheduleRegistry {
	private ISchedule modelSchedule;

	private ArrayList<IAction> preRunActions;

	private ArrayList<IAction> postRunActions;
	
	private Runner scheduleRunner;

	/**
	 * Retrieves the schedule that models should use to schedule their actions.
	 * 
	 * @return the schedule to be used used by models
	 */
	public ISchedule getModelSchedule() {
		return modelSchedule;
	}

	/**
	 * Sets the schedule users should use to schedule their actions. Users
	 * should not use this method as it is intended for internal Repast use. If
	 * a user would like a custom schedule to be used they should specify a
	 * ScheduleFactory to the Controller.
	 * 
	 * @param modelSchedule
	 *            the schedule used by models
	 */
	public void setModelSchedule(ISchedule modelSchedule) {
		this.modelSchedule = modelSchedule;
	}

	/**
	 * Retrieves the actions that are to occur before a run of the model
	 * schedule.
	 * 
	 * @return the actions that are to occur before a run of the model schedule
	 */
	public Iterable<IAction> getPreRunActions() {
		return preRunActions;
	}

	/**
	 * Adds an action that should occur before a run of the model schedule.
	 * 
	 * @param action
	 *            an action that should occur before a run of the model schedule
	 */
	public void addPreRunAction(IAction action) {
		preRunActions.add(action);
	}

	/**
	 * Removes an action from the actions that should occur before a run of the
	 * model schedule.
	 * 
	 * @param action
	 *            an action that should occur before a run of the model schedule
	 */
	public void removePreRunAction(IAction action) {
		preRunActions.remove(action);
	}

	/**
	 * Retrieves the actions that are to occur after a run of the model
	 * schedule.
	 * 
	 * @return the actions that are to occur after a run of the model schedule
	 */
	public Iterable<IAction> getPostRunActions() {
		return postRunActions;
	}

	/**
	 * Adds an action that should occur after a run of the model schedule.
	 * 
	 * @param action
	 *            an action that should occur after a run of the model schedule
	 */
	public void addPostRunAction(IAction action) {
		postRunActions.add(action);
	}

	/**
	 * Removes an action from the actions that should occur after a run of the
	 * model schedule.
	 * 
	 * @param action
	 *            an action that should occur after a run of the model schedule
	 */
	public void removePostRunAction(IAction action) {
		postRunActions.remove(action);
	}

	/**
	 * Sets the runner used for executing the schedule.
	 * 
	 * @param runner
	 *            the schedule runner
	 */
	public void setScheduleRunner(Runner runner) {
		this.scheduleRunner = runner;
	}

	/**
	 * Retrieves the runner used for executing the schedule.
	 * 
	 * @return the schedule runner
	 */
	public Runner getScheduleRunner() {
		return scheduleRunner;
	}
}
