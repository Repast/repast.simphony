package repast.simphony.ws.gis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;

import cern.colt.Arrays;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.visualization.Layout;
import repast.simphony.ws.DisplayProperties;

public class StyledNetLayerGIS{

	protected Map<Object, DisplayProperties<RepastEdge<?>>> toBeAdded = new HashMap<>();
	protected Set<Object> toBeRemoved = new HashSet<>();
	protected Map<Object, DisplayProperties<RepastEdge<?>>> objMap = new HashMap<>();
	protected ServerNetStyleGIS style;
	protected String layerName;
	protected int layerid;
	protected Geography<?> geography;

	public StyledNetLayerGIS(ServerNetStyleGIS style, String layerName, int layerid ) {
		this.style = style;
		this.layerName = layerName;
		this.layerid = layerid;
	}

	public void setGeography(Geography<?> geography) {
		this.geography = geography;
	}

	public int getLayerId() {
		return layerid;
	}


	
	public void addObject(RepastEdge<?> obj, int id) {
		// remove it from the removed objects because
		// there might be a remove and then add in the same
		// update -- for example when a object switches contexts
		// the display is part of their parent context
		toBeRemoved.remove(obj);
		toBeAdded.put(obj, new DisplayProperties<RepastEdge<?>>(id, obj));
		
	}

	public void removeObject(RepastEdge<?> obj) {
		// if the object to remove is in addedObjects
		// we don't need to actually remove it because it
		// hasn't been added yet -- so just remove it from there
		// otherwise add it to the objects to remove.
		if (toBeAdded.remove(obj) == null) toBeRemoved.add(obj);
	}

	protected void processRemoved(StringBuilder builder) {
		builder.append("\"remove\": [");
		boolean first = true;
		for (Object obj : toBeRemoved) {
			DisplayProperties<RepastEdge<?>> props = objMap.remove(obj);
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

	protected void appendUpdate(StringBuilder builder, DisplayProperties<RepastEdge<?>> props, 
			double[] source_loc, double[] target_loc) {
		builder.append("{ \"id\":");
		builder.append(props.id);
		builder.append(", \"source\": ");
		builder.append(Arrays.toString(source_loc));
		
		builder.append(", \"target\": ");
		builder.append(Arrays.toString(target_loc));
		
		builder.append(", \"color\": ");

		// GIS color format is hex #xxxxxx
		builder.append(String.format("\"#%02x%02x%02x\"", props.color.getRed(), props.color.getGreen(),
				props.color.getBlue()));

		builder.append(", \"size\": ");
		builder.append(props.size);
		
		// TODO other styles - but need to check style change only, not send entire style each time.

		builder.append("}");

	}

	protected void updateFromIterable(StringBuilder builder, Layout layout, 
			Iterable<DisplayProperties<RepastEdge<?>>> iter) {
		
		boolean first = true;
		for (DisplayProperties<RepastEdge<?>> props : iter) {
			objMap.put(props.agent, props);
			if (!first) {
				builder.append(",");
			}

			RepastEdge<?> edge = props.agent;
			
			Object source = edge.getSource();
	  	Object target = edge.getTarget();
	  	
	  	if (source == null || target == null) continue;
	  	
	  	Geometry sourcegeom = geography.getGeometry(source);
	  	Geometry targetgeom = geography.getGeometry(target);

	  	Coordinate s_coord = sourcegeom.getCoordinate();
	  	Coordinate t_coord = targetgeom.getCoordinate();
	  	
	  	// TODO Currently assumes coords are degree lat lon.  We need to provide
	 	  //      some utils from r.s.gis. that can convert from the source CRS to WGS84.
	  	//
			// NOTE! Also y,x ordering!!!
	  	double[] source_loc = new double[] {s_coord.y, s_coord.x};
	  	double[] target_loc = new double[] {t_coord.y, t_coord.x};
	  	
			props.color = style.getColor(props.agent);
			props.size = style.getSize(props.agent);

			
			appendUpdate(builder, props, source_loc, target_loc);
			first = false;
		}
	}

	protected void processAdded(StringBuilder builder, Layout layout) {
		builder.append("\"add\": [");
		updateFromIterable(builder, layout, toBeAdded.values());
		builder.append("]");
		toBeAdded.clear();
	}

	protected void updateExisting(StringBuilder builder, Layout layout) {
		builder.append("\"update\": [");
		updateFromIterable(builder, layout, objMap.values());
		builder.append("],");
	}

	public void update(StringBuilder builder, Layout<?, ?> layout) {
		builder.append("{\"name\":\"");
		builder.append(layerName);
		builder.append("\", \"layer_id\":");
		builder.append(layerid);
		builder.append(", \"data\": {");
		processRemoved(builder);

		updateExisting(builder, layout);
		processAdded(builder, layout);
		builder.append("}}");
	}

	public void setStyle(ServerNetStyleGIS style) {
		this.style = style;
	}

	public String getName() {
		return layerName;
	}
}
