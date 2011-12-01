/*CopyrightHere*/
package repast.simphony.freezedry.datasource;

import junit.framework.TestCase;
import repast.simphony.freezedry.ArrayValueHandler;
import simphony.util.messages.Log4jMessageListener;

import java.io.IOException;
import java.lang.reflect.Array;

public class ArrayValueHandlerTest extends TestCase {
	protected String arrayToString(Object array) {
		int len = Array.getLength(array);
		StringBuilder builder = new StringBuilder("[");
		for (int i = 0; i < len; i++) {
			if (i != 0) {
				builder.append(", ");
			}
			builder.append(Array.get(array, i));
		}
		builder.append("]");
		
		return builder.toString();
	}
	
	public void fail(Object expected, Object result) {
		fail("expected: <" + arrayToString(expected) + "> but was: <" + arrayToString(result) + ">");
	}
	
	public void assertEqualsArray(Object expected, Object result) {
		int expectedLen = Array.getLength(expected);
		
		if (expectedLen != Array.getLength(result)) {
			fail(expected, result);
		}
		
		for (int i = 0; i < expectedLen; i++) {
			if (!Array.get(expected, i).equals(Array.get(result, i))) {
				fail(expected, result);
			}
		}
	}
	/*
	 * Test method for 'repast.simphony.freezedry.datasource.ArrayValueHandler.readArray(String, String, char)'
	 */
	public void testReadArrayDouble() {
		double[] expected = { 0d, 1.0, 2E5, -Math.PI };
		String value = "0, 1.0, 2E5, -" + Math.PI; 
		
		assertEqualsArray(expected, ArrayValueHandler.readArray(double[].class.getName(), value,
				','));
	}
	
	public void testReadArrayString() throws IOException {
		String[] expected = { "a", "bob was here", "test this\"", "hi, i'm here" };
		String value = " \"a\", \"bob was here\",\"test this\\\"\", \"hi, i'm here\"";
		
		assertEqualsArray(expected, ArrayValueHandler.readArray(String[].class.getName(), value,
				','));
	}
	
	public void testReadArrayMisc() {
		
	}

	public void testWriteArrayDouble() {
		double[] expected = { 0d, 1.2, 83, -932.108E-6, Math.E };
		assertEqualsArray(expected, ArrayValueHandler.readArray(double[].class.getName(),
				ArrayValueHandler.writeArray(expected, ','), ','));
	}

	public void testWriteArrayBigDDouble() {
		Double[] expected = { 0d, 1.2, 83.0, -932.108E-6, Math.E };
		assertEqualsArray(expected, ArrayValueHandler.readArray(Double[].class.getName(),
				ArrayValueHandler.writeArray(expected, ','), ','));
	}


	public void testWriteArrayString() {
		Log4jMessageListener.loadDefaultSettings();
		String[] expected = { "blah", "help-12,", "car\"dbo*ard\"" };
		
		assertEquals("\"blah\", \"help-12,\", \"car\\\"dbo*ard\\\"\"", ArrayValueHandler.writeArray(expected, ','));
		
		assertEqualsArray(expected, ArrayValueHandler.readArray(String[].class.getName(),
				ArrayValueHandler.writeArray(expected, ','), ','));
	}
}
