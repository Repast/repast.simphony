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

/**
 * A parameter descriptor describes
 * <ul>
 * <li>input parameters - entries in the dictionary which are expected by the
 * task </li>
 * <li>output parameters - entries which are put in the dictionary by the task.
 * </li>
 * </ul>
 * 
 * @author agreif (Adapted by Michael J. North for Use in Repast Simphony from
 *         Alexander Greif’s Flow4J-Eclipse
 *         (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks
 *         to the Original Author)
 * 
 */
@SuppressWarnings("unchecked")
public class TaskParameterDescriptor {

	private String name;
	private String type;
	private String description;

	/**
	 * Constructor
	 * 
	 * @param name
	 *            the parameter's name
	 * @param type
	 *            the parameter's type
	 * @param description
	 *            the parameter's description
	 */
	public TaskParameterDescriptor(String name, String type, String description) {
		this.name = name;
		this.type = type;
		this.description = description;
	}

	/**
	 * Constructor
	 * 
	 * @param name
	 *            the parameter's name
	 * @param clazz
	 *            the parameter's type as class
	 * @param description
	 *            the parameter's description
	 */
	public TaskParameterDescriptor(String name, Class clazz, String description) {
		this.name = name;
		this.type = clazz.getName();
		this.description = description;
	}

	/**
	 * Returns the parameter's description.
	 * 
	 * @return the parameter's description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Returns the parameter's name.
	 * 
	 * @return the parameter's name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the parameter's type.
	 * 
	 * @return the parameter's type.
	 */
	public String getType() {
		return type;
	}

}
