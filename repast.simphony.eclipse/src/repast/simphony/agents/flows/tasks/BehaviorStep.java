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

import java.util.Properties;

/**
 * Represents a start flowlet in the flow. External frameworks can iterate over
 * the start flowlets of a flow to determine the flow's entry points.s
 * 
 * @author greifa (Adapted by Michael J. North for Use in Repast Simphony from
 *         Alexander Greif’s Flow4J-Eclipse
 *         (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks
 *         to the Original Author)
 * 
 */
public class BehaviorStep implements IAgentPropertyorStep {

	private String name;
	private Properties properties;

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            the name of the start flowlet.
	 */
	public BehaviorStep(String name) {
		setName(name);
	}

	/**
	 * Sets the name of the start flowlet.
	 * 
	 * @param name
	 *            the name of the start flowlet.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the name of the start flowlet.
	 * 
	 * @return the name of the start flowlet
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets a property.
	 * 
	 * @param key
	 *            the property key.
	 * @param value
	 *            the property value.
	 */
	public void setProperty(String key, String value) {
		if (properties == null)
			properties = new Properties();

		properties.setProperty(key, value);
	}

	/**
	 * Returns the value of a property which is associated with the given key or
	 * <code>null</code> if the start flowlet has no properties or the flowlet
	 * has no property with the given key.
	 * 
	 * @param key
	 * @return the value of a property which is associated with the given key.
	 */
	public String getProperty(String key) {
		return properties == null ? null : properties.getProperty(key);
	}

	/**
	 * Returns <code>null</code> if the flowlet has no properties or the
	 * Properties instance that has at least one property.
	 * 
	 * @return <code>null</code> if the flowlet has no properties or the
	 *         Properties instance that has at least one property.
	 */
	public Properties getProperties() {
		return properties;
	}

}
