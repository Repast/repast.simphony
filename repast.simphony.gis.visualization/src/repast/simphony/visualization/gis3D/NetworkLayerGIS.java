package repast.simphony.visualization.gis3D;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.render.BasicShapeAttributes;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.ShapeAttributes;
import gov.nasa.worldwind.render.SurfacePolyline;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.space.projection.ProjectionEvent;
import repast.simphony.space.projection.ProjectionListener;
import repast.simphony.visualization.gis3D.style.NetworkStyleGIS;

/**
 * Style display layer for network projections in GIS.
 * 
 * @author Eric Tatara
 *
 */
public class NetworkLayerGIS extends AbstractRenderableLayer<NetworkStyleGIS<RepastEdge<?>>,SurfacePolyline> implements ProjectionListener {

	public NetworkLayerGIS(String name, NetworkStyleGIS<RepastEdge<?>> style,
			Network<?> network) {
		
		super(name, style);
		
		network.addProjectionListener(this);

		for (RepastEdge edge : network.getEdges()) {
			addedObjects.add(edge);
		}
	}

	@Override
	protected void applyUpdatesToShape(Object o) {
		RepastEdge<?> edge = (RepastEdge<?>)o;
		
		SurfacePolyline line = getVisualItem(edge);
		
		Object source = edge.getSource();
  	Object target = edge.getTarget();
  	
  	if (source == null || target == null) return;
  	
  	Geometry sourcegeom = geography.getGeometry(source);
  	Geometry targetgeom = geography.getGeometry(target);
  	
  	if (sourcegeom == null || targetgeom == null) return;
  	
  	// TODO GIS optimize for speed if location not changed
  	
   	List<LatLon> pts = new ArrayList<LatLon>();

   	pts.add(WWUtils.CoordToLatLon(sourcegeom.getCoordinate()));
   	pts.add(WWUtils.CoordToLatLon(targetgeom.getCoordinate()));
  	
   	ShapeAttributes attrs = line.getAttributes();
    
    if (attrs == null)
      attrs = new BasicShapeAttributes();

    attrs.setOutlineMaterial(new Material(style.getLineColor(edge)));
    attrs.setOutlineOpacity(style.getLineOpacity(edge));
    attrs.setOutlineWidth(style.getLineWidth(edge));

    // TODO get the stipple pattern from the style
//    attrs.setOutlineStippleFactor(1);
//    attrs.setOutlineStipplePattern(pattern);
    
    line.setAttributes(attrs);
	}
	
  protected SurfacePolyline createVisualItem(Object o) {
  	RepastEdge<?> edge = (RepastEdge<?>)o;
  	
  	SurfacePolyline line = style.getSurfaceShape(edge,null);
  	
    visualItemMap.put(edge, line);

    return line;
  }

	@Override
	public void projectionEventOccurred(ProjectionEvent evt) {
		if (evt.getType() == ProjectionEvent.EDGE_ADDED) {
			addObject((RepastEdge) evt.getSubject());
		} else if (evt.getType() == ProjectionEvent.EDGE_REMOVED) {
			removeObject((RepastEdge) evt.getSubject());
		}
	}
}