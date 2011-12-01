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

import org.eclipse.jdt.core.dom.ThisExpression;

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
public class Method extends DepthCounter implements ISrcStructure {

	private String returnType = "void";
	private List extraAnnotations = new ArrayList();
	private List modifiers = new ArrayList();
	private List methodParameters = new ArrayList();
	private List throwsExceptionTypes;
	private CodeBlock codeBlock = new CodeBlock();
	private String displayName = "";

	private String scheduleAnnotationStart = "";
	private String scheduleAnnotationPick = "";
	private String scheduleAnnotationInterval = "";
	private String scheduleAnnotationPriority = "";
	private String scheduleAnnotationDuration = "";
	private String scheduleAnnotationShuffle = "0";

	private String watchAnnotationId = "";
	private String watchAnnotationQuery = "";
	private String watchAnnotationTargetClassName = "";
	private String watchAnnotationTargetFieldNames = "";
	private String watchAnnotationTriggerCondition = "";
	private String watchAnnotationTriggerSchedule = "0";
	private String watchAnnotationTriggerDelta = "";
	private String watchAnnotationTriggerPriority = "";
	private String watchAnnotationPick = "";

	private String comment = "This is the step behavior.";
	private String visibility = "0";
	private String compiledName = "step";
	private String parameters = "";

	/**
	 * Creates a Method instance with the given method name
	 * 
	 * @param methodName
	 *            the name of the method
	 */
	public Method(String compiledName) {
		super();
		this.compiledName = compiledName;
		this.codeBlock.setEncloseBlock(false);
	}

	/**
	 * TODO
	 * 
	 * @return
	 */
	public String getName() {
		return compiledName;
	}

	/**
	 * TODO
	 * 
	 * @return
	 */
	public void addToCodeBlock(ISrcStructure fragment) {
		codeBlock.addFragment(fragment);
	}

	/**
	 * TODO
	 * 
	 * @param returnType
	 */
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	/**
	 * Adds a parameter to this method
	 * 
	 * @param parameter
	 *            the parameter containing type and name
	 */
	public void addMethodParameter(MethodParameter parameter) {
		methodParameters.add(parameter);
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
	 * Adds a modifier to the method.
	 * 
	 * @param modifier
	 *            the modifier
	 */
	public void addExtraAnnotation(String annotation) {
		extraAnnotations.add(annotation);
	}

	/**
	 * Adds an inner class.
	 * 
	 * @param innerClass
	 *            the inner class to add
	 */
	public void addInnerClass(JavaClass innerClass) {
		addToCodeBlock(innerClass);
	}

	/**
	 * @see repast.simphony.agents.model.codegen.structure.ISrcStructure#serialize()
	 */
	public void serialize(Writer writer) throws IOException {

		this.writeBlanks(writer);
		writer.write("/**");
		writer.write(Util.getPlatformLineDelimiter());

		this.writeBlanks(writer);
		writer.write(" *");
		writer.write(Util.getPlatformLineDelimiter());

		this.writeBlanks(writer);
		if (DepthCounter.isGeneratingGroovy()) {
			
			if (DepthCounter.isSet(this.comment)) {
				writer.write(" * " + this.comment);
			} else if (DepthCounter.isSet(this.displayName)) {
				writer.write(" * This is the " + this.displayName + " field.");
			} else {
				writer.write(" * This is the " + this.compiledName + " field.");
			}
			writer.write(Util.getPlatformLineDelimiter());
			
		} else {

			this.writeBlanks(writer);
			writer.write("<li>There is a method named \"" + this.compiledName + "\" method has visibility ");

			this.writeBlanks(writer);
			for (Iterator iter = modifiers.iterator(); iter.hasNext();) {
				writer.write("\"" + (String) iter.next() + "\"");
			}

			if (DepthCounter.isSet(this.returnType)) {
				if (this.returnType.trim().equals("<Constructor>")) {
					writer.write(" is a constructor ");
				} else {
					writer
							.write(" has return type \"" + this.returnType
									+ "\"");
				}
			}

			if (this.methodParameters.size() > 0) {
				writer.write(" and parameters [");
				for (Iterator iter = this.methodParameters.iterator(); iter
						.hasNext();) {
					((MethodParameter) iter.next()).serialize(writer);
					if (iter.hasNext())
						writer.write(", ");
				}
				writer.write("]");
			}

			// throwsExceptionTypes
			if (throwsExceptionTypes != null) {
				writer.write(" and throws ");
				for (Iterator iter = throwsExceptionTypes.iterator(); iter
						.hasNext();) {
					writer.write(((String) iter.next()).trim());
					if (iter.hasNext())
						writer.write(", ");
				}
			}
			writer.write(". ");

			if (DepthCounter.isSet(this.comment)) {
				writer.write(this.comment);
			}
			if (DepthCounter.isSet(this.displayName)) {
				writer.write(" The block is tagged with the name \"" + this.displayName + "\".");
			}
			writer.write("</li>");
			writer.write(Util.getPlatformLineDelimiter());
			
		}

		if (DepthCounter.isGeneratingGroovy()) {
			this.writeBlanks(writer);
			writer.write(" * @method " + this.compiledName);
			writer.write(Util.getPlatformLineDelimiter());
		}

		this.writeBlanks(writer);
		writer.write(" *");
		writer.write(Util.getPlatformLineDelimiter());

		this.writeBlanks(writer);
		writer.write(" */");
		writer.write(Util.getPlatformLineDelimiter());

		List<String> annotationParameterList = new ArrayList<String>();

		if (DepthCounter.isSet(this.scheduleAnnotationStart)) {

			if (Method.isSetWithGroovyMathMap(this.scheduleAnnotationStart)) {
				if (DepthCounter.isNumberString(this.scheduleAnnotationStart)) {
					annotationParameterList.add("start = "
							+ this.scheduleAnnotationStart + "d");
				} else {
					annotationParameterList.add("start = "
							+ mapForGroovyMath(this.scheduleAnnotationStart));
				}
			}
			if (Method.isSetWithGroovyMathMap(this.scheduleAnnotationPick)) {
				if (DepthCounter.isNumberString(this.scheduleAnnotationPick)) {
					annotationParameterList.add("pick = "
							+ this.scheduleAnnotationPick + "l");
				} else {
					annotationParameterList.add("pick = "
							+ mapForGroovyMath(this.scheduleAnnotationPick));
				}
			}
			if (Method.isSetWithGroovyMathMap(this.scheduleAnnotationInterval)) {
				if (DepthCounter
						.isNumberString(this.scheduleAnnotationInterval)) {
					annotationParameterList.add("interval = "
							+ this.scheduleAnnotationInterval + "d");
				} else {
					annotationParameterList
							.add("interval = "
									+ mapForGroovyMath(this.scheduleAnnotationInterval));
				}
			}
			if (Method.isSetWithGroovyMathMap(this.scheduleAnnotationPriority)) {
				if (DepthCounter
						.isNumberString(this.scheduleAnnotationPriority)) {
					annotationParameterList.add("priority = "
							+ this.scheduleAnnotationPriority + "d");
				} else {
					annotationParameterList
							.add("priority = "
									+ mapForGroovyMath(this.scheduleAnnotationPriority));
				}
			}
			if (Method.isSetWithGroovyMathMap(this.scheduleAnnotationDuration)) {
				if (DepthCounter
						.isNumberString(this.scheduleAnnotationDuration)) {
					annotationParameterList.add("duration = "
							+ scheduleAnnotationDuration + "d");
				} else {
					annotationParameterList
							.add("duration = "
									+ mapForGroovyMath(this.scheduleAnnotationDuration));
				}
			}
			if (DepthCounter.isSet(this.scheduleAnnotationShuffle)) {
				String shuffle = "true";
				if (this.scheduleAnnotationShuffle.equals("1")) {
					shuffle = "false";
				}
				annotationParameterList.add("shuffle = " + shuffle);
			}
			if (annotationParameterList.size() > 0) {

				this.writeBlanks(writer);
				if (DepthCounter.isGeneratingGroovy()) {
					writer.write("@ScheduledMethod(");
				} else {
					writer.write("<BLOCKQUOTE>The " + this.compiledName
							+ " method is statically scheduled as follows:<BLOCKQUOTE>");
				}
				writer.write(Util.getPlatformLineDelimiter());

				this.incrementDepth();

				String annotationParameter;
				Iterator<String> it = annotationParameterList.iterator();
				while (it.hasNext()) {

					annotationParameter = it.next();
					this.writeBlanks(writer);
					if (!DepthCounter.isGeneratingGroovy())
						writer.write("<li>");
					writer.write(annotationParameter);
					if (it.hasNext()) {
						if (DepthCounter.isGeneratingGroovy())
							writer.write(",");
					}
					if (!DepthCounter.isGeneratingGroovy())
						writer.write("</li>");
					writer.write(Util.getPlatformLineDelimiter());

				}

				this.decrementDepth();

				this.writeBlanks(writer);
				if (DepthCounter.isGeneratingGroovy()) {
					writer.write(")");
				} else {
					writer.write("</BLOCKQUOTE></BLOCKQUOTE>");
				}
				writer.write(Util.getPlatformLineDelimiter());

			}

		}

		if (DepthCounter.isSet(this.watchAnnotationTargetClassName)) {

			annotationParameterList.clear();
			if (DepthCounter.isSet(this.watchAnnotationId)) {
				annotationParameterList.add("id = '" + this.watchAnnotationId
						+ "'");
			}
			if (DepthCounter.isSet(this.watchAnnotationTargetClassName)) {
				annotationParameterList.add("watcheeClassName = '"
						+ this.watchAnnotationTargetClassName + "'");
			}
			if (DepthCounter.isSet(this.watchAnnotationTargetFieldNames)) {
				annotationParameterList.add("watcheeFieldNames = '"
						+ this.watchAnnotationTargetFieldNames + "'");
			}
			if (DepthCounter.isSet(this.watchAnnotationQuery)) {
				annotationParameterList.add("query = '"
						+ mapForGroovyStrings(this.watchAnnotationQuery) + "'");
			}
			if (DepthCounter.isSet(this.watchAnnotationTriggerCondition)) {
				annotationParameterList
						.add("triggerCondition = '"
								+ mapForGroovyStrings(this.watchAnnotationTriggerCondition)
								+ "'");
			}
			if (DepthCounter.isSet(this.watchAnnotationTriggerSchedule)) {
				String whenToTrigger = "WatcherTriggerSchedule.LATER";
				if (this.watchAnnotationTriggerSchedule.equals("0")) {
					whenToTrigger = "WatcherTriggerSchedule.IMMEDIATE";
				}
				annotationParameterList.add("whenToTrigger = " + whenToTrigger);
			}
			if (Method.isSetWithGroovyMathMap(this.watchAnnotationTriggerDelta)) {
				if (DepthCounter
						.isNumberString(this.watchAnnotationTriggerDelta)) {
					annotationParameterList.add("scheduleTriggerDelta = "
							+ this.watchAnnotationTriggerDelta + "d");
				} else {
					annotationParameterList
							.add("scheduleTriggerDelta = "
									+ mapForGroovyMath(this.watchAnnotationTriggerDelta));
				}
			}
			if (Method
					.isSetWithGroovyMathMap(this.watchAnnotationTriggerPriority)) {
				if (DepthCounter
						.isNumberString(this.watchAnnotationTriggerPriority)) {
					annotationParameterList.add("scheduleTriggerPriority = "
							+ this.watchAnnotationTriggerPriority + "d");
				} else {
					annotationParameterList
							.add("scheduleTriggerPriority = "
									+ mapForGroovyMath(this.watchAnnotationTriggerPriority));
				}
			}
			if (annotationParameterList.size() > 0) {

				this.writeBlanks(writer);
				if (DepthCounter.isGeneratingGroovy()) {
					writer.write("@Watch(");
				} else {
					writer.write("<BLOCKQUOTE>The " + this.compiledName
							+ " method will trigger as follows:<BLOCKQUOTE>");
				}
				writer.write(Util.getPlatformLineDelimiter());

				this.incrementDepth();

				String annotationParameter;
				Iterator<String> it = annotationParameterList.iterator();
				while (it.hasNext()) {

					annotationParameter = it.next();
					this.writeBlanks(writer);
					if (!DepthCounter.isGeneratingGroovy())
						writer.write("<li>");
					writer.write(annotationParameter);
					if (it.hasNext()) {
						if (DepthCounter.isGeneratingGroovy())
							writer.write(",");
					}
					if (!DepthCounter.isGeneratingGroovy())
						writer.write("</li>");
					writer.write(Util.getPlatformLineDelimiter());

				}

				this.decrementDepth();

				this.writeBlanks(writer);
				if (DepthCounter.isGeneratingGroovy()) {
					writer.write(")");
				} else {
					writer.write("</BLOCKQUOTE></BLOCKQUOTE>");
				}
				writer.write(Util.getPlatformLineDelimiter());

			}

		}

		for (Iterator iter = this.extraAnnotations.iterator(); iter.hasNext();) {
			this.writeBlanks(writer);
			writer.write((String) iter.next());
			writer.write(Util.getPlatformLineDelimiter());
		}

		if (DepthCounter.isGeneratingGroovy()) {

			this.writeBlanks(writer);
			for (Iterator iter = modifiers.iterator(); iter.hasNext();) {
				writer.write((String) iter.next() + " ");
			}

			String tempReturnType = "def";
			if (DepthCounter.isSet(this.returnType)) {
				if (this.returnType.trim().equals("<Constructor>")) {
					tempReturnType = "";
				} else {
					tempReturnType = this.returnType;
				}
			}

			if (this.methodParameters.size() == 0) {
				if (tempReturnType.equals("")) {
					writer.write(compiledName + "()");
				} else {
					writer.write(tempReturnType + " " + compiledName + "()");
				}
			} else {
				if (tempReturnType.equals("")) {
					writer.write(compiledName + "(");
				} else {
					writer.write(tempReturnType + " " + compiledName + "(");
				}
				for (Iterator iter = this.methodParameters.iterator(); iter
						.hasNext();) {
					((MethodParameter) iter.next()).serialize(writer);
					if (iter.hasNext())
						writer.write(", ");
				}
				writer.write(")");
			}

			// throwsExceptionTypes
			if (throwsExceptionTypes != null) {
				writer.write(" throws ");
				for (Iterator iter = throwsExceptionTypes.iterator(); iter
						.hasNext();) {
					writer.write(((String) iter.next()).trim());
					if (iter.hasNext())
						writer.write(", ");
				}
			}

		}

		writer.write(" {");

		if (!DepthCounter.isGeneratingGroovy()) {
			writer.write("The code for the \"" + this.compiledName + "\" method is as follows:<BLOCKQUOTE><CODE>");
			writer.write(Util.getPlatformLineDelimiter());
		}
		
		writer.write(Util.getPlatformLineDelimiter());
		writer.write(Util.getPlatformLineDelimiter());
		this.incrementDepth();

		if ((DepthCounter.isSet(this.returnType))
				&& (!this.returnType.equals("<Constructor>"))) {
			
			if (!this.returnType.equals("void")) {
				this.writeBlanks(writer);
				writer.write("// Define the return value variable.");
				writer.write(Util.getPlatformLineDelimiter());
	
				this.writeBlanks(writer);
				writer.write("def returnValue");
				writer.write(Util.getPlatformLineDelimiter());
				writer.write(Util.getPlatformLineDelimiter());

			}
			
			this.writeBlanks(writer);
			writer.write("// Note the simulation time.");
			writer.write(Util.getPlatformLineDelimiter());
	
			this.writeBlanks(writer);
			writer.write("def time = GetTickCountInTimeUnits()");
			writer.write(Util.getPlatformLineDelimiter());
			writer.write(Util.getPlatformLineDelimiter());

		}

		// code block
		codeBlock.serialize(writer);

		if ((DepthCounter.isSet(this.returnType))
				&& (!this.returnType.equals("void"))
				&& (!this.returnType.equals("<Constructor>"))) {

			this.writeBlanks(writer);
			writer.write("// Return the results.");
			writer.write(Util.getPlatformLineDelimiter());
			this.writeBlanks(writer);

			writer.write("return returnValue");

			writer.write(Util.getPlatformLineDelimiter());
			writer.write(Util.getPlatformLineDelimiter());

		}

		this.decrementDepth();
		this.writeBlanks(writer);
		writer.write("}");
		
		if (!DepthCounter.isGeneratingGroovy()) {
			writer.write("</BLOCKQUOTE></CODE>");
			writer.write(Util.getPlatformLineDelimiter());
		}
		
		writer.write(Util.getPlatformLineDelimiter());
		writer.write(Util.getPlatformLineDelimiter());

	}

	/**
	 * Returns the code block of this method
	 * 
	 * @return the code block of this method
	 */
	public CodeBlock getCodeBlock() {
		return codeBlock;
	}

	public void addThrowsExceptionType(String type) {
		if (throwsExceptionTypes == null)
			throwsExceptionTypes = new ArrayList();

		throwsExceptionTypes.add(type);
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public List getExtraAnnotations() {
		return extraAnnotations;
	}

	public void setExtraAnnotations(List extraAnnotations) {
		this.extraAnnotations = extraAnnotations;
	}

	public List getModifiers() {
		return modifiers;
	}

	public void setModifiers(List modifiers) {
		this.modifiers = modifiers;
	}

	public List getMethodParameters() {
		return methodParameters;
	}

	public void setMethodParameters(List methodParameters) {
		this.methodParameters = methodParameters;
	}

	public List getThrowsExceptionTypes() {
		return throwsExceptionTypes;
	}

	public void setThrowsExceptionTypes(List throwsExceptionTypes) {
		this.throwsExceptionTypes = throwsExceptionTypes;
	}

	public String getScheduleAnnotationStart() {
		return scheduleAnnotationStart;
	}

	public void setScheduleAnnotationStart(String scheduleAnnotationStart) {
		this.scheduleAnnotationStart = scheduleAnnotationStart;
	}

	public String getScheduleAnnotationInterval() {
		return scheduleAnnotationInterval;
	}

	public void setScheduleAnnotationInterval(String scheduleAnnotationInterval) {
		this.scheduleAnnotationInterval = scheduleAnnotationInterval;
	}

	public String getScheduleAnnotationPriority() {
		return scheduleAnnotationPriority;
	}

	public void setScheduleAnnotationPriority(String scheduleAnnotationPriority) {
		this.scheduleAnnotationPriority = scheduleAnnotationPriority;
	}

	public String getScheduleAnnotationDuration() {
		return scheduleAnnotationDuration;
	}

	public void setScheduleAnnotationDuration(String scheduleAnnotationDuration) {
		this.scheduleAnnotationDuration = scheduleAnnotationDuration;
	}

	public String getScheduleAnnotationShuffle() {
		return scheduleAnnotationShuffle;
	}

	public void setScheduleAnnotationShuffle(String scheduleAnnotationShuffle) {
		this.scheduleAnnotationShuffle = scheduleAnnotationShuffle;
	}

	public String getWatchAnnotationId() {
		return watchAnnotationId;
	}

	public void setWatchAnnotationId(String watchAnnotationId) {
		this.watchAnnotationId = watchAnnotationId;
	}

	public String getWatchAnnotationQuery() {
		return watchAnnotationQuery;
	}

	public void setWatchAnnotationQuery(String watchAnnotationQuery) {
		this.watchAnnotationQuery = watchAnnotationQuery;
	}

	public String getWatchAnnotationTargetClassName() {
		return watchAnnotationTargetClassName;
	}

	public void setWatchAnnotationTargetClassName(
			String watchAnnotationTargetClassName) {
		this.watchAnnotationTargetClassName = watchAnnotationTargetClassName;
	}

	public String getWatchAnnotationTargetFieldNames() {
		return watchAnnotationTargetFieldNames;
	}

	public void setWatchAnnotationTargetFieldNames(
			String watchAnnotationTargetFieldNames) {
		this.watchAnnotationTargetFieldNames = watchAnnotationTargetFieldNames;
	}

	public String getWatchAnnotationTriggerCondition() {
		return watchAnnotationTriggerCondition;
	}

	public void setWatchAnnotationTriggerCondition(
			String watchAnnotationTriggerCondition) {
		this.watchAnnotationTriggerCondition = watchAnnotationTriggerCondition;
	}

	public String getWatchAnnotationTriggerSchedule() {
		return watchAnnotationTriggerSchedule;
	}

	public void setWatchAnnotationTriggerSchedule(
			String watchAnnotationTriggerSchedule) {
		this.watchAnnotationTriggerSchedule = watchAnnotationTriggerSchedule;
	}

	public String getWatchAnnotationTriggerDelta() {
		return watchAnnotationTriggerDelta;
	}

	public void setWatchAnnotationTriggerDelta(
			String watchAnnotationTriggerDelta) {
		this.watchAnnotationTriggerDelta = watchAnnotationTriggerDelta;
	}

	public String getWatchAnnotationTriggerPriority() {
		return watchAnnotationTriggerPriority;
	}

	public void setWatchAnnotationTriggerPriority(
			String watchAnnotationTriggerPriority) {
		this.watchAnnotationTriggerPriority = watchAnnotationTriggerPriority;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public String getCompiledName() {
		return compiledName;
	}

	public void setCompiledName(String compiledName) {
		this.compiledName = compiledName;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setCodeBlock(CodeBlock codeBlock) {
		this.codeBlock = codeBlock;
	}

	public String getWatchAnnotationPick() {
		return watchAnnotationPick;
	}

	public void setWatchAnnotationPick(String watchAnnotationPick) {
		this.watchAnnotationPick = watchAnnotationPick;
	}

	public String getScheduleAnnotationPick() {
		return scheduleAnnotationPick;
	}

	public void setScheduleAnnotationPick(String scheduleAnnotationPick) {
		this.scheduleAnnotationPick = scheduleAnnotationPick;
	}

	// This routine compensates for Groovy's mapping of
	// numbers to BigIntegers and BigDecimals within
	// annotation assignments.
	public static String mapForGroovyMath(String value) {

		String newValue = value.trim();

		if (newValue.equals("ScheduleParameters.RANDOM_PRIORITY")) {
			newValue = null;
		} else if (newValue.equals("ScheduleParameters.FIRST_PRIORITY")) {
			newValue = "1.7976931348623157E308d";
		} else if (newValue.equals("ScheduleParameters.LAST_PRIORITY")) {
			newValue = "-1.7976931348623157E308d";
		} else if (newValue.equals("ScheduledMethod.END")) {
			newValue = null;
		} else if (newValue.equals("ScheduledMethod.ALL")) {
			newValue = "9223372036854775807l";
		} else if (newValue.equals("Long.MAX_VALUE")) {
			newValue = "9223372036854775807l";
		}

		return newValue;

	}

	public static boolean isSetWithGroovyMathMap(String value) {
		return ((DepthCounter.isSet(value)) && (Method.mapForGroovyMath(value) != null));
	}

	// This routine compensates for Groovy's mapping of
	// numbers to BigIntegers and BigDecimals within
	// annotation assignments.
	public static String mapForGroovyStrings(String value) {
		return value.replaceAll("'", "\\\\'");
	}

}
