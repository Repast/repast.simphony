package repast.simphony.space.gis;

import com.vividsolutions.jts.geom.Geometry;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.geotools.referencing.FactoryFinder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.cs.DefaultCartesianCS;
import org.geotools.referencing.factory.FactoryGroup;
import org.geotools.referencing.operation.DefaultCoordinateOperationFactory;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchIdentifierException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.GeographicCRS;
import org.opengis.referencing.crs.ProjectedCRS;
import org.opengis.referencing.cs.CartesianCS;
import org.opengis.referencing.operation.CoordinateOperation;
import org.opengis.referencing.operation.MathTransformFactory;
import org.opengis.referencing.operation.TransformException;
import simphony.util.messages.MessageCenter;

import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Finds the UTM CRS appropriate to a specified lon, lat location.
 *
 * @author Michelle Kelherer
 */
public class UTMFinder {

  private static final MessageCenter center = MessageCenter.getMessageCenter(UTMFinder.class);

  private static DefaultCoordinateOperationFactory cFactory = new DefaultCoordinateOperationFactory();
  private static CoordinateReferenceSystem wgs84 = DefaultGeographicCRS.WGS84;

  /**
   * Determine the zone for the given lat/lon
   *
   * @param lat The Latitude
   * @param lon The Longitude
   * @return The zone
   */
  public static short determineZone(double lat, double lon) {
    if (lon < -180 || lon > 180) throw new IllegalArgumentException("Lon value must be between -180 and 180");
    lon += 180;
    double val = lon / 6;
    if (val == 0 || (val == (int)val && val % 2 == 1)) {
      val++;
    }
    short zn = (short)Math.ceil(val);
    return (short) ((lat < 0) ? -zn : zn);
  }

  /**
   * Gets the UTM appropriate for the specified geometry in the specified
   * CoordinateReferenceSystem. The UTM will be for the coordinate that is
   * returned by geom.getCoordinate().
   *
   * @param geom the geometry
   * @param crs the geometry's coordinate reference system
   * @return the appropriate UTM.
   */
  public static CoordinateReferenceSystem getUTMFor(Geometry geom, CoordinateReferenceSystem crs) {
    try {
      if (!crs.equals(wgs84)) {
        CoordinateOperation op = cFactory.createOperation(crs, wgs84);
        geom = JTS.transform(geom, op.getMathTransform());
      }
    } catch (FactoryException e) {
      center.error("Error during crs conversion", e);
    } catch (TransformException e) {
      center.error("Error during geometry transform", e);
    }
    // default wgs84 (lon, lat)
    return getUTMfor(geom.getCoordinate().y, geom.getCoordinate().x);
  }

  /**
   * Gets the UTM CRS for the specified lat and lon.
   *
   * @param lat the latitude
   * @param lon the longitude
   * @return Gets the UTM CRS for the specified lat and lon.
   */
  public static CoordinateReferenceSystem getUTMfor(double lat, double lon) {
    return getUTMfor(determineZone(lat, lon));
  }


  public static CoordinateReferenceSystem getUTMfor(short zone) {
    FactoryGroup factories = new FactoryGroup(null);
    GeographicCRS geoCRS = DefaultGeographicCRS.WGS84;
    MathTransformFactory mtFactory = FactoryFinder.getMathTransformFactory(null);
    CartesianCS cartCS = DefaultCartesianCS.GENERIC_2D;
    ParameterValueGroup parameters = null;
    try {

      parameters = mtFactory.getDefaultParameters("Transverse_Mercator");
      parameters.parameter("central_meridian").setValue((6 * Math.abs(zone)) - 183);
      parameters.parameter("latitude_of_origin").setValue(0.0);
      parameters.parameter("scale_factor").setValue(0.9996);
      double northing = zone > 0 ? 0.0 : 10000000.0;
      parameters.parameter("false_easting").setValue(500000.0);
      parameters.parameter("false_northing").setValue(northing);
    } catch (NoSuchIdentifierException e) {
      center.error("Projection parameters not found creating UTM zone: " + zone, e);
      return null;
    }

    Map properties = Collections.singletonMap("name", "WGS 84 / UTM Zone " + zone + (zone > 0 ? "N" : "S"));
    ProjectedCRS projCRS = null;
    try {
      projCRS = factories.createProjectedCRS(properties, geoCRS, null, parameters, cartCS);
    } catch (FactoryException e) {
      center.error("Error creating Projected CRS for UTM zone: " + zone, e);
    }
    return projCRS;
  }


}
