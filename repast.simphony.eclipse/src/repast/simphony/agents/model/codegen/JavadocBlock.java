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

package repast.simphony.agents.model.codegen;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import repast.simphony.agents.base.Util;

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
public class JavadocBlock extends DepthCounter implements ISrcStructure {

	private String description;
	private List blockTags;

	/**
	 * Constructor
	 * 
	 * @param desc
	 *            the javadoc description
	 */
	public JavadocBlock(String desc) {
		description = desc;
	}

	/**
	 * Constructor
	 * 
	 */
	public JavadocBlock() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see repast.simphony.agents.model.codegen.structure.ISrcStructure#serialize(java.io.Writer)
	 */
	public void serialize(Writer writer) throws IOException {
		writer.write(Util.getPlatformLineDelimiter());
		writer.write("/**");
		writer.write(Util.getPlatformLineDelimiter());
		writer.write(" * ");
		writer.write(Util.getPlatformLineDelimiter());
		if (description != null && description.length() != 0) {
			writer.write(" * ");
			writer.write(description);
			writer.write(Util.getPlatformLineDelimiter());
		}
		if (blockTags != null) {
			for (Iterator iter = blockTags.iterator(); iter.hasNext();) {
				Tag tag = (Tag) iter.next();
				tag.serialize(writer);
			}
		}
		writer.write(Util.getPlatformLineDelimiter());
		writer.write(" * ");
		writer.write(Util.getPlatformLineDelimiter());
		writer.write(" */");
		writer.write(Util.getPlatformLineDelimiter());
	}

	public class Tag extends DepthCounter implements ISrcStructure {

		private String name;

		public Tag(String name) {
			this.name = name;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see repast.simphony.agents.model.codegen.structure.ISrcStructure#serialize(java.io.Writer)
		 */
		public void serialize(Writer writer) throws IOException {
			writer.write(" * @");
			writer.write(name);
		}
	}

	public class JavadocTag extends Tag {

		private String key;
		private String description;

		public JavadocTag(String name) {
			super(name);
		}

		public JavadocTag(String name, String key, String description) {
			super(name);
			this.key = key;
			this.description = description;
		}

		public JavadocTag(String name, String description) {
			super(name);
			this.description = description;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see repast.simphony.agents.model.codegen.structure.ISrcStructure#serialize(java.io.Writer)
		 */
		@Override
		public void serialize(Writer writer) throws IOException {
			super.serialize(writer);
			if (key != null && key.length() != 0) {
				writer.write(" ");
				writer.write(key);
			}
			if (description != null && description.length() != 0) {
				writer.write(" ");
				writer.write(description);
			}
			writer.write(Util.getPlatformLineDelimiter());
		}
	}

	public class XDocletTag extends Tag {

		private List params;

		public XDocletTag(String name) {
			super(name);
		}

		public Parameter addParam(String key, String value) {
			if (value == null)
				throw new IllegalStateException("value must not be null!");
			if (params == null)
				params = new ArrayList();
			Parameter param = new Parameter(key, value);
			params.add(param);

			return param;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see repast.simphony.agents.model.codegen.structure.ISrcStructure#serialize(java.io.Writer)
		 */
		@Override
		public void serialize(Writer writer) throws IOException {
			super.serialize(writer);
			if (params == null || params.isEmpty())
				return;
			for (Iterator iter = params.iterator(); iter.hasNext();) {
				Parameter param = (Parameter) iter.next();
				writer.write(" ");
				param.serialize(writer);
			}
			writer.write(Util.getPlatformLineDelimiter());
		}

		public class Parameter extends DepthCounter implements ISrcStructure {
			private String key;
			private String value;

			public Parameter(String key, String value) {
				this.key = key;
				this.value = value;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see repast.simphony.agents.model.codegen.structure.ISrcStructure#serialize(java.io.Writer)
			 */
			public void serialize(Writer writer) throws IOException {
				writer.write(key);
				writer.write("=\"");
				writer.write(value);
				writer.write("\"");
			}

			/**
			 * Sets the parameter's value.
			 * 
			 * @param string
			 *            the parameter's value.
			 */
			public void setValue(String string) {
				value = string;
			}

			/**
			 * Returns the parameter's value.
			 * 
			 * @return the parameter's value.
			 */
			public String getValue() {
				return value;
			}
		}
	}

	/**
	 * Adds a tag to this javadoc block.
	 * 
	 * @param tag
	 *            he tag to add
	 */
	public void addTag(Tag tag) {
		if (blockTags == null)
			blockTags = new ArrayList();
		blockTags.add(tag);
	}
}
