package repast.simphony.visualization.gis3D;

import gov.nasa.worldwind.geom.LatLon;

import java.util.ArrayList;
import java.util.List;

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.opengis.spatialschema.geometry.MismatchedDimensionException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

public class WWUtils {

	public static LatLon CoordToLatLon(Coordinate coord){
		
		// TODO HACK
		// prevent conversion error for cases where lon might be slightly larger than 180.
		if (coord.x > 180)
			coord.x = 180;
		
		return LatLon.fromDegrees(coord.y,coord.x);
	}
	
	public static List<LatLon> CoordToLatLon(Coordinate[] coords){
		
		List<LatLon> latlon = new ArrayList<LatLon>();
		
		for (Coordinate coord : coords)
		  latlon.add(LatLon.fromDegrees(coord.y,coord.x));
		
		return latlon;
	}
	
	public static Geometry projectGeometryToWGS84(Geometry geom, 
			CoordinateReferenceSystem sourceCRS){
		
		return projectGeometry(geom, sourceCRS, DefaultGeographicCRS.WGS84);
	}
	
	public static Geometry projectGeometry(Geometry geom, 
			CoordinateReferenceSystem sourceCRS, CoordinateReferenceSystem targetCRS){
		
		MathTransform transform = null; 
		
		try {
			transform = CRS.findMathTransform(sourceCRS, targetCRS, true);
			
			return JTS.transform(geom, transform); 
			
		} catch (FactoryException e) {
		  // TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MismatchedDimensionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return null;
	}
}
