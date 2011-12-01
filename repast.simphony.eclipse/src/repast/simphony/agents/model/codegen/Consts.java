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

package repast.simphony.agents.model.codegen;

import repast.simphony.agents.AgentBuilderConsts;

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
public class Consts {

	static public final String FILE_EXTENSION_GROOVY = "groovy"; //".groovy" files
	static public final String FILE_EXTENSION_HTML = "html"; //".html" files

	static public final String MODIFIER_STATIC = "static";
	static public final String MODIFIER_PUBLIC = "public";
	static public final String MODIFIER_PRIVATE = "private";
	static public final String MODIFIER_PROTECTED = "protected";
	static public final String MODIFIER_FINAL = "final";

	static public final String PREFIX_METHOD_START = AgentBuilderConsts.BEHAVIOR
			+ "_";
	static public final String PREFIX_METHOD_TASK = AgentBuilderConsts.TASK
			+ "_";
	static public final String PREFIX_METHOD_DECISION = AgentBuilderConsts.DECISION
			+ "_";
	static public final String PREFIX_METHOD_JOIN = AgentBuilderConsts.JOIN
			+ "_";
	static public final String PREFIX_METHOD_CALL = AgentBuilderConsts.LOOP
			+ "_";
	static public final String PREFIX_METHOD_JUMP = AgentBuilderConsts.SCAN
			+ "_";
	static public final String PREFIX_METHOD_TEMPLATE = AgentBuilderConsts.PROPERTY
			+ "_";
	static public final String PREFIX_METHOD_END = AgentBuilderConsts.END + "_";

}
