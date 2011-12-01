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
import java.util.List;

import repast.simphony.agents.model.bind.TransitionBind;
import repast.simphony.agents.model.bind.TransitionSourceBind;
import repast.simphony.agents.model.bind.TransitionTargetBind;

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
public class TransitionConverter implements Converter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thoughtworks.xstream.converters.Converter#canConvert(java.lang.Class)
	 */
	public boolean canConvert(Class type) {
		return TransitionBind.class.isAssignableFrom(type);
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
		writer.startNode(getTransitionElementName());

		marshalBody(source, writer, context);

		writer.endNode();
	}

	protected String getTransitionElementName() {
		return TransitionBind.ALIAS;
	}

	protected void marshalBody(Object source, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		TransitionBind transition = (TransitionBind) source;
		context.convertAnother(transition.getSourceFlowlet());
		context.convertAnother(transition.getTargetFlowlet());

		AgentModelConverter.marshalCollection(writer, context, "bendpoints",
				transition.getBendpoints());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thoughtworks.xstream.converters.Converter#unmarshal(com.thoughtworks.xstream.io.HierarchicalStreamReader,
	 *      com.thoughtworks.xstream.converters.UnmarshallingContext)
	 */
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		TransitionBind transition = getNewTransitionBind();

		unmarshalBody(transition, reader, context);

		return transition;
	}

	protected TransitionBind getNewTransitionBind() {
		return new TransitionBind();
	}

	protected void unmarshalBody(TransitionBind transition,
			HierarchicalStreamReader reader, UnmarshallingContext context) {
		reader.moveDown();

		transition.setSourceFlowlet((TransitionSourceBind) context
				.convertAnother(null, TransitionSourceBind.class));

		reader.moveUp();
		reader.moveDown();

		transition.setTargetFlowlet((TransitionTargetBind) context
				.convertAnother(null, TransitionTargetBind.class));

		reader.moveUp();

		// bendpoints
		reader.moveDown();
		transition.setBendpoints((List) context.convertAnother(null,
				ArrayList.class));

		reader.moveUp();
	}

}
