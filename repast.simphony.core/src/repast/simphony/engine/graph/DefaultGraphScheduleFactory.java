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

import repast.simphony.engine.schedule.DefaultSchedulableActionFactory;
import repast.simphony.engine.schedule.ISchedulableActionFactory;
import repast.simphony.engine.schedule.IScheduleFactory;

/**
 * ScheduleFactory that produces
 * {@link repast.simphony.engine.graph.DefaultGraphSchedule}s.
 * 
 * @author Jerry Vos
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 */
public class DefaultGraphScheduleFactory implements IScheduleFactory {
	private ISchedulableActionFactory defaultActionFactory;

	/**
	 * Synonymous with DefaultGraphScheduleFactory(new
	 * DefaultSchedulableaActionFactory()).
	 * 
	 * @see DefaultSchedulableActionFactory
	 * @see #DefaultGraphScheduleFactory(ISchedulableActionFactory)
	 */
	public DefaultGraphScheduleFactory() {
		this(new DefaultSchedulableActionFactory());
	}

	/**
	 * Constructs this graph schedule factory which will default to producing
	 * graphs with the specified action factory.
	 * 
	 * @param defaultActionFactory
	 *            the factory the schedule will use to construct actions
	 */
	public DefaultGraphScheduleFactory(
			ISchedulableActionFactory defaultActionFactory) {
		super();

		this.defaultActionFactory = defaultActionFactory;
	}

	/**
	 * Creates a schedule using the specified action factory
	 * 
	 * @param factory
	 *            the factory to use to construct actions
	 */
	public IGraphSchedule createSchedule(ISchedulableActionFactory factory) {
		return new DefaultGraphSchedule(factory);
	}

	/**
	 * Constructs a DefaultGraphSchedule using the default action factory
	 * specified in this class's construction.
	 * 
	 * @see #DefaultGraphScheduleFactory(ISchedulableActionFactory)
	 */
	public IGraphSchedule createSchedule() {
		return new DefaultGraphSchedule(defaultActionFactory);
	}

	/**
	 * Sets the default action factory to be used with constructing schedules
	 * 
	 * @see #createSchedule()
	 * 
	 * @param defaultFactory
	 *            the default factory to use when constructing schedules
	 */
	public void setDefaultSchedulableActionFactory(
			ISchedulableActionFactory defaultFactory) {
		this.defaultActionFactory = defaultFactory;
	}
}
