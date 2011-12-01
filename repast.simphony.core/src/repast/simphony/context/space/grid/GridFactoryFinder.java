package repast.simphony.context.space.grid;

import java.util.Map;


public class GridFactoryFinder {
	public static GridFactory createGridFactory(Map<String, Object> hints) {
		return new DefaultGridFactory();
	}
}
