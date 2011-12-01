package repast.simphony.relogo.factories;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.relogo.Observer;
import repast.simphony.relogo.Patch;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.RandomCartesianAdder;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.RandomGridAdder;

public class ReLogoWorldFactory {
	Context parentContext;
	Context context;
	Grid grid;
	ContinuousSpace space;
	RLWorldDimensions rLWorldDimensions;
	TurtleFactory turtleFactory;
	PatchFactory patchFactory;
	LinkFactory linkFactory;
	
	public Context getContext(){
		return context;
	}
	
	public RLWorldDimensions getrLWorldDimensions() {
		return rLWorldDimensions;
	}

	public TurtleFactory getTurtleFactory() {
		return turtleFactory;
	}

	public PatchFactory getPatchFactory() {
		return patchFactory;
	}

	public LinkFactory getLinkFactory() {
		return linkFactory;
	}

	public ReLogoWorldFactory(Context parentContext, String worldID, RLWorldDimensions rLWorldDimensions, TurtleFactory turtleFactory, PatchFactory patchFactory, LinkFactory linkFactory){
		this.parentContext = parentContext;
		this.context = new DefaultContext(worldID);
		this.rLWorldDimensions = rLWorldDimensions;
		this.turtleFactory = turtleFactory;
		this.patchFactory = patchFactory;
		this.linkFactory = linkFactory;
	}
	
	public void createWorld(Observer observer){

		turtleFactory.init(observer);
		patchFactory.init(observer);
		linkFactory.init(observer);
		
		addGrid();
		addContinuousSpace();
		addPatches();
		parentContext.addSubContext(context);
	}

	public void addGrid() {
		// The inputs to the
		// GridFactory include the grid name, the context in which to place the grid,
		// and the grid parameters.  Grid parameters include the border specification,
		// random adder for populating the grid with agents, boolean for multiple occupancy,
		// and the dimensions of the grid.
		this.grid = GridFactoryFinder.createGridFactory(null).createGrid("Grid2d", context,
				new GridBuilderParameters(rLWorldDimensions.getPgt(),
						new RandomGridAdder(), true, rLWorldDimensions.getDims(), rLWorldDimensions.getOrigin()));
	}

	
	public void addContinuousSpace() {
		// Create a new 2D continuous space to model the physical space on which the sheep
		// and wolves will move.  The inputs to the Space Factory include the space name, 
		// the context in which to place the space, border specification,
		// random adder for populating the grid with agents,
		// and the dimensions of the grid.
		this.space = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null)
		.createContinuousSpace("Space2d", context, new RandomCartesianAdder(),
				rLWorldDimensions.getPt(), rLWorldDimensions.getDDims(), rLWorldDimensions.getDOrigin());
		
	}


	public void addPatches() {
		
		// Populate the world with Patch objects
		// Iterate over the dimensions of the world grid
		for (int i=rLWorldDimensions.getMinPxcor(); i<=rLWorldDimensions.getMaxPxcor(); i++){
			for (int j=rLWorldDimensions.getMinPycor(); j<=rLWorldDimensions.getMaxPycor(); j++){
				Patch patch = patchFactory.createPatch();				// create a new Patch object patch
				context.add(patch);								// add the patch to the root context
				grid.moveTo(patch, i, j);				// move the patch to its position on the grid
				space.moveTo(patch, i,j);				//  and to its position on the continuous space
			}
		}
		
	}

}
