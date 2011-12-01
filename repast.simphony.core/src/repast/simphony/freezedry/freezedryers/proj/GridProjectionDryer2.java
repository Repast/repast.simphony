/*CopyrightHere*/
package repast.simphony.freezedry.freezedryers.proj;

import repast.simphony.context.Context;
import repast.simphony.context.space.grid.ContextGrid;
import repast.simphony.space.grid.*;
import simphony.util.messages.MessageCenter;

import java.util.HashMap;
import java.util.Map;

/**
 * A projection layer that builds a {@link repast.simphony.space.continuous.ContinuousSpace}.
 *
 * @author Jerry Vos
 */
public class GridProjectionDryer2 extends ProjectionDryer<Grid<?>> {

  public static final String TRANSLATOR_KEY = "translator";

  public static final String ADDER_KEY = "adder";
  public static final String CELL_ACCESSOR_KEY = "CELL_ACCESSOR";

  public static final String AGENT_LOCATIONS_KEY = "agentLocations";

  public static final String SPACE_DIMENSIONS_KEY = "spaceDimensions";
  
  public static final String SPACE_ORIGIN_KEY = "spaceOrigin";

  private static final MessageCenter LOG = MessageCenter
          .getMessageCenter(GridProjectionDryer2.class);

  /**
   * Stores the spaces's agent locations, dimensions, adder, and translator.
   *
   * @param context the ignored
   * @param t       the space to store properties of
   * @param map     the properties destination
   */
  @Override
  protected void addProperties(Context<?> context, Grid<?> t, Map<String, Object> map) {
    HashMap<Object, int[]> agentLocs = new HashMap<Object, int[]>();
    for (Object o : t.getObjects()) {
      int[] loc = new int[t.getDimensions().size()];
      t.getLocation(o).toIntArray(loc);
      agentLocs.put(o, loc);
    }

    map.put(AGENT_LOCATIONS_KEY, agentLocs);

    int[] dimensions = new int[t.getDimensions().size()];
    t.getDimensions().toIntArray(dimensions);
    map.put(SPACE_DIMENSIONS_KEY, dimensions);
    int[] origin = new int[t.getDimensions().size()];
    t.getDimensions().originToIntArray(origin);
    map.put(SPACE_ORIGIN_KEY, dimensions);
    
    map.put(ADDER_KEY, t.getAdder());
    map.put(TRANSLATOR_KEY, t.getGridPointTranslator());
    map.put(CELL_ACCESSOR_KEY, t.getCellAccessor());
  }

  /**
   * Loads the space's agents. This also loads the space's adder, translator if they are stored in
   * the given properties.
   *
   * @param context    ignored
   * @param proj       the space
   * @param properties the properties of the space
   */
  @Override
  protected void loadProperties(Context<?> context, Grid<?> proj,
                                Map<String, Object> properties) {
    super.loadProperties(context, proj, properties);
    loadAgents(context, proj, properties);
  }

  protected void loadAgents(Context<?> context, Grid proj,
                            Map<String, Object> properties) {
    HashMap locations = (HashMap) properties.get(AGENT_LOCATIONS_KEY);

    if (locations == null) {
      LOG.info("Could not find any locations for context '" + context + "'.");
    }
    for (Object o : locations.keySet()) {
      if (locations.get(o) instanceof int[]) {
        int[] loc = (int[]) locations.get(o);
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
   * @param context    ignored
   * @param properties the properties of the space
   */
  @Override
  protected DefaultGrid<?> instantiate(Context<?> context, Map<String, Object> properties) {
    int[] dimensions = (int[]) properties.get(SPACE_DIMENSIONS_KEY);
    if (dimensions == null) {
      LOG.warn("Space's dimensions resolved to null for context '" + context
              + "', returning null");
      return null;
    }
    int[] origin = (int[]) properties.get(SPACE_ORIGIN_KEY);
    if (origin == null) {
      LOG.warn("Space's origin resolved to null for context '" + context
              + "', returning null");
      return null;
    }
    String name = (String) properties.get(NAME_KEY);

    GridAdder adder = (GridAdder) properties.get(ADDER_KEY);
    if (adder == null) {
      LOG.info("Space's adder resolved to null for context '" + context
              + "', using default random adder");
      adder = new RandomGridAdder();
    }

    GridPointTranslator translator = (GridPointTranslator) properties.get(TRANSLATOR_KEY);
    if (translator == null) {
      LOG.info("Space's translator resolved to null for context '" + context
              + "' using default strict borders");
      translator = new StrictBorders();
    }

    CellAccessor accessor = (CellAccessor) properties.get(CELL_ACCESSOR_KEY);
    if (accessor == null) {
      LOG.info("Space's cell accessor resolved to null for context '" + context
              + "', using default single occupancy accessor");
      accessor = new SingleOccupancyCellAccessor();
    }
    translator.init(new GridDimensions(dimensions,origin));

    ContextGrid grid = new ContextGrid(name, adder, translator, accessor, dimensions, origin);
    return grid;
  }

  /**
   * Returns true for {@link repast.simphony.space.continuous.ContinuousSpace}s.
   */
  @Override
  public boolean handles(Class<?> type) {
    return Grid.class.isAssignableFrom(type);
	}
}
