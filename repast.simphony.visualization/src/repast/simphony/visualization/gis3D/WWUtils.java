package repast.simphony.visualization.gis3D;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.layers.CompassLayer;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.LayerList;
import gov.nasa.worldwind.layers.placename.PlaceNameLayer;

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
	
	public static void insertBeforeCompass(WorldWindow wwd, Layer layer)
  {
      // Insert the layer into the layer list just before the compass.
      int compassPosition = 0;
      LayerList layers = wwd.getModel().getLayers();
      for (Layer l : layers)
      {
          if (l instanceof CompassLayer)
              compassPosition = layers.indexOf(l);
      }
      layers.add(compassPosition, layer);
  }

  public static void insertBeforePlacenames(WorldWindow wwd, Layer layer)
  {
      // Insert the layer into the layer list just before the placenames.
      int compassPosition = 0;
      LayerList layers = wwd.getModel().getLayers();
      for (Layer l : layers)
      {
          if (l instanceof PlaceNameLayer)
              compassPosition = layers.indexOf(l);
      }
      layers.add(compassPosition, layer);
  }

  public static void insertAfterPlacenames(WorldWindow wwd, Layer layer)
  {
      // Insert the layer into the layer list just after the placenames.
      int compassPosition = 0;
      LayerList layers = wwd.getModel().getLayers();
      for (Layer l : layers)
      {
          if (l instanceof PlaceNameLayer)
              compassPosition = layers.indexOf(l);
      }
      layers.add(compassPosition + 1, layer);
  }

  public static void insertBeforeLayerName(WorldWindow wwd, Layer layer, String targetName)
  {
      // Insert the layer into the layer list just before the target layer.
      int targetPosition = 0;
      LayerList layers = wwd.getModel().getLayers();
      for (Layer l : layers)
      {
          if (l.getName().indexOf(targetName) != -1)
          {
              targetPosition = layers.indexOf(l);
              break;
          }
      }
      layers.add(targetPosition, layer);
  }
}
