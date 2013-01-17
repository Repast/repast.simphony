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

import repast.simphony.agents.base.Util;
import repast.simphony.agents.designer.model.DecisionStepModelPart;

/**
 * Class IfThenElse TODO
 * 
 * @author agreif (Adapted by Michael J. North for Use in Repast Simphony from
 *         Alexander Greif’s Flow4J-Eclipse
 *         (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks
 *         to the Original Author)
 * 
 */
public class IfThenElse extends DepthCounter implements ISrcStructure {

	private String booleanStatement = "";
	private CodeBlock trueBlock = new CodeBlock();
	private CodeBlock falseBlock = new CodeBlock();
	private String branchType = DecisionStepModelPart.PROP_DECISION_BRANCH_TYPE_IF;

	/**
	 * Adds a code fragment to the "true" block
	 * 
	 * @param fragment
	 */
	public void addTrueFragment(ISrcStructure fragment) {
		trueBlock.addFragment(fragment);
	}

	/**
	 * Adds a code fragment to the "false" block
	 * 
	 * @param fragment
	 */
	public void addFalseFragment(ISrcStructure fragment) {
		falseBlock.addFragment(fragment);
	}

	/**
	 * Sets the boolean statement of this if statement
	 * 
	 * @param string
	 *            the if statement
	 */
	public void setBooleanStatement(String string) {
		booleanStatement = string;
	}

	/**
	 * Serializes the if then else block to the given writer
	 * 
	 * @see repast.simphony.agents.model.codegen.structure.ISrcStructure#serialize(java.io.BufferedWriter)
	 */
	public void serialize(Writer writer) throws IOException {

		writer.write(Util.getPlatformLineDelimiter());
		if (DepthCounter.isSet(this.comment)) {
			this.writeBlanks(writer);
			writer.write("// " + this.comment);
		} else {
			this.writeBlanks(writer);
			writer.write("// Make a decision.");
		}
		writer.write(Util.getPlatformLineDelimiter());

		this.writeBlanks(writer);
		if (this.branchType
				.equals(DecisionStepModelPart.PROP_DECISION_BRANCH_TYPE_IF)) {

			writer.write("if (" + booleanStatement + ")");

			this.incrementDepth();
			trueBlock.setEndWithNewLine(false);
			trueBlock.serialize(writer);
			this.decrementDepth();

			writer.write(" else ");

			this.incrementDepth();
			falseBlock.serialize(writer);
			this.decrementDepth();

		} else {

			boolean useWhileStatement = true;

			int inIndex = booleanStatement.indexOf(" in ");
			if (inIndex >= 0) {
				if ((booleanStatement.substring(0, inIndex).indexOf("\"") < 0)
						&& (booleanStatement.substring(0, inIndex).indexOf("{") < 0)) {
					useWhileStatement = false;
				}
				if ((booleanStatement.substring(0, inIndex).indexOf("\"") < 0)
						&& (booleanStatement.substring(0, inIndex).indexOf("{") < 0)) {
					useWhileStatement = false;
				}
			}
			inIndex = booleanStatement.indexOf(";");
			if (inIndex >= 0) {
				if ((booleanStatement.substring(0, inIndex).indexOf("\"") < 0)
						&& (booleanStatement.substring(0, inIndex).indexOf("{") < 0)) {
					useWhileStatement = false;
				}
				if ((booleanStatement.substring(0, inIndex).indexOf("\"") < 0)
						&& (booleanStatement.substring(0, inIndex).indexOf("{") < 0)) {
					useWhileStatement = false;
				}
			}

			if (useWhileStatement) {
				writer.write("while (" + booleanStatement + ")");
			} else {
				writer.write("for (" + booleanStatement + ")");
			}

			this.incrementDepth();
			trueBlock.setEndWithNewLine(false);
			trueBlock.serialize(writer);
			this.decrementDepth();

			writer.write(Util.getPlatformLineDelimiter());
			writer.write(Util.getPlatformLineDelimiter());
			falseBlock.setEncloseBlock(false);
			falseBlock.serialize(writer);

		}

	}

	/**
	 * Returns the "false" blockof the statement
	 * 
	 * @return the "false" blockof the statement
	 */
	public CodeBlock getFalseBlock() {
		return falseBlock;
	}

	/**
	 * Returns the "true" blockof the statement
	 * 
	 * @return the "true" blockof the statement
	 */
	public CodeBlock getTrueBlock() {
		return trueBlock;
	}

	public String getBranchType() {
		return branchType;
	}

	public void setBranchType(String branchType) {
		this.branchType = branchType;
	}

}
