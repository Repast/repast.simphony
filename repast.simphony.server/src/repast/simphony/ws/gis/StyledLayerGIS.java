package repast.simphony.ws.gis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;

import repast.simphony.space.gis.Geography;
import repast.simphony.visualization.Layout;
import repast.simphony.ws.DisplayProperties;

/**
 * Layer for GIS Display Server.
 * 
 * Note that GIS displays use a NullLayout since the geography context is directly
 *   queried for the agent positions.
 *   
 * @author Eric Tatara
 *
 */
public class StyledLayerGIS {

	protected Map<Object, DisplayProperties<?>> toBeAdded = new HashMap<>();
	protected Set<Object> toBeRemoved = new HashSet<>();
	
	protected Set<Object> toBeMoved = new HashSet<>();
	
	protected Map<Object, DisplayProperties<?>> objMap = new HashMap<>();
	
	protected ServerStyleGIS style;
	protected String layerName;
	protected int layerid;
	protected Geography<?> geography;
	
  // The layer symbol type: wkt, line, or polygon. Default is circle marker.
	protected String symbolType = "circle";  

	public StyledLayerGIS(ServerStyleGIS style, String layerName, int layerid) {
		this.layerName = layerName;
		this.layerid = layerid;
		setStyle(style);
	}

	public void setGeography(Geography<?> geography) {
		this.geography = geography;
	}

	public int getLayerId() {
		return layerid;
	}
	
	public void moveObject(Object obj) {
			toBeMoved.add(obj);
	}

	public void addObject(Object obj, int id) {
		// remove it from the removed objects because
		// there might be a remove and then add in the same
		// update -- for example when a object switches contexts
		// the display is part of their parent context
		toBeRemoved.remove(obj);
		toBeAdded.put(obj, new DisplayProperties(id, obj));
	}

	public void removeObject(Object obj) {
		// if the object to remove is in addedObjects
		// we don't need to actually remove it because it
		// hasn't been added yet -- so just remove it from there
		// otherwise add it to the objects to remove.
		if (toBeAdded.remove(obj) == null) toBeRemoved.add(obj);
		
		toBeMoved.remove(obj);
	}

	protected void processRemoved(StringBuilder builder) {
		builder.append("\"remove\": [");
		boolean first = true;
		for (Object obj : toBeRemoved) {
			DisplayProperties<?> props = objMap.remove(obj);
			if (props != null) {
				if (!first) {
					builder.append(",");
				}
				builder.append(props.id);
				first = false;
			}
		}
		builder.append("],");
		toBeRemoved.clear();
	}
	
	protected void moveFromIterable(StringBuilder builder, Iterable<Object> iter) {
		
		boolean first = true;
		for (Object obj : iter) {
			Geometry geom = geography.getGeometry(obj);
			
			DisplayProperties<?> props = objMap.get(obj);
			
			if (!first) {
				builder.append(",");
			}
			first = false;

			builder.append("{");                           // agent start
			builder.append(" \"id\":").append(props.id);   // number val json with no quotes
			builder.append(", ");
			processGeomCoordinates(builder, geom.getCoordinates());
			builder.append("}");                          // agent end
		}
	}

	protected void updateFromIterable(StringBuilder builder, 
			Iterable<DisplayProperties<?>> iter, boolean add) {
		
		boolean first = true;
		for (DisplayProperties<?> props : iter) {
			Geometry geom = geography.getGeometry(props.agent);
			if (geom == null) continue;
			
			if (add) {
				objMap.put(props.agent, props);
			}
			if (!first) {
				builder.append(",");
			}
			first = false;

			props.color = style.getColor(props.agent);
			props.size = style.getSize(props.agent);
				
			builder.append("{");                           // agent start
			builder.append(" \"id\":").append(props.id);   // number val json with no quotes
			
			// Only provide a new latlon when the agent has moved
			if (add) {
				builder.append(", ");
				processGeomCoordinates(builder, geom.getCoordinates());
			}
			
			builder.append(", \"color\": ");

			// GIS color format is hex #xxxxxx
			builder.append(String.format("\"#%02x%02x%02x\"", props.color.getRed(), 
					props.color.getGreen(), props.color.getBlue()));

			// TODO other styles - but need to check style change only, not send entire style each time.

			builder.append("}");                          // agent end
			
		}
	}

	protected void processGeomCoordinates(StringBuilder builder, Coordinate[] coords ) {
		
		// TODO Currently assumes coords are degree lat lon.  We need to provide
		//      some utils from r.s.gis. that can convert from the source CRS to WGS84.
		
		// Geometry coord arrays in the form  "pt" : [[lat, lon],[lat, lon],...]
		// Point shapes will only have a single coord pair.
		// Lines and polygons will have a coord pair sequence.
		// Note that the array isn't in quotes

		builder.append("\"latlons\": [");
		boolean first = true;
		
		// Each coord pair is [lat,lon]
		for (Coordinate c : coords) {
			if (!first) {
				builder.append(",");
			}
			// Note y,x order
			builder.append("[").append(c.y).append(",").append(c.x).append("]");
			first = false;
		}
		builder.append("]");
	}
	
	protected void processAdded(StringBuilder builder) {
		builder.append("\"add\": [");
		updateFromIterable(builder, toBeAdded.values(), true);
		builder.append("]");
		toBeAdded.clear();
	}

	protected void updateExisting(StringBuilder builder) {
		builder.append("\"update\": [");
		updateFromIterable(builder, objMap.values(), false);
		builder.append("],");
	}
	
	protected void processMoved(StringBuilder builder){
		builder.append(",\"move\": [");
		moveFromIterable(builder, toBeMoved);
		builder.append("]");
		
		toBeMoved.clear();
	}

	public void update(StringBuilder builder, Layout<?, ?> layout) {
		builder.append("{\"name\":\"");
		builder.append(layerName);
		builder.append("\", \"layer_id\":");
		builder.append(layerid);
		builder.append(", \"data\": {");
		
		processRemoved(builder);
		updateExisting(builder);
		processAdded(builder);
		processMoved(builder);
		builder.append("}}");
	}

	public void setStyle(ServerStyleGIS style) {
		this.style = style;
		
		this.symbolType = style.getWellKnownText();
	}

	public String getName() {
		return layerName;
	}
	
	public String getSymbolType() {
		return symbolType;
	}
}
