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
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.util.ClassUtilities;
import repast.simphony.util.SimUtilities;

import java.io.IOException;
import java.util.List;

/**
 * Tests for the DataSetHandler class.
 * 
 * @author Jerry Vos
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:26:02 $
 */
public class UtilitiesTest extends TestCase {

	// TODO: implement this
	// public void testFindMethod() {
	// }

	/*
	public void testInsertTimeVarToString() {
		String baseString = "asdf";
		String expectedString = "asdf.${" + FileOutputter.TIME_VAR + "}";
		assertEquals(expectedString, Utilities
				.insertTimeVarToString(baseString));

		baseString = "asdf.txt";
		expectedString = "asdf.${" + FileOutputter.TIME_VAR + "}.txt";
		assertEquals(expectedString, Utilities
				.insertTimeVarToString(baseString));

		baseString = "./";
		expectedString = "./.${" + FileOutputter.TIME_VAR + "}";
		assertEquals(expectedString, Utilities
				.insertTimeVarToString(baseString));

		baseString = "../";
		expectedString = "../.${" + FileOutputter.TIME_VAR + "}";
		assertEquals(expectedString, Utilities
				.insertTimeVarToString(baseString));

		baseString = "../asdf";
		expectedString = "../asdf.${" + FileOutputter.TIME_VAR + "}";
		assertEquals(expectedString, Utilities
				.insertTimeVarToString(baseString));

		baseString = "../${Repast}.asdf.txt";
		expectedString = "../${Repast}.asdf.${" + FileOutputter.TIME_VAR
				+ "}.txt";
		assertEquals(expectedString, Utilities
				.insertTimeVarToString(baseString));
		
		baseString = "../${Repast}.$" + FileOutputter.TIME_VAR + "}.asdf.txt";
		expectedString = "../${Repast}.$" + FileOutputter.TIME_VAR + "}.asdf.txt";
		assertEquals(expectedString, Utilities
				.insertTimeVarToString(baseString));
	}
	*/

	interface IntWithAnnotation {
		@ScheduledMethod
		void methodWithAnnotation();
	}

	interface IntWithoutAnnotation {
		void methodWithoutAnnotation();
	}

	class ClassWithAnnotation {
		@ScheduledMethod
		public void methodWithAnnotation() { }
	}

	class ClassWithOutAnnotation {
		public void methodWithoutAnnotation() { }
	}

	class ClassWithAnnotatedInterface implements IntWithAnnotation {
		public void methodWithAnnotation() { }
	}

	class ClassWithoutAnnotatedInterface implements IntWithoutAnnotation {
		public void methodWithoutAnnotation() { }
	}

	class SubClassWithAnnotation extends ClassWithAnnotatedInterface {
		@Override
		public void methodWithAnnotation() { }
	}

	public void testDeepAnnotationCheck() throws SecurityException, NoSuchMethodException {
		assertEquals(ClassUtilities.deepAnnotationCheck(IntWithAnnotation.class
				.getMethod("methodWithAnnotation"), ScheduledMethod.class),
				IntWithAnnotation.class.getMethod("methodWithAnnotation")
						.getAnnotation(ScheduledMethod.class));

		assertNull(ClassUtilities.deepAnnotationCheck(IntWithoutAnnotation.class
				.getMethod("methodWithoutAnnotation"), ScheduledMethod.class));

		assertEquals(ClassWithAnnotation.class.getMethod("methodWithAnnotation")
				.getAnnotation(ScheduledMethod.class),
				ClassUtilities.deepAnnotationCheck(ClassWithAnnotation.class
				.getMethod("methodWithAnnotation"), ScheduledMethod.class));

		assertNull(ClassUtilities.deepAnnotationCheck(ClassWithOutAnnotation.class
				.getMethod("methodWithoutAnnotation"), ScheduledMethod.class));

		assertEquals(IntWithAnnotation.class.getMethod("methodWithAnnotation")
				.getAnnotation(ScheduledMethod.class),
				ClassUtilities.deepAnnotationCheck(ClassWithAnnotatedInterface.class
				.getMethod("methodWithAnnotation"), ScheduledMethod.class));

		assertNull(ClassUtilities.deepAnnotationCheck(ClassWithoutAnnotatedInterface.class
				.getMethod("methodWithoutAnnotation"), ScheduledMethod.class));

		assertEquals(IntWithAnnotation.class.getMethod("methodWithAnnotation")
				.getAnnotation(ScheduledMethod.class),
				ClassUtilities.deepAnnotationCheck(SubClassWithAnnotation.class
				.getMethod("methodWithAnnotation"), ScheduledMethod.class));
	}

	public void testScale() {
		assertEquals(0.0000001, SimUtilities.scale(0.00000009, 7));

		assertEquals(0.0000001, SimUtilities.scale(0.0000001, 7));

		assertEquals(0.0000000, SimUtilities.scale(0.00000001, 7));
	}

	public void testGetClasses() {
		try {
			List<Class> classes = ClassUtilities.getClasses("../repast.test.models/bin");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
