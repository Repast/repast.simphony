package repast.simphony.relogo;

import groovy.lang.Closure;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import javolution.util.FastSet;

import org.apache.commons.collections15.Predicate;
import org.apache.commons.collections15.functors.EqualPredicate;
import org.apache.commons.collections15.functors.InstanceofPredicate;
import org.apache.commons.collections15.functors.NotPredicate;

import repast.simphony.context.Context;
import repast.simphony.data2.DataConstants;
import repast.simphony.data2.DataSetRegistry;
import repast.simphony.data2.FileDataSink;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.environment.RunState;
import repast.simphony.query.PropertyEquals;
import repast.simphony.query.Query;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.SpatialException;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.graph.Network;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.ui.RSApplication;
import repast.simphony.util.SimUtilities;
import repast.simphony.util.collections.FilteredIterator;

/**
 * ReLogo Utility class.
 * 
 * @author jozik
 *
 */
public class Utility {
	public static final int PEN_UP = 0;
	public static final int PEN_DOWN = 1;
	public static final int PEN_ERASE = 2;

	/**
	 * Returns a random number (floating point).
	 * 
	 * @param num
	 *            a number
	 * @return random number (floating point) in range [0, num) if num > 0 or
	 *         (num,0] if num < 0
	 */
	public static double randomFloat(Number num) {
		if (num.doubleValue() == 0)
			return 0.0;
		return (num.doubleValue() > 0) ? RandomHelper.nextDoubleFromTo(0,
				num.doubleValue()) : RandomHelper.nextDoubleFromTo(
				num.doubleValue(), 0);
	}

	/**
	 * Returns a random number (integer).
	 * 
	 * @param num
	 *            a number
	 * @return random number (integer) in range [0, num) if num > 0 or (num, 0]
	 *         if num < 0
	 */
	public static int random(Number num) {
		if (num.doubleValue() == 0)
			return 0;
		return (num.doubleValue() > 0) ? RandomHelper.nextIntFromTo(0,
				num.intValue() - 1) : RandomHelper.nextIntFromTo(
				num.intValue() + 1, 0);
	}

	// stop
	public static void observerStop() {
		// RunEnvironment.getInstance().pauseRun()
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				pauseReLogo();
				resetAllToggleButtons();
			}
		});
		// pauseReLogo()
	}

	// Must be called on EDT
	public static void resetAllToggleButtons() {
		Map actionMap = ReLogoModel.getInstance().getActions();
		Set keys = actionMap.keySet();
		Map modelParams = ReLogoModel.getInstance().getModelParams();
		for (Object key : keys) {
			modelParams.put(key, false);
		}
	}

	// Must be called on EDT
	public static void pauseReLogo() {

		if (!RSApplication.getRSApplicationInstance().isStartSim()) {
			RunEnvironment.getInstance().pauseRun();
		}

	}

	@SuppressWarnings("unchecked")
	public static void resumeReLogo() {
		if (RSApplication.getRSApplicationInstance().isStartSim()) {
			RSApplication.getRSApplicationInstance().start();
			RunEnvironment.getInstance().resumeRun();
		} else {
			SwingWorker worker = new SwingWorker() {
				@Override
				protected Object doInBackground() throws Exception {
					RunEnvironment.getInstance().resumeRun();
					return null;
				}

			};
			worker.execute();

		}
	}

	public static void checkToPause() {
		
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					if (ReLogoModel.getInstance().getActiveButtons() == 0) {
						pauseReLogo();
					}
				}
			});
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

	}

	public static double randomXcorU(Observer observer) {
		return RandomHelper.nextDoubleFromTo(((double) observer
				.getRLDimensions().getMinPxcor()) - 0.5, ((double) observer
				.getRLDimensions().getMaxPxcor()) + 0.5);
	}

	public static double randomYcorU(Observer observer) {
		return RandomHelper.nextDoubleFromTo(((double) observer
				.getRLDimensions().getMinPycor()) - 0.5, ((double) observer
				.getRLDimensions().getMaxPycor()) + 0.5);
	}

	/**
	 * Returns a random x coordinate for patches.
	 * 
	 * @param observer
	 *            an observer
	 * 
	 * @return random x coordinate for patches
	 */
	public static int randomPxcorU(Observer observer) {
		return RandomHelper.nextIntFromTo(observer.getRLDimensions()
				.getMinPxcor(), observer.getRLDimensions().getMaxPxcor());
	}

	/**
	 * Returns a random y coordinate for patches.
	 * 
	 * @param observer
	 *            an observer
	 * 
	 * @return random y coordinate for patches
	 */
	public static int randomPycorU(Observer observer) {
		return RandomHelper.nextIntFromTo(observer.getRLDimensions()
				.getMinPycor(), observer.getRLDimensions().getMaxPycor());
	}

	public static int worldHeightU(Observer observer) {
		return (observer.getRLDimensions().getYdim());
	}

	public static int worldWidthU(Observer observer) {
		return (observer.getRLDimensions().getXdim());
	}

	/**
	 * Returns the minimum x coordinate for all patches.
	 * 
	 * @param observer
	 *            an observer
	 * @return maximum x coordinate for patches
	 */
	public static int getMinPxcorU(Observer observer) {
		return observer.getRLDimensions().getMinPxcor();
	}

	/**
	 * Returns the maximum x coordinate for all patches.
	 * 
	 * @param observer
	 *            an observer
	 * @return maximum x coordinate for all patches
	 */
	public static int getMaxPxcorU(Observer observer) {
		return observer.getRLDimensions().getMaxPxcor();
	}

	/**
	 * Returns the minimum y coordinate for all patches.
	 * 
	 * @param observer
	 *            an observer
	 * @return maximum y coordinate for patches
	 */
	public static int getMinPycorU(Observer observer) {
		return observer.getRLDimensions().getMinPycor();
	}

	/**
	 * Returns the maximum y coordinate for all patches.
	 * 
	 * @param observer
	 *            an observer
	 * @return maximum y coordinate for all patches
	 */
	public static int getMaxPycorU(Observer observer) {
		return observer.getRLDimensions().getMaxPycor();
	}

	/**
	 * Given a continuous space location, return the corresponding grid location
	 * as an int array. The convention here is that, for each coordinate, [ i -
	 * 0.5 , i + 0.5 ) returns i. E.g., -2.5 -> -2 , -0.5 -> 0, 0.5 -> 1
	 */
	public static int[] ndPointToIntArray(NdPoint loc) {
		int[] gridLocation = new int[loc.dimensionCount()];
		
		if (!(loc == null)) {
			for (int i = 0; i < loc.dimensionCount(); i++) {
				double coord = loc.getCoord(i);
				gridLocation[i] = (int)Math.round(coord);
			}
		}
		return gridLocation;
	}

	/**
	 * Given a continuous space location, return the corresponding grid location
	 * as a GridPoint. The convention here is that, for each coordinate, [ i -
	 * 0.5 , i + 0.5 ) returns i. E.g., -2.5 -> -2 , -0.5 -> 0, 0.5 -> 1
	 */
	public static GridPoint ndPointToGridPoint(NdPoint loc) {
		return new GridPoint(ndPointToIntArray(loc));
	}

	/**
	 * Returns the angle in degrees corresponding to the displacement (dX, dY).
	 * 
	 * @param dX
	 * @param dY
	 * @return
	 */
	public static double angleFromDisplacement(double dX, double dY) {
		if (dY == 0) {
			return dX >= 0 ? 90.0 : 270.0;
		}
		double angRad = Math.atan(dX / dY);
		if (dY > 0) {// quadrants I and IV
			return (angRad < 0) ? Math.toDegrees(2 * Math.PI + angRad) : Math
					.toDegrees(angRad); // quadrant IV
		} else {// quadrants II and III
			return Math.toDegrees(Math.PI + angRad);
		}
	}

	public static AgentSet agentSetFromIterable(Iterable i) {
		AgentSet a = new AgentSet();
		for (Object o : i) {
			a.add(o);
		}
		return a;
	}

	public static <E extends ReLogoAgent> AgentSet<E> agentSetFromIterator(Iterator<E> i) {
		AgentSet<E> a = new AgentSet<E>();
		while (i.hasNext()) {
			a.add(i.next());
		}
		return a;
	}

	/**
	 * Returns the turtles on the patch corresponding to the patchLocation
	 * GridPoint. Throws SpatialException if location is not allowed.
	 * 
	 * @param patchLocation
	 * @return
	 */
	public static AgentSet<Turtle> getTurtlesOnGridPoint(
			GridPoint patchLocation, Observer observer) {
		Grid grid = observer.getGrid();
		Iterable objectsAt = grid.getObjectsAt(patchLocation.toIntArray(null));
		FilteredIterator turtlesAt = new FilteredIterator(objectsAt.iterator(),
				new InstanceofPredicate(Turtle.class));
		return Utility.agentSetFromIterator(turtlesAt);
	}

	/**
	 * Returns the turtles of this type on the patch corresponding to the
	 * patchLocation GridPoint. Throws SpatialException if location is not
	 * allowed.
	 * 
	 * @param patchLocation
	 * @return
	 */
	public static AgentSet<Turtle> getTurtlesOnGridPoint(
			GridPoint patchLocation, Observer observer, Class type) {
		Grid grid = observer.getGrid();
		Iterable objectsAt = grid.getObjectsAt(patchLocation.toIntArray(null));
		FilteredIterator turtlesAt = new FilteredIterator(objectsAt.iterator(),
				new InstanceofPredicate(type));
		return Utility.agentSetFromIterator(turtlesAt);
	}

	/**
	 * Returns the turtles of this typeName on the patch corresponding to the
	 * patchLocation GridPoint. Throws SpatialException if location is not
	 * allowed.
	 * 
	 * @param patchLocation
	 * @return
	 */
	public static AgentSet<Turtle> getTurtlesOnGridPoint(
			GridPoint patchLocation, Observer observer, String typeName) {
		Grid grid = observer.getGrid();
		Iterable objectsAt = grid.getObjectsAt(patchLocation.toIntArray(null));
		Class clazz = observer.getTurtleFactory().getTurtleTypeClass(typeName);

		FilteredIterator turtlesAt = new FilteredIterator(objectsAt.iterator(),
				new InstanceofPredicate(clazz));
		return Utility.agentSetFromIterator(turtlesAt);
	}

	/**
	 * Returns the Patch object at the gridPoint location. Throws
	 * SpatialException if location is not allowed.
	 * 
	 * @param gridPoint
	 * @return
	 */
	public static Patch getPatchAtLocation(GridPoint gridPoint,
			Observer observer) {
		Grid grid = observer.getGrid();
		Iterable objectsAt = grid.getObjectsAt(gridPoint.toIntArray(null));
		// not instance of filter and then I want a predicate
		FilteredIterator patchAt = new FilteredIterator(objectsAt.iterator(),
				new InstanceofPredicate(Patch.class));
		return (Patch) patchAt.next();
	}

	/**
	 * Returns the patch containing a point.
	 * 
	 * @param nX
	 *            x coordinate
	 * @param nY
	 *            y coordinate
	 * @param observer
	 *            an observer
	 * @return patch that contains the point (nX, nY)
	 */
	public static Patch patchU(double x, double y, Observer observer) {
		try {
			return getPatchAtLocation(ndPointToGridPoint(new NdPoint(x, y)),
					observer);
		} catch (SpatialException e) {
			return null;
		}
	}

	/**
	 * Returns the link between two turtles.
	 * 
	 * @param oneEnd
	 *            an integer
	 * @param otherEnd
	 *            an integer
	 * @param observer
	 *            an observer
	 * 
	 * @return link between two turtles
	 */
	public static Link linkU(int oneEnd, int otherEnd, Observer observer) {
		Link link = linkDir(oneEnd, otherEnd, observer);
		if (link != null) {
			return link;
		} else {
			return linkUndir(oneEnd, otherEnd, observer);
		}
	}

	public static Link linkDir(int oneEnd, int otherEnd, Observer observer) {
		return (Link) observer.getNetwork("DirectedLinks").getEdge(
				turtleU(oneEnd, observer), turtleU(otherEnd, observer));
	}

	public static Link linkUndir(int oneEnd, int otherEnd, Observer observer) {
		return (Link) observer.getNetwork("UndirectedLinks").getEdge(
				turtleU(oneEnd, observer), turtleU(otherEnd, observer));
	}

	/**
	 * Reports the turtle with the given who number, or null if there is no such
	 * turtle.
	 * 
	 * @param number
	 * @return
	 */
	public static Turtle turtleU(int number, Observer observer) {
		Context tpc = observer.getContext();
		Query query = new PropertyEquals(tpc, "who", number);
		Iterator result = query.query().iterator();
		if (result.hasNext()) {
			return (Turtle) result.next();
		}
		return null;
	}

	/**
	 * Returns the GridPoint object of the grid location corresponding to the
	 * displacement from the NdPoint currentLocation. Throws SpatialException if
	 * displacement is not allowed.
	 * 
	 * @param currentLocation
	 * @param displacement
	 * @return
	 */
	public static GridPoint getGridPointAtDisplacement(NdPoint currentLocation,
			double[] displacement, Observer observer) {
		ContinuousSpace space = observer.getSpace();
		// TODO: does this need to be modified to account for 3D?
		double[] dLoc = new double[2];
		space.getPointTranslator().translate(currentLocation, dLoc,
				displacement);
		NdPoint newLocation = new NdPoint(dLoc);
		return new GridPoint(Utility.ndPointToIntArray(newLocation));
	}

	/**
	 * Returns the Logo appropriate angle, i.e., between 0 to <360, given an
	 * angle in degrees.
	 * 
	 * @param number
	 * @return
	 */
	public static double getNLAngle(double angleInDegrees) {
		double tempHeading = angleInDegrees % 360;
		return (tempHeading < 0) ? tempHeading + 360 : tempHeading;
	}

	/**
	 * Returns the displacement double array corresponding to moving in the
	 * "heading" direction for distance "distance".
	 * 
	 * @param heading
	 * @param distance
	 * @return
	 */
	public static double[] getDisplacementFromHeadingAndDistance(
			double heading, double distance) {
		// TODO: does this need to be modified to account for 3D?
		double[] anglesForDisplacement = {
				(Math.PI / 2) - Math.toRadians(heading), 0.0 };
		return SpatialMath.getDisplacement(2, 0, distance,
				anglesForDisplacement);
	}

	/**
	 * Queries if object is a turtle.
	 * 
	 * @param o
	 *            an object
	 * @return true or false based on whether the object is a turtle
	 */
	public static boolean isTurtleQ(Object o) {
		return (o instanceof Turtle);
	}

	/**
	 * Queries if object is a patch.
	 * 
	 * @param o
	 *            an object
	 * @return true or false based on whether the object is a patch
	 */
	public static boolean isPatchQ(Object o) {
		return (o instanceof Patch);
	}

	/**
	 * Queries if object is a list.
	 * 
	 * @param o
	 *            an object
	 * @return true or false based on whether the object is a list
	 */
	public static boolean isListQ(Object o) {
		return (o instanceof List);
	}

	/**
	 * Queries if object is a string.
	 * 
	 * @param o
	 *            an object
	 * @return true or false based on whether the object is a string
	 */
	public static boolean isStringQ(Object o) {
		return (o instanceof String);
	}

	/**
	 * Queries if object is a directed link.
	 * 
	 * @param o
	 *            an object
	 * @return true or false based on whether the object is a directed link
	 */
	public static boolean isDirectedLinkQ(Object o) {
		if (o instanceof Link) {
			Link l = (Link) o;
			return l.isDirected();
		} else
			return false;
	}

	/**
	 * Queries if object is an undirected link.
	 * 
	 * @param o
	 *            an object
	 * @return true or false based on whether the object is an undirected link
	 */
	public static boolean isUndirectedLinkQ(Object o) {
		if (o instanceof Link) {
			Link l = (Link) o;
			return !(l.isDirected());
		} else
			return false;
	}

	/**
	 * Queries if object is a link.
	 * 
	 * @param o
	 *            an object
	 * @return true or false based on whether the object is a link
	 */
	public static boolean isLinkQ(Object o) {
		return (o instanceof Link);
	}

	/**
	 * Queries if object is an agentset of links.
	 * 
	 * @param o
	 *            an object
	 * @return true or false based on whether the object is an agentset of links
	 */
	public static boolean isLinkSetQ(Object o) {
		if (o instanceof AgentSet) {
			AgentSet a = (AgentSet) o;
			if (a.size() == 0) {
				return true;
			} else {
				if (a.get(0) instanceof Link) {
					return true;
				} else
					return false;
			}
		}
		return false;
	}

	/**
	 * Queries if object is an agentset of patches.
	 * 
	 * @param o
	 *            an object
	 * @return true or false based on whether the object is an agentset of
	 *         patches
	 */
	public static boolean isPatchSetQ(Object o) {
		if (o instanceof AgentSet) {
			AgentSet a = (AgentSet) o;
			if (a.size() == 0) {
				return true;
			} else {
				if (a.get(0) instanceof Patch) {
					return true;
				} else
					return false;
			}
		}
		return false;
	}

	/**
	 * Queries if object is an agentset of turtles.
	 * 
	 * @param o
	 *            an object
	 * @return true or false based on whether the object is an agentset of
	 *         turtles
	 */
	public static boolean isTurtleSetQ(Object o) {
		if (o instanceof AgentSet) {
			AgentSet a = (AgentSet) o;
			if (a.size() == 0) {
				return true;
			} else {
				if (a.get(0) instanceof Turtle) {
					return true;
				} else
					return false;
			}
		}
		return false;
	}

	/**
	 * Queries if object is a number.
	 * 
	 * @param o
	 *            an object
	 * @return true or false based on whether the object is a number
	 */
	public static boolean isNumberQ(Object o) {
		return (o instanceof Number);
	}

	/**
	 * Queries if object is a boolean.
	 * 
	 * @param o
	 *            an object
	 * @return true or false based on whether the object is a boolean
	 */
	public static boolean isBooleanQ(Object o) {
		return (o instanceof Boolean);
	}

	/**
	 * Queries if object is an agentset.
	 * 
	 * @param o
	 *            an object
	 * @return true or false based on whether the object is an agentset
	 */
	public static boolean isAgentSetQ(Object o) {
		return (o instanceof AgentSet);
	}

	/**
	 * Queries if object is an agent.
	 * 
	 * @param o
	 *            an object
	 * @return true or false based on whether the object is an agent
	 */
	public static boolean isAgentQ(Object o) {
		return ((o instanceof ReLogoAgent) || (o instanceof Observer));
	}

	/**
	 * Queries if there exist any agents in an agentset.
	 * 
	 * @param a
	 *            an agentset
	 * @return true or false based on whether there exist any agents in a
	 */
	public static boolean anyQ(Collection a) {
		return (!a.isEmpty());
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
	public static boolean allQU(Collection<? extends ReLogoAgent> a, Closure closure) {
		return allQU(null, a, closure);
	}

	public static boolean allQU(Object caller, Collection<? extends ReLogoAgent> a,
			Closure closure) {
		if (caller != null) {
			for (ReLogoAgent o : a) {
				o.setMyself(caller);
			}
		}
		closure.setResolveStrategy(Closure.DELEGATE_FIRST);
		for (ReLogoAgent agent : a) {
			closure.setDelegate(agent);
			if (!(Boolean) closure.call(agent)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns an empty agentset (of turtles).
	 * 
	 * @return empty turtle agentset
	 */
	public static AgentSet<Turtle> noTurtles() {
		return new AgentSet<Turtle>();
	}

	/**
	 * Returns an empty agentset (of patches).
	 * 
	 * @return empty patch agentset
	 */
	public static AgentSet<Patch> noPatches() {
		return new AgentSet<Patch>();
	}

	/**
	 * Returns an empty agentset (of links).
	 * 
	 * @return empty link agentset
	 */
	public static AgentSet<Link> noLinks() {
		return new AgentSet<Link>();
	}

	/**
	 * Returns the difference of two headings.
	 * 
	 * @param to
	 *            an angle in degrees
	 * @param from
	 *            an angle in degrees
	 * @return difference of two headings in the range [-180, 180]
	 */
	public static double subtractHeadings(double to, double from) {
		double temp = (to - from) % 360;
		temp = temp < 0 ? temp + 360 : temp;
		return (temp > 180 ? temp - 360 : temp);
	}

	/**
	 * Returns the first item in a list.
	 * 
	 * @params a a list
	 * @return first item in list a
	 */
	public static <E> E first(List<E> a) {
		if (a.size() > 0) {
			return a.get(0);
		} else {
			return null;
		}
	}

	/**
	 * Returns the first character in a string.
	 * 
	 * @params s a string
	 * @return first character in string s
	 */
	public static String first(String s) {
		if (s.length() > 0) {
			return s.substring(0, 1);
		} else {
			return null;
		}
	}

	/**
	 * Appends item to the front of a list then returns the new list.
	 * 
	 * @param item
	 *            an object
	 * @param list
	 *            a list
	 * @return a list with item appended to the front of list
	 */
	public static ArrayList fput(Object item, List list) {
		ArrayList resultList = new ArrayList(list);
		resultList.add(0, item);
		return resultList;
	}

	/**
	 * Appends item to the end of a list then returns the new list.
	 * 
	 * @param item
	 *            an object
	 * @param list
	 *            a list
	 * @return list with item appended to the end
	 */
	public static ArrayList lput(Object item, List list) {
		ArrayList resultList = new ArrayList(list);
		resultList.add(item);
		return resultList;
	}

	/**
	 * Returns item i in a list.
	 * 
	 * @param i
	 *            an integer
	 * @param a
	 *            a list
	 * @return item i in list a
	 */
	public static <E> E item(int i, List<E> a) {
		return a.get(i);
	}

	/**
	 * Returns character i in a string.
	 * 
	 * @param i
	 *            an integer
	 * @param s
	 *            a string
	 * @return character i in string s
	 */
	public static String item(int i, String s) {
		return s.substring(i, i + 1);
	}

	/**
	 * Returns the last item in a list.
	 * 
	 * @param a
	 *            a list
	 * @return last item in list a
	 */
	public static <E> E last(List<E> a) {
		int len = a.size();
		if (len > 0) {
			return a.get(len - 1);
		} else {
			return null;
		}
	}

	/**
	 * Returns the last character in a string.
	 * 
	 * @param s
	 *            a string
	 * @return last character in string s
	 */
	public static String last(String s) {
		int len = s.length();
		if (len > 0) {
			return s.substring(len - 1, len);
		} else {
			return null;
		}
	}

	/**
	 * Returns the number of items in a collection.
	 * 
	 * @param c a collection
	 *            
	 * @return number of items in collection c
	 */
	public static int length(Collection c) {
		return c.size();
	}

	/**
	 * Returns the number of characters in a string.
	 * 
	 * @param s
	 *            a string
	 * @return number of characters in string s
	 */
	public static int length(String s) {
		return s.length();
	}

	/**
	 * Returns a list randomly reordered.
	 * 
	 * @param a
	 *            a list
	 * @return list randomly reordered
	 */
	public static <E> ArrayList<E> shuffle(List<E> a) {
		ArrayList<E> result = new ArrayList<E>(a);
		SimUtilities.shuffle(result, RandomHelper.getUniform());
		return result;
	}

	/**
	 * Queries if a string is empty.
	 * 
	 * @param string
	 *            a string
	 * @return true or false based on whether the string is empty
	 */
	public static boolean emptyQ(String string) {
		return (string.equals(""));
	}

	/**
	 * Queries if a list is empty.
	 * 
	 * @param list
	 *            a list
	 * @return true or false based on whether the list is empty
	 */
	public static boolean emptyQ(Collection c) {
		return (c.isEmpty());
	}

	/**
	 * Returns the portion of a string from one position to before another
	 * position.
	 * 
	 * @param string
	 *            a string
	 * @param position1
	 *            a zero-based index
	 * @param position2
	 *            a zero-based index
	 * @return portion of string from position1 to before position2
	 */
	public static String substring(String string, int position1, int position2) {
		return string.substring(position1, position2);
	}

	/**
	 * Returns the arc cosine of a number.
	 * 
	 * @param a
	 *            a number in the range [-1, 1]
	 * @return angle in degrees in the range [0, 180]
	 */
	public static double acos(Number a) {
		return Math.toDegrees(Math.acos(a.doubleValue()));
	}

	/**
	 * Returns the arc sine of a number.
	 * 
	 * @param a
	 *            a number in the range [-1, 1]
	 * @return angle in degrees in the range [-90, 90]
	 */
	public static double asin(Number a) {
		return Math.toDegrees(Math.asin(a.doubleValue()));
	}

	/**
	 * Changes x,y offsets to degrees.
	 * 
	 * @param x
	 *            a number
	 * @param y
	 *            a number
	 * @return angle in degrees in the range [0, 360]
	 */
	public static double atan(Number x, Number y) {
		return angleFromDisplacement(x.doubleValue(), y.doubleValue());
	}

	/**
	 * Returns smallest integer greater than or equal to a number.
	 * 
	 * @param num
	 *            a number
	 * @return smallest integer greater than or equal to num
	 */
	public static double ceiling(Number num) {
		return Math.ceil(num.doubleValue());
	}

	/**
	 * Returns largest integer less than or equal to a number.
	 * 
	 * @param num
	 *            a number
	 * @return largest integer less than or equal to num
	 */
	public static double floor(Number num) {
		return Math.floor(num.doubleValue());
	}

	/**
	 * Returns the sine of an angle.
	 * 
	 * @param aDeg
	 *            an angle in degrees
	 * @return sine of aDeg
	 */
	public static double sin(Number aDeg) {
		return Math.sin(Math.toRadians(aDeg.doubleValue()));
	}

	/**
	 * Returns the cosine of an angle.
	 * 
	 * @param num
	 *            an angle in degrees
	 * @return cosine of num
	 */
	public static double cos(Number num) {
		return Math.cos(Math.toRadians(num.doubleValue()));
	}

	/**
	 * Returns the tangent of an angle.
	 * 
	 * @param num
	 *            an angle in degrees
	 * @return tan of num
	 */
	public static double tan(Number num) {
		return Math.tan(Math.toRadians(num.doubleValue()));
	}

	/**
	 * Returns the exponential.
	 * 
	 * @returns the exponential
	 */
	public static double e() {
		return Math.E;
	}

	/**
	 * Returns the number for pi.
	 * 
	 * @returns number for pi.
	 */
	public static double pi() {
		return Math.PI;
	}

	/**
	 * Returns the integer portion of a number.
	 * 
	 * @param num
	 *            a number
	 * @return integer portion of a number
	 */
	public static int intPart(Number num) {
		return num.intValue();
	}

	/**
	 * Returns the natural logarithm of a number
	 * 
	 * @param num
	 *            a number
	 * @return natural logarithm of num
	 */
	public static double ln(Number num) {
		return Math.log(num.doubleValue());
	}

	/**
	 * Returns the logarithm of a number.
	 * 
	 * @param num
	 *            a number
	 * @param base
	 *            a number
	 * @return logarithm of num in base base
	 */
	public static double log(Number num, Number base) {
		return Math.log(num.doubleValue()) / Math.log(base.doubleValue());
	}

	/**
	 * The modulus operator.
	 * 
	 * @param number1
	 *            a number
	 * @param number2
	 *            a number
	 * @return number number1 modulo number number2
	 */
	public static double mod(Number number1, Number number2) {
		return (number1.doubleValue() - (Math.floor(number1.doubleValue() / number2.doubleValue())) * number2.doubleValue());
	}

	/**
	 * Returns a number to seed the random number generator.
	 * 
	 * @return number to seed the random number generator
	 */
	public static int newSeed() {
		return RandomHelper.getUniform().nextIntFromTo(Integer.MIN_VALUE,
				Integer.MAX_VALUE);
	}

	/**
	 * Returns a number with precision to a given decimal place.
	 * 
	 * @param b
	 *            a number
	 * @param places
	 *            an integer
	 * @return number b with precision to places
	 */
	public static BigDecimal precision(BigDecimal b, int places) {
		return (b.setScale(places, BigDecimal.ROUND_HALF_UP));
	}

	/**
	 * Returns a random floating point number (exponentially distributed).
	 * 
	 * @param mean
	 *            a number
	 * @return random floating point number (exponentially distributed with mean
	 *         mean)
	 */
	public static double randomExponential(Number mean) {
		return RandomHelper.createExponential(mean.doubleValue()).nextDouble();
	}

	/**
	 * Returns a random floating point number (gamma distributed).
	 * 
	 * @param alpha
	 *            a number
	 * @param lambda
	 *            a number
	 * @return random floating point number (gamma distributed with parameters
	 *         alpha and lambda)
	 */
	public static double randomGamma(Number alpha, Number lambda) {
		return RandomHelper.createGamma(alpha.doubleValue(), lambda.doubleValue()).nextDouble();
	}

	/**
	 * Returns a random floating point number (normally distributed).
	 * 
	 * @param mean
	 *            a number
	 * @param standardDeviation
	 *            a number
	 * @return random floating point number (normally distributed with mean mean
	 *         and standard deviation standardDeviation)
	 */
	public static double randomNormal(Number mean, Number standardDeviation) {
		return RandomHelper.createNormal(mean.doubleValue(), standardDeviation.doubleValue()).nextDouble();
	}

	/**
	 * Returns a random integer (poisson distributed).
	 * 
	 * @param mean
	 *            a number
	 * @return random integer (poisson distributed with mean mean)
	 */
	public static int randomPoisson(Number mean) {
		return RandomHelper.createPoisson(mean.doubleValue()).nextInt();
	}

	/**
	 * Sets the random seed.
	 * 
	 * @param seed
	 *            an integer
	 */
	public static void randomSeed(int seed) {
		RandomHelper.setSeed(seed);
	}

	/**
	 * Returns the remainder of one number divided by another number.
	 * 
	 * @param number1
	 *            a number
	 * @param number2
	 *            a number
	 * @return remainder of number1 divided by number2
	 */
	public static double remainder(Number number1, Number number2) {
		return (number1.doubleValue() - (intPart(number1.doubleValue() / number2.doubleValue()) * number2.doubleValue()));
	}

	/**
	 * Returns the closest integer to the number.
	 * 
	 * @param number
	 *            a number
	 * @return closest integer to number
	 */
	public static int round(Number number) {
		return (int) Math.round(number.doubleValue());
	}

	/**
	 * The agent waits timeInSecs number of seconds
	 * 
	 * @param timeInSecs
	 *            a number
	 */
	public static void wait(Number timeInSecs) {
		Double timeInMillisecs = timeInSecs.doubleValue() * 1000;
		try {
			java.lang.Thread.sleep(timeInMillisecs.longValue());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// Plot Utility methods

	/**
	 * Does nothing, included for translation compatibility.
	 */
	public static void setCurrentPlot(String string) {
		// Model.getInstance().getPlot().setCurrentPlot(string);
	}

	/**
	 * Does nothing, included for translation compatibility.
	 */
	public static void setCurrentPlotPen(String string) {
		// Model.getInstance().getPlot().setCurrentPlotPen(string);
	}

	/**
	 * Does nothing, included for translation compatibility.
	 */
	public static void plot(Number point) {
		// Model.getInstance().getPlot().plot(point);
	}

	/**
	 * Does nothing, included for translation compatibility.
	 */
	public static void setPlotYRange(Number left, Number right) {
		// Model.getInstance().getPlot().setPlotYRange(left, right);
	}

	/**
	 * Does nothing, included for translation compatibility.
	 */
	public static void setPlotXRange(Number left, Number right) {
		// Model.getInstance().getPlot().setPlotYRange(left, right);
	}

	/**
	 * Does nothing, included for translation compatibility.
	 */
	public static boolean autoplotQ() {
		return false;
	}

	/**
	 * Does nothing, included for translation compatibility.
	 */
	public static void autoPlotOff() {
	}

	/**
	 * Does nothing, included for translation compatibility.
	 */
	public static void autoPlotOn() {
	}

	/**
	 * Does nothing, included for translation compatibility.
	 */
	public static void clearAllPlots() {
	}

	/**
	 * Does nothing, included for translation compatibility.
	 */
	public static void clearPlot() {
	}

	/**
	 * Does nothing, included for translation compatibility.
	 */
	public static void createTemporaryPlotPen() {
	}

	/**
	 * Does nothing, included for translation compatibility.
	 */
	public static void histogram() {
	}

	/**
	 * Does nothing, included for translation compatibility.
	 */
	public static String plotName() {
		return "plotName";
	}

	/**
	 * Does nothing, included for translation compatibility.
	 */
	public static boolean plotPenExistsQ() {
		return false;
	}

	/**
	 * Does nothing, included for translation compatibility.
	 */
	public static void plotPenDown() {
	}

	/**
	 * Does nothing, included for translation compatibility.
	 */
	public static void plotPenReset() {
	}

	/**
	 * Does nothing, included for translation compatibility.
	 */
	public static void plotPenUp() {
	}

	/**
	 * Does nothing, included for translation compatibility.
	 */
	public static double plotXMax() {
		return 0;
	}

	/**
	 * Does nothing, included for translation compatibility.
	 */
	public static double plotXMin() {
		return 0;
	}

	/**
	 * Does nothing, included for translation compatibility.
	 */
	public static double plotYMax() {
		return 0;
	}

	/**
	 * Does nothing, included for translation compatibility.
	 */
	public static double plotYMin() {
		return 0;
	}

	/**
	 * Does nothing, included for translation compatibility.
	 */
	public static void plotxy(Number num1, Number num2) {
	}

	/**
	 * Does nothing, included for translation compatibility.
	 */
	public static void setHistogramNumBars(Number num) {
	}

	/**
	 * Does nothing, included for translation compatibility.
	 */
	public static void setPlotPenColor(Number color) {
	}

	/**
	 * Does nothing, included for translation compatibility.
	 */
	public static void setPlotPenInterval(Number color) {
	}

	/**
	 * Does nothing, included for translation compatibility.
	 */
	public static void setPlotPenMode(Number color) {
	}

	/**
	 * Does nothing, included for translation compatibility.
	 */
	public static void display() {
	}

	public static AgentSet<Turtle> getTurtleAgentSetOfType(String typeName,
			Observer observer) {
		Class clazz = observer.getTurtleFactory().getTurtleTypeClass(typeName);
		return getAgentSetOfClass(clazz, observer);
	}

	public static AgentSet<Turtle> turtlesU(Observer observer) {
		return getAgentSetOfClass(Turtle.class, observer);
	}

	public static AgentSet<Patch> patchesU(Observer observer) {
		return getAgentSetOfClass(Patch.class, observer);
	}

	/**
	 * Returns the agentset of all links.
	 * 
	 * @param observer
	 *            an observer
	 * @return agentset of all links
	 */
	public static AgentSet<Link> allLinksU(Observer observer) {
		return getAgentSetOfClass(Link.class, observer);
	}
	
	/**
	 * Returns the agentset of all generic links.
	 * 
	 * @param observer
	 *            an observer
	 * @return agentset of all links
	 */
	public static AgentSet<Link> linksU(Observer observer) {
		AgentSet a = agentSetFromIterable(observer.getNetwork("UndirectedLinks").getEdges());
		AgentSet d = agentSetFromIterable(observer.getNetwork("DirectedLinks").getEdges());
		a.addAll(d);
		return a;
	}

	public static AgentSet getAgentSetOfClass(Class E, Observer observer) {
		Context tpc = observer.getContext();
		AgentSet a = new AgentSet();
		for (Object e : tpc.getObjects(E)) {
			a.add(e);
		}
		return a;
	}

	public static ArrayList getWayPoints(Observer observer) {
		Context tpc = observer.getContext();
		ArrayList a = new ArrayList();
		for (Object w : tpc.getObjects(WayPoint.class)) {
			a.add(w);
		}
		return a;
	}

	public static AgentSet combineAgentSets(AgentSet a1, AgentSet a2) {
		FastSet s = new FastSet();
		s.addAll(a1);
		s.addAll(a2);
		return new AgentSet(s);
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
	 * @exclude
	 */
	public static ReLogoAgent minOneOfU(Collection<? extends ReLogoAgent> a,
			Closure closure) {
		return minOneOfU(null, a, closure);
	}

	public static ReLogoAgent minOneOfU(Object caller,
			Collection<? extends ReLogoAgent> a, Closure closure) {
		List<ReLogoAgent> b = new ArrayList(a);
		if (b.size() == 0){
			return null;
		}
		if (b.size() > 0 && !(b.get(0) instanceof ReLogoAgent) ){
			System.err.println("minOneOf expects a collection of ReLogoAgents.");
			return null;
		}
		SimUtilities.shuffle(b, RandomHelper.getUniform());
		if (caller != null) {
			for (ReLogoAgent o : b) {
				o.setMyself(caller);
			}
		}
		closure.setResolveStrategy(Closure.DELEGATE_FIRST);
		double currentMin = Double.POSITIVE_INFINITY;
		ReLogoAgent currentMinAgent = null;
		for (ReLogoAgent o : b) {
			closure.setDelegate(o);
			Number result = (Number) closure.call(o);
			if (result.doubleValue() < currentMin) {
				currentMin = result.doubleValue();
				currentMinAgent = o;
			}
		}
		return currentMinAgent;
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
	 * @exclude
	 */
	public static AgentSet minNOfU(int number, Collection<? extends ReLogoAgent> a,
			Closure closure) {
		return minNOfU(null, number, a, closure);
	}

	public static AgentSet minNOfU(Object caller, int number,
			Collection<? extends ReLogoAgent> a, final Closure closure) {
		List<ReLogoAgent> b = new ArrayList(a);
		int l = b.size();
		if (l == 0){
			return new AgentSet();
		}
		if (l > 0 && !(b.get(0) instanceof ReLogoAgent) ){
			System.err.println("minNOf expects a collection of ReLogoAgents.");
			return new AgentSet();
		}
		SimUtilities.shuffle(b, RandomHelper.getUniform());
		if (caller != null) {
			for (ReLogoAgent o : b) {
				o.setMyself(caller);
			}
		}
		closure.setResolveStrategy(Closure.DELEGATE_FIRST);
		Collections.sort(b, new Comparator() {
			public int compare(Object o1, Object o2) {
				closure.setDelegate(o1);
				Number n1 = (Number) closure.call(o1);
				closure.setDelegate(o2);
				Number n2 = (Number) closure.call(o2);
				return (n1.doubleValue() < n2.doubleValue() ? -1 : (n1
						.doubleValue() == n2.doubleValue() ? 0 : 1));
			}
		});
		AgentSet<ReLogoAgent> result = new AgentSet();
		for (int i = 0; i < l && i < number; i++) {
			result.add(b.get(i));
		}
		return result;
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
	 * @exclude
	 */
	public static ReLogoAgent maxOneOfU(Collection<? extends ReLogoAgent> a,
			Closure closure) {
		return maxOneOfU(null, a, closure);
	}

	public static ReLogoAgent maxOneOfU(Object caller,
			Collection<? extends ReLogoAgent> a, Closure closure) {
		List<ReLogoAgent> b = new ArrayList(a);
		if (b.size() == 0){
			return null;
		}
		if (b.size() > 0 && !(b.get(0) instanceof ReLogoAgent) ){
			System.err.println("maxOneOf expects a collection of ReLogoAgents.");
			return null;
		}
		SimUtilities.shuffle(b, RandomHelper.getUniform());
		if (caller != null) {
			for (ReLogoAgent o : b) {
				o.setMyself(caller);
			}
		}
		closure.setResolveStrategy(Closure.DELEGATE_FIRST);
		double currentMax = Double.NEGATIVE_INFINITY;
		ReLogoAgent currentMaxAgent = null;
		for (ReLogoAgent o : b) {
			closure.setDelegate(o);
			Number result = (Number) closure.call(o);
			if (result.doubleValue() > currentMax) {
				currentMax = result.doubleValue();
				currentMaxAgent = o;
			}
		}
		return currentMaxAgent;
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
	 * @exclude
	 */
	public static AgentSet maxNOfU(int number, Collection<? extends ReLogoAgent> a,
			Closure closure) {
		return maxNOfU(null, number, a, closure);
	}

	public static AgentSet maxNOfU(Object caller, int number,
			Collection<? extends ReLogoAgent> c, final Closure closure) {
		List<ReLogoAgent> b = new ArrayList(c);
		int l = b.size();
		if (l == 0){
			return new AgentSet();
		}
		if (l > 0 && !(b.get(0) instanceof ReLogoAgent) ){
			System.err.println("maxNOf expects a collection of ReLogoAgents.");
			return new AgentSet();
		}
		SimUtilities.shuffle(b, RandomHelper.getUniform());
		if (caller != null) {
			for (ReLogoAgent o : b) {
				o.setMyself(caller);
			}
		}
		closure.setResolveStrategy(Closure.DELEGATE_FIRST);
		Collections.sort(b, new Comparator() {
			public int compare(Object o1, Object o2) {
				closure.setDelegate(o1);
				Number n1 = (Number) closure.call(o1);
				closure.setDelegate(o2);
				Number n2 = (Number) closure.call(o2);
				return (n1.doubleValue() < n2.doubleValue() ? 1 : (n1
						.doubleValue() == n2.doubleValue() ? 0 : -1));
			}
		});
		AgentSet<ReLogoAgent> result = new AgentSet();
		for (int i = 0; i < l && i < number; i++) {
			result.add(b.get(i));
		}
		return result;
	}

	public static AgentSet removeLinkFromAgentSet(AgentSet a, Link l) {
		Predicate pr = NotPredicate.getInstance(EqualPredicate.getInstance(l));
		FilteredIterator result = new FilteredIterator(a.iterator(), pr);
		AgentSet b = new AgentSet();
		while (result.hasNext()) {
			b.add(result.next());
		}
		return b;
	}

	public static AgentSet removeLinkFromCollection(Collection a, Link l) {
		while(a.remove(l));
		return new AgentSet(a);
	}

	public static AgentSet removePatchFromAgentSet(AgentSet a, Patch p) {
		Predicate pr = NotPredicate.getInstance(EqualPredicate.getInstance(p));
		FilteredIterator result = new FilteredIterator(a.iterator(), pr);
		AgentSet b = new AgentSet();
		while (result.hasNext()) {
			b.add(result.next());
		}
		return b;
	}

	public static AgentSet removePatchFromCollection(Collection a, Patch p) {
		while(a.remove(p));
		return new AgentSet(a);
	}

	public static AgentSet removeTurtleFromAgentSet(AgentSet a, Turtle t) {
		Predicate pr = NotPredicate.getInstance(EqualPredicate.getInstance(t));
		FilteredIterator result = new FilteredIterator(a.iterator(), pr);
		AgentSet b = new AgentSet();
		while (result.hasNext()) {
			b.add(result.next());
		}
		return b;
	}

	public static AgentSet removeTurtleFromCollection(Collection a, Turtle t) {
		while(a.remove(t));
		return new AgentSet(a);
	}

	/**
	 * Collects arguments into an agenset of turtles.
	 * 
	 * @param args
	 *            turtles, set of turtles, agentset of turtles
	 * @return agentset of turtles
	 */
	public static AgentSet<Turtle> turtleSet(Object... args) {
		Set tempSet = new HashSet();

		for (Object o : args) {
			if (o == null) {
			} else if (o instanceof AgentSet) {
				tempSet.addAll((AgentSet) o);
			} else if (o instanceof Turtle) {
				tempSet.add((Turtle) o);
			} else if (o instanceof Collection) {
				tempSet.addAll(flatten((Collection) o));
			}
		}
		return new AgentSet(tempSet);
	}

	public static Collection flatten(Collection c) {
		return UtilityG.gflatten(c);
	}

	/**
	 * Collects arguments into an agenset of patches.
	 * 
	 * @param args
	 *            patches, set of patches, agentset of patches
	 * @return agentset of patches
	 */
	public static AgentSet<Patch> patchSet(Object... args) {
		Set tempSet = new HashSet();

		for (Object o : args) {
			if (o == null) {
			} else if (o instanceof AgentSet) {
				tempSet.addAll((AgentSet) o);
			} else if (o instanceof Patch) {
				tempSet.add((Patch) o);
			} else if (o instanceof Collection) {
				tempSet.addAll(flatten((Collection) o));
			}
		}
		return new AgentSet(tempSet);
	}

	/**
	 * Collects arguments into an agenset of links.
	 * 
	 * @param args
	 *            link, set of links, agentset of links
	 * @return agentset of links
	 */
	public static AgentSet<Link> linkSet(Object... args) {
		Set tempSet = new HashSet();

		for (Object o : args) {
			if (o == null) {
			} else if (o instanceof AgentSet) {
				tempSet.addAll((AgentSet) o);
			} else if (o instanceof Patch) {
				tempSet.add((Link) o);
			} else if (o instanceof Collection) {
				tempSet.addAll(flatten((Collection) o));
			}
		}
		return new AgentSet(tempSet);
	}

	/**
	 * Returns a list minus first item.
	 * 
	 * @param a
	 *            a list
	 * @return list minus first list item
	 */
	public static <E> List<E> butFirst(List<E> a) {

		if (a.size() > 1) {
			ArrayList<E> result = new ArrayList<E>(a);
			result.subList(0, 1).clear();
			return result;
		} else {
			return new ArrayList<E>();
		}
	}

	/**
	 * Returns a string minus first character.
	 * 
	 * @param s
	 *            a string of characters
	 * @return string minus first character
	 */
	public static String butFirst(String s) {
		int length = s.length();
		if (length > 1) {
			return s.substring(1);
		} else {
			return "";
		}
	}

	/**
	 * Returns a list minus last item.
	 * 
	 * @param a
	 *            a list
	 * @return list minus last list item
	 */
	public static <E> List<E> butLast(List<E> a) {
		int size = a.size();
		if (size > 1) {
			ArrayList<E> result = new ArrayList<E>(a);
			result.subList(size - 1, size).clear();
			return result;
		} else {
			return new ArrayList<E>();
		}
	}

	/**
	 * Returns a string minus last character.
	 * 
	 * @param s
	 *            a string of characters
	 * @return string minus last character
	 */
	public static String butLast(String s) {
		int length = s.length();
		if (length > 1) {
			return s.substring(0, length - 1);
		} else {
			return "";
		}
	}

	/**
	 * Queries if a value is in a collection.
	 * 
	 * @param value
	 *            an object
	 * @param c
	 *            a collection
	 * @return true or false based on whether object value is in collection c
	 */
	public static boolean memberQ(Object value, Collection c) {
		return c.contains(value);
	}

	/**
	 * Queries if a string is within another string.
	 * 
	 * @param string1
	 *            a string
	 * @param string2
	 *            a string
	 * @return true or false based on whether string string1 is within string
	 *         string2
	 */
	public static boolean memberQ(String string1, String string2) {
		return string2.contains(string1);
	}

	/**
	 * Returns a random subset of an agentset of size number.
	 * 
	 * @param number
	 *            an integer
	 * @param a
	 *            an agentset
	 * @return random subset of agentset a of size number
	 */
	public static <E extends ReLogoAgent> AgentSet<E> nOf(int number, AgentSet<E> a) {
		int size = a.size();
		if (size <= number) {
			return a;
		}
		AgentSet<E> b = new AgentSet<E>(a);
		SimUtilities.shuffle(b, RandomHelper.getUniform());
		b.subList(number, size).clear();
		return b;
	}

	/**
	 * Returns a random subset of a collection of size number.
	 * 
	 * @param number
	 *            an integer
	 * @param c
	 *            a collection
	 * @return random subset of collection c of size number
	 */
	public static <E> List<E> nOf(int number, Collection<E> c) {
		List<E> a = new ArrayList<E>(c);
		int size = a.size();
		if (size <= number) {
			return a;
		}
		ArrayList<Integer> indices = new ArrayList<Integer>();
		for (int i = 0; i < number; i++) {
			indices.add(i);
		}
		SimUtilities.shuffle(indices, RandomHelper.getUniform());
		indices.subList(number, size).clear();
		Collections.sort(indices);
		ArrayList<E> result = new ArrayList<E>();
		for (Integer o : indices) {
			result.add(a.get(o));
		}
		return result;
	}

	/**
	 * Returns a list of length number from running the set of commands number
	 * times.
	 * 
	 * @param number
	 *            an integer
	 * @param closure
	 *            a set of commands
	 * @return list of length number from running the set of commands number
	 *         times
	 */
	public static List nValues(int number, Closure closure) {
		ArrayList temp = new ArrayList();
		for (int i = 0; i < number; i++) {
			temp.add(closure.call(i));
		}
		return temp;
	}

	/**
	 * Returns the position of the first occurrence of an item in a list.
	 * 
	 * @param item
	 *            an object
	 * @param list
	 *            a list
	 * @return position of the first occurrence of item in list
	 */
	public static Object position(Object item, List list) {
		int index = list.indexOf(item);
		return index == -1 ? false : index;
	}

	/**
	 * Returns the position of the first occurrence of a string within another
	 * string.
	 * 
	 * @param string1
	 *            a string
	 * @param string2
	 *            a string
	 * @return position of the first occurrence of string1 within string2
	 */
	public static Object position(String string1, String string2) {
		int index = string2.indexOf(string1);
		return index == -1 ? false : index;
	}


	/**
	 * Returns a random item from a list.
	 * 
	 * @param a
	 *            a list
	 * @return random item from list a
	 */
	public static <X> X oneOf(Collection<X> c) {
		if (c.size() == 0) {
			return null;
		}
		int index = RandomHelper.nextIntFromTo(0, c.size() - 1);
		return new ArrayList<X>(c).get(index);
	}

	/**
	 * Returns a list minus an item.
	 * 
	 * @param item
	 *            an object
	 * @param list
	 *            a list
	 * @return list minus item
	 */
	public static <E> Collection<E> remove(Object item, Collection<E> c) {
		while(c.remove(item));
		return c;
	}

	/**
	 * Returns a string minus a substring.
	 * 
	 * @param string1
	 *            a string
	 * @param string2
	 *            a string
	 * @return string2 minus the substring string1
	 */
	public static String remove(String string1, String string2) {
		return string2.replace(string1, "");
	}

	/**
	 * Returns a list minus duplicated items.
	 * 
	 * @param list
	 *            a list
	 * @return list minus duplicated items
	 */
	public static <E> List<E> removeDuplicates(List<E> list) {
		Set<E> set = new HashSet<E>();
		ArrayList<E> newList = new ArrayList<E>();
		for (E o : list) {
			if (set.add(o)) {
				newList.add(o);
			}
		}
		return newList;
	}

	/**
	 * Returns a list minus the index item.
	 * 
	 * @param index
	 *            an integer
	 * @param list
	 *            a list
	 * @return list minus the index item
	 */
	public static <E> List<E> removeItem(int index, List<E> list) {
		ArrayList<E> temp = new ArrayList<E>(list);
		temp.remove(index);
		return temp;
	}

	/**
	 * Returns a string minus the character at the index.
	 * 
	 * @param index
	 *            an integer
	 * @param string
	 *            a string
	 * @return string minus the character at the index
	 */
	public static String removeItem(int index, String string) {
		int length = string.length();
		if (index >= length) {
			return string;
		}
		String one = string.substring(0, index);
		if (index == length - 1) {
			return one;
		}
		String two = string.substring(index + 1);
		return one.concat(two);
	}

	/**
	 * Returns a list with the item at location index replaced with a value.
	 * 
	 * @param index
	 *            an integer
	 * @param list
	 *            a list
	 * @param value
	 *            an object
	 * @return list with the item at location index replaced with value
	 */
	public static ArrayList replaceItem(int index, List list, Object value) {
		ArrayList temp = new ArrayList(list);
		temp.set(index, value);
		return temp;
	}

	/**
	 * Returns a string with the substring at a specified location replaced with
	 * another string.
	 * 
	 * @param index
	 *            an integer
	 * @param string1
	 *            a string
	 * @param string2
	 *            a string
	 * @return string with string1 at location index replaced with string2
	 */
	public static String replaceItem(int index, String string1, String string2) {
		int length = string1.length();
		if (index >= length) {
			return string1;
		}
		StringBuffer sb = new StringBuffer();
		sb.append(string1.substring(0, index));
		sb.append(string2);
		if (index == length - 1) {
			return sb.toString();
		}
		sb.append(string1.substring(index + 1));
		return sb.toString();
	}

	/**
	 * Returns a list in reversed order.
	 * 
	 * @param list
	 *            a list
	 * @return list in reversed order
	 */
	public static <E> List<E> reverse(List<E> list) {
		ArrayList<E> result = new ArrayList<E>(list);
		Collections.reverse(result);
		return result;
	}

	/**
	 * Returns a string in reversed order.
	 * 
	 * @param string
	 *            a string
	 * @return string in reversed order
	 */
	public static String reverse(String string) {
		StringBuffer sb = new StringBuffer();
		int length = string.length();
		for (int i = (length - 1); i >= 0; i--) {
			sb.append(string.charAt(i));
		}
		return sb.toString();
	}

	/**
	 * Creates a list from the objects.
	 * 
	 * @param args
	 *            any number of items
	 * @return list created from the args
	 */
	public static List sentence(Object... args) {
		List result = new ArrayList();
		for (Object o : args) {
			if (o instanceof Collection) {
				result.addAll((Collection) o);
			} else {
				result.add(o);
			}
		}
		return result;
	}

	/**
	 * Returns a list that has been sorted by ascending order.
	 * 
	 * @param a
	 *            list
	 * @return list that has been sorted by ascending order
	 */
	public static List sort(List a) {
		int size = a.size();
		if (size <= 1) {
			return a;
		}
		List result = new ArrayList(a);
		Object firstItem = a.get(0);
		if (firstItem instanceof Number || firstItem instanceof String) {
			Collections.sort(result);
			return result;
		} else {
			return a;
		}
	}

	/**
	 * Returns an agentset that has been sorted by the natural ordering of the
	 * agents.
	 * 
	 * @param a
	 *            agentset
	 * @return agentset that has been sorted by the natural ordering of the
	 *         agents
	 */
	public static List sort(AgentSet a) {
		List temp = new ArrayList(a);
		Collections.sort(temp);
		return temp;
	}

	/**
	 * Returns the portion of a list from one position to before another
	 * position.
	 * 
	 * @param a
	 *            a list
	 * @param position1
	 *            a zero-based index
	 * @param position2
	 *            a zero-based index
	 * @return portion of a from position1 to before position2
	 */
	public static <E> List<E> sublist(List<E> a, int position1, int position2) {
		return new ArrayList<E>(a.subList(position1, position2));
	}

	/**
	 * Returns a string created by combining objects.
	 * 
	 * @param args
	 *            an object
	 * @return string created by combining args
	 */
	public static String word(Object... args) {
		StringBuffer sb = new StringBuffer();
		for (Object o : args) {
			sb.append(o.toString());
		}
		return sb.toString();
	}

	/**
	 * Returns the largest number in a collection.
	 * 
	 * @param c
	 *            a collection
	 * @return largest number in collection
	 */
	public static Number max(Collection c) {
		return Collections.max(c, new Comparator() {
			public int compare(Object o1, Object o2) {
				if (!(o1 instanceof Number && o2 instanceof Number)) {
					return 0;
				}
				if (o1 instanceof Number) {
					if (!(o2 instanceof Number)) {
						return 1;
					} else {
						return (((Number) o1).doubleValue() < ((Number) o2)
								.doubleValue() ? -1
								: (((Number) o1).doubleValue() == ((Number) o2)
										.doubleValue() ? 0 : 1));
					}
				}
				return -1;
			}
		});
	}

	/**
	 * Returns the smallest number in a collection.
	 * 
	 * @param c
	 *            a collection
	 * @return smallest number in collection
	 */
	public static Number min(Collection c) {
		return Collections.min(c, new Comparator() {
			public int compare(Object o1, Object o2) {
				if (!(o1 instanceof Number && o2 instanceof Number)) {
					return 0;
				}
				if (o1 instanceof Number) {
					if (!(o2 instanceof Number)) {
						return -1;
					} else {
						return (((Number) o1).doubleValue() < ((Number) o2)
								.doubleValue() ? -1
								: (((Number) o1).doubleValue() == ((Number) o2)
										.doubleValue() ? 0 : 1));
					}
				}
				return 1;
			}
		});
	}

	/**
	 * Returns the mean of a collection of numbers.
	 * 
	 * @param a
	 *            a collection of numbers
	 * @return mean of collection a
	 */
	public static double mean(Collection a) {
		if (a.size() == 0) {
			return 0;
		}
		int numericalMembers = 0;
		double sum = 0.0;
		for (Object o : a) {
			if (o instanceof Number) {
				numericalMembers++;
				sum += ((Number) o).doubleValue();
			}
		}
		if (numericalMembers == 0) {
			return 0;
		} else {
			return sum / (double) numericalMembers;
		}
	}

	/**
	 * Returns the median of a collection of numbers.
	 * 
	 * @param a
	 *            a collection of numbers
	 * @return median of collection a
	 */
	public static double median(Collection a) {
		ArrayList<Double> temp = new ArrayList();
		for (Object o : a) {
			if (o instanceof Number) {
				temp.add(((Number) o).doubleValue());
			}
		}
		Collections.sort(temp);
		int middle = temp.size() / 2;
		if (temp.size() % 2 == 1) {
			return temp.get(middle);
		} else {
			return (temp.get(middle - 1) + temp.get(middle)) / 2.0;
		}
	}

	/**
	 * Returns the standard deviation for a collection.
	 * 
	 * @param a
	 *            a collection
	 * @return standard deviation for a
	 */
	public static double standardDeviation(Collection a) {
		return Math.sqrt(variance(a));
	}

	/**
	 * Returns the square root of a number.
	 * 
	 * @param n
	 *            a number
	 * @return square root of n
	 */
	public static double sqrt(Number n) {
		return Math.sqrt(n.doubleValue());
	}

	/**
	 * Returns the absolute value of a number.
	 * 
	 * @param n
	 *            a number
	 * @return absolute value of number
	 */
	public static double abs(Number n) {
		return Math.abs(n.doubleValue());
	}

	/**
	 * Returns the variance for a list.
	 * 
	 * @param a
	 *            a list
	 * @return variance for a
	 */
	public static double variance(Collection a) {
		double mean = mean(a);
		ArrayList<Double> temp = new ArrayList();
		for (Object o : a) {
			if (o instanceof Number) {
				temp.add(((Number) o).doubleValue());
			}
		}
		double sum = 0;
		int tempSize = temp.size();
		if (tempSize <= 1) {
			return 0;
		}
		for (int i = 0; i < tempSize; i++) {
			double v = temp.get(i) - mean;
			sum += v * v;
		}
		return (sum / (tempSize - 1));
	}

	/**
	 * Returns the sum of a collection.
	 * 
	 * @param a
	 *            a collection
	 * @return sum of a
	 */
	public static double sum(Collection a) {
		if (a.size() == 0) {
			return 0;
		}
		double sum = 0.0;
		for (Object o : a) {
			if (o instanceof Number) {
				sum += ((Number) o).doubleValue();
			}
		}
		return sum;
	}

	/**
	 * Runs a set of commands a number of times.
	 * 
	 * @param number
	 *            a number
	 * @param commands
	 *            a set of commands
	 */
	public static void repeat(Number number, Closure commands) {
		for (int i = 0; i < number.intValue(); i++) {
			commands.call();
		}
	}

	// unsupported command, will just run the commands supplied to it
	public static void withLocalRandomness(Closure commands) {

		commands.call();

	}

	/**
	 * Returns the current date and time.
	 * 
	 * @return current date and time as string
	 */
	public static String dateAndTime() {
		String pattern = "hh:mm:ss.SSS a dd-MMM-yyyy";
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		return formatter.format(new Date());
	}

	/**
	 * Returns the time since "resetTimer" was last called.
	 * 
	 * @return time since "resetTimer" was last called
	 */
	public static double timer() {
		long lastTimer = ReLogoModel.getInstance().getLastTimer();
		long currentTime = (new Date()).getTime();
		double result = (double) (currentTime - lastTimer);
		return result / 1000.0;
	}

	/**
	 * The timer is reset to zero seconds.
	 */
	public static void resetTimer() {
		ReLogoModel.getInstance().setLastTimer(new Date().getTime());
	}

	public static Observer getObserverByID(String observerID) {
		Context context = RunState.getInstance().getMasterContext();
		Query query = new PropertyEquals(context, "observerID", observerID);
		Iterator result = query.query().iterator();
		if (result.hasNext()) {
			return (Observer) result.next();
		}
		return null;
	}

	public static List<Network> getDirectedNetworks(Observer observer) {
		Iterable networks = observer.getContext().getProjections(Network.class);
		List<Network> directedNetworks = new ArrayList<Network>();
		for (Object network : networks) {
			if (((Network) network).isDirected()) {
				directedNetworks.add(((Network) network));
			}
		}
		return directedNetworks;
	}

	public static List<Network> getUndirectedNetworks(Observer observer) {
		Iterable networks = observer.getContext().getProjections(Network.class);
		List<Network> undirectedNetworks = new ArrayList<Network>();
		for (Object network : networks) {
			if (!(((Network) network).isDirected())) {
				undirectedNetworks.add(((Network) network));
			}
		}
		return undirectedNetworks;
	}

	/**
	 * The tick counter is reset to zero.
	 */
	public static void resetTicks() {
		ReLogoModel.getInstance().setTicks(0.0);
	}

	/**
	 * The tick counter is incremented by one.
	 */
	public static void tick() {
		double currentTicks = ReLogoModel.getInstance().getTicks();
		ReLogoModel.getInstance().setTicks(currentTicks + 1.0);
	}

	/**
	 * The tick counter is incremented by num ticks.
	 * 
	 * @param num
	 *            a number
	 */
	public static void tickAdvance(Number num) {
		double currentTicks = ReLogoModel.getInstance().getTicks();
		if (num.doubleValue() >= 0.0) {
			ReLogoModel.getInstance().setTicks(currentTicks + num.doubleValue());
		}
	}

	/**
	 * Returns the present value of the tick counter.
	 * 
	 * @return present value of the tick counter
	 */
	public static double ticks() {
		return ReLogoModel.getInstance().getTicks();
	}

	/**
	 * Returns total number of agents in an agentset.
	 * 
	 * @param a
	 *            an agentset
	 * @returns total number of agents in a
	 */
	public static int count(Collection a) {
		return a.size();
	}

	// Colors section

	// Colors primitives
	public static void approximateHsb() {
		// TODO: implement
	}

	public static void approximateRgb() {
		// TODO: implement
	}

	public static void extractHsb(Number color) {
		// TODO: implement, should return a List
	}

	public static void extractRgb(Number color) {
		// TODO: implement, should return a List
	}

	/**
	 * Lists fundamental colors.
	 * 
	 * @return list of colors
	 */
	public static List baseColors() {
		Integer[] baseColorsList = { 5, 15, 25, 35, 45, 55, 65, 75, 85, 95,
				105, 115, 125, 135 };
		return new ArrayList(Arrays.asList(baseColorsList));
	}

	/**
	 * Returns a rgb color list.
	 * 
	 * @param hh
	 *            a number in the range [0, 255]
	 * @param ss
	 *            a number in the range [0, 255]
	 * @param bb
	 *            a number in the range [0, 255]
	 * @return rgb color list in the range [0, 255]
	 */
	public static List hsb(Number hh, Number ss, Number bb) {
		int rgb = Color.HSBtoRGB(hh.floatValue(), ss.floatValue(),
				bb.floatValue());
		int red = (rgb >> 16) & 0xFF;
		int green = (rgb >> 8) & 0xFF;
		int blue = rgb & 0xFF;
		int[] color = { red, green, blue };
		return new ArrayList(Arrays.asList(color));
	}

	/**
	 * Returns a rgb color list.
	 * 
	 * @param rr
	 *            a number in the range [0, 255]
	 * @param gg
	 *            a number in the range [0, 255]
	 * @parm bb a number in the range [0, 255]
	 * @return rgb color list in the range [0, 255]
	 */
	public static List rgb(Number rr, Number gg, Number bb) {
		int r = rr.intValue();
		if (r < 0 || r > 255) {
			throw new IllegalArgumentException("RGB component out of range.");
		}
		int g = gg.intValue();
		if (g < 0 || g > 255) {
			throw new IllegalArgumentException("RGB component out of range.");
		}
		int b = bb.intValue();
		if (b < 0 || b > 255) {
			throw new IllegalArgumentException("RGB component out of range.");
		}
		List result = new ArrayList();
		result.add(r);
		result.add(g);
		result.add(b);
		return result;
	}

	/**
	 * Returns a shaded color.
	 * 
	 * @param color
	 *            a number in the range [0,140)
	 * @param nVariableValue
	 *            a value
	 * @param nR1
	 *            a number
	 * @param nR2
	 *            a number
	 * @returns shaded color where the shading depends on where nVariableValue
	 *          falls between nR1 and nR2
	 */
	public static double scaleColor(Number color, Number nVariableValue,
			Number nR1, Number nR2) {
		double minValueForColor = ((int) (color.doubleValue() / 10.0)) * 10.0;
		double variableValue = nVariableValue.doubleValue();
		double r1 = nR1.doubleValue();
		double r2 = nR2.doubleValue();
		if (r1 <= r2) {
			if (variableValue < r1) {
				return minValueForColor;
			}
			if (variableValue > r2) {
				return minValueForColor + 9.9999;
			} else {
				double range = r2 - r1;
				double valueInRange = variableValue - r1;
				if (range != 0.0 && valueInRange != range) {
					return (minValueForColor + 10 * (valueInRange / range));
				} else if (range == 0.0) {
					return minValueForColor + 5.0;
				} else {
					return minValueForColor + 9.9999;
				}
			}
		} else {
			if (variableValue < r2) {
				return minValueForColor + 9.9999;
			}
			if (variableValue > r1) {
				return minValueForColor;
			}
			double range = r1 - r2;
			double valueInRange = r1 - variableValue;
			if (range != 0.0 && valueInRange != range) {
				return (minValueForColor + 10.0 * (valueInRange / range));
			} else if (range == 0.0) {
				return minValueForColor + 5.0;
			} else {
				return minValueForColor + 9.9999;
			}
		}
	}

	/**
	 * Queries if two shades are of the same color.
	 * 
	 * @param nColor1
	 *            a number
	 * @param nColor2
	 *            a number
	 * @return true or false based on whether nColor1 and nColor2 are shades of
	 *         the same color
	 */
	public static boolean shadeOfQ(Number nColor1, Number nColor2) {
		double minValue = ((int) (nColor1.doubleValue() / 10.0)) * 10.0;
		double color2 = nColor2.doubleValue();
		if (color2 >= minValue && color2 < minValue + 10.0) {
			return true;
		}
		return false;
	}

	/**
	 * Returns a color in the range [0, 140).
	 * 
	 * @param inputColor
	 *            a number
	 * @return color in the range [0, 140).
	 */
	public static double wrapColor(Number inputColor) {
		double d = inputColor.doubleValue();
		if (d < 0.0) {
			while (d < 0.0) {
				d += 140.0;
			}
		} else if (d >= 140.0) {
			while (d >= 140.0) {
				d -= 140.0;
			}
		}
		return d;
	}

	/**
	 * Returns the numerical value corresponding to the color black.
	 */
	public static double black() {
		return 0.0d;
	}

	/**
	 * Returns the numerical value corresponding to the color gray.
	 */
	public static double gray() {
		return 5.0d;
	}

	/**
	 * Returns the numerical value corresponding to the color white.
	 */
	public static double white() {
		return 9.99d;
	}

	/**
	 * Returns the numerical value corresponding to the color red.
	 */
	public static double red() {
		return 15.0d;
	}

	/**
	 * Returns the numerical value corresponding to the color orange.
	 */
	public static double orange() {
		return 25.0d;
	}

	/**
	 * Returns the numerical value corresponding to the color brown.
	 */
	public static double brown() {
		return 35.0d;
	}

	/**
	 * Returns the numerical value corresponding to the color yellow.
	 */
	public static double yellow() {
		return 45.0d;
	}

	/**
	 * Returns the numerical value corresponding to the color green.
	 */
	public static double green() {
		return 55.0d;
	}

	/**
	 * Returns the numerical value corresponding to the color lime.
	 */
	public static double lime() {
		return 65.0d;
	}

	/**
	 * Returns the numerical value corresponding to the color turquoise.
	 */
	public static double turquoise() {
		return 75.0d;
	}

	/**
	 * Returns the numerical value corresponding to the color cyan.
	 */
	public static double cyan() {
		return 85.0d;
	}

	/**
	 * Returns the numerical value corresponding to the color sky.
	 */
	public static double sky() {
		return 95.0d;
	}

	/**
	 * Returns the numerical value corresponding to the color blue.
	 */
	public static double blue() {
		return 105.0d;
	}

	/**
	 * Returns the numerical value corresponding to the color violet.
	 */
	public static double violet() {
		return 115.0d;
	}

	/**
	 * Returns the numerical value corresponding to the color magenta.
	 */
	public static double magenta() {
		return 125.0d;
	}

	/**
	 * Returns the numerical value corresponding to the color pink.
	 */
	public static double pink() {
		return 135.0d;
	}

	/**
	 * Does nothing, included for translation compatibility.
	 */
	public static void resetPerspective() {
	}
	
	
	/**
	 * Flush file data sinks.
	 */
	public static void flushFileSinks(){
		
		DataSetRegistry registry = (DataSetRegistry) RunState.getInstance().getFromRegistry(DataConstants.REGISTRY_KEY);
		for ( FileDataSink fds : registry.fileSinks()){
			fds.flush();
		}
	}
	
	/**
	 * Stop the simulation.
	 */
	public static void stop(){
		RunEnvironment.getInstance().endRun();
	}
	
	
	/**
	 * Pause the simulation.
	 */
	public static void pause(){
		RunEnvironment.getInstance().pauseRun();
	}
}
