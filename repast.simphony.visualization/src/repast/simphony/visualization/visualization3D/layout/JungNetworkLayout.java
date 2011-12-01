package repast.simphony.visualization.visualization3D.layout;

/*
* Port from JUNG AbstractLayout.java v. 1.7.4
* See http://jung.sourceforge.net/ for futher information
* 
* JUNG is open-source under the BSD license; see either
* "license.txt" or
* http://jung.sourceforge.net/license.txt for a description.
*/

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.event.ChangeListener;

import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.visualization.AbstractNetworkLayout;
import repast.simphony.visualization.Layout;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.visualization.util.ChangeEventSupport;
import edu.uci.ics.jung.visualization.util.DefaultChangeEventSupport;

/**
 * 
 * Abstract class to construct various types of JUNG graph layouts
 * 
 * @author M. Altaweel
 * 
 */
public abstract class JungNetworkLayout<T> extends AbstractNetworkLayout<T>
		implements ChangeEventSupport {

	/**
	 * @@@@Note RePast Network = JUNG Graph@@@@@
	 * 
	 */
	protected List<ChangeListener> changeListeners = new ArrayList<ChangeListener>();

	protected ChangeEventSupport changeSupport = new DefaultChangeEventSupport(
			this);

	/**
	 * a set of vertices that should not move in relation to the other vertices
	 */

	protected Set dontmove;

	private static final Object BASE_KEY = "edu.uci.ics.jung.Base_Visualization_Key";

	private Dimension currentSize;

	// protected Set<Object> vertices = new HashSet<Object>();
	protected Network visibleGraph;

	protected JungVertexLocationFunction vertex_locations;

	protected Set<Object> visibleVertices;

	protected Set<Object> visibleEdges;

	private Object key;

	protected Map<Object, Object> objectData;
	
	protected int xSize;
	
	protected int ySize;
	
	public JungNetworkLayout() {
	}

	/**
	 * The set of nodes that have been locked. When running layout, it is
	 * important to check
	 * 
	 * <pre>
	 *     if (dontmove( n )) { ... }
	 * </pre>
	 * 
	 * @return whether this node may be legally moved or not
	 */
	public boolean dontMove(Object o) {
		return dontmove.contains(o);
	}

	/**
	 * @return the visble vertex iterator object
	 */

	public void initialize(Dimension size) {
		initialize(size, new JungRandomVertexLocationDecorator(size));
	}

//	/**
//	 * @return the vertex (nodes) iterator is returned
//	 */
//	public Iterator getVertexIterator() {
//		return getVisibleVertices().iterator();
//	}

	/**
	 * 
	 * @param size
	 *            is a given dimension
	 * @param v_locations
	 *            is the calculated location for the node
	 */
	public void initialize(Dimension size,
			JungVertexLocationFunction v_locations) {
		this.currentSize = size;
		this.vertex_locations = v_locations;
		initialize_local();
		initializeLocations();
	}

	/**
	 * Initializes all local information, and is called immediately within the
	 * <tt>initialize()</tt> process. The user is responsible for overriding
	 * this method to do any construction that may be necessary: for example, to
	 * initialize local per-edge or graph-wide data.
	 * 
	 */
	protected void initialize_local() {
	}

	/**
	 * may be overridden to do something after initializeLocations call
	 * 
	 */
	protected void postInitialize() {
	}

	/**
	 * Initializes the local information on a single vertex. The user is
	 * responsible for overriding this method to do any vertex-level
	 * construction that may be necessary: for example, to attach vertex-level
	 * information to each vertex.
	 */
	protected abstract void initialize_local_vertex(Object o);

	/**
	 * Returns a visualization-specific key, not currently used
	 */
	public Object getBaseKey() {
		if (key == null)
			try {
				key = new Pair(this, BASE_KEY);
			} catch (Exception e) {
				e.printStackTrace();
			}
		return key;
	}

	protected void initializeLocations() {
		try {
			for (Iterator iter = baseGraph.getNodes().iterator(); iter
					.hasNext();) {
				Object o = iter.next();

				double[] coord = returnMatchingCoordinate(o);
				if (coord == null) {
					coord = new double[2];
					locationData.put(o, coord);
				}
				if (!dontmove.contains(o))
					initializeLocation(o, coord, currentSize);
				initialize_local_vertex(o);
			}
		} catch (ConcurrentModificationException cme) {
			initializeLocations();
		}

	}

	/**
	 * Returns a visualization-specific key that can be used to access UserData
	 * related to the <tt>AbstractLayout</tt>. Same as JUNG
	 * AbstractLayout.getBaseKey()
	 */

	public double[] returnMatchingCoordinate(Object o) {
		double[] coordinate = locationData.get(o);
		return coordinate;
	}

	/*
	 * public Object getBaseKey() throws Exception { if (key == null) key = new
	 * JungPair(this, BASE_KEY); return key; }
	 */
	/* ------------------------- */

	/**
	 * Sets random locations for a vertex within the dimensions of the space. If
	 * you want to initialize in some different way, override this method.
	 * 
	 * @param coord
	 * @param o
	 * @param d
	 */
	protected void initializeLocation(Object o, double[] coord, Dimension d) {
		locationData.put(o, vertex_locations.getLocation(o));
	}

	/**
	 * {@inheritDoc}By default, an <tt>AbstractLayout</tt> returns null for
	 * its status.
	 */
	public String getStatus() {
		return null;
	}

	/**
	 * set update by default to advance positions
	 */
	public void update() {
		initializeLocations();
	}

	/**
	 * Implementors must override this method in order to create a Layout.
	 */
	public abstract void advancePositions();

	/**
	 * Accessor for the graph that represets all visible vertices
	 * 
	 * @return the current visible graph (i.e. network).
	 * @see #getVisibleEdges
	 * @see #getVisibleVertices
	 */
	protected Network getVisibleGraph() {
		return visibleGraph;
	}

	/**
	 * Returns the current size of the visualization space, same as JUNG
	 * AbstractLayout.getCurrentSize()
	 * 
	 * @return the current size of the screen
	 */
	public Dimension getCurrentSize() {
		return currentSize;
	}

	/**
	 * Utility method, gets a single vertex from this edge
	 * 
	 * @param an
	 *            edge object that you want a vertex from
	 * @return a vertex object or null
	 */
	protected Object getAVertex(RepastEdge e) {
		Object vertex = null;
		for (Object v : visibleVertices) {
			if (e.getSource() == v) {
				vertex = v;
			} else if (e.getTarget() == v) {
				vertex = v;
			}
		}

		return vertex;
	}

	/**
	 * Returns the coordinates (float[2]) that stores the vertex' x and y
	 * location.
	 * 
	 * @param o=
	 *            the object to get coordinates from
	 * @return a coordinate float with x and y locations.
	 */
	public double[] getCoordinates(Object o) {
		double[] coordinates = locationData.get(o);
		return coordinates;
	}

	/**
	 * Returns the x coordinate of the vertex from the Coordinates object.
	 * 
	 */
	public double getX(Object o) {
		double[] coordinates = locationData.get(o);
		return coordinates[0];
	}

	/**
	 * Returns the y coordinate of the vertex from the Coordinates object.
	 * 
	 */
	public double getY(Object o) {
		double[] coordinates = locationData.get(o);
		return coordinates[1];
	}

	/**
	 * Finds the location of the object
	 * 
	 * @param obj you want to find location of
	 * @return the float[] containing the location
	 */
	public float[] getLocation(Object obj) {
		double coordinates[] = locationData.get(obj);
		float[] coords = null;

		if(coordinates==null) {
			coordinates=vertex_locations.getLocation(obj);
		}
		
		coords = new float[coordinates.length];
		for (int i = 0; i < coordinates.length; i++) {
			coords[i] = (float) coordinates[i];
		}
		return coords;
	}

	public float[] getLocation(Object obj,boolean threeD) {
		double coordinates[] = locationData.get(obj);
		float[] coords = null;

		if(coordinates==null) {
			coordinates=vertex_locations.getLocation(obj,true);
		}
		
		coords = new float[coordinates.length];
		for (int i = 0; i < coordinates.length; i++) {
			coords[i] = (float) coordinates[i];
		}
		return coords;
	}
	/**
	 * When a visualization is resized, it presumably wants to fix the locations
	 * of the vertices and possibly to reinitialize its data.
	 * 
	 * @param size
	 *            represents the dimension that will set the current size
	 * 
	 */
	public void resize(Dimension size) {
		// are we initialized yet?

		if (currentSize == null) {
			currentSize = size;
			return;
		}

		Dimension oldSize;
		synchronized (currentSize) {
			if (currentSize.equals(size))
				return;
			oldSize = currentSize;
			this.currentSize = size;
		}

		int xOffset = (size.width - oldSize.width) / 2;
		int yOffset = (size.height - oldSize.height) / 2;

		// now, move each vertex to be at the new screen center
		while (true) {
			try {
				for (Iterator iter = getGraph().getNodes().iterator(); iter
						.hasNext();) {
					Object o = (Object) iter.next();
					offsetVertex(o, xOffset, yOffset);
				}
				break;
			} catch (ConcurrentModificationException cme) {
			}
		}

		// optionally, we may want to restart
	}

	/**
	 * Offset the object based on the inputs
	 * 
	 * @param o
	 *            object to offset
	 * @param xOffset
	 * @param yOffset
	 */
	protected void offsetVertex(Object o, double xOffset, double yOffset) {
		double[] coordinates = getCoordinates(o);
		coordinates[0] = xOffset + coordinates[0];
		coordinates[1] = yOffset + coordinates[1];
		forceMove(o, coordinates[0], coordinates[1]);
	}

	/**
	 * @see Layout#restart() in JUNG
	 */
	public void restart() {
		initialize_local();
		initializeLocations();
	}

	/**
	 * Calls get vertex to get object of nearest location to x and y
	 * 
	 * @param x
	 *            and y represent location you want object to be near
	 * @deprecated
	 */
	public Object getVertex(double x, double y) {
		return getVertex(x, y, Math.sqrt(Double.MAX_VALUE - 1000));
	}

	/**
	 * Gets the vertex nearest to the location of the (x,y) location selected,
	 * within a distance of maxDistance. Iterates through all visible vertices
	 * and checks their distance from the click. Override this method to provde
	 * a more efficient implementation.
	 * 
	 * @deprecated
	 */
	public Object getVertex(double x, double y, double maxDistance) {

		double minDistance = maxDistance * maxDistance;
		Object closest = null;
		while (true) {
			try {
				for (Iterator iter = getVisibleVertices().iterator(); iter
						.hasNext();) {
					Object o = (Object) iter.next();
					double[] location = getCoordinates(o);
					double dx = location[0] - x;
					double dy = location[1] - y;
					double dist = dx * dx + dy * dy;
					if (dist < minDistance) {
						minDistance = dist;
						closest = o;
					}
				}
				break;
			} catch (ConcurrentModificationException cme) {
			}
		}
		return closest;
	}

	/**
	 * Gets the edge nearest to the location of the (x,y) location selected.
	 * Calls the longer form of the call.
	 * 
	 * @params x and y location of object
	 * @deprecated
	 */
	public RepastEdge getEdge(double x, double y) {
		return getEdge(x, y, Math.sqrt(Double.MAX_VALUE - 1000));
	}

	/**
	 * Gets the edge nearest to the location of the (x,y) location selected,
	 * within a distance of <tt>maxDistance</tt>, Iterates through all
	 * visible edges and checks their distance from the click. Override this
	 * method to provide a more efficient implementation.
	 * 
	 * @deprecated
	 * @param x
	 * @param y
	 * @param maxDistance
	 * @return Edge closest to the click.
	 */
	public RepastEdge getEdge(double x, double y, double maxDistance) {

		double minDistance = maxDistance * maxDistance;
		RepastEdge closest = null;
		while (true) {
			try {
				for (Iterator iter = getVisibleEdges().iterator(); iter
						.hasNext();) {
					RepastEdge e = (RepastEdge) iter.next();
					// if anyone uses a hyperedge, this is too complex.
					if (e.getSource() != null && e.getTarget() != null)
						continue;
					// Could replace all this set stuff with getFrom_internal()
					// etc.
					Set vertices = getVerticesFromEdge(e);
					Iterator vertexIterator = vertices.iterator();
					Object v1 = vertexIterator.next();
					Object v2 = vertexIterator.next();
					// Get coords
					double location1[] = getCoordinates(v1);
					double location2[] = getCoordinates(v2);
					double x1 = location1[0];
					double y1 = location1[1];
					double x2 = location2[0];
					double y2 = location2[1];
					// Calculate location on line closest to (x,y)
					// First, check that v1 and v2 are not coincident.
					if (x1 == x2 && y1 == y2)
						continue;
					double b = ((y - y1) * (y2 - y1) + (x - x1) * (x2 - x1))
							/ ((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
					//
					double distance2; // square of the distance
					if (b <= 0)
						distance2 = (x - x1) * (x - x1) + (y - y1) * (y - y1);
					else if (b >= 1)
						distance2 = (x - x2) * (x - x2) + (y - y2) * (y - y2);
					else {
						double x3 = x1 + b * (x2 - x1);
						double y3 = y1 + b * (y2 - y1);
						distance2 = (x - x3) * (x - x3) + (y - y3) * (y - y3);
					}

					if (distance2 < minDistance) {
						minDistance = distance2;
						closest = e;
					}
				}
				break;
			} catch (ConcurrentModificationException cme) {
			}
		}
		return closest;
	}

	/**
	 * Accessor for the graph that represets all vertices. must match the nodes
	 * in the graph with the vertices created
	 * 
	 * @return the graph that contains all vertices.
	 */
	public Network getGraph() {
		return super.getGraph();
	}

	/**
	 * Gets edges that are equivalent to the ones passed in from the argument to
	 * applyFilter().
	 * 
	 * @return set of edges from the original
	 */
	public Set getVisibleEdges() {
		return visibleEdges;
	}

	/**
	 * Gets vertices that are equivalent to the ones passed in from the argument
	 * to applyFilter().
	 * 
	 * @return set of vertices from the original
	 */
	public Set getVisibleVertices() {
		return visibleVertices;
	}

	/**
	 * Forcibly moves a vertex to the (x,y) location by setting its x and y
	 * locations to the inputted location. Does not add the vertex to the
	 * "dontmove" list, and (in the default implementation) does not make any
	 * adjustments to the rest of the graph.
	 */
	public void forceMove(Object picked, double x, double y) {
		double coord[] = getCoordinates(picked);
		coord[0] = x;
		coord[1] = y;
		fireStateChanged();
	}
	
	public void forceMove(Object picked, double x, double y,double z) {
		double coord[] = getCoordinates(picked);
		coord[0] = x;
		coord[1] = y;
		coord[2] = z;
		fireStateChanged();
	}

	/**
	 * Adds the vertex to the DontMove list
	 */
	public void lockVertex(Object o) {
		dontmove.add(o);
	}

	/**
	 * Removes the vertex from the DontMove list
	 */
	public void unlockVertex(Object o) {
		dontmove.remove(o);
	}

	/**
	 * Applies the filter to the current network and network g. Shared vertices
	 * and edges are set to the visible edges and vertices
	 * 
	 */
	public void applyFilter(Network n) {
		this.visibleGraph = n;
		Set thisVertices = addEdgesOrVertices(n.getNodes());
		Set thisEdges = addEdgesOrVertices(n.getEdges());

		Set thatVertices = addEdgesOrVertices(baseGraph.getNodes());
		Set thatEdges = addEdgesOrVertices(baseGraph.getEdges());

		this.visibleVertices = returnEqualSet(thisVertices, thatVertices);
		this.visibleEdges = returnEqualSet(thisEdges, thatEdges);

	}

	/**
	 * Adds a change listener to listen for change events produced by network
	 * layout.
	 * 
	 * @param arg0
	 *            the listener to add
	 */
	public void addChangeListener(ChangeListener arg0) {
		changeListeners.add(arg0);
	}

	/**
	 * Removes a change listener
	 * 
	 * @param arg0
	 *            the listener to remove
	 */
	public void removeChangeListener(ChangeListener arg0) {
		changeListeners.remove(arg0);
	}

	/**
	 * 
	 * Returns an array of change listener objects
	 * 
	 */
	public ChangeListener[] getChangeListeners() {
		ChangeListener[] changeListenerArray = (ChangeListener[]) changeListeners
				.toArray();
		;
		return changeListenerArray;
	}

	/**
	 * Notifies all listeners that have registered interest for notification on
	 * this event type. The primary listeners will be views that need to be
	 * repainted because of changes in this model instance
	 * 
	 * @see event listener list
	 */
	public void fireStateChanged() {
		changeSupport.fireStateChanged();
	}

	/**
	 * two vertices or edges to compare to see if they are equal
	 * 
	 * @param thisEdges
	 * @param thatEdges
	 * @return the equal vertices between the two compared
	 */
	public Set returnEqualSet(Set<Object> thisVertices, Set<Object> thatVertices) {
		Set<Object> equalVertices = new HashSet<Object>();
		for (Object o : thisVertices) {
			if (thatVertices.contains(o)) {
				equalVertices.add(o);
			}
		}
		return equalVertices;
	}

	/**
	 * Get the vertex values for the edge nodes
	 * 
	 * @param an
	 *            edge object
	 * @return a set of vertices
	 */
	private Set getVerticesFromEdge(RepastEdge e) {
		Set<Object> set = new HashSet<Object>();
		Object s = e.getSource();
		Object t = e.getTarget();
		set.add(s);
		set.add(t);

		return set;
	}

	/**
	 * This gets the edges or vertices from the network
	 * 
	 * @param it
	 *            represents the iterable object
	 * @return the return set of the object used for edges or vertices
	 */
	protected Set<Object> addEdgesOrVertices(Iterable it) {
		Set<Object> objects = new HashSet<Object>();
		for (Object o : it) {
			objects.add(o);
		}
		return objects;
	}

	/**
	 * This adds repast.simphony edges to the set
	 * 
	 * @param it iterable edges
	 * @return a set of edges
	 */
	private Set<RepastEdge> addEdges(Iterable<RepastEdge> it) {
		Set<RepastEdge> objects = new HashSet<RepastEdge>();
		for (RepastEdge re : it) {
			objects.add(re);
		}
		return objects;
	}

	@Override
	public void setProjection(Network projection) {
		this.baseGraph = projection;
		this.visibleGraph = projection;

		Iterable edges = projection.getEdges();
		this.visibleEdges = addEdgesOrVertices(edges);
		Iterable nodes = projection.getNodes();
		this.visibleVertices = addEdgesOrVertices(nodes);
		this.dontmove = new HashSet();
		this.locationData = new HashMap<Object, double[]>();
		this.objectData = new HashMap<Object, Object>(); // added map for
														// extra
		this.currentSize=new Dimension(3200,1600);
		initialize(currentSize);
	}
	
	//temporary addition until proper listeners are in place to allow updating of vertices and edges
	public void resetVisibleEdgesAndVertices() {
		Iterable edges = baseGraph.getEdges();
		Iterable vertices = baseGraph.getNodes();
		
		this.visibleEdges = addEdgesOrVertices(edges);
		this.visibleVertices = addEdgesOrVertices(vertices);
	}

	public int getXSize() {
		return xSize;
	}

	public void setXSize(int size) {
		xSize = size;
	}

	public int getYSize() {
		return ySize;
	}

	public void setYSize(int size) {
		ySize = size;
	}

	public String getName() {
		return super.getName();
	}
	

}
