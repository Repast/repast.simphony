package repast.simphony.context.space.grid;

import repast.simphony.context.Context;
import repast.simphony.space.grid.*;

import java.util.Map;

/**
 * Factory for creating grids.
 * 
 * @author Nick Collier
 */
public class DefaultGridFactory implements GridFactory {

	protected DefaultGridFactory() {

	}

	public <T> Grid<T> createGrid(String name, Context<T> context,
	                              GridBuilderParameters<T> params) {
		CellAccessor<T, Map<GridPoint, Object>> accessor = params.isMultOccupancy() ? new MultiOccupancyCellAccessor<T>()
				: new SingleOccupancyCellAccessor<T>();
		ContextGrid<T> dGrid = new ContextGrid<T>(name, params.getAdder(),
				params.getGridPointTranslator(), accessor, params
						.getDimensions(), params.getOrigin());
		context.addProjection(dGrid);
		return dGrid;
	}
}
