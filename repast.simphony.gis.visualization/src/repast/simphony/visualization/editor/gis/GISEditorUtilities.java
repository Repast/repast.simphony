package repast.simphony.visualization.editor.gis;

import org.geotools.geometry.jts.JTS;
import org.locationtech.jts.geom.Coordinate;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;

/**
 * Utility methods for GIS editor classes.
 *
 * @author Nick Collier
 * @deprecated 2D piccolo based code is being removed
 */
public class GISEditorUtilities {

  /**
   * Calculates the buffer value to supply to Point.buffer()
   * that will approximate an 8x8 pixel area.
   *
   * @param coordinate     the picked coordinate
   * @param crs            the current CRS
   * @param metersPerPixel the number of meters per pixel
   * @return the buffer value to supply to Point.buffer()
   *         that will approximate an 8x8 pixel area.
   */
  public static double calcPointPickBuffer(Coordinate coordinate, CoordinateReferenceSystem crs,
                                           double metersPerPixel) {
    Coordinate coord = new Coordinate(coordinate.x + .001, coordinate.y);
    try {
      // should give us distance in meters for .001 along x-axis.
      double distance = JTS.orthodromicDistance(coordinate, coord, crs);
      double meterPer8pixels = metersPerPixel * 8;
      // should give us the number of ".001"s per 8 pixels, so buffering
      // by this amount should give us roughly an 8x8 pixel square.
      return meterPer8pixels / distance * .001;

    } catch (TransformException e) {
      e.printStackTrace();
    }

    return .001;
  }

}
