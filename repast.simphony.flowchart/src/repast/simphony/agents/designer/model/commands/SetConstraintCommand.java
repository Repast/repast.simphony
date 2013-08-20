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

package repast.simphony.agents.designer.model.commands;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import repast.simphony.agents.designer.core.AgentBuilderPlugin;
import repast.simphony.agents.designer.model.AgentDiagramElementModelPart;
import repast.simphony.agents.designer.model.EndStepModelPart;
import repast.simphony.agents.designer.model.JoinStepModelPart;
import repast.simphony.agents.designer.ui.editors.AgentEditor;

/**
 * @author agreif (Adapted by Michael J. North for Use in Repast Simphony from
 *         Alexander Greif’s Flow4J-Eclipse
 *         (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks
 *         to the Original Author)
 * 
 *         TODO
 * 
 * 
 * 
 */
public class SetConstraintCommand extends Command {

	private Point newPos;
	// private Dimension newSize;
	private Point oldPos;
	// private Dimension oldSize;
	private AgentDiagramElementModelPart model;

	private boolean enableCommand = true;

	public boolean isEnableCommand() {
		return enableCommand;
	}

	public void setEnableCommand(boolean enableCommand) {
		this.enableCommand = enableCommand;
	}

	/**
	 * TODO
	 * 
	 */
	public SetConstraintCommand() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * TODO
	 * 
	 * @param label
	 */
	public SetConstraintCommand(String label) {
		super(label);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		if (isEnableCommand()) {
			// oldSize = model.getSize();
			oldPos = model.getLocation();
			if (model instanceof JoinStepModelPart) {
				model.setLocation(AgentEditor
						.snapPoint(newPos, new Point(5, 5)));
			} else if (model instanceof EndStepModelPart) {
				model.setLocation(AgentEditor
						.snapPoint(newPos, new Point(5, 5)));
			} else {
				model.setLocation(AgentEditor.snapPoint(newPos));
			}
			// model.setSize(newSize);
		}
	}

	/**
	 * @see org.eclipse.gef.commands.Command#getLabel()
	 */
	@Override
	public String getLabel() {
		// if (oldSize.equals(newSize))
		return AgentBuilderPlugin
				.getResourceString("Command_SetLocation.labelLocation");
		// return
		//AgentBuilderPlugin.getResourceString("Command_SetLocation.labelResize"
		// );
	}

	/**
	 * @see org.eclipse.gef.commands.Command#redo()
	 */
	@Override
	public void redo() {
		// model.setSize(newSize);
		if (model instanceof JoinStepModelPart) {
			model.setLocation(AgentEditor.snapPoint(newPos, new Point(5, 5)));
		} else if (model instanceof EndStepModelPart) {
			model.setLocation(AgentEditor.snapPoint(newPos, new Point(5, 5)));
		} else {
			model.setLocation(AgentEditor.snapPoint(newPos));
		}
	}

	/**
	 * TODO
	 * 
	 * @param r
	 */
	public void setLocation(Rectangle r) {
		setLocation(r.getLocation());
		// setSize(r.getSize());
	}

	/**
	 * TODO
	 * 
	 * @param p
	 */
	public void setLocation(Point p) {
		newPos = p;
	}

	/**
	 * TODO
	 * 
	 * @param model
	 */
	public void setPart(AgentDiagramElementModelPart part) {
		this.model = part;
	}

	/**
	 * TODO
	 * 
	 * @param p
	 */
	/*
	 * public void setSize(Dimension p) { newSize = p; }
	 */

	/**
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
		// model.setSize(oldSize);
		model.setLocation(oldPos);
	}

}
