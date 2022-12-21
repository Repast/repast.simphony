package repast.simphony.gis.ui.probe;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;

import repast.simphony.space.gis.Geography;
import repast.simphony.ui.probe.LocationProbe;
import repast.simphony.ui.probe.Utils;

/**
 * Provides the location of probed objects in Geography projections.
 * 
 * @author Nick Collier
 */
public class GeographyLocationProbe implements LocationProbe{

  protected Object obj;
  protected Geography<?> geog;

  public GeographyLocationProbe(Object obj, Geography<?> geog) {
    this.obj = obj;
    this.geog = geog;
  }

  /**
   * Gets the property descriptor for the location property of this probe.
   * 
   * @return
   * @throws IntrospectionException
   */
  public PropertyDescriptor getLocationDescriptor() throws IntrospectionException {
    PropertyDescriptor pd = new PropertyDescriptor("location", this.getClass(), "getLocation", null);
    pd.setDisplayName(geog.getName() + " Location");
    return pd;
  }

  /**
   * Gets the objects location as a String.
   * 
   * @return the objects location as a String.
   */
  public String getLocation() {
    Geometry geom = geog.getGeometry(obj);
    if (geom == null) {
      return "N/A";
    } else {
      Point centroid = geom.getCentroid();
      if (centroid == null) {
        return "N/A";
      } else {
        Coordinate coordinate = centroid.getCoordinate();
        if (coordinate == null) {
          return "N/A";
        } else {
          String val = "";
          boolean needBreak = false;
          if ((!Double.isNaN(coordinate.x)) && (!Double.isInfinite(coordinate.x))) {
            val += Utils.getNumberFormatInstance().format(coordinate.x);
            needBreak = true;
          }
          if ((!Double.isNaN(coordinate.y)) && (!Double.isInfinite(coordinate.y))) {
            if (needBreak) {
              val += ", ";
              needBreak = false;
            }
            val += Utils.getNumberFormatInstance().format(coordinate.y);
            needBreak = true;
          }
          if ((!Double.isNaN(coordinate.z)) && (!Double.isInfinite(coordinate.z))) {
            if (needBreak) {
              val += ", ";
              needBreak = false;
            }
            val += Utils.getNumberFormatInstance().format(coordinate.z);
          }
          return val;
        }
      }
    }
  }
}