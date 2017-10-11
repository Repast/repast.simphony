/*CopyrightHere*/
package repast.simphony.gis.freezedry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Geometry;

import repast.simphony.context.Context;
import repast.simphony.context.space.gis.GeographyFactoryFinder;
import repast.simphony.freezedry.freezedryers.proj.ProjectionDryer;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.gis.GeographyParameters;
import simphony.util.messages.MessageCenter;

/**
 * A projection layer that builds a Geography projection
 *
 * @author Jerry Vos
 * @author Eric Tatara
 */
public class GeographyProjectionDryer extends ProjectionDryer<Geography<?>> {

	public static final String COVERAGES_KEY = "coverages";
  public static final String AGENT_LOCATIONS_KEY = "agentLocations";
  public static final String CRS_KEY = "crs";

  private static final MessageCenter LOG = MessageCenter.getMessageCenter(GeographyProjectionDryer.class);

  /**
   * Stores the spaces's agent locations, dimensions, adder, and translator.
   *
   * @param context the ignored
   * @param geog       the space to store properties of
   * @param map     the properties destination
   */
  @Override
  protected void addProperties(Context<?> context, Geography<?> geog, Map<String, Object> map) {
    HashMap<Object, Geometry> agentLocs = new HashMap<Object, Geometry>();
    for (Object o : geog.getAllObjects()) {
      Geometry geometry = geog.getGeometry(o);

      agentLocs.put(o, geometry.getClass().cast(geometry));
    }
    map.put(AGENT_LOCATIONS_KEY, agentLocs);
    
    CoordinateReferenceSystem crs = geog.getCRS();
    if (!crs.equals(DefaultGeographicCRS.WGS84)) map.put(CRS_KEY, crs.toWKT());
    
    map.put(NAME_KEY, geog.getName());
    
    Map<String,GridCoverage2D> coverageMap = new HashMap<String,GridCoverage2D>();
    
    for (String coverageName : geog.getCoverageNames()) {
    	coverageMap.put(coverageName, geog.getCoverage(coverageName));
    }
    map.put(COVERAGES_KEY, coverageMap);
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
  protected void loadProperties(Context<?> context, Geography<?> proj,
                                Map<String, Object> properties) {
    super.loadProperties(context, proj, properties);
    loadAgents(context, proj, properties);
  }

  protected void loadAgents(Context<?> context, Geography proj,
                            Map<String, Object> properties) {
    HashMap locations = (HashMap) properties.get(AGENT_LOCATIONS_KEY);

    if (locations == null) {
      LOG.info("Could not find any locations for context '" + context + "'.");
    }
    for (Object o : locations.keySet()) {
      if (locations.get(o) instanceof Geometry) {
        Geometry loc = (Geometry) locations.get(o);
        proj.move(o, loc);
      } else {
        LOG.warn("Object '"
                        + o
                        + "'s location did not resolve to a geometry, the object will not be placed in the geography.");
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
  protected Geography<?> instantiate(Context<?> context, Map<String, Object> properties) {
    String crs = (String) properties.get(CRS_KEY);

    String name = (String) properties.get(NAME_KEY);
    GeographyParameters geoParams = new GeographyParameters();
    Geography geography = GeographyFactoryFinder.createGeographyFactory(null)
            .createGeography(name, context, geoParams);
    if (crs != null) geography.setCRS(crs);
    return geography;
  }

  /**
   * Returns true for {@link repast.simphony.space.continuous.ContinuousSpace}s.
   */
  @Override
  public boolean handles(Class<?> type) {
    return Geography.class.isAssignableFrom(type);
	}
}