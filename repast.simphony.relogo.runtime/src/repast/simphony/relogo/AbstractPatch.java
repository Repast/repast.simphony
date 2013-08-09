package repast.simphony.relogo;

import groovy.lang.Closure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections15.Predicate;
import org.apache.commons.collections15.PredicateUtils;

import repast.simphony.query.QueryUtils;
import repast.simphony.query.space.continuous.ContinuousWithin;
import repast.simphony.query.space.grid.MooreQuery;
import repast.simphony.query.space.grid.VNQuery;
import repast.simphony.relogo.factories.PatchFactory;
import repast.simphony.space.SpatialException;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.ui.probe.ProbeID;
import repast.simphony.util.collections.FilteredIterator;

public abstract class AbstractPatch implements Patch {

	private Object myselfObject;
	private Observer myObserver;
	private PatchFactory myPatchFactory;
	public double pcolor = Utility.black();
	public double plabelColor = Utility.white();
	Object plabel;

	public Observer getMyObserver() {
		return myObserver;
	}

	public void setMyObserver(Observer myObserver) {
		this.myObserver = myObserver;
	}

	public PatchFactory getMyPatchFactory() {
		return myPatchFactory;
	}

	public void setMyPatchFactory(PatchFactory myPatchFactory) {
		this.myPatchFactory = myPatchFactory;
	}

	/**
	 * Sets the agent that initiated the asking to the value o.
	 * 
	 * @param o
	 *            an object
	 * @exclude
	 */
	public void setMyself(Object o) {
		this.myselfObject = o;
	}

	/**
	 * The agent that initiated the asking.
	 * 
	 * @return the agent that initiated the asking
	 */
	public Object myself() {
		return myselfObject;
	}

	
	/**
	 * {@inheritDoc}
	 */
	public void setToDefault() {
		pcolor = Utility.black();
		plabel = null;
		plabelColor = Utility.white();
	}


	/**
	 * Returns the x coordinate of patch.
	 * 
	 * @return x coordinate of patch
	 */
	public int getPxcor() {
		return getGridLocation().getX();
	}

	/**
	 * Returns the y coordinate of patch.
	 * 
	 * @return y coordinate of patch
	 */
	public int getPycor() {
		return getGridLocation().getY();
	}

	public GridPoint getGridLocation() {
		return getMyObserver().getGrid().getLocation(this);
	}

	public int[] getXY() {
		int x = getPxcor() + getMinPxcor();
		int y = getPycor() + getMinPycor();
		int[] result = { x, y };
		return result;
	}

	private List rowAndColumn = null;

	/**
	 * Returns the turtle of the given number.
	 * 
	 * @param number
	 *            a number
	 * @return turtle number
	 */
	public Turtle turtle(Number number) {
		return Utility.turtleU(number.intValue(), getMyObserver());
	}

	/**
	 * Returns an agentset containing all turtles.
	 * 
	 * @return agentset of all turtles
	 */
	public AgentSet<Turtle> turtles() {
		return Utility.turtlesU(getMyObserver());
	}

	/**
	 * Returns a random x coordinate for turtles.
	 * 
	 * @return random x coordinate for turtles
	 */
	public double randomXcor() {
		return Utility.randomXcorU(getMyObserver());
	}

	/**
	 * Returns a random y coordinate for turtles.
	 * 
	 * @return random y coordinate for turtles
	 */
	public double randomYcor() {
		return Utility.randomYcorU(getMyObserver());
	}

	/**
	 * Returns the minimum x coordinate for all patches.
	 * 
	 * @return maximum x coordinate for patches
	 */
	public int getMinPxcor() {
		return Utility.getMinPxcorU(getMyObserver());
	}

	/**
	 * Returns the maximum x coordinate for all patches.
	 * 
	 * @return maximum x coordinate for all patches
	 */
	public int getMaxPxcor() {
		return Utility.getMaxPxcorU(getMyObserver());
	}

	/**
	 * Returns the minimum y coordinate for all patches.
	 * 
	 * @return maximum y coordinate for patches
	 */
	public int getMinPycor() {
		return Utility.getMinPycorU(getMyObserver());
	}

	/**
	 * Returns the maximum y coordinate for all patches.
	 * 
	 * @return maximum y coordinate for all patches
	 */
	public int getMaxPycor() {
		return Utility.getMaxPycorU(getMyObserver());
	}

	/**
	 * Returns a random x coordinate for patches.
	 * 
	 * @return random x coordinate for patches
	 */
	public int randomPxcor() {
		return Utility.randomPxcorU(getMyObserver());
	}

	/**
	 * Returns a random y coordinate for patches.
	 * 
	 * @return random y coordinate for patches
	 */
	public int randomPycor() {
		return Utility.randomPycorU(getMyObserver());
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
		return Utility.patchU(x, y, getMyObserver());
	}

	/**
	 * Returns an agentset containing all patches.
	 * 
	 * @return agentset containing all patches
	 */
	public AgentSet<Patch> patches() {
		return getMyObserver().patches();
	}

	public List getRowCol() {
		if (this.rowAndColumn == null) {
			this.rowAndColumn = new ArrayList();
			this.rowAndColumn.add(0, this.worldHeight()
					- (this.getPycor() - this.getMinPycor()) - 1);
			this.rowAndColumn.add(1, this.getPxcor() - this.getMinPxcor());
			// row = this.worldHeight() - (this.getPycor() - this.getMinPycor())
			// - 1;
			// column = this.getPxcor() - this.getMinPxcor();
		}
		return this.rowAndColumn;
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
		return Utility.linkU(oneEnd.intValue(), otherEnd.intValue(),
				getMyObserver());
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
	 * {@inheritDoc}
	 */
	public AgentSet<Link> allLinks() {
		return Utility.allLinksU(getMyObserver());
	}
	
	/**
	 * {@inheritDoc}
	 */
	public AgentSet<Link> links() {
		return Utility.linksU(getMyObserver());
	}

	/**
	 * Returns the height of the world.
	 * 
	 * @return height of the world
	 */
	public int worldHeight() {
		return Utility.worldHeightU(getMyObserver());
	}

	/**
	 * Returns the width of the world.
	 * 
	 * @return width of the world
	 */
	public int worldWidth() {
		return Utility.worldWidthU(getMyObserver());
	}

	/**
	 * Does nothing, included for translation compatibility.
	 */
	public void watchMe() {

	}

	public NdPoint getGridLocationAsNdPoint() {
		GridPoint loc = getGridLocation();
		return new NdPoint(loc.getX(), loc.getY());
	}

	/**
	 * Returns the color of a patch.
	 * 
	 * @return color of a patch in the range [0, 140)
	 */
	public double getPcolor() {
		return pcolor;
	}

	/**
	 * Sets the color of a patch to pcolor.
	 * 
	 * @param pcolor
	 *            a number in range [0,140)
	 */
	public void setPcolor(double pcolor) {
		this.pcolor = Utility.wrapColor(pcolor);
	}

	/**
	 * Returns the patch label.
	 * 
	 * @return the patch label
	 */
	public Object getPlabel() {
		return plabel;
	}

	/**
	 * Sets the patch label.
	 * 
	 * @param plabel
	 *            a label
	 */
	public void setPlabel(Object plabel) {
		this.plabel = plabel;
	}

	/**
	 * Returns the patch label color.
	 * 
	 * @return the patch label color
	 */
	public double getPlabelColor() {
		return plabelColor;
	}

	/**
	 * Sets the patch label color.
	 * 
	 * @param plabelColor
	 *            a numerical color
	 */
	public void setPlabelColor(Number plabelColor) {
		this.plabelColor = Utility.wrapColor(plabelColor.doubleValue());
	}

	/**
	 * Returns the distance from the caller to a turtle.
	 * 
	 * @param t
	 *            a turtle
	 * @returns distance from turtle t to the caller
	 */
	public double distance(Turtle t) {
		return getMyObserver().getSpace().getDistance(
				this.getGridLocationAsNdPoint(), t.getTurtleLocation());
	}

	/**
	 * Returns the distance from the caller to a patch.
	 * 
	 * @param p
	 *            a patch
	 * @returns distance from patch p to the caller
	 */
	public double distance(Patch p) {
		return getMyObserver().getSpace().getDistance(
				this.getGridLocationAsNdPoint(), p.getGridLocationAsNdPoint());
	}

	/**
	 * Returns the distance from the caller to a point.
	 * 
	 * @param nx
	 *            a number
	 * @param ny
	 *            a number
	 * @returns distance from the caller to the point (nx,ny)
	 */
	public double distancexy(Number nx, Number ny) {
		double x = nx.doubleValue();
		double y = ny.doubleValue();
		return getMyObserver().getSpace().getDistance(
				this.getGridLocationAsNdPoint(), new NdPoint(x, y));
	}

	/**
	 * Returns this turtle, patch, or link.
	 * 
	 * @return this turtle, patch, or link.
	 */
	public Patch self() {
		return this;
	}

	private AgentSet<Patch> mooreNeighbors2D(int... extents){
		MooreQuery mq = new MooreQuery(getMyObserver().getGrid(), this, extents);
		Iterable i = mq.query();
		Predicate pr = PredicateUtils.andPredicate(PredicateUtils
				.instanceofPredicate(Patch.class), PredicateUtils
				.notPredicate(PredicateUtils.equalPredicate(this)));
		FilteredIterator<Patch> patchesAt = new FilteredIterator<Patch>(
				i.iterator(), pr);
		return Utility.agentSetFromIterator(patchesAt);
	}
	/**
	 * Returns the agentset of the eight neighboring patches (Moore
	 * neighborhood).
	 * 
	 * @return agentset of the eight neighboring patches
	 */
	public AgentSet<Patch> neighbors() {
		return mooreNeighbors2D(1,1);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public AgentSet<Patch> neighbors(int extent){
		return mooreNeighbors2D(extent,extent);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public AgentSet<Patch> neighbors(int extentX, int extentY){
		return mooreNeighbors2D(extentX,extentY);
	}

	private AgentSet<Patch> vnNeighbors2D(int... extents){
		VNQuery mq = new VNQuery(getMyObserver().getGrid(), this, extents);
		Iterable i = mq.query();
		Predicate pr = PredicateUtils.andPredicate(PredicateUtils
				.instanceofPredicate(Patch.class), PredicateUtils
				.notPredicate(PredicateUtils.equalPredicate(this)));
		FilteredIterator<Patch> patchesAt = new FilteredIterator<Patch>(
				i.iterator(), pr);
		return Utility.agentSetFromIterator(patchesAt);
	}
	
	/**
	 * Returns the agentset of the four neighboring patches (von Neumann
	 * neighborhood).
	 * 
	 * @return agentset of the four neighboring patches
	 */
	public AgentSet<Patch> neighbors4() {
		return vnNeighbors2D(1,1);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public AgentSet<Patch> neighbors4(int extent){
		return vnNeighbors2D(extent,extent);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public AgentSet<Patch> neighbors4(int extentX, int extentY){
		return vnNeighbors2D(extentX,extentY);
	}

	/**
	 * Returns an agentset within a distance of the caller.
	 * 
	 * @param a
	 *            a collection of agents
	 * @param num
	 *            a distance
	 * @return agentset subset of collection a within a distance num of the caller
	 */
	public AgentSet inRadius(Collection a, Number num) {
		ContinuousWithin cwq = new ContinuousWithin(getMyObserver().getSpace(),
				this, num.doubleValue());
		Predicate pr = QueryUtils.createContains(cwq.query());
		FilteredIterator result = new FilteredIterator(a.iterator(), pr);
		List temp = new ArrayList();
		while (result.hasNext()) {
			temp.add(result.next());
		}
		return new AgentSet(temp);
	}

	/**
	 * Returns an agentset minus the caller.
	 * 
	 * @param a
	 *            an agentset
	 * @return agentset a minus the caller
	 */
	public AgentSet other(Collection a) {
		return Utility.removePatchFromCollection(a, (Patch) this);
	}

	/**
	 * Returns the agentset on the patch at the direction (ndx, ndy) from the
	 * caller.
	 * 
	 * @param ndx
	 *            a number
	 * @param ndy
	 *            a number
	 * @returns agentset at the direction (ndx, ndy) from the caller
	 */
	public AgentSet<Turtle> turtlesAt(Number ndx, Number ndy) {
		double dx = ndx.doubleValue();
		double dy = ndy.doubleValue();
		double[] displacement = { dx, dy };
		try {
			GridPoint gridPoint = Utility.getGridPointAtDisplacement(
					getGridLocationAsNdPoint(), displacement, getMyObserver());
			return Utility.getTurtlesOnGridPoint(gridPoint, getMyObserver());
		} catch (SpatialException e) {
			return null;
		}
	}

	/**
	 * Returns an agentset of turtles from the patch of the caller.
	 * 
	 * @return agentset of turtles from the caller's patch
	 */
	public AgentSet<Turtle> turtlesHere() {
		// get grid and space
		Grid grid = getMyObserver().getGrid();
		// get the turtle's grid location
		GridPoint gridPoint = grid.getLocation(this);
		// get all the turtles at the grid location
		return Utility.getTurtlesOnGridPoint(gridPoint, getMyObserver());
	}

	/**
	 * Returns an agentset of turtles on a given patch.
	 * 
	 * @param p
	 *            a patch
	 * @return agentset of turtles on patch p
	 */
	public AgentSet<Turtle> turtlesOn(Patch p) {
		return Utility.getTurtlesOnGridPoint(p.getGridLocation(),
				getMyObserver());
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
				Utility.ndPointToGridPoint(t.getTurtleLocation()),
				getMyObserver());
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
						Utility.ndPointToGridPoint(location), getMyObserver());
				total.addAll(temp);
			}
		} else {
			for (Object p : a) {
				GridPoint location = ((Patch) p).getGridLocation();
				AgentSet temp = Utility.getTurtlesOnGridPoint(location,
						getMyObserver());
				total.addAll(temp);
			}
		}
		return new AgentSet(total);
	}

	/**
	 * Returns the patch at a direction (ndx, ndy) from the caller.
	 * 
	 * @param ndx
	 *            a number
	 * @param ndy
	 *            a number
	 * @return patch at a direction (ndx, ndy) from the caller
	 */
	public Patch patchAt(Number ndx, Number ndy) {
		double dx = ndx.doubleValue();
		double dy = ndy.doubleValue();
		double[] displacement = { dx, dy };
		try {
			return Utility.getPatchAtLocation(Utility
					.getGridPointAtDisplacement(
							this.getGridLocationAsNdPoint(), displacement,
							getMyObserver()), getMyObserver());
		} catch (SpatialException e) {
			return null;
		}
	}

	/**
	 * Returns the patch that is at a direction and distance from the caller.
	 * 
	 * @param heading
	 *            a number
	 * @param distance
	 *            a number
	 * @return patch that is at heading and distance from the caller
	 */
	public Patch patchAtHeadingAndDistance(Number heading, Number distance) {
		double[] displacement = Utility.getDisplacementFromHeadingAndDistance(
				heading.doubleValue(), distance.doubleValue());
		try {
			return Utility.getPatchAtLocation(Utility
					.getGridPointAtDisplacement(
							this.getGridLocationAsNdPoint(), displacement,
							getMyObserver()), getMyObserver());
		} catch (SpatialException e) {
			return null;
		}
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
	 * @exclude
	 */
	public void askPatch(Closure cl) {
		cl.setResolveStrategy(Closure.DELEGATE_FIRST);
		cl.setDelegate(this);
		cl.call(this);
	}

	/**
	 * Makes a number of random new turtles.
	 * 
	 * @param number
	 *            a number
	 * @return created turtles
	 */
	public AgentSet<Turtle> sprout(Number number) {
		return sprout(number, null);
	}

	/**
	 * Makes a number of random new turtles then executes a set of commands on
	 * the created turtles.
	 * 
	 * @param number
	 *            a number
	 * @param closure
	 *            a set of commands
	 * @return created turtles
	 */
	public AgentSet<Turtle> sprout(Number number, Closure closure) {
		return sprout(number, null, "default");
	}

	/**
	 * Makes a number of random new turtles of a specific type then executes a
	 * set of commands on the created turtles.
	 * 
	 * @param number
	 *            a number
	 * @param closure
	 *            a set of commands
	 * @param turtleType
	 *            a turtle type
	 * @return created turtles
	 */
	public AgentSet<Turtle> sprout(Number number, Closure closure, String turtleType) {

		AgentSet<Turtle> newTurtles = new AgentSet<Turtle>();

		for (int i = 0; i < number.intValue(); i++) {
			newTurtles.add(getMyObserver().getTurtleFactory().createTurtle(
					turtleType, this.getGridLocationAsNdPoint()));
		}
		if (closure != null) {
			ask(newTurtles, closure);
		}
		return newTurtles;
	}
	
	/**
	 * Makes a number of random new turtles of a specific type then executes a
	 * set of commands on the created turtles.
	 * 
	 * @param number
	 *            a number
	 * @param closure
	 *            a set of commands
	 * @param turtleType
	 *            a turtle class
	 * @return created turtles
	 */
	public AgentSet<Turtle> sprout(Number number, Closure closure, Class turtleType) {
		return sprout(number, closure, turtleType.getSimpleName());
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
	 * @method toString
	 *
	 */
	public String toString() {
		return "patch " + getPxcor() + " " + getPycor();
	}

	public int compareTo(Patch p) {
		int thisPxcor = this.getPxcor();
		int thisPycor = this.getPycor();
		int pPxcor = p.getPxcor();
		int pPycor = p.getPycor();
		if (thisPxcor == pPxcor && thisPycor == pPycor)
			return 0;
		if (thisPycor != pPycor)
			return (thisPycor > pPycor ? -1 : 1);
		else {
			return (thisPxcor < pPxcor ? -1 : 1);
		}
	}

	/**
	 * Stops a patch executing within a command closure.
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

}
