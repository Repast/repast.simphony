/*CopyrightHere*/
package repast.simphony.freezedry.freezedryers.proj;

import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContextSpace;
import repast.simphony.space.Dimensions;
import repast.simphony.space.continuous.*;
import simphony.util.messages.MessageCenter;

import java.util.HashMap;
import java.util.Map;

/**
 * A projection layer that builds a {@link repast.simphony.space.continuous.ContinuousSpace}.
 * 
 * @author Jerry Vos
 */
public class ContinuousProjectionDryer extends ProjectionDryer<ContinuousSpace<?>> {

	public static final String TRANSLATOR_KEY = "translator";

	public static final String ADDER_KEY = "adder";

	public static final String AGENT_LOCATIONS_KEY = "agentLocations";

	public static final String SPACE_DIMENSIONS_KEY = "spaceDimensions";
	
	public static final String SPACE_ORIGIN_KEY = "spaceOrigin";

	private static final MessageCenter LOG = MessageCenter
			.getMessageCenter(ContinuousProjectionDryer.class);

	/**
	 * Stores the spaces's agent locations, dimensions, adder, and translator.
	 * 
	 * @param context
	 *            the ignored
	 * @param t
	 *            the space to store properties of
	 * @param map
	 *            the properties destination
	 */
	@Override
	protected void addProperties(Context<?> context, ContinuousSpace<?> t, Map<String, Object> map) {
		HashMap<Object, double[]> agentLocs = new HashMap<Object, double[]>();
		for (Object o : t.getObjects()) {
			double[] loc = new double[t.getDimensions().size()];
			t.getLocation(o).toDoubleArray(loc);
			agentLocs.put(o, loc);
		}

		map.put(AGENT_LOCATIONS_KEY, agentLocs);

		double[] dimensions = new double[t.getDimensions().size()];
		t.getDimensions().toDoubleArray(dimensions);
		map.put(SPACE_DIMENSIONS_KEY, dimensions);
		double[] origin = new double[t.getDimensions().size()];
		t.getDimensions().originToDoubleArray(origin);
		map.put(SPACE_ORIGIN_KEY, origin);

		map.put(ADDER_KEY, t.getAdder());
		map.put(TRANSLATOR_KEY, t.getPointTranslator());
	}

	/**
	 * Loads the space's agents. This also loads the space's adder, translator if they are stored in
	 * the given properties.
	 * 
	 * @param context
	 *            ignored
	 * @param proj
	 *            the space
	 * @param properties
	 *            the properties of the space
	 */
	@Override
	protected void loadProperties(Context<?> context, ContinuousSpace<?> proj,
			Map<String, Object> properties) {
		super.loadProperties(context, proj, properties);
		loadAgents(context, proj, properties);
	}

	protected void loadAgents(Context<?> context, ContinuousSpace proj,
			Map<String, Object> properties) {
		HashMap locations = (HashMap) properties.get(AGENT_LOCATIONS_KEY);

		if (locations == null) {
			LOG.info("Could not find any locations for context '" + context + "'.");
		}
		for (Object o : locations.keySet()) {
			if (locations.get(o) instanceof double[]) {
				double[] loc = (double[]) locations.get(o);
				proj.moveTo(o, loc);
			} else {
				LOG
						.warn("Object '"
								+ o
								+ "'s location did not resolve to a double, the object will not be placed on the space.");
			}
		}
	}
	

	/**
	 * Builds a {@link repast.simphony.space.continuous.DefaultContinuousSpace} with the projection's name and dimensions.
	 * 
	 * @param context
	 *            ignored
	 * @param properties
	 *            the properties of the space
	 */
	@Override
	protected ContinuousSpace<?> instantiate(Context<?> context, Map<String, Object> properties) {
		double[] dimensions = (double[]) properties.get(SPACE_DIMENSIONS_KEY);
		if (dimensions == null) {
			LOG.warn("Space's dimensions resolved to null for context '" + context
					+ "', returning null");
			return null;
		}
		double[] origin = (double[]) properties.get(SPACE_ORIGIN_KEY);
		if (origin == null) {
			LOG.warn("Space's origin resolved to null for context '" + context
					+ "', returning null");
			return null;
		}
		String name = (String) properties.get(NAME_KEY);

		ContinuousAdder adder = (ContinuousAdder) properties.get(ADDER_KEY);
		if (adder == null) {
			LOG.info("Space's adder resolved to null for context '" + context
					+ "', using default random adder");
      adder = new RandomCartesianAdder();
    }
		PointTranslator translator = (PointTranslator) properties.get(TRANSLATOR_KEY);
		if (translator == null) {
			LOG.info("Space's translator resolved to null for context '" + context
					+ "', using default sticky borders");
      translator = new StickyBorders();
    }
		translator.init(new Dimensions(dimensions, origin));

    ContextSpace space = new ContextSpace(name, adder, translator, dimensions, origin);

		return space;
	}

	/**
	 * Returns true for {@link ContinuousSpace}s.
	 */
	@Override
	public boolean handles(Class<?> type) {
		return ContinuousSpace.class.isAssignableFrom(type);
	}
}
