package repast.simphony.relogo;

import java.util.Collection;
import java.util.List;

import groovy.lang.Closure;

import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.GridPoint;

/**
 * ReLogo Patch interface.
 * 
 * @author jozik
 *
 */
public interface Patch extends Comparable<Patch>, ReLogoAgent {

	public String toString();

	/**
	 * Returns the agentset of all generic links.
	 * 
	 * @return agentset of all links
	 */
	public AgentSet<Link> links();

	/**
	 * Returns the agentset of all links.
	 * 
	 * @return agentset of all links
	 */
	public AgentSet<Link> allLinks();
	
	/**
	 * The agent that initiated the asking.
	 * 
	 * @return the agent that initiated the asking
	 */
	public Object myself();

	/**
	 * Returns this turtle, patch, or link.
	 * 
	 * @return this turtle, patch, or link.
	 */
	public Patch self();

	/**
	 * Returns the x coordinate of patch.
	 * 
	 * @return x coordinate of patch
	 */
	public int getPxcor();

	/**
	 * Returns the y coordinate of patch.
	 * 
	 * @return y coordinate of patch
	 */
	public int getPycor();

	public GridPoint getGridLocation();

	public NdPoint getGridLocationAsNdPoint();

	/**
	 * Returns the color of a patch.
	 * 
	 * @return color of a patch in the range [0, 140)
	 */
	public double getPcolor();

	/**
	 * Returns the patch label.
	 * 
	 * @return the patch label
	 */
	public Object getPlabel();

	/**
	 * Sets the patch label.
	 * 
	 * @param plabel
	 *            a label
	 */
	public void setPlabel(Object plabel);

	/**
	 * Returns the patch label color.
	 * 
	 * @return the patch label color
	 */
	public double getPlabelColor();

	/**
	 * Sets the patch label color.
	 * 
	 * @param plabelColor
	 *            a numerical color
	 */
	public void setPlabelColor(Number plabelColor);

	/**
	 * Returns the patch containing a point.
	 * 
	 * @param nX
	 *            x coordinate
	 * @param nY
	 *            y coordinate
	 * @return patch that contains the point (nX, nY)
	 */
	public Patch patch(Number nX, Number nY);

	/**
	 * Returns an agentset containing all patches.
	 * 
	 * @return agentset containing all patches
	 */
	public AgentSet<Patch> patches();

	/**
	 * Returns an agentset of turtles on a given patch.
	 * 
	 * @param p
	 *            a patch
	 * @return agentset of turtles on patch p
	 */
	public AgentSet<Turtle> turtlesOn(Patch p);

	/**
	 * Returns an agentset of turtles on the same patch as a turtle.
	 * 
	 * @param t
	 *            a turtle
	 * @return agentset of turtles on the same patch at turtle t
	 */
	public AgentSet<Turtle> turtlesOn(Turtle t);

	/**
	 * Returns an agentset of turtles on the patches in a collection or on the
	 * patches that a collection of turtles are.
	 * 
	 * @param a
	 *            a collection
	 * @return agentset of turtles on the patches in collection a or on the
	 *         patches that collection a turtles are
	 */
	public AgentSet<Turtle> turtlesOn(Collection a);

	/**
	 * Sets the color of a patch to pcolor.
	 * 
	 * @param pcolor
	 *            a number in range [0,140)
	 */
	public void setPcolor(double pcolor);

	/**
	 * Returns the distance from the caller to a turtle.
	 * 
	 * @param t
	 *            a turtle
	 * @returns distance from turtle t to the caller
	 */
	public double distance(Turtle t);

	/**
	 * Returns the distance from the caller to a patch.
	 * 
	 * @param p
	 *            a patch
	 * @returns distance from patch p to the caller
	 */
	public double distance(Patch p);

	/**
	 * Returns the distance from the caller to a point.
	 * 
	 * @param nx
	 *            a number
	 * @param ny
	 *            a number
	 * @returns distance from the caller to the point (nx,ny)
	 */
	public double distancexy(Number nx, Number ny);

	/**
	 * Returns the agentset of the eight neighboring patches (Moore
	 * neighborhood).
	 * 
	 * @return agentset of the eight neighboring patches
	 */
	public AgentSet<Patch> neighbors();
	
	/**
	 * Returns the agentset of patches making up the Moore
	 * neighborhood of this patch (excluding the calling patch)
	 * of extent.
	 * 
	 * @param extent the x and y Moore extents
	 * 
	 * @return Moore agentset of patches with extent
	 */
	public AgentSet<Patch> neighbors(int extent);
	
	/**
	 * Returns the agentset of patches making up the Moore
	 * neighborhood of this patch (excluding the calling patch)
	 * of extents.
	 * 
	 * @param extentX the x Moore extent
	 * 
	 * @param extentY the y Moore extent
	 * 
	 * @return Moore agentset of patches with extents
	 */
	public AgentSet<Patch> neighbors(int extentX, int extentY);

	/**
	 * Returns the agentset of the four neighboring patches (von Neumann
	 * neighborhood).
	 * 
	 * @return agentset of the four neighboring patches
	 */
	public AgentSet<Patch> neighbors4();
	
	/**
	 * Returns the agentset of patches making up the von Neumann
	 * neighborhood of this patch (excluding the calling patch)
	 * of extent.
	 * 
	 * @param extent the x and y von Neumann extents
	 * 
	 * @return von Neumann agentset of patches with extent
	 */
	public AgentSet<Patch> neighbors4(int extent);
	
	/**
	 * Returns the agentset of patches making up the von Neumann
	 * neighborhood of this patch (excluding the calling patch)
	 * of extents.
	 * 
	 * @param extentX the x von Neumann extent
	 * 
	 * @param extentY the y von Neumann extent
	 * 
	 * @return von Neumann agentset of patches with extents
	 */
	public AgentSet<Patch> neighbors4(int extentX, int extentY);

	/**
	 * Returns an agentset minus the caller.
	 * 
	 * @param a
	 *            an agentset
	 * @return agentset a minus the caller
	 */
	public AgentSet other(Collection a);

	/**
	 * Returns the patch at a direction (ndx, ndy) from the caller.
	 * 
	 * @param ndx
	 *            a number
	 * @param ndy
	 *            a number
	 * @return patch at a direction (ndx, ndy) from the caller
	 */
	public Patch patchAt(Number ndx, Number ndy);

	/**
	 * Returns the patch that is at a direction and distance from the caller.
	 * 
	 * @param heading
	 *            a number
	 * @param distance
	 *            a number
	 * @return patch that is at heading and distance from the caller
	 */
	public Patch patchAtHeadingAndDistance(Number heading, Number distance);

	/**
	 * Executes a set of commands for an agentset in random order.
	 * 
	 * @param a
	 *            an agentset
	 * @param askBlock
	 *            a set of commands
	 */
	public void ask(AgentSet<? extends ReLogoAgent> a, Closure askBlock);

	/**
	 * Executes a set of commands for a collection of agents.
	 * 
	 * @param c
	 *            a collection of agents
	 * @param askBlock
	 *            a set of commands
	 */
	public void ask(Collection<? extends ReLogoAgent> c, Closure askBlock);

	/**
	 * Executes a set of commands for a turtle.
	 * 
	 * @param t
	 *            a turtle
	 * @param askBlock
	 *            a set of commands
	 */
	public void ask(Turtle t, Closure askBlock);

	/**
	 * Executes a set of commands for a patch.
	 * 
	 * @param p
	 *            a patch
	 * @param askBlock
	 *            a set of commands
	 */
	public void ask(Patch p, Closure askBlock);

	/**
	 * Executes a set of commands for a link.
	 * 
	 * @param l
	 *            a link
	 * @param askBlock
	 *            a set of commands
	 */
	public void ask(Link l, Closure askBlock);

	/**
	 * @exclude
	 */
	public void askPatch(Closure cl);

	/**
	 * Makes a number of random new turtles.
	 * 
	 * @param number
	 *            a number
	 * @return created turtles
	 */
	public AgentSet<Turtle> sprout(Number number);

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
	public AgentSet<Turtle> sprout(Number number, Closure closure);

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
	public AgentSet<Turtle> sprout(Number number, Closure closure, String turtleType);
	
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
	public AgentSet<Turtle> sprout(Number number, Closure closure, Class turtleType);

	/**
	 * Prints value with agent identifier to current file with a newline.
	 * 
	 * @param value
	 *            any object
	 */
	public void fileShow(Object value);

	/**
	 * Prints value with agent identifier to the console.
	 * 
	 * @param value
	 *            any object
	 */
	public void show(Object value);

	/**
	 * Sets the standard patch variables to their default values.
	 * Called by clearPatches() and cp().
	 * Override and call super.setToDefault() to reset other variables.
	 */
	public void setToDefault();
	
	/**
	 * Stops a patch executing within a command closure.
	 * @deprecated use the {@link repast.simphony.relogo.Utility#stop()} method instead.
	 */
	@Deprecated
	public Stop oldStop();
	

	public List getRowCol();

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
	public boolean allQ(Collection a, Closure closure);

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
	public ReLogoAgent minOneOf(Collection<? extends ReLogoAgent> a, Closure closure);

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
			Closure closure);

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
	public ReLogoAgent maxOneOf(Collection<? extends ReLogoAgent> a,Closure closure);

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
			Closure closure);


	/**
	 * Returns an agentset within a distance of the caller.
	 * 
	 * @param c
	 *            a collection of agents
	 * @param num
	 *            a distance
	 * @return agentset subset of collection a within a distance num of the caller
	 */
	public AgentSet inRadius(Collection a, Number num);

	/**
	 * Returns the minimum x coordinate for all patches.
	 * 
	 * @return maximum x coordinate for patches
	 */
	public int getMinPxcor();

	/**
	 * Returns the maximum x coordinate for all patches.
	 * 
	 * @return maximum x coordinate for all patches
	 */
	public int getMaxPxcor();

	/**
	 * Returns the minimum y coordinate for all patches.
	 * 
	 * @return maximum y coordinate for patches
	 */
	public int getMinPycor();

	/**
	 * Returns the maximum y coordinate for all patches.
	 * 
	 * @return maximum y coordinate for all patches
	 */
	public int getMaxPycor();

	/**
	 * Returns the link between two turtles.
	 * 
	 * @param oneEnd
	 *            an integer
	 * @param otherEnd
	 *            an integer
	 * @return link between two turtles
	 */
	public Link link(Number oneEnd, Number otherEnd);

	/**
	 * Returns the link between two turtles.
	 * 
	 * @param oneEnd
	 *            a turtle
	 * @param otherEnd
	 *            a turtle
	 * @return link between two turtles
	 */
	public Link link(Turtle oneEnd, Turtle otherEnd);

	/**
	 * Returns a random x coordinate for patches.
	 * 
	 * @return random x coordinate for patches
	 */
	public int randomPxcor();

	/**
	 * Returns a random y coordinate for patches.
	 * 
	 * @return random y coordinate for patches
	 */
	public int randomPycor();

	/**
	 * Returns a random x coordinate for turtles.
	 * 
	 * @return random x coordinate for turtles
	 */
	public double randomXcor();

	/**
	 * Returns a random y coordinate for turtles.
	 * 
	 * @return random y coordinate for turtles
	 */
	public double randomYcor();

	/**
	 * Interprets a string as commands then runs the commands.
	 * 
	 * @param string
	 *            a string
	 */
	public void run(String string);

	/**
	 * Interprets a string as a command then returns the result.
	 * 
	 * @param string
	 *            a string
	 * @return result of interpreting string
	 */
	public Object runresult(String string);
	
	/**
	 * Returns the turtle of the given number.
	 * 
	 * @param number
	 *            a number
	 * @return turtle number
	 */
	public Turtle turtle(Number number);
	
	/**
	 * Returns an agentset containing all turtles.
	 * 
	 * @return agentset of all turtles
	 */
	public AgentSet<Turtle> turtles();

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
	public AgentSet<Turtle> turtlesAt(Number ndx, Number ndy);
	
	/**
	 * Returns an agentset of turtles from the patch of the caller.
	 * 
	 * @return agentset of turtles from the caller's patch
	 */
	public AgentSet<Turtle> turtlesHere();
	
	/**
	 * Returns the height of the world.
	 * 
	 * @return height of the world
	 */
	public int worldHeight();

	/**
	 * Returns the width of the world.
	 * 
	 * @return width of the world
	 */
	public int worldWidth();
	
	/**
	 * Does nothing, included for translation compatibility.
	 */
	public void watchMe();
}
