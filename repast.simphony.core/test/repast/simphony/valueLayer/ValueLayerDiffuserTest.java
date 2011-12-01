package repast.simphony.valueLayer;

import junit.framework.TestCase;
import repast.simphony.valueLayer.GridValueLayer;
import repast.simphony.valueLayer.ValueLayerDiffuser;

public class ValueLayerDiffuserTest extends TestCase {
	GridValueLayer layer;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	public void test2D() {
		layer = new GridValueLayer("2D grid", true, 10, 10);
		
		ValueLayerDiffuser diffuser = new ValueLayerDiffuser(layer, .5, 1);
		
		double[][] expected;
		expected = new double[][] {
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 10, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
		};
		
		layer.set(10.0, 4, 4);

		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				assertEquals(expected[x][y], layer.get(x, y));
			}
		}
		
		diffuser.diffuse();
		

//		System.out.println("result:");
//		for (int x = 0; x < 10; x++) {
//			for (int y = 0; y < 10; y++) {
//				System.out.print(layer.get(x, y));
//				if (y < 9) {
//					System.out.print(", ");
//				}
//			}
//			System.out.println();
//		}

		expected	=	new	double[][]	{
				{ 0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0 },
				{ 0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0 },
				{ 0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0 },
				{ 0.0,	0.0,	0.0,	0.25,	1.0,	0.25,	0.0,	0.0,	0.0,	0 },
				{ 0.0,	0.0,	0.0,	1.0,	0.0,	1.0,	0.0,	0.0,	0.0,	0 },
				{ 0.0,	0.0,	0.0,	0.25,	1.0,	0.25,	0.0,	0.0,	0.0,	0 },
				{ 0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0 },
				{ 0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0 },
				{ 0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0 },
				{ 0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0 },
		};
		
//		System.out.println("expects:");
//		for (int x = 0; x < 10; x++) {
//			for (int y = 0; y < 10; y++) {
//				System.out.print(expected[x][y]);
//				if (y < 9) {
//					System.out.print(", ");
//				}
//			}
//			System.out.println();
//		}
		
		
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				assertEquals(expected[x][y], layer.get(x, y));
			}
		}
		
		diffuser.diffuse();
		
//		System.out.println("result:");
//		for (int x = 0; x < 10; x++) {
//			for (int y = 0; y < 10; y++) {
//				System.out.print(layer.get(x, y));
//				if (y < 9) {
//					System.out.print(", ");
//				}
//			}
//			System.out.println();
//		}
		
		double c0 = (.25/5.0 + 1.0/20.0)/2.0;
		double c1 = (2*.25/20.0 + 1.0/5.0)/2.0;
		double c2 = (.25/20.0)/2.0;
		double c5 = (2*1.0/5.0)/2.0;
		double c6 = (2*1.0/20.0 + 2*.25/5.0)/2.0;
		double c8 = (4*1.0/5.0 + 4*.25/20.0)/2.0;
		
		expected	=	new	double[][]	{
				{ 0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0 },
				{ 0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0 },
				{ 0.0,	0.0,	c2,		c0,		c1,		c0,		c2,		0.0,	0.0,	0 },
				{ 0.0,	0.0,	c0,		c5,		c6,		c5,		c0,		0.0,	0.0,	0 },
				{ 0.0,	0.0,	c1,		c6,		c8,		c6,		c1,		0.0,	0.0,	0 },
				{ 0.0,	0.0,	c0,		c5,		c6,		c5,		c0,		0.0,	0.0,	0 },
				{ 0.0,	0.0,	c2,		c0,		c1,		c0,		c2,		0.0,	0.0,	0 },
				{ 0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0 },
				{ 0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0 },
				{ 0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0 },
		};
		

//		System.out.println("expects:");
//		for (int x = 0; x < 10; x++) {
//			for (int y = 0; y < 10; y++) {
//				System.out.print(expected[x][y]);
//				if (y < 9) {
//					System.out.print(", ");
//				}
//			}
//			System.out.println();
//		}
		
		
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				assertEquals(expected[x][y], layer.get(x, y), .0000001);
			}
		}
	}
	
	public void test2DTorus() {
		layer = new GridValueLayer("2D grid", true, 10, 10);
		
		ValueLayerDiffuser diffuser = new ValueLayerDiffuser(layer, 1, 1);
		
		double[][] expected;
		expected = new double[][] {
				{ 0, 0, 0, 0, 10, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
		};
		
		layer.set(10.0, 0, 4);

		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				assertEquals(expected[x][y], layer.get(x, y));
			}
		}
		
		diffuser.diffuse();
		

//		System.out.println("result:");
//		for (int x = 0; x < 10; x++) {
//			for (int y = 0; y < 10; y++) {
//				System.out.print(layer.get(x, y));
//				if (y < 9) {
//					System.out.print(", ");
//				}
//			}
//			System.out.println();
//		}

		expected	=	new	double[][]	{
				{ 0.0,	0.0,	0.0,	2.0,	0.0,	2.0,	0.0,	0.0,	0.0,	0 },
				{ 0.0,	0.0,	0.0,	0.5,	2.0,	0.5,	0.0,	0.0,	0.0,	0 },
				{ 0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0 },
				{ 0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0 },
				{ 0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0 },
				{ 0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0 },
				{ 0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0 },
				{ 0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0 },
				{ 0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0 },
				{ 0.0,	0.0,	0.0,	0.5,	2.0,	0.5,	0.0,	0.0,	0.0,	0 },
		};
		
//		System.out.println("expects:");
//		for (int x = 0; x < 10; x++) {
//			for (int y = 0; y < 10; y++) {
//				System.out.print(expected[x][y]);
//				if (y < 9) {
//					System.out.print(", ");
//				}
//			}
//			System.out.println();
//		}
		
		
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				assertEquals(expected[x][y], layer.get(x, y));
			}
		}
		
		diffuser.diffuse();
		
//		System.out.println("result:");
//		for (int x = 0; x < 10; x++) {
//			for (int y = 0; y < 10; y++) {
//				System.out.print(layer.get(x, y));
//				if (y < 9) {
//					System.out.print(", ");
//				}
//			}
//			System.out.println();
//		}
		
		double c0 = .5/5.0 + 2.0/20.0;
		double c1 = 2.0*.5/20.0 + 2.0/5.0;
		double c2 = .5/20.0;
		double c5 = 2.0*2.0/5.0;
		double c6 = 2.0/20.0*2.0 + .5/5.0*2.0;
		double c8 = 2.0/5.0*4.0 + .5/20.0*4.0;
		
		expected	=	new	double[][]	{
				{ 0.0,	0.0,	c1,		c6,		c8,		c6,		c1,		0.0,	0.0,	0 },
				{ 0.0,	0.0,	c0,		c5,		c6,		c5,		c0,		0.0,	0.0,	0 },
				{ 0.0,	0.0,	c2,		c0,		c1,		c0,		c2,		0.0,	0.0,	0 },
				{ 0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0 },
				{ 0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0 },
				{ 0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0 },
				{ 0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0 },
				{ 0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0 },
				{ 0.0,	0.0,	c2,		c0,		c1,		c0,		c2,		0.0,	0.0,	0 },
				{ 0.0,	0.0,	c0,		c5,		c6,		c5,		c0,		0.0,	0.0,	0 },
		};
		

//		System.out.println("expects:");
//		for (int x = 0; x < 10; x++) {
//			for (int y = 0; y < 10; y++) {
//				System.out.print(expected[x][y]);
//				if (y < 9) {
//					System.out.print(", ");
//				}
//			}
//			System.out.println();
//		}
		
		
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				assertEquals(expected[x][y], layer.get(x, y), .0000001);
			}
		}
	}
	
	public void test2DNonTorus() {
		layer = new GridValueLayer("2D grid", true, 10, 10);
		
		ValueLayerDiffuser diffuser = new ValueLayerDiffuser(layer, 1, 1, false);
		
		double[][] expected;
		expected = new double[][] {
				{ 0, 0, 0, 0, 10, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
		};
		
		layer.set(10.0, 0, 4);

		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				assertEquals(expected[x][y], layer.get(x, y));
			}
		}
		
		diffuser.diffuse();
		

//		System.out.println("result:");
//		for (int x = 0; x < 10; x++) {
//			for (int y = 0; y < 10; y++) {
//				System.out.print(layer.get(x, y));
//				if (y < 9) {
//					System.out.print(", ");
//				}
//			}
//			System.out.println();
//		}

		expected	=	new	double[][]	{
				{ 0.0,	0.0,	0.0,	2.0,	0.0,	2.0,	0.0,	0.0,	0.0,	0 },
				{ 0.0,	0.0,	0.0,	0.5,	2.0,	0.5,	0.0,	0.0,	0.0,	0 },
				{ 0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0 },
				{ 0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0 },
				{ 0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0 },
				{ 0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0 },
				{ 0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0 },
				{ 0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0 },
				{ 0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0 },
				{ 0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0 },
		};
		
//		System.out.println("expects:");
//		for (int x = 0; x < 10; x++) {
//			for (int y = 0; y < 10; y++) {
//				System.out.print(expected[x][y]);
//				if (y < 9) {
//					System.out.print(", ");
//				}
//			}
//			System.out.println();
//		}
		
		
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				assertEquals(expected[x][y], layer.get(x, y));
			}
		}
		
		diffuser.diffuse();
		
//		System.out.println("result:");
//		for (int x = 0; x < 10; x++) {
//			for (int y = 0; y < 10; y++) {
//				System.out.print(layer.get(x, y));
//				if (y < 9) {
//					System.out.print(", ");
//				}
//			}
//			System.out.println();
//		}
		
		double c0 = .5/5.0 + 2.0/20.0;
		double c1 = 1.0*.5/20.0 + 2.0/5.0;
		double c2 = .5/20.0;
		double c3 = 2.0/20.0*2.0 + .5*2/5;
		double c4 = 2.0/5 + .5/20*2.0;
		double c5 = 2.0*2.0/5.0;
		double c6 = 2.0/20.0*1.0 + .5/5.0*1.0;
		double c8 = 2.0/5.0*3.0 + .5/20.0*2.0;
		
		expected	=	new	double[][]	{
				{ 0.0,	0.0,	c1,		c6,		c8,		c6,		c1,		0.0,	0.0,	0 },
				{ 0.0,	0.0,	c0,		c5,		c3,		c5,		c0,		0.0,	0.0,	0 },
				{ 0.0,	0.0,	c2,		c0,		c4,		c0,		c2,		0.0,	0.0,	0 },
				{ 0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0 },
				{ 0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0 },
				{ 0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0 },
				{ 0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0 },
				{ 0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0 },
				{ 0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0 },
				{ 0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0 },
		};
		

//		System.out.println("expects:");
//		for (int x = 0; x < 10; x++) {
//			for (int y = 0; y < 10; y++) {
//				System.out.print(expected[x][y]);
//				if (y < 9) {
//					System.out.print(", ");
//				}
//			}
//			System.out.println();
//		}
		
		
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				assertEquals(expected[x][y], layer.get(x, y), .0000001);
			}
		}
	}
	
	public void test1D() {
		layer = new GridValueLayer("1D grid", true, 10);
		
		ValueLayerDiffuser diffuser = new ValueLayerDiffuser(layer, .5, 1);
		
		double[] expected;
		expected = new double[] { 0, 0, 0, 0, 10.0, 0, 0, 0, 0, 0 };
		
		layer.set(10.0, 4);

		for (int x = 0; x < 10; x++) {
			assertEquals(expected[x], layer.get(x));
		}
		
		diffuser.diffuse();
		
//		System.out.println("results:");
//		for (int x = 0; x < 10; x++) {
//			System.out.print(layer.get(x));
//			if (x < 9) {
//				System.out.print(", ");
//			}
//		}
//		System.out.println();

		expected = new double[] { 0,	0,	0,	2.5,0.0,	2.5,	0,	0,	0,	0 };
		
//		System.out.println("expects:");
//		for (int x = 0; x < 10; x++) {
//			System.out.print(expected[x]);
//			if (x < 9) {
//				System.out.print(", ");
//			}
//		}
//		System.out.println();

		for (int x = 0; x < 10; x++) {
			assertEquals(expected[x], layer.get(x));
		}
		
		diffuser.diffuse();
		
//		System.out.println("result:");
//		for (int x = 0; x < 10; x++) {
//			System.out.print(layer.get(x));
//			if (x < 9) {
//				System.out.print(", ");
//			}
//		}
//		System.out.println();
		
		expected = new double[] { 0, 0, 1.25/2, 0, 2.5/2, 0.0, 1.25/2, 0, 0, 0 };

//		System.out.println("expects:");
//		for (int x = 0; x < 10; x++) {
//			System.out.print(expected[x]);
//			if (x < 9) {
//				System.out.print(", ");
//			}
//		}
//		System.out.println();
		for (int x = 0; x < 10; x++) {
			assertEquals(expected[x], layer.get(x));
		}
	}
	
	public void test1DTorus() {
		layer = new GridValueLayer("1D grid", true, 10);
		
		ValueLayerDiffuser diffuser = new ValueLayerDiffuser(layer, 1, 1);
		
		double[] expected;
		expected = new double[] { 10.0, 0, 0, 0, 0.0, 0, 0, 0, 0, 0 };
		
		layer.set(10.0, 0);

		for (int x = 0; x < 10; x++) {
			assertEquals(expected[x], layer.get(x));
		}
		
		diffuser.diffuse();
		
//		System.out.println("results:");
//		for (int x = 0; x < 10; x++) {
//			System.out.print(layer.get(x));
//			if (x < 9) {
//				System.out.print(", ");
//			}
//		}
//		System.out.println();

		expected = new double[] { 0,	5.0,	0,	0.0,0.0,	0.0,	0,	0,	0,	5.0 };
		
//		System.out.println("expects:");
//		for (int x = 0; x < 10; x++) {
//			System.out.print(expected[x]);
//			if (x < 9) {
//				System.out.print(", ");
//			}
//		}
//		System.out.println();

		for (int x = 0; x < 10; x++) {
			assertEquals(expected[x], layer.get(x));
		}
		
		diffuser.diffuse();
		
//		System.out.println("result:");
//		for (int x = 0; x < 10; x++) {
//			System.out.print(layer.get(x));
//			if (x < 9) {
//				System.out.print(", ");
//			}
//		}
//		System.out.println();
		
		expected = new double[] { 5.0,	0.0,	2.5,	0.0,0.0,	0.0,	0,	0,	2.5,	0.0 };

//		System.out.println("expects:");
//		for (int x = 0; x < 10; x++) {
//			System.out.print(expected[x]);
//			if (x < 9) {
//				System.out.print(", ");
//			}
//		}
//		System.out.println();
		for (int x = 0; x < 10; x++) {
			assertEquals(expected[x], layer.get(x));
		}
	}
	
	public void test1DNonTorus() {
		layer = new GridValueLayer("1D grid", true, 10);
		
		ValueLayerDiffuser diffuser = new ValueLayerDiffuser(layer, 1, 1, false);
		
		double[] expected;
		expected = new double[] { 10.0, 0, 0, 0, 0.0, 0, 0, 0, 0, 0 };
		
		layer.set(10.0, 0);

		for (int x = 0; x < 10; x++) {
			assertEquals(expected[x], layer.get(x));
		}
		
		diffuser.diffuse();
		
//		System.out.println("results:");
//		for (int x = 0; x < 10; x++) {
//			System.out.print(layer.get(x));
//			if (x < 9) {
//				System.out.print(", ");
//			}
//		}
//		System.out.println();

		expected = new double[] { 0,	5.0,	0,	0.0,0.0,	0.0,	0,	0,	0,	0.0 };
		
//		System.out.println("expects:");
//		for (int x = 0; x < 10; x++) {
//			System.out.print(expected[x]);
//			if (x < 9) {
//				System.out.print(", ");
//			}
//		}
//		System.out.println();

		for (int x = 0; x < 10; x++) {
			assertEquals(expected[x], layer.get(x));
		}
		
		diffuser.diffuse();
		
//		System.out.println("result:");
//		for (int x = 0; x < 10; x++) {
//			System.out.print(layer.get(x));
//			if (x < 9) {
//				System.out.print(", ");
//			}
//		}
//		System.out.println();
		
		expected = new double[] { 2.5, 0.0, 2.5, 0.0,0.0, 0.0, 0, 0, 0.0, 0.0 };

//		System.out.println("expects:");
//		for (int x = 0; x < 10; x++) {
//			System.out.print(expected[x]);
//			if (x < 9) {
//				System.out.print(", ");
//			}
//		}
//		System.out.println();
		for (int x = 0; x < 10; x++) {
			assertEquals(expected[x], layer.get(x));
		}
	}
	

	public void test3D() {
		layer = new GridValueLayer("3D grid", true, 10, 10, 10);
		
		ValueLayerDiffuser diffuser = new ValueLayerDiffuser(layer, .5, 1);
		
		layer.set(10.0, 4, 4, 4);

		
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				for (int z = 0; z < 10; z++) {
					if (x == 4 && y == 4 && z == 4) {
						assertEquals(10.0, layer.get(x, y, z));						
					} else {
						assertEquals(0.0, layer.get(x, y, z));
					}
				}
			}
		}
		
		diffuser.diffuse();

//		System.out.println("result:");
//		for (int x = 0; x < 10; x++) {
//			for (int y = 0; y < 10; y++) {
//				for (int z = 0; z < 10; z++) {
//					System.out.print(layer.get(x, y, z));
//					if (z < 9) {
//						System.out.print(", ");
//					}				
//				}
//				System.out.println(": " + x + "," + y);
//			}
//			System.out.println();
//		}
		
		double totalNeighbors = 6*4 + 20;
		double directNeighborValue = (4 * 10 / totalNeighbors) / 2;
		double diagonalNeighborValue = (10 / totalNeighbors) / 2;

		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				for (int z = 0; z < 10; z++) {
					int eqCount = (x == 4 ? 1 : 0);
					eqCount += (y == 4 ? 1 : 0);
					eqCount += (z == 4 ? 1 : 0);
					
					if ((3 <= x && x <= 5) && (3 <= y && y <= 5) && (3 <= z && z <= 5)) {
						if (eqCount == 3) {
							assertEquals(0.0, layer.get(x, y, z), .000001);
						} else if (eqCount == 2) {
							assertEquals(directNeighborValue, layer.get(x, y, z), .000001);
						} else {
							assertEquals(diagonalNeighborValue, layer.get(x, y, z), .000001);
						} 	
					} else {
						assertEquals(0.0, layer.get(x, y, z));
					}				
				}
			}
		}
	}
	
	public void test3DTorus() {
		layer = new GridValueLayer("3D grid", true, 10, 10, 10);
		
		ValueLayerDiffuser diffuser = new ValueLayerDiffuser(layer, .5, 1);
		
		layer.set(10.0, 4, 4, 0);

		
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				for (int z = 0; z < 10; z++) {
					if (x == 4 && y == 4 && z == 0) {
						assertEquals(10.0, layer.get(x, y, z));						
					} else {
						assertEquals(0.0, layer.get(x, y, z));
					}
				}
			}
		}
		
		diffuser.diffuse();

//		System.out.println("result:");
//		for (int x = 0; x < 10; x++) {
//			for (int y = 0; y < 10; y++) {
//				for (int z = 0; z < 10; z++) {
//					System.out.print(layer.get(x, y, z));
//					if (z < 9) {
//						System.out.print(", ");
//					}				
//				}
//				System.out.println(": " + x + "," + y);
//			}
//			System.out.println();
//		}
		
		double totalNeighbors = 6*4 + 20;
		double directNeighborValue = (4 * 10 / totalNeighbors) / 2;
		double diagonalNeighborValue = (10 / totalNeighbors) / 2;

		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				for (int z = 0; z < 10; z++) {
					int eqCount = (x == 4 ? 1 : 0);
					eqCount += (y == 4 ? 1 : 0);
					eqCount += (z == 0 ? 1 : 0);
					
					if ((3 <= x && x <= 5) && (3 <= y && y <= 5) && (z == 0 || z == 1 || z == 9)) {
						if (eqCount == 3) {
							assertEquals(0.0, layer.get(x, y, z), .000001);
						} else if (eqCount == 2) {
							assertEquals(directNeighborValue, layer.get(x, y, z), .000001);
						} else {
							System.out.println(x + "," + y + "," + z);
							assertEquals(diagonalNeighborValue, layer.get(x, y, z), .000001);
						} 	
					} else {
						assertEquals(0.0, layer.get(x, y, z));
					}				
				}
			}
		}
	}
	
	public void test3DNonTorus() {
		layer = new GridValueLayer("3D grid", true, 10, 10, 10);
		
		ValueLayerDiffuser diffuser = new ValueLayerDiffuser(layer, .5, 1, false);
		
		layer.set(10.0, 4, 4, 0);

		
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				for (int z = 0; z < 10; z++) {
					if (x == 4 && y == 4 && z == 0) {
						assertEquals(10.0, layer.get(x, y, z));						
					} else {
						assertEquals(0.0, layer.get(x, y, z));
					}
				}
			}
		}
		
		diffuser.diffuse();

//		System.out.println("result:");
//		for (int x = 0; x < 10; x++) {
//			for (int y = 0; y < 10; y++) {
//				for (int z = 0; z < 10; z++) {
//					System.out.print(layer.get(x, y, z));
//					if (z < 9) {
//						System.out.print(", ");
//					}				
//				}
//				System.out.println(": " + x + "," + y);
//			}
//			System.out.println();
//		}
		
		double totalNeighbors = 6*4 + 20;
		double directNeighborValue = (4 * 10 / totalNeighbors) / 2;
		double diagonalNeighborValue = (10 / totalNeighbors) / 2;

		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				for (int z = 0; z < 10; z++) {
					int eqCount = (x == 4 ? 1 : 0);
					eqCount += (y == 4 ? 1 : 0);
					eqCount += (z == 0 ? 1 : 0);
					
					if ((3 <= x && x <= 5) && (3 <= y && y <= 5) && (z == 0 || z == 1)) {
						if (eqCount == 3) {
							assertEquals(0.0, layer.get(x, y, z), .000001);
						} else if (eqCount == 2) {
							assertEquals(directNeighborValue, layer.get(x, y, z), .000001);
						} else {
							System.out.println(x + "," + y + "," + z);
							assertEquals(diagonalNeighborValue, layer.get(x, y, z), .000001);
						} 	
					} else {
						assertEquals(0.0, layer.get(x, y, z));
					}				
				}
			}
		}
	}
}
