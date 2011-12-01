package repast.simphony.valueLayer;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import repast.simphony.space.grid.WrapAroundBorders;

/**
 * Tests for the grid value layer.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
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

	public void testWrappedBorders(){
		GridValueLayer grid = new GridValueLayer("Grid", 0, true, 
				new WrapAroundBorders(), 50, 50);
		
		grid.set(1.0, 50, 50);
		
		assertEquals(grid.get(50,50), 1.0);
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
	

	public static junit.framework.Test suite() {
		return new TestSuite(GridValueLayerTest.class);
	}
}
