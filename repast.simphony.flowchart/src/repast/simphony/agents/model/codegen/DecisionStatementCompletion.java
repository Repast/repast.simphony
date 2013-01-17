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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import repast.simphony.agents.flows.tasks.AgentBuilderDictionary;

/**
 * Class DecisionStatementCompletion TODO
 * 
 * @author agreif (Adapted by Michael J. North for Use in Repast Simphony from
 *         Alexander Greif’s Flow4J-Eclipse
 *         (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks
 *         to the Original Author)
 * 
 */
@SuppressWarnings("unchecked")
public class DecisionStatementCompletion {

	static final private Method[] flowDictionaryMethods = AgentBuilderDictionary.class
			.getMethods();

	/**
	 * completes the boolean statement with the given dictionary parameter name.
	 * Adds the dict param infront of the method and the dot.
	 * 
	 * @param stmnt
	 * @param dictParamName
	 * @return
	 */
	public String completeStatement(String stmnt, String dictParamName) {
		StringBuffer completedStatement = new StringBuffer();
		List descs = getFlowDictionaryMethodDescriptors(stmnt);
		int pos = 0;
		for (Iterator iter = descs.iterator(); iter.hasNext();) {
			MethodDescriptor desc = (MethodDescriptor) iter.next();
			int start = pos;
			int end = desc.getPos();
			completedStatement.append(stmnt.substring(start, end));
			completedStatement.append(dictParamName).append(".").append(
					desc.getName());
			pos = end + desc.getName().length();
		}
		completedStatement.append(stmnt.substring(pos));

		return completedStatement.toString();
	}

	private List getFlowDictionaryMethodDescriptors(String stmnt) {
		List descs = new ArrayList();
		int bracketPos = -1;
		while ((bracketPos = stmnt.indexOf("(", bracketPos + 1)) != -1) {
			String identifier = getIdentifier(stmnt, bracketPos);
			if (identifier == null)
				continue;

			if (!isFlowDictionaryMethod(identifier))
				continue;

			MethodDescriptor desc = new MethodDescriptor(identifier, bracketPos
					- identifier.length());
			descs.add(desc);
		}

		return descs;
	}

	static private boolean isFlowDictionaryMethod(String methodName) {
		for (int i = 0; i < flowDictionaryMethods.length; i++) {
			Method method = flowDictionaryMethods[i];
			if (method.getName().equals(methodName))
				return true;
		}
		return false;
	}

	static private String getIdentifier(String stmnt, int pos) {
		if (pos == 0)
			return null;

		boolean isWhitespaceCleanup = true;
		boolean identifierFound = false;

		StringBuffer buf = new StringBuffer();

		for (; pos >= 0; pos--) {
			char c = stmnt.charAt(pos);
			if (isWhitespaceCleanup && (c == ' ' || c == '\t'))
				continue;

			if (isWhitespaceCleanup)
				isWhitespaceCleanup = false;

			if (c == '.')
				return null;

			if (identifierFound)
				break;

			if (!identifierFound) {
				buf.insert(0, c);

				if (isJavaIdentifierPart(buf.toString()))
					continue;

				if (!isJavaIdentifier(buf.toString())) {
					buf.deleteCharAt(0);
					identifierFound = true;
					isWhitespaceCleanup = true;
				}
			}
		}

		return buf.length() == 0 ? null : buf.toString();
	}

	/**
	 * Returns true if s is a legal Java identifier.
	 * 
	 * @return true if s is a legal Java identifier.
	 */
	private static boolean isJavaIdentifier(String s) {
		if (s.length() == 0 || !Character.isJavaIdentifierStart(s.charAt(0))) {
			return false;
		}
		return isJavaIdentifierPart(s.substring(1));
	}

	/**
	 * Returns true if s is a legal Java identifier part.
	 * 
	 * @return true if s is a legal Java identifier part.
	 */
	private static boolean isJavaIdentifierPart(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (!Character.isJavaIdentifierPart(s.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	private class MethodDescriptor {
		/**
		 * Name of the method
		 */
		private String name;
		/**
		 * The method name's start pos in the statement
		 */
		private int pos;

		/**
		 * Creates a new instance of this Class with the given values
		 * 
		 * @param name
		 *            name of the mehtod
		 * @param pos
		 *            position where the name begins in the statement
		 */
		private MethodDescriptor(String name, int pos) {
			this.name = name;
			this.pos = pos;
		}

		/**
		 * TODO
		 * 
		 * @return
		 */
		public String getName() {
			return name;
		}

		/**
		 * TODO
		 * 
		 * @return
		 */
		public int getPos() {
			return pos;
		}

	}

	private static void checkEquals(String a, String b) {
	}

	public static void main(String[] args) {
		String s;
		DecisionStatementCompletion completion = new DecisionStatementCompletion();

		s = "s2i(\"hhh\")";
		checkEquals("dictionary.s2i(\"hhh\")", completion.completeStatement(s,
				"dictionary"));

		s = "i2s(\"hhh\")";
		checkEquals("dictionary.i2s(\"hhh\")", completion.completeStatement(s,
				"dictionary"));

		s = "\"hh\"  .  equals(\"h\")";
		checkEquals("\"hh\"  .  equals(\"h\")", completion.completeStatement(s,
				"dictionary"));

		s = "i(123) && (s(456))";
		checkEquals("dictionary.i(123) && (dictionary.s(456))", completion
				.completeStatement(s, "dictionary"));

		s = "(i())";
		checkEquals("(dictionary.i())", completion.completeStatement(s,
				"dictionary"));

		s = "  (i())";
		checkEquals("  (dictionary.i())", completion.completeStatement(s,
				"dictionary"));

		s = "  (  i())";
		checkEquals("  (  dictionary.i())", completion.completeStatement(s,
				"dictionary"));

		s = "l()";
		checkEquals("dictionary.l()", completion.completeStatement(s,
				"dictionary"));

		s = "str(123)";
		checkEquals("dictionary.str(123)", completion.completeStatement(s,
				"dictionary"));

		s = "getString(123) && l(456)";
		checkEquals("dictionary.getString(123) && dictionary.l(456)",
				completion.completeStatement(s, "dictionary"));

		s = "\"Alex\".equals((String)get(\"name\"))";
		checkEquals("\"Alex\".equals((String)dictionary.get(\"name\"))",
				completion.completeStatement(s, "dictionary"));

		s = "containsKey(\"hhh\")";
		checkEquals("dictionary.containsKey(\"hhh\")", completion
				.completeStatement(s, "dictionary"));

		// negative tests

		s = "ccontainsKey(\"hhh\")";
		checkEquals("ccontainsKey(\"hhh\")", completion.completeStatement(s,
				"dictionary"));

	}

}
