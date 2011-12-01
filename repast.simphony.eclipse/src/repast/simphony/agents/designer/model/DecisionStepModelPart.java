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

package repast.simphony.agents.designer.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import repast.simphony.agents.AgentBuilderConsts;
import repast.simphony.agents.designer.core.AgentBuilderPlugin;
import repast.simphony.agents.designer.model.propertydescriptors.EditableComboBoxPropertyDescriptor;
import repast.simphony.agents.designer.model.propertydescriptors.ExtendedComboBoxPropertyDescriptor;
import repast.simphony.agents.designer.ui.editors.AgentEditor;
import repast.simphony.agents.designer.ui.wizards.NewCodeWizardEntry;
import repast.simphony.agents.model.codegen.DepthCounter;

/**
 * Model Part for Decision Flowlets.
 * 
 * @author greifa (Adapted by Michael J. North for Use in Repast Simphony from
 *         Alexander Greif’s Flow4J-Eclipse
 *         (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks
 *         to the Original Author)
 */
@SuppressWarnings("unchecked")
public class DecisionStepModelPart extends
		AgentPropertyorStepWithLabelModelPart {

	private static final String THIS_IS_AN_AGENT_DECISION = "This is an agent decision.";
	public static final String PROP_DECISION_COMMENT = "decision_comment"; //$NON-NLS-1$
	public static final String PROP_DECISION_EXAMPLE = "decision_example"; //$NON-NLS-1$
	public static final String PROP_DECISION_CRITERIA = "decision_criteria"; //$NON-NLS-1$
	public static final String PROP_DECISION_BRANCH_TYPE = "decision_branch_type"; //$NON-NLS-1$

	private HashSet decisionFlowletPropertyDescriptors = null;
	private String comment = DecisionStepModelPart.THIS_IS_AN_AGENT_DECISION;
	private String criteria = "";
	private Integer example = new Integer(0);
	private String[] exampleListIf;
	private String[] exampleListLoop;
	private String[] exampleList;
	private String[] criteriaList = {
			EditableComboBoxPropertyDescriptor.EDIT_USING_TEXT_BOX_DIALOG };
	private ArrayList<NewCodeWizardEntry> exampleListWizard;
	private ArrayList<NewCodeWizardEntry> exampleListIfWizard;
	private ArrayList<NewCodeWizardEntry> exampleListLoopWizard;

	public static final String PROP_DECISION_BRANCH_TYPE_IF = "if"; //$NON-NLS-1$
	public static final String PROP_DECISION_BRANCH_TYPE_WHILE = "while"; //$NON-NLS-1$
	private String branchType = DecisionStepModelPart.PROP_DECISION_BRANCH_TYPE_IF;

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	/**
	 * @see repast.simphony.agents.designer.model.AgentPropertyorStepModelPart#getType()
	 */
	@Override
	public String getType() {
		return AgentBuilderConsts.DECISION;
	}

	/**
	 * Returns the name of the resource entry that contains the property name
	 * 
	 * @return key in the resources file
	 * @see repast.simphony.agents.designer.model.AgentPropertyorStepWithLabelModelPart#getLabelPropertyNameResource()
	 */
	@Override
	protected String getLabelPropertyNameResource() {
		return "PropertyDescriptor_PropertyOrStep.label";
	}

	/**
	 * Collects all property descriptors. Those of the superclass and those of
	 * this class
	 * 
	 * @return Collection with all Property descriptors
	 */
	@Override
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
				+ " exampleListIf");
		if (temp1 == null) {
			temp1 = AgentEditor
					.getExampleList("java.lang.Object exampleListIf");
		}
		if (temp1 == null) {
			exampleListIf = new String[] { "" };
		} else {
			exampleListIf = (String[]) temp1.toArray(new String[0]);
		}

		List temp2 = AgentEditor.getExampleList(parentClassName
				+ " exampleListLoop");
		if (temp2 == null) {
			temp1 = AgentEditor
					.getExampleList("java.lang.Object exampleListLoop");
		}
		if (temp2 == null) {
			exampleListLoop = new String[] { "" };
		} else {
			exampleListLoop = (String[]) temp2.toArray(new String[0]);
		}

		exampleList = exampleListIf;

		if (decisionFlowletPropertyDescriptors != null)
			return;

		decisionFlowletPropertyDescriptors = new HashSet(super
				.getPropertyDescriptorsCollection());

		decisionFlowletPropertyDescriptors
				.add(new TextPropertyDescriptor(
						PROP_DECISION_COMMENT,
						AgentBuilderPlugin
								.getResourceString("PropertyDescriptor_DecisionStep.comment")));
		if (this.getBranchType().equals(
				DecisionStepModelPart.PROP_DECISION_BRANCH_TYPE_WHILE)) {
			this.exampleList = this.exampleListLoop;
		}

		this.exampleListIfWizard = (ArrayList<NewCodeWizardEntry>) AgentEditor
				.getExampleList(parentClassName + " exampleListIfWizard");
		if (this.exampleListIfWizard == null) {
			this.exampleListIfWizard = (ArrayList<NewCodeWizardEntry>) AgentEditor
					.getExampleList("java.lang.Object exampleListIfWizard");
		}
		this.exampleListLoopWizard = (ArrayList<NewCodeWizardEntry>) AgentEditor
				.getExampleList(parentClassName + " exampleListLoopWizard");
		if (this.exampleListLoopWizard == null) {
			this.exampleListLoopWizard = (ArrayList<NewCodeWizardEntry>) AgentEditor
					.getExampleList("java.lang.Object exampleListLoopWizard");
		}
		if (this.getBranchType().equals(
				DecisionStepModelPart.PROP_DECISION_BRANCH_TYPE_WHILE)) {
			this.exampleListWizard = this.exampleListLoopWizard;
		} else {
			this.exampleListWizard = this.exampleListIfWizard;
		}
		decisionFlowletPropertyDescriptors
				.add(new EditableComboBoxPropertyDescriptor(
						PROP_DECISION_CRITERIA,
						AgentBuilderPlugin
								.getResourceString("PropertyDescriptor_DecisionStep_.criteria"),
						this.criteriaList, this.exampleListWizard));
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
	@Override
	public Object getPropertyValue(Object propName) {

		if (PROP_DECISION_COMMENT.equals(propName)) {
			return getComment();
		} else if (PROP_DECISION_EXAMPLE.equals(propName)) {
			return getExample();
		} else if (PROP_DECISION_CRITERIA.equals(propName)) {
			return getCriteria();
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
	@Override
	public void setPropertyValue(Object id, Object value) {

		if (PROP_DECISION_COMMENT.equals(id)) {
			setComment((String) value);
		} else if (PROP_DECISION_EXAMPLE.equals(id)) {
			setExample((Integer) value);
		} else if (PROP_DECISION_CRITERIA.equals(id)) {
			setCriteria((String) value);
		} else {
			super.setPropertyValue(id, value);
		}

	}

	/**
	 * Connects the Transition to this DecisionFlowlet and adjusts the value
	 * change listeners for the transitions.
	 * 
	 * @see repast.simphony.agents.designer.model.AgentPropertyorStepModelPart#connectSource(repast.simphony.agents.designer.model.TransitionModelPart)
	 */
	@Override
	public void connectSource(TransitionModelPart newTransition) {
		// if there is already one connected transition then set the value of
		// the new
		// one to the opposite of the old one
		if (getSourceTransitions().size() == 1) {
			BooleanTransitionModelPart other = (BooleanTransitionModelPart) getSourceTransitions()
					.get(0);
			((BooleanTransitionModelPart) newTransition).setValue(!other
					.getBoolValue());
		}

		super.connectSource(newTransition);

		if (getSourceTransitions().size() == 1)
			return; // its only me

		for (Iterator iter = getSourceTransitions().iterator(); iter.hasNext();) {
			BooleanTransitionModelPart booleanTransition = (BooleanTransitionModelPart) iter
					.next();
			if (newTransition != booleanTransition) {
				// set the transitions as listeners for each other
				newTransition.addPropertyChangeListener(booleanTransition);
				booleanTransition.addPropertyChangeListener(newTransition);
			}
		}
	}

	/**
	 * Disconnects the Transition from this DecisionFlowlet and removes the
	 * value change listeners from the still connected transitions.
	 * 
	 * @see repast.simphony.agents.designer.model.AgentPropertyorStepModelPart#disconnectSource(repast.simphony.agents.designer.model.TransitionModelPart)
	 */
	@Override
	public void disconnectSource(TransitionModelPart transition) {
		super.disconnectSource(transition);

		if (getSourceTransitions().size() == 1)
			return; // nobody left

		for (Iterator iter = getSourceTransitions().iterator(); iter.hasNext();) {
			BooleanTransitionModelPart booleanTransition = (BooleanTransitionModelPart) iter
					.next();
			if (transition != booleanTransition)
				booleanTransition.removePropertyChangeListener(transition);
		}
	}

	public Integer getExample() {
		return example;
	}

	public void setExample(Integer example) {
		this.example = example;
		if ((!DepthCounter.isSet(this.criteria)) && (0 <= example)
				&& (example < this.exampleList.length)) {
			this.criteria = this.exampleList[example];
			firePropertyChange(PROP_DECISION_CRITERIA, null, this.criteria);
		}
	}

	public String getBranchType() {
		return branchType;
	}

	public void setBranchType(String branchType) {
		this.branchType = branchType;
	}

}
