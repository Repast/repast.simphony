package repast.simphony.space.gis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.locationtech.jts.geom.Geometry;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Singleton class that creates FeatureAgentFactories.
 *
 * @author Nick Collier
 */
public class FeatureAgentFactoryFinder {

  private static FeatureAgentFactoryFinder instance = new FeatureAgentFactoryFinder();

  private Map<Class<?>, List<FeatureAttributeAdapter>> adapterMap =
          new HashMap<Class<?>, List<FeatureAttributeAdapter>>();

  /**
   * Returns the singleton instance of this finder.
   *
   * @return the singleton instance of this finder.
   */
  public static FeatureAgentFactoryFinder getInstance() {
    return instance;
  }

  /**
   * Clears any attribute adapters from this factory finder.
   */
  public void clearAdapters() {
    adapterMap.clear();
  }

  private FeatureAgentFactoryFinder() {
  }

  /**
   * Adds an adapter for the specified class. When objects of this class
   * are adapted to features, these additional adapters will be included.
   *
   * @param adapteeClass the class to add the adapter for
   * @param adapter      the adapter to add
   */
  public void addAdapter(Class<?> adapteeClass, FeatureAttributeAdapter adapter) {
    List<FeatureAttributeAdapter> list = adapterMap.get(adapteeClass);
    if (list == null) {
      list = new ArrayList<FeatureAttributeAdapter>();
      adapterMap.put(adapteeClass, list);
    }
    // only add the adapter if it doesn't already contain
    // an adapter for the feature name
    if (!containsAdapterFor(list, adapter.getAttributeName())) {
      list.add(adapter);
    }
  }

  // returns true if the list of feature attribute adapters contains an
  // adapter for the specified feature name
  private boolean containsAdapterFor(List<FeatureAttributeAdapter> list, String featureName) {
    for (FeatureAttributeAdapter adapter : list) {
      if (adapter.getAttributeName().equals(featureName)) return true;
    }

    return false;
  }

  /**
   * Gets a feature agent factory for the specified agent type.
   *
   * @param agentType
   * @param geometryType
   * @param crs
   * @return
   */
  public DefaultFeatureAgentFactory getFeatureAgentFactory(Class<?> agentType,
                                                           Class<? extends Geometry> geometryType,
                                                           CoordinateReferenceSystem crs) {
    List<FeatureAttributeAdapter> list = adapterMap.get(agentType);
    if (list == null) list = new ArrayList<FeatureAttributeAdapter>();
    return new DefaultFeatureAgentFactory(agentType, geometryType, crs, list);
  }

  /**
   * Gets a shapefile feature agent factory for the specified agent type.
   *
   * @param agentType
   * @param geometryType
   * @param crs
   * @return
   */
  public ShapefileFeatureAgentFactory getShapefileFeatureAgentFactory(Class<?> agentType,
                                                                      Class<? extends Geometry> geometryType,
                                                                      CoordinateReferenceSystem crs) {
    List<FeatureAttributeAdapter> list = adapterMap.get(agentType);
    if (list == null) list = new ArrayList<FeatureAttributeAdapter>();
    else {
      // remove the selected attribute from the adapters -- that's completely
      // synthetic and doesn't need to be written to the file.
      for (Iterator<FeatureAttributeAdapter> iter = list.iterator(); iter.hasNext();) {
        FeatureAttributeAdapter adapter = iter.next();
        if (adapter.getAttributeName().equals(GISConstants.SELECTED_ATTRIBUTE_NAME)) iter.remove();
      }
    }
    return new ShapefileFeatureAgentFactory(agentType, geometryType, crs, list);
  }


}
