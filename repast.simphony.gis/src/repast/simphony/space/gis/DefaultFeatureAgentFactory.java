package repast.simphony.space.gis;

import java.beans.IntrospectionException;
import java.util.List;

import org.geotools.feature.SchemaException;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import simphony.util.messages.MessageCenter;

/**
 * Default factory class for creating feature agents.
 * Instances of the this class can be created using the FeatureAgentFactoryFinder.
 * 
 * @author Nick Collier
 * @author Eric Tatara
 *
 */
public class DefaultFeatureAgentFactory<T> extends FeatureAgentFactory<T> {
	MessageCenter msg = MessageCenter.getMessageCenter(getClass());

	private CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;

	private Class<? extends Geometry> geometryType = Point.class;

	private SimpleFeatureType featureType;

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
  public SimpleFeatureType getFeatureType() {
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

  private void init(Class<T> agentType) {
  	try {
			featureType = getFeatureType(agentType, crs, geometryType, adapters);
		} catch (IntrospectionException e) {
			msg.error("Unable to introspect feature class: "
					+ agentType.getName(), e);
		} catch (SchemaException e) {
			msg.error("Error creating FeatureType", e);
		}
  	createClassAttributes(agentType);
	}

  /**
   * Create a FeatureAgent instance from the provided agent.
   * 
   * @param agent the agent instance from which to create a FeatureAgent
   * @param geography the geography in which the FeatureAgent resides
   * @return the FeatureAgent instance
   */
  @Override
	public FeatureAgent getFeature(T agent, Geography geography) {
		FeatureAgent<T> featureAgent = new FeatureAgent<T>(featureType, agent, 
				geography, adapters, classAttributeList);
		return featureAgent;
	}
}
