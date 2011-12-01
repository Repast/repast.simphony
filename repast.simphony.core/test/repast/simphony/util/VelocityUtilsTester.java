/*$$
 * Copyright (c) 2007, Argonne National Laboratory
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with 
 * or without modification, are permitted provided that the following 
 * conditions are met:
 *
 *	 Redistributions of source code must retain the above copyright notice,
 *	 this list of conditions and the following disclaimer.
 *
 *	 Redistributions in binary form must reproduce the above copyright notice,
 *	 this list of conditions and the following disclaimer in the documentation
 *	 and/or other materials provided with the distribution.
 *
 * Neither the name of the Repast project nor the names the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE TRUSTEES OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *$$*/
package repast.simphony.util;

import junit.framework.TestCase;
import org.apache.velocity.VelocityContext;
import repast.simphony.TestUtils;
import repast.simphony.util.VelocityUtils;

import java.util.ArrayList;

/**
 * Tests for the DataSetHandler class.
 * 
 * @author Jerry Vos
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:26:02 $
 */
public class VelocityUtilsTester extends TestCase {

	public void testEvaluate() {
		String baseString = null;
		String expectedResultString = null;

		VelocityContext context = new VelocityContext();
		baseString = "$Var1";
		context.put("Var1", new Integer(999));
		expectedResultString = "999";
		assertEquals(expectedResultString, VelocityUtils.evaluate(context,
				baseString, "errorString"));

		context = new VelocityContext();
		baseString = "${Var1}";
		context.put("Var1", new Integer(999));
		expectedResultString = "999";
		assertEquals(expectedResultString, VelocityUtils.evaluate(context,
				baseString, "errorString"));

		context = new VelocityContext();
		baseString = "${Var1}, $Repast";
		context.put("Var1", new Integer(999));
		context.put("Repast", "rpst");
		expectedResultString = "999, rpst";
		assertEquals(expectedResultString, VelocityUtils.evaluate(context,
				baseString, "errorString"));
	}

	public void testEvaluateError() {
		assertEquals("errorString", VelocityUtils.evaluate(
				new VelocityContext(), null, "errorString"));
	}

	public void testGetTemplateVarNames() {
		ArrayList<String> expected = new ArrayList<String>();
		String varString = "${Var1}";
		expected.add("Var1");
		assertTrue(TestUtils.collectionsContentsEqual(expected, VelocityUtils
				.getTemplateVarNames(varString)));

		expected = new ArrayList<String>();
		varString = "$Var1";
		expected.add("Var1");
		assertTrue(TestUtils.collectionsContentsEqual(expected, VelocityUtils
				.getTemplateVarNames(varString)));

		expected = new ArrayList<String>();
		varString = "$Var1, ${repast}";
		expected.add("Var1");
		expected.add("repast");
		assertTrue(TestUtils.collectionsContentsEqual(expected, VelocityUtils
				.getTemplateVarNames(varString)));
	}

}
