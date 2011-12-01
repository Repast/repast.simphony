/**
 * 
 */
package repast.simphony.relogo

import repast.simphony.space.graph.RepastEdge
import static repast.simphony.relogo.Utility.*import repast.simphony.space.graph.UndirectedJungNetworkimport repast.simphony.space.graph.DirectedJungNetwork
import repast.simphony.engine.environment.RunStateimport repast.simphony.context.Contextimport repast.simphony.context.DefaultContextimport repast.simphony.space.grid.GridBuilderParametersimport repast.simphony.space.grid.RandomGridAdderimport repast.simphony.space.continuous.RandomCartesianAdderimport repast.simphony.context.space.grid.GridFactoryFinderimport repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinderimport repast.simphony.space.grid.Gridimport repast.simphony.space.continuous.ContinuousSpaceimport repast.simphony.space.continuous.NdPointimport repast.simphony.relogo.factories.PatchFactoryimport repast.simphony.relogo.factories.TurtleFactory/**
 * @author jozik
 *
 */
public class PatchTest extends GroovyTestCase{
	
	private Observer observer; 
	private Context context;
	private Context tPContext;
	// TODO: the only class requiring the RunState to exist and be initialized is the ButtonFactory 
	private RunState runState;
	private int minPxcor = -10 //(Integer)p.getValue("minPxcor")
	private int maxPxcor = 10 //(Integer)p.getValue("maxPxcor")
	private int minPycor = -10 //(Integer)p.getValue("minPycor")
	private int maxPycor = 10 //(Integer)p.getValue("maxPycor")

	public void setUp() {
		
		//TODO: modify this to reflect the SimBuilder context creator
		context = new DefaultContext("ReLogo", "ReLogo")
		tPContext = new DefaultContext("TPContext", "TPContext")
		context.addSubContext(tPContext)
		for (int i = 0; i < 10; i++) {
			context.add(i);
		}
		// TODO: this part involving RunState can likely be eliminated since the ButtonFactory class is not being examined
		runState = RunState.init()
		runState.setMasterContext(context) // specify the master context
		
		
		int xdim = -minPxcor + maxPxcor + 1;
		int ydim = -minPycor + maxPycor + 1;
		
		int[] dims = [xdim, ydim];
		int[] origin = [-minPxcor, -minPycor];
		
		double[] dDims = [(double) xdim,(double)  ydim];
		double[] dOrigin = [((double) origin[0]) + 0.5, ((double) origin[1]) + 0.5];


		// The inputs to the
		// GridFactory include the grid name, the context in which to place the grid,
		// and the grid parameters.  Grid parameters include the border specification,
		// random adder for populating the grid with agents, boolean for multiple occupancy,
		// and the dimensions of the grid.
		GridFactoryFinder.createGridFactory(null).createGrid("Grid2d", tPContext, new GridBuilderParameters(new repast.simphony.space.grid.WrapAroundBorders(),new RandomGridAdder(), true, dims, origin));

		// Create a new 2D continuous space to model the physical space on which the sheep
		// and wolves will move.  The inputs to the Space Factory include the space name, 
		// the context in which to place the space, border specification,
		// random adder for populating the grid with agents,
		// and the dimensions of the grid.
		ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null).createContinuousSpace("Space2d", tPContext, new RandomCartesianAdder(), new repast.simphony.space.continuous.WrapAroundBorders(), dDims, dOrigin);
		
		// Get the patch grid and continuous space from the root context
		Grid grid = (Grid) tPContext.getProjection("Grid2d");
		ContinuousSpace space = (ContinuousSpace) tPContext.getProjection("Space2d");
		// Populate the world with Patch objects
		// Iterate over the dimensions of the world grid
		observer = new BaseObserver(); 
		PatchFactory pf = new PatchFactory(BasePatch)
		pf.init(observer)
		for (int i=minPxcor; i<=maxPxcor; i++){
			for (int j=minPycor; j<=maxPycor; j++){
				Patch patch = pf.createPatch();				// create a new Patch object patch
				tPContext.add(patch);								// add the patch to the root context
				grid.moveTo(patch, i, j);				// move the patch to its position on the grid
				space.moveTo(patch, i as double ,j as double);				//  and to its position on the continuous space
			}
		} 
	}
	
	
	
	
}
