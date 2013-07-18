package repast.simphony.relogo;

import groovy.lang.Closure;

import java.util.Collection;
import java.util.List;

import repast.simphony.space.graph.RepastEdge;

/**
 * ReLogo Link abstract class.
 * 
 * @author jozik
 *
 * @param <T>
 */
public abstract class Link<T> extends RepastEdge<T> implements
		Comparable<Link>, ReLogoAgent {

	/**
	 * Returns the agentset of all generic links.
	 * 
	 * @return agentset of all links
	 */
	public abstract AgentSet<Link> links();
	

	/**
	 * Returns the agentset of all links.
	 * 
	 * @return agentset of all links
	 */
	public abstract AgentSet<Link> allLinks();

	// The default constructor is included for LinkFactory use.
	protected Link() {
	}

	public void setSource(T source) {
		this.source = source;
	}

	public void setTarget(T target) {
		this.target = target;
	}

	public void setDirected(boolean directed) {
		this.directed = directed;
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
	public abstract Patch patch(Number nX, Number nY);

	/**
	 * Returns an agentset containing all patches.
	 * 
	 * @return agentset containing all patches
	 */
	public abstract AgentSet<Patch> patches();

	/**
	 * Returns the first turtle of a link.
	 * 
	 * @return first turtle of a link
	 */
	public abstract T getEnd1();

	/**
	 * Sets the first turtle of a link to end1.
	 * 
	 * @param end1
	 *            a turtle
	 */
	public abstract void setEnd1(T end1);

	/**
	 * Returns the second turtle of a link.
	 * 
	 * @return second turtle of a link
	 */
	public abstract T getEnd2();

	/**
	 * Sets the second turtle of a link to end2.
	 * 
	 * @param end2
	 *            a turtle
	 */
	public abstract void setEnd2(T end2);

	/**
	 * The agent that initiated the asking.
	 * 
	 * @return the agent that initiated the asking
	 */
	public abstract Object myself();

	/**
	 * Returns this turtle, patch, or link.
	 * 
	 * @return this turtle, patch, or link.
	 */
	public abstract Link self();

	/**
	 * Sets the color of a link to the value color.
	 * 
	 * @param color
	 *            a number
	 */
	public abstract void setColor(Number color);

	/**
	 * Returns the color of a link.
	 * 
	 * @ returns color of a link in the range [0, 140)
	 */
	public abstract double getColor();

	/**
	 * Joins together two ends of a link and ties the movements of the ends, including headings.
	 */
	public abstract void tie();
	
	/**
	 * Joins together two ends of a link and ties the movements of the ends, except for headings.
	 */
	public abstract void free();

	/**
	 * Unties two ends of a link.
	 */
	public abstract void untie();

	/**
	 * Sets tie mode to "none", "fixed", or "free".
	 * @param mode one of ["none", "fixed", "free"]
	 */
	public abstract void setTieMode(String mode);

	/**
	 * Sets link visibility.
	 * @param hidden boolean visibility
	 */
	public abstract void setHiddenQ(boolean hidden);

	/**
	 * Queries if caller is hidden.
	 * 
	 * @return true or false based on whether caller is hidden
	 */
	public abstract boolean isHiddenQ();

	/**
	 * Link appears hidden.
	 */
	public abstract void hideLink();

	/**
	 * Makes link visible.
	 */
	public abstract void showLink();

	/**
	 * Prints value with agent identifier to current file with a newline.
	 * 
	 * @param value
	 *            any object
	 */
	public abstract void fileShow(Object value);

	/**
	 * Prints value with agent identifier to the console.
	 * 
	 * @param value
	 *            any object
	 */
	public abstract void show(Object value);

	/**
	 * Executes a set of commands for an agentset in random order.
	 * 
	 * @param a
	 *            an agentset
	 * @param askBlock
	 *            a set of commands
	 */
	public abstract void ask(AgentSet<? extends ReLogoAgent> a, Closure askBlock);

	/**
	 * Executes a set of commands for a collection of agents.
	 * 
	 * @param c
	 *            a collection of agents
	 * @param askBlock
	 *            a set of commands
	 */
	public abstract void ask(Collection<? extends ReLogoAgent> c, Closure askBlock);

	/**
	 * Executes a set of commands for a turtle.
	 * 
	 * @param t
	 *            a turtle
	 * @param askBlock
	 *            a set of commands
	 */
	public abstract void ask(Turtle t, Closure askBlock);

	/**
	 * Executes a set of commands for a patch.
	 * 
	 * @param p
	 *            a patch
	 * @param askBlock
	 *            a set of commands
	 */
	public abstract void ask(Patch p, Closure askBlock);

	/**
	 * Executes a set of commands for a link.
	 * 
	 * @param l
	 *            a link
	 * @param askBlock
	 *            a set of commands
	 */
	public abstract void ask(Link l, Closure askBlock);

	public abstract void askLink(Closure cl);

	/**
	 * Returns two agents connected by link.
	 * 
	 * @returns agentset of two linked agents
	 */
	public abstract AgentSet<Turtle> bothEnds();

	/**
	 * Removes the link.
	 */
	public abstract void die();

	/**
	 * Returns the heading of a link.
	 * 
	 * @return heading of a link in degrees in the range [0, 360)
	 */
	public abstract double linkHeading();

	/**
	 * Returns the length of a link.
	 * 
	 * @return length of a link
	 */
	public abstract double linkLength();

	/**
	 * Returns the turtle opposite the asking turtle.
	 * 
	 * @return turtle opposite the asking turtle
	 */
	public abstract Turtle otherEnd();

	/**
	 * Stops a link executing within a command closure.
	 * @deprecated use the {@link repast.simphony.relogo.Utility#stop()} method instead.
	 */
	@Deprecated
	public abstract Stop oldStop();

	public abstract String getLinkType();

	/**
	 * Returns an agentset of turtles on the patches in a collection or on the patches
	 * that a collection of turtles are.
	 * 
	 * @param a
	 *            a collection
	 * @return agentset of turtles on the patches in collection a or on the patches
	 *         that collection a turtles are
	 */
	public abstract AgentSet<Turtle> turtlesOn(Collection a);

	/**
	 * Returns an agentset of turtles on a given patch.
	 * 
	 * @param p
	 *            a patch
	 * @return agentset of turtles on patch p
	 */
	public abstract AgentSet<Turtle> turtlesOn(Patch p);

	/**
	 * Returns an agentset of turtles on the same patch as a turtle.
	 * 
	 * @param t
	 *            a turtle
	 * @return agentset of turtles on the same patch at turtle t
	 */
	public abstract AgentSet<Turtle> turtlesOn(Turtle t);

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
	public abstract boolean allQ(Collection a, Closure closure);

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
	public abstract ReLogoAgent minOneOf(Collection<? extends ReLogoAgent> a, Closure closure);

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
	public abstract AgentSet minNOf(int number, Collection<? extends ReLogoAgent> a,
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
	public abstract ReLogoAgent maxOneOf(Collection<? extends ReLogoAgent> a, Closure closure);

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
	public abstract AgentSet maxNOf(int number, Collection<? extends ReLogoAgent> a,
			Closure closure);


	/**
	 * Returns an agentset minus the caller.
	 * 
	 * @param a
	 *            an agentset
	 * @return agentset a minus the caller
	 */
	public abstract AgentSet other(Collection a);

	/**
	 * Returns the minimum x coordinate for all patches.
	 * 
	 * @return maximum x coordinate for patches
	 */
	public abstract int getMinPxcor();

	/**
	 * Returns the maximum x coordinate for all patches.
	 * 
	 * @return maximum x coordinate for all patches
	 */
	public abstract int getMaxPxcor();

	/**
	 * Returns the minimum y coordinate for all patches.
	 * 
	 * @return maximum y coordinate for patches
	 */
	public abstract int getMinPycor();

	/**
	 * Returns the maximum y coordinate for all patches.
	 * 
	 * @return maximum y coordinate for all patches
	 */
	public abstract int getMaxPycor();

	/**
	 * Returns the link between two turtles.
	 * 
	 * @param oneEnd
	 *            an integer
	 * @param otherEnd
	 *            an integer
	 * @return link between two turtles
	 */
	public abstract Link link(Number oneEnd, Number otherEnd);

	/**
	 * Returns the link between two turtles.
	 * 
	 * @param oneEnd
	 *            a turtle
	 * @param otherEnd
	 *            a turtle
	 * @return link between two turtles
	 */
	public abstract Link link(Turtle oneEnd, Turtle otherEnd);

	/**
	 * Returns a random x coordinate for patches.
	 * 
	 * @return random x coordinate for patches
	 */
	public abstract int randomPxcor();

	/**
	 * Returns a random y coordinate for patches.
	 * 
	 * @return random y coordinate for patches
	 */
	public abstract int randomPycor();

	/**
	 * Returns a random x coordinate for turtles.
	 * 
	 * @return random x coordinate for turtles
	 */
	public abstract double randomXcor();

	/**
	 * Returns a random y coordinate for turtles.
	 * 
	 * @return random y coordinate for turtles
	 */
	public abstract double randomYcor();

	/**
	 * Interprets a string as commands then runs the commands.
	 * 
	 * @param string
	 *            a string
	 */
	public abstract void run(String string);

	/**
	 * Interprets a string as a command then returns the result.
	 * 
	 * @param string
	 *            a string
	 * @return result of interpreting string
	 */
	public abstract Object runresult(String string);

	/**
	 * Returns the label color for a turtle or link. @ returns label color in
	 * the range [0, 140)
	 */
	public abstract double getLabelColor();

	/**
	 * Sets the label color for a link to labelColor.
	 * 
	 * @param labelColor
	 *            a number in range [0, 140)
	 */
	public abstract void setLabelColor(Number labelColor);

	/**
	 * Returns the turtle of the given number.
	 * 
	 * @param number
	 *            a number
	 * @return turtle number
	 */
	public abstract Turtle turtle(Number number);

	/**
	 * Returns an agentset containing all turtles.
	 * 
	 * @return agentset of all turtles
	 */
	public abstract AgentSet<Turtle> turtles();
	
	/**
	 * Returns the height of the world.
	 * 
	 * @return height of the world
	 */
	public abstract int worldHeight();

	/**
	 * Returns the width of the world.
	 * 
	 * @return width of the world
	 */
	public abstract int worldWidth();

}
