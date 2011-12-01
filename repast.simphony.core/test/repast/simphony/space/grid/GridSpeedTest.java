/*CopyrightHere*/
package repast.simphony.space.grid;

import repast.simphony.context.DefaultContext;
import repast.simphony.random.RandomHelper;

public class GridSpeedTest {
	private DefaultContext context;

	protected void setUp() throws Exception {
		RandomHelper.init();
		context = new DefaultContext("context");
	}

	public void testDenseSpeed() throws Exception {
		setUp();
		FastDenseSingleOccuGrid grid = new FastDenseSingleOccuGrid("blah", new SimpleGridAdder(),
				new StickyBorders(), 100, 100);
		context.addProjection(grid);

		String[] agents = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
				"k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
				"w", "x", "y", "z" };
		for (int i = 0; i < agents.length; i++) {
			context.add(agents[i]);
		}
		int numAgents = agents.length;

		int moveCount = 1000000;
		System.out.println("moves: " + moveCount);
		System.out.println("number of agents: " + numAgents);
		long start = System.currentTimeMillis();
		for (int i = 0; i < moveCount; i++) {
			for (int j = 0; j < numAgents; j++) {
				grid.moveTo(agents[j], RandomHelper.nextIntFromTo(0, 2),
						RandomHelper.nextIntFromTo(0, 2));
			}
		}
		long end = System.currentTimeMillis();
		System.out.println("start: " + start);
		System.out.println("end: " + end);
		System.out.println("avg time per set of moves: " + (end - start)
				* 1000.0 / moveCount + "ms");
	}

	public void testSpeedMulti() throws Exception {
		setUp();
		DefaultGrid grid = new DefaultGrid("blah", new SimpleGridAdder(),
				new StickyBorders(), new MultiOccupancyCellAccessor(), 100, 100);
		context.addProjection(grid);

		String[] agents = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
				"k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
				"w", "x", "y", "z" };
		for (int i = 0; i < agents.length; i++) {
			context.add(agents[i]);
		}
		int numAgents = agents.length;

		int moveCount = 1000000;
		System.out.println("moves: " + moveCount);
		System.out.println("number of agents: " + numAgents);
		long start = System.currentTimeMillis();
		for (int i = 0; i < moveCount; i++) {
			for (int j = 0; j < numAgents; j++) {
				grid.moveTo(agents[j], RandomHelper.nextIntFromTo(0, 2),
						RandomHelper.nextIntFromTo(0, 2));
			}
		}
		long end = System.currentTimeMillis();
		System.out.println("start: " + start);
		System.out.println("end: " + end);
		System.out.println("avg time per set of moves: " + (end - start)
				* 1000.0 / moveCount + "ms");
	}

	public void testSpeedSingle() throws Exception {
		setUp();
		DefaultGrid grid = new DefaultGrid("blah", new SimpleGridAdder(),
				new StickyBorders(), new SingleOccupancyCellAccessor(), 100,
				100);
		context.addProjection(grid);

		String[] agents = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
				"k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
				"w", "x", "y", "z" };
		for (int i = 0; i < agents.length; i++) {
			context.add(agents[i]);
		}
		int numAgents = agents.length;

		int moveCount = 1000000;
		System.out.println("moves: " + moveCount);
		System.out.println("number of agents: " + numAgents);
		long start = System.currentTimeMillis();
		for (int i = 0; i < moveCount; i++) {
			for (int j = 0; j < numAgents; j++) {
				grid.moveTo(agents[j], RandomHelper.nextIntFromTo(0, 2),
						RandomHelper.nextIntFromTo(0, 2));
			}
		}
		long end = System.currentTimeMillis();
		System.out.println("start: " + start);
		System.out.println("end: " + end);
		System.out.println("avg time per set of moves: " + (end - start)
				* 1000.0 / moveCount + "ms");
	}

	public static void main(String[] args) throws Exception {
		GridSpeedTest test = new GridSpeedTest();
		test.setUp();
		System.out.println("start");
		test.testDenseSpeed();
		System.out.println("done");
	}
}
