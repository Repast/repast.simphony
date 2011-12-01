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

package repast.simphony.agents.model.bind.converters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import repast.simphony.agents.base.Util;
import repast.simphony.agents.model.bind.AgentModelBind;
import repast.simphony.agents.model.bind.BooleanTransitionBind;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

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
public class AgentModelConverter implements Converter {

	public static final String VERSION = "version";
	public static final String AGENT_COMMENT = "agentcomment";
	public static final String AGENT_CLASS_NAME = "agentclassname";
	public static final String AGENT_PARENT_CLASS_NAME = "agentparentclassname";
	public static final String AGENT_INTERFACES = "agentinterfaces";
	public static final String AGENT_IMPORTS = "agentimports";
	public static final String TASK_STEPS = "tasksteps";
	public static final String BOOLEANTRANSITIONS = "booleantransitions";
	public static final String TRANSITIONS = "transitions";
	public static final String CONNECTIONS = "connections";
	public static final String END_STEPS = "endsteps";
	public static final String AGENT_PROPERTY_STEPS = "agentproperties";
	public static final String JOIN_STEPS = "joinsteps";
	public static final String DECISION_STEPS = "decisionsteps";
	public static final String BEHAVIOR_STEPS = "behaviorsteps";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thoughtworks.xstream.converters.Converter#canConvert(java.lang.Class)
	 */
	public boolean canConvert(Class type) {
		return AgentModelBind.class.isAssignableFrom(type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thoughtworks.xstream.converters.Converter#marshal(java.lang.Object,
	 *      com.thoughtworks.xstream.io.HierarchicalStreamWriter,
	 *      com.thoughtworks.xstream.converters.MarshallingContext)
	 */
	public void marshal(Object source, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		AgentModelBind bind = (AgentModelBind) source;
		writer
				.addAttribute(AgentModelConverter.VERSION, ""
						+ bind.getVersion());
		writer.addAttribute(AgentModelConverter.AGENT_COMMENT, Util
				.escapeXml(bind.getComment()));
		writer.addAttribute(AgentModelConverter.AGENT_CLASS_NAME, Util
				.escapeXml(bind.getFlowName()));
		writer.addAttribute(AgentModelConverter.AGENT_PARENT_CLASS_NAME, ""
				+ bind.getParentClassName());
		writer.addAttribute(AgentModelConverter.AGENT_INTERFACES, ""
				+ bind.getInterfaces());
		writer.addAttribute(AgentModelConverter.AGENT_IMPORTS, ""
				+ bind.getImports());
		marshalCollection(writer, context, AgentModelConverter.BEHAVIOR_STEPS,
				bind.getBehaviorStepFlowlets());
		marshalCollection(writer, context, AgentModelConverter.TASK_STEPS, bind
				.getTaskFlowlets());
		marshalCollection(writer, context, AgentModelConverter.DECISION_STEPS,
				bind.getDecisionFlowlets());
		marshalCollection(writer, context, AgentModelConverter.JOIN_STEPS, bind
				.getJoinFlowlets());
		marshalCollection(writer, context,
				AgentModelConverter.AGENT_PROPERTY_STEPS, bind
						.getPropertyFlowlets());
		marshalCollection(writer, context, AgentModelConverter.END_STEPS, bind
				.getEndFlowlets());

		// connections
		writer.startNode(AgentModelConverter.CONNECTIONS);
		marshalCollection(writer, context, AgentModelConverter.TRANSITIONS,
				bind.getTransitions());
		marshalCollection(writer, context,
				AgentModelConverter.BOOLEANTRANSITIONS, bind
						.getBooleanTransitions());

		writer.endNode();
	}

	protected static void marshalCollection(HierarchicalStreamWriter writer,
			MarshallingContext context, String elementName, List collection) {
		writer.startNode(elementName);
		if (collection != null)
			for (Iterator iter = collection.iterator(); iter.hasNext();) {
				context.convertAnother(iter.next());
			}
		writer.endNode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thoughtworks.xstream.converters.Converter#unmarshal(com.thoughtworks.xstream.io.HierarchicalStreamReader,
	 *      com.thoughtworks.xstream.converters.UnmarshallingContext)
	 */
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {

		AgentModelBind flowModel = new AgentModelBind();
		flowModel.setVersion(Integer.parseInt(reader
				.getAttribute(AgentModelConverter.VERSION)));
		flowModel.setComment(reader
				.getAttribute(AgentModelConverter.AGENT_COMMENT));
		flowModel.setFlowName(reader
				.getAttribute(AgentModelConverter.AGENT_CLASS_NAME));
		flowModel.setParentClassName(reader
				.getAttribute(AgentModelConverter.AGENT_PARENT_CLASS_NAME));
		flowModel.setInterfaces(reader
				.getAttribute(AgentModelConverter.AGENT_INTERFACES));
		flowModel.setImports(reader
				.getAttribute(AgentModelConverter.AGENT_IMPORTS));
		String nodeName;
		while (reader.hasMoreChildren()) {
			reader.moveDown();
			nodeName = reader.getNodeName();
			if (AgentModelConverter.BEHAVIOR_STEPS.equals(nodeName))
				flowModel.setStartFlowlets((List) context.convertAnother(null,
						ArrayList.class));
			else if (AgentModelConverter.TASK_STEPS.equals(nodeName))
				flowModel.setTaskFlowlets((List) context.convertAnother(null,
						ArrayList.class));
			else if (AgentModelConverter.DECISION_STEPS.equals(nodeName))
				flowModel.setDecisionFlowlets((List) context.convertAnother(
						null, ArrayList.class));
			else if (AgentModelConverter.JOIN_STEPS.equals(nodeName))
				flowModel.setJoinFlowlets((List) context.convertAnother(null,
						ArrayList.class));
			else if (AgentModelConverter.AGENT_PROPERTY_STEPS.equals(nodeName))
				flowModel.setPropertyFlowlets((List) context.convertAnother(
						null, ArrayList.class));
			else if (AgentModelConverter.END_STEPS.equals(nodeName))
				flowModel.setEndFlowlets((List) context.convertAnother(null,
						ArrayList.class));
			else if (AgentModelConverter.CONNECTIONS.equals(nodeName)) {
				reader.moveDown();
				while (reader.hasMoreChildren()) {
					nodeName = reader.getNodeName();
					if (AgentModelConverter.TRANSITIONS.equals(nodeName))
						flowModel.setTransitions((List) context.convertAnother(
								null, ArrayList.class));
				}
				reader.moveUp();
				reader.moveDown();
				while (reader.hasMoreChildren()) {
					nodeName = reader.getNodeName();
					if (AgentModelConverter.BOOLEANTRANSITIONS.equals(nodeName)) {
						List booleanTransitionsList = (List) context
								.convertAnother(null, ArrayList.class);
						Collections.sort(booleanTransitionsList,
								new Comparator() {
									public int compare(Object left, Object right) {
										if ((left instanceof BooleanTransitionBind)
												&& (right instanceof BooleanTransitionBind)) {
											boolean leftValue = ((BooleanTransitionBind) left)
													.getValue();
											boolean rightValue = ((BooleanTransitionBind) right)
													.getValue();
											if ((leftValue) && (!rightValue)) {
												return -1;
											} else if ((!leftValue)
													&& (rightValue)) {
												return 1;
											} else if ((leftValue)
													&& (rightValue)) {
												return 0;
											} else if ((!leftValue)
													&& (!rightValue)) {
												return 0;
											}
										}
										return 0;
									}

								});
						flowModel.setBooleanTransitions(booleanTransitionsList);
					}
				}
				reader.moveUp();
			}
			reader.moveUp();
		}
		return flowModel;
	}

}
