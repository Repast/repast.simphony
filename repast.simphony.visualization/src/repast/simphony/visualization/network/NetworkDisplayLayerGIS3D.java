package repast.simphony.visualization.network;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.geom.LatLon;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.space.projection.ProjectionEvent;
import repast.simphony.space.projection.ProjectionListener;
import repast.simphony.visualization.gis3D.AbstractDisplayLayerGIS3D;
import repast.simphony.visualization.gis3D.DisplayGIS3D;
import repast.simphony.visualization.gis3D.EdgeCylinder;
import repast.simphony.visualization.gis3D.RenderableShape;
import repast.simphony.visualization.gis3D.WWUtils;
import repast.simphony.visualization.gis3D.style.DefaultEdgeStyleGIS3D;
import repast.simphony.visualization.gis3D.style.EdgeStyleGIS3D;

/**
 * 
 * @author Eric Tatara
 *
 */
public class NetworkDisplayLayerGIS3D extends AbstractDisplayLayerGIS3D implements ProjectionListener{

	private EdgeStyleGIS3D style;
	private boolean isDirected = false;

	public NetworkDisplayLayerGIS3D(Network<?> net, EdgeStyleGIS3D style, 
			DisplayGIS3D display, WorldWindow wwglCanvas, String name) {

		if (style == null) 
			this.style = new DefaultEdgeStyleGIS3D();
		else
			this.style = style;
		
		init(name, wwglCanvas);
		
		net.addProjectionListener(this);

		for (RepastEdge edge : net.getEdges()) {
			addedObjects.add(edge);
		}
	}

	@Override
	protected void applyUpdatesToNode(RenderableShape node) {

		RepastEdge edge = (RepastEdge)shapeToObjectMap.get(node);

		Object source = edge.getSource();
		Object target = edge.getTarget();
		
		if (geography.getGeometry(source) == null || 
				geography.getGeometry(target) == null)
			return;
				
		LatLon sourceLatLon = WWUtils.CoordToLatLon(geography.getGeometry(source).getCoordinate());
		LatLon targetLatLon = WWUtils.CoordToLatLon(geography.getGeometry(target).getCoordinate());

		((EdgeCylinder)node).setSourceLatLon(sourceLatLon);
		((EdgeCylinder)node).setTargetLatLon(targetLatLon);
		((EdgeCylinder)node).setRadius(style.edgeRadius(edge));
		
		node.setMaterial(style.getMaterial(edge, node.getMaterial()));
	}

	@Override
	protected RenderableShape createVisualItem(Object o){
		RepastEdge edge = (RepastEdge)o;
		
		Object source = edge.getSource();
		Object target = edge.getTarget();
				
		if (geography.getGeometry(source) == null || 
				geography.getGeometry(target) == null)
			return null;
		
		LatLon sourceLatLon = WWUtils.CoordToLatLon(geography.getGeometry(source).getCoordinate());
		LatLon targetLatLon = WWUtils.CoordToLatLon(geography.getGeometry(target).getCoordinate());		
		RenderableShape node = new EdgeCylinder(sourceLatLon, targetLatLon);
		((EdgeCylinder)node).setRadius(style.edgeRadius(edge));
		node.setMaterial(style.getMaterial(edge, node.getMaterial()));
		visualItemMap.put(o, node);
		
		return node;
	}

	public void setStyle(EdgeStyleGIS3D style) {
		this.style = style;
	}

	public void projectionEventOccurred(ProjectionEvent evt) {
		if (evt.getType() == ProjectionEvent.EDGE_ADDED) {
			addObject((RepastEdge) evt.getSubject());
		} else if (evt.getType() == ProjectionEvent.EDGE_REMOVED) {
			removeObject((RepastEdge) evt.getSubject());
		}
	}
}
