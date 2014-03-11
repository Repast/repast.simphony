/*
 * 
 * Repast Simphony Code Snippets
 * 
 * Author: Michael J. North
 * 
 */

package repast.simphony.essentials;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.josql.QueryExecutionException;
import org.josql.QueryParseException;
import org.josql.QueryResults;
import org.jscience.physics.amount.Amount;

import repast.simphony.context.Context;
import repast.simphony.context.Contexts;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.graph.DefaultNodeCreator;
import repast.simphony.context.space.graph.Lattice1DGenerator;
import repast.simphony.context.space.graph.Lattice2DGenerator;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.context.space.graph.NetworkFactory;
import repast.simphony.context.space.graph.NetworkFactoryFinder;
import repast.simphony.context.space.graph.NetworkFileFormat;
import repast.simphony.context.space.graph.NetworkGenerator;
import repast.simphony.context.space.graph.RandomDensityGenerator;
import repast.simphony.context.space.graph.WattsBetaSmallWorldGenerator;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.schedule.IAction;
import repast.simphony.engine.schedule.ISchedulableAction;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.integration.DataFileReader;
import repast.simphony.integration.DataFileWriter;
import repast.simphony.matlab.link.LinkMatlab;
import repast.simphony.query.OrQuery;
import repast.simphony.query.Query;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.PointTranslator;
import repast.simphony.space.continuous.RandomCartesianAdder;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.GridPointTranslator;
import repast.simphony.space.grid.RandomGridAdder;
import repast.simphony.space.projection.Projection;
import repast.simphony.util.collections.IndexedIterable;
import repast.simphony.valueLayer.ContinuousValueLayer;
import repast.simphony.valueLayer.GridValueLayer;
import repast.simphony.valueLayer.ValueLayer;
import simphony.util.messages.MessageCenter;

public class RepastEssentials {

	public final static long serialVersionUID = 0;

	static MessageCenter msgCenter = MessageCenter
			.getMessageCenter(RepastEssentials.class);

	public static List FindAgentsInContext(String contextPath, String sqlQuery) {

		return FindAgentsInContext(contextPath, sqlQuery, null);

	}

	public static List FindAgentsInContext(String contextPath, String sqlQuery,
			Object functionHandler) {

		try {
			org.josql.Query model = new org.josql.Query();
			model.setClassLoader(RepastEssentials.class.getClassLoader());
			model.addFunctionHandler(functionHandler);
			model.parse(sqlQuery);

			Context context = FindContext(contextPath);

			Class fromClass = model.getFromObjectClass();
			ArrayList list = new ArrayList();
			for (Object obj : context.getObjects(fromClass)) {
				if ((obj != null)
						&& (fromClass.isAssignableFrom(obj.getClass()))) {
					list.add(obj);
				}
			}
			QueryResults results = model.execute(list);

			return list;
		} catch (QueryParseException e) {
			e.printStackTrace();
		} catch (QueryExecutionException e) {
			e.printStackTrace();
		}

		return null;

	}

	public static List FindAgentsInNetwork(String contextPath, String sqlQuery) {

		return FindAgentsInNetwork(contextPath, sqlQuery, null);

	}

	public static List FindAgentsInNetwork(String networkPath, String sqlQuery,
			Object functionHandler) {

		try {
			org.josql.Query model = new org.josql.Query();
			model.setClassLoader(RepastEssentials.class.getClassLoader());
			model.addFunctionHandler(functionHandler);
			model.parse(sqlQuery);

			Projection projection = FindProjection(networkPath);
			if (projection instanceof Network) {

				Network network = (Network) projection;
				ArrayList list = new ArrayList();
				for (Object obj : network.getEdges()) {
					if ((obj != null)
							&& (RepastEdge.class.isAssignableFrom(obj
									.getClass()))) {
						list.add(obj);
					}
				}
				QueryResults results = model.execute(list);
				return list;

			} else {

				return null;

			}

		} catch (QueryParseException e) {
			e.printStackTrace();
		} catch (QueryExecutionException e) {
			e.printStackTrace();
		}

		return null;

	}

	public static Context FindContext(String contextPath) {
		Context currentContext = RunState.getSafeMasterContext();
		if (contextPath.startsWith("/"))
			contextPath = contextPath.substring(1, contextPath.length());
		String[] contextPathParts = contextPath.split("/");

		int startIndex = 0;
		if (currentContext.getId().equals(contextPathParts[0])) {
			if (contextPathParts.length == 1)
				return currentContext;
			else {
				startIndex = 1;
			}
		}

		return FindContext(currentContext, contextPathParts, startIndex,
				contextPathParts.length);
	}

	public static IndexedIterable GetObjects(String contextPath,
			String className) {
		Context context = FindContext(contextPath);
		try {
			Class clazz = Class.forName(className);
			return context.getObjects(clazz);
		} catch (ClassNotFoundException e) {
			msgCenter.error("Error while getting objects", e);
		}

		return null;
	}

	public static Iterable GetRandomObjects(String contextPath,
			String className, long num) {
		Context context = FindContext(contextPath);
		try {
			Class clazz = Class.forName(className);
			return context.getRandomObjects(clazz, num);
		} catch (ClassNotFoundException e) {
			msgCenter.error("Error while getting objects", e);
		}

		return null;
	}

	private static Context FindContext(Context context, String[] path,
			BigDecimal startIndex, BigDecimal endIndex) {
		return FindContext(context, path, startIndex.intValue(), endIndex
				.intValue());
	}

	private static Context FindContext(Context context, String[] path,
			int startIndex, int endIndex) {
		for (int i = startIndex; i < endIndex; i++) {
			if (context == null)
				break;
			context = context.getSubContext(path[i]);
		}
		return context;
	}

	public static Context FindParentContext(String contextPath) {
		Context currentContext = RunState.getSafeMasterContext();
		if (contextPath.startsWith("/"))
			contextPath = contextPath.substring(1, contextPath.length());
		String[] contextPathParts = contextPath.split("/");

		int startIndex = 0;
		if (currentContext.getId().equals(contextPathParts[0])) {
			if (contextPathParts.length == 2)
				return currentContext;
			else {
				startIndex = 1;
			}
		}

		return FindContext(currentContext, contextPathParts, startIndex,
				contextPathParts.length - 1);

	}

	/**
	 * Finds the ValueLayer that matches the specified path. The first part of
	 * the path is the context heirarchy and the last element is the value layer
	 * name.
	 * 
	 * @param valueLayerPath
	 *            the path to the value layer
	 * @return the found value layer.
	 */
	public static ValueLayer FindValueLayer(String valueLayerPath) {
		Context currentContext = RunState.getSafeMasterContext();
		if (valueLayerPath.startsWith("/"))
			valueLayerPath = valueLayerPath.substring(1, valueLayerPath
					.length());
		String[] pathParts = valueLayerPath.split("/");
		if (pathParts.length == 1)
			return currentContext.getValueLayer(pathParts[0]);
		int startIndex = pathParts[0].equals(currentContext.getId()) ? 1 : 0;
		Context context = FindContext(currentContext, pathParts, startIndex,
				pathParts.length - 1);
		return context.getValueLayer(pathParts[pathParts.length - 1]);
	}

	/**
	 * Adds the specified value layer to the context on the specified path.
	 * 
	 * @param contextPath
	 *            the path to the context
	 * @param layer
	 *            the value layer to add
	 */
	public static void AddValueLayer(String contextPath, ValueLayer layer) {
		Context context = FindContext(contextPath);
		context.addValueLayer(layer);
	}

	public static GridValueLayer CreateGridValueLayer(String contextPath,
			String name, BigDecimal... dimensions) {
		return CreateGridValueLayer(contextPath, name,
				bigDecimalArrayToIntArray(dimensions));
	}

	/**
	 * Creates a GridValueLayer and adds it to the specified context.
	 * 
	 * @param contextPath
	 * @param name
	 * @param dimensions
	 * @return the created GridValueLayer.
	 */
	public static GridValueLayer CreateGridValueLayer(String contextPath,
			String name, int... dimensions) {
		Context context = FindContext(contextPath);
		GridValueLayer layer = new GridValueLayer(name, true, dimensions);
		context.addValueLayer(layer);
		return layer;
	}

	public static ContinuousValueLayer CreateContinuousValueLayer(
			String contextPath, String name, BigDecimal... dimensions) {
		return CreateContinuousValueLayer(contextPath, name,
				bigDecimalArrayToDoubleArray(dimensions));
	}

	/**
	 * Creates a ContinousValueLayer and adds it to the specified context.
	 * 
	 * @param contextPath
	 * @param name
	 * @param dimensions
	 * @return the created ContinuousValueLayer.
	 */
	public static ContinuousValueLayer CreateContinuousValueLayer(
			String contextPath, String name, double... dimensions) {
		Context context = FindContext(contextPath);
		ContinuousValueLayer layer = new ContinuousValueLayer(name, dimensions);
		context.addValueLayer(layer);
		return layer;
	}

	// idea is that last part of path is a projection name and everything
	// prior to that is a context path
	public static Projection FindProjection(String projectionPath) {
		Context currentContext = RunState.getSafeMasterContext();
		if (projectionPath.startsWith("/"))
			projectionPath = projectionPath.substring(1, projectionPath
					.length());
		String[] projectionPathParts = projectionPath.split("/");
		if (projectionPathParts.length == 1)
			return currentContext.getProjection(projectionPathParts[0]);
		int startIndex = projectionPathParts[0].equals(currentContext.getId()) ? 1
				: 0;
		Context context = FindContext(currentContext, projectionPathParts,
				startIndex, projectionPathParts.length - 1);
		return context
				.getProjection(projectionPathParts[projectionPathParts.length - 1]);
	}

	/**
	 * Finds the Network projection from the given path.
	 * 
	 * @param networkPath
	 *            the path of the Network
	 * @return the Network
	 */
	public static Network FindNetwork(String networkPath) {
		return (Network) FindProjection(networkPath);
	}

	/**
	 * Finds the Grid projection from the given path.
	 * 
	 * @param gridPath
	 *            the path of the Grid
	 * @return the Grid
	 */
	public static Grid FindGrid(String gridPath) {
		return (Grid) FindProjection(gridPath);
	}

	/**
	 * Finds the Continuous Space projection from the given path.
	 * 
	 * @param spacePath
	 *            the path of the Space
	 * @return the Continuous Space
	 */
	public static ContinuousSpace FindContinuousSpace(String spacePath) {
		return (ContinuousSpace) FindProjection(spacePath);
	}

	public static Context CreateContext(String parentContextPath,
			String newContextName) {

		Context parentContext = FindContext(parentContextPath);
		Context newContext = Contexts.createContext(Object.class,
				newContextName);
		parentContext.addSubContext(newContext);
		return newContext;

	}

	public static Context RemoveContext(String contextPath) {

		Context parentContext = FindParentContext(contextPath);
		if (contextPath.startsWith("/"))
			contextPath = contextPath.substring(1, contextPath.length());
		String contextPathParts[] = contextPath.split("/");
		String contextName = contextPathParts[(contextPathParts.length - 1)];
		Context subContext = parentContext.getSubContext(contextName);
		if (subContext != null)
			parentContext.removeSubContext(subContext);
		return subContext;

	}

	public static Network CreateRandomDensityNetwork(String parentContextPath,
			String networkName, boolean directed, double density,
			boolean allowSelfLoops, boolean symmetric) {
		RandomDensityGenerator gen = new RandomDensityGenerator(density,
				allowSelfLoops, symmetric);
		return RepastEssentials.CreateNetwork(parentContextPath, networkName,
				directed, gen);
	}

	public static Network Create1DLatticeNetwork(String parentContextPath,
			String networkName, boolean directed, boolean toroidal,
			boolean symmetrical) {
		Lattice1DGenerator gen = new Lattice1DGenerator(toroidal, symmetrical);
		return RepastEssentials.CreateNetwork(parentContextPath, networkName,
				directed, gen);
	}

	public static Network Create2DLatticeNetwork(String parentContextPath,
			String networkName, boolean directed, boolean toroidal) {
		Lattice2DGenerator gen = new Lattice2DGenerator(toroidal);
		return RepastEssentials.CreateNetwork(parentContextPath, networkName,
				directed, gen);
	}

	public static Network CreateWattsBetaSmallWorldNetwork(
			String parentContextPath, String networkName, boolean directed,
			double beta, BigDecimal nghSize, boolean symmetrical) {
		return CreateWattsBetaSmallWorldNetwork(parentContextPath, networkName,
				directed, beta, nghSize.intValue(), symmetrical);
	}

	public static Network CreateWattsBetaSmallWorldNetwork(
			String parentContextPath, String networkName, boolean directed,
			double beta, int nghSize, boolean symmetrical) {
		WattsBetaSmallWorldGenerator gen = new WattsBetaSmallWorldGenerator(
				beta, nghSize, symmetrical);
		return RepastEssentials.CreateNetwork(parentContextPath, networkName,
				directed, gen);
	}

	public static Network CreateNetwork(String parentContextPath,
			String netName, boolean isDirected, String agentClassName,
			String fileName, NetworkFileFormat format) {
		Context context = FindContext(parentContextPath);
		NetworkBuilder builder = new NetworkBuilder(netName, context,
				isDirected);

		Class clazz;
		try {
			clazz = Class.forName(agentClassName, true, builder.getClass()
					.getClassLoader());
			builder.load(fileName, format, new DefaultNodeCreator(clazz));
		} catch (ClassNotFoundException e) {
			msgCenter.error("Error while creating network", e);
		} catch (IOException e) {
			msgCenter.error("Error while creating network", e);
		}

		return builder.buildNetwork();
	}

	public static Network CreateNetwork(String parentContextPath,
			String networkName, boolean directed, NetworkGenerator gen) {
		Context parentContext = FindContext(parentContextPath);
		NetworkFactory factory = NetworkFactoryFinder
				.createNetworkFactory(new HashMap());
		return factory.createNetwork(networkName, parentContext, gen, directed);
	}
	
	public static Network CreateNetwork(String parentContextPath,
			String netName, boolean isDirected) {
		Context context = FindContext(parentContextPath);
		NetworkBuilder builder = new NetworkBuilder(netName, context,
				isDirected);

		return builder.buildNetwork();
	}

	public static Grid CreateGrid(String parentContextPath, 
			String newProjectionName, String borders, BigDecimal... dimensions){
		
		return (Grid)RepastEssentials.CreateProjection(parentContextPath, 
				newProjectionName, "Grid", borders, dimensions);
	}
	
	public static Grid CreateGrid(String parentContextPath, 
			String newProjectionName, String borders, int... dimensions){
		
		return (Grid)RepastEssentials.CreateProjection(parentContextPath, 
				newProjectionName, "Grid", borders, dimensions);
	}
	
	public static ContinuousSpace CreateContinuousSpace(String parentContextPath, 
			String newProjectionName, String borders, BigDecimal... dimensions){
		
		return (ContinuousSpace)RepastEssentials.CreateProjection(parentContextPath, 
				newProjectionName, "Continuous", borders, dimensions);
	}
	
	public static ContinuousSpace CreateContinuousSpace(String parentContextPath, 
			String newProjectionName, String borders, int... dimensions){
		
		return (ContinuousSpace)RepastEssentials.CreateProjection(parentContextPath, 
				newProjectionName, "Continuous", borders, dimensions);
	}
	
	public static Projection CreateProjection(String parentContextPath,
			String newProjectionName, String newProjectionType) {
		return RepastEssentials.CreateProjection(parentContextPath,
				newProjectionName, newProjectionType, true,
				"WrapAroundBorders", 100, 100);
	}

	public static Projection CreateProjection(String parentContextPath,
			String newProjectionName, String newProjectionType,
			boolean optionalProjectionKind) {
		return RepastEssentials.CreateProjection(parentContextPath,
				newProjectionName, newProjectionType, optionalProjectionKind,
				"WrapAroundBorders", 100, 100);
	}

	public static Projection CreateProjection(String parentContextPath,
			String newProjectionName, String newProjectionType,
			BigDecimal... optionalDimensions) {
		return CreateProjection(parentContextPath, newProjectionName,
				newProjectionType,
				bigDecimalArrayToIntArray(optionalDimensions));
	}

	public static Projection CreateProjection(String parentContextPath,
			String newProjectionName, String newProjectionType,
			int... optionalDimensions) {
		return RepastEssentials.CreateProjection(parentContextPath,
				newProjectionName, newProjectionType, true,
				"WrapAroundBorders", optionalDimensions);
	}

	public static Projection CreateProjection(String parentContextPath,
			String newProjectionName, String newProjectionType,
			String optionalBorders, BigDecimal... optionalDimensions) {
		return CreateProjection(parentContextPath, newProjectionName,
				newProjectionType, optionalBorders,
				bigDecimalArrayToIntArray(optionalDimensions));
	}

	public static Projection CreateProjection(String parentContextPath,
			String newProjectionName, String newProjectionType,
			String optionalBorders, int... optionalDimensions) {
		return RepastEssentials.CreateProjection(parentContextPath,
				newProjectionName, newProjectionType, true, optionalBorders,
				optionalDimensions);
	}

	/**
	 * @param parentContextPath
	 * @param newProjectionName
	 * @param newProjectionType
	 * @param optionalProjectionKind
	 *            if net then specified isDirected, if grid then specified is
	 *            mult. occupancy
	 * @param optionalBorders
	 * @param optionalDimensions
	 * @return
	 */
	public static Projection CreateProjection(String parentContextPath,
			String newProjectionName, String newProjectionType,
			boolean optionalProjectionKind, String optionalBorders,
			int... optionalDimensions) {

		Context parentContext = FindContext(parentContextPath);
		Projection newProjection = null;
		if (newProjectionType.equalsIgnoreCase("Grid")) {

			GridPointTranslator gridPointTranslator = null;
			try {
				gridPointTranslator = (GridPointTranslator) Class.forName(
						"repast.simphony.space.grid." + optionalBorders)
						.newInstance();
			} catch (Exception e) {
				msgCenter.error(optionalBorders, e);
			}
			newProjection = GridFactoryFinder
					.createGridFactory(null)
					.createGrid(
							newProjectionName,
							parentContext,
							new GridBuilderParameters(gridPointTranslator,
									new RandomGridAdder(),
									optionalProjectionKind, optionalDimensions));

		} else if (newProjectionType.equalsIgnoreCase("Graph")) {

			newProjection = NetworkFactoryFinder.createNetworkFactory(null)
					.createNetwork(newProjectionName, parentContext,
							optionalProjectionKind);

		} else if (newProjectionType.equalsIgnoreCase("Continuous")) {

			PointTranslator pointTranslator = null;
			try {
				pointTranslator = (PointTranslator) Class.forName(
						"repast.simphony.space.continuous." + optionalBorders)
						.newInstance();
			} catch (Exception e) {
				msgCenter.error(optionalBorders, e);
			}
			double[] dOptionalDimensions = new double[optionalDimensions.length];
			for (int i = 0; i < optionalDimensions.length; i++) {
				dOptionalDimensions[i] = (double) optionalDimensions[i];
			}
			newProjection = ContinuousSpaceFactoryFinder
					.createContinuousSpaceFactory(null).createContinuousSpace(
							newProjectionName, parentContext,
							new RandomCartesianAdder(), pointTranslator,
							dOptionalDimensions);

		} else {
			throw new IllegalArgumentException("Projection type '"
					+ newProjectionType + "' not found.");
		}

		parentContext.addProjection(newProjection);

		return newProjection;

	}

	public static Projection RemoveProjection(String path) {

		Context parentContext = FindParentContext(path);
		if (path.startsWith("/"))
			path = path.substring(1, path.length());
		String pathParts[] = path.split("/");
		return parentContext.removeProjection(pathParts[pathParts.length - 1]);
	}

	public static Object CreateAgent(String parentContextPath,
			String newAgentType) {

		Context parentContext = FindContext(parentContextPath);
		Object newAgent = null;
		try {
			newAgent = Class.forName(newAgentType).newInstance();
			parentContext.add(newAgent);
		} catch (Exception e) {
			msgCenter.error(newAgentType, e);
		}
		return newAgent;

	}

	public static Object CreateAgents(String parentContextPath,
			String newAgentType, int agentCount) {

		Context parentContext = FindContext(parentContextPath);
		Object newAgent = null;
		try {
			for (int i = 0; i < agentCount; i++) {
				newAgent = Class.forName(newAgentType).newInstance();
				parentContext.add(newAgent);
			}
		} catch (Exception e) {
			msgCenter.error(newAgentType, e);
		}
		return newAgent;

	}

	public static RepastEdge<?> CreateEdge(String networkProjectionPath,
			Object agentSource, Object agentTarget) {
		return RepastEssentials.CreateEdge(networkProjectionPath, agentSource,
				agentTarget, 0);
	}

	public static RepastEdge<?> CreateEdge(String networkProjectionPath,
			Object agentSource, Object agentTarget, double optionalWeight) {

		Network network = (Network) FindProjection(networkProjectionPath);
		return network.addEdge(agentSource, agentTarget, optionalWeight);

	}

	public static RepastEdge FindEdge(String networkProjectionPath,
			Object agentSource, Object agentTarget) {

		Network network = (Network) FindProjection(networkProjectionPath);
		return network.getEdge(agentSource, agentTarget);

	}

	public static double GetEdgeWeight(String networkProjectionPath,
			Object agentSource, Object agentTarget) {

		Network network = (Network) FindProjection(networkProjectionPath);
		return network.getEdge(agentSource, agentTarget).getWeight();

	}

	public static RepastEdge SetEdgeWeight(String networkProjectionPath,
			Object agentSource, Object agentTarget, double newWeight) {

		Network network = (Network) FindProjection(networkProjectionPath);
		RepastEdge edge = network.getEdge(agentSource, agentTarget);
		edge.setWeight(newWeight);
		return edge;
	}

	public static RepastEdge RemoveEdge(String networkProjectionPath,
			Object agentSource, Object agentTarget) {

		Network network = (Network) FindProjection(networkProjectionPath);
		RepastEdge edge = network.getEdge(agentSource, agentTarget);
		if (edge != null)
			network.removeEdge(edge);
		return edge;
	}

	/**
	 * Gets the predecessors of the specified agent in the specified network.
	 * 
	 * @param networkPath
	 *            the path to the network
	 * @param agent
	 *            the agent whose predecessors we want
	 * @return the predecessors of the specified agent in the specified network.
	 */
	public static List GetPredecessors(String networkPath, Object agent) {
		Network net = (Network) FindProjection(networkPath);
		List list = new ArrayList();
		if (net == null)
			return list;
		Iterable iter = net.getPredecessors(agent);
		if (iter instanceof Collection)
			list.addAll((Collection) iter);
		else {
			for (Object obj : iter) {
				list.add(obj);
			}
		}

		return list;
	}

	/**
	 * Gets the successors of the specified agent in the specified network.
	 * 
	 * @param networkPath
	 *            the path to the network
	 * @param agent
	 *            the agent whose successors we want
	 * @return the successors of the specified agent in the specified network.
	 */
	public static List GetSuccessors(String networkPath, Object agent) {
		Network net = (Network) FindProjection(networkPath);
		List list = new ArrayList();
		if (net == null)
			return list;
		Iterable iter = net.getSuccessors(agent);
		if (iter instanceof Collection)
			list.addAll((Collection) iter);
		else {
			for (Object obj : iter) {
				list.add(obj);
			}
		}

		return list;
	}

	/**
	 * Gets the agents adjacent to the specified agent in the specified network.
	 * 
	 * @param networkPath
	 *            the path to the network
	 * @param agent
	 *            the agent whose adjacents we want
	 * @return the agents adjacent to the specified agent in the specified
	 *         network.
	 */
	public static List GetAdjacent(String networkPath, Object agent) {
		Network net = (Network) FindProjection(networkPath);
		List list = new ArrayList();
		if (net == null)
			return list;
		Iterable iter = net.getAdjacent(agent);
		if (iter instanceof Collection)
			list.addAll((Collection) iter);
		else {
			for (Object obj : iter) {
				list.add(obj);
			}
		}

		return list;
	}

	/**
	 * Gets the edges for which the specified agent is the target in the
	 * specified network. For a undirected net this is all the edges connected
	 * to the agent.
	 * 
	 * @param networkPath
	 *            the path to the network
	 * @param agent
	 *            the agent whose in edges we want
	 * @return the edges for which the specified agent is the target in the
	 *         specified network
	 */
	public static List<RepastEdge> GetInEdges(String networkPath, Object agent) {
		Network net = (Network) FindProjection(networkPath);
		List<RepastEdge> list = new ArrayList<RepastEdge>();
		if (net == null)
			return list;
		Iterable<RepastEdge> iter = net.getInEdges(agent);
		if (iter instanceof Collection)
			list.addAll((Collection) iter);
		else {
			for (RepastEdge obj : iter) {
				list.add(obj);
			}
		}
		return list;
	}

	/**
	 * Gets the edges for which the specified agent is the source in the
	 * specified network. For a undirected net this is all the edges connected
	 * to the agent.
	 * 
	 * @param networkPath
	 *            the path to the network
	 * @param agent
	 *            the agent whose in edges we want
	 * @return the edges for which the specified agent is the source in the
	 *         specified network
	 */
	public static List<RepastEdge> GetOutEdges(String networkPath, Object agent) {
		Network net = (Network) FindProjection(networkPath);
		List<RepastEdge> list = new ArrayList<RepastEdge>();
		if (net == null)
			return list;
		Iterable<RepastEdge> iter = net.getOutEdges(agent);
		if (iter instanceof Collection)
			list.addAll((Collection) iter);
		else {
			for (RepastEdge obj : iter) {
				list.add(obj);
			}
		}
		return list;
	}

	/**
	 * Gets the edges connected to the specified agent in the specified network.
	 * 
	 * @param networkPath
	 *            the path to the network
	 * @param agent
	 *            the agent whose in edges we want
	 * @return the edges connected to the specified agent in the specified
	 *         network.
	 */
	public static List<RepastEdge> GetEdges(String networkPath, Object agent) {
		Network net = (Network) FindProjection(networkPath);
		List<RepastEdge> list = new ArrayList<RepastEdge>();
		if (net == null)
			return list;
		Iterable<RepastEdge> iter = net.getEdges(agent);
		if (iter instanceof Collection)
			list.addAll((Collection) iter);
		else {
			for (RepastEdge obj : iter) {
				list.add(obj);
			}
		}

		return list;
	}

	public static Context AddAgentToContext(String parentContextPath,
			Object agent) {

		Context parentContext = FindContext(parentContextPath);
		parentContext.add(agent);
		return parentContext;

	}

	public static Context RemoveAgentFromContext(String parentContextPath,
			Object agent) {

		Context parentContext = FindContext(parentContextPath);
		parentContext.remove(agent);
		return parentContext;

	}
	
	public static Context RemoveAgentFromModel(Object agent) {

		Context parentContext = RunState.getSafeMasterContext();
		parentContext.remove(agent);
		return parentContext;

	}

	public static boolean MoveAgent(String projectionPath, Object agent,
			Number... newAgentLocation) {

		boolean success = false;
		Projection projection = FindProjection(projectionPath);
		if (projection instanceof Grid) {
			Grid grid = (Grid) projection;
			int[] newIntAgentLocation = new int[newAgentLocation.length];
			for (int i = 0; i < newIntAgentLocation.length; i++) {
				newIntAgentLocation[i] = ((Number) newAgentLocation[i]).intValue();
			}
			success = grid.moveTo(agent, newIntAgentLocation);
		} else {

			double[] newDoubleAgentLocation = new double[newAgentLocation.length];
			for (int i = 0; i < newDoubleAgentLocation.length; i++) {
				newDoubleAgentLocation[i] = ((Number) newAgentLocation[i]).doubleValue();
			}
			ContinuousSpace space = (ContinuousSpace) projection;
			success = space.moveTo(agent, newDoubleAgentLocation);
		}

		return success;

	}

	public static void MoveAgentByDisplacement(String projectionPath, Object agent,
			Number... displacement) {

		Projection projection = FindProjection(projectionPath);
		if (projection instanceof Grid) {
			Grid grid = (Grid) projection;
			int[] intDisplacement = new int[displacement.length];
			for (int i = 0; i < intDisplacement.length; i++) {
				intDisplacement[i] = ((Number) displacement[i]).intValue();
			}
			grid.moveByDisplacement(agent, intDisplacement);
		} else {

			double[] doubleDisplacement = new double[displacement.length];
			for (int i = 0; i < doubleDisplacement.length; i++) {
				doubleDisplacement[i] = ((Number) displacement[i]).doubleValue();
			}
			ContinuousSpace space = (ContinuousSpace) projection;
			space.moveByDisplacement(agent, doubleDisplacement);
		}

	}

	public static void MoveAgentByVector(String projectionPath, Object agent,
			Number distance, Number... anglesInRadians) {

		Projection projection = FindProjection(projectionPath);
		if (projection instanceof Grid) {
			Grid grid = (Grid) projection;
			double[] newAngles = new double[anglesInRadians.length];
			for (int i = 0; i < newAngles.length; i++) {
				newAngles[i] = ((Number) anglesInRadians[i]).doubleValue();
			}
			grid.moveByVector(agent, ((Number) distance).doubleValue(), newAngles);
		} else {

			double[] newAngles = new double[anglesInRadians.length];
			for (int i = 0; i < newAngles.length; i++) {
				newAngles[i] = ((Number) anglesInRadians[i]).doubleValue();
			}
			ContinuousSpace space = (ContinuousSpace) projection;
			space.moveByVector(agent, ((Number) distance).doubleValue(), newAngles);
		}

	}

	public static double GetTickCount() {

		return RunEnvironment.getInstance().getCurrentSchedule().getTickCount();

	}

	public static Amount GetTickCountInTimeUnits() {

		return RunEnvironment.getInstance().getCurrentSchedule()
				.getTickCountInTimeUnits();

	}

	public static ISchedulableAction ScheduleAction(Object agent,
			String methodName, Object... parameters) {
		return RepastEssentials.ScheduleAction(agent, 1, 0, methodName,
				parameters);
	}

	public static ISchedulableAction ScheduleAction(Object agent,
			double optionalStartTime, String methodName, Object... parameters) {
		return RepastEssentials.ScheduleAction(agent, optionalStartTime, 0,
				methodName, parameters);
	}

	public static ISchedulableAction ScheduleAction(Object agent,
			double optionalStartTime, double optionalRepeatInterval,
			String methodName, Object... parameters) {

		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		ISchedulableAction newAction;
		if (optionalRepeatInterval > 0) {
			newAction = schedule.schedule(ScheduleParameters.createRepeating(
					optionalStartTime, optionalRepeatInterval), agent,
					methodName, parameters);
		} else {
			newAction = schedule.schedule(ScheduleParameters
					.createOneTime(optionalStartTime), agent, methodName,
					parameters);
		}
		return newAction;

	}

	public static IAction CancelAction(ISchedulableAction actionToCancel) {

		RunEnvironment.getInstance().getCurrentSchedule().removeAction(
				actionToCancel);
		return actionToCancel;

	}

	public static void EndSimulationRun() {
		RunEnvironment.getInstance().endRun();
	}

	public static void EndSimulationRunAt(double tick) {
		RunEnvironment.getInstance().endAt(tick);
	}

	public static void PauseSimulationRun() {
		RunEnvironment.getInstance().pauseRun();
	}

	public static void PauseSimulationRunAt(double tick) {
		RunEnvironment.getInstance().pauseAt(tick);
	}

	private static String cleanXMLDataDescriptionFile(
			String xmlDataFileDescriptionFileName) {

		String tempFileName = xmlDataFileDescriptionFileName + ".temp";

		try {
			DeleteExternalFile(tempFileName);
			BufferedReader input = new BufferedReader(new FileReader(new File(
					xmlDataFileDescriptionFileName)));
			BufferedWriter output = new BufferedWriter(new FileWriter(new File(
					tempFileName)));
			String nextLine = input.readLine();
			while (nextLine != null) {
				nextLine = nextLine.replace("xmi:version=\"2.0\"", "");
				nextLine = nextLine.replace(
						"xmlns:xmi=\"http://www.omg.org/XMI\"", "");
				nextLine = nextLine.replace("xmlns=\"datadescriptor.xsd\"", "");
				nextLine = nextLine.replace("array=\"\"", "");
				nextLine = nextLine.replace("branch=\"\"", "");
				nextLine = nextLine.replace("colDelimiter=\"\"", "");
				nextLine = nextLine.replace("cols=\"\"", "");
				nextLine = nextLine.replace("columns=\"\"", "");
				nextLine = nextLine.replace("count=\"\"", "");
				nextLine = nextLine.replace("countType=\"\"", "");
				nextLine = nextLine.replace("data=\"\"", "");
				nextLine = nextLine.replace("dataType=\"\"", "");
				nextLine = nextLine.replace("dataTypeType=\"\"", "");
				nextLine = nextLine.replace("newlineType=\"\"", "");
				nextLine = nextLine.replace("newLineTypeType=\"\"", "");
				nextLine = nextLine.replace("pattern=\"\"", "");
				nextLine = nextLine.replace("record=\"\"", "");
				nextLine = nextLine.replace("rowDelimiter=\"\"", "");
				nextLine = nextLine.replace("rows=\"\"", "");
				nextLine = nextLine.replace("table=\"\"", "");
				nextLine = nextLine.replace("target=\"\"", "");
				nextLine = nextLine.replace("type=\"\"", "");
				output.write(nextLine);
				output.write(System.getProperty("line.separator"));
				nextLine = input.readLine();
			}
			output.flush();
			output.close();
			input.close();

		} catch (Exception e) {
			//msgCenter.error("Error cleaning external data description XML file"
			// , e);
		}
		return tempFileName;

	}

	public static boolean DeleteExternalFile(String fileName) {

		boolean results = false;

		try {
			results = (new File(fileName)).delete();
		} catch (Exception e) {
			// msgCenter.error("Error deleting external file", e);
		}

		return results;

	}

	public static boolean WriteExternalFile(Object source,
			String xmlDataFileDescriptionFileName, String targetFileName) {

		boolean results = false;

		DataFileWriter writer = new DataFileWriter(source);
		writer.setDestFileName(targetFileName);
		String tempFileName = cleanXMLDataDescriptionFile(xmlDataFileDescriptionFileName);
		writer.setDescriptorFileName(tempFileName);
		try {
			writer.write();
			results = true;
		} catch (Exception e) {
			// msgCenter.error("Error writing to external file", e);
		}
		if (results)
			DeleteExternalFile(tempFileName);
		return results;

	}

	public static boolean ReadExternalFile(Object target,
			String xmlDataFileDescriptionFileName, String sourceFileName) {

		boolean results = false;

		DataFileReader reader = new DataFileReader(target);
		reader.setFileToParseName(sourceFileName);
		String tempFileName = cleanXMLDataDescriptionFile(xmlDataFileDescriptionFileName);
		reader.setDescriptorFileName(tempFileName);
		try {
			reader.read();
			results = true;
		} catch (Exception e) {
			// msgCenter.error("Error reading from external file", e);
		}
		if (results)
			DeleteExternalFile(tempFileName);
		return results;

	}

	/**
	 * Gets the named parameter's value.
	 * 
	 * @param name
	 *            the name of the parameter
	 * @return the named parameter's value
	 */
	public static Object GetParameter(String name) {

		return RunEnvironment.getInstance().getParameters().getValue(name);

	}

	public static boolean RandomDrawAgainstThreshold(BigDecimal threshold) {

		return RepastEssentials.RandomDrawAgainstThreshold(threshold
				.doubleValue());

	}

	public static boolean RandomDrawAgainstThreshold(double threshold) {

		return RandomHelper.getUniform().nextDouble() > threshold;

	}

	public static double RandomDraw() {

		return RandomHelper.getUniform().nextDouble();

	}

	public static double RandomDraw(double lowerBound, double upperBound) {

		return RandomHelper.getUniform().nextDoubleFromTo(lowerBound,
				upperBound);

	}

	public static double RandomDraw(BigDecimal lowerBound, BigDecimal upperBound) {

		return RepastEssentials.RandomDraw(lowerBound.doubleValue(), upperBound
				.doubleValue());

	}

	public static int ExecuteProgram(String directory, String commandLine) {

		int results = 0;

		try {
			ProcessBuilder processBuilder = new ProcessBuilder(directory + "\\"
					+ commandLine);
			processBuilder.directory(new File(directory));
			Process process = processBuilder.start();
			DevNull errorDevNull = new DevNull(process.getErrorStream());
			DevNull outputDevNull = new DevNull(process.getInputStream());
			outputDevNull.start();
			errorDevNull.start();
			process.waitFor();
		} catch (Exception e) {
			msgCenter.error(commandLine, e);
		}
		return results;

	}

	public static String CallMATLAB(String command) {

		return LinkMatlab.evaluateMatlabFunction(command);

	}

	public static double GetMATLABValue(String command) {

		return LinkMatlab.getScalarValue(command);

	}

	public static void ResetMATLAB() {

		LinkMatlab.closeMatlab();
		LinkMatlab.openMatlab();

	}

	public static Query OrQuery(Query query1, Query query2) {
		return new OrQuery(query1, query2);
	}

	// public static Query AndQuery(Query query1, Query query2) {
	// return new ;
	// }
	//			
	// public static Query NotQuery(Context context, Query query) {
	//
	// public static Query NetPathWithin(network, node, 3) {
	// public static Query NetworkAdjacent(network,
	// node).query(optNestedQueryResult)",
	// public static Query NetworkPredecessor(network,
	// node).query(optNestedQueryResult)",
	// public static Query NetworkSuccessor(network,
	// node).query(optNestedQueryResult)",
	//	
	// public static Query GridWithin(grid, agent,
	// 3).query(optNestedQueryResult)",
	// public static Query MooreQuery(grid, agent, 3,
	// 3).query(optNestedQueryResult)",
	// public static Query VNQuery(grid, agent, 2, 1,
	// 1).query(optNestedQueryResult)",
	//	
	// public static Query ContinuousWithin(continuousSpace, agent,
	// 11.5).query(optNestedQueryResult)",
	//	
	// public static Query ContainsQuery(geography,
	// geometry).query(optNestedQueryResult)",
	// public static Query ItersectsQuery(geography,
	// geometry).query(optNestedQueryResult)",
	// public static Query GeographyWithin(geography,
	// geometry).query(optNestedQueryResult)",
	// public static Query TouchesQuery(geography,
	// geometry).query(optNestedQueryResult)",
	// public static Query WithinQuery(geography,
	// geometry).query(optNestedQueryResult)",
	//	
	// public static Query PropertyEquals(context,
	// \"name\", \"Nick\").query(optNestedQueryResult)",
	// public static Query PropertyGreaterThan(context,
	// \"wealth\", 3.14).query(optNestedQueryResult)",
	// public static Query PropertyLessThan(context,
	// \"wealth\", 3.14).query(optNestedQueryResult)",

	public static int[] bigDecimalArrayToIntArray(BigDecimal[] bigDecimalArray) {
		int[] intArray = new int[bigDecimalArray.length];
		for (int i = 0; i < bigDecimalArray.length; i++) {
			intArray[i] = bigDecimalArray[i].intValue();
		}
		return intArray;
	}

	public static double[] bigDecimalArrayToDoubleArray(
			BigDecimal[] bigDecimalArray) {
		double[] doubleArray = new double[bigDecimalArray.length];
		for (int i = 0; i < bigDecimalArray.length; i++) {
			doubleArray[i] = bigDecimalArray[i].doubleValue();
		}
		return doubleArray;
	}

}