/*
 * Copyright (c) 2003-2004, Alexander Greif. All rights reserved. (Adapted by
 * Michael J. North for Use in Repast Simphony from Alexander Greif’s
 * Flow4J-Eclipse (http://flow4jeclipse.sourceforge.net/docs/index.html), with
 * Thanks to the Original Author) (Michael J. North’s Modifications are
 * Copyright 2007 Under the Repast Simphony License, All Rights Reserved)
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

package repast.simphony.agents.designer.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import repast.simphony.agents.AgentBuilderConsts;
import repast.simphony.agents.designer.core.AgentBuilderPlugin;
import repast.simphony.agents.designer.model.propertydescriptors.EditableComboBoxPropertyDescriptor;
import repast.simphony.agents.designer.model.propertydescriptors.ExtendedComboBoxPropertyDescriptor;
import repast.simphony.agents.designer.ui.editors.AgentEditor;
import repast.simphony.agents.designer.ui.wizards.NewCodeWizardEntry;
import repast.simphony.agents.model.codegen.DepthCounter;

/**
 * 
 * Task flowlet model part.
 * 
 * @author agreif (Adapted by Michael J. North for Use in Repast Simphony from
 *         Alexander Greif’s Flow4J-Eclipse
 *         (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks
 *         to the Original Author)
 */
@SuppressWarnings("unchecked")
public class TaskStepModelPart extends AbstractAgentPropertyorStepModelPart {

	public static final String PROP_TASK_COMMENT = "task_comment"; //$NON-NLS-1$
	public static final String PROP_TASK_EXAMPLE1 = "task_example1"; //$NON-NLS-1$
	public static final String PROP_TASK_EXAMPLE2 = "task_example2"; //$NON-NLS-1$
	public static final String PROP_TASK_COMMAND1 = "task_command1"; //$NON-NLS-1$
	public static final String PROP_TASK_COMMAND2 = "task_command2"; //$NON-NLS-1$
	public static final String PROP_TASK_COMMAND3 = "task_command3"; //$NON-NLS-1$
	public static final String PROP_TASK_COMMAND4 = "task_command4"; //$NON-NLS-1$
	public static final String PROP_TASK_COMMAND5 = "task_command5"; //$NON-NLS-1$

	private HashSet decisionFlowletPropertyDescriptors = null;
	private String comment = "This is a task.";
	private Integer example1 = new Integer(0);
	private String[] exampleList1;
	private String[] exampleList2;

	private String example2 = "";
	private String taskCommand1 = "";
	private String taskCommand2 = "";
	private String taskCommand3 = "";
	private String taskCommand4 = "";
	private String taskCommand5 = "";

	private String[] commandList = {
			EditableComboBoxPropertyDescriptor.EDIT_USING_TEXT_BOX_DIALOG };

	/**
	 * TODO
	 * 
	 */
	public TaskStepModelPart() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see repast.simphony.agents.designer.model.AgentPropertyorStepModelPart#getType()
	 */
	public String getType() {
		return AgentBuilderConsts.TASK;
	}

	/**
	 * Returns the name of the resource entry that contains the property name
	 * 
	 * @return key in the resources file
	 * @see repast.simphony.agents.designer.model.AgentPropertyorStepWithLabelModelPart#getLabelPropertyNameResource()
	 */
	protected String getLabelPropertyNameResource() {
		return "PropertyDescriptor_PropertyOrStep.label";
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * Collects all property descriptors. Those of the superclass and those of
	 * this class
	 * 
	 * @return Collection with all Property descriptors
	 */
	protected Collection getPropertyDescriptorsCollection() {
		if (decisionFlowletPropertyDescriptors == null)
			createPropertyDescriptors();

		return decisionFlowletPropertyDescriptors;
	}

	/**
	 * Creates the property descriptors if they have not been created yet. The
	 * following descriptor is added:<br>
	 * ReadOnlyTextPropertyDescriptor for the task's description
	 */
	private void createPropertyDescriptors() {

		List temp1 = AgentEditor.getExampleList(parentClassName
				+ " exampleListTask1");
		if (temp1 == null) {
			temp1 = AgentEditor
					.getExampleList("java.lang.Object exampleListTask1");
		}
		if (temp1 == null) {
			exampleList1 = new String[] { "" };
		} else {
			exampleList1 = (String[]) temp1.toArray(new String[0]);
		}

		List temp2 = AgentEditor.getExampleList(parentClassName
				+ " exampleListTask2");
		if (temp2 == null) {
			temp2 = AgentEditor
					.getExampleList("java.lang.Object exampleListTask2");
		}
		if (temp2 == null) {
			exampleList2 = new String[] { "" };
		} else {
			exampleList2 = (String[]) temp2.toArray(new String[0]);
		}

		this.example2 = exampleList2[0];

		if (decisionFlowletPropertyDescriptors != null)
			return;

		decisionFlowletPropertyDescriptors = new HashSet(super
				.getPropertyDescriptorsCollection());

		decisionFlowletPropertyDescriptors
				.add(new TextPropertyDescriptor(
						PROP_TASK_COMMENT,
						AgentBuilderPlugin
								.getResourceString("PropertyDescriptor_TaskStep.comment")));

		List temp3 = AgentEditor.getExampleList(parentClassName
				+ " exampleListTaskWizard");
		if (temp3 == null) {
			temp3 = AgentEditor
					.getExampleList("java.lang.Object exampleListTaskWizard");
		}

		decisionFlowletPropertyDescriptors
				.add(new EditableComboBoxPropertyDescriptor(
						PROP_TASK_COMMAND1,
						AgentBuilderPlugin
								.getResourceString("PropertyDescriptor_TaskStep_.taskCommand1"),
						this.commandList, (ArrayList<NewCodeWizardEntry>) temp3));
		decisionFlowletPropertyDescriptors
				.add(new EditableComboBoxPropertyDescriptor(
						PROP_TASK_COMMAND2,
						AgentBuilderPlugin
								.getResourceString("PropertyDescriptor_TaskStep_.taskCommand2"),
						this.commandList, (ArrayList<NewCodeWizardEntry>) temp3));
		decisionFlowletPropertyDescriptors
				.add(new EditableComboBoxPropertyDescriptor(
						PROP_TASK_COMMAND3,
						AgentBuilderPlugin
								.getResourceString("PropertyDescriptor_TaskStep_.taskCommand3"),
						this.commandList, (ArrayList<NewCodeWizardEntry>) temp3));
		decisionFlowletPropertyDescriptors
				.add(new EditableComboBoxPropertyDescriptor(
						PROP_TASK_COMMAND4,
						AgentBuilderPlugin
								.getResourceString("PropertyDescriptor_TaskStep_.taskCommand4"),
						this.commandList, (ArrayList<NewCodeWizardEntry>) temp3));
		decisionFlowletPropertyDescriptors
				.add(new EditableComboBoxPropertyDescriptor(
						PROP_TASK_COMMAND5,
						AgentBuilderPlugin
								.getResourceString("PropertyDescriptor_TaskStep_.taskCommand5"),
						this.commandList, (ArrayList<NewCodeWizardEntry>) temp3));
	}

	/**
	 * Returns an Object which represents the appropriate value for the property
	 * name supplied. In case of label property the request will be routed to
	 * the label modelPart.
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java.lang.Object)
	 * @param propName
	 *            Name of the property for which the the values are needed.
	 * @return Object which is the value of the property.
	 */
	public Object getPropertyValue(Object propName) {

		if (PROP_TASK_COMMENT.equals(propName)) {
			return getComment();
		} else if (PROP_TASK_EXAMPLE1.equals(propName)) {
			return getExample1();
		} else if (PROP_TASK_EXAMPLE2.equals(propName)) {
			return getExample2();
		} else if (PROP_TASK_COMMAND1.equals(propName)) {
			return getTaskCommand1();
		} else if (PROP_TASK_COMMAND2.equals(propName)) {
			return getTaskCommand2();
		} else if (PROP_TASK_COMMAND3.equals(propName)) {
			return getTaskCommand3();
		} else if (PROP_TASK_COMMAND4.equals(propName)) {
			return getTaskCommand4();
		} else if (PROP_TASK_COMMAND5.equals(propName)) {
			return getTaskCommand5();
		} else {
			return super.getPropertyValue(propName);
		}
	}

	/**
	 * Sets the value of a given property with the value supplied. Also fires a
	 * property change if necessary.
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(java.lang.Object,
	 *      java.lang.Object)
	 * @param id
	 *            Name of the parameter to be changed.
	 * @param value
	 *            Value to be set to the given parameter.
	 */
	public void setPropertyValue(Object id, Object value) {

		if (PROP_TASK_COMMENT.equals(id)) {
			setComment((String) value);
		} else if (PROP_TASK_EXAMPLE1.equals(id)) {
			setExample1((Integer) value);
		} else if (PROP_TASK_EXAMPLE2.equals(id)) {
			setExample2((String) value);
		} else if (PROP_TASK_COMMAND1.equals(id)) {
			setTaskCommand1((String) value);
		} else if (PROP_TASK_COMMAND2.equals(id)) {
			setTaskCommand2((String) value);
		} else if (PROP_TASK_COMMAND3.equals(id)) {
			setTaskCommand3((String) value);
		} else if (PROP_TASK_COMMAND4.equals(id)) {
			setTaskCommand4((String) value);
		} else if (PROP_TASK_COMMAND5.equals(id)) {
			setTaskCommand5((String) value);
		} else {
			super.setPropertyValue(id, value);
		}

	}

	public Integer getExample1() {
		return example1;
	}

	public void setExample1(Integer example) {
		this.example1 = example;
		if (((0 <= example) && (example < exampleList1.length))
				&& ((0 <= example) && (example < exampleList2.length))) {
			this.example2 = exampleList2[example];
			firePropertyChange(PROP_TASK_EXAMPLE2, null, this.taskCommand1);
			firePropertyChange(PROP_TASK_EXAMPLE2, null, this.taskCommand2);
			firePropertyChange(PROP_TASK_EXAMPLE2, null, this.taskCommand3);
			firePropertyChange(PROP_TASK_EXAMPLE2, null, this.taskCommand4);
			firePropertyChange(PROP_TASK_EXAMPLE2, null, this.taskCommand5);
			if (!DepthCounter.isSet(this.taskCommand1)) {
				this.taskCommand1 = exampleList2[example];
				firePropertyChange(PROP_TASK_COMMAND1, null, this.taskCommand1);
			} else {
				if (!DepthCounter.isSet(this.taskCommand2)) {
					this.taskCommand2 = exampleList2[example];
					firePropertyChange(PROP_TASK_COMMAND2, null,
							this.taskCommand2);
				} else {
					if (!DepthCounter.isSet(this.taskCommand3)) {
						this.taskCommand3 = exampleList2[example];
						firePropertyChange(PROP_TASK_COMMAND3, null,
								this.taskCommand3);
					} else {
						if (!DepthCounter.isSet(this.taskCommand4)) {
							this.taskCommand4 = exampleList2[example];
							firePropertyChange(PROP_TASK_COMMAND4, null,
									this.taskCommand4);
						} else {
							if (!DepthCounter.isSet(this.taskCommand5)) {
								this.taskCommand5 = exampleList2[example];
								firePropertyChange(PROP_TASK_COMMAND5, null,
										this.taskCommand5);
							}
						}
					}
				}
			}
		}
	}

	public String getExample2() {
		return example2;
	}

	public void setExample2(String example2) {
		this.example2 = example2;
	}

	public String getTaskCommand1() {
		return taskCommand1;
	}

	public void setTaskCommand1(String taskCommand1) {
		this.taskCommand1 = taskCommand1;
	}

	public String getTaskCommand2() {
		return taskCommand2;
	}

	public void setTaskCommand2(String taskCommand2) {
		this.taskCommand2 = taskCommand2;
	}

	public String getTaskCommand3() {
		return taskCommand3;
	}

	public void setTaskCommand3(String taskCommand3) {
		this.taskCommand3 = taskCommand3;
	}

	public String getTaskCommand4() {
		return taskCommand4;
	}

	public void setTaskCommand4(String taskCommand4) {
		this.taskCommand4 = taskCommand4;
	}

	public String getTaskCommand5() {
		return taskCommand5;
	}

	public void setTaskCommand5(String taskCommand5) {
		this.taskCommand5 = taskCommand5;
	}

}
