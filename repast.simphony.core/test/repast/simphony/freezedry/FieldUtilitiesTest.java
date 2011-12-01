/*CopyrightHere*/
package repast.simphony.freezedry;

import junit.framework.TestCase;
import repast.simphony.freezedry.FieldUtilities;

import java.lang.reflect.Field;

public class FieldUtilitiesTest extends TestCase {
	FieldUtilities util;
	
	class Prims {
		int INT;
		long LONG;
		double DOUBLE;
		float FLOAT;
		char CHAR;
		byte BYTE;
		short SHORT;

		Integer BINT;
		Long BLONG;
		Double BDOUBLE;
		Float BFLOAT;
		Character BCHAR;
		Byte BBYTE;
		Short BSHORT;
	}
	class PrimsArray {
		int[] INT;
		long[] LONG;
		double[] DOUBLE;
		float[] FLOAT;
		char[] CHAR;
		byte[] BYTE;
		short[] SHORT;

		Integer[] BINT;
		Long[] BLONG;
		Double[] BDOUBLE;
		Float[] BFLOAT;
		Character[] BCHAR;
		Byte[] BBYTE;
		Short[] BSHORT;
	}
	
	
	protected void setUp() throws Exception {
		util = FieldUtilities.INSTANCE;
	}

	void assertPrim(Class<?> clazz) {
		assertTrue(util.isPrimitive(clazz));
		assertTrue(util.isPrimitive(clazz.getName()));
	}

	void assertPrimArray(Class<?> clazz) {
		assertTrue(util.isPrimitive(clazz));
		assertTrue(util.isPrimitive(clazz.getName()));
	}
	
	public void testIsPrimitiveField() {
		for (Field field : Prims.class.getFields()) {
			assertTrue(util.isPrimitive(field));
		}
	}

	public void testIsPrimitiveClassString() {
		for (Field field : Prims.class.getFields()) {
			assertPrim(field.getClass());
		}
	}

	public void testIsPrimitiveArrayClassString() {
		for (Field field : PrimsArray.class.getFields()) {
			assertPrimArray(field.getClass());
		}
	}
	
	public void testIsPrimitiveArrayField() {
		for (Field field : PrimsArray.class.getFields()) {
			assertTrue(util.isPrimitiveArray(field));
		}
	}
	
	public void testGetClassFromString() throws ClassNotFoundException {
		for (Field field : PrimsArray.class.getFields()) {
			assertEquals(field.getType(), util.getClassFromString(util.getTypeAsString(field
					.getType())));
		}
	}
}
