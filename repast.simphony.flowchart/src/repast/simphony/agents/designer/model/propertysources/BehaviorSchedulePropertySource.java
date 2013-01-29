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

package repast.simphony.agents.designer.model.propertysources;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import repast.simphony.agents.designer.core.AgentBuilderPlugin;
import repast.simphony.agents.designer.model.propertydescriptors.EditableComboBoxPropertyDescriptor;
import repast.simphony.agents.designer.model.propertydescriptors.ExtendedComboBoxPropertyDescriptor;

/**
 * @author agreif (Adapted by Michael J. North for Use in Repast Simphony from
 *         Alexander Greif’s Flow4J-Eclipse
 *         (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks
 *         to the Original Author)
 * 
 * TODO
 * 
 * 
 * 
 */
public class BehaviorSchedulePropertySource implements IPropertySource {

	private static final String PROP_BEHAVIOR_SCHEDULEANNOTATIONSTART = "behavior_scheduleAnnotationStart";
	private static final String PROP_BEHAVIOR_SCHEDULEANNOTATIONPICK = "behavior_scheduleAnnotationPick";
	private static final String PROP_BEHAVIOR_SCHEDULEANNOTATIONINTERVAL = "behavior_scheduleAnnotationInterval";
	private static final String PROP_BEHAVIOR_SCHEDULEANNOTATIONPRIORITY = "behavior_scheduleAnnotationPriority";
	private static final String PROP_BEHAVIOR_SCHEDULEANNOTATIONDURATION = "behavior_scheduleAnnotationDuration";
	private static final String PROP_BEHAVIOR_SCHEDULEANNOTATIONSHUFFLE = "behavior_scheduleAnnotationShuffle";

	private String scheduleAnnotationStart = "";
	private String scheduleAnnotationPick = "";
	private String scheduleAnnotationInterval = "";
	private String scheduleAnnotationPriority = "";
	private String scheduleAnnotationDuration = "";
	private Integer scheduleAnnotationShuffle = new Integer(0);
	private static String[] scheduleAnnotationShuffleList = { "True", "False" };
	private static String[] schedulePriorityList = { "",
			"ScheduleParameters.RANDOM_PRIORITY",
			"ScheduleParameters.FIRST_PRIORITY",
			"ScheduleParameters.LAST_PRIORITY" };
	private static String[] schedulePickList = { "", "ScheduledMethod.ALL" };
	private static String[] scheduleStartList = { "", "ScheduledMethod.END" };

	private static IPropertyDescriptor[] descriptors;

	static {
		descriptors = new IPropertyDescriptor[] {

				new EditableComboBoxPropertyDescriptor(
						PROP_BEHAVIOR_SCHEDULEANNOTATIONSTART,
						AgentBuilderPlugin
								.getResourceString("PropertyDescriptor_BehaviorStep.scheduleAnnotationStart"),
						scheduleStartList, null),
				new EditableComboBoxPropertyDescriptor(
						PROP_BEHAVIOR_SCHEDULEANNOTATIONPICK,
						AgentBuilderPlugin
								.getResourceString("PropertyDescriptor_BehaviorStep.scheduleAnnotationPick"),
						schedulePickList, null),
				new TextPropertyDescriptor(
						PROP_BEHAVIOR_SCHEDULEANNOTATIONINTERVAL,
						AgentBuilderPlugin
								.getResourceString("PropertyDescriptor_BehaviorStep.scheduleAnnotationInterval")),
				new EditableComboBoxPropertyDescriptor(
						PROP_BEHAVIOR_SCHEDULEANNOTATIONPRIORITY,
						AgentBuilderPlugin
								.getResourceString("PropertyDescriptor_BehaviorStep.scheduleAnnotationPriority"),
						schedulePriorityList, null),
				new TextPropertyDescriptor(
						PROP_BEHAVIOR_SCHEDULEANNOTATIONDURATION,
						AgentBuilderPlugin
								.getResourceString("PropertyDescriptor_BehaviorStep.scheduleAnnotationDuration")),
				new ExtendedComboBoxPropertyDescriptor(
						PROP_BEHAVIOR_SCHEDULEANNOTATIONSHUFFLE,
						AgentBuilderPlugin
								.getResourceString("PropertyDescriptor_BehaviorStep.scheduleAnnotationShuffle"),
						scheduleAnnotationShuffleList)

		};
	}

	/**
	 * Constructor
	 * 
	 */
	public BehaviorSchedulePropertySource() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see org.eclipse.ui.views.properties.IPropertySource#getEditableValue()
	 */
	public Object getEditableValue() {
		return this;
	}

	/**
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyDescriptors()
	 */
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return descriptors;
	}

	/**
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java.lang.Object)
	 */
	public Object getPropertyValue(Object id) {

		if (PROP_BEHAVIOR_SCHEDULEANNOTATIONSTART.equals(id)) {
			return getScheduleAnnotationStart();
		} else if (PROP_BEHAVIOR_SCHEDULEANNOTATIONPICK.equals(id)) {
			return getScheduleAnnotationPick();
		} else if (PROP_BEHAVIOR_SCHEDULEANNOTATIONINTERVAL.equals(id)) {
			return getScheduleAnnotationInterval();
		} else if (PROP_BEHAVIOR_SCHEDULEANNOTATIONPRIORITY.equals(id)) {
			return getScheduleAnnotationPriority();
		} else if (PROP_BEHAVIOR_SCHEDULEANNOTATIONDURATION.equals(id)) {
			return getScheduleAnnotationDuration();
		} else if (PROP_BEHAVIOR_SCHEDULEANNOTATIONSHUFFLE.equals(id)) {
			return getScheduleAnnotationShuffle();
		} else {
			return "";
		}

	}

	/**
	 * @see org.eclipse.ui.views.properties.IPropertySource#isPropertySet(java.lang.Object)
	 */
	public boolean isPropertySet(Object id) {
		return (PROP_BEHAVIOR_SCHEDULEANNOTATIONSTART.equals(id)
				|| PROP_BEHAVIOR_SCHEDULEANNOTATIONPICK.equals(id)
				|| PROP_BEHAVIOR_SCHEDULEANNOTATIONINTERVAL.equals(id)
				|| PROP_BEHAVIOR_SCHEDULEANNOTATIONPRIORITY.equals(id)
				|| PROP_BEHAVIOR_SCHEDULEANNOTATIONDURATION.equals(id) || PROP_BEHAVIOR_SCHEDULEANNOTATIONSHUFFLE
				.equals(id));
	}

	/**
	 * @see org.eclipse.ui.views.properties.IPropertySource#resetPropertyValue(java.lang.Object)
	 */
	public void resetPropertyValue(Object id) {
	}

	/**
	 * @see org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(java.lang.Object,
	 *      java.lang.Object)
	 */
	public void setPropertyValue(Object id, Object value) {

		if (PROP_BEHAVIOR_SCHEDULEANNOTATIONSTART.equals(id)) {
			setScheduleAnnotationStart((String) value);
		} else if (PROP_BEHAVIOR_SCHEDULEANNOTATIONPICK.equals(id)) {
			setScheduleAnnotationPick((String) value);
		} else if (PROP_BEHAVIOR_SCHEDULEANNOTATIONINTERVAL.equals(id)) {
			setScheduleAnnotationInterval((String) value);
		} else if (PROP_BEHAVIOR_SCHEDULEANNOTATIONPRIORITY.equals(id)) {
			setScheduleAnnotationPriority((String) value);
		} else if (PROP_BEHAVIOR_SCHEDULEANNOTATIONDURATION.equals(id)) {
			setScheduleAnnotationDuration((String) value);
		} else if (PROP_BEHAVIOR_SCHEDULEANNOTATIONSHUFFLE.equals(id)) {
			setScheduleAnnotationShuffle((Integer) value);
		}

	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new String("");//$NON-NLS-1$
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

	public Integer getScheduleAnnotationShuffle() {
		return scheduleAnnotationShuffle;
	}

	public void setScheduleAnnotationShuffle(Integer scheduleAnnotationShuffle) {
		this.scheduleAnnotationShuffle = scheduleAnnotationShuffle;
	}

	public String[] getScheduleAnnotationShuffleList() {
		return scheduleAnnotationShuffleList;
	}

	public void setScheduleAnnotationShuffleList(
			String[] scheduleAnnotationShuffleList) {
		BehaviorSchedulePropertySource.scheduleAnnotationShuffleList = scheduleAnnotationShuffleList;
	}

	public String getScheduleAnnotationPick() {
		return scheduleAnnotationPick;
	}

	public void setScheduleAnnotationPick(String scheduleAnnotationPick) {
		this.scheduleAnnotationPick = scheduleAnnotationPick;
	}

}
