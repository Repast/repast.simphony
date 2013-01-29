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

package repast.simphony.agents.flows.tasks.descriptors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This is collection class for task parameters. Two types of task parameters
 * are currently available
 * <ul>
 * <li>input parameters - describe which entries in the dictionary are expected
 * by the task </li>
 * <li>output parameters - describe which entries are put in the dictionary by
 * the task. </li>
 * </ul>
 * 
 * @author agreif (Adapted by Michael J. North for Use in Repast Simphony from
 *         Alexander Greif’s Flow4J-Eclipse
 *         (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks
 *         to the Original Author)
 * 
 */
@SuppressWarnings("unchecked")
public class TaskParameterDescriptors {

	private List parameterDescriptors = new ArrayList();

	/**
	 * Add a task parameter descriptor
	 * 
	 * @param name
	 *            name of the parameter
	 * @param type
	 *            name of the parameter
	 * @param description
	 *            the parameter's description
	 */
	public void addParameterDescriptor(String name, String type,
			String description) {
		parameterDescriptors.add(new TaskParameterDescriptor(name, type,
				description));
	}

	/**
	 * Add a task parameter descriptor
	 * 
	 * @param name
	 *            name of the parameter
	 * @param type
	 *            name of the parameter
	 * @param description
	 *            the parameter's description
	 */
	public void addParameterDescriptor(String name, Class clazz,
			String description) {
		parameterDescriptors.add(new TaskParameterDescriptor(name, clazz,
				description));
	}

	/**
	 * Returns an Iterator object of the parameter descriptors.
	 * 
	 * @return an Iterator object of the parameter descriptors.
	 */
	public Iterator iterator() {
		return parameterDescriptors.iterator();
	}

	/**
	 * Returns whether this instance contains any parameter descriptors.
	 * 
	 * @return true is this instance contains any parameter descriptors
	 */
	public boolean isEmpty() {
		return parameterDescriptors.isEmpty();
	}

	/**
	 * Returns the number of parameters.
	 * 
	 * @return the number of parameters.
	 */
	public int size() {
		return parameterDescriptors.size();
	}

}
