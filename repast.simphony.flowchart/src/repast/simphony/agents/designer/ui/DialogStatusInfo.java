/*
 * Copyright (c) 2003, Alexander Greif All rights reserved. (Adapted by Michael
 * J. North for Use in Repast Simphony from Alexander Greif’s Flow4J-Eclipse
 * (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks to the
 * Original Author) (Michael J. North’s Modifications are Copyright 2007 Under
 * the Repast Simphony License, All Rights Reserved)
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

package repast.simphony.agents.designer.ui;

import org.eclipse.core.runtime.IStatus;

import repast.simphony.agents.designer.AgentBuilderDesignerException;
import repast.simphony.agents.designer.core.AgentBuilderPlugin;

/**
 * A settable IStatus. Can be an error, warning, info or ok. For error, info and
 * warning states, a message describes the problem.
 */
public class DialogStatusInfo implements IStatus {

	private String message;
	private int severity;

	/**
	 * Creates a status set to OK (no message)
	 */
	public DialogStatusInfo() {
		this(OK, null);
	}

	/**
	 * Creates a status .
	 * 
	 * @param severity
	 *            The status severity: ERROR, WARNING, INFO and OK.
	 * @param message
	 *            The message of the status. Applies only for ERROR, WARNING and
	 *            INFO.
	 */
	public DialogStatusInfo(int severity, String message) {
		this.message = message;
		this.severity = severity;
	}

	/**
	 * Returns if the status' severity is OK.
	 */
	public boolean isOK() {
		return severity == IStatus.OK;
	}

	/**
	 * Returns if the status' severity is WARNING.
	 */
	public boolean isWarning() {
		return severity == IStatus.WARNING;
	}

	/**
	 * Returns if the status' severity is INFO.
	 */
	public boolean isInfo() {
		return severity == IStatus.INFO;
	}

	/**
	 * Returns if the status' severity is ERROR.
	 */
	public boolean isError() {
		return severity == IStatus.ERROR;
	}

	/**
	 * @see IStatus#getMessage
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the status to ERROR.
	 * 
	 * @param The
	 *            error message (can be empty, but not null)
	 */
	public void setError(String errorMessage) {
		if (errorMessage == null)
			throw new AgentBuilderDesignerException();
		message = errorMessage;
		severity = IStatus.ERROR;
	}

	/**
	 * Sets the status to WARNING.
	 * 
	 * @param The
	 *            warning message (can be empty, but not null)
	 */
	public void setWarning(String warningMessage) {
		if (warningMessage == null)
			throw new AgentBuilderDesignerException();
		message = warningMessage;
		severity = IStatus.WARNING;
	}

	/**
	 * Sets the status to INFO.
	 * 
	 * @param The
	 *            info message (can be empty, but not null)
	 */
	public void setInfo(String infoMessage) {
		if (infoMessage == null)
			throw new AgentBuilderDesignerException();
		message = infoMessage;
		severity = IStatus.INFO;
	}

	/**
	 * Sets the status to OK.
	 */
	public void setOK() {
		message = null;
		severity = IStatus.OK;
	}

	/*
	 * @see IStatus#matches(int)
	 */
	public boolean matches(int severityMask) {
		return (severity & severityMask) != 0;
	}

	/**
	 * Returns always <code>false</code>.
	 * 
	 * @see IStatus#isMultiStatus()
	 */
	public boolean isMultiStatus() {
		return false;
	}

	/*
	 * @see IStatus#getSeverity()
	 */
	public int getSeverity() {
		return severity;
	}

	/*
	 * @see IStatus#getPlugin()
	 */
	public String getPlugin() {
		return AgentBuilderPlugin.AGENT_BUILDER_PLUGIN_ID;
	}

	/**
	 * Returns always <code>null</code>.
	 * 
	 * @see IStatus#getException()
	 */
	public Throwable getException() {
		return null;
	}

	/**
	 * Returns always the error severity.
	 * 
	 * @see IStatus#getCode()
	 */
	public int getCode() {
		return severity;
	}

	/**
	 * Returns always <code>null</code>.
	 * 
	 * @see IStatus#getChildren()
	 */
	public IStatus[] getChildren() {
		return new IStatus[0];
	}

}
