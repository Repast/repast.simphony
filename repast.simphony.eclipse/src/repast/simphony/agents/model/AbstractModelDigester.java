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

package repast.simphony.agents.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import repast.simphony.agents.model.bind.AgentModelBind;
import repast.simphony.agents.model.bind.AgentPropertyBind;
import repast.simphony.agents.model.bind.BehaviorStepBind;
import repast.simphony.agents.model.bind.BendpointBind;
import repast.simphony.agents.model.bind.BooleanTransitionBind;
import repast.simphony.agents.model.bind.DecisionStepBind;
import repast.simphony.agents.model.bind.EndStepBind;
import repast.simphony.agents.model.bind.IPropertyOrStepBind;
import repast.simphony.agents.model.bind.JoinStepBind;
import repast.simphony.agents.model.bind.TaskStepBind;
import repast.simphony.agents.model.bind.TransitionBind;

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
public abstract class AbstractModelDigester {

	/**
	 * Map storing the flowlet id as key and the binding object as value
	 */
	private Map flowletId2BindObjMap = new HashMap();

	/**
	 * Digests all child elements of the root element
	 * 
	 * @see repast.simphony.agents.model.IModelDigester#digest(repast.simphony.agents.model.bind.AgentModelBind)
	 */
	public void digest(AgentModelBind agentModelBind) {
		digestBehaviorStepFlowlet(agentModelBind);
		digestTaskFlowlets(agentModelBind);
		digestDecisionFlowlets(agentModelBind);
		digestJoinFlowlets(agentModelBind);
		digestPropertyFlowlets(agentModelBind);
		digestEndFlowlets(agentModelBind);
		digestTransitions(agentModelBind);
		digestBooleanTransitions(agentModelBind);

	}

	/**
	 * @param agentModelBind
	 */
	protected void digestBehaviorStepFlowlet(AgentModelBind agentModelBind) {
		if (agentModelBind.getBehaviorStepFlowlets() == null)
			return;
		for (Iterator iter = agentModelBind.getBehaviorStepFlowlets()
				.iterator(); iter.hasNext();) {
			BehaviorStepBind flowlet = (BehaviorStepBind) iter.next();
			flowletId2BindObjMap.put(flowlet.getId(agentModelBind), flowlet);
			digestBehaviorStepFlowlet(flowlet, agentModelBind);
		}
	}

	/**
	 * @param flowletBind
	 * @param agentModelBind
	 */
	protected void digestBehaviorStepFlowlet(BehaviorStepBind flowletBind,
			AgentModelBind agentModelBind) {
	}

	/**
	 * @param agentModelBind
	 */
	protected void digestTaskFlowlets(AgentModelBind agentModelBind) {
		if (agentModelBind.getTaskFlowlets() == null)
			return;
		for (Iterator iter = agentModelBind.getTaskFlowlets().iterator(); iter
				.hasNext();) {
			TaskStepBind flowlet = (TaskStepBind) iter.next();
			flowletId2BindObjMap.put(flowlet.getId(agentModelBind), flowlet);
			digestTaskFlowlet(flowlet, agentModelBind);
		}
	}

	/**
	 * @param flowletBind
	 * @param agentModelBind
	 */
	protected void digestTaskFlowlet(TaskStepBind flowletBind,
			AgentModelBind agentModelBind) {
	}

	/**
	 * @param agentModelBind
	 */
	protected void digestDecisionFlowlets(AgentModelBind agentModelBind) {
		if (agentModelBind.getDecisionFlowlets() == null)
			return;
		for (Iterator iter = agentModelBind.getDecisionFlowlets().iterator(); iter
				.hasNext();) {
			DecisionStepBind flowlet = (DecisionStepBind) iter.next();
			flowletId2BindObjMap.put(flowlet.getId(agentModelBind), flowlet);
			digestDecisionFlowlet(flowlet, agentModelBind);
		}
	}

	/**
	 * @param flowletBind
	 * @param agentModelBind
	 */
	protected void digestDecisionFlowlet(DecisionStepBind flowletBind,
			AgentModelBind agentModelBind) {
	}

	/**
	 * @param agentModelBind
	 */
	protected void digestJoinFlowlets(AgentModelBind agentModelBind) {
		if (agentModelBind.getJoinFlowlets() == null)
			return;
		for (Iterator iter = agentModelBind.getJoinFlowlets().iterator(); iter
				.hasNext();) {
			JoinStepBind flowlet = (JoinStepBind) iter.next();
			flowletId2BindObjMap.put(flowlet.getId(agentModelBind), flowlet);
			digestJoinFlowlet(flowlet, agentModelBind);
		}
	}

	/**
	 * @param flowletBind
	 * @param agentModelBind
	 */
	protected void digestJoinFlowlet(JoinStepBind flowletBind,
			AgentModelBind agentModelBind) {
	}

	/**
	 * @param agentModelBind
	 */
	protected void digestPropertyFlowlets(AgentModelBind agentModelBind) {
		if (agentModelBind.getPropertyFlowlets() == null)
			return;
		for (Iterator iter = agentModelBind.getPropertyFlowlets().iterator(); iter
				.hasNext();) {
			AgentPropertyBind flowlet = (AgentPropertyBind) iter.next();
			flowletId2BindObjMap.put(flowlet.getId(agentModelBind), flowlet);
			digestPropertyFlowlet(flowlet, agentModelBind);
		}
	}

	/**
	 * @param flowletBind
	 * @param agentModelBind
	 */
	protected void digestPropertyFlowlet(AgentPropertyBind flowletBind,
			AgentModelBind agentModelBind) {
	}

	/**
	 * @param agentModelBind
	 */
	protected void digestEndFlowlets(AgentModelBind agentModelBind) {
		if (agentModelBind.getEndFlowlets() == null)
			return;
		for (Iterator iter = agentModelBind.getEndFlowlets().iterator(); iter
				.hasNext();) {
			EndStepBind flowlet = (EndStepBind) iter.next();
			flowletId2BindObjMap.put(flowlet.getId(agentModelBind), flowlet);
			digestEndFlowlet(flowlet, agentModelBind);
		}
	}

	/**
	 * @param flowletBind
	 * @param agentModelBind
	 */
	protected void digestEndFlowlet(EndStepBind flowletBind,
			AgentModelBind agentModelBind) {
	}

	/**
	 * @param agentModelBind
	 */
	protected void digestTransitions(AgentModelBind agentModelBind) {
		if (agentModelBind.getTransitions() == null)
			return;
		for (Iterator iter = agentModelBind.getTransitions().iterator(); iter
				.hasNext();) {
			TransitionBind transition = (TransitionBind) iter.next();
			IPropertyOrStepBind sourceFlowlet = (IPropertyOrStepBind) flowletId2BindObjMap
					.get(transition.getSourceId());
			IPropertyOrStepBind targetFlowlet = (IPropertyOrStepBind) flowletId2BindObjMap
					.get(transition.getTargetId());
			digestTransition(transition, sourceFlowlet, targetFlowlet,
					agentModelBind);
		}
	}

	/**
	 * @param flowletBind
	 * @param agentModelBind
	 */
	protected void digestTransition(TransitionBind transitionBind,
			IPropertyOrStepBind sourceFlowletBind,
			IPropertyOrStepBind targetFlowletBind, AgentModelBind agentModelBind) {

		// null if the flow file was just created by the wizard
		if (transitionBind.getBendpoints() == null)
			return;

		for (Iterator iter = transitionBind.getBendpoints().iterator(); iter
				.hasNext();) {
			BendpointBind bendpoint = (BendpointBind) iter.next();
			digestBendpoint(transitionBind, sourceFlowletBind,
					targetFlowletBind, bendpoint, agentModelBind);
		}
	}

	/**
	 * @param agentModelBind
	 */
	protected void digestBooleanTransitions(AgentModelBind agentModelBind) {
		if (agentModelBind.getBooleanTransitions() == null)
			return;
		for (Iterator iter = agentModelBind.getBooleanTransitions().iterator(); iter
				.hasNext();) {
			BooleanTransitionBind transition = (BooleanTransitionBind) iter
					.next();
			IPropertyOrStepBind sourceFlowlet = (IPropertyOrStepBind) flowletId2BindObjMap
					.get(transition.getSourceId());
			IPropertyOrStepBind targetFlowlet = (IPropertyOrStepBind) flowletId2BindObjMap
					.get(transition.getTargetId());
			digestBooleanTransition(transition, sourceFlowlet, targetFlowlet,
					agentModelBind);
		}
	}

	/**
	 * @param flowletBind
	 * @param agentModelBind
	 */
	protected void digestBooleanTransition(
			BooleanTransitionBind transitionBind,
			IPropertyOrStepBind sourceFlowletBind,
			IPropertyOrStepBind targetFlowletBind, AgentModelBind agentModelBind) {

		// null if the flow file was just created by the wizard
		if (transitionBind.getBendpoints() == null)
			return;

		for (Iterator iter = transitionBind.getBendpoints().iterator(); iter
				.hasNext();) {
			BendpointBind bendpoint = (BendpointBind) iter.next();
			digestBendpoint(transitionBind, sourceFlowletBind,
					targetFlowletBind, bendpoint, agentModelBind);
		}
	}

	protected void digestBendpoint(TransitionBind transitionBind,
			IPropertyOrStepBind sourceFlowletBind,
			IPropertyOrStepBind targetFlowletBind, BendpointBind bendpointBind,
			AgentModelBind agentModelBind) {

	}

}
