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

package repast.simphony.agents.model.bind;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import repast.simphony.agents.model.bind.converters.AgentModelConverter;
import repast.simphony.agents.model.bind.converters.AgentPropertyConverter;
import repast.simphony.agents.model.bind.converters.AgentPropertyorStepLabelConverter;
import repast.simphony.agents.model.bind.converters.BehaviorStepConverter;
import repast.simphony.agents.model.bind.converters.BendpointConverter;
import repast.simphony.agents.model.bind.converters.BooleanTransitionConverter;
import repast.simphony.agents.model.bind.converters.DecisionStepConverter;
import repast.simphony.agents.model.bind.converters.EndStepConverter;
import repast.simphony.agents.model.bind.converters.JoinStepConverter;
import repast.simphony.agents.model.bind.converters.PropertyConverter;
import repast.simphony.agents.model.bind.converters.TaskStepConverter;
import repast.simphony.agents.model.bind.converters.TransitionConverter;
import repast.simphony.agents.model.bind.converters.TransitionSourceConverter;
import repast.simphony.agents.model.bind.converters.TransitionTargetConverter;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * @author greifa (Adapted by Michael J. North for Use in Repast Simphony from
 *         Alexander Greif’s Flow4J-Eclipse
 *         (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks
 *         to the Original Author)
 * 
 * 
 * 
 */
public class BindingHandler {

	private static BindingHandler instance;
	private XStream xstream;

	private BindingHandler() {
		xstream = new XStream(new DomDriver());
		// does not require XPP3 library

		xstream.alias(AgentModelBind.ALIAS, AgentModelBind.class);
		xstream.registerConverter(new AgentModelConverter());

		xstream.alias(PropertyOrStepLabelBind.ALIAS,
				PropertyOrStepLabelBind.class);
		xstream.registerConverter(new AgentPropertyorStepLabelConverter());

		xstream.alias(BehaviorStepBind.ALIAS, BehaviorStepBind.class);
		xstream.registerConverter(new BehaviorStepConverter());

		xstream.alias(TaskStepBind.ALIAS, TaskStepBind.class);
		xstream.registerConverter(new TaskStepConverter());

		xstream.alias(DecisionStepBind.ALIAS, DecisionStepBind.class);
		xstream.registerConverter(new DecisionStepConverter());

		xstream.alias(JoinStepBind.ALIAS, JoinStepBind.class);
		xstream.registerConverter(new JoinStepConverter());

		xstream.alias(AgentPropertyBind.ALIAS, AgentPropertyBind.class);
		xstream.registerConverter(new AgentPropertyConverter());

		xstream.alias(EndStepBind.ALIAS, EndStepBind.class);
		xstream.registerConverter(new EndStepConverter());

		xstream.alias(PropertyBind.ALIAS, PropertyBind.class);
		xstream.registerConverter(new PropertyConverter());

		// connections
		xstream.alias(TransitionBind.ALIAS, TransitionBind.class);
		xstream.registerConverter(new TransitionConverter());

		xstream.alias(BooleanTransitionBind.ALIAS, BooleanTransitionBind.class);
		xstream.registerConverter(new BooleanTransitionConverter());

		xstream.alias(TransitionSourceBind.ALIAS, TransitionSourceBind.class);
		xstream.registerConverter(new TransitionSourceConverter());

		xstream.alias(TransitionTargetBind.ALIAS, TransitionTargetBind.class);
		xstream.registerConverter(new TransitionTargetConverter());

		xstream.alias(BendpointBind.ALIAS, BendpointBind.class);
		xstream.registerConverter(new BendpointConverter());
		
		xstream.setClassLoader(this.getClass().getClassLoader());
	}

	public static BindingHandler getInstance() {
		if (instance == null)
			instance = new BindingHandler();

		return instance;
	}

	public AgentModelBind loadFlowModel(File file) throws IOException {
		return loadFlowModel(file, null);
	}

	public AgentModelBind loadFlowModel(File file,
			ClassLoader taskFlowletClassLoader) throws IOException {
		// set the task class loader for property verification
		TaskStepBind.setTaskClassLoader(taskFlowletClassLoader);

		FileReader fr = null;
		AgentModelBind agentModelBind = null;
		try {
			fr = new FileReader(file);
			agentModelBind = (AgentModelBind) xstream.fromXML(fr);
			String fileName = file.getName();
			String className = fileName.substring(0, fileName.lastIndexOf("."));
			agentModelBind.setFlowName(className);
		} catch (FileNotFoundException e) {
			throw e;
		} finally {
			if (fr != null)
				fr.close();
		}

		return agentModelBind;
	}

	public void saveFlowModel(AgentModelBind agentModelBind, OutputStream out)
			throws IOException {
		OutputStreamWriter osw = new OutputStreamWriter(out, "UTF-8");
		BufferedWriter bw = new BufferedWriter(osw);
		bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		bw.newLine();
		xstream.toXML(agentModelBind, bw);
		bw.flush();
	}

	public ByteArrayInputStream saveFlowModel(AgentModelBind agentModelBind)
			throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		saveFlowModel(agentModelBind, bout);
		bout.close();

		return new ByteArrayInputStream(bout.toByteArray());
	}

}
