package repast.simphony.gis.util;

import java.util.ArrayList;
import java.util.List;

import javax.measure.unit.SI;

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.operation.DefaultCoordinateOperationFactory;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.shape.random.RandomPointsBuilder;

import cern.jet.random.Uniform;
import cern.jet.random.engine.RandomEngine;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.gis.UTMFinder;

/**
 * Utilities methods for geometries
 *
 * @author Nick Collier
 * @author Eric Tatara
 */
public class GeometryUtil {

	private static DefaultCoordinateOperationFactory cFactory = new DefaultCoordinateOperationFactory();
	
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
  
  /**
	 * Generates a Geometry that represents a buffer zone around the Geometry 
	 * geom argument.  An automatic conversion of the geometry CRS is done to UTM
	 * if neccessary.  The distance argument is in SI meters and represent the 
	 * distance from the edge of the geometry to the border. 
	 * 
	 * @param geography the geography
	 * @param geom the geometry for which to create a buffer
	 * @param distance the distance in meters between the geometry and the boundary
	 * 
	 * @return the buffer geometry
	 */
	public static Geometry generateBuffer(Geography geography, Geometry geom, double distance){
		boolean convert = !geography.getUnits(0).equals(SI.METER);

		CoordinateReferenceSystem utm = null;
		Geometry buffer = null;
		CoordinateReferenceSystem crs = geography.getCRS();
		Geometry g2 = geom;

		try {
			// convert p to UTM
			if (convert) {
				utm = UTMFinder.getUTMFor(geom, crs);
				g2 = JTS.transform(geom, cFactory.createOperation(crs, utm).getMathTransform());
			}

			buffer = g2.buffer(distance);

			// convert buffer back to geography's crs.
			if (convert) {
				buffer = JTS.transform(buffer, cFactory.createOperation(utm, crs).getMathTransform());
			}
		} catch (FactoryException e) {
			e.printStackTrace();
		} catch (TransformException e) {
			e.printStackTrace();
		}

		return buffer;
	}
	
	/**
	 * Generate a collection of random coordinates that fall within the provided
	 *   mask geometry.  The default Repast uniform distribution instance is use 
	 *   for random number generation and can be controlled with the model seed.
	 *   
	 * @param mask the mask Geometry (border) to enclose random points
	 * @param numPoints the number of random points to generate
	 * 
	 * @return a collection of random coordinates
	 */
	public static List<Coordinate> generateRandomPointsInPolygon(Geometry mask, 
			int numPoints){

		return generateRandomPointsInPolygon(mask, numPoints, null);
	}

	/**
	 * Generate a collection of random coordinates that fall within the provided
	 *   mask geometry.  The RandomEngine instance is use for random number generation.
	 *   
	 * @param mask the mask Geometry (border) to enclose random points
	 * @param numPoints the number of random points to generate
	 * @param generator the random generator engine
	 * @return a collection of random coordinates
	 */
	public static List<Coordinate> generateRandomPointsInPolygon(Geometry mask, 
			int numPoints, RandomEngine generator){
		List<Coordinate> list = new ArrayList<Coordinate>();
		
		Uniform u = null;
		
		if (generator == null)
			u = RandomHelper.getUniform();
		else
			u = new Uniform(generator);
		
		final Uniform uniform = u;
		
		RandomPointsBuilder builder = new RandomPointsBuilder() {	
			
			@Override
			protected Coordinate createRandomCoord(Envelope env){
		    double x = env.getMinX() + env.getWidth() * uniform.nextDouble();
		    double y = env.getMinY() + env.getHeight() * uniform.nextDouble();
		    return createCoord(x, y);
		  }
		};
		
		builder.setExtent(mask);
		builder.setNumPoints(numPoints);
    
		MultiPoint mtPoint = (MultiPoint)builder.getGeometry();

		for (Coordinate coord : mtPoint.getCoordinates()){
		   list.add(coord);
		}

		return list;		
	}
}
