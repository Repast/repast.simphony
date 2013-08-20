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
 * A container class for holding properties of a task flowlet. Task flowlet
 * implementors can declare properties for a task flowlet. Every instance of the
 * same task flowlet type can have a different value of the same property, which
 * helps s lot to improve the task reusability. <br>
 * For example a SendMail task an have a property for setting the smtp address.
 * So the SendMail task can be used many times in the applicatgion with
 * different smtp addresses.<br>
 * Convenience method are suppoted to add property descriptors.
 * 
 * @author agreif (Adapted by Michael J. North for Use in Repast Simphony from
 *         Alexander Greif’s Flow4J-Eclipse
 *         (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks
 *         to the Original Author)
 * 
 */
@SuppressWarnings("unchecked")
public class TaskPropertyDescriptors {

	private List propertyDescriptors = new ArrayList();

	/**
	 * Add a task property descriptor
	 * 
	 * @param name
	 *            name of the property
	 * @param description
	 *            the property's description
	 */
	public void addPropertyDescriptor(String name, String description) {
		propertyDescriptors.add(new TaskPropertyDescriptor(name, description));
	}

	/**
	 * Returns an Iterator object of the property descriptors.
	 * 
	 * @return an Iterator object of the property descriptors.
	 */
	public Iterator iterator() {
		return propertyDescriptors.iterator();
	}

	/**
	 * Returns whether this instance contains any property descriptors.
	 * 
	 * @return true is this instance contains any property descriptors
	 */
	public boolean isEmpty() {
		return propertyDescriptors.isEmpty();
	}

}
