package repast.simphony.context.space.grid;

import repast.simphony.context.Context;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;

public interface GridFactory {
	public <T> Grid<T> createGrid(String name, Context<T> context,
	                              GridBuilderParameters<T> params);
}