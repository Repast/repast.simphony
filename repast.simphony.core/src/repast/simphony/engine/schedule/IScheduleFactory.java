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
package repast.simphony.engine.schedule;


/**
 * Interface for Factories producing Schedules.<p>
 * 
 * @see repast.simphony.engine.schedule.Schedule
 * @see repast.simphony.engine.schedule.ISchedulableAction
 * @see repast.simphony.engine.schedule.ISchedulableActionFactory
 * 
 * @author Jerry Vos
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 */
public interface IScheduleFactory {
	/**
	 * Creates a Schedule that will use the specified ISchedulableActionFactory
	 * to create its scheduled actions.
	 * 
	 * @param factory
	 *            the factory to use to create the actions that the Schedule
	 *            will schedule
	 * 
	 * @return a schedule that uses the specified factory
	 */
	ISchedule createSchedule(ISchedulableActionFactory factory);

	/**
	 * Creates a Schedule that by default uses the factory set in this interface
	 * to create its scheduled actions.<p/>
	 * 
	 * The behavior that occurs when a default action factory has not been set
	 * is per-implementation specific, but in most cases there should be a
	 * reasonable default.
	 * 
	 * @return a schedule that uses the default factory
	 */
	ISchedule createSchedule();

	/**
	 * Sets the action factory that will be used when createSchedule is called
	 * with no arguments.
	 * 
	 * @param defaultFactory
	 *            the default factory to use if one isn't specified
	 */
	void setDefaultSchedulableActionFactory(
			ISchedulableActionFactory defaultFactory);
}
