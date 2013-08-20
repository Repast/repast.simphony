package repast.simphony.relogo;

import groovy.lang.Closure;
import groovy.util.ObservableMap;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import repast.simphony.context.Context;
import repast.simphony.relogo.factories.LinkFactory;
import repast.simphony.relogo.factories.PatchFactory;
import repast.simphony.relogo.factories.RLWorldDimensions;
import repast.simphony.relogo.factories.TurtleFactory;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.graph.Network;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.space.projection.ProjectionEvent;
import repast.simphony.statecharts.StateChartScheduler;
import repast.simphony.ui.probe.ProbeID;
import cern.colt.function.DoubleFunction;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.jet.math.Functions;

public abstract class AbstractObserver implements Observer {

	TurtleFactory tf;
	PatchFactory pf;
	LinkFactory lf;
	Context context;

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	RLWorldDimensions rLWorldDimensions;
	AgentSet<Patch> cachedPatches;
	String observerID;

	public String getObserverID() {
		return observerID;
	}

	public void setObserverID(String observerID) {
		this.observerID = observerID;
	}

	Map<String, DenseDoubleMatrix2D> patchesVarsMap = new ConcurrentHashMap<String, DenseDoubleMatrix2D>();
	Set<DiffusiblePatchVariable> dpvs = new HashSet<DiffusiblePatchVariable>();
	
	private Set<DiffusiblePatchVariable> getPatchVars(){
		return dpvs;
	}

	public DenseDoubleMatrix2D getPatchVarMatrix(String varName) {
		return patchesVarsMap.get(varName);
	}

	public void createPatchVar(DiffusiblePatchVariable var) {
		dpvs.add(var);
		int xdim = this.worldWidth();
		int ydim = this.worldHeight();
		DenseDoubleMatrix2D ddm = new DenseDoubleMatrix2D(ydim, xdim);
		for (int x = 0; x < xdim; x++) {
			for (int y = 0; y < ydim; y++) {
				ddm.setQuick(y, x, var.getDefaultValue());
			}
		}
		this.setPatchVarMatrix(var.getName(), ddm);
	}

	public void setPatchVarMatrix(String varName, DenseDoubleMatrix2D mat) {
		patchesVarsMap.put(varName, mat);
	}

	public LinkFactory getLinkFactory() {
		return lf;
	}

	public TurtleFactory getTurtleFactory() {
		return tf;
	}

	public RLWorldDimensions getRLDimensions() {
		return this.rLWorldDimensions;
	}

	public void setRLWorldDimensions(RLWorldDimensions rLD) {
		this.rLWorldDimensions = rLD;
	}

	public void projectionEventOccurred(ProjectionEvent p) {
		if (p.getType() == ProjectionEvent.EDGE_REMOVED) {
			context.remove(p.getSubject());
		}
	}

	public void initializeBaseObserver(String observerID, Context context,
			TurtleFactory tf, PatchFactory pf, LinkFactory lf,
			RLWorldDimensions rLD) {
		setObserverID(observerID);
		setContext(context);
		setRLWorldDimensions(rLD);
		this.tf = tf;
		this.pf = pf;
		this.lf = lf;
	}

	/**
	 * Executes a set of commands for an agentset in random order.
	 * 
	 * @param a
	 *            an agentset
	 * @param askBlock
	 *            a set of commands
	 */
	public void ask(AgentSet<? extends ReLogoAgent> a, Closure askBlock) {
		try {
			for (ReLogoAgent r : a) {
				r.setMyself(this);
			}
			try {
				a.askAgentSet(askBlock);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			System.err.println("ask(" + a + ") failed.");
			System.err
					.println("ask(AgentSet) requires all members of AgentSet to extend ReLogoAgent.");

		}
	}

	/**
	 * Executes a set of commands for a collection of agents.
	 * 
	 * @param c
	 *            a collection of agents
	 * @param askBlock
	 *            a set of commands
	 */
	public void ask(Collection<? extends ReLogoAgent> c, Closure askBlock) {
		try {
			for (ReLogoAgent r : c) {
				r.setMyself(this);
			}
			try {
				askCollection(c, askBlock);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			System.err.println("ask(" + c + ") failed.");
			System.err
					.println("ask(List) requires all members of List to extend ReLogoAgent.");
		}
	}

	public void askCollection(Collection<? extends ReLogoAgent> c, Closure cl) {
		cl.setResolveStrategy(Closure.DELEGATE_FIRST);
		for (ReLogoAgent o : c) {
			cl.setDelegate(o);
			cl.call(o);
		}
	}

	/**
	 * Executes a set of commands for a turtle.
	 * 
	 * @param t
	 *            a turtle
	 * @param askBlock
	 *            a set of commands
	 */
	public void ask(Turtle t, Closure askBlock) {
		t.setMyself(this);
		t.askTurtle(askBlock);
	}

	/**
	 * Executes a set of commands for a patch.
	 * 
	 * @param p
	 *            a patch
	 * @param askBlock
	 *            a set of commands
	 */
	public void ask(Patch p, Closure askBlock) {
		p.setMyself(this);
		p.askPatch(askBlock);

	}

	/**
	 * Executes a set of commands for a link.
	 * 
	 * @param l
	 *            a link
	 * @param askBlock
	 *            a set of commands
	 */
	public void ask(Link l, Closure askBlock) {
		l.setMyself(this);
		l.askLink(askBlock);

	}

	/**
	 * Clears the world to the default state.
	 * 
	 * @see #clearAll()
	 */
	public void ca() {
		ReLogoModel.getInstance().setTicks(0.0);
		ct();
		cp();
		clearTrackingNetwork();
		StateChartScheduler.INSTANCE.initialize();
	}

	/**
	 * Clears the world to the default state.
	 */
	public void clearAll() {
		ca();
	}

	/**
	 * Removes all turtles.
	 * 
	 * @see #clearTurtles()
	 */
	public void ct() {
		AgentSet<Turtle> ts = turtles();
		for (Turtle t : ts) {
			t.die();
		}
		BaseTurtle.setWhoCounter(0);
	}

	/**
	 * Removes all turtles.
	 */
	public void clearTurtles() {
		ct();
	}

	/**
	 * Returns an agentset containing all turtles.
	 * 
	 * @return agentset of all turtles
	 */
	public AgentSet<Turtle> turtles() {
		return Utility.turtlesU(this);
	}

	/**
	 * Returns an agentset of turtles on a given patch.
	 * 
	 * @param p
	 *            a patch
	 * @return agentset of turtles on patch p
	 */
	public AgentSet<Turtle> turtlesOn(Patch p) {
		return Utility.getTurtlesOnGridPoint(p.getGridLocation(), this);
	}

	/**
	 * Returns an agentset of turtles on the same patch as a turtle.
	 * 
	 * @param t
	 *            a turtle
	 * @return agentset of turtles on the same patch at turtle t
	 */
	public AgentSet<Turtle> turtlesOn(Turtle t) {
		return Utility.getTurtlesOnGridPoint(
				Utility.ndPointToGridPoint(t.getTurtleLocation()), this);
	}

	/**
	 * Returns an agentset of turtles on the patches in a collection or on the patches
	 * that a collection of turtles are.
	 * 
	 * @param a
	 *            a collection
	 * @return agentset of turtles on the patches in collection a or on the patches
	 *         that collection a turtles are
	 */
	public AgentSet<Turtle> turtlesOn(Collection a) {

		if (a == null || a.isEmpty()) {
			return new AgentSet();
		}

		Set total = new HashSet();
		if (a.iterator().next() instanceof Turtle) {
			for (Object t : a) {
				NdPoint location = ((Turtle) t).getTurtleLocation();
				AgentSet temp = Utility.getTurtlesOnGridPoint(
						Utility.ndPointToGridPoint(location), this);
				total.addAll(temp);
			}
		} else {
			for (Object p : a) {
				GridPoint location = ((Patch) p).getGridLocation();
				AgentSet temp = Utility.getTurtlesOnGridPoint(location, this);
				total.addAll(temp);
			}
		}
		return new AgentSet(total);
	}

	/**
	 * Removes all links.
	 */
	public void clearLinks() {
		AgentSet<Link> ls = allLinks();
		for (Link l : ls) {
			l.die();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public AgentSet<Link> links() {
		return Utility.linksU(this);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public AgentSet<Link> allLinks() {
		return Utility.allLinksU(this);
	}


	/**
	 * Sets all standard and diffusible patch variables to their default values.
	 */
	public void clearPatches() {
		cp();
	}

	public void clearTrackingNetwork() {
		for (Object o : Utility.getWayPoints(this)) {
			context.remove(o);
		}
	}

	/**
	 * Sets all standard and diffusible patch variables to their default values.
	 * 
	 * @see #clearPatches()
	 */
	public void cp(){
		AgentSet<Patch> a = patches();
		for (Patch p : a){
			p.setToDefault();
		}
		for (DiffusiblePatchVariable var : getPatchVars()){
			getPatchVarMatrix(var.getName()).assign(var.getDefaultValue());
		}
	}

	/**
	 * Makes a number of randomly oriented turtles.
	 * 
	 * @param number
	 *            an integer
	 * @return created turtles
	 * @see #createTurtles()
	 */
	public AgentSet<Turtle> crt(int number) {
		return crt(number, null);
	}

	/**
	 * Makes a number of randomly oriented turtles then executes a set of
	 * commands on the turtles.
	 * 
	 * @param number
	 *            an integer
	 * @param closure
	 *            a set of commands
	 * @return created turtles
	 * 
	 * @see #createTurtles(int, Closure)
	 */
	public AgentSet<Turtle> crt(int number, Closure closure) {
		return crt(number, closure, "default");
	}

	/**
	 * Makes a number of randomly oriented turtles of a specific type then
	 * executes a set of commands on the turtles.
	 * 
	 * @param number
	 *            an integer
	 * @param closure
	 *            a set of commands
	 * @param type
	 *            a turtle type
	 * @return created turtles
	 * 
	 * @see #createTurtles(int, Closure, String)
	 */
	public AgentSet<Turtle> crt(int number, Closure closure, String type) {
		AgentSet newTurtles = new AgentSet();
		for (int i = 0; i < number; i++) {
			newTurtles.add(tf.createTurtle(type));
		}
		if (closure != null) {
			ask(newTurtles, closure);
		}
		return newTurtles;
	}

	/**
	 * Makes a number of randomly oriented turtles of a specific type then
	 * executes a set of commands on the turtles.
	 * 
	 * @param number
	 *            an integer
	 * @param closure
	 *            a set of commands
	 * @param type
	 *            a turtle type
	 * @return created turtles
	 * 
	 * @see #createTurtles(int, Closure, Class)
	 */
	public AgentSet<Turtle> crt(int number, Closure closure, Class type) {
		return crt(number, closure, type.getSimpleName());
	}

	/**
	 * Makes a number of randomly oriented turtles of a specific type then
	 * executes a set of commands on the turtles.
	 * 
	 * @param number
	 *            an integer
	 * @param closure
	 *            a set of commands
	 * @param type
	 *            a turtle type
	 * @return created turtles
	 */
	public AgentSet<Turtle> createTurtles(int number, Closure closure, String type) {
		return crt(number, closure, type);
	}

	/**
	 * Makes a number of randomly oriented turtles of a specific type then
	 * executes a set of commands on the turtles.
	 * 
	 * @param number
	 *            an integer
	 * @param closure
	 *            a set of commands
	 * @param type
	 *            a turtle type
	 * @return created turtles
	 */
	public AgentSet<Turtle> createTurtles(int number, Closure closure, Class type) {
		return crt(number, closure, type.getSimpleName());
	}

	/**
	 * Makes a number of randomly oriented turtles.
	 * 
	 * @param number
	 *            an integer
	 * @return created turtles
	 */
	public AgentSet<Turtle> createTurtles(int number) {
		return crt(number, null);
	}

	/**
	 * Makes a number of randomly oriented turtles then executes a set of
	 * commands on the turtles.
	 * 
	 * @param number
	 *            an integer
	 * @param closure
	 *            a set of commands
	 * @return created turtles
	 */
	public AgentSet<Turtle> createTurtles(int number, Closure closure) {
		return crt(number, closure);
	}

	/**
	 * Makes a number of uniformly fanned turtles.
	 * 
	 * @param number
	 *            an integer
	 * @return created turtles
	 * @see #createOrderedTurtles(int)
	 */
	public AgentSet<Turtle> cro(int number) {
		return cro(number, null);
	}

	/**
	 * Makes a number of uniformly fanned turtles then executes a set of
	 * commands on the turtles.
	 * 
	 * @param number
	 *            an integer
	 * @param closure
	 *            a set of commands
	 * @return created turtles
	 * @see #createOrderedTurtles(int, Closure)
	 */
	public AgentSet<Turtle> cro(int number, Closure closure) {
		return cro(number, closure, "default");
	}

	/**
	 * Makes a number of uniformly fanned turtles of a specific type then
	 * executes a set of commands on the turtles.
	 * 
	 * @param number
	 *            an integer
	 * @param closure
	 *            a set of commands
	 * @param type
	 *            a turtle type
	 * @return created turtles
	 */
	public AgentSet<Turtle> cro(int number, Closure closure, String type) {
		AgentSet<Turtle> newTurtles = new AgentSet<Turtle>();
		double headingIncrement = 360.0 / ((double) number);

		for (int i = 0; i < number; i++) {
			newTurtles.add(tf.createTurtle(type, i * headingIncrement));
		}
		if (closure != null) {
			ask(newTurtles, closure);
		}
		return newTurtles;
	}

	/**
	 * Makes a number of uniformly fanned turtles of a specific type then
	 * executes a set of commands on the turtles.
	 * 
	 * @param number
	 *            an integer
	 * @param closure
	 *            a set of commands
	 * @param type
	 *            a turtle type
	 * @return created turtles
	 */
	public AgentSet<Turtle> createOrderedTurtles(int number, Closure closure, String type) {
		return cro(number, closure, type);
	}

	/**
	 * Makes a number of uniformly fanned turtles of a specific type then
	 * executes a set of commands on the turtles.
	 * 
	 * @param number
	 *            an integer
	 * @param closure
	 *            a set of commands
	 * @param type
	 *            a turtle type
	 * @return created turtles
	 */
	public AgentSet<Turtle> createOrderedTurtles(int number, Closure closure, Class type) {
		return cro(number, closure, type);
	}

	/**
	 * Makes a number of uniformly fanned turtles of a specific type then
	 * executes a set of commands on the turtles.
	 * 
	 * @param number
	 *            an integer
	 * @param closure
	 *            a set of commands
	 * @param type
	 *            a turtle type
	 * @return created turtles
	 */
	public AgentSet<Turtle> cro(int number, Closure closure, Class type) {
		return cro(number, closure, type.getSimpleName());
	}

	/**
	 * Makes a number of uniformly fanned turtles.
	 * 
	 * @param number
	 *            an integer
	 * @return created turtles
	 */
	public AgentSet<Turtle> createOrderedTurtles(int number) {
		return cro(number, null);
	}

	/**
	 * Makes a number of uniformly fanned turtles then executes a set of
	 * commands on the turtles.
	 * 
	 * @param number
	 *            an integer
	 * @param closure
	 *            a set of commands
	 * @return created turtles
	 */
	public AgentSet<Turtle> createOrderedTurtles(int number, Closure closure) {
		return cro(number, closure);
	}

	/**
	 * Sets the default shape for new turtleType turtles to shape.
	 * 
	 * @param turtleType
	 *            a string
	 * @param shape
	 *            a string
	 */
	public void setDefaultShape(String turtleType, String shape) {
		tf.setDefaultShape(turtleType, shape);
	}

	/**
	 * Sets the default shape for new turtleType turtles to shape.
	 * 
	 * @param turtleType
	 *            a class
	 * @param shape
	 *            a string
	 */
	public void setDefaultShape(Class turtleType, String shape) {
		tf.setDefaultShape(turtleType.getSimpleName(), shape);
	}

	/**
	 * Prints value with agent identifier to current file with a newline.
	 * 
	 * @param value
	 *            any object
	 */
	public void fileShow(Object value) {
		UtilityG.fileShowU(toString(), value);
	}

	/**
	 * Prints value with agent identifier to the console.
	 * 
	 * @param value
	 *            any object
	 */
	public void show(Object value) {
		UtilityG.showU(toString(), value);
	}
	
	/**
	 * 
	 * This method provides a human-readable name for the agent.
	 * 
	 * @method toString
	 * 
	 */
	public String toString() {
		return observerID;
	}

	public Grid getGrid() {
		return (Grid) context.getProjection("Grid2d");
	}

	public ContinuousSpace getSpace() {
		return (ContinuousSpace) context.getProjection("Space2d");
	}

	public Network getNetwork(String name) {
		return (Network) context.getProjection(name);
	}

	// TODO: get geography?

	/**
	 * Returns the turtle of the given number.
	 * 
	 * @param number
	 *            a number
	 * @return turtle number
	 */
	public Turtle turtle(Number number) {
		return Utility.turtleU(number.intValue(), this);
	}

	/**
	 * Returns a random x coordinate for turtles.
	 * 
	 * @return random x coordinate for turtles
	 */
	public double randomXcor() {
		return Utility.randomXcorU(this);
	}

	/**
	 * Returns a random y coordinate for turtles.
	 * 
	 * @return random y coordinate for turtles
	 */
	public double randomYcor() {
		return Utility.randomYcorU(this);
	}

	/**
	 * Returns a random x coordinate for patches.
	 * 
	 * @return random x coordinate for patches
	 */
	public int randomPxcor() {
		return Utility.randomPxcorU(this);
	}

	/**
	 * Returns a random y coordinate for patches.
	 * 
	 * @return random y coordinate for patches
	 */
	public int randomPycor() {
		return Utility.randomPycorU(this);
	}

	/**
	 * Returns the patch containing a point.
	 * 
	 * @param nX
	 *            x coordinate
	 * @param nY
	 *            y coordinate
	 * @return patch that contains the point (nX, nY)
	 */
	public Patch patch(Number nX, Number nY) {
		double x = nX.doubleValue();
		double y = nY.doubleValue();
		return Utility.patchU(x, y, this);
	}

	/**
	 * Returns an agentset containing all patches.
	 * 
	 * @return agentset containing all patches
	 */
	public AgentSet<Patch> patches() {
		if (cachedPatches == null) {
			cachedPatches = Utility.patchesU(this);
		}
		return cachedPatches;
	}

	/**
	 * Returns the link between two turtles.
	 * 
	 * @param oneEnd
	 *            an integer
	 * @param otherEnd
	 *            an integer
	 * @return link between two turtles
	 */
	public Link link(Number oneEnd, Number otherEnd) {
		return Utility.linkU(oneEnd.intValue(), otherEnd.intValue(), this);
	}

	/**
	 * Returns the link between two turtles.
	 * 
	 * @param oneEnd
	 *            a turtle
	 * @param otherEnd
	 *            a turtle
	 * @return link between two turtles
	 */
	public Link link(Turtle oneEnd, Turtle otherEnd) {
		return link(oneEnd.getWho(), otherEnd.getWho());
	}

	/**
	 * Returns the height of the world.
	 * 
	 * @return height of the world
	 */
	public int worldHeight() {
		return Utility.worldHeightU(this);
	}

	/**
	 * Returns the width of the world.
	 * 
	 * @return width of the world
	 */
	public int worldWidth() {
		return Utility.worldWidthU(this);
	}

	/**
	 * Returns the minimum x coordinate for all patches.
	 * 
	 * @return maximum x coordinate for patches
	 */
	public int getMinPxcor() {
		return Utility.getMinPxcorU(this);
	}

	/**
	 * Returns the maximum x coordinate for all patches.
	 * 
	 * @return maximum x coordinate for all patches
	 */
	public int getMaxPxcor() {
		return Utility.getMaxPxcorU(this);
	}

	/**
	 * Returns the minimum y coordinate for all patches.
	 * 
	 * @return maximum y coordinate for patches
	 */
	public int getMinPycor() {
		return Utility.getMinPycorU(this);
	}

	/**
	 * Returns the maximum y coordinate for all patches.
	 * 
	 * @return maximum y coordinate for all patches
	 */
	public int getMaxPycor() {
		return Utility.getMaxPycorU(this);
	}

	/**
	 * Diffuses to the eight neighboring patches a fraction number of the patch
	 * variable.
	 * 
	 * @param patchVarible
	 *            the name of a diffusible patch variable
	 * @param number
	 *            a number in the range [0,1]
	 */
	public void diffuse(String patchVariable, double number) {
		diff(patchVariable, number, true);
	}

	/**
	 * Diffuses to the four neighboring patches a fraction number of the patch
	 * variable.
	 * 
	 * @param patchVarible
	 *            the name of a diffusible patch variable
	 * @param number
	 *            a number number in the range [0,1]
	 */
	public void diffuse4(String patchVariable, double number) {
		diff(patchVariable, number, false);
	}

	private void diff(String patchVariable, double number, boolean isMoore) {
//		int possibleNumberOfNeighbors = isMoore ? 8 : 4;
		// String neighborsString = isMoore ? "neighbors" : "neighbors4"
		DenseDoubleMatrix2D ddm = getPatchVarMatrix(patchVariable);
		DenseDoubleMatrix2D ddm2 = new DenseDoubleMatrix2D(ddm.rows(),
				ddm.columns());
		Grid grid = getGrid();
		if (grid != null){
			boolean isPeriodic = grid.isPeriodic();
			JavaUtility.diffuse(ddm, ddm2, number, isMoore, isPeriodic);
			setPatchVarMatrix(patchVariable, ddm2);
		}
	}

	/**
	 * Multiply a diffusible patch variable of all patches by a number.
	 * 
	 * @param patchVariable
	 *            the name of a diffusible patch variable
	 * @param number
	 *            a number
	 */
	public void diffusibleMultiply(String patchVariable, Number number) {
		getPatchVarMatrix(patchVariable).assign(
				Functions.mult(number.doubleValue()));
	}

	/**
	 * Divide a diffusible patch variable of all patches by a number.
	 * 
	 * @param patchVariable
	 *            the name of a diffusible patch variable
	 * @param number
	 *            a number
	 */
	public void diffusibleDivide(String patchVariable, Number number) {
		getPatchVarMatrix(patchVariable).assign(
				Functions.div(number.doubleValue()));
	}

	/**
	 * Add a number to a diffusible patch variable of all patches.
	 * 
	 * @param patchVariable
	 *            the name of a diffusible patch variable
	 * @param number
	 *            a number
	 */
	public void diffusibleAdd(String patchVariable, Number number) {
		getPatchVarMatrix(patchVariable).assign(
				Functions.plus(number.doubleValue()));
	}

	/**
	 * Subtract a number from a diffusible patch variable of all patches.
	 * 
	 * @param patchVariable
	 *            the name of a diffusible patch variable
	 * @param number
	 *            a number
	 */
	public void diffusibleSubtract(String patchVariable, Number number) {
		getPatchVarMatrix(patchVariable).assign(
				Functions.minus(number.doubleValue()));
	}

	/**
	 * Apply a function to a diffusible patch variable of all patches.
	 * 
	 * @param patchVariable
	 *            the name of a diffusible patch variable
	 * @param function
	 *            a DoubleFunction function
	 */
	public void diffusibleApply(String patchVariable, DoubleFunction function) {
		getPatchVarMatrix(patchVariable).assign(function);
	}

	/**
	 * Stops an observer executing within a command closure.
	 * @deprecated use the {@link repast.simphony.relogo.Utility#stop()} method instead.
	 */
	@Deprecated
	public Stop oldStop() {
		return Stop.TRUE;
	}

	/**
	 * Returns the ReLogoAgent with the smallest value when operated on by a set of
	 * commands.
	 * 
	 * @param a
	 *            a collection
	 * @param closure
	 *            a set of commands
	 * @return ReLogoAgent with the smallest value when operated on by closure
	 */

	public ReLogoAgent minOneOf(Collection<? extends ReLogoAgent> a, Closure closure) {
		return Utility.minOneOfU(this, a, closure);
	}

	/**
	 * Returns an agentset consisting of a specified number of agents which have
	 * the lowest value when operated on by a set of commands.
	 * 
	 * @param number
	 *            an integer
	 * @param a
	 *            a collection
	 * @param closure
	 *            a set of commands
	 * @return agentset containing number agents with smallest values when
	 *         operated on by closure
	 */
	public AgentSet minNOf(int number, Collection<? extends ReLogoAgent> a,
			Closure closure) {
		return Utility.minNOfU(this, number, a, closure);
	}

	/**
	 * Returns the ReLogoAgent with the largest value when operated on by a set of
	 * commands.
	 * 
	 * @param a
	 *            a collection
	 * @param closure
	 *            a set of commands
	 * @return ReLogoAgent with the largest value when operated on by closure
	 */
	public ReLogoAgent maxOneOf(Collection<? extends ReLogoAgent> a, Closure closure) {
		return Utility.maxOneOfU(this, a, closure);
	}

	/**
	 * Returns an agentset consisting of a specified number of agents which have
	 * the greatest value when operated on by a set of commands.
	 * 
	 * @param number
	 *            an integer
	 * @param a
	 *            a collection
	 * @param closure
	 *            a set of commands
	 * @return agentset containing number agents with largest values when
	 *         operated on by closure
	 */
	public AgentSet maxNOf(int number, Collection<? extends ReLogoAgent> a,
			Closure closure) {
		return Utility.maxNOfU(this, number, a, closure);
	}

	/**
	 * Queries if all agents in a collection are true for a boolean closure.
	 * 
	 * @param a
	 *            a collection of ReLogoAgents
	 * @param closure
	 *            a boolean closure
	 * @return true or false based on whether all agents in a collection are true
	 *         for closure
	 */
	public boolean allQ(Collection a, Closure closure) {
		return Utility.allQU(this, a, closure);
	}

	/**
	 * Interprets a string as commands then runs the commands.
	 * 
	 * @param string
	 *            a string
	 */
	public void run(String string) {
		UtilityG.runU(string, this);
	}

	/**
	 * Interprets a string as a command then returns the result.
	 * 
	 * @param string
	 *            a string
	 * @return result of interpreting string
	 */
	public Object runresult(String string) {
		return UtilityG.runresultU(string, this);
	}

	private Map<String,PropertyChangeListener> modelParamsListenersMap = new HashMap<String,PropertyChangeListener>();
	
	/**
	 * {@inheritDoc}
	 */
	public void registerModelParameterListener(String varName, final Closure closure){
		ObservableMap oMap = (ObservableMap)ReLogoModel.getInstance().getModelParams();
		PropertyChangeListener pcl = modelParamsListenersMap.get(varName);
		if (pcl != null){
			oMap.removePropertyChangeListener(pcl);
		}
		pcl = new PropertyChangeListener(){

			public void propertyChange(PropertyChangeEvent evt) {
				closure.call(evt);
				
			}
			
		};		
		oMap.addPropertyChangeListener(varName, pcl);
		
	}
}
