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

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import repast.simphony.agents.base.Util;

/**
 * @author agreif (Adapted by Michael J. North for Use in Repast Simphony from
 *         Alexander Greif’s Flow4J-Eclipse
 *         (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks
 *         to the Original Author)
 * 
 *         TODO
 * 
 * 
 * 
 */
@SuppressWarnings("unchecked")
public class Field extends DepthCounter implements ISrcStructure {

	private String type;
	private String name;
	/** The value of the field or null for default initialization value */
	private String displayName = "";
	private String defaultValue = "";
	private List modifiers = new ArrayList();

	/**
	 * Creates a new Field instance and sets its name
	 * 
	 * @param name
	 *            the field's name
	 */
	public Field(String name) {
		super();
		this.name = name;
	}

	/**
	 * Write the field to the writer
	 * 
	 * @see repast.simphony.agents.model.codegen.structure.ISrcStructure#serialize()
	 */
	public void serialize(Writer writer) throws IOException {

		if (DepthCounter.isGeneratingGroovy()) {

			this.writeBlanks(writer);
			writer.write("/**");
			writer.write(Util.getPlatformLineDelimiter());

			this.writeBlanks(writer);
			writer.write(" *");
			writer.write(Util.getPlatformLineDelimiter());

			this.writeBlanks(writer);
			if (DepthCounter.isSet(this.comment)) {
				writer.write(" * " + this.comment);
			} else if (DepthCounter.isSet(this.displayName)) {
				writer.write(" * This is the " + this.displayName + " field.");
			} else {
				writer.write(" * This is the " + this.name + " field.");
			}
			writer.write(Util.getPlatformLineDelimiter());

			this.writeBlanks(writer);
			writer.write(" * @field " + this.name);
			writer.write(Util.getPlatformLineDelimiter());

			this.writeBlanks(writer);
			writer.write(" *");
			writer.write(Util.getPlatformLineDelimiter());

			this.writeBlanks(writer);
			writer.write(" */");
			writer.write(Util.getPlatformLineDelimiter());

			if (DepthCounter.isSet(this.displayName)) {

				this.writeBlanks(writer);
				writer.write("@Parameter (displayName = \"" + this.displayName
						+ "\", usageName = \"" + this.name + "\")");
				writer.write(Util.getPlatformLineDelimiter());

				String getterSetterName = ("" + this.name.charAt(0))
						.toUpperCase();
				if (this.name.length() > 1) {
					getterSetterName = getterSetterName
							+ this.name.substring(1);
				}

				this.writeBlanks(writer);
				for (Iterator iter = modifiers.iterator(); iter.hasNext();) {
					writer.write((String) iter.next() + " ");
				}
				writer.write(type + " get" + getterSetterName + "() {");
				writer.write(Util.getPlatformLineDelimiter());

				this.incrementDepth();
				this.writeBlanks(writer);
				writer.write("return " + this.name);
				writer.write(Util.getPlatformLineDelimiter());
				this.decrementDepth();

				this.writeBlanks(writer);
				writer.write("}");
				writer.write(Util.getPlatformLineDelimiter());

				this.writeBlanks(writer);
				for (Iterator iter = modifiers.iterator(); iter.hasNext();) {
					writer.write((String) iter.next() + " ");
				}
				writer.write("void set" + getterSetterName + "(" + type
						+ " newValue) {");
				writer.write(Util.getPlatformLineDelimiter());

				this.incrementDepth();
				this.writeBlanks(writer);
				writer.write(this.name + " = newValue");
				writer.write(Util.getPlatformLineDelimiter());
				this.decrementDepth();

				this.writeBlanks(writer);
				writer.write("}");
				writer.write(Util.getPlatformLineDelimiter());

			}

			this.writeBlanks(writer);
			for (Iterator iter = modifiers.iterator(); iter.hasNext();) {
				writer.write((String) iter.next() + " ");
			}
			writer.write(type.trim() + " " + name.trim());
			if (this.defaultValue != null) {
				writer.write(" = " + this.defaultValue);
			}
			writer.write("");
			writer.write(Util.getPlatformLineDelimiter());
			writer.write(Util.getPlatformLineDelimiter());

		} else {

			String trimmedName = name.trim();

			this.writeBlanks(writer);
			writer.write("<li>There is a field named \"" + trimmedName + "\" with visibility \"");
			for (Iterator iter = modifiers.iterator(); iter.hasNext();) {
				writer.write((String) iter.next());
				if (iter.hasNext()) writer.write(" ");
			}
			writer.write("\" of type \"");
			writer.write(type.trim());
			writer.write("\"");
			if (this.defaultValue != null) {
				writer.write(" and default value " + this.defaultValue + ". ");
			}
			
			if (DepthCounter.isSet(this.comment)) {
				writer.write(this.comment);
			}
			if (DepthCounter.isSet(this.displayName)) {
				writer.write(" The block is tagged with the name \"" + this.displayName + "\".");
			}

			writer.write("</li>");
			writer.write(Util.getPlatformLineDelimiter());
			writer.write(Util.getPlatformLineDelimiter());

		}

	}

	/**
	 * Adds a modifier to the method.
	 * 
	 * @param modifier
	 *            the modifier
	 */
	public void addModifier(String modifier) {
		modifiers.add(modifier);
	}

	/**
	 * TODO
	 * 
	 * @param string
	 */
	public void setName(String string) {
		name = string;
	}

	/**
	 * TODO
	 * 
	 * @param string
	 */
	public void setType(String string) {
		type = string;
	}

	/**
	 * TODO
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

}
