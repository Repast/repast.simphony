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

import org.eclipse.gef.commands.Command;

import repast.simphony.agents.designer.core.AgentBuilderPlugin;
import repast.simphony.agents.designer.model.AgentPropertyorStepModelPart;
import repast.simphony.agents.designer.model.TransitionModelPart;

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
public class ConnectionCommand extends Command {

	private TransitionModelPart transition;

	private AgentPropertyorStepModelPart source;
	private AgentPropertyorStepModelPart target;
	private AgentPropertyorStepModelPart oldSource;
	private AgentPropertyorStepModelPart oldTarget;

	private int sourceAnchorIndex;
	private int targetAnchorIndex;
	private int oldSourceAnchorIndex;
	private int oldTargetAnchorIndex;

	/**
	 * Constructor that creates a ConnectionCommand
	 * 
	 */
	public ConnectionCommand() {
		super(AgentBuilderPlugin.getResourceString("Command_Connection.label"));
	}

	/**
	 * TODO
	 * 
	 * @param label
	 */
	public ConnectionCommand(String label) {
		super(label);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	@Override
	public boolean canExecute() {
		return true;
	}

	/**
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		if (source != null) {
			transition.detachSource();
			transition.setSource(source);
			transition.setSourceAnchorIndex(sourceAnchorIndex);
			transition.attachSource();
		}
		if (target != null) {
			transition.detachTarget();
			transition.setTarget(target);
			transition.setTargetAnchorIndex(targetAnchorIndex);
			transition.attachTarget();
		}
		if (source == null && target == null) {
			transition.detachSource();
			transition.detachTarget();
			transition.setTarget(null);
			transition.setSource(null);
		}
	}

	/**
	 * @see org.eclipse.gef.commands.Command#redo()
	 */
	@Override
	public void redo() {
		execute();
	}

	/**
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
		source = transition.getSource();
		target = transition.getTarget();
		sourceAnchorIndex = transition.getSourceAnchorIndex();
		targetAnchorIndex = transition.getTargetAnchorIndex();

		transition.detachSource();
		transition.detachTarget();

		transition.setSource(oldSource);
		transition.setTarget(oldTarget);
		transition.setSourceAnchorIndex(oldSourceAnchorIndex);
		transition.setTargetAnchorIndex(oldTargetAnchorIndex);

		transition.attachSource();
		transition.attachTarget();
	}

	/**
	 * Returns the transition model part
	 * 
	 * @return the transition model part
	 */
	public TransitionModelPart getTransition() {
		return transition;
	}

	/**
	 * TODO
	 * 
	 * @param transition
	 */
	public void setTransition(TransitionModelPart t) {
		transition = t;
		oldSource = t.getSource();
		oldTarget = t.getTarget();
		oldSourceAnchorIndex = t.getSourceAnchorIndex();
		oldTargetAnchorIndex = t.getTargetAnchorIndex();
	}

	/**
	 * TODO
	 * 
	 * @return
	 */
	public AgentPropertyorStepModelPart getSource() {
		return source;
	}

	/**
	 * TODO
	 * 
	 * @param source
	 */
	public void setSource(AgentPropertyorStepModelPart source) {
		this.source = source;
	}

	/**
	 * TODO
	 * 
	 * @return
	 */
	public AgentPropertyorStepModelPart getTarget() {
		return target;
	}

	/**
	 * TODO
	 * 
	 * @param target
	 */
	public void setTarget(AgentPropertyorStepModelPart target) {
		this.target = target;
	}

	/**
	 * TODO
	 * 
	 * @return
	 */
	public int getSourceAnchorIndex() {
		return sourceAnchorIndex;
	}

	/**
	 * TODO
	 * 
	 * @param sourceAnchorIndex
	 */
	public void setSourceAnchorIndex(int sourceAnchorIndex) {
		this.sourceAnchorIndex = sourceAnchorIndex;
	}

	/**
	 * TODO
	 * 
	 * @return
	 */
	public int getTargetAnchorIndex() {
		return targetAnchorIndex;
	}

	/**
	 * TODO
	 * 
	 * @param targetAnchorIndex
	 */
	public void setTargetAnchorIndex(int targetAnchorIndex) {
		this.targetAnchorIndex = targetAnchorIndex;
	}

}
