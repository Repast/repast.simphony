/*
 * Copyright (c) 2007 by Argonne National Laboratory. All right reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer. * Redistributions in
 * binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other
 * materials provided with the distribution. * Neither the name of the Repast
 * project nor the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
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

public class DepthCounter {
	
	protected static boolean generatingGroovy = true;

	public static boolean isGeneratingGroovy() {
		return generatingGroovy;
	}

	public static void setGeneratingGroovy(boolean newGeneratingGroovy) {
		generatingGroovy = newGeneratingGroovy;
	}

	protected String comment = "";

	private static int depth = 1;

	public static boolean isNumberString(Object value) {
		if ((DepthCounter.isSet(value)) && (value instanceof String)) {
			try {
				Double.parseDouble((String) value);
				return true;
			} catch (Exception e) {
				return false;
			}
		} else {
			return false;
		}
	}

	public static boolean isSet(Object value) {
		if (value != null) {
			if (value instanceof String) {
				String valueString = ((String) value);
				if (valueString.length() > 0) {
					return true;
				} else {
					return false;
				}
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	public void writeBlanks(Writer writer) throws IOException {
		for (int i = 0; i < depth; i++)
			writer.write("    ");
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		DepthCounter.depth = depth;
	}

	public void incrementDepth() {
		DepthCounter.depth++;
	}

	public void decrementDepth() {
		DepthCounter.depth--;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
