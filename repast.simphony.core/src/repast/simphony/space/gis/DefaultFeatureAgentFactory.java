package repast.simphony.space.gis;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.FeatureType;
import org.geotools.feature.SchemaException;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import simphony.util.messages.MessageCenter;

import java.beans.IntrospectionException;
import java.util.List;

/**
 * Default factory class for creating feature agents.
 * Instances of the this class
 * can be created using the FeatureAgentFactoryFinder.
 *
 */
public class DefaultFeatureAgentFactory<T> extends FeatureAgentFactory {
	MessageCenter msg = MessageCenter.getMessageCenter(getClass());

	private CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;

	private Class<? extends Geometry> geometryType = Point.class;

	private FeatureType featureType;

	private FeatureCollection collection;
  private List<FeatureAttributeAdapter> adapters;

  DefaultFeatureAgentFactory(Class<T> agentType, Class<? extends Geometry> geometryType,
                                    CoordinateReferenceSystem crs, List<FeatureAttributeAdapter> adapters) {
    this.geometryType = geometryType;
    this.crs = crs;
    this.adapters = adapters;
    init(agentType);
	}

  /**
   * Gets the created feature type.
   *
   * @return the created feature type.
   */
  public FeatureType getFeatureType() {
    return featureType;
  }

  /**
   * Gets the coordinate reference system for this factory.
   *
   * @return the coordinate reference system for this factory.
   */
  public CoordinateReferenceSystem getCrs() {
    return crs;
  }

  /**
   * Resets this factory by creating a new feature collection.
   */
  public void reset() {
    collection = FeatureCollections.newCollection();
  }

  private void init(Class<T> agentType) {
		try {
			featureType = getFeatureType(agentType, crs, geometryType, adapters);
			collection = FeatureCollections.newCollection();
		} catch (IntrospectionException e) {
			msg.error("Unable to introspect feature class: "
					+ agentType.getName(), e);
		} catch (SchemaException e) {
			msg.error("Error creating FeatureType", e);
		}
	}

	public FeatureAgent2 getFeature(T agent, Geography geography) {
		FeatureAgent2<T> featureAgent = new FeatureAgent2<T>(featureType, agent, geography, adapters);
		featureAgent.setParent(collection);
		collection.add(featureAgent);
		return featureAgent;
	}

	public FeatureCollection getFeatures() {
		return collection;
	}
}
