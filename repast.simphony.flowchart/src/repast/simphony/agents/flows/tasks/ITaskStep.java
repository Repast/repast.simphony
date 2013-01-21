/*
 * Copyright (c) 2003-2004, Alexander Greif. All rights reserved. (Adapted by
 * Michael J. North for Use in Repast Simphony from Alexander Greif’s
 * Flow4J-Eclipse (http://flow4jeclipse.sourceforge.net/docs/index.html), with
 * Thanks to the Original Author) (Michael J. North’s Modifications are
 * Copyright 2007 Under the Repast Simphony License, All Rights Reserved)
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer. * Redistributions in
 * binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other
 * materials provided with the distribution. * Neither the name of the
 * Flow4J-Eclipse project nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior
 * written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package repast.simphony.agents.flows.tasks;

import repast.simphony.agents.flows.tasks.descriptors.TaskPropertyDescriptors;

/**
 * @author alex
 * 
 * TODO
 * 
 * 
 * 
 */
public interface ITaskStep extends IAgentPropertyorStep {

	/**
	 * Returns the name of the taskflowlet. The name is used only to display it
	 * in the designer
	 * 
	 * @return the name of the taskflowlet
	 */
	public String getName();

	/**
	 * Executes the flowlet.
	 * 
	 * @param dict
	 *            the <code>AgentBuilderDictionary</code>contains data in a
	 *            map that can be used by this flowlet.
	 */
	public void execute(AgentBuilderDictionary dict);

	/**
	 * Returns the task's description. It is used in the designer property view.
	 * 
	 * @return the task's description.
	 */
	public String getDescription();

	/**
	 * Returns a Collection of Property descriptors.
	 * 
	 * @return a Collection of Property descriptors.
	 */
	public TaskPropertyDescriptors getPropertyDescriptors();

	/**
	 * Sets a string property of this task flowlet instance. This method is
	 * normally caled from the generated Flow class.
	 * 
	 * @param name
	 *            name of the proeprty
	 * @param value
	 *            value of the property
	 */
	public void setProperty(String name, String value);

	/**
	 * Returns the string value of the property with the given name or null if
	 * no value is associared with the given property name.
	 * 
	 * @param name
	 *            name of the property
	 * @return the string value of the property or null.
	 */
	public String getProperty(String name);

}
