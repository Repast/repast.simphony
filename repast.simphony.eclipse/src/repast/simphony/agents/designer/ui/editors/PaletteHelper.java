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

package repast.simphony.agents.designer.ui.editors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteSeparator;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.jface.resource.ImageDescriptor;

import repast.simphony.agents.designer.core.AgentBuilderPlugin;
import repast.simphony.agents.designer.model.AgentPropertyModelPart;
import repast.simphony.agents.designer.model.BehaviorStepModelPart;
import repast.simphony.agents.designer.model.DecisionStepModelPart;
import repast.simphony.agents.designer.model.EndStepModelPart;
import repast.simphony.agents.designer.model.JoinStepModelPart;
import repast.simphony.agents.designer.model.TaskStepModelPart;

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
@SuppressWarnings("unchecked")
public class PaletteHelper {

	/**
	 * Returns the newly created palette's root.
	 * 
	 * @return the palette's root.
	 */
	static PaletteRoot createPalette() {
		PaletteRoot flowPalette = new PaletteRoot();
		flowPalette.addAll(createFlowPaletteCategories(flowPalette));

		return flowPalette;
	}

	/**
	 * TODO
	 * 
	 * @param root
	 * @return
	 */
	static private List createFlowPaletteCategories(PaletteRoot root) {
		List categories = new ArrayList();

		categories.add(createControlGroup(root));
		categories.add(createFlowletsDrawer());

		return categories;
	}

	/**
	 * Creates the control tools.
	 * 
	 * @param root
	 * @return the control group
	 */
	static private PaletteContainer createControlGroup(PaletteRoot root) {
		PaletteGroup controlGroup = new PaletteGroup(
				AgentBuilderPlugin
						.getResourceString("AgentEditor_Palette_Category_ControlGroup.label"));//$NON-NLS-1$

		List entries = new ArrayList();

		ToolEntry tool = new AgentBuilderSelectionToolEntry();
		entries.add(tool);
		root.setDefaultEntry(tool);

		tool = new MarqueeToolEntry();
		entries.add(tool);

		PaletteSeparator sep = new PaletteSeparator(
				"repast.simphony.agents.designer.ui.editors.sep"); //$NON-NLS-1$
		sep
				.setUserModificationPermission(PaletteEntry.PERMISSION_NO_MODIFICATION);
		entries.add(sep);

		tool = new AgentBuilderConnectionCreationToolEntry(
				AgentBuilderPlugin
						.getResourceString("AgentEditor_Palette_Tool_CreationTool_Connection.label"),//$NON-NLS-1$
				AgentBuilderPlugin
						.getResourceString("AgentEditor_Palette_Tool_CreationTool_Connection.description"),//$NON-NLS-1$
				null, ImageDescriptor.createFromFile(
						BehaviorStepModelPart.class, "icons/connection16.gif"),//$NON-NLS-1$
				ImageDescriptor.createFromFile(BehaviorStepModelPart.class,
						"icons/connection24.gif")//$NON-NLS-1$
		);
		entries.add(tool);

		controlGroup.addAll(entries);
		return controlGroup;
	}

	/**
	 * Creates the Flowlets drawer in the palette
	 * 
	 * @return the filled drawer container
	 */
	static private PaletteContainer createFlowletsDrawer() {
		PaletteDrawer drawer = new PaletteDrawer(AgentBuilderPlugin
				.getResourceString("AgentEditor_Palette_Category_Steps.label"),//$NON-NLS-1$
				ImageDescriptor.createFromFile(BehaviorStepModelPart.class,
						"icons/comp.gif"));//$NON-NLS-1$

		List entries = new ArrayList();
		CombinedTemplateCreationEntry combined;

		combined = new CombinedTemplateCreationEntry(
				AgentBuilderPlugin
						.getResourceString("AgentEditor_Palette_Tool_CreationTool_AgentProperty.label"),//$NON-NLS-1$
				AgentBuilderPlugin
						.getResourceString("AgentEditor_Palette_Tool_CreationTool_AgentProperty.description"),//$NON-NLS-1$
				AgentPropertyModelPart.class, new SimpleFactory(
						AgentPropertyModelPart.class), ImageDescriptor
						.createFromFile(AgentPropertyModelPart.class,
								"icons/agentproperty16.gif"),//$NON-NLS-1$
				ImageDescriptor.createFromFile(AgentPropertyModelPart.class,
						"icons/agentproperty24.gif")//$NON-NLS-1$
		);
		entries.add(combined);

		combined = new CombinedTemplateCreationEntry(
				AgentBuilderPlugin
						.getResourceString("AgentEditor_Palette_Tool_CreationTool_BehaviorStep.label"),//$NON-NLS-1$
				AgentBuilderPlugin
						.getResourceString("AgentEditor_Palette_Tool_CreationTool_BehaviorStep.description"),//$NON-NLS-1$
				BehaviorStepModelPart.class, new SimpleFactory(
						BehaviorStepModelPart.class), ImageDescriptor
						.createFromFile(BehaviorStepModelPart.class,
								"icons/behaviorstep16.gif"),//$NON-NLS-1$
				ImageDescriptor.createFromFile(BehaviorStepModelPart.class,
						"icons/behaviorstep24.gif")//$NON-NLS-1$
		);
		entries.add(combined);

		combined = new CombinedTemplateCreationEntry(
				AgentBuilderPlugin
						.getResourceString("AgentEditor_Palette_Tool_CreationTool_TaskStep.label"),//$NON-NLS-1$
				AgentBuilderPlugin
						.getResourceString("AgentEditor_Palette_Tool_CreationTool_TaskStep.description"),//$NON-NLS-1$
				TaskStepModelPart.class, new SimpleFactory(
						TaskStepModelPart.class), ImageDescriptor
						.createFromFile(TaskStepModelPart.class,
								"icons/taskstep16.gif"),//$NON-NLS-1$
				ImageDescriptor.createFromFile(TaskStepModelPart.class,
						"icons/taskstep24.gif")//$NON-NLS-1$
		);
		entries.add(combined);

		combined = new CombinedTemplateCreationEntry(
				AgentBuilderPlugin
						.getResourceString("AgentEditor_Palette_Tool_CreationTool_DecisionStep.label"),//$NON-NLS-1$
				AgentBuilderPlugin
						.getResourceString("AgentEditor_Palette_Tool_CreationTool_DecisionStep.description"),//$NON-NLS-1$
				DecisionStepModelPart.class, new SimpleFactory(
						DecisionStepModelPart.class), ImageDescriptor
						.createFromFile(DecisionStepModelPart.class,
								"icons/decisionstep16.gif"),//$NON-NLS-1$
				ImageDescriptor.createFromFile(DecisionStepModelPart.class,
						"icons/decisionstep24.gif")//$NON-NLS-1$
		);
		entries.add(combined);

		combined = new CombinedTemplateCreationEntry(
				AgentBuilderPlugin
						.getResourceString("AgentEditor_Palette_Tool_CreationTool_JoinStep.label"),//$NON-NLS-1$
				AgentBuilderPlugin
						.getResourceString("AgentEditor_Palette_Tool_CreationTool_JoinStep.description"),//$NON-NLS-1$
				JoinStepModelPart.class, new SimpleFactory(
						JoinStepModelPart.class), ImageDescriptor
						.createFromFile(JoinStepModelPart.class,
								"icons/joinstep16.gif"),//$NON-NLS-1$
				ImageDescriptor.createFromFile(JoinStepModelPart.class,
						"icons/joinstep24.gif")//$NON-NLS-1$
		);
		entries.add(combined);

		combined = new CombinedTemplateCreationEntry(
				AgentBuilderPlugin
						.getResourceString("AgentEditor_Palette_Tool_CreationTool_LoopStep.label"),//$NON-NLS-1$
				AgentBuilderPlugin
						.getResourceString("AgentEditor_Palette_Tool_CreationTool_LoopStep.description"),//$NON-NLS-1$
				DecisionStepModelPart.class, new SimpleFactory(
						DecisionStepModelPart.class) {

					@Override
					public Object getNewObject() {
						// TODO Auto-generated method stub
						Object obj = super.getNewObject();
						if (obj instanceof DecisionStepModelPart) {
							((DecisionStepModelPart) obj)
									.setBranchType(DecisionStepModelPart.PROP_DECISION_BRANCH_TYPE_WHILE);
							((DecisionStepModelPart) obj)
									.setComment("This is a loop.");
						}
						return obj;
					}

				}

				, ImageDescriptor.createFromFile(DecisionStepModelPart.class,
						"icons/loopstep16.gif"),//$NON-NLS-1$
				ImageDescriptor.createFromFile(DecisionStepModelPart.class,
						"icons/loopstep24.gif")//$NON-NLS-1$
		);
		entries.add(combined);

		combined = new CombinedTemplateCreationEntry(
				AgentBuilderPlugin
						.getResourceString("AgentEditor_Palette_Tool_CreationTool_EndStep.label"),//$NON-NLS-1$
				AgentBuilderPlugin
						.getResourceString("AgentEditor_Palette_Tool_CreationTool_EndStep.description"),//$NON-NLS-1$
				EndStepModelPart.class, new SimpleFactory(
						EndStepModelPart.class), ImageDescriptor
						.createFromFile(EndStepModelPart.class,
								"icons/endstep16.gif"),//$NON-NLS-1$
				ImageDescriptor.createFromFile(EndStepModelPart.class,
						"icons/endstep24.gif")//$NON-NLS-1$
		);
		entries.add(combined);

		entries.add(new PaletteSeparator());
		drawer.addAll(entries);

		return drawer;
	}

}
