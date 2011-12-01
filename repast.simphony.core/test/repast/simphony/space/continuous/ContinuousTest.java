package repast.simphony.space.continuous;

import junit.framework.TestCase;
import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.query.space.continuous.ContinuousWithin;
import repast.simphony.query.space.projection.Within;
import repast.simphony.space.SpatialException;

import java.util.*;

/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class ContinuousTest extends TestCase {

	private Context<Integer> context;

	public void setUp() {
		context = new DefaultContext<Integer>();
		for (int i = 0; i < 10; i++) {
			context.add(i);
		}
	}

	public void testWithinPredicate() {
		Context<Integer> context = new DefaultContext<Integer>();
		for (int i = 0; i < 10; i++) {
			context.add(i);
		}

		ContinuousSpace<Integer> space = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null)
										.createContinuousSpace("cs1", context, new SimpleCartesianAdder<Integer>(),
														new StickyBorders(), 30, 30);

		space.moveTo(1, 10, 10);
		space.moveTo(2, 10, 11);

		Within within = new Within(1, 2, 3);
		assertTrue(space.evaluate(within));

		space.moveTo(1, 29, 29);
		assertFalse(space.evaluate(within));
	}

	public void testMultiOccupancy() {
		ContinuousSpace<Integer> cs1 = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null)
								.createContinuousSpace("cs1", context, new SimpleCartesianAdder<Integer>(),
												new StickyBorders(), 50, 50);

		cs1.moveTo(1, 30, 30);
		cs1.moveTo(2, 30, 30);
		Set<Integer> expected = new HashSet<Integer>();
		expected.add(1);
		expected.add(2);
		for (Integer val : cs1.getObjectsAt(30, 30)) {
			assertTrue(expected.remove(val));
		}
		assertEquals(0, expected.size());
	}

	public void testWithin() {
		double[] size = {30, 30};
		ContinuousSpace<Integer> cs1 = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null)
						.createContinuousSpace("cs1", context, new SimpleCartesianAdder<Integer>(), new WrapAroundBorders(), size);
		cs1.moveTo(0, 3, 3);
		cs1.moveTo(1, 4, 4);
		cs1.moveTo(2, 4.323, 3);
		cs1.moveTo(3, 10, 10);
		for (int i = 4; i < 10; i++) {
			cs1.moveTo(i, 20, 20);
		}

		Set<Integer> expected = new HashSet<Integer>();
		double dist = 10;
		NdPoint origin = cs1.getLocation(1);
		for (int i = 0; i < 10; i++) {
			NdPoint point = cs1.getLocation(i);
			if (cs1.getDistanceSq(origin,point) <= dist * dist) {
				expected.add(i);
			}
		}

		// remove the source object itself.
		expected.remove(1);

		ContinuousWithin<Integer> query = new ContinuousWithin<Integer>(context, 1, 10);
		for (Integer val : query.query()) {
			assertTrue(expected.remove(val));
		}
		assertEquals(0, expected.size());


		expected = new HashSet<Integer>();
		for (int i = 0; i < 10; i++) {
			NdPoint point = cs1.getLocation(i);
			if (cs1.getDistanceSq(origin,point) <= dist * dist) {
				expected.add(i);
			}
		}

		// remove the source object itself.
		expected.remove(1);

		query = new ContinuousWithin<Integer>(cs1, 1, 10);
		for (Integer val : query.query()) {
			assertTrue(expected.remove(val));
		}
		assertEquals(0, expected.size());

		expected.add(3);
		for (Integer val : query.query(expected)) {
			assertTrue(expected.remove(val));
		}
		assertEquals(0, expected.size());
	}

	public void testAdder() {
		double[] size = {50, 50};
		ContinuousSpace<Integer> cs1 = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null)
						.createContinuousSpace("cs1", context, new SimpleCartesianAdder<Integer>(), new WrapAroundBorders(), size);

		ContinuousAdder ca = cs1.getAdder();
		assertTrue(ca instanceof SimpleCartesianAdder);
	}

	public void testStrictBorders() {
		ContinuousSpace<Integer> cs1 = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null)
						.createContinuousSpace("cs1", context, new SimpleCartesianAdder<Integer>(), new StrictBorders(), 50, 50);

		try {
			cs1.moveTo(1, 50, 50);
			fail("should throw exception");
		} catch (SpatialException ex) {
		}

		cs1.moveTo(1, 0, 49);

		try {
			cs1.moveByDisplacement(1, -1, 0);
			fail("should throw exception");
		} catch (SpatialException ex) {
		}

		try {
			cs1.moveByDisplacement(1, 0, 1);
			fail("should throw exception");
		} catch (SpatialException ex) {
		}
	}

	public void testStickyBorders() {
		ContinuousSpace<Integer> cs1 = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null)
						.createContinuousSpace("cs1", context, new SimpleCartesianAdder<Integer>(), new StickyBorders(), 50, 50);

		try {
			cs1.moveTo(1, 50, 50);
			fail("should throw exception");
		} catch (SpatialException ex) {
		}

		try {
			cs1.moveTo(1, 30, 50);
			fail("should throw exception");
		} catch (SpatialException ex) {
		}

		cs1.moveTo(1, 0, 49);

		NdPoint point = cs1.moveByDisplacement(1, -1, 0);
		assertEquals(0.0, point.getX());
		assertEquals(49.0, point.getY());

		point = cs1.moveByDisplacement(1, 0, 200);
		assertEquals(0.0, point.getX());
		assertEquals(AbstractPointTranslator.minusEpsilon(50), point.getY());
	}


	public void testWrappedBorders() {
		ContinuousSpace<Integer> cs1 = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null)
						.createContinuousSpace("cs1", context, new SimpleCartesianAdder<Integer>(), new WrapAroundBorders(), 50.5, 50);
		cs1.moveTo(1, 50.5, 50);
		NdPoint point = cs1.getLocation(1);
		assertEquals(0.0, point.getX());
		assertEquals(0.0, point.getY());

		cs1.moveTo(1, 0, 49);

		point = cs1.moveByDisplacement(1, -1, 0);
		assertEquals(49.5, point.getX());
		assertEquals(49.0, point.getY());

		point = cs1.moveByDisplacement(1, 0, 1);
		assertEquals(49.5, point.getX());
		assertEquals(0.0, point.getY());
		
		cs1.moveTo(1, -1, -1);
		point = cs1.getLocation(1);
		assertEquals(49.5, point.getX());
		assertEquals(49.0, point.getY());
		
		cs1.moveTo(1, -103, -152);
		point = cs1.getLocation(1);
		assertEquals(48.5, point.getX());
		assertEquals(48.0, point.getY());
	}

	public void testSize() {
		double[] size = {50, 50};
		ContinuousSpace<Integer> cs1 = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null)
						.createContinuousSpace("cs1", context, new SimpleCartesianAdder<Integer>(), new WrapAroundBorders(), size);

		for (int i = 0; i < cs1.size(); i++) {
			cs1.moveTo(i, 5 * i, 3 + cs1.size());
		}


		int size1 = cs1.size();

		assertEquals(10, size1);
	}

	public void testLocation() {
		double[] size = {50, 50};
		ContinuousSpace<Integer> cs1 = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null)
						.createContinuousSpace("cs1", context, new SimpleCartesianAdder<Integer>(), new WrapAroundBorders(), size);

		for (int i = 0; i < cs1.size(); i++) {
			cs1.moveTo(i, 5 * i, 3 + cs1.size());
		}


		for (int i = 0; i < 10; i++) {
			NdPoint point = cs1.getLocation(i);
			assertTrue(point instanceof NdPoint);

			double x1 = i * 5;
			double x2 = point.getX();
			assertTrue(x1 == x2);
			assertTrue(13 == point.getY());
		}


	}

	public void testObjectAt() {
		double[] size = {50, 50};
		ContinuousSpace<Integer> cs1 = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null)
						.createContinuousSpace("cs1", context, new SimpleCartesianAdder<Integer>(), new WrapAroundBorders(), size);

		int yl = 0;
		double locations[] = new double[cs1.size() * 2];
		for (int i = 0; i < cs1.size(); i++) {
			yl += cs1.size();
			cs1.moveTo(i, 8 * i, yl);
			int s = i * 2;
			locations[s] = 8 * i;
			locations[s + 1] = yl;
		}

		for (int i = 0; i < cs1.size() * 2; i++) {
			double x = locations[i];
			double y = locations[i + 1];

			Integer o = cs1.getObjectAt(x, y);

			Set st = (Set) cs1.getObjects();
			Iterator ii = st.iterator();
			while (ii.hasNext()) {
				Integer ig = (Integer) ii.next();
				if (ig == o)
					assertTrue(o == ig);
			}

			i++;
		}

	}

	public void testDisplacement() {
		double[] size = {50, 50};
		Context<Integer> context = new DefaultContext<Integer>();
		ContinuousSpace<Integer> cs1 = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null)
						.createContinuousSpace("cs1", context, new SimpleCartesianAdder<Integer>(), new WrapAroundBorders(), size);


		List<Integer> all = new ArrayList<Integer>();
		List<Integer> nghs = new ArrayList<Integer>();

		int i = 0;
		for (int x = 0; x < 10; x++) {
			Integer val = new Integer(x);
			context.add(val);
			cs1.moveTo(val, x, x * 2);
			nghs.add(x * 2);
			all.add(val);
		}

		double[] displacement = new double[cs1.size()];

		for (int x = 0; x < 10; x++) {
			Integer in = all.get(x);
			displacement[x] = in.doubleValue() * -1;
		}

		for (int x = 0; x < 10; x++) {
			NdPoint p = cs1.moveByDisplacement(all.get(x), displacement[x]);
			double xx = p.getX();
			double yy = p.getY();
			assertTrue(xx == 0);
			assertTrue(yy == nghs.get(x));
		}

	}

	public void testMoveByVector() {
		double[] size = {50, 50};
		Context<Integer> context = new DefaultContext<Integer>();
		ContinuousSpace<Integer> cs1 = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null)
						.createContinuousSpace("cs1", context, new SimpleCartesianAdder<Integer>(), new WrapAroundBorders(), size);


		List<Integer> all = new ArrayList<Integer>();
		List<Integer> nghs = new ArrayList<Integer>();

		int i = 0;
		for (int x = 0; x < 10; x++) {
			Integer val = new Integer(x);
			context.add(val);
			cs1.moveTo(val, x, x * 2);
			nghs.add(x * 2);
			all.add(val);
		}

		double[] distance = new double[cs1.size()];
		double[] angle = new double[cs1.size()];

		for (int x = 0; x < 10; x++) {
			Integer in = all.get(x);
			distance[x] = in.doubleValue() * 2;
			angle[x] = in.doubleValue() / 10;
		}

		for (int x = 0; x < 10; x++) {
			NdPoint p = cs1.moveByDisplacement(all.get(x), distance[x], angle[x]);
			double xx = p.getX();
			double yy = p.getY();
			assertTrue(all.get(x) * 3 == xx);
			double re = nghs.get(x) * 1.050;
			assertTrue(Math.round(re) == Math.round(yy));
		}
	}

	public void testToroidal() {
		double[] size = {50, 50};
		Context<Integer> context = new DefaultContext<Integer>();
		ContinuousSpace<Integer> cs1 = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null)
						.createContinuousSpace("cs1", context, new SimpleCartesianAdder<Integer>(), new WrapAroundBorders(), size);

		assertTrue(cs1.isPeriodic());
	}

	public void testComplexBounce3D() {
		BouncyBorders bouncer = new BouncyBorders(12, 10, 14);

		// bounce diag down and to left off back wall
		double[] loc = {1, 7, 11};
		bouncer.translate(loc, 3, -3, 3);
		assertEquals(4.0, loc[0]);
		assertEquals(4.0, loc[1]);
		assertEquals(AbstractPointTranslator.minusEpsilon(14.0), loc[2]);

		loc = new double[]{1, 7, 11};
		bouncer.translate(loc, 3, -6, 4);
		assertEquals(4.0, loc[0]);
		assertEquals(1.0, loc[1]);
		assertEquals(13.0, loc[2]);

//		GridPoint gp = new GridPoint(new double[] { 1, 7, 11 });
		double[] iLoc = new double[]{1, 7, 11};
		bouncer.translate(/*gp,*/ iLoc, 3, -6, 4);
		assertEquals(4.0, iLoc[0]);
		assertEquals(1.0, iLoc[1]);
		assertEquals(13.0, iLoc[2]);
	}

	// mult intersect is when the line from inside the
	// box to the new location intersects mult. boundaries
	// and we need to choose the first one intersected and
	// bounce off that.
	public void testMultIntersect3D() {
		BouncyBorders bouncer = new BouncyBorders(7, 10, 10);

		// through back wall far enough that appears to
		// intersect right wall
		double[] loc = {4, 4, 8};
		// bouncer.translate(loc, 3, 0, 3);
		// assertEquals(5.0, loc[0]);
		// assertEquals(4.0, loc[1]);
		// assertEquals(7.0, loc[2]);

		// through front wall, moving downwards so hits
		// front wall then floor then up
		loc = new double[]{3, 4, 2};
		bouncer.translate(loc, -4, -6, -4);
		assertEquals(1.0, loc[0]);
		assertEquals(2.0, loc[1], 1);
		assertEquals(2.0, loc[2], 1);

//		GridPoint gp = new GridPoint(new double[] { 3, 4, 2 });
		double[] iLoc = new double[]{3, 4, 2};
		bouncer.translate(/*gp,*/ iLoc, -4, -6, -4);
		assertEquals(1.0, iLoc[0]);
		assertEquals(2.0, iLoc[1]);
		assertEquals(2.0, iLoc[2]);
	}

	public void testBounce2D() {
		BouncyBorders bouncer = new BouncyBorders(7, 10);
		double[] gp = new double[]{2, 3};

		// a simple bounce
		bouncer.translate(gp, -3, 0);
		assertEquals(1.0, gp[0]);
		assertEquals(3.0, gp[1]);

		bouncer.translate(gp, 8, 0);
		assertEquals(5.0, gp[0]);
		assertEquals(3.0, gp[1]);

		// a simple bounce
		bouncer.translate(gp, 0, -7);
		assertEquals(5.0, gp[0]);
		assertEquals(4.0, gp[1]);

		bouncer.translate(gp, 0, 7);
		assertEquals(5.0, gp[0]);
		assertEquals(9.0, gp[1]);

		gp = new double[]{1, 3};
		bouncer.translate(gp, -2, 5);
		assertEquals(1.0, gp[0]);
		assertEquals(8.0, gp[1]);

		gp = new double[]{5, 1};
		bouncer.translate(gp, 4, 5);
		assertEquals(5.0, gp[0]);
		assertEquals(6.0, gp[1]);

		gp = new double[]{1, 7};
		bouncer.translate(gp, 1, 5);
		assertEquals(2.0, gp[0]);
		assertEquals(8.0, gp[1]);

		gp = new double[]{1, 2};
		bouncer.translate(gp, 1, -3);
		assertEquals(2.0, gp[0]);
		assertEquals(1.0, gp[1]);

		// int bounce
		gp = new double[]{4, 1};
		bouncer.translate(gp, 5, -3);
		assertEquals(5.0, gp[0]);
		assertEquals(2.0, gp[1]);

		gp = new double[]{4, 8};
		bouncer.translate(gp, 5, 5);
		assertEquals(5.0, gp[0]);
		assertEquals(7.0, gp[1]);

		gp = new double[]{2, 8};
		bouncer.translate(gp, -3, 5);
		assertEquals(1.0, gp[0]);
		assertEquals(7.0, gp[1]);

		gp = new double[]{1, 2};
		bouncer.translate(gp, -3, -3);
		assertEquals(2.0, gp[0]);
		assertEquals(1.0, gp[1]);

		// corner bounce
		gp = new double[]{2, 2};
		bouncer.translate(gp, -4, -4);
		assertEquals(2.0, gp[0]);
		assertEquals(2.0, gp[1]);
	}

	public void testBounceSpace() {
		ContinuousSpace<Integer> grid = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null)
						.createContinuousSpace("cs1", context, new SimpleCartesianAdder<Integer>(),
										new BouncyBorders(), 7, 10, 10);
		grid.moveTo(1, 3, 4, 2);
		NdPoint point = grid.moveByDisplacement(1, -4, -6, -4);
		assertEquals(1.0, point.getX());
		assertEquals(2.0, point.getY());
		assertEquals(2.0, point.getZ());
	}
}
