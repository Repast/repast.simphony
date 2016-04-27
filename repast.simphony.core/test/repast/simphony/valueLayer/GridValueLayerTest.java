package repast.simphony.valueLayer;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import repast.simphony.space.grid.WrapAroundBorders;

/**
 * Tests for the grid value layer.
 *
 * @author Nick Collier
 *
 */
public class GridValueLayerTest extends TestCase {

	public void testDefaultVal() {
		GridValueLayer grid = new GridValueLayer("Grid", true, 3, 3);
		for (int x = 0; x < grid.getDimensions().getWidth(); x++) {
			for (int y = 0; y < grid.getDimensions().getHeight(); y++) {
				assertEquals(0.0, grid.get(x, y));
			}
		}

		double defVal = 3.143;
		grid = new GridValueLayer("Grid", defVal, true, 3, 3);
		for (int x = 0; x < grid.getDimensions().getWidth(); x++) {
			for (int y = 0; y < grid.getDimensions().getHeight(); y++) {
				assertEquals(defVal, grid.get(x, y));
			}
		}
	}

	public void testVals() {
		GridValueLayer grid = new GridValueLayer("Grid", true, 20, 10, 3, 4);
		double i = 0;
		for (int x = 0; x < grid.getDimensions().getWidth(); x++) {
			for (int y = 0; y < grid.getDimensions().getHeight(); y++) {
				for (int z = 0; z < grid.getDimensions().getDepth(); z++) {
					for (int n = 0; n < grid.getDimensions().getDimension(3); n++) {
						grid.set(i++, x, y, z, n);
					}
				}
			}
		}

		i = 0;
		for (int x = 0; x < grid.getDimensions().getWidth(); x++) {
			for (int y = 0; y < grid.getDimensions().getHeight(); y++) {
				for (int z = 0; z < grid.getDimensions().getDepth(); z++) {
					for (int n = 0; n < grid.getDimensions().getDimension(3); n++) {
						assertEquals(i++, grid.get(x, y, z, n));
					}
				}
			}
		}
	}
	
	public void testSparse(){
		int xdim = 1000000;
		int ydim = 1000000;
		double defaultValue = 21;
		GridValueLayer grid = new GridValueLayer("Grid", defaultValue, false, xdim, ydim);
		
		grid.set(1.0, 50, 50);
		grid.set(2.0, 250, 50);
		grid.set(3.0, 50, 500000);
		grid.set(4.0, 100000, 100000);
		grid.set(5.0, 999999, 999999);
		grid.set(6.0, 500000, 5);
		grid.set(7.0, 500000, 500000);

		assertEquals(1.0,grid.get(50, 50));
		assertEquals(2.0,grid.get(250, 50));
		assertEquals(3.0,grid.get(50, 500000));
		assertEquals(4.0,grid.get(100000, 100000));
		assertEquals(5.0,grid.get(999999, 999999));
		assertEquals(6.0,grid.get(500000, 5));
		assertEquals(7.0,grid.get(500000, 500000));
		
		// Sparse size only counts the entries
		assertEquals(7, grid.size());
		
		// Sparse entries written with the default value are not stored
	  grid.set(1.2345, 100, 100);
		grid.set(defaultValue, 100, 100);

		assertEquals(7, grid.size());
	}

	public void testWrappedBorders(){
		GridValueLayer grid = new GridValueLayer("Grid", 0, true, 
				new WrapAroundBorders(), 50, 50);
		
		grid.set(1.0, 50, 50);
		
		assertEquals(grid.get(50,50), 1.0);
		assertEquals(grid.get(0,0), 1.0);
		assertEquals(grid.get(0,1), 0.0);
	}
	
	public void testSparseWrappedBorders(){
		GridValueLayer grid = new GridValueLayer("Grid", 0, false, 
				new WrapAroundBorders(), 500000, 500000);
		
		grid.set(1.0, 500000, 500000);
		
		assertEquals(grid.get(500000,500000), 1.0);
		assertEquals(grid.get(0,0), 1.0);
		assertEquals(grid.get(0,1), 0.0);
	}
	
	public void testOrigin(){
		int[] dimensions = {50,50};
		int[] origin = {3,4};
		GridValueLayer grid = new GridValueLayer("Grid", 0, true, 
				new WrapAroundBorders(), dimensions, origin);
		
		grid.set(1.0, 0, 0);
		grid.set(2.0, -3, -4);
		grid.set(3.0, -4, -4);
		
		
		assertEquals(grid.get(0,0), 1.0);
		assertEquals(grid.get(-3,-4), 2.0);
		assertEquals(grid.get(46,-4), 3.0);
		assertEquals(grid.get(47,-4), 2.0);
		assertEquals(grid.get(0,1), 0.0);
	}
	
	public void testSparseOrigin(){
		int[] dimensions = {500000,500000};
		int[] origin = {30000,40000};
		GridValueLayer grid = new GridValueLayer("Grid", 0, false, 
				new WrapAroundBorders(), dimensions, origin);
		
		grid.set(1.0, 0, 0);
		grid.set(2.0, -30000, -40000);
		grid.set(3.0, -40000, -40000);
		
		
		assertEquals(grid.get(0,0), 1.0);
		assertEquals(grid.get(-30000,-40000), 2.0);
		assertEquals(grid.get(460000,-40000), 3.0);
		assertEquals(grid.get(470000,-40000), 2.0);
		assertEquals(grid.get(0,1), 0.0);
	}
	
	

	public static junit.framework.Test suite() {
		return new TestSuite(GridValueLayerTest.class);
	}
}
