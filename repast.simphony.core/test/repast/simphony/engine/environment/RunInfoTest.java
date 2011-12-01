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
package repast.simphony.engine.environment;

import junit.framework.TestCase;
import junitx.util.PrivateAccessor;
import repast.simphony.engine.environment.RunInfo;

/**
 * Tests for the DataSetHandler class.
 * 
 * @author Jerry Vos
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:35 $
 */
public class RunInfoTest extends TestCase {

	public void testRunInfo() throws NoSuchFieldException {
		RunInfo runInfo = new RunInfo("My model", 123, 321);
		
		assertEquals("My model", PrivateAccessor.getField(runInfo, "modelName"));
		
		assertEquals(123, PrivateAccessor.getField(runInfo, "batchNumber"));
		
		assertEquals(321, PrivateAccessor.getField(runInfo, "runNumber"));
	}

	public void testRunCopy() throws NoSuchFieldException {
		RunInfo runInfo = new RunInfo("My model", 123, 321);
		RunInfo copy = new RunInfo(runInfo);
		
		//		this(base.model, base.modelName, base.schedule, base.runNumber,
		//base.subRunNumber);
		
		assertEquals(PrivateAccessor.getField(runInfo, "batchNumber"), PrivateAccessor.getField(copy, "batchNumber"));
		assertEquals(PrivateAccessor.getField(runInfo, "runNumber"), PrivateAccessor.getField(copy, "runNumber"));
		assertEquals(PrivateAccessor.getField(runInfo, "modelName"), PrivateAccessor.getField(copy, "modelName"));
	}	
	
	public void testGetModelName() {
		RunInfo runInfo = new RunInfo("My model", 123, 321);
		
		assertEquals("My model", runInfo.getModelName());
	}

	public void testGetBatchNumber() {
		RunInfo runInfo = new RunInfo("My model", 123, 321);
		
		assertEquals(123, runInfo.getBatchNumber());
	}

	public void testGetRunNumber() {
		RunInfo runInfo = new RunInfo("My model", 123, 321);
		
		assertEquals(321, runInfo.getRunNumber());
	}

	public void testEquals() {
		RunInfo runInfo = new RunInfo( "My model", 123, 321);
		
		assertFalse(runInfo.equals(new RunInfo("model", 123, 321)));
		assertFalse(runInfo.equals(new RunInfo("My model", 111, 321)));
		assertFalse(runInfo.equals(new RunInfo("My model", 123, 333)));
		
		assertFalse(runInfo.equals(new Object()));
		
		assertTrue(runInfo.equals(new RunInfo("My model", 123, 321)));
	}
	
	public void testHashCode() {
		RunInfo runInfo1 = new RunInfo("My model", 123, 321);
		RunInfo runInfo2 = new RunInfo("My model2", 123, 321);
		RunInfo runInfo3 = new RunInfo("My model", 123, 321);
		
		assertTrue(runInfo1.hashCode() != runInfo2.hashCode());
		assertTrue(runInfo1.hashCode() == runInfo3.hashCode());
	}
}
