/*
 * Copyright (c) 2003, Alexander Greif All rights reserved. (Adapted by Michael
 * J. North for Use in Repast Simphony from Alexander Greif’s Flow4J-Eclipse
 * (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks to the
 * Original Author) (Michael J. North’s Modifications are Copyright 2007 Under
 * the Repast Simphony License, All Rights Reserved)
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. * Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of the Flow4J-Eclipse project nor
 * the names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
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

import java.io.IOException;
import java.io.Writer;
import java.util.StringTokenizer;

import repast.simphony.agents.base.Util;

/**
 * Class CodeLine TODO
 * 
 * @author agreif (Adapted by Michael J. North for Use in Repast Simphony from
 *         Alexander Greif’s Flow4J-Eclipse
 *         (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks
 *         to the Original Author)
 * 
 */
public class CodeLine extends DepthCounter implements ISrcStructure {

	private String text;

	private boolean includeLineFeed = true;

	/**
	 * Constructs a line of code
	 * 
	 * @param text
	 *            the line's contents
	 */
	public CodeLine(String text) {
		setText(text);
	}

	/**
	 * Sets the text of this line
	 * 
	 * @param text
	 *            the new text of this line
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Writes the line to the given writer
	 * 
	 * @see repast.simphony.agents.model.codegen.structure.ISrcStructure#serialize(java.io.BufferedWriter)
	 */
	public void serialize(Writer writer) throws IOException {

		StringTokenizer tokenizer = new StringTokenizer(Util
				.convertToLineFeeds(text), "\n\r\f");
		while (tokenizer.hasMoreTokens()) {
			this.writeBlanks(writer);
			writer.write(tokenizer.nextToken());
			if (tokenizer.hasMoreTokens())
				writer.write(Util.getPlatformLineDelimiter());
		}

		if (this.includeLineFeed)
			writer.write(Util.getPlatformLineDelimiter());
	}

	public boolean isIncludeLineFeed() {
		return includeLineFeed;
	}

	public void setIncludeLineFeed(boolean includeLineFeed) {
		this.includeLineFeed = includeLineFeed;
	}

}
