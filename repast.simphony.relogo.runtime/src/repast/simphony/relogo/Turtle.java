package repast.simphony.relogo;

import groovy.lang.Closure;

import java.util.Collection;
import java.util.List;

import repast.simphony.space.continuous.NdPoint;

/**
 * ReLogo Turtle interface.
 * 
 * @author jozik
 *
 */
public interface Turtle extends Comparable<Turtle>, ReLogoAgent,
		OutOfContextSubject<Turtle>, OutOfContextSubscriber<Turtle> {

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
	public Turtle self();

	/**
	 * Makes a number of new turtles.
	 * 
	 * @param number
	 *            a number
	 * @return created turtles
	 */
	public AgentSet<Turtle> hatch(Number number);

	/**
	 * Makes a number of new turtles and then executes a set of commands on the
	 * created turtles.
	 * 
	 * @param number
	 *            a number
	 * @param closure
	 *            a set of commands
	 * @return created turtles
	 */
	public AgentSet<Turtle> hatch(Number number, Closure closure);

	/**
	 * Makes a number of new turtles of a specific type and then executes a set
	 * of commands on the created turtles.
	 * 
	 * @param number
	 *            a number
	 * @param closure
	 *            a set of commands
	 * @param childType
	 *            a string
	 * 
	 * @param number
	 * @param closure
	 * @param childType
	 * @return created turtles
	 * 
	 */
	public AgentSet<Turtle> hatch(Number number, Closure closure, String childType);

	/**
	 * Makes a number of new turtles of a specific type and then executes a set
	 * of commands on the created turtles.
	 * 
	 * @param number
	 *            a number
	 * @param closure
	 *            a set of commands
	 * @param childType
	 *            a turtle class
	 * 
	 * @param number
	 * @param closure
	 * @param childType
	 * @return created turtles
	 */
	public AgentSet<Turtle> hatch(Number number, Closure closure, Class childType);

	/**
	 * Moves the turtle to the lowest value of a patch variable of eight
	 * neighboring patches.
	 * 
	 * @param patchVariable
	 *            a string
	 */
	public void downhill(String patchVariable);

	/**
	 * Moves the turtle to the lowest value of a patch variable of four
	 * neighboring patches.
	 * 
	 * @param patchVariable
	 *            a string
	 */
	public void downhill4(String patchVariable);

	/**
	 * Moves the turtle to the highest valued patch of patchVariable in the
	 * eight neighboring patches plus the current patch.
	 * 
	 * @param patchVariable
	 *            a string
	 */
	public void uphill(String patchVariable);

	/**
	 * Moves the turtle to the highest valued patch of patchVariable in the four
	 * neighboring patches plus the current patch.
	 * 
	 * @param patchVariable
	 *            a string
	 */
	public void uphill4(String patchVariable);

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
	public void askTurtle(Closure cl);

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
	 * 
	 * This method provides a human-readable name for the agent.
	 * 
	 * @method toString
	 * 
	 */

	public String toString();

	/**
	 * Returns the x coordinate of a turtle.
	 * 
	 * @return present x coordinate of the turtle
	 */
	public double getXcor();

	/**
	 * Returns the y coordinate of a turtle.
	 * 
	 * @return y coordinate of a turtle
	 */
	public double getYcor();

	public NdPoint getTurtleLocation();

	/**
	 * Sets the color of a turtle to the value color.
	 * 
	 * @param color
	 *            a number
	 */
	public void setColor(Number color);

	/**
	 * Returns the heading of the turtle.
	 * 
	 * @ return heading of the turtle in the range [0, 360)
	 */
	public double getHeading();

	public double getHeadingInRads();

	/**
	 * Sets the heading of the turtle to nNum.
	 * 
	 * @param nNum
	 *            a number in degrees
	 */
	public void setHeading(Number num);

	/**
	 * Returns the color of a turtle.
	 * 
	 * @ returns color of a turtle in the range [0, 140)
	 */
	public double getColor();

	/**
	 * Returns the color of patch here.
	 * 
	 * @return color of patch here in the range [0, 140)
	 */
	public double getPcolor();

	/**
	 * Sets the color of a patch here to pcolor.
	 * 
	 * @param pcolor
	 *            a number in range [0,140)
	 */
	public void setPcolor(Number pcolor);

	/**
	 * Queries if caller is hidden.
	 * 
	 * @return true or false based on whether caller is hidden
	 */
	public boolean isHiddenQ();

	/**
	 * Sets turtle visibility.
	 * 
	 * @param hidden
	 *            boolean visibility
	 */
	public void setHiddenQ(boolean hidden);

	/**
	 * Returns the label.
	 * 
	 * @return the turtle label
	 */
	public Object getLabel();

	/**
	 * Sets the label.
	 * 
	 * @param label
	 *            a label
	 */
	public void setLabel(Object label);

	/**
	 * Returns the label color for a turtle or link.
	 * 
	 * @ returns label color in the range [0, 140)
	 */
	public double getLabelColor();

	/**
	 * Sets the label color for a turtle to labelColor.
	 * 
	 * @param labelColor
	 *            a number in range [0, 140)
	 */
	public void setLabelColor(Number labelColor);

	/**
	 * Returns the pen setting of a turtle.
	 * 
	 * @return pen setting of a turtle
	 */
	public int getPenMode();

	/**
	 * Sets the pen setting of a turtle to penMode.
	 * 
	 * @param penMode
	 *            a pen mode, PEN_UP or PEN_DOWN
	 */
	public void setPenMode(Number penMode);

	/**
	 * Sets the turtle's pen to stop drawing lines.
	 */
	public void pu();

	/**
	 * Sets the turtle's pen to stop drawing lines.
	 */
	public void penUp();

	/**
	 * Sets the turtle's pen to draw lines.
	 */
	public void pd();

	/**
	 * Sets the turtle's pen to draw lines.
	 */
	public void penDown();

	/**
	 * Does nothing, included for translation compatibility.
	 */
	public void pe();

	/**
	 * Does nothing, included for translation compatibility.
	 */
	public void penErase();

	/**
	 * Returns the pen width of a turtle.
	 * 
	 * @return pen width of a turtle
	 */
	public int getPenSize();

	/**
	 * Sets the pen width of a turtle to penSize.
	 * 
	 * @param penSize
	 *            a number
	 */
	public void setPenSize(Number penSize);

	/**
	 * Returns the shape of a turtle.
	 * 
	 * @return shape of a turtle
	 */
	public String getShape();

	/**
	 * Sets the shape of a turtle to shape.
	 * 
	 * @param shape
	 *            a string
	 */
	public void setShape(String shape);

	public boolean isShapeChanged();

	public void setShapeChanged(boolean changed);

	/**
	 * Returns the size of a turtle.
	 * 
	 * @return size of a turtle
	 */
	public double getSize();

	/**
	 * Sets the size of a turtle to size.
	 * 
	 * @param size
	 *            a number
	 */
	public void setSize(Number size);

	/**
	 * Returns the id number of a turtle.
	 * 
	 * @return id number of a turtle
	 */
	public int getWho();

	

	/**
	 * Rotates the turtle to the right num degrees.
	 * 
	 * @param num
	 *            an angle in degrees in the range [0, 360]
	 */
	public void rt(Number num);

	/**
	 * Rotates the turtle to the right num degrees.
	 * 
	 * @param num
	 *            an angle in degrees in the range [0, 360]
	 */
	public void right(Number num);

	/**
	 * Rotates the turtle to the left num degrees.
	 * 
	 * @param num
	 *            an angle in degrees in the range [0, 360]
	 */
	public void lt(Number num);

	/**
	 * Rotates the turtle to the left num degrees.
	 * 
	 * @param num
	 *            an angle in degrees in the range [0, 360]
	 */
	public void left(Number num);

	/**
	 * Steps turtle forward by a distance.
	 * 
	 * @param num
	 *            a distance
	 */
	public void fd(Number num);

	/**
	 * Steps turtle forward by a distance.
	 * 
	 * @param num
	 *            a distance
	 */
	public void forward(Number num);

	/**
	 * Steps turtle backwards by a distance.
	 * 
	 * @param num
	 *            a distance
	 */
	public void bk(Number num);

	/**
	 * Steps turtle backwards by a distance.
	 * 
	 * @param num
	 *            a distance
	 */
	public void back(Number num);

	/**
	 * Moves turtle forward num units.
	 * 
	 * @param num
	 *            a number
	 */
	public void jump(Number num);

	public void mv(Number number);

	/**
	 * Sets the x-y coordinates of a turtle to (nX, nY).
	 * 
	 * @param nX
	 *            a number
	 * @param nY
	 *            a number
	 */
	public void setxy(Number x, Number y);

	/**
	 * Sets the x coordinate of a turtle to number.
	 * 
	 * @param number
	 *            a number
	 */
	public void setXcor(Number number);

	/**
	 * Sets the y coordinate of a turtle to number.
	 * 
	 * @param number
	 *            a number
	 */
	public void setYcor(Number number);

	/**
	 * Removes the turtle.
	 */
	public void die();

	/**
	 * Turtle appears hidden.
	 */
	public void ht();

	/**
	 * Turtle appears hidden.
	 */
	public void hideTurtle();

	/**
	 * Makes turtle visible.
	 */
	public void st();

	/**
	 * Makes turtle visible.
	 */
	public void showTurtle();

	/**
	 * Turtle goes to (0,0).
	 */
	public void home();

	/**
	 * Returns the turtle's x increment for one step.
	 * 
	 * @returns turtle's x increment for one step
	 */
	public double dx();

	/**
	 * Returns the turtle's y increment for one step.
	 * 
	 * @returns turtle's y increment for one step
	 */
	public double dy();

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
	 * Returns the direction to turtle t.
	 * 
	 * @param t
	 *            a turtle
	 * @return direction from this turtle to turtle t
	 */
	public double towards(Turtle t);

	/**
	 * Returns the direction to patch p.
	 * 
	 * @param p
	 *            a patch
	 * @return direction from this patch to patch p
	 */
	public double towards(Patch p);

	/**
	 * Returns the direction from a turtle or patch to a point.
	 * 
	 * @param x
	 *            a number
	 * @param y
	 *            a number
	 * @return direction from a turtle or patch to the point (x, y)
	 */
	public double towardsxy(Number x, Number y);

	/**
	 * Faces the caller towards a turtle.
	 * 
	 * @param t
	 *            a turtle
	 */
	public void face(Turtle t);

	/**
	 * Faces the caller towards a patch.
	 * 
	 * @param p
	 *            a patch
	 */
	public void face(Patch p);

	/**
	 * Faces the caller towards a point.
	 * 
	 * @param nX
	 *            x coordinate
	 * @param nY
	 *            y coordinate
	 */
	public void facexy(Number nX, Number nY);

	/**
	 * Queries if turtle can move a distance.
	 * 
	 * @param dist
	 *            a distance
	 * @return true or false based on whether turtle can move distance
	 */
	public boolean canMoveQ(Number dist);

	/**
	 * Moves a turtle to the same location as turtle t.
	 * 
	 * @param t
	 *            a turtle
	 */
	public void moveTo(Turtle t);

	/**
	 * Moves a turtle to the same location as patch p.
	 * 
	 * @param p
	 *            a patch
	 */
	public void moveTo(Patch p);

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
	 * Returns an agentset of turtles on the patches in a collection or on the patches
	 * that a collection of turtles are.
	 * 
	 * @param a
	 *            a collection
	 * @return agentset of turtles on the patches in collection a or on the patches
	 *         that collection a turtles are
	 */
	public AgentSet<Turtle> turtlesOn(Collection a);

	/**
	 * Returns the patch where the turtle is located.
	 * 
	 * @return patch where the turtle is located
	 */
	public Patch patchHere();

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
	 * Returns the patch that is at a distance ahead of a turtle.
	 * 
	 * @param distance
	 *            a number
	 * @return patch that is at distance from the calling turtle
	 */
	public Patch patchAhead(Number distance);

	/**
	 * Returns the patch at a direction (nX, nY) from the caller.
	 * 
	 * @param nX
	 *            a number
	 * @param nY
	 *            a number
	 * @return patch at a direction (nX, nY) from the caller
	 */
	public Patch patchAt(Number nX, Number xY);

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
	 * Returns the patch that is at a distance and degrees left from the caller.
	 * 
	 * @param nAngle
	 *            an angle in degrees
	 * @param nDistance
	 *            a number
	 * @return patch that is at nDistance and nAngle left from the caller
	 */
	public Patch patchLeftAndAhead(Number angle, Number distance);

	/**
	 * Returns the patch that is at a distance and degrees right from the
	 * caller.
	 * 
	 * @param nAngle
	 *            an angle in degrees
	 * @param nDistance
	 *            a number
	 * @return patch that is at nDistance and nAngle right from the caller
	 */
	public Patch patchRightAndAhead(Number angle, Number distance);

	/**
	 * Returns the agentset of the eight neighboring patches (Moore
	 * neighborhood).
	 * 
	 * @return agentset of the eight neighboring patches
	 */
	public AgentSet<Patch> neighbors();
	
	/**
	 * Returns the agentset of patches making up the Moore
	 * neighborhood of this turtle (excluding the patch under this turtle)
	 * of extent.
	 * 
	 * @param extent the x and y Moore extents
	 * 
	 * @return Moore agentset of patches with extent
	 */
	public AgentSet<Patch> neighbors(int extent);
	
	/**
	 * Returns the agentset of patches making up the Moore
	 * neighborhood of this turtle (excluding the patch under this turtle)
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
	 * neighborhood of this turtle (excluding the patch under this turtle)
	 * of extent.
	 * 
	 * @param extent the x and y von Neumann extents
	 * 
	 * @return von Neumann agentset of patches with extent
	 */
	public AgentSet<Patch> neighbors4(int extent);
	
	/**
	 * Returns the agentset of patches making up the von Neumann
	 * neighborhood of this turtle (excluding the patch under this turtle)
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

	// create links code

	/**
	 * Makes a directed link from a turtle to the caller.
	 * 
	 * @param t
	 *            a turtle
	 * @return created link
	 */
	public Link createLinkFrom(Turtle t);

	/**
	 * Makes a directed link from a turtle to the caller then executes a set of
	 * commands on the created link.
	 * 
	 * @param t
	 *            a turtle
	 * @param closure
	 *            a set of commands
	 * @return created link
	 */
	public Link createLinkFrom(Turtle t, Closure closure);

	/**
	 * Makes a directed link from the caller to a turtle.
	 * 
	 * @param t
	 *            a turtle
	 * @return created link
	 */
	public Link createLinkTo(Turtle t);

	/**
	 * Makes a directed link from the caller to a turtle then executes a set of
	 * commands on the created link.
	 * 
	 * @param t
	 *            a turtle
	 * @param closure
	 *            a set of commands
	 * @return created link
	 */
	public Link createLinkTo(Turtle t, Closure closure);

	/**
	 * Makes a undirected link between the caller and a turtle.
	 * 
	 * @param t
	 *            a turtle
	 * @return created link
	 */
	public Link createLinkWith(Turtle t);

	/**
	 * Makes an undirected link between the caller and a turtle then executes a
	 * set of commands on the created link.
	 * 
	 * @param t
	 *            a turtle
	 * @param closure
	 *            a set of commands
	 * @return created link
	 */
	public Link createLinkWith(Turtle t, Closure closure);

	/**
	 * Makes directed links from a collection of agents to the caller.
	 * 
	 * @param a
	 *            a collection of agents
	 * @return created links
	 */
	public AgentSet<Link> createLinksFrom(Collection<? extends Turtle> a);

	/**
	 * Makes directed links from a collection of agents to the caller then executes a
	 * set of commands on the created links.
	 * 
	 * @param a
	 *            a collection of agents
	 * @param closure
	 *            a set of commands
	 * @return created links
	 */
	public AgentSet<Link> createLinksFrom(Collection<? extends Turtle> a, Closure closure);

	/**
	 * Makes directed links from the caller to a collection of agents.
	 * 
	 * @param a
	 *            a collection of agents
	 * @return created links
	 */
	public AgentSet<Link> createLinksTo(Collection<? extends Turtle> a);

	/**
	 * Makes directed links from the caller to a collection of agents then executes a
	 * set of commands on the created links.
	 * 
	 * @param a
	 *            a collection of agents
	 * @param closure
	 *            a set of commands
	 * @return created links
	 */
	public AgentSet<Link> createLinksTo(Collection<? extends Turtle> a, Closure closure);


	/**
	 * Makes undirected links between the caller and a collection of agents then
	 * executes a set of commands on the created links.
	 * 
	 * @param a
	 *            a collection of agents
	 * @param closure
	 *            a set of commands
	 * @return created links
	 */
	public AgentSet<Link> createLinksWith(Collection<? extends Turtle> a, Closure closure);

	/**
	 * Makes undirected links between the caller and a collection of agents.
	 * 
	 * @param a
	 *            an collection of agents
	 * @return created links
	 */
	public AgentSet<Link> createLinksWith(Collection<? extends Turtle> a);

	/**
	 * Queries if there is a directed link from a turtle to the caller.
	 * 
	 * @param t
	 *            a turtle
	 * @return true or false based on whether there is a directed link from
	 *         turtle t to the caller
	 */
	public boolean inLinkNeighborQ(Turtle t);

	/**
	 * Queries if there is a directed link from the caller to the turtle.
	 * 
	 * @param t
	 *            a turtle
	 * @return true or false based on whether there is a directed link from the
	 *         caller to the turtle.
	 */
	public boolean outLinkNeighborQ(Turtle t);

	/**
	 * Reports true if there is an undirected link connecting t and the caller.
	 */
	public boolean linkNeighborQ(Turtle t);

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
	 * Returns the agentset with directed links to the caller.
	 * 
	 * @return agentset with directed links to the caller
	 */
	public AgentSet<Turtle> inLinkNeighbors();

	/**
	 * Returns the agentset of the caller's out link neighbor turtles.
	 * 
	 * @return agentset of the caller's out link neighbor turtles
	 */
	public AgentSet<Turtle> outLinkNeighbors();

	/**
	 * Reports the agentset of all turtles found at the other end of undirected
	 * links connected to the calling turtle.
	 */
	public AgentSet<Turtle> linkNeighbors();

	/**
	 * Returns the directed link from a turtle to the caller.
	 * 
	 * @param t
	 *            a turtle
	 * @return directed link from turtle t to the caller
	 */
	public Link inLinkFrom(Turtle t);

	/**
	 * Returns the caller's directed link to a turtle.
	 * 
	 * @param t
	 *            a turtle
	 * @return the caller's directed link to turtle t
	 */
	public Link outLinkTo(Turtle t);

	/**
	 * Report the link between t and the caller. If no link exists then it
	 * reports null.
	 */
	public Link linkWith(Turtle t);

	/**
	 * Returns an agentset of generic directed links from other turtles to the caller.
	 * 
	 * @return agentset of directed links from other turtles to the caller
	 */
	public AgentSet<Link> myInLinks();
	
	/**
	 * Returns an agentset of all directed links from other turtles to the caller.
	 * 
	 * @return agentset of directed links from other turtles to the caller
	 */
	public AgentSet<Link> allMyInLinks();

	/**
	 * Returns an agentset of generic directed links from the caller to other turtles.
	 * 
	 * @return agentset of directed links from the caller to other turtles
	 */
	public AgentSet<Link> myOutLinks();
	
	/**
	 * Returns an agentset of all directed links from the caller to other turtles.
	 * 
	 * @return agentset of directed links from the caller to other turtles
	 */
	public AgentSet<Link> allMyOutLinks();

	/**
	 * Returns an agentset of the caller's generic undirected links.
	 * 
	 * @return agentset of the caller's undirected links
	 */
	public AgentSet<Link> myLinks();
	
	/**
	 * Returns an agentset of all the caller's undirected links.
	 * 
	 * @return agentset of the caller's undirected links
	 */
	public AgentSet<Link> allMyLinks();

	/**
	 * Returns the turtle opposite the asking link.
	 * 
	 * @return turtle opposite the asking link
	 */
	public Turtle otherEnd();

	public boolean isVisibilityChanged();

	public void setVisibilityChanged(boolean b);

	/**
	 * Stops a turtle executing within a command closure.
	 * @deprecated use the {@link repast.simphony.relogo.Utility#stop()} method instead.
	 */
	@Deprecated
	public Stop oldStop();

	public boolean fixedLeavesContains(Turtle t);

	public void removeFromFixedLeaves(OutOfContextSubject t);

	public void addToFixedLeaves(Turtle t);

	public boolean freeLeavesContains(Object o);

	public void removeFromFreeLeaves(Turtle o);

	public void addToFreeLeaves(Turtle o);

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
	public ReLogoAgent maxOneOf(Collection<? extends ReLogoAgent> a, Closure closure);

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
	 * @param a
	 *            a collection of agents
	 * @param num
	 *            a distance
	 * @return agentset subset of collection a within a distance num of the caller
	 */
	public AgentSet inRadius(Collection a, Number num);
	
	/**
	 * Returns an agentset within a distance and heading cone of the caller.
	 * 
	 * @param a
	 *            a collection of agents
	 * @param num
	 *            a distance
	 *           
	 * @param anlge
	 *            the cone angle centered on caller's heading
	 *            
	 * @return agentset
	 * 			subset of collection a within a distance num and within cone angle of the caller
	 */
	public AgentSet inCone(Collection a, Number num, Number angle);

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
	 * Returns the x coordinate of patch here.
	 * 
	 * @return x coordinate of patch here
	 */
	public int getPxcor();

	/**
	 * Returns the y coordinate of patch here.
	 * 
	 * @return y coordinate of patch here
	 */
	public int getPycor();

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
	 * Does nothing, included for translation compatibility.
	 */
	public void watchMe();

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

}
