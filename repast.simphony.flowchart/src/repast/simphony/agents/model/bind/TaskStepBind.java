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

package repast.simphony.agents.model.bind;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import repast.simphony.agents.AgentBuilderConsts;

/**
 * @author greifa (Adapted by Michael J. North for Use in Repast Simphony from
 *         Alexander Greif’s Flow4J-Eclipse
 *         (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks
 *         to the Original Author)
 * 
 * 
 * 
 */
@SuppressWarnings("unchecked")
public class TaskStepBind extends PropertyOrStepWithLabelBind implements
		IPropertyHolderBind {

	public static String ALIAS = "taskstep";

	private List properties;
	protected static ClassLoader taskClassLoader;
	private String comment = "";
	private String command1 = "";
	private String command2 = "";
	private String command3 = "";
	private String command4 = "";
	private String command5 = "";

	/**
	 * @return
	 */
	public List getProperties() {
		return properties;
	}

	/**
	 * @param list
	 */
	public void setProperties(List list) {
		properties = list;
	}

	/**
	 * TODO
	 * 
	 * @see repast.simphony.agents.model.bind.IPropertyOrStepBind#getId()
	 */
	public String getId(AgentModelBind agentModelBind) {
		return BindConsts.REF_PREFIX_JAVATASK_FLOWLET
				+ agentModelBind.getTaskFlowlets().indexOf(this);
	}

	/**
	 * @see repast.simphony.agents.model.bind.IPropertyOrStepBind#getType()
	 */
	public String getType() {
		return AgentBuilderConsts.TASK;
	}

	/**
	 * @param loader
	 */
	public static void setTaskClassLoader(ClassLoader loader) {
		taskClassLoader = loader;
	}

	/**
	 * @see repast.simphony.agents.model.bind.IPropertyHolderBind#getProperty(java.lang.String)
	 */
	public String getProperty(String key) {
		String result = null;
		if (properties == null)
			return null;
		for (Iterator iter = properties.iterator(); iter.hasNext();) {
			PropertyBind prop = (PropertyBind) iter.next();
			if (key.equals(prop.getName()))
				return prop.getValue();
		}

		return result;
	}

	public void addProperty(PropertyBind propBind) {
		if (properties == null)
			properties = new ArrayList();
		properties.add(propBind);
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCommand1() {
		return command1;
	}

	public void setCommand1(String command1) {
		this.command1 = command1;
	}

	public String getCommand2() {
		return command2;
	}

	public void setCommand2(String command2) {
		this.command2 = command2;
	}

	public String getCommand3() {
		return command3;
	}

	public void setCommand3(String command3) {
		this.command3 = command3;
	}

	public String getCommand4() {
		return command4;
	}

	public void setCommand4(String command4) {
		this.command4 = command4;
	}

	public String getCommand5() {
		return command5;
	}

	public void setCommand5(String command5) {
		this.command5 = command5;
	}

}
