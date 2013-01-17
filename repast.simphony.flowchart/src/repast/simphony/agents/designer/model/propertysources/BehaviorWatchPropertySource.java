/*
 * Copyright (c) 2003, Alexander Greif All rights reserved. (Adapted by Michael
 * J. North for Use in Repast Simphony from Alexander Greif’s Flow4J-Eclipse
 * (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks to the
 * Original Author) (Michael J. North’s Modifications are Copyright 2007 Under
 * the Repast Simphony License, All Rights Reserved)
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. * Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of the Flow4J-Eclipse project nor
 * the names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
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

import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import repast.simphony.agents.designer.core.AgentBuilderPlugin;
import repast.simphony.agents.designer.model.BehaviorStepModelPart;
import repast.simphony.agents.designer.model.propertydescriptors.EditableComboBoxPropertyDescriptor;
import repast.simphony.agents.designer.model.propertydescriptors.ExtendedComboBoxPropertyDescriptor;
import repast.simphony.agents.designer.model.propertydescriptors.TypeLookupPropertyDescriptor;

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
public class BehaviorWatchPropertySource implements IPropertySource {

	private static final String PROP_BEHAVIOR_WATCHANNOTATIONID = "behavior_watchAnnotationId";
	private static final String PROP_BEHAVIOR_WATCHANNOTATIONQUERY = "behavior_watchAnnotationQuery";
	private static final String PROP_BEHAVIOR_WATCHANNOTATIONTARGETCLASSNAME = "behavior_watchAnnotationTargetClassName";
	private static final String PROP_BEHAVIOR_WATCHANNOTATIONTARGETFIELDNAMES = "behavior_watchAnnotationTargetFieldNames";
	private static final String PROP_BEHAVIOR_WATCHANNOTATIONTRIGGERCONDITION = "behavior_watchAnnotationTriggerCondition";
	private static final String PROP_BEHAVIOR_WATCHANNOTATIONTRIGGERSCHEDULE = "behavior_watchAnnotationTriggerSchedule=";
	private static final String PROP_BEHAVIOR_WATCHANNOTATIONTRIGGERDELTA = "behavior_watchAnnotationTriggerDelta";
	private static final String PROP_BEHAVIOR_WATCHANNOTATIONTRIGGERPRIORITY = "behavior_watchAnnotationTriggerPriority";
	private static final String PROP_BEHAVIOR_WATCHANNOTATIONPICK = "behavior_watchAnnotationPick";

	private String watchAnnotationId = "";
	private String watchAnnotationQuery = "";
	private String watchAnnotationTargetClassName = "";
	private String watchAnnotationTargetFieldNames = "";
	private String watchAnnotationTriggerCondition = "";
	private Integer watchAnnotationTriggerSchedule = new Integer(1);
	private static String[] watchAnnotationTriggerScheduleList = { "Immediate",
			"Later" };
	private String watchAnnotationTriggerDelta = "1";
	private String watchAnnotationTriggerPriority = "";
	private String watchAnnotationPick = "";
	private static String[] watchPriorityList = { "",
			"ScheduleParameters.RANDOM_PRIORITY",
			"ScheduleParameters.FIRST_PRIORITY",
			"ScheduleParameters.LAST_PRIORITY" };
	private static String[] triggerConditionList = {
			"",
			"$watchee.getVoltage() > 120",
			"$watcher.voltage < $watchee.getHappiness()",
			"!($watchee.getSize() > 120)",
			"($watchee.getWealth() > 120) && ($watcher.happiness < $watchee.getHappiness())",
			"($watchee.getWealth() > 120) || ($watcher.happiness < $watchee.getHappiness())" };
	private static String[] watchQueryList = { "", "colocated", "linked_to",
			"linked_to 'FriendshipNetwork'", "linked_from",
			"linked_from 'FriendshipNetwork'", "within 10",
			"within 10 'FriendshipNetwork'", "within_vn 2",
			"within_vn 2 'CityGrid'", "within_moore 3",
			"within_moore 3 'CityGrid'", "within 2",
			"within 2 'StateGeography'",
			"within 2 'family' and not linked_to 'business'",
			"within_moore 2 and within 10 'city of chicago'" };

	private IPropertyDescriptor[] descriptors;

	private BehaviorStepModelPart parent = null;

	/**
	 * Constructor
	 * 
	 */
	public BehaviorWatchPropertySource(BehaviorStepModelPart newParent) {
		super();
		this.parent = newParent;
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
		descriptors = new IPropertyDescriptor[] {

				new TextPropertyDescriptor(
						PROP_BEHAVIOR_WATCHANNOTATIONID,
						AgentBuilderPlugin
								.getResourceString("PropertyDescriptor_BehaviorStep.watchAnnotationId")),
				new EditableComboBoxPropertyDescriptor(
						PROP_BEHAVIOR_WATCHANNOTATIONQUERY,
						AgentBuilderPlugin
								.getResourceString("PropertyDescriptor_BehaviorStep.watchAnnotationQuery"),
						watchQueryList, null),
				new TypeLookupPropertyDescriptor(
						PROP_BEHAVIOR_WATCHANNOTATIONTARGETCLASSNAME,
						AgentBuilderPlugin
								.getResourceString("PropertyDescriptor_BehaviorStep.watchAnnotationTargetClassName"),
						false, IJavaElementSearchConstants.CONSIDER_ALL_TYPES,
						this.watchAnnotationTargetClassName),
				new TextPropertyDescriptor(
						PROP_BEHAVIOR_WATCHANNOTATIONTARGETFIELDNAMES,
						AgentBuilderPlugin
								.getResourceString("PropertyDescriptor_BehaviorStep.watchAnnotationTargetFieldNames")),
				new EditableComboBoxPropertyDescriptor(
						PROP_BEHAVIOR_WATCHANNOTATIONTRIGGERCONDITION,
						AgentBuilderPlugin
								.getResourceString("PropertyDescriptor_BehaviorStep.watchAnnotationTriggerCondition"),
						triggerConditionList, null),
				new ExtendedComboBoxPropertyDescriptor(
						PROP_BEHAVIOR_WATCHANNOTATIONTRIGGERSCHEDULE,
						AgentBuilderPlugin
								.getResourceString("PropertyDescriptor_BehaviorStep.watchAnnotationTriggerSchedule"),
						watchAnnotationTriggerScheduleList),
				new TextPropertyDescriptor(
						PROP_BEHAVIOR_WATCHANNOTATIONTRIGGERDELTA,
						AgentBuilderPlugin
								.getResourceString("PropertyDescriptor_BehaviorStep.watchAnnotationTriggerDelta")),
				new EditableComboBoxPropertyDescriptor(
						PROP_BEHAVIOR_WATCHANNOTATIONTRIGGERPRIORITY,
						AgentBuilderPlugin
								.getResourceString("PropertyDescriptor_BehaviorStep.watchAnnotationTriggerPriority"),
						watchPriorityList, null) };

		return descriptors;
	}

	/**
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java.lang.Object)
	 */
	public Object getPropertyValue(Object id) {

		if (PROP_BEHAVIOR_WATCHANNOTATIONID.equals(id)) {
			return getWatchAnnotationId();
		} else if (PROP_BEHAVIOR_WATCHANNOTATIONQUERY.equals(id)) {
			return getWatchAnnotationQuery();
		} else if (PROP_BEHAVIOR_WATCHANNOTATIONTARGETCLASSNAME.equals(id)) {
			return getWatchAnnotationTargetClassName();
		} else if (PROP_BEHAVIOR_WATCHANNOTATIONTARGETFIELDNAMES.equals(id)) {
			return getWatchAnnotationTargetFieldNames();
		} else if (PROP_BEHAVIOR_WATCHANNOTATIONTRIGGERCONDITION.equals(id)) {
			return getWatchAnnotationTriggerCondition();
		} else if (PROP_BEHAVIOR_WATCHANNOTATIONTRIGGERSCHEDULE.equals(id)) {
			return getWatchAnnotationTriggerSchedule();
		} else if (PROP_BEHAVIOR_WATCHANNOTATIONTRIGGERDELTA.equals(id)) {
			return getWatchAnnotationTriggerDelta();
		} else if (PROP_BEHAVIOR_WATCHANNOTATIONTRIGGERPRIORITY.equals(id)) {
			return getWatchAnnotationTriggerPriority();
		} else if (PROP_BEHAVIOR_WATCHANNOTATIONPICK.equals(id)) {
			return getWatchAnnotationPick();
		} else {
			return "";
		}

	}

	/**
	 * @see org.eclipse.ui.views.properties.IPropertySource#isPropertySet(java.lang.Object)
	 */
	public boolean isPropertySet(Object id) {
		return (PROP_BEHAVIOR_WATCHANNOTATIONID.equals(id)
				|| PROP_BEHAVIOR_WATCHANNOTATIONQUERY.equals(id)
				|| PROP_BEHAVIOR_WATCHANNOTATIONTARGETCLASSNAME.equals(id)
				|| PROP_BEHAVIOR_WATCHANNOTATIONTARGETFIELDNAMES.equals(id)
				|| PROP_BEHAVIOR_WATCHANNOTATIONTRIGGERCONDITION.equals(id)
				|| PROP_BEHAVIOR_WATCHANNOTATIONTRIGGERSCHEDULE.equals(id)
				|| PROP_BEHAVIOR_WATCHANNOTATIONTRIGGERDELTA.equals(id)
				|| PROP_BEHAVIOR_WATCHANNOTATIONTRIGGERPRIORITY.equals(id) || PROP_BEHAVIOR_WATCHANNOTATIONPICK
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

		if (PROP_BEHAVIOR_WATCHANNOTATIONID.equals(id)) {
			setWatchAnnotationId((String) value);
		} else if (PROP_BEHAVIOR_WATCHANNOTATIONQUERY.equals(id)) {
			setWatchAnnotationQuery((String) value);
		} else if (PROP_BEHAVIOR_WATCHANNOTATIONTARGETCLASSNAME.equals(id)) {
			setWatchAnnotationTargetClassName((String) value);
		} else if (PROP_BEHAVIOR_WATCHANNOTATIONTARGETFIELDNAMES.equals(id)) {
			setWatchAnnotationTargetFieldNames((String) value);
		} else if (PROP_BEHAVIOR_WATCHANNOTATIONTRIGGERCONDITION.equals(id)) {
			setWatchAnnotationTriggerCondition((String) value);
		} else if (PROP_BEHAVIOR_WATCHANNOTATIONTRIGGERSCHEDULE.equals(id)) {
			setWatchAnnotationTriggerSchedule((Integer) value);
		} else if (PROP_BEHAVIOR_WATCHANNOTATIONTRIGGERDELTA.equals(id)) {
			setWatchAnnotationTriggerDelta((String) value);
		} else if (PROP_BEHAVIOR_WATCHANNOTATIONTRIGGERPRIORITY.equals(id)) {
			setWatchAnnotationTriggerPriority((String) value);
		} else if (PROP_BEHAVIOR_WATCHANNOTATIONPICK.equals(id)) {
			setWatchAnnotationPick((String) value);
		}

	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new String("");//$NON-NLS-1$
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
		if ((this.watchAnnotationTargetClassName != null)
				&& (this.watchAnnotationTargetClassName.trim().length() > 0)
				&& (this.parent != null)
				&& ((this.parent.getParameters() == null)
						||
					this.parent.getParameters().equals(""))) {
			String newParameters = this.watchAnnotationTargetClassName.trim()
					+ " watchedAgent";
			this.parent.setParameters(newParameters);
			this.parent.firePropertyChange(
					BehaviorStepModelPart.PROP_BEHAVIOR_PARAMETERS, null,
					newParameters);
		}
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

	public Integer getWatchAnnotationTriggerSchedule() {
		return watchAnnotationTriggerSchedule;
	}

	public void setWatchAnnotationTriggerSchedule(
			Integer watchAnnotationTriggerSchedule) {
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

	public String getWatchAnnotationPick() {
		return watchAnnotationPick;
	}

	public void setWatchAnnotationPick(String watchAnnotationPick) {
		this.watchAnnotationPick = watchAnnotationPick;
	}

}
