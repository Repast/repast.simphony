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
public class BehaviorStepBind extends PropertyOrStepWithLabelBind implements
		IPropertyHolderBind {

	public static String ALIAS = "behaviorstep";

	private String scheduleAnnotationStart = "";
	private String scheduleAnnotationPick = "";
	private String scheduleAnnotationInterval = "";
	private String scheduleAnnotationPriority = "";
	private String scheduleAnnotationDuration = "";
	private String scheduleAnnotationShuffle = "0";

	private String watchAnnotationId = "";
	private String watchAnnotationQuery = "";
	private String watchAnnotationTargetClassName = "";
	private String watchAnnotationTargetFieldNames = "";
	private String watchAnnotationTriggerCondition = "";
	private String watchAnnotationTriggerSchedule = "1";
	private String watchAnnotationTriggerDelta = "";
	private String watchAnnotationTriggerPriority = "";
	private String watchAnnotationPick = "";

	private String comment = "This is the step behavior.";
	private String visibility = "0";
	private String returnType = "";
	private String compiledName = "step";
	private String parameters = "";
	private String exceptions = "";

	private List properties;

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
		return BindConsts.REF_PREFIX_START_FLOWLET
				+ agentModelBind.getBehaviorStepFlowlets().indexOf(this);
	}

	/**
	 * @see repast.simphony.agents.model.bind.IPropertyOrStepBind#getType()
	 */
	public String getType() {
		return AgentBuilderConsts.BEHAVIOR;
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

	public String getScheduleAnnotationStart() {
		return scheduleAnnotationStart;
	}

	public void setScheduleAnnotationStart(String scheduleAnnotationStart) {
		this.scheduleAnnotationStart = scheduleAnnotationStart;
	}

	public String getScheduleAnnotationInterval() {
		return scheduleAnnotationInterval;
	}

	public void setScheduleAnnotationInterval(String scheduleAnnotationInterval) {
		this.scheduleAnnotationInterval = scheduleAnnotationInterval;
	}

	public String getScheduleAnnotationPriority() {
		return scheduleAnnotationPriority;
	}

	public void setScheduleAnnotationPriority(String scheduleAnnotationPriority) {
		this.scheduleAnnotationPriority = scheduleAnnotationPriority;
	}

	public String getScheduleAnnotationDuration() {
		return scheduleAnnotationDuration;
	}

	public void setScheduleAnnotationDuration(String scheduleAnnotationDuration) {
		this.scheduleAnnotationDuration = scheduleAnnotationDuration;
	}

	public String getScheduleAnnotationShuffle() {
		return scheduleAnnotationShuffle;
	}

	public void setScheduleAnnotationShuffle(String scheduleAnnotationShuffle) {
		this.scheduleAnnotationShuffle = scheduleAnnotationShuffle;
	}

	public String getWatchAnnotationId() {
		return watchAnnotationId;
	}

	public void setWatchAnnotationId(String watchAnnotationId) {
		this.watchAnnotationId = watchAnnotationId;
	}

	public String getWatchAnnotationQuery() {
		return watchAnnotationQuery;
	}

	public void setWatchAnnotationQuery(String watchAnnotationQuery) {
		this.watchAnnotationQuery = watchAnnotationQuery;
	}

	public String getWatchAnnotationTargetClassName() {
		return watchAnnotationTargetClassName;
	}

	public void setWatchAnnotationTargetClassName(
			String watchAnnotationTargetClassName) {
		this.watchAnnotationTargetClassName = watchAnnotationTargetClassName;
	}

	public String getWatchAnnotationTargetFieldNames() {
		return watchAnnotationTargetFieldNames;
	}

	public void setWatchAnnotationTargetFieldNames(
			String watchAnnotationTargetFieldNames) {
		this.watchAnnotationTargetFieldNames = watchAnnotationTargetFieldNames;
	}

	public String getWatchAnnotationTriggerCondition() {
		return watchAnnotationTriggerCondition;
	}

	public void setWatchAnnotationTriggerCondition(
			String watchAnnotationTriggerCondition) {
		this.watchAnnotationTriggerCondition = watchAnnotationTriggerCondition;
	}

	public String getWatchAnnotationTriggerSchedule() {
		return watchAnnotationTriggerSchedule;
	}

	public void setWatchAnnotationTriggerSchedule(
			String watchAnnotationTriggerSchedule) {
		this.watchAnnotationTriggerSchedule = watchAnnotationTriggerSchedule;
	}

	public String getWatchAnnotationTriggerDelta() {
		return watchAnnotationTriggerDelta;
	}

	public void setWatchAnnotationTriggerDelta(
			String watchAnnotationTriggerDelta) {
		this.watchAnnotationTriggerDelta = watchAnnotationTriggerDelta;
	}

	public String getWatchAnnotationTriggerPriority() {
		return watchAnnotationTriggerPriority;
	}

	public void setWatchAnnotationTriggerPriority(
			String watchAnnotationTriggerPriority) {
		this.watchAnnotationTriggerPriority = watchAnnotationTriggerPriority;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public String getCompiledName() {
		return compiledName;
	}

	public void setCompiledName(String compiledName) {
		this.compiledName = compiledName;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public String getExceptions() {
		return exceptions;
	}

	public void setExceptions(String exceptions) {
		this.exceptions = exceptions;
	}

	public String getWatchAnnotationPick() {
		return watchAnnotationPick;
	}

	public void setWatchAnnotationPick(String watchAnnotationPick) {
		this.watchAnnotationPick = watchAnnotationPick;
	}

	public String getScheduleAnnotationPick() {
		return scheduleAnnotationPick;
	}

	public void setScheduleAnnotationPick(String scheduleAnnotationPick) {
		this.scheduleAnnotationPick = scheduleAnnotationPick;
	}

}
