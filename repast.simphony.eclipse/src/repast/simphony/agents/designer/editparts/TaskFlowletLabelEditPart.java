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

package repast.simphony.agents.designer.editparts;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.Label;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveListener;
import org.eclipse.ui.IWorkbenchPage;

import repast.simphony.agents.designer.core.AgentBuilderPlugin;
import repast.simphony.agents.designer.figures.PropertyOrStepLabelFigure;
import repast.simphony.agents.designer.figures.TaskStepToolTipFigure;
import repast.simphony.agents.designer.model.AgentPropertyorStepLabelModelPart;
import repast.simphony.agents.designer.model.TaskStepModelPart;

/**
 * Edit part for the TaskFlowletLabel. Checks for the executable class. If it's
 * found then the task's name is displayed in black, otherwise the classname is
 * displayed in red.
 * 
 * @author alex
 */
public class TaskFlowletLabelEditPart extends AgentPropertyorStepLabelEditPart
		implements IPerspectiveListener {

	static private final Color TASK_CLASS_NOT_FOUND_COLOR = new Color(null,
			255, 0, 0);
	// new Color(null, 255, 0, 0);
	static private final Color TASK_CLASS_FOUND_COLOR = new Color(null, 0, 0, 0);

	// private PropertyOrStepLabelFigure flowletLabelFigure;

	/**
	 * Activates the edit part and updates the flowlet's tooltip's classname
	 * property. This will be the initial value of the tooltip.
	 * 
	 * @see org.eclipse.gef.EditPart#activate()
	 */
	@Override
	public void activate() {
		if (isActive())
			return;
		super.activate();
		// update flowlet's tooltip
		TaskStepEditPart taskFlowletEditPart = getTaskFlowletEditPart();
		IAgentBuilderToolTip toolTip = taskFlowletEditPart
				.getFlowDiagramElementToolTip();
		String className = ((AgentPropertyorStepLabelModelPart) getModel())
				.getText();
		toolTip.update(TaskStepToolTipFigure.TOOLTIP_PROP_CLASSNAME, className);

		// task label may contain parameterized property references

		taskFlowletEditPart.getTaskFlowlet().addPropertyChangeListener(this);
	}

	/**
	 * Convenience method to return the Task
	 * AgentPropertyorStepWithLabelEditPart.
	 * 
	 * @return the Task AgentPropertyorStepWithLabelEditPart.
	 */
	private TaskStepEditPart getTaskFlowletEditPart() {
		return (TaskStepEditPart) getFlowletWithLabelEditPart();
	}

	/**
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	/*
	 * protected IFigure createFigure() { if (getModel() == null) return null;
	 * 
	 * PropertyOrStepLabelFigure flowletLabelFigure =
	 * (PropertyOrStepLabelFigure)
	 * AgentBuilderFigureFactory.createFlowletLabelFigure();
	 * adjustFlowletLabelFigure(flowletLabelFigure); return flowletLabelFigure; }
	 */

	/**
	 * Returns the fully qualified class name of the Task Flowlet. It is the
	 * text property of the Flowlet Label Modelpart
	 * 
	 * @return the fully qualified class name of the Task Flowlet.
	 */
	private String getTaskClassName() {
		return ((AgentPropertyorStepLabelModelPart) getModel()).getText();
	}

	/**
	 * Reacts on the change of the label's text. Also updates the flowlet's
	 * tooltip.
	 * 
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		String propName = event.getPropertyName();
		if (AgentPropertyorStepLabelModelPart.PROP_LABEL.equals(propName)) {
			String taskClassName = (String) event.getNewValue();

			adjustFlowletLabelFigure((PropertyOrStepLabelFigure) getFigure());
			// update TaskStepEditPart's tooltip
			TaskStepEditPart taskFlowletEditPart = getTaskFlowletEditPart();
			IAgentBuilderToolTip toolTip = taskFlowletEditPart
					.getFlowDiagramElementToolTip();
			toolTip.update(TaskStepToolTipFigure.TOOLTIP_PROP_CLASSNAME,
					taskClassName);
		} else
			super.propertyChange(event);
	}

	/**
	 * Sets the label's text and color. If the editor is not open yet, then a
	 * listener for the open window action is registered and the label is
	 * updated later.
	 * 
	 * @param propertyOrStepLabelFigure
	 *            the figure of the label
	 */
	@Override
	protected void adjustFlowletLabelFigure(
			PropertyOrStepLabelFigure propertyOrStepLabelFigure) {
		String labelText = getTaskClassName();
		Color labelColor = TASK_CLASS_NOT_FOUND_COLOR;

		try {
			/*
			 * List runtimeLibs = new ArrayList(); IJavaProject javaProject =
			 * AgentBuilderPlugin.getJavaProject(((AgentDiagramEditPart)
			 * getParent()).getFlowFile().getProject()); List classpathList =
			 * new ArrayList();
			 * AgentBuilderPlugin.getResolvedClasspath(AgentBuilderPlugin.getProject(javaProject),
			 * classpathList); for (Iterator iter = classpathList.iterator();
			 * iter.hasNext();) { IPath path = (IPath) iter.next();
			 * runtimeLibs.add(path.toFile().toURL()); } ITaskStep taskInstance =
			 * getTaskFlowletEditPart().getTaskFlowlet().getTaskFlowletInstance(getTaskClassName(),
			 * runtimeLibs);
			 */

			// IJavaProject javaProject =
			// AgentBuilderPlugin.getJavaProject(((AgentDiagramEditPart)
			// getParent()).getFlowFile().getProject());
			// ITaskStep taskInstance =
			// AgentBuilderPlugin.getTaskFlowletInstance(getTaskClassName(),
			// javaProject);
			TaskStepModelPart jmp = getTaskFlowletEditPart().getTaskFlowlet();
			labelText = (String) jmp
					.getPropertyValue(AgentPropertyorStepLabelModelPart.PROP_LABEL);

			// set the label's text to the Task Flowlet's name
			if (labelText != null)
				labelColor = TASK_CLASS_FOUND_COLOR;

			/*
			 * IEditorPart activeEditor = AgentBuilderPlugin.getActiveEditor();
			 * if (activeEditor != null && (activeEditor instanceof AgentEditor) &&
			 * ((AgentEditor) activeEditor).getFlowFile().equals(
			 * ((AgentDiagramEditPart) getParent()).getFlowFile())) {
			 * 
			 * ITaskStep taskInstance =
			 * getTaskFlowletEditPart().getTaskFlowlet().getTaskFlowletInstance(getTaskClassName()); //
			 * set the label's text to the Task Flowlet's name labelText =
			 * Util.replaceProperties(taskInstance.getName(),getTaskFlowletEditPart().getTaskFlowlet().getTaskProperties());
			 * labelColor = TASK_CLASS_FOUND_COLOR; } else { // editor is not
			 * open yet, add this as listener and set the labels later
			 * AgentBuilderPlugin.getActiveWorkbenchWindow().addPerspectiveListener(
			 * this); }
			 */
		} catch (Error e) {
			// can happen if a task, that has compilation errors,
			// is dragged in the flow editor
		} catch (Throwable e) {
			AgentBuilderPlugin.log(e);
		}
		// set the figure's text
		propertyOrStepLabelFigure.setText(labelText);
		propertyOrStepLabelFigure.setForegroundColor(labelColor);

	}

	/**
	 * Does nothing currently.
	 * 
	 * @see org.eclipse.ui.IPerspectiveListener#perspectiveActivated(org.eclipse.ui.IWorkbenchPage,
	 *      org.eclipse.ui.IPerspectiveDescriptor)
	 */
	public void perspectiveActivated(IWorkbenchPage page,
			IPerspectiveDescriptor perspective) {
		// TODO Auto-generated method stub
	}

	/**
	 * Sets the color and text if the editor is opened.
	 * 
	 * @see org.eclipse.ui.IPerspectiveListener#perspectiveChanged(org.eclipse.ui.IWorkbenchPage,
	 *      org.eclipse.ui.IPerspectiveDescriptor, java.lang.String)
	 */
	public void perspectiveChanged(IWorkbenchPage page,
			IPerspectiveDescriptor perspective, String changeId) {
		if (IWorkbenchPage.CHANGE_EDITOR_OPEN.equals(changeId)
				|| IWorkbenchPage.CHANGE_ACTION_SET_SHOW.equals(changeId)) {
			AgentBuilderPlugin.getActiveWorkbenchWindow()
					.removePerspectiveListener(this);
			adjustFlowletLabelFigure((PropertyOrStepLabelFigure) getFigure());
		}
	}

	/**
	 * Returns the FlowletLabelEditManager for this type of EditPart. Sets the
	 * <code>forceDirty</code> flag to <code>true</code> because the edit
	 * text differs from the display text.
	 * 
	 * @return the FlowletLabelEditManager for this type of EditPart.
	 * @see repast.simphony.agents.designer.editparts.AgentPropertyorStepLabelEditPart#getFlowletLabelEditManager()
	 */
	@Override
	protected FlowletLabelEditManager getFlowletLabelEditManager() {
		return new AgentPropertyorStepLabelEditPart.FlowletLabelEditManager(
				this, new LabelCellEditorLocator((Label) getFigure()), true);
	}

	/**
	 * The direct edit manager for the flowlet's label. The extra feature here
	 * is that the flowlet's name should be displayed if it was displayed before
	 * and no change performed while direct editing.
	 * 
	 * @author alex / private class TaskFlowletLabelEditManager extends
	 *         AgentPropertyorStepLabelEditPart.FlowletLabelEditManager { / **
	 *         we retreive the original text from here * / private
	 *         AgentPropertyorStepLabelEditPart.LabelCellEditorLocator locator; / **
	 *         Contstructor of the inner class
	 * @param source
	 *            the source edit part
	 * @param locator
	 *            the cell editor / public TaskFlowletLabelEditManager(
	 *            GraphicalEditPart source, LabelCellEditorLocator locator) {
	 *            super(source, locator); this.locator = locator; } / ** Sets
	 *            the flowlets name if the classname did not change.
	 * @see org.eclipse.gef.tools.DirectEditManager#commit() / protected void
	 *      commit() { if (!isDirty()) { Text text = (Text)
	 *      getCellEditor().getControl(); // set to the original Text
	 *      getCellEditor().setValue(locator.getLabel().getText());
	 *      setDirty(true); } super.commit(); } }
	 */
}
