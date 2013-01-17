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

import repast.simphony.agents.base.Util;
import repast.simphony.agents.model.bind.BehaviorStepBind;
import repast.simphony.agents.model.bind.PropertyOrStepBind;

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
public class BehaviorStepConverter extends
		AgentPropertyorStepWithLabelConverter {

	public static final String SCHEDULEANNOTATIONSTART = "scheduleannotationstart";
	public static final String SCHEDULEANNOTATIONPICK = "scheduleannotationpick";
	public static final String SCHEDULEANNOTATIONINTERVAL = "scheduleannotationinterval";
	public static final String SCHEDULEANNOTATIONPRIORITY = "scheduleannotationpriority";
	public static final String SCHEDULEANNOTATIONDURATION = "scheduleannotationduration";
	public static final String SCHEDULEANNOTATIONSHUFFLE = "scheduleannotationshuffle";
	public static final String WATCHANNOTATIONID = "watchannotationid";
	public static final String WATCHANNOTATIONQUERY = "watchannotationquery";
	public static final String WATCHANNOTATIONTARGETCLASSNAME = "watchannotationtargetclassname";
	public static final String WATCHANNOTATIONTARGETFIELDNAMES = "watchannotationtargetfieldnames";
	public static final String WATCHANNOTATIONTRIGGERCONDITION = "watchannotationtriggercondition";
	public static final String WATCHANNOTATIONTRIGGERSCHEDULE = "watchannotationtriggerschedule";
	public static final String WATCHANNOTATIONTRIGGERDELTA = "watchannotationtriggerdelta";
	public static final String WATCHANNOTATIONTRIGGERPRIORITY = "watchannotationtriggerpriority";
	public static final String WATCHANNOTATIONPICK = "watchannotationpick";
	public static final String COMMENT = "comment";
	public static final String VISIBILITY = "visibility";
	public static final String RETURNTYPE = "returntype";
	public static final String COMPILEDNAME = "compiledname";
	public static final String PARAMETERS = "parameters";
	public static final String EXCEPTIONS = "exceptions";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thoughtworks.xstream.converters.Converter#canConvert(java.lang.Class)
	 */
	public boolean canConvert(Class type) {
		return BehaviorStepBind.class.isAssignableFrom(type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see repast.simphony.agents.model.bind.converters.AgentPropertyorStepConverter#getFlowletElementName()
	 */
	@Override
	protected String getFlowletElementName() {
		return BehaviorStepBind.ALIAS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see repast.simphony.agents.model.bind.converters.AgentPropertyorStepConverter#getNewFlowletBind()
	 */
	@Override
	protected PropertyOrStepBind getNewFlowletBind() {
		return new BehaviorStepBind();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see repast.simphony.agents.model.bind.converters.AgentPropertyorStepConverter#marshalBody(java.lang.Object,
	 *      com.thoughtworks.xstream.io.HierarchicalStreamWriter,
	 *      com.thoughtworks.xstream.converters.MarshallingContext)
	 */
	@Override
	public void marshalBody(Object source, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		BehaviorStepBind flowlet = (BehaviorStepBind) source;

		String text = flowlet.getScheduleAnnotationStart();
		if (text != null)
			writer.addAttribute(BehaviorStepConverter.SCHEDULEANNOTATIONSTART,
					Util.escapeXml(text));
		text = flowlet.getScheduleAnnotationPick();
		if (text != null)
			writer.addAttribute(BehaviorStepConverter.SCHEDULEANNOTATIONPICK,
					Util.escapeXml(text));
		text = flowlet.getScheduleAnnotationInterval();
		if (text != null)
			writer.addAttribute(
					BehaviorStepConverter.SCHEDULEANNOTATIONINTERVAL, Util
							.escapeXml(text));
		text = flowlet.getScheduleAnnotationPriority();
		if (text != null)
			writer.addAttribute(
					BehaviorStepConverter.SCHEDULEANNOTATIONPRIORITY, Util
							.escapeXml(text));
		text = flowlet.getScheduleAnnotationDuration();
		if (text != null)
			writer.addAttribute(
					BehaviorStepConverter.SCHEDULEANNOTATIONDURATION, Util
							.escapeXml(text));
		text = flowlet.getScheduleAnnotationShuffle();
		if (text != null)
			writer.addAttribute(
					BehaviorStepConverter.SCHEDULEANNOTATIONSHUFFLE, Util
							.escapeXml(text));
		text = flowlet.getWatchAnnotationId();
		if (text != null)
			writer.addAttribute(BehaviorStepConverter.WATCHANNOTATIONID, Util
					.escapeXml(text));
		text = flowlet.getWatchAnnotationQuery();
		if (text != null)
			writer.addAttribute(BehaviorStepConverter.WATCHANNOTATIONQUERY,
					Util.escapeXml(text));
		text = flowlet.getWatchAnnotationTargetClassName();
		if (text != null)
			writer.addAttribute(
					BehaviorStepConverter.WATCHANNOTATIONTARGETCLASSNAME, Util
							.escapeXml(text));
		text = flowlet.getWatchAnnotationTargetFieldNames();
		if (text != null)
			writer.addAttribute(
					BehaviorStepConverter.WATCHANNOTATIONTARGETFIELDNAMES, Util
							.escapeXml(text));
		text = flowlet.getWatchAnnotationTriggerCondition();
		if (text != null)
			writer.addAttribute(
					BehaviorStepConverter.WATCHANNOTATIONTRIGGERCONDITION, Util
							.escapeXml(text));
		text = flowlet.getWatchAnnotationTriggerSchedule();
		if (text != null)
			writer.addAttribute(
					BehaviorStepConverter.WATCHANNOTATIONTRIGGERSCHEDULE, Util
							.escapeXml(text));
		text = flowlet.getWatchAnnotationTriggerDelta();
		if (text != null)
			writer.addAttribute(
					BehaviorStepConverter.WATCHANNOTATIONTRIGGERDELTA, Util
							.escapeXml(text));
		text = flowlet.getWatchAnnotationTriggerPriority();
		if (text != null)
			writer.addAttribute(
					BehaviorStepConverter.WATCHANNOTATIONTRIGGERPRIORITY, Util
							.escapeXml(text));
		text = flowlet.getWatchAnnotationPick();
		if (text != null)
			writer.addAttribute(BehaviorStepConverter.WATCHANNOTATIONPICK, Util
					.escapeXml(text));
		text = flowlet.getComment();
		if (text != null)
			writer.addAttribute(BehaviorStepConverter.COMMENT, Util
					.escapeXml(text));
		text = flowlet.getVisibility();
		if (text != null)
			writer.addAttribute(BehaviorStepConverter.VISIBILITY, Util
					.escapeXml(text));
		text = flowlet.getReturnType();
		if (text != null)
			writer.addAttribute(BehaviorStepConverter.RETURNTYPE, Util
					.escapeXml(text));
		text = flowlet.getCompiledName();
		if (text != null)
			writer.addAttribute(BehaviorStepConverter.COMPILEDNAME, Util
					.escapeXml(text));
		text = flowlet.getParameters();
		if (text != null)
			writer.addAttribute(BehaviorStepConverter.PARAMETERS, Util
					.escapeXml(text));
		text = flowlet.getExceptions();
		if (text != null)
			writer.addAttribute(BehaviorStepConverter.EXCEPTIONS, Util
					.escapeXml(text));

		super.marshalBody(source, writer, context);

	}

	@Override
	protected void unmarshalBody(PropertyOrStepBind target,
			HierarchicalStreamReader reader, UnmarshallingContext context) {
		BehaviorStepBind flowlet = (BehaviorStepBind) target;
		super.unmarshalBody(target, reader, context);

		flowlet.setScheduleAnnotationStart(reader
				.getAttribute(BehaviorStepConverter.SCHEDULEANNOTATIONSTART));
		flowlet.setScheduleAnnotationPick(reader
				.getAttribute(BehaviorStepConverter.SCHEDULEANNOTATIONPICK));
		flowlet
				.setScheduleAnnotationInterval(reader
						.getAttribute(BehaviorStepConverter.SCHEDULEANNOTATIONINTERVAL));
		flowlet
				.setScheduleAnnotationPriority(reader
						.getAttribute(BehaviorStepConverter.SCHEDULEANNOTATIONPRIORITY));
		flowlet
				.setScheduleAnnotationDuration(reader
						.getAttribute(BehaviorStepConverter.SCHEDULEANNOTATIONDURATION));
		flowlet.setScheduleAnnotationShuffle(reader
				.getAttribute(BehaviorStepConverter.SCHEDULEANNOTATIONSHUFFLE));
		flowlet.setWatchAnnotationId(reader
				.getAttribute(BehaviorStepConverter.WATCHANNOTATIONID));
		flowlet.setWatchAnnotationQuery(reader
				.getAttribute(BehaviorStepConverter.WATCHANNOTATIONQUERY));
		flowlet
				.setWatchAnnotationTargetClassName(reader
						.getAttribute(BehaviorStepConverter.WATCHANNOTATIONTARGETCLASSNAME));
		flowlet
				.setWatchAnnotationTargetFieldNames(reader
						.getAttribute(BehaviorStepConverter.WATCHANNOTATIONTARGETFIELDNAMES));
		flowlet
				.setWatchAnnotationTriggerCondition(reader
						.getAttribute(BehaviorStepConverter.WATCHANNOTATIONTRIGGERCONDITION));
		flowlet
				.setWatchAnnotationTriggerSchedule(reader
						.getAttribute(BehaviorStepConverter.WATCHANNOTATIONTRIGGERSCHEDULE));
		flowlet
				.setWatchAnnotationTriggerDelta(reader
						.getAttribute(BehaviorStepConverter.WATCHANNOTATIONTRIGGERDELTA));
		flowlet
				.setWatchAnnotationTriggerPriority(reader
						.getAttribute(BehaviorStepConverter.WATCHANNOTATIONTRIGGERPRIORITY));
		flowlet.setWatchAnnotationPick(reader
				.getAttribute(BehaviorStepConverter.WATCHANNOTATIONPICK));
		flowlet.setComment(reader.getAttribute(BehaviorStepConverter.COMMENT));
		flowlet.setVisibility(reader
				.getAttribute(BehaviorStepConverter.VISIBILITY));
		flowlet.setReturnType(reader
				.getAttribute(BehaviorStepConverter.RETURNTYPE));
		flowlet.setCompiledName(reader
				.getAttribute(BehaviorStepConverter.COMPILEDNAME));
		flowlet.setParameters(reader
				.getAttribute(BehaviorStepConverter.PARAMETERS));
		flowlet.setExceptions(reader
				.getAttribute(BehaviorStepConverter.EXCEPTIONS));
	}

}
