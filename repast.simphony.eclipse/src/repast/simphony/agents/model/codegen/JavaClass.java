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
 * TODO
 * 
 * 
 * 
 */
@SuppressWarnings("unchecked")
public class JavaClass extends DepthCounter implements ISrcStructure,
		IJavadocHolder {

	private JavadocBlock javadoc;
	private String className = "";
	private String superClassName = "";
	private String comment = "";

	private List interfaces = new ArrayList();
	private List modifiers = new ArrayList();
	private List fields = new ArrayList();
	private CodeBlock staticInitializer; // will be initialized lazily
	private List methods = new ArrayList();
	private List innerClasses; // will be initialized lazily
	private List constructors; // will be initialized lazily

	/**
	 * Creates a JavaClass instance
	 * 
	 */
	public JavaClass() {
		super();

	}

	/**
	 * Creates a new java class instance with the given class name.
	 * 
	 * @param className
	 *            the name of the class
	 */
	public JavaClass(String className) {
		this();
		setClassName(className);
	}

	/**
	 * TODO
	 * 
	 * @param className
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * Returns the class name
	 * 
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * Sets the name of the super class
	 * 
	 * @param string
	 *            the name of the super class;
	 */
	public void setSuperClassName(String superClassName) {
		this.superClassName = superClassName;
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
	 * Adds a method to the class
	 * 
	 * @param method
	 *            the method to be added
	 */
	public void addMethod(Method method) {
		methods.add(method);
	}

	/**
	 * Adds a field to the class
	 * 
	 * @param field
	 *            the field to be added
	 */
	public void addField(Field field) {
		fields.add(field);
	}

	/**
	 * Adds an inner class. if the inner class container is null then a new
	 * container will be created
	 * 
	 * @param innerClass
	 *            the inner class to add
	 */
	public void addInnerClass(JavaClass innerClass) {
		if (innerClasses == null)
			innerClasses = new ArrayList();
		innerClasses.add(innerClass);
	}

	/**
	 * Adds a constructor. if the constructors container is null then a new
	 * container will be created
	 * 
	 * @param constructor
	 *            the constructor to add
	 */
	private void addConstructor(Constructor constructor) {
		if (constructors == null)
			constructors = new ArrayList();
		constructors.add(constructor);
	}

	/**
	 * Returns a new Constructor instance, that is added to the constructors
	 * collection of this class.
	 * 
	 * @return a new Constructor instance
	 */
	public Constructor getNewConstructor() {
		Constructor c = new Constructor(className);
		addConstructor(c);

		return c;
	}

	/**
	 * returns true if the class already contains a method with the given name
	 * 
	 * @param methodName
	 *            method name
	 * @return true if the class already contains the method with the given name
	 */
	public boolean hasMethod(String methodName) {
		for (Iterator iter = methods.iterator(); iter.hasNext();) {
			Method method = (Method) iter.next();
			if (method.getName().equals(methodName))
				return true;
		}
		return false;
	}

	/**
	 * returns true if the class already contains a field with the given name
	 * 
	 * @param fieldName
	 *            field name
	 * @return true if the class already contains the field with the given name
	 */
	public boolean hasField(String fieldName) {
		for (Iterator iter = fields.iterator(); iter.hasNext();) {
			Field field = (Field) iter.next();
			if (field.getName().equals(fieldName))
				return true;
		}
		return false;
	}

	/**
	 * Adds an interface to the class
	 * 
	 * @param interfaceName
	 *            the name of the interface
	 */
	public void addInterface(String interfaceName) {
		interfaces.add(interfaceName);
	}

	public void addIDRepastComponents() {

		Field serialVersionUIDField = new Field("serialVersionUID");
		serialVersionUIDField.setType("long");
		serialVersionUIDField
				.setComment("This value is used to automatically generate agent identifiers.");
		serialVersionUIDField.setDefaultValue("1L");
		serialVersionUIDField.addModifier("private");
		serialVersionUIDField.addModifier("static");
		serialVersionUIDField.addModifier("final");
		this.fields.add(serialVersionUIDField);

		Field idCounterField = new Field("agentIDCounter");
		idCounterField.setType("long");
		idCounterField
				.setComment("This value is used to automatically generate agent identifiers.");
		idCounterField.setDefaultValue("1");
		idCounterField.addModifier("protected");
		idCounterField.addModifier("static");
		this.fields.add(idCounterField);

		Field idField = new Field("agentID");
		idField.setType("String");
		idField.setComment("This value is the agent's identifier.");
		if (DepthCounter.isSet(this.className)) {
			idField.setDefaultValue("\"" + this.className
					+ " \" + (agentIDCounter++)");
		} else {
			idField.setDefaultValue("agentIDCounter++");
		}
		idField.addModifier("protected");
		this.fields.add(idField);

		Method idMethod = new Method("toString");
		idMethod
				.setComment("This method provides a human-readable name for the agent.");
		idMethod.addExtraAnnotation("@ProbeID()");
		idMethod.setReturnType("String");
		idMethod.addModifier("public");
		CodeLine codeLine = new CodeLine("// Set the default agent identifier.");
		idMethod.addToCodeBlock(codeLine);
		codeLine = new CodeLine("returnValue = this.agentID");
		idMethod.addToCodeBlock(codeLine);
		this.methods.add(idMethod);

		// Method resetIDMethod = new Method("ResetAgentIDCounter");
		// resetIDMethod
		// .setComment("This method resets the agent identifier counter at the
		// end of a simulation run.");
		// resetIDMethod.setScheduleAnnotationStart("ScheduledMethod.END");
		// resetIDMethod.setScheduleAnnotationPick("1");
		// resetIDMethod.setReturnType("void");
		// resetIDMethod.addModifier("public");
		// codeLine = new CodeLine("// Reset the agent identifier counter.");
		// resetIDMethod.addToCodeBlock(codeLine);
		// codeLine = new CodeLine("agentIDCounter = 1");
		// resetIDMethod.addToCodeBlock(codeLine);
		// this.methods.add(resetIDMethod);

	}

	/**
	 * 
	 * Serializes the classes source code to the given Writer
	 * 
	 * @param writer
	 *            the writer where the contents should be serialized to
	 */
	public void serialize(Writer writer) throws IOException {
		// javadoc

		if (DepthCounter.isGeneratingGroovy())  {

			writer.write("/**");
			writer.write(Util.getPlatformLineDelimiter());
	
			writer.write(" *");
			writer.write(Util.getPlatformLineDelimiter());
	
			if (DepthCounter.isSet(this.comment)) {
				writer.write(" * " + this.comment);
			} else {
				writer.write(" * This is the " + this.className + " class.");
				writer.write(Util.getPlatformLineDelimiter());
				writer.write(" * @class " + this.className);
			}
			writer.write(Util.getPlatformLineDelimiter());
	
			writer.write(" *");
			writer.write(Util.getPlatformLineDelimiter());
	
			writer.write(" */");
			writer.write(Util.getPlatformLineDelimiter());
	
			// class
			for (Iterator iter = modifiers.iterator(); iter.hasNext();)
				writer.write((String) iter.next() + " ");
	
			writer.write("class " + className + " ");
			if (superClassName != null && superClassName.trim().length() != 0)
				writer.write("extends " + superClassName + " ");
	
			if (interfaces.size() != 0) {
				writer.write("implements ");
				for (Iterator iter = interfaces.iterator(); iter.hasNext();) {
					writer.write((String) iter.next());
					if (iter.hasNext()) {
						writer.write(", ");
					}
				}
			}
			
		} else {
			
			// class
			writer.write("The class \"" + className + "\" is defined with visibility ");
			for (Iterator iter = modifiers.iterator(); iter.hasNext();)
				writer.write("\"" + (String) iter.next() + "\" ");
	
			if (superClassName != null && superClassName.trim().length() != 0)
				writer.write("which extends parent class \"" + superClassName + "\" ");
	
			if (interfaces.size() != 0) {
				writer.write("which implements ");
				for (Iterator iter = interfaces.iterator(); iter.hasNext();) {
					writer.write("\"" + (String) iter.next() + "\"");
					if (iter.hasNext()) {
						writer.write(", ");
					}
				}
			}
			
			if (DepthCounter.isSet(this.comment)) {
				writer.write(". ");
				writer.write(this.comment);
			} else {
				writer.write(".");
			}
			writer.write(Util.getPlatformLineDelimiter());
	
		}

		writer.write(" {");
		writer.write(Util.getPlatformLineDelimiter());
		writer.write(Util.getPlatformLineDelimiter());
		
		// fields
		if (!this.hasMethod("toString") && (DepthCounter.isGeneratingGroovy())) {
			this.addIDRepastComponents();
		}

		for (Iterator iter = fields.iterator(); iter.hasNext();)
			((Field) iter.next()).serialize(writer);

		// static initializer
		if (staticInitializer != null)
			staticInitializer.serialize(writer);

		// constructors
		if (constructors != null) {
			for (Iterator iter = constructors.iterator(); iter.hasNext();) {
				Constructor constructor = (Constructor) iter.next();
				constructor.serialize(writer);
			}
		}

		// methods
		for (Iterator iter = methods.iterator(); iter.hasNext();)
			((Method) iter.next()).serialize(writer);

		// inner classes
		if (innerClasses != null) {
			for (Iterator iter = innerClasses.iterator(); iter.hasNext();) {
				JavaClass javaClass = (JavaClass) iter.next();
				javaClass.serialize(writer);
			}
		}

		writer.write(Util.getPlatformLineDelimiter());
		writer.write("}");
		writer.write(Util.getPlatformLineDelimiter());
		writer.write(Util.getPlatformLineDelimiter());
			
	}

	/**
	 * Returns the static initializer field. If it is null the a new instance is
	 * created and then returned
	 * 
	 * @return the static initializer field
	 */
	public CodeBlock getStaticInitializer() {
		if (staticInitializer == null)
			staticInitializer = new CodeBlock();
		return staticInitializer;
	}

	/**
	 * Sets the class's javadoc.
	 * 
	 * @see repast.simphony.agents.model.codegen.IJavadocHolder#addJavadoc(repast.simphony.agents.model.codegen.JavadocBlock)
	 */
	public void setJavadoc(JavadocBlock javadoc) {
		this.javadoc = javadoc;
	}

	/**
	 * Returns the class's javadoc
	 * 
	 * @see repast.simphony.agents.model.codegen.IJavadocHolder#getJavadoc()
	 */
	public JavadocBlock getJavadoc() {
		return javadoc;
	}

	public List getInterfaces() {
		return interfaces;
	}

	public void setInterfaces(List interfaces) {
		this.interfaces = interfaces;
	}

	@Override
	public String getComment() {
		return comment;
	}

	@Override
	public void setComment(String comment) {
		this.comment = comment;
	}

	public List getModifiers() {
		return modifiers;
	}

	public void setModifiers(List modifiers) {
		this.modifiers = modifiers;
	}

	public String getSuperClassName() {
		return superClassName;
	}

}
