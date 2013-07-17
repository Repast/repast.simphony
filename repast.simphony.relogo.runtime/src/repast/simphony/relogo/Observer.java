package repast.simphony.relogo;

import groovy.lang.Closure;

import java.util.Collection;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.relogo.factories.RLWorldDimensions;
import repast.simphony.relogo.factories.TurtleFactory;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.graph.Network;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.projection.ProjectionListener;
import cern.colt.function.DoubleFunction;

/**
 * ReLogo Observer interface.
 * 
 * @author jozik
 *
 */
public interface Observer extends ProjectionListener {

	public TurtleFactory getTurtleFactory();

	public Grid getGrid();

	public ContinuousSpace getSpace();

	public Network getNetwork(String name);

	public Context getContext();

	public RLWorldDimensions getRLDimensions();

	public void createPatchVar(DiffusiblePatchVariable var);

	/**
	 * Diffuses to the eight neighboring patches a fraction number of the patch
	 * variable.
	 * 
	 * @param patchVarible
	 *            the name of a diffusible patch variable
	 * @param number
	 *            a number in the range [0,1]
	 */
	public void diffuse(String patchVariable, double number);

	/**
	 * Diffuses to the four neighboring patches a fraction number of the patch
	 * variable.
	 * 
	 * @param patchVarible
	 *            the name of a diffusible patch variable
	 * @param number
	 *            a number number in the range [0,1]
	 */
	public void diffuse4(String patchVariable, double number);

	/**
	 * Multiply a diffusible patch variable of all patches by a number.
	 * 
	 * @param patchVariable
	 *            the name of a diffusible patch variable
	 * @param number
	 *            a number
	 */
	public void diffusibleMultiply(String patchVariable, Number number);

	/**
	 * Divide a diffusible patch variable of all patches by a number.
	 * 
	 * @param patchVariable
	 *            the name of a diffusible patch variable
	 * @param number
	 *            a number
	 */
	public void diffusibleDivide(String patchVariable, Number number);

	/**
	 * Add a number to a diffusible patch variable of all patches.
	 * 
	 * @param patchVariable
	 *            the name of a diffusible patch variable
	 * @param number
	 *            a number
	 */
	public void diffusibleAdd(String patchVariable, Number number);

	/**
	 * Subtract a number from a diffusible patch variable of all patches.
	 * 
	 * @param patchVariable
	 *            the name of a diffusible patch variable
	 * @param number
	 *            a number
	 */
	public void diffusibleSubtract(String patchVariable, Number number);

	/**
	 * Apply a function to a diffusible patch variable of all patches.
	 * 
	 * @param patchVariable
	 *            the name of a diffusible patch variable
	 * @param function
	 *            a DoubleFunction function
	 */
	public void diffusibleApply(String patchVariable, DoubleFunction function);

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
	 * Makes a number of randomly oriented turtles.
	 * 
	 * @param number
	 *            an integer
	 * @return created turtles
	 */
	public AgentSet<Turtle> createTurtles(int number);

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
	public AgentSet<Turtle> createTurtles(int number, Closure closure);

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
	public AgentSet<Turtle> createTurtles(int number, Closure closure, String type);

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
	public AgentSet<Turtle> createTurtles(int number, Closure closure, Class type);

	/**
	 * Makes a number of randomly oriented turtles.
	 * 
	 * @param number
	 *            an integer
	 * @return created turtles
	 * @see #createTurtles()
	 */
	public AgentSet<Turtle> crt(int number);

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
	public AgentSet<Turtle> crt(int number, Closure closure);

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
	public AgentSet<Turtle> crt(int number, Closure closure, String type);

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
	public AgentSet<Turtle> crt(int number, Closure closure, Class type);

	/**
	 * Makes a number of uniformly fanned turtles.
	 * 
	 * @param number
	 *            an integer
	 * @return created turtles
	 * @see #createOrderedTurtles(int)
	 */
	public AgentSet<Turtle> cro(int number);

	/**
	 * Makes a number of uniformly fanned turtles.
	 * 
	 * @param number
	 *            an integer
	 * @return created turtles
	 */
	public AgentSet<Turtle> createOrderedTurtles(int number);

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
	public AgentSet<Turtle> cro(int number, Closure closure);

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
	public AgentSet<Turtle> createOrderedTurtles(int number, Closure closure);

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
	public AgentSet<Turtle> cro(int number, Closure closure, String type);

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
	public AgentSet<Turtle> cro(int number, Closure closure, Class type);

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
	public AgentSet<Turtle> createOrderedTurtles(int number, Closure closure, String type);

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
	public AgentSet<Turtle> createOrderedTurtles(int number, Closure closure, Class type);

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

	public String toString();

	/**
	 * Sets the default shape for new turtleType turtles to shape.
	 * 
	 * @param turtleType
	 *            a string
	 * @param shape
	 *            a string
	 */
	public void setDefaultShape(String turtleType, String shape);

	/**
	 * Sets the default shape for new turtleType turtles to shape.
	 * 
	 * @param turtleType
	 *            a class
	 * @param shape
	 *            a string
	 */
	public void setDefaultShape(Class turtleType, String shape);


	/**
	 * Clears the world to the default state.
	 * 
	 * @see #clearAll()
	 */
	public void ca();

	/**
	 * Clears the world to the default state.
	 */
	public void clearAll();

	/**
	 * Removes all turtles.
	 * 
	 * @see #clearTurtles()
	 */
	public void ct();

	/**
	 * Removes all turtles.
	 */
	public void clearTurtles();

	/**
	 * Removes all links.
	 */
	public void clearLinks();

	/**
	 * Sets all standard and diffusible patch variables to their default values.
	 * 
	 * @see #clearPatches()
	 */
	public void cp();

	/**
	 * Sets all standard and diffusible patch variables to their default values.
	 */
	public void clearPatches();

	public void clearTrackingNetwork();

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
	 * Stops an observer executing within a command closure.
	 * @deprecated use the {@link repast.simphony.relogo.Utility#stop()} method instead.
	 */
	@Deprecated
	public Stop oldStop();

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
	* Does nothing, included for translation compatibility.
	*/
	public void watch(Object watched);
	
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
	 * Registers a model parameter listener to execute the provided closure.
	 * 
	 * @param varName model parameter variable name
	 * @param closure the closure to execute (the closure input parameter is a java.beans.PropertyChangeEvent)
	 */
	public void registerModelParameterListener(String varName, final Closure closure);
	
	/**
	 * Creates default turtles from a CSV file.
	 * Optionally provide an initialization closure.
	 * <p>
	 * The format for the CSV file is:<p>
	 * 	prop1,prop2,prop3<br>
	 * 	val1a,val2a,val3a<br>
	 * 	val1b,val2b,val3b<br>
	 * <p>
	 * Where if the turtle type has any of the properties 
	 * (prop1,prop2,prop3), they will be set to (val1a,val2a,val3a)
	 * for the first turtle, (val1b,val2b,val3b) for the second 
	 * turtle, and so on.<p>
	 *
	 * @param fileName the path to the CSV file
	 * @param initClosure the initialization routine
	 *
	 */
	public AgentSet<Turtle> createTurtlesFromCSV(String fileName, Closure initClosure);
	
	/**
	 * Creates default turtles from a CSV file.
	 * <p>
	 * The format for the CSV file is:<p>
	 * 	prop1,prop2,prop3<br>
	 * 	val1a,val2a,val3a<br>
	 * 	val1b,val2b,val3b<br>
	 * <p>
	 * Where if the turtle type has any of the properties 
	 * (prop1,prop2,prop3), they will be set to (val1a,val2a,val3a)
	 * for the first turtle, (val1b,val2b,val3b) for the second 
	 * turtle, and so on.<p>
	 *
	 * @param fileName the path to the CSV file
	 *
	 */
	public AgentSet<Turtle> createTurtlesFromCSV(String fileName);
	
	/**
	 * Creates turtles of specific type from a CSV file.
	 * Optionally provide an initialization closure.
	 * <p>
	 * The format for the CSV file is:<p>
	 * 	prop1,prop2,prop3<br>
	 * 	val1a,val2a,val3a<br>
	 * 	val1b,val2b,val3b<br>
	 * <p>
	 * Where if the turtle type has any of the properties 
	 * (prop1,prop2,prop3), they will be set to (val1a,val2a,val3a)
	 * for the first turtle, (val1b,val2b,val3b) for the second 
	 * turtle, and so on.<p>
	 * 
	 * @param fileName the path to the CSV file
	 * @param turtleType the class of turtle to create
	 * @param initClosure the initialization routine
	 * 
	 */
	public <E extends ReLogoAgent> AgentSet<E> createTurtlesFromCSV(String fileName, Class<E> turtleType, Closure initClosure);
	
	/**
	 * Creates turtles of specific type from a CSV file.
	 * <p>
	 * The format for the CSV file is:<p>
	 * 	prop1,prop2,prop3<br>
	 * 	val1a,val2a,val3a<br>
	 * 	val1b,val2b,val3b<br>
	 * <p>
	 * Where if the turtle type has any of the properties 
	 * (prop1,prop2,prop3), they will be set to (val1a,val2a,val3a)
	 * for the first turtle, (val1b,val2b,val3b) for the second 
	 * turtle, and so on.<p>
	 * 
	 * @param fileName the path to the CSV file
	 * @param turtleType the class of turtle to create
	 * 
	 */
	public <E extends ReLogoAgent> AgentSet<E> createTurtlesFromCSV(String fileName, Class<E> turtleType);
	
	/**
	 * Creates default ordered turtles from a CSV file.
	 * Optionally provide an initialization closure.
	 * <p>
	 * The format for the CSV file is:<p>
	 * 	prop1,prop2,prop3<br>
	 * 	val1a,val2a,val3a<br>
	 * 	val1b,val2b,val3b<br>
	 * <p>
	 * Where if the turtle type has any of the properties 
	 * (prop1,prop2,prop3), they will be set to (val1a,val2a,val3a)
	 * for the first turtle, (val1b,val2b,val3b) for the second 
	 * turtle, and so on.<p>
	 * 
	 * @param fileName the path to the CSV file
	 * @param initClosure the initialization routine
	 *
	 */
	public AgentSet<Turtle> createOrderedTurtlesFromCSV(String fileName, Closure initClosure);

	/**
	 * Creates default ordered turtles from a CSV file.
	 * <p>
	 * The format for the CSV file is:<p>
	 * 	prop1,prop2,prop3<br>
	 * 	val1a,val2a,val3a<br>
	 * 	val1b,val2b,val3b<br>
	 * <p>
	 * Where if the turtle type has any of the properties 
	 * (prop1,prop2,prop3), they will be set to (val1a,val2a,val3a)
	 * for the first turtle, (val1b,val2b,val3b) for the second 
	 * turtle, and so on.<p>
	 * 
	 * @param fileName the path to the CSV file
	 *
	 */
	public AgentSet<Turtle> createOrderedTurtlesFromCSV(String fileName);
	
	/**
	 * Creates ordered turtles of specific type from a CSV file.
	 * Optionally provide an initialization closure.
	 * <p>
	 * The format for the CSV file is:<p>
	 * 	prop1,prop2,prop3<br>
	 * 	val1a,val2a,val3a<br>
	 * 	val1b,val2b,val3b<br>
	 * <p>
	 * Where if the turtle type has any of the properties 
	 * (prop1,prop2,prop3), they will be set to (val1a,val2a,val3a)
	 * for the first turtle, (val1b,val2b,val3b) for the second 
	 * turtle, and so on.<p>
	 *
	 * @param fileName the path to the CSV file
	 * @param turtleType the class of turtle to create
	 * @param initClosure the initialization routine
	 *
	 */
	public <E extends ReLogoAgent> AgentSet<E> createOrderedTurtlesFromCSV(String fileName, Class<E> turtleType, Closure initClosure);

	/**
	 * Creates ordered turtles of specific type from a CSV file.
	 * <p>
	 * The format for the CSV file is:<p>
	 * 	prop1,prop2,prop3<br>
	 * 	val1a,val2a,val3a<br>
	 * 	val1b,val2b,val3b<br>
	 * <p>
	 * Where if the turtle type has any of the properties 
	 * (prop1,prop2,prop3), they will be set to (val1a,val2a,val3a)
	 * for the first turtle, (val1b,val2b,val3b) for the second 
	 * turtle, and so on.<p>
	 *
	 * @param fileName the path to the CSV file
	 * @param turtleType the class of turtle to create
	 *
	 */
	public <E extends ReLogoAgent> AgentSet<E> createOrderedTurtlesFromCSV(String fileName, Class<E> turtleType);
	
	/**
	 * Creates default turtles from the first sheet of an Excel file.
	 * Optionally provide an initialization closure.
	 * <p>
	 * The format for the Excel file is:<p>
	 * 	| prop1 | prop2 | prop3 |<br>
	 * 	| val1a | val2a | val3a |<br>
	 * 	| val1b | val2b | val3b |<br>
	 * <p>
	 * Where if the turtle type has any of the properties 
	 * (prop1,prop2,prop3), they will be set to (val1a,val2a,val3a)
	 * for the first turtle, (val1b,val2b,val3b) for the second 
	 * turtle, and so on.<p>
	 *
	 * @param fileName the path from the default system directory
	 * @param initClosure the initialization routine
	 *
	 */
	public AgentSet<Turtle> createTurtlesFromExcel(String fileName, Closure initClosure);

	/**
	 * Creates default turtles from the first sheet of an Excel file.
	 * <p>
	 * The format for the Excel file is:<p>
	 * 	| prop1 | prop2 | prop3 |<br>
	 * 	| val1a | val2a | val3a |<br>
	 * 	| val1b | val2b | val3b |<br>
	 * <p>
	 * Where if the turtle type has any of the properties 
	 * (prop1,prop2,prop3), they will be set to (val1a,val2a,val3a)
	 * for the first turtle, (val1b,val2b,val3b) for the second 
	 * turtle, and so on.<p>
	 *
	 * @param fileName the path from the default system directory
	 *
	 */
	public AgentSet<Turtle> createTurtlesFromExcel(String fileName);
	
	/**
	 * Creates default turtles from a specific sheet in an Excel file.
	 * Optionally provide an initialization closure.
	 * <p>
	 * The format for the Excel file is:<p>
	 * 	| prop1 | prop2 | prop3 |<br>
	 * 	| val1a | val2a | val3a |<br>
	 * 	| val1b | val2b | val3b |<br>
	 * <p>
	 * Where if the turtle type has any of the properties 
	 * (prop1,prop2,prop3), they will be set to (val1a,val2a,val3a)
	 * for the first turtle, (val1b,val2b,val3b) for the second 
	 * turtle, and so on.<p>
	 *
	 * @param fileName the path from the default system directory
	 * @param sheetName the specific sheet name
	 * @param initClosure the initialization routine
	 *
	 */
	public AgentSet<Turtle> createTurtlesFromExcelWithSheet(String fileName, String sheetName, Closure initClosure);

	/**
	 * Creates default turtles from a specific sheet in an Excel file.
	 * <p>
	 * The format for the Excel file is:<p>
	 * 	| prop1 | prop2 | prop3 |<br>
	 * 	| val1a | val2a | val3a |<br>
	 * 	| val1b | val2b | val3b |<br>
	 * <p>
	 * Where if the turtle type has any of the properties 
	 * (prop1,prop2,prop3), they will be set to (val1a,val2a,val3a)
	 * for the first turtle, (val1b,val2b,val3b) for the second 
	 * turtle, and so on.<p>
	 *
	 * @param fileName the path from the default system directory
	 * @param sheetName the specific sheet name
	 *
	 */
	public AgentSet<Turtle> createTurtlesFromExcelWithSheet(String fileName, String sheetName);
	
	/**
	 * Creates turtles of specific type from the first sheet of an Excel file.
	 * Optionally provide an initialization closure.
	 * <p>
	 * The format for the Excel file is:<p>
	 * 	| prop1 | prop2 | prop3 |<br>
	 * 	| val1a | val2a | val3a |<br>
	 * 	| val1b | val2b | val3b |<br>
	 * <p>
	 * Where if the turtle type has any of the properties 
	 * (prop1,prop2,prop3), they will be set to (val1a,val2a,val3a)
	 * for the first turtle, (val1b,val2b,val3b) for the second 
	 * turtle, and so on.<p>
	 *
	 * @param fileName the path from the default system directory
	 * @param turtleType the class of turtle to create
	 * @param initClosure the initialization routine
	 *
	 */
	public <E extends ReLogoAgent> AgentSet<E> createTurtlesFromExcel(String fileName, Class<E> turtleType, Closure initClosure);

	/**
	 * Creates turtles of specific type from the first sheet of an Excel file.
	 * <p>
	 * The format for the Excel file is:<p>
	 * 	| prop1 | prop2 | prop3 |<br>
	 * 	| val1a | val2a | val3a |<br>
	 * 	| val1b | val2b | val3b |<br>
	 * <p>
	 * Where if the turtle type has any of the properties 
	 * (prop1,prop2,prop3), they will be set to (val1a,val2a,val3a)
	 * for the first turtle, (val1b,val2b,val3b) for the second 
	 * turtle, and so on.<p>
	 *
	 * @param fileName the path from the default system directory
	 * @param turtleType the class of turtle to create
	 *
	 */
	public <E extends ReLogoAgent> AgentSet<E> createTurtlesFromExcel(String fileName, Class<E> turtleType);
	
	/**
	 * Creates turtles of specific type from a specific sheet in an Excel file.
	 * Optionally provide an initialization closure.
	 * <p>
	 * The format for the Excel file is:<p>
	 * 	| prop1 | prop2 | prop3 |<br>
	 * 	| val1a | val2a | val3a |<br>
	 * 	| val1b | val2b | val3b |<br>
	 * <p>
	 * Where if the turtle type has any of the properties 
	 * (prop1,prop2,prop3), they will be set to (val1a,val2a,val3a)
	 * for the first turtle, (val1b,val2b,val3b) for the second 
	 * turtle, and so on.<p>
	 *
	 * @param fileName the path from the default system directory
	 * @param sheetName the specific sheet name
	 * @param turtleType the class of turtle to create
	 * @param initClosure the initialization routine
	 *
	 */
	public <E extends ReLogoAgent> AgentSet<E> createTurtlesFromExcelWithSheet(String fileName, String sheetName, Class<E> turtleType, Closure initClosure);

	/**
	 * Creates turtles of specific type from a specific sheet in an Excel file.
	 * <p>
	 * The format for the Excel file is:<p>
	 * 	| prop1 | prop2 | prop3 |<br>
	 * 	| val1a | val2a | val3a |<br>
	 * 	| val1b | val2b | val3b |<br>
	 * <p>
	 * Where if the turtle type has any of the properties 
	 * (prop1,prop2,prop3), they will be set to (val1a,val2a,val3a)
	 * for the first turtle, (val1b,val2b,val3b) for the second 
	 * turtle, and so on.<p>
	 *
	 * @param fileName the path from the default system directory
	 * @param sheetName the specific sheet name
	 * @param turtleType the class of turtle to create
	 *
	 */
	public <E extends ReLogoAgent> AgentSet<E> createTurtlesFromExcelWithSheet(String fileName, String sheetName, Class<E> turtleType);

	
	/**
	 * Creates ordered default turtles from the first sheet of an Excel file.
	 * Optionally provide an initialization closure.
	 * <p>
	 * The format for the Excel file is:<p>
	 * 	| prop1 | prop2 | prop3 |<br>
	 * 	| val1a | val2a | val3a |<br>
	 * 	| val1b | val2b | val3b |<br>
	 * <p>
	 * Where if the turtle type has any of the properties 
	 * (prop1,prop2,prop3), they will be set to (val1a,val2a,val3a)
	 * for the first turtle, (val1b,val2b,val3b) for the second 
	 * turtle, and so on.<p>
	 *
	 * @param fileName the path from the default system directory
	 * @param initClosure the initialization routine
	 *
	 */
	public AgentSet<Turtle> createOrderedTurtlesFromExcel(String fileName, Closure initClosure);

	/**
	 * Creates ordered default turtles from the first sheet of an Excel file.
	 * <p>
	 * The format for the Excel file is:<p>
	 * 	| prop1 | prop2 | prop3 |<br>
	 * 	| val1a | val2a | val3a |<br>
	 * 	| val1b | val2b | val3b |<br>
	 * <p>
	 * Where if the turtle type has any of the properties 
	 * (prop1,prop2,prop3), they will be set to (val1a,val2a,val3a)
	 * for the first turtle, (val1b,val2b,val3b) for the second 
	 * turtle, and so on.<p>
	 *
	 * @param fileName the path from the default system directory
	 *
	 */
	public AgentSet<Turtle> createOrderedTurtlesFromExcel(String fileName);
	
	/**
	 * Creates ordered default turtles from a specific sheet in an Excel file.
	 * Optionally provide an initialization closure.
	 *
	 * @param fileName the path from the default system directory
	 * @param sheetName the specific sheet name
	 * @param initClosure the initialization routine
	 *
	 */
	public AgentSet<Turtle> createOrderedTurtlesFromExcelWithSheet(String fileName, String sheetName, Closure initClosure);

	/**
	 * Creates ordered default turtles from a specific sheet in an Excel file.
	 *
	 * @param fileName the path from the default system directory
	 * @param sheetName the specific sheet name
	 *
	 */
	public AgentSet<Turtle> createOrderedTurtlesFromExcelWithSheet(String fileName, String sheetName);
	
	/**
	 * Creates ordered turtles of specific type from the first sheet of an Excel file.
	 * Optionally provide an initialization closure.
	 * <p>
	 * The format for the Excel file is:<p>
	 * 	| prop1 | prop2 | prop3 |<br>
	 * 	| val1a | val2a | val3a |<br>
	 * 	| val1b | val2b | val3b |<br>
	 * <p>
	 * Where if the turtle type has any of the properties 
	 * (prop1,prop2,prop3), they will be set to (val1a,val2a,val3a)
	 * for the first turtle, (val1b,val2b,val3b) for the second 
	 * turtle, and so on.<p>
	 *
	 * @param fileName the path from the default system directory
	 * @param turtleType the class of turtle to create
	 * @param initClosure the initialization routine
	 *
	 */
	public <E extends ReLogoAgent> AgentSet<E> createOrderedTurtlesFromExcel(String fileName, Class<E> turtleType, Closure initClosure);

	/**
	 * Creates ordered turtles of specific type from the first sheet of an Excel file.
	 * <p>
	 * The format for the Excel file is:<p>
	 * 	| prop1 | prop2 | prop3 |<br>
	 * 	| val1a | val2a | val3a |<br>
	 * 	| val1b | val2b | val3b |<br>
	 * <p>
	 * Where if the turtle type has any of the properties 
	 * (prop1,prop2,prop3), they will be set to (val1a,val2a,val3a)
	 * for the first turtle, (val1b,val2b,val3b) for the second 
	 * turtle, and so on.<p>
	 *
	 * @param fileName the path from the default system directory
	 * @param turtleType the class of turtle to create
	 *
	 */
	public <E extends ReLogoAgent> AgentSet<E> createOrderedTurtlesFromExcel(String fileName, Class<E> turtleType);

	
	/**
	 * Creates ordered turtles of specific type from a specific sheet in an Excel file.
	 * Optionally provide an initialization closure.
	 *
	 * @param fileName the path from the default system directory
	 * @param sheetName the specific sheet name
	 * @param turtleType the class of turtle to create
	 * @param initClosure the initialization routine
	 *
	 */
	public <E extends ReLogoAgent> AgentSet<E> createOrderedTurtlesFromExcelWithSheet(String fileName, String sheetName, Class<E> turtleType, Closure initClosure);
	
	/**
	 * Creates ordered turtles of specific type from a specific sheet in an Excel file.
	 *
	 * @param fileName the path from the default system directory
	 * @param sheetName the specific sheet name
	 * @param turtleType the class of turtle to create
	 *
	 */
	public <E extends ReLogoAgent> AgentSet<E> createOrderedTurtlesFromExcelWithSheet(String fileName, String sheetName, Class<E> turtleType);
}
