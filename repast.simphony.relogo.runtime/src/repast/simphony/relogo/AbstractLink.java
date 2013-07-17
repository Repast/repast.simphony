package repast.simphony.relogo;

import groovy.lang.Closure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import repast.simphony.context.Context;
import repast.simphony.relogo.factories.LinkFactory;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.graph.Network;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.ui.probe.ProbeID;

public abstract class AbstractLink<T> extends Link<T> {

	private T end1;
	private T end2;

	/**
	 * Returns the first turtle of a link.
	 * 
	 * @return first turtle of a link
	 */
	public T getEnd1() {
		return end1;
	}

	/**
	 * Sets the first turtle of a link to end1.
	 * 
	 * @param end1
	 *            a turtle
	 */
	public void setEnd1(T end1) {
		this.end1 = end1;
	}

	/**
	 * Returns the second turtle of a link.
	 * 
	 * @return second turtle of a link
	 */
	public T getEnd2() {
		return end2;
	}

	/**
	 * Sets the second turtle of a link to end2.
	 * 
	 * @param end2
	 *            a turtle
	 */
	public void setEnd2(T end2) {
		this.end2 = end2;
	}

	private Object myselfObject;

	private Observer myObserver;
	private LinkFactory myLinkFactory;

	public double color = Utility.white();

	/**
	 * Sets the color of a link to the value color.
	 * 
	 * @param color
	 *            a number
	 */
	public void setColor(Number color) {
		this.color = Utility.wrapColor(color.doubleValue());
	}

	/**
	 * Returns the color of a link.
	 * 
	 * @ returns color of a link in the range [0, 140)
	 */
	public double getColor() {
		return color;
	}

	/**
	 * The agent that initiated the asking.
	 * 
	 * @return the agent that initiated the asking
	 */
	public Object myself() {
		return this.myselfObject;
	}
	
	/**
	 * Returns this turtle, patch, or link.
	 * 
	 * @return this turtle, patch, or link.
	 */
	public Link self() {
		return this;
	}

	public Observer getMyObserver() {
		return myObserver;
	}

	public void setMyObserver(Observer myObserver) {
		this.myObserver = myObserver;
	}

	public LinkFactory getMyLinkFactory() {
		return myLinkFactory;
	}

	public void setMyLinkFactory(LinkFactory myLinkFactory) {
		this.myLinkFactory = myLinkFactory;
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
	 * Returns an agentset containing all patches.
	 * 
	 * @return agentset containing all patches
	 */
	public AgentSet<Patch> patches() {
		return getMyObserver().patches();
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

	public void setBaseLinkProperties(Observer observer,
			LinkFactory linkFactory, T source, T target, boolean directed,
			Number weight) {
		setMyObserver(observer);
		setMyLinkFactory(linkFactory);
		setSource(source);
		setTarget(target);
		setDirected(directed);
		setWeight(weight.doubleValue());
		if (directed) {
			setEnd1(source);
			setEnd2(target);
		} else {
			if (source instanceof Turtle) {
				if (((Turtle) source).getWho() < ((Turtle) target).getWho()) {
					setEnd1(source);
					setEnd2(target);
				} else {
					setEnd1(target);
					setEnd2(source);
				}
			} else {
				setEnd1(source);
				setEnd2(target);
			}
		}
		Context context = observer.getContext();
		context.add(this);
	}

	public boolean hiddenQ = false;

	public Object label;

	public double labelColor = Utility.white();

	public String shape = "default";

	public int thickness = 0;

	/**
	 * Possible values: "none", "fixed", "free"
	 */
	public String tieMode = "none";

	/**
	 * Joins together two ends of a link and ties the movements of the ends.
	 */
	public void tie() {
		// TODO: surround the whole operation in a synchronized block?
		this.tieMode = "fixed";
		Turtle e1 = (Turtle) this.getEnd1();
		Turtle e2 = (Turtle) this.getEnd2();
		if (this.isDirected()) {
			e1.removeFromFreeLeaves(e2);
			e1.addToFixedLeaves(e2);
		} else {
			e1.removeFromFreeLeaves(e2);
			e2.removeFromFreeLeaves(e1);
			e1.addToFixedLeaves(e2);
			e2.addToFixedLeaves(e1);
		}
	}

	/**
	 * Unties two ends of a link.
	 */
	public void untie() {
		this.tieMode = "none";
		Turtle e1 = (Turtle) this.end1;
		Turtle e2 = (Turtle) this.end2;
		if (this.isDirected()) {
			e1.removeFromFreeLeaves(e2);
			e1.removeFromFixedLeaves(e2);
		} else {
			e1.removeFromFreeLeaves(e2);
			e1.removeFromFixedLeaves(e2);
			e2.removeFromFreeLeaves(e1);
			e2.removeFromFixedLeaves(e1);
		}
	}

	/**
	 * Joins together two ends of a link and ties the movements of the ends, except for headings.
	 */
	public void free() {
		this.tieMode = "free";
		Turtle e1 = (Turtle) this.end1;
		Turtle e2 = (Turtle) this.end2;
		if (this.isDirected()) {
			e1.removeFromFixedLeaves(e2);
			e1.addToFreeLeaves(e2);
		} else {
			e1.removeFromFixedLeaves(e2);
			e2.removeFromFixedLeaves(e1);
			e1.addToFreeLeaves(e2);
			e2.addToFreeLeaves(e1);
		}
	}

	/**
	 * Sets link visibility.
	 * @param hidden boolean visibility
	 */
	public void setHiddenQ(boolean hidden) {
		this.hiddenQ = hidden;
	}

	/**
	 * Queries if caller is hidden.
	 * 
	 * @return true or false based on whether caller is hidden
	 */
	public boolean isHiddenQ() {
		return this.hiddenQ;
	}

	/**
	 * Link appears hidden.
	 */
	public void hideLink() {
		setHiddenQ(true);
	}

	/**
	 * Makes link visible.
	 */
	public void showLink() {
		setHiddenQ(false);
	}

	public String getLinkType() {
		return (this.getClass().getSimpleName());
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
		return getLinkType() + " " + ((Turtle) end1).getWho() + " "
		+ ((Turtle) end2).getWho();
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
	public void askLink(Closure cl) {
		cl.setResolveStrategy(Closure.DELEGATE_FIRST);
		cl.setDelegate(this);
		cl.call(this);
	}

	/**
	 * Returns two agents connected by link.
	 * 
	 * @returns agentset of two linked agents
	 */
	public AgentSet<Turtle> bothEnds() {
		List list = new ArrayList();
		list.add(this.getSource());
		list.add(this.getTarget());
		return new AgentSet(list);
	}

	/**
	 * Removes the link.
	 */
	public void die() {
	   myObserver.getContext().remove(this);
	   Network n1 = myObserver.getNetwork(getLinkType());
	   if (n1.containsEdge(this)){
		   n1.removeEdge(this);
	   }
	   else {
		   List<Network> networks = new ArrayList<Network>();
		   networks.add(myObserver.getNetwork("DirectedLinks"));
		   networks.add(myObserver.getNetwork("UndirectedLinks"));
		   for (Network n : networks){
			   if (n.containsEdge(this)){
				   n.removeEdge(this);
				   break;
			   }
		   }
	   }
   }

	/**
	 * Returns the heading of a link.
	 * 
	 * @return heading of a link in degrees in the range [0, 360)
	 */
	public double linkHeading() {
		return ((Turtle) end1).towards(((Turtle) end2));
	}

	/**
	 * Returns the length of a link.
	 * 
	 * @return length of a link
	 */
	public double linkLength() {
		return ((Turtle) end1).distance(((Turtle) end2));
	}

	/**
	 * Returns the turtle opposite the asking turtle.
	 * 
	 * @return turtle opposite the asking turtle
	 */
	public Turtle otherEnd() {
		AgentSet a = bothEnds();
		if (a.get(0) == myself()) {
			return (Turtle) a.get(1);
		} else {
			return (Turtle) a.get(0);
		}
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

	public int compareTo(Link l) {
		/*
		 * Links are ordered by end points and in case of a tie by type. So link
		 * 0 9 is before link 1 10 as the end1 is smaller, and link 0 8 is less
		 * than link 0 9.
		 */
		if (this.getEnd1() != l.getEnd1()) {
			return (((Turtle) this.getEnd1()).compareTo((Turtle) l.getEnd1()));
		} else if (this.getEnd2() != l.getEnd2()) {
			return (((Turtle) this.getEnd2()).compareTo((Turtle) l.getEnd2()));
		} else {
			return (this.getLinkType().compareTo(l.getLinkType()));
		}
	}

	/**
	 * Stops a link executing within a command closure.
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
	 * Returns an agentset minus the caller.
	 * 
	 * @param a
	 *            an agentset
	 * @return agentset a minus the caller
	 */
	public AgentSet other(Collection a) {
		return Utility.removeLinkFromCollection(a, (Link) this);
	}

	
	/**
	 * Returns the label color for a turtle or link. @ returns label color in
	 * the range [0, 140)
	 */
	public double getLabelColor() {
		return labelColor;
	}

	/**
	 * Sets the label color for a link to labelColor.
	 * 
	 * @param labelColor
	 *            a number in range [0, 140)
	 */
	public void setLabelColor(Number labelColor) {
		this.labelColor = Utility.wrapColor(labelColor.doubleValue());
	}
}
