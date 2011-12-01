package repast.simphony.space;

import junit.framework.TestCase;


public class TestDimensions extends TestCase {
	
	public void testOrigin(){
		double[] dim = {5, 5.6, 100};
		double[] origin = {0, 3.4, 50};
		
		Dimensions dimensions = new Dimensions(dim, origin);
		double[] results = dimensions.originToDoubleArray(null);
		
		assertEquals(results[0], 0.0 );
		assertEquals(results[1], 3.4 );
		assertEquals(results[2], 50.0 );
	}
	
	public void testDefaultOrigin(){
		double[] dim = {5, 5.6, 100};
		
		Dimensions dimensions = new Dimensions(dim);
		double[] results = dimensions.originToDoubleArray(null);
		
		assertEquals(results[0], 0.0 );
		assertEquals(results[1], 0.0 );
		assertEquals(results[2], 0.0 );
	}
	
	public void testDoubleDimensionIntOrigin(){
		double[] dim = {5, 5.6, 100};
		int[] origin = {0, 4, 50};
		
		Dimensions dimensions = new Dimensions(dim, origin);
		double[] results = dimensions.originToDoubleArray(null);
		
		assertEquals(results[0], 0.0 );
		assertEquals(results[1], 4.0 );
		assertEquals(results[2], 50.0 );
	}
	
}
