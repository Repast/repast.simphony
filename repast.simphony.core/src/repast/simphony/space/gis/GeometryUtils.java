package repast.simphony.space.gis;

import javax.measure.unit.SI;

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.operation.DefaultCoordinateOperationFactory;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Utilities for GIS operations
 * 
 * @author Eric Tatara
 * @author Nick Collier
 *
 */
public class GeometryUtils {

	private static DefaultCoordinateOperationFactory cFactory = new DefaultCoordinateOperationFactory();
	
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
}
