package repast.simphony.gis.util;

import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;

/**
 * Utilities methods for geometries
 *
 * @author Nick Collier
 * @version $Revision: 1.2 $ $Date: 2007/04/18 19:25:53 $
 */
public class GeometryUtil {

  public static enum GeometryType {
    POINT, LINE, POLYGON, OTHER
  }
  
  /**
   * Finds the geometry type of the specified com.vividsolutions.jts.geom class.
   *
   * @param geom the com.vividsolutions.jts.geom class whose type we want.
   * @return the geometry type of the specified com.vividsolutions.jts.geom class.
   */
  public static GeometryType findGeometryType(Class geomClass) {
    if (com.vividsolutions.jts.geom.Point.class.equals(geomClass) || 
    		com.vividsolutions.jts.geom.MultiPoint.class.equals(geomClass))
      return GeometryType.POINT;
    
    else if (com.vividsolutions.jts.geom.Polygon.class.equals(geomClass) || 
    		     com.vividsolutions.jts.geom.MultiPolygon.class.equals(geomClass))
      return GeometryType.POLYGON;
    
    else if (com.vividsolutions.jts.geom.LineString.class.equals(geomClass) || 
    		     com.vividsolutions.jts.geom.MultiLineString.class.equals(geomClass))
      return GeometryType.LINE;
    
    else return GeometryType.OTHER;
  }

  /**
   * Finds the geometry type of the specified geometry.
   *
   * @param geom the geometry whose type we want.
   * @return the geometry type of the specified geometry.
   */
  public static GeometryType findGeometryType(Geometry geom) {
  	if (geom instanceof com.vividsolutions.jts.geom.Point || geom instanceof MultiPoint)
      return GeometryType.POINT;
    else if (geom instanceof com.vividsolutions.jts.geom.Polygon || geom instanceof MultiPolygon)
      return GeometryType.POLYGON;
    else if (geom instanceof LineString || geom instanceof MultiLineString)
      return GeometryType.LINE;
    else return GeometryType.OTHER;
  }

  /**
   * Finds the geometry type associated with a style.
   *
   * @param style the style to check
   * @return the geometry type associated with a style.
   */
  public static GeometryType findGeometryType(Style style) {
    if (SLD.pointSymbolizer(style) != null) return GeometryType.POINT;
    if (SLD.lineSymbolizer(style) != null) return GeometryType.LINE;
    if (SLD.polySymbolizer(style) != null) return GeometryType.POLYGON;
    return GeometryType.OTHER;
  }

  /**
   * Finds the geometry type of the specified feature.
   *
   * @param feature the feature whose type we want.
   * @return the geometry type of the specified feature.
   */
  public static GeometryType findGeometryType(SimpleFeature feature) {
    Object geom = feature.getDefaultGeometry();
    if (geom instanceof com.vividsolutions.jts.geom.Point || geom instanceof MultiPoint)
      return GeometryType.POINT;
    else if (geom instanceof com.vividsolutions.jts.geom.Polygon || geom instanceof MultiPolygon)
      return GeometryType.POLYGON;
    else if (geom instanceof LineString || geom instanceof MultiLineString)
      return GeometryType.LINE;
    else return GeometryType.OTHER;
  }
}
