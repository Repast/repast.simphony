package repast.simphony.visualization.gis3D;

import java.util.ArrayList;
import java.util.List;

import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Sector;
import gov.nasa.worldwind.layers.CompassLayer;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.LayerList;
import gov.nasa.worldwind.layers.placename.PlaceNameLayer;

/**
 * Utilities for GIS visualization in WorldWind.  The WWJ display uses a WGS84
 *   coordinate system, so all returned objects are converted to WGS84 lat/lon
 *   degrees irrespective of the original objects CRS.
 * 
 * @author Eric Tatara
 *
 */
public class WWUtils {

	public static LatLon CoordToLatLon(Coordinate coord){
		return LatLon.fromDegrees(coord.y,coord.x);
	}

	// TODO GIS - assumes coords are in degrees lat/lon - need to generalize
	// TODO GIS - assumes coords are in lon, lat order - need to generalizes

	/**
	 * Convert a coordinate array to a list of WWJ LatLon
	 * 
	 * @param coords
	 * @return
	 */
	public static List<LatLon> CoordToLatLon(Coordinate[] coords){

		List<LatLon> latlon = new ArrayList<LatLon>();

		for (Coordinate coord : coords)
			latlon.add(LatLon.fromDegrees(coord.y,coord.x));

		return latlon;
	}

	/**
	 * Project the given Geometry to WGS84
	 * 
	 * @param geom the geometry to project
	 * @param sourceCRS the source CRS
	 * @return
	 */
	public static Geometry projectGeometryToWGS84(Geometry geom, 
			CoordinateReferenceSystem sourceCRS){

		return projectGeometry(geom, sourceCRS, DefaultGeographicCRS.WGS84);
	}

	/**
	 * Projectthe given Geometry and CRS to a target CRS
	 * 
	 * @param geom
	 * @param sourceCRS
	 * @param targetCRS
	 * @return
	 */
	public static Geometry projectGeometry(Geometry geom, 
			CoordinateReferenceSystem sourceCRS, CoordinateReferenceSystem targetCRS){

		MathTransform transform = null; 

		try {
			transform = CRS.findMathTransform(sourceCRS, targetCRS, true);

			return JTS.transform(geom, transform); 

		} catch (FactoryException e) {
			e.printStackTrace();
		} catch (MismatchedDimensionException e) {
			e.printStackTrace();
		} catch (TransformException e) {
			e.printStackTrace();
		} 

		return null;
	}

	/**
	 * Create a WWJ Sector in WGS84 CRS from a GeoTools ReferencedEnvelope
	 * 
	 * @param envelope the ReferencedEnvelope in any CRS
	 * @return the WWJ Sector in WGS84
	 *
	 */
	public static Sector envelopeToSectorWGS84(ReferencedEnvelope envelope){

		CoordinateReferenceSystem crs = envelope.getCoordinateReferenceSystem();
		
		// Transform envelope coords to WGS84 if not already.
		if (crs != DefaultGeographicCRS.WGS84){
			try {
				envelope = envelope.transform(DefaultGeographicCRS.WGS84, true);
			} catch (TransformException | FactoryException e) {
				e.printStackTrace();
			}
		}

		crs = envelope.getCoordinateReferenceSystem();
		
		// The DefaultGeographicCRS.WGS84 axis order should always be EAST,NORTH;
		return Sector.fromDegrees(envelope.getMinY(), envelope.getMaxY(), 
				envelope.getMinX(), envelope.getMaxX());
		
//		// CRS axis order is EAST,NORTH (Lon,Lat)
//		if (crs.getCoordinateSystem().getAxis(0).getDirection() == AxisDirection.EAST) {
//			return Sector.fromDegrees(envelope.getMinY(), envelope.getMaxY(), 
//					envelope.getMinX(), envelope.getMaxX());
//		}
//		// CRS axis order is NORTH,EAST (Lat,Lon)
//		else {
//			return Sector.fromDegrees(envelope.getMinX(), envelope.getMaxX(),
//					envelope.getMinY(), envelope.getMaxY());
//		}
	}

	public static void insertBeforeCompass(WorldWindow wwd, Layer layer){
		// Insert the layer into the layer list just before the compass.
		int compassPosition = 0;
		LayerList layers = wwd.getModel().getLayers();
		for (Layer l : layers){
			if (l instanceof CompassLayer)
				compassPosition = layers.indexOf(l);
		}
		layers.add(compassPosition, layer);
	}

	public static void insertBeforePlacenames(WorldWindow wwd, Layer layer){
		// Insert the layer into the layer list just before the placenames.
		int compassPosition = 0;
		LayerList layers = wwd.getModel().getLayers();
		for (Layer l : layers){
			if (l instanceof PlaceNameLayer)
				compassPosition = layers.indexOf(l);
		}
		layers.add(compassPosition, layer);
	}

	public static void insertAfterPlacenames(WorldWindow wwd, Layer layer){
		// Insert the layer into the layer list just after the placenames.
		int compassPosition = 0;
		LayerList layers = wwd.getModel().getLayers();
		for (Layer l : layers){
			if (l instanceof PlaceNameLayer)
				compassPosition = layers.indexOf(l);
		}
		layers.add(compassPosition + 1, layer);
	}

	public static void insertBeforeLayerName(WorldWindow wwd, Layer layer, String targetName){
		// Insert the layer into the layer list just before the target layer.
		int targetPosition = 0;
		LayerList layers = wwd.getModel().getLayers();
		for (Layer l : layers){
			if (l.getName().indexOf(targetName) != -1){
				targetPosition = layers.indexOf(l);
				break;
			}
		}
		layers.add(targetPosition, layer);
	}
}
