package repast.simphony.visualization.network;

import java.util.Iterator;

import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Group;
import javax.media.j3d.Shape3D;
import javax.vecmath.Point3f;

import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.space.projection.ProjectionEvent;
import repast.simphony.space.projection.ProjectionListener;
import repast.simphony.visualization.Layout;
import repast.simphony.visualization.visualization3D.AbstractDisplayLayer3D;
import repast.simphony.visualization.visualization3D.Display3D;
import repast.simphony.visualization.visualization3D.Label;
import repast.simphony.visualization.visualization3D.ShapeFactory;
import repast.simphony.visualization.visualization3D.VisualItem3D;
import repast.simphony.visualization.visualization3D.style.DefaultEdgeStyle3D;
import repast.simphony.visualization.visualization3D.style.EdgeStyle3D;
import repast.simphony.visualization.visualization3D.style.TaggedAppearance;
import repast.simphony.visualization.visualization3D.style.TaggedBranchGroup;

/**
 * @author Nick Collier
 */
public class NetworkDisplayLayer3D extends AbstractDisplayLayer3D  implements ProjectionListener {

	private Display3D display;
	private EdgeStyle3D edgeStyle;
	private boolean isDirected = false;

	public NetworkDisplayLayer3D(Network<?> net, EdgeStyle3D style, Group topGroup, Display3D display) {
		super(style, topGroup);
		this.edgeStyle = style;
		this.display = display;
		if (style == null) this.style = new DefaultEdgeStyle3D();

		net.addProjectionListener(this);

		for (RepastEdge edge : net.getEdges()) {
			objsToAdd.add(edge);
		}

	}

	public EdgeStyle3D getEdgeStyle() {
		return edgeStyle;
	}

	protected void createItemsForAddedObjects(Layout layout, boolean doLayout) {
		for (Object obj : objsToAdd) {
			VisualItem3D item = createVisualItem(obj);
			visualItemMap.put(obj, item);
			adder.addItemForAddition(item);
		}

		objsToAdd.clear();
	}

	protected VisualItem3D createVisualItem(Object obj) {
		VisualItem3D item;
		if (edgeStyle.getEdgeType() == EdgeStyle3D.EdgeType.LINE) {
			TaggedBranchGroup rep = new TaggedBranchGroup("_LINE_EDGE_");
			BranchGroup group = rep.getBranchGroup();
			Shape3D shape;
			shape = ShapeFactory.createLine(1f, "_LINE_EDGE_");
			group.addChild(shape);
			if (isDirected) item = new DirectedLineEdgeVisualItem(rep, obj, createLabel(), edgeStyle);
			else item = new LineEdgeVisualItem3D(rep, obj, createLabel());
			TaggedAppearance attrib = style.getAppearance(obj, null, shape.getUserData());
			item.setShapeAppearance(shape, attrib);

		} else {
			TaggedBranchGroup rep = style.getBranchGroup(obj, null);
			if (isDirected) item = new DirectedEdgeVisualItem(rep, obj, createLabel(), edgeStyle);
			else item = new EdgeVisualItem(rep, obj, createLabel());
			for (Iterator<Shape3D> iter = item.shapes(); iter.hasNext();) {
				Shape3D shape = iter.next();
				TaggedAppearance attrib = style.getAppearance(obj, null, shape.getUserData());
				item.setShapeAppearance(shape, attrib);
			}
		}


		return item;
	}

	protected Label createLabel() {
		return new EdgeLabel();
		//return new NullLabel();
	}

	public void doUpdate(Layout layout, boolean layoutPerformed) {
		for (VisualItem3D item : visualItemMap.values()) {
			RepastEdge edge = (RepastEdge) item.getVisualizedObject();

			item.updateTaggedBranchGroup(edgeStyle);
			item.updateAppearance(edgeStyle);
			item.updateLabel(edgeStyle);
			item.updateScale(style);

			VisualItem3D source = display.getVisualObject(edge.getSource());
			VisualItem3D target = display.getVisualObject(edge.getTarget());

			if (source.hasMoved() || target.hasMoved() || adder.isNew(item)) {
				// layout the edge again
				Point3f pt1 = source.getLocation();
				Point3f pt2 = target.getLocation();

				float sourceRadius;
				float targetRadius;

				// get the radii of the bounding spheres for the source and target.  The
				// edge will be drawn beetween these points.  Scale the radii by the scale
				// of the node style if available, otherwise scale by unit length.
				float scale = 1.0f;
				if (source.getScale() != null)
					scale = source.getScale()[0];
				else 
					scale = 1.0f;

				sourceRadius = scale * (float)((BoundingSphere)source.getTaggedBranchGroup().getBranchGroup()
						.getBounds()).getRadius();

				if (target.getScale() != null)
					scale = target.getScale()[0];
				else 
					scale = 1.0f;

				targetRadius = scale * (float)((BoundingSphere)target.getTaggedBranchGroup().getBranchGroup()
						.getBounds()).getRadius();

				item.updateLocation(pt1, sourceRadius, pt2, targetRadius);
			}
		}
	}

	public void setNetworkDirected(boolean directed) {
		isDirected = directed;
	}

	public void projectionEventOccurred(ProjectionEvent evt) {
		if (evt.getType() == ProjectionEvent.EDGE_ADDED) {
			addObject((RepastEdge) evt.getSubject());
		} else if (evt.getType() == ProjectionEvent.EDGE_REMOVED) {
			removeObject((RepastEdge) evt.getSubject());
		}
	}
}