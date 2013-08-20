package repast.simphony.relogo;

import groovy.lang.Closure;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import repast.simphony.context.Context;
import repast.simphony.relogo.factories.TurtleFactory;
import repast.simphony.space.SpatialException;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.continuous.PointTranslator;
import repast.simphony.space.continuous.WrapAroundBorders;
import repast.simphony.space.graph.Network;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.ui.probe.ProbeID;

public abstract class AbstractTurtle implements Turtle {

	private Object myselfObject;

	/**
	 * Sets the agent that initiated the asking to the value o.
	 * 
	 * @param o
	 *          an object
	 * @exclude
	 */
	public void setMyself(Object o) {
		this.myselfObject = o;
	}

	Map props;
	Set<Turtle> fixedLeaves = Collections.synchronizedSet(new HashSet<Turtle>());
	Set<Turtle> freeLeaves = Collections.synchronizedSet(new HashSet<Turtle>());
	boolean moved = false;
	boolean shapeChanged = false;

	public boolean isShapeChanged() {
		return shapeChanged;
	}

	public void setShapeChanged(boolean shapeChanged) {
		this.shapeChanged = shapeChanged;
	}

	boolean visibilityChanged = false;

	public boolean fixedLeavesContains(Turtle t) {
		synchronized (fixedLeaves) {
			return fixedLeaves.contains(t);
		}
	}

	public void removeFromFixedLeaves(OutOfContextSubject t) {
		synchronized (fixedLeaves) {
			fixedLeaves.remove(t);
		}
		t.removeSubscriber(this);
	}

	public void addToFixedLeaves(Turtle t) {
		synchronized (fixedLeaves) {
			fixedLeaves.add(t);
		}
		t.registerSubscriber(this);
	}

	public boolean freeLeavesContains(Object o) {
		synchronized (freeLeaves) {
			return freeLeaves.contains(o);
		}
	}

	public void removeFromFreeLeaves(Turtle o) {
		synchronized (freeLeaves) {
			freeLeaves.remove(o);
		}
		o.removeSubscriber(this);
	}

	public void addToFreeLeaves(Turtle o) {
		synchronized (freeLeaves) {
			freeLeaves.add(o);
		}
		o.registerSubscriber(this);
	}

	Set<OutOfContextSubscriber> outOfContextSubscribers = Collections
			.synchronizedSet(new HashSet<OutOfContextSubscriber>());

	public void registerSubscriber(Turtle t) {
		synchronized (outOfContextSubscribers) {
			outOfContextSubscribers.add(t);
		}
	}

	public void removeSubscriber(Turtle t) {
		synchronized (outOfContextSubscribers) {
			outOfContextSubscribers.remove(t);
		}
	}

	public void notifySubscribers() {
		for (OutOfContextSubscriber o : outOfContextSubscribers) {
			o.update(this);
		}
	}

	public void update(Turtle o) {
		removeFromFixedLeaves(o);
		removeFromFreeLeaves(o);
	}

	WayPoint lastWayPoint;

	Object label;

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
	public Turtle self() {
		return this;
	}

	Observer myObserver;
	TurtleFactory myTurtleFactory;

	public Observer getMyObserver() {
		return myObserver;
	}

	public void setMyObserver(Observer myObserver) {
		this.myObserver = myObserver;
	}

	public TurtleFactory getMyTurtleFactory() {
		return myTurtleFactory;
	}

	public void setMyTurtleFactory(TurtleFactory myTurtleFactory) {
		this.myTurtleFactory = myTurtleFactory;
	}

	public void setBaseTurtleProperties(Observer observer, TurtleFactory turtleFactory,
			String turtleShape, double heading, double color, NdPoint loc) {
		setMyObserver(observer);
		setMyTurtleFactory(turtleFactory);
		this.shape = turtleShape;
		setHeading(heading);
		setColor(color);
		observer.getContext().add(this);
		setxy(loc.getX(), loc.getY());
		this.who = AbstractTurtle.whoCounter++;
	}

	static int whoCounter = 0;

	double color;

	double heading = 0;

	public boolean hiddenQ = false;

	double labelColor = Utility.white();

	int penMode = Utility.PEN_UP;

	int penSize = 1;

	String shape = "default";

	double size = 1.0;

	int who;

	/**
	 * Makes a number of new turtles.
	 * 
	 * @param number
	 *          a number
	 * @return created turtles
	 */
	public AgentSet<Turtle> hatch(Number number) {
		return hatch(number, null);
	}

	/**
	 * Makes a number of new turtles and then executes a set of commands on the
	 * created turtles.
	 * 
	 * @param number
	 *          a number
	 * @param closure
	 *          a set of commands
	 * @return created turtles
	 */
	public AgentSet<Turtle> hatch(Number number, Closure closure) {

		AgentSet<Turtle> newTurtles = new AgentSet<Turtle>();
		for (int i = 0; i < number.intValue(); i++) {
			newTurtles.add(getMyTurtleFactory().createIdenticalTurtle(this));
		}
		if (closure != null) {
			ask(newTurtles, closure);
		}
		return newTurtles;
	}

	/**
	 * Makes a number of new turtles of a specific type and then executes a set of
	 * commands on the created turtles.
	 * 
	 * @param number
	 *          a number
	 * @param closure
	 *          a set of commands
	 * @param childType
	 *          a string
	 * 
	 * @param number
	 * @param closure
	 * @param childType
	 * @return created turtles
	 * 
	 */
	public AgentSet<Turtle> hatch(Number number, Closure closure, String childType) {

		AgentSet<Turtle> newTurtles = new AgentSet<Turtle>();
		for (int i = 0; i < number.intValue(); i++) {
			newTurtles.add(getMyTurtleFactory().createIdenticalTurtle(this, childType));
		}
		if (closure != null) {
			ask(newTurtles, closure);
		}
		return newTurtles;
	}

	/**
	 * Makes a number of new turtles of a specific type and then executes a set of
	 * commands on the created turtles.
	 * 
	 * @param number
	 *          a number
	 * @param closure
	 *          a set of commands
	 * @param childType
	 *          a turtle class
	 * 
	 * @param number
	 * @param closure
	 * @param childType
	 * @return created turtles
	 */
	public AgentSet<Turtle> hatch(Number number, Closure closure, Class childType) {
		return hatch(number, closure, childType.getSimpleName());
	}

	/**
	 * Executes a set of commands for an agentset in random order.
	 * 
	 * @param a
	 *          an agentset
	 * @param askBlock
	 *          a set of commands
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
			System.err.println("ask(AgentSet) requires all members of AgentSet to extend ReLogoAgent.");
		}
	}

	/**
	 * Executes a set of commands for a collection of agents.
	 * 
	 * @param c
	 *          a collection of agents
	 * @param askBlock
	 *          a set of commands
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
			System.err.println("ask(List) requires all members of List to extend ReLogoAgent.");
		}
	}

	public void askCollection(Collection<? extends ReLogoAgent> l, Closure cl) {
		cl.setResolveStrategy(Closure.DELEGATE_FIRST);
		for (ReLogoAgent o : l) {
			cl.setDelegate(o);
			cl.call(o);
		}
	}

	/**
	 * Executes a set of commands for a turtle.
	 * 
	 * @param t
	 *          a turtle
	 * @param askBlock
	 *          a set of commands
	 */
	public void ask(Turtle t, Closure askBlock) {
		t.setMyself(this);
		t.askTurtle(askBlock);

	}

	/**
	 * Executes a set of commands for a patch.
	 * 
	 * @param p
	 *          a patch
	 * @param askBlock
	 *          a set of commands
	 */
	public void ask(Patch p, Closure askBlock) {
		p.setMyself(this);
		p.askPatch(askBlock);

	}

	/**
	 * Executes a set of commands for a link.
	 * 
	 * @param l
	 *          a link
	 * @param askBlock
	 *          a set of commands
	 */
	public void ask(Link l, Closure askBlock) {
		l.setMyself(this);
		l.askLink(askBlock);

	}

	/**
	 * @exclude
	 */
	public void askTurtle(Closure cl) {
		cl.setResolveStrategy(Closure.DELEGATE_FIRST);
		cl.setDelegate(this);
		cl.call(this);
	}

	/**
	 * Interprets a string as commands then runs the commands.
	 * 
	 * @param string
	 *          a string
	 */
	public void run(String string) {
		UtilityG.runU(string, this);
	}

	/**
	 * Interprets a string as a command then returns the result.
	 * 
	 * @param string
	 *          a string
	 * @return result of interpreting string
	 */
	public Object runresult(String string) {
		return UtilityG.runresultU(string, this);
	}

	private NdPoint loc = new NdPoint(0.0, 0.0);

	public String getTurtleType() {
		return (this.getClass().getSimpleName());
	}

	public void setUserDefinedVariables(Turtle parent) {
		List<Class<?>> parentHierarchy = new ArrayList<Class<?>>();
		List<Class<?>> thisHierarchy = new ArrayList<Class<?>>();
		Class parentClass = parent.getClass();
		while (parentClass != BaseTurtle.class) {
			parentHierarchy.add(parentClass);
			parentClass = parentClass.getSuperclass();
		}
		Collections.reverse(parentHierarchy);
		Class thisClass = this.getClass();
		while (thisClass != BaseTurtle.class) {
			thisHierarchy.add(thisClass);
			thisClass = thisClass.getSuperclass();
		}
		Collections.reverse(thisHierarchy);
		int i = 0;
		while (i < thisHierarchy.size() && i < parentHierarchy.size()
				&& thisHierarchy.get(i) == parentHierarchy.get(i)) {
			for (Field field : (thisHierarchy.get(i).getDeclaredFields())) {
				int modifier = field.getModifiers();
				if (!(Modifier.isStatic(modifier) || Modifier.isTransient(modifier))) {
					field.setAccessible(true);
					try {
						field.set(this, field.get(parent));
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
			i++;
		}
	}

	/**
	 * Returns the x coordinate of a turtle.
	 * 
	 * @return present x coordinate of the turtle
	 */
	public double getXcor() {
		return getTurtleLocation().getX();
	}

	/**
	 * Sets the x coordinate of a turtle to number.
	 * 
	 * @param number
	 *          a number
	 */
	public void setXcor(Number number) {
		this.setxy(number, getTurtleLocation().getY());
	}

	/**
	 * Returns the y coordinate of a turtle.
	 * 
	 * @return y coordinate of a turtle
	 */
	public double getYcor() {
		return getTurtleLocation().getY();
	}

	/**
	 * Sets the y coordinate of a turtle to number.
	 * 
	 * @param number
	 *          a number
	 */
	public void setYcor(Number number) {
		this.setxy(getTurtleLocation().getX(), number);
	}

	public NdPoint getTurtleLocation() {
		return loc;
	}

	/**
	 * Returns the color of patch here.
	 * 
	 * @return color of patch here in the range [0, 140)
	 */
	public double getPcolor() {
		return patchHere().getPcolor();
	}

	/**
	 * Sets the color of patch here to pcolor.
	 * 
	 * @param pcolor
	 *          a number in range [0,140)
	 */
	public void setPcolor(Number color) {
		patchHere().setPcolor(color.doubleValue());
	}

	/**
	 * Returns the x coordinate of patch here.
	 * 
	 * @return x coordinate of patch here
	 */
	public int getPxcor() {
		return patchHere().getPxcor();
	}

	/**
	 * Returns the y coordinate of patch here.
	 * 
	 * @return y coordinate of patch here
	 */
	public int getPycor() {
		return patchHere().getPycor();
	}

	/**
	 * Sets the color of a turtle to the value color.
	 * 
	 * @param color
	 *          a number
	 */
	public void setColor(Number color) {
		this.color = Utility.wrapColor(color.doubleValue());
	}

	/**
	 * Returns the heading of the turtle.
	 * 
	 * @ return heading of the turtle in the range [0, 360)
	 */
	public double getHeading() {
		return heading;
	}

	public double getHeadingInRads() {
		return (this.heading * Math.PI / 180.0);
	}

	/**
	 * Sets the heading of the turtle to nNum.
	 * 
	 * @param nNum
	 *          a number in degrees
	 */
	public void setHeading(Number nNum) {
		double num = nNum.doubleValue();
		if (!moved) {
			moved = true;
			double priorHeading = this.getHeading();
			double tempHeading = num % 360;
			this.heading = (tempHeading < 0) ? tempHeading + 360 : tempHeading;
			if (!fixedLeaves.isEmpty() || !freeLeaves.isEmpty()) {
				double angleTurned = Utility.subtractHeadings(this.getHeading(), priorHeading);
				Context context = getMyObserver().getContext();
				Set<Turtle> temporarySet = new HashSet<Turtle>(fixedLeaves);
				temporarySet.addAll(freeLeaves);
				for (Turtle t : temporarySet) {
					double initialLinkAngle = this.towards(t);
					// figure out the distance between t1 and t2 = B
					double distanceToLeaf = this.distance(t);
					// with A and B get the displacement from t1 that t2 should
					// be moved to = C
					double[] displacementToLeaf = Utility.getDisplacementFromHeadingAndDistance(
							Utility.getNLAngle(initialLinkAngle + angleTurned), distanceToLeaf);
					// move t2 to C plus the location of t1
					double[] leafLocation = { displacementToLeaf[0] + this.getXcor(),
							displacementToLeaf[1] + this.getYcor() };
					t.setxy(leafLocation[0], leafLocation[1]);
				}
				for (Turtle t : fixedLeaves) {
					t.setHeading(t.getHeading() + angleTurned);
				}
			}
			moved = false;
		}
	}

	/**
	 * Returns the color of a turtle.
	 * 
	 * @ returns color of a turtle in the range [0, 140)
	 */
	public double getColor() {
		return color;
	}

	/**
	 * Queries if caller is hidden.
	 * 
	 * @return true or false based on whether caller is hidden
	 */
	public boolean isHiddenQ() {
		return this.hiddenQ;
	}

	public boolean isVisibilityChanged() {
		return visibilityChanged;
	}

	public void setVisibilityChanged(boolean visibilityChanged) {
		this.visibilityChanged = visibilityChanged;
	}

	/**
	 * Sets turtle visibility.
	 * 
	 * @param hidden
	 *          boolean visibility
	 */
	public void setHiddenQ(boolean hidden) {
		if (this.hiddenQ != hidden) {
			setVisibilityChanged(true);
		}
		this.hiddenQ = hidden;
	}

	/**
	 * Returns the label.
	 * 
	 * @return the turtle label
	 */
	public Object getLabel() {
		return label;
	}

	/**
	 * Sets the label.
	 * 
	 * @param label
	 *          a label
	 */
	public void setLabel(Object label) {
		this.label = label;
	}

	/**
	 * Returns the label color for a turtle or link.
	 * 
	 * @ returns label color in the range [0, 140)
	 */
	public double getLabelColor() {
		return labelColor;
	}

	/**
	 * Sets the label color for a turtle to labelColor.
	 * 
	 * @param labelColor
	 *          a number in range [0, 140)
	 */
	public void setLabelColor(Number labelColor) {
		this.labelColor = Utility.wrapColor(labelColor.doubleValue());
	}

	/**
	 * Returns the pen setting of a turtle.
	 * 
	 * @return pen setting of a turtle, PEN_UP or PEN_DOWN
	 */
	public int getPenMode() {
		return penMode;
	}

	/**
	 * Sets the pen setting of a turtle to penMode.
	 * 
	 * @param penMode
	 *          a pen mode, PEN_UP or PEN_DOWN
	 */
	public void setPenMode(Number penMode) {
		this.penMode = penMode.intValue();
	}

	/**
	 * Sets the turtle's pen to stop drawing lines.
	 */
	public void pu() {
		penUp();
	}

	/**
	 * Sets the turtle's pen to stop drawing lines.
	 */
	public void penUp() {
		setPenMode(Utility.PEN_UP);
		this.lastWayPoint = null;
	}

	/**
	 * Sets the turtle's pen to draw lines.
	 */
	public void pd() {
		penDown();
	}

	/**
	 * Sets the turtle's pen to draw lines.
	 */
	public void penDown() {
		setPenMode(Utility.PEN_DOWN);
	}

	/**
	 * Does nothing, included for translation compatibility.
	 */
	public void pe() {
		penErase();
	}

	/**
	 * Does nothing, included for translation compatibility.
	 */
	public void penErase() {
		// do nothing
	}

	/**
	 * Returns the pen width of a turtle.
	 * 
	 * @return pen width of a turtle
	 */
	public int getPenSize() {
		return penSize;
	}

	/**
	 * Sets the pen width of a turtle to penSize.
	 * 
	 * @param penSize
	 *          a number
	 */
	public void setPenSize(Number penSize) {
		this.penSize = penSize.intValue();
	}

	/**
	 * Returns the shape of a turtle.
	 * 
	 * @return shape of a turtle
	 */
	public String getShape() {
		return shape;
	}

	/**
	 * Sets the shape of a turtle to shape.
	 * 
	 * @param shape
	 *          a string
	 */
	public void setShape(String shape) {
		this.shape = shape;
		setShapeChanged(true);
	}

	/**
	 * Returns the size of a turtle.
	 * 
	 * @return size of a turtle
	 */
	public double getSize() {
		return size;
	}

	/**
	 * Sets the size of a turtle to size.
	 * 
	 * @param size
	 *          a number
	 */
	public void setSize(Number size) {
		this.size = size.doubleValue();
	}

	/**
	 * Returns the id number of a turtle.
	 * 
	 * @return id number of a turtle
	 */
	public int getWho() {
		return who;
	}

	/**
	 * Rotates the turtle to the right num degrees.
	 * 
	 * @param num
	 *          an angle in degrees in the range [0, 360]
	 */
	public void rt(Number num) {
		this.setHeading(heading + num.doubleValue());
	}

	/**
	 * Rotates the turtle to the right num degrees.
	 * 
	 * @param num
	 *          an angle in degrees in the range [0, 360]
	 */
	public void right(Number num) {
		rt(num);
	}

	/**
	 * Rotates the turtle to the left num degrees.
	 * 
	 * @param num
	 *          an angle in degrees in the range [0, 360]
	 */
	public void lt(Number num) {
		this.setHeading(heading - num.doubleValue());
	}

	/**
	 * Rotates the turtle to the left num degrees.
	 * 
	 * @param num
	 *          an angle in degrees in the range [0, 360]
	 */
	public void left(Number num) {
		lt(num);
	}

	/**
	 * Steps turtle forward by a distance.
	 * 
	 * @param num
	 *          a distance
	 */
	public void fd(Number num) {
		mv(num);
	}

	/**
	 * Steps turtle forward by a distance.
	 * 
	 * @param num
	 *          a distance
	 */
	public void forward(Number num) {
		fd(num);
	}

	/**
	 * Steps turtle backwards by a distance.
	 * 
	 * @param num
	 *          a distance
	 */
	public void bk(Number num) {
		mv(-num.doubleValue());
	}

	/**
	 * Steps turtle backwards by a distance.
	 * 
	 * @param num
	 *          a distance
	 */
	public void back(Number num) {
		bk(num);
	}

	/**
	 * Moves turtle forward num units.
	 * 
	 * @param num
	 *          a number
	 */
	public void jump(Number num) {
		if (this.canMoveQ(num)) {
			fd(num);
		}
	}

	public void move(Number nNumber) {
		mv(nNumber);
	}

	public void mv(Number nNumber) {
		double number = nNumber.doubleValue();
		if (!moved) {
			this.moved = true;
			ContinuousSpace space = getMyObserver().getSpace();

			if (penMode == Utility.PEN_DOWN) {
				if (lastWayPoint == null) {
					lastWayPoint = new WayPoint();
					getMyObserver().getContext().add(lastWayPoint);
					space.moveTo(lastWayPoint, space.getLocation(this).toDoubleArray(null));
				}
			}

			// Note that Theta_R = Pi/2 - Theta_N_in_Rads
			double[] anglesForMoveByVector = { (Math.PI / 2) - getHeadingInRads(), 0.0 };
			NdPoint oldLocation = new NdPoint(getTurtleLocation().toDoubleArray(null));

			this.loc = space.moveByVector(this, number, anglesForMoveByVector); // move
			// in
			// space
			getMyObserver().getGrid().moveTo(this, Utility.ndPointToIntArray(getTurtleLocation())); // move
																																															// in
			// grid
			// setxy(xcor+(number * Math.sin(heading * Math.PI / 180)),
			// ycor+(number * Math.cos(heading * Math.PI / 180)) )

			if (!fixedLeaves.isEmpty() || !freeLeaves.isEmpty()) {
				double[] displacement = space.getDisplacement(oldLocation, getTurtleLocation());
				// for all leaves, displace by same amount
				Set<Turtle> temporarySet = new HashSet<Turtle>(fixedLeaves);
				temporarySet.addAll(freeLeaves);
				for (Turtle t : temporarySet) {
					setxy(t.getXcor() + displacement[0], t.getYcor() + displacement[1]);
				}
			}

			if (penMode == Utility.PEN_DOWN) {
				boolean newLocationWrapped = false;
				if (space.isPeriodic()) {
					newLocationWrapped = ((WrapAroundBorders) space.getPointTranslator())
							.isNewLocationWrapped();
				}

				WayPoint newWayPoint = new WayPoint();
				getMyObserver().getContext().add(newWayPoint);
				space.moveTo(newWayPoint, space.getLocation(this).toDoubleArray(null));

				if (!newLocationWrapped) {

					Network<WayPoint> net = (Network<WayPoint>) getMyObserver().getContext().getProjection(
							"Tracking Network");
					TrackingEdge trackingEdge = (TrackingEdge) net.addEdge(lastWayPoint, newWayPoint);
					trackingEdge.setColor(this.getColor());
					trackingEdge.setSize(this.getPenSize());
				}

				lastWayPoint = newWayPoint;
			}
			this.moved = false;
		}
	}

	/**
	 * Sets the x-y coordinates of a turtle to (nX, nY).
	 * 
	 * @param nX
	 *          a number
	 * @param nY
	 *          a number
	 */
	public void setxy(Number nX, Number nY) {
		double x = nX.doubleValue();
		double y = nY.doubleValue();

		if (!moved) {
			this.moved = true;
			ContinuousSpace space = getMyObserver().getSpace();
			if (penMode == Utility.PEN_DOWN) {
				if (lastWayPoint == null) {
					lastWayPoint = new WayPoint();
					getMyObserver().getContext().add(lastWayPoint);
					space.moveTo(lastWayPoint, space.getLocation(this).toDoubleArray(null));
				}
			}
			double[] point = { x, y };
			NdPoint oldLocation = new NdPoint(getTurtleLocation().toDoubleArray(null));

			getMyObserver().getSpace().moveTo(this, point);
			this.loc = new NdPoint(point);
			getMyObserver().getGrid().moveTo(this, Utility.ndPointToIntArray(getTurtleLocation()));

			if (!fixedLeaves.isEmpty() || !freeLeaves.isEmpty()) {
				double[] displacement = space.getDisplacement(oldLocation, getTurtleLocation());
				Set<Turtle> temporarySet = new HashSet<Turtle>(fixedLeaves);
				temporarySet.addAll(freeLeaves);
				for (Turtle t : temporarySet) {
					setxy(t.getXcor() + displacement[0], t.getYcor() + displacement[1]);
				}
			}

			if (penMode == Utility.PEN_DOWN) {
				WayPoint newWayPoint = new WayPoint();
				getMyObserver().getContext().add(newWayPoint);

				space.moveTo(newWayPoint, space.getLocation(this).toDoubleArray(null));

				Network net = (Network) getMyObserver().getContext().getProjection("Tracking Network");
				TrackingEdge trackingEdge = (TrackingEdge) net.addEdge(lastWayPoint, newWayPoint);
				trackingEdge.setColor(this.getColor());
				trackingEdge.setSize(this.getPenSize());

				lastWayPoint = newWayPoint;
			}
			this.moved = false;
		}
	}

	/**
	 * Removes the turtle.
	 */
	public void die() {
		getMyObserver().getContext().remove(this);
		notifySubscribers();
	}

	/**
	 * Turtle appears hidden.
	 */
	public void ht() {
		setHiddenQ(true);
	}

	/**
	 * Turtle appears hidden.
	 */
	public void hideTurtle() {
		ht();
	}

	/**
	 * Makes turtle visible.
	 */
	public void st() {
		setHiddenQ(false);
	}

	/**
	 * Makes turtle visible.
	 */
	public void showTurtle() {
		st();
	}

	/**
	 * Turtle goes to (0,0).
	 */
	public void home() {
		setxy(0.0, 0.0);
	}

	/**
	 * Returns the turtle's x increment for one step.
	 * 
	 * @returns turtle's x increment for one step
	 */
	public double dx() {
		return Math.sin(heading * Math.PI / 180);
	}

	/**
	 * Returns the turtle's y increment for one step.
	 * 
	 * @returns turtle's y increment for one step
	 */
	public double dy() {
		return Math.cos(heading * Math.PI / 180);
	}

	/**
	 * Returns the distance from the caller to a turtle.
	 * 
	 * @param t
	 *          a turtle
	 * @returns distance from turtle t to the caller
	 */
	public double distance(Turtle t) {
		return getMyObserver().getSpace().getDistance(getTurtleLocation(), t.getTurtleLocation());
	}

	/**
	 * Returns the distance from the caller to a patch.
	 * 
	 * @param p
	 *          a patch
	 * @returns distance from patch p to the caller
	 */
	public double distance(Patch p) {
		return getMyObserver().getSpace()
				.getDistance(getTurtleLocation(), p.getGridLocationAsNdPoint());
	}

	/**
	 * Returns the distance from the caller to a point.
	 * 
	 * @param nx
	 *          a number
	 * @param ny
	 *          a number
	 * @returns distance from the caller to the point (nx,ny)
	 */
	public double distancexy(Number nX, Number nY) {
		double x = nX.doubleValue();
		double y = nY.doubleValue();
		return getMyObserver().getSpace().getDistance(getTurtleLocation(), new NdPoint(x, y));
	}

	/**
	 * Returns the direction to turtle t.
	 * 
	 * @param t
	 *          a turtle
	 * @return direction from this turtle to turtle t
	 */
	public double towards(Turtle t) {
		double[] displacement = getMyObserver().getSpace().getDisplacement(getTurtleLocation(),
				t.getTurtleLocation());
		return Utility.angleFromDisplacement(displacement[0], displacement[1]);
	}

	/**
	 * Returns the direction to patch p.
	 * 
	 * @param p
	 *          a patch
	 * @return direction from this patch to patch p
	 */
	public double towards(Patch p) {
		double[] displacement = getMyObserver().getSpace().getDisplacement(getTurtleLocation(),
				p.getGridLocationAsNdPoint());
		return Utility.angleFromDisplacement(displacement[0], displacement[1]);
	}

	/**
	 * Returns the direction from a turtle or patch to a point.
	 * 
	 * @param nX
	 *          a number
	 * @param nY
	 *          a number
	 * @return direction from a turtle or patch to the point (nX, nY)
	 */
	public double towardsxy(Number nX, Number nY) {
		double x = nX.doubleValue();
		double y = nY.doubleValue();
		double[] displacement = getMyObserver().getSpace().getDisplacement(getTurtleLocation(),
				new NdPoint(x, y));
		return Utility.angleFromDisplacement(displacement[0], displacement[1]);
	}

	/**
	 * Faces the caller towards a turtle.
	 * 
	 * @param t
	 *          a turtle
	 */
	public void face(Turtle t) {
		if (!t.getTurtleLocation().equals(getTurtleLocation())) {
			this.setHeading(this.towards(t));
		}
	}

	/**
	 * Faces the caller towards a patch.
	 * 
	 * @param p
	 *          a patch
	 */
	public void face(Patch p) {
		if (!p.getGridLocationAsNdPoint().equals(getTurtleLocation())) {
			this.setHeading(this.towards(p));
		}
	}

	/**
	 * Faces the caller towards a point.
	 * 
	 * @param nX
	 *          x coordinate
	 * @param nY
	 *          y coordinate
	 */
	public void facexy(Number nX, Number nY) {
		double x = nX.doubleValue();
		double y = nY.doubleValue();
		if (!getTurtleLocation().equals(new NdPoint(x, y))) {
			this.setHeading(this.towardsxy(x, y));
		}
	}

	/**
	 * Queries if turtle can move a distance.
	 * 
	 * @param nDist
	 *          a distance
	 * @return true or false based on whether turtle can move distance
	 */
	public boolean canMoveQ(Number nDist) {
		double dist = nDist.doubleValue();
		double xDisp = getTurtleLocation().getX() + dist * this.dx();
		double yDisp = getTurtleLocation().getY() + dist * this.dy();
		PointTranslator pt = getMyObserver().getSpace().getPointTranslator();
		try {
			double[] temp = new double[2];
			pt.translate(getTurtleLocation(), temp, xDisp, yDisp);
			return true;
		} catch (SpatialException se) {
			return false;
		}
	}

	/**
	 * Moves a turtle to the same location as turtle t.
	 * 
	 * @param t
	 *          a turtle
	 */
	public void moveTo(Turtle t) {
		this.setxy(t.getTurtleLocation().getX(), t.getTurtleLocation().getY());
	}

	/**
	 * Moves a turtle to the same location as patch p.
	 * 
	 * @param p
	 *          a patch
	 */
	public void moveTo(Patch p) {
		this.setxy(p.getGridLocationAsNdPoint().getX(), p.getGridLocationAsNdPoint().getY());
	}

	/**
	 * Returns the agentset on the patch at the direction (ndx, ndy) from the
	 * caller.
	 * 
	 * @param nX
	 *          a number
	 * @param nY
	 *          a number
	 * @returns agentset at the direction (nX, nY) from the caller
	 */
	public AgentSet<Turtle> turtlesAt(Number nX, Number nY) {
		double dx = nX.doubleValue();
		double dy = nY.doubleValue();
		double[] displacement = { dx, dy };
		try {
			GridPoint gridPoint = Utility.getGridPointAtDisplacement(getTurtleLocation(), displacement,
					getMyObserver());
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
	 *          a patch
	 * @return agentset of turtles on patch p
	 */
	public AgentSet<Turtle> turtlesOn(Patch p) {
		return Utility.getTurtlesOnGridPoint(p.getGridLocation(), getMyObserver());
	}

	/**
	 * Returns an agentset of turtles on the same patch as a turtle.
	 * 
	 * @param t
	 *          a turtle
	 * @return agentset of turtles on the same patch as turtle t
	 */
	public AgentSet<Turtle> turtlesOn(Turtle t) {
		return Utility.getTurtlesOnGridPoint(Utility.ndPointToGridPoint(t.getTurtleLocation()),
				getMyObserver());
	}

	/**
	 * Returns an agentset of turtles on the patches in a collection or on the
	 * patches that a collection of turtles are.
	 * 
	 * @param a
	 *          a collection
	 * @return agentset of turtles on the patches in collection a or on the
	 *         patches that collection a turtles are
	 */
	public AgentSet<Turtle> turtlesOn(Collection a) {

		if (a == null || a.isEmpty()) {
			return new AgentSet();
		}

		Set total = new HashSet();
		if (a.iterator().next() instanceof Turtle) {
			for (Object t : a) {
				NdPoint location = ((Turtle) t).getTurtleLocation();
				AgentSet temp = Utility.getTurtlesOnGridPoint(Utility.ndPointToGridPoint(location),
						getMyObserver());
				total.addAll(temp);
			}
		} else {
			for (Object p : a) {
				GridPoint location = ((Patch) p).getGridLocation();
				AgentSet temp = Utility.getTurtlesOnGridPoint(location, getMyObserver());
				total.addAll(temp);
			}
		}
		return new AgentSet(total);
	}

	/**
	 * Returns the patch where the turtle is located.
	 * 
	 * @return patch where the turtle is located
	 */
	public Patch patchHere() {
		return Utility.getPatchAtLocation(Utility.ndPointToGridPoint(this.getTurtleLocation()),
				getMyObserver());
	}

	/**
	 * Returns the patch that is at a distance ahead of a turtle.
	 * 
	 * @param distance
	 *          a number
	 * @return patch that is at distance from the calling turtle
	 */
	public Patch patchAhead(Number distance) {
		double[] displacement = Utility.getDisplacementFromHeadingAndDistance(this.heading,
				distance.doubleValue());
		try {
			return Utility.getPatchAtLocation(
					Utility.getGridPointAtDisplacement(getTurtleLocation(), displacement, getMyObserver()),
					getMyObserver());
		} catch (SpatialException e) {
			return null;
		}
	}

	/**
	 * Returns the patch at a direction (nX, nY) from the caller.
	 * 
	 * @param nX
	 *          a number
	 * @param nY
	 *          a number
	 * @return patch at a direction (nX, nY) from the caller
	 */
	public Patch patchAt(Number nX, Number nY) {
		double dx = nX.doubleValue();
		double dy = nY.doubleValue();
		double[] displacement = { dx, dy };
		try {
			return Utility.getPatchAtLocation(
					Utility.getGridPointAtDisplacement(getTurtleLocation(), displacement, getMyObserver()),
					getMyObserver());
		} catch (SpatialException e) {
			return null;
		}
	}

	/**
	 * Returns the patch that is at a direction and distance from the caller.
	 * 
	 * @param nHeading
	 *          a number
	 * @param nDistance
	 *          a number
	 * @return patch that is at nHeading and nDistance from the caller
	 */
	public Patch patchAtHeadingAndDistance(Number nHeading, Number nDistance) {
		double[] displacement = Utility.getDisplacementFromHeadingAndDistance(nHeading.doubleValue(),
				nDistance.doubleValue());
		try {
			return Utility.getPatchAtLocation(
					Utility.getGridPointAtDisplacement(getTurtleLocation(), displacement, getMyObserver()),
					getMyObserver());
		} catch (SpatialException e) {
			return null;
		}
	}

	/**
	 * Returns the patch that is at a distance and degrees left from the caller.
	 * 
	 * @param nAngle
	 *          an angle in degrees
	 * @param nDistance
	 *          a number
	 * @return patch that is at nDistance and nAngle left from the caller
	 */
	public Patch patchLeftAndAhead(Number nAngle, Number nDistance) {
		double angle = nAngle.doubleValue();
		double distance = nDistance.doubleValue();
		double heading = Utility.getNLAngle(this.heading - angle);
		double[] displacement = Utility.getDisplacementFromHeadingAndDistance(heading, distance);
		try {
			return Utility.getPatchAtLocation(
					Utility.getGridPointAtDisplacement(getTurtleLocation(), displacement, getMyObserver()),
					getMyObserver());
		} catch (SpatialException e) {
			return null;
		}
	}

	/**
	 * Returns the patch that is at a distance and degrees right from the caller.
	 * 
	 * @param nAngle
	 *          an angle in degrees
	 * @param nDistance
	 *          a number
	 * @return patch that is at nDistance and nAngle right from the caller
	 */
	public Patch patchRightAndAhead(Number nAngle, Number nDistance) {
		double angle = nAngle.doubleValue();
		double distance = nDistance.doubleValue();
		double heading = Utility.getNLAngle(this.heading + angle);
		double[] displacement = Utility.getDisplacementFromHeadingAndDistance(heading, distance);
		try {
			return Utility.getPatchAtLocation(
					Utility.getGridPointAtDisplacement(getTurtleLocation(), displacement, getMyObserver()),
					getMyObserver());
		} catch (SpatialException e) {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public AgentSet<Patch> neighbors() {
		return patchHere().neighbors();
	}

	/**
	 * {@inheritDoc}
	 */
	public AgentSet<Patch> neighbors(int extent) {
		return patchHere().neighbors(extent, extent);
	}

	/**
	 * {@inheritDoc}
	 */
	public AgentSet<Patch> neighbors(int extentX, int extentY) {
		return patchHere().neighbors(extentX, extentY);
	}

	/**
	 * {@inheritDoc}
	 */
	public AgentSet<Patch> neighbors4() {
		return patchHere().neighbors4();
	}

	/**
	 * {@inheritDoc}
	 */
	public AgentSet<Patch> neighbors4(int extent) {
		return patchHere().neighbors4(extent, extent);
	}

	/**
	 * {@inheritDoc}
	 */
	public AgentSet<Patch> neighbors4(int extentX, int extentY) {
		return patchHere().neighbors4(extentX, extentY);
	}

	/**
	 * Returns an agentset within a distance of the caller.
	 * 
	 * @param a
	 *          a collection of agents
	 * @param num
	 *          a distance
	 * @return agentset subset of collection a within a distance num of the caller
	 */
	public AgentSet inRadius(Collection a, Number num) {

		ContinuousSpace space = getMyObserver().getSpace();
		double distSq = num.doubleValue() * num.doubleValue();
		AgentSet list = new AgentSet();
		for (Object o : a) {
			if (space.getDistanceSq(getTurtleLocation(), space.getLocation(o)) <= distSq) {
				list.add(o);
			}
		}
		return list;
	}

	/**
	 * Returns an agentset within a distance and heading cone of the caller.
	 * 
	 * @param a
	 *          a collection of agents
	 * @param num
	 *          a distance
	 * 
	 * @param anlge
	 *          the cone angle centered on caller's heading
	 * 
	 * @return agentset subset of collection a within a distance num and within
	 *         cone angle of the caller
	 */
	public AgentSet inCone(Collection a, Number num, Number angle) {
		if (angle.doubleValue() >= 360) {
			return inRadius(a, num);
		}
		ContinuousSpace space = getMyObserver().getSpace();
		double distSq = num.doubleValue() * num.doubleValue();
		AgentSet list = new AgentSet();
		for (Object o : a) {
			if (space.getDistanceSq(getTurtleLocation(), space.getLocation(o)) <= distSq) {
				if (o instanceof Turtle) {
					double candidateHeading = towards((Turtle) o);
					if (Math.abs(Utility.subtractHeadings(getHeading(), candidateHeading)) < angle
							.doubleValue() / 2) {
						list.add(o);
					}
				}
				if (o instanceof Patch) {
					double candidateHeading = towards((Patch) o);
					if (Math.abs(Utility.subtractHeadings(getHeading(), candidateHeading)) < angle
							.doubleValue() / 2) {
						list.add(o);
					}
				}

			}
		}
		return list;
	}

	/**
	 * Returns an agentset minus the caller.
	 * 
	 * @param a
	 *          an agentset
	 * @return agentset a minus the caller
	 */
	public AgentSet other(Collection a) {
		return Utility.removeTurtleFromCollection(a, (Turtle) this);
	}

	public static int getWhoCounter() {
		return whoCounter;
	}

	public static void setWhoCounter(Number whoCounter) {
		BaseTurtle.whoCounter = whoCounter.intValue();
	}

	/**
	 * Prints value with agent identifier to current file with a newline.
	 * 
	 * @param value
	 *          any object
	 */
	public void fileShow(Object value) {
		UtilityG.fileShowU(toString(), value);
	}

	/**
	 * Prints value with agent identifier to the console.
	 * 
	 * @param value
	 *          any object
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
		return getTurtleType() + " " + getWho();
	}

	// create links code

	/**
	 * Makes a directed link from a turtle to the caller.
	 * 
	 * @param t
	 *          a turtle
	 * @return created link
	 */
	public Link createLinkFrom(Turtle t) {
		return createLinkFrom(t, null);
	}

	/**
	 * Makes a directed link from a turtle to the caller then executes a set of
	 * commands on the created link.
	 * 
	 * @param t
	 *          a turtle
	 * @param closure
	 *          a set of commands
	 * @return created link
	 */
	public Link createLinkFrom(Turtle t, Closure closure) {
		Network network = getMyObserver().getNetwork("DirectedLinks");
		Link link = (Link) network.addEdge(t, this);

		if (closure != null) {
			ask(link, closure);
		}
		return link;
	}

	/**
	 * Makes a directed link from the caller to a turtle.
	 * 
	 * @param t
	 *          a turtle
	 * @return created link
	 */
	public Link createLinkTo(Turtle t) {
		return createLinkTo(t, null);
	}

	/**
	 * Makes a directed link from the caller to a turtle then executes a set of
	 * commands on the created link.
	 * 
	 * @param t
	 *          a turtle
	 * @param closure
	 *          a set of commands
	 * @return created link
	 */
	public Link createLinkTo(Turtle t, Closure closure) {
		Network network = getMyObserver().getNetwork("DirectedLinks");
		Link link = (Link) network.addEdge(this, t);

		if (closure != null) {
			ask(link, closure);
		}
		return link;

	}

	/**
	 * Makes a undirected link between the caller and a turtle.
	 * 
	 * @param t
	 *          a turtle
	 * @return created link
	 */
	public Link createLinkWith(Turtle t) {
		return createLinkWith(t, null);
	}

	/**
	 * Makes an undirected link between the caller and a turtle then executes a
	 * set of commands on the created link.
	 * 
	 * @param t
	 *          a turtle
	 * @param closure
	 *          a set of commands
	 * @return created link
	 */
	public Link createLinkWith(Turtle t, Closure closure) {
		Network network = getMyObserver().getNetwork("UndirectedLinks");
		Link link = (Link) network.addEdge(this, t);

		if (closure != null) {
			ask(link, closure);
		}
		return link;
	}

	/**
	 * Makes directed links from a collection of agents to the caller.
	 * 
	 * @param a
	 *          a collection of agents
	 * @return created links
	 */
	public AgentSet<Link> createLinksFrom(Collection<? extends Turtle> a) {
		return createLinksFrom(a, null);
	}

	/**
	 * Makes directed links from a collection of turtles to the caller then
	 * executes a set of commands on the created links.
	 * 
	 * @param a
	 *          a collection of agents
	 * @param closure
	 *          a set of commands
	 * @return created links
	 */
	public AgentSet<Link> createLinksFrom(Collection<? extends Turtle> a, Closure closure) {
		Network network = getMyObserver().getNetwork("DirectedLinks");
		AgentSet links = new AgentSet();
		for (Turtle t : a) {
			links.add(network.addEdge((Turtle) t, this));
		}
		if (closure != null) {
			ask(links, closure);
		}
		return links;
	}

	/**
	 * Makes directed links from the caller to a collection of agents.
	 * 
	 * @param a
	 *          a collection of agents
	 * @return created links
	 */
	public AgentSet<Link> createLinksTo(Collection<? extends Turtle> a) {
		return createLinksTo(a, null);
	}


	/**
	 * Makes directed links from the caller to a collection of agents then
	 * executes a set of commands on the created links.
	 * 
	 * @param a
	 *          a collection of agents
	 * @param closure
	 *          a set of commands
	 * @return created links
	 */
	public AgentSet<Link> createLinksTo(Collection<? extends Turtle> a, Closure closure) {
		Network network = getMyObserver().getNetwork("DirectedLinks");
		AgentSet links = new AgentSet();
		for (Turtle t : a) {
			links.add(network.addEdge(this, t));
		}
		if (closure != null) {
			ask(links, closure);
		}
		return links;
	}

	/**
	 * Makes undirected links between the caller and a collection of agents.
	 * 
	 * @param a
	 *          an collection of agents
	 * @return created links
	 */
	public AgentSet<Link> createLinksWith(Collection<? extends Turtle> a) {
		return createLinksWith(a, null);
	}

	/**
	 * Makes undirected links between the caller and a collection of agents then
	 * executes a set of commands on the created links.
	 * 
	 * @param a
	 *          a collection of agents
	 * @param closure
	 *          a set of commands
	 * @return created links
	 */
	public AgentSet<Link> createLinksWith(Collection<? extends Turtle> a, Closure closure) {
		Network network = getMyObserver().getNetwork("UndirectedLinks");
		AgentSet links = new AgentSet();
		for (Turtle t : a) {
			links.add(network.addEdge(this, t));
		}
		if (closure != null) {
			ask(links, closure);
		}
		return links;
	}

	/**
	 * Queries if there is a directed link from a turtle to the caller.
	 * 
	 * @param t
	 *          a turtle
	 * @return true or false based on whether there is a directed link from turtle
	 *         t to the caller
	 */
	public boolean inLinkNeighborQ(Turtle t) {
		Network network = getMyObserver().getNetwork("DirectedLinks");
		return network.isPredecessor(t, this);
	}

	/**
	 * Queries if there is a directed link from the caller to the turtle.
	 * 
	 * @param t
	 *          a turtle
	 * @return true or false based on whether there is a directed link from the
	 *         caller to the turtle.
	 */
	public boolean outLinkNeighborQ(Turtle t) {
		Network network = getMyObserver().getNetwork("DirectedLinks");
		return network.isPredecessor(this, t);
	}

	/**
	 * Reports true if there is an undirected link connecting t and the caller.
	 */
	public boolean linkNeighborQ(Turtle t) {
		Network network = getMyObserver().getNetwork("UndirectedLinks");
		return network.isAdjacent(this, t);
	}

	/**
	 * Returns the agentset with directed links to the caller.
	 * 
	 * @return agentset with directed links to the caller
	 */
	public AgentSet<Turtle> inLinkNeighbors() {
		Network network = getMyObserver().getNetwork("DirectedLinks");
		return Utility.agentSetFromIterable(network.getPredecessors(this));
	}

	/**
	 * Returns the agentset of the caller's out link neighbor turtles.
	 * 
	 * @return agentset of the caller's out link neighbor turtles
	 */
	public AgentSet<Turtle> outLinkNeighbors() {
		Network network = getMyObserver().getNetwork("DirectedLinks");
		return Utility.agentSetFromIterable(network.getSuccessors(this));
	}

	/**
	 * Reports the agentset of all turtles found at the other end of undirected
	 * links connected to the calling turtle.
	 */
	public AgentSet<Turtle> linkNeighbors() {
		Network network = getMyObserver().getNetwork("UndirectedLinks");
		return Utility.agentSetFromIterable(network.getAdjacent(this));
	}

	/**
	 * Returns the directed link from a turtle to the caller.
	 * 
	 * @param t
	 *          a turtle
	 * @return directed link from turtle t to the caller
	 */
	public Link inLinkFrom(Turtle t) {
		Network network = getMyObserver().getNetwork("DirectedLinks");
		return (Link) network.getEdge(t, this);
	}

	/**
	 * Returns the caller's directed link to a turtle.
	 * 
	 * @param t
	 *          a turtle
	 * @return the caller's directed link to turtle t
	 */
	public Link outLinkTo(Turtle t) {
		Network network = getMyObserver().getNetwork("DirectedLinks");
		return (Link) network.getEdge(this, t);
	}

	/**
	 * Report the link between t and the caller. If no link exists then it reports
	 * null.
	 */
	public Link linkWith(Turtle t) {
		Network network = getMyObserver().getNetwork("UndirectedLinks");
		return (Link) network.getEdge(this, t);
	}

	/**
	 * {@inheritDoc}
	 */
	public AgentSet<Link> allMyInLinks() {
		List list = new ArrayList();
		List directedNetworks = Utility.getDirectedNetworks(getMyObserver());
		for (Object n : directedNetworks) {
			for (Object l : ((Network) n).getInEdges(this)) {
				list.add(l);
			}
		}
		return new AgentSet(list);
	}

	/**
	 * {@inheritDoc}
	 */
	public AgentSet<Link> myInLinks() {
		return Utility.agentSetFromIterable(getMyObserver().getNetwork("DirectedLinks")
				.getInEdges(this));
	}

	/**
	 * {@inheritDoc}
	 */
	public AgentSet<Link> allMyOutLinks() {
		List list = new ArrayList();
		List directedNetworks = Utility.getDirectedNetworks(getMyObserver());
		for (Object n : directedNetworks) {
			for (Object l : ((Network) n).getOutEdges(this)) {
				list.add(l);
			}
		}
		return new AgentSet(list);
	}

	/**
	 * {@inheritDoc}
	 */
	public AgentSet<Link> myOutLinks() {
		return Utility.agentSetFromIterable(getMyObserver().getNetwork("DirectedLinks").getOutEdges(
				this));
	}

	/**
	 * {@inheritDoc}
	 */
	public AgentSet<Link> allMyLinks() {
		List list = new ArrayList();
		List directedNetworks = Utility.getUndirectedNetworks(getMyObserver());
		for (Object n : directedNetworks) {
			for (Object l : ((Network) n).getEdges(this)) {
				list.add(l);
			}
		}
		return new AgentSet(list);
	}

	/**
	 * {@inheritDoc}
	 */
	public AgentSet<Link> myLinks() {
		return Utility.agentSetFromIterable(getMyObserver().getNetwork("UndirectedLinks")
				.getEdges(this));
	}

	/**
	 * Returns the turtle opposite the asking link.
	 * 
	 * @return turtle opposite the asking link
	 */
	public Turtle otherEnd() {
		return (Turtle) other(((Link) myself()).bothEnds()).get(0);
	}

	/**
	 * Returns the turtle of the given number.
	 * 
	 * @param number
	 *          a number
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
	 *          x coordinate
	 * @param nY
	 *          y coordinate
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

	/**
	 * Returns the link between two turtles.
	 * 
	 * @param oneEnd
	 *          an integer
	 * @param otherEnd
	 *          an integer
	 * @return link between two turtles
	 */
	public Link link(Number oneEnd, Number otherEnd) {
		return Utility.linkU(oneEnd.intValue(), otherEnd.intValue(), getMyObserver());
	}

	/**
	 * Returns the link between two turtles.
	 * 
	 * @param oneEnd
	 *          a turtle
	 * @param otherEnd
	 *          a turtle
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

	public int compareTo(Turtle t) {

		return (this.who == ((Turtle) t).getWho() ? 0 : this.who < ((Turtle) t).getWho() ? -1 : 1);

	}

	/**
	 * Stops a turtle executing within a command closure.
	 * @deprecated use the {@link repast.simphony.relogo.Utility#stop()} method instead.
	 */
	@Deprecated
	public Stop oldStop() {
		return Stop.TRUE;
	}

	/**
	 * Returns the ReLogoAgent with the smallest value when operated on by a set
	 * of commands.
	 * 
	 * @param a
	 *          a collection
	 * @param closure
	 *          a set of commands
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
	 *          an integer
	 * @param a
	 *          a collection
	 * @param closure
	 *          a set of commands
	 * @return agentset containing number agents with smallest values when
	 *         operated on by closure
	 */
	public AgentSet minNOf(int number, Collection<? extends ReLogoAgent> a, Closure closure) {
		return Utility.minNOfU(this, number, a, closure);
	}

	/**
	 * Returns the ReLogoAgent with the largest value when operated on by a set of
	 * commands.
	 * 
	 * @param a
	 *          a collection
	 * @param closure
	 *          a set of commands
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
	 *          an integer
	 * @param a
	 *          a collection
	 * @param closure
	 *          a set of commands
	 * @return agentset containing number agents with largest values when operated
	 *         on by closure
	 */
	public AgentSet maxNOf(int number, Collection<? extends ReLogoAgent> a, Closure closure) {
		return Utility.maxNOfU(this, number, a, closure);
	}

	/**
	 * Queries if all agents in a collection are true for a boolean closure.
	 * 
	 * @param a
	 *          a collection of ReLogoAgents
	 * @param closure
	 *          a boolean closure
	 * @return true or false based on whether all agents in a collection are true
	 *         for closure
	 */
	public boolean allQ(Collection a, Closure closure) {
		return Utility.allQU(this, a, closure);
	}

}
