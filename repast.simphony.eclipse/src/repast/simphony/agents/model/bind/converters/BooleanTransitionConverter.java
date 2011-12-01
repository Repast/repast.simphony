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

import repast.simphony.agents.model.bind.BooleanTransitionBind;
import repast.simphony.agents.model.bind.TransitionBind;

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
public class BooleanTransitionConverter extends TransitionConverter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thoughtworks.xstream.converters.Converter#canConvert(java.lang.Class)
	 */
	@Override
	public boolean canConvert(Class type) {
		return BooleanTransitionBind.class.isAssignableFrom(type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see repast.simphony.agents.model.bind.converters.TransitionConverter#getNewTransitionBind()
	 */
	@Override
	protected TransitionBind getNewTransitionBind() {
		return new BooleanTransitionBind();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see repast.simphony.agents.model.bind.converters.TransitionConverter#getTransitionElementName()
	 */
	@Override
	protected String getTransitionElementName() {
		return BooleanTransitionBind.ALIAS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see repast.simphony.agents.model.bind.converters.TransitionConverter#marshalBody(java.lang.Object,
	 *      com.thoughtworks.xstream.io.HierarchicalStreamWriter,
	 *      com.thoughtworks.xstream.converters.MarshallingContext)
	 */
	@Override
	protected void marshalBody(Object source, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		BooleanTransitionBind booleanTransition = (BooleanTransitionBind) source;
		writer.addAttribute("value", new Boolean(booleanTransition.getValue())
				.toString());
		super.marshalBody(source, writer, context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see repast.simphony.agents.model.bind.converters.TransitionConverter#unmarshalBody(repast.simphony.agents.model.bind.TransitionBind,
	 *      com.thoughtworks.xstream.io.HierarchicalStreamReader,
	 *      com.thoughtworks.xstream.converters.UnmarshallingContext)
	 */
	@Override
	protected void unmarshalBody(TransitionBind transition,
			HierarchicalStreamReader reader, UnmarshallingContext context) {
		BooleanTransitionBind booleanTransition = (BooleanTransitionBind) transition;
		booleanTransition.setValue(new Boolean(reader.getAttribute("value"))
				.booleanValue());
		super.unmarshalBody(transition, reader, context);
	}

}
