package repast.simphony.dataLoader.ui.wizard.builder;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.util.HashMap;

import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;

public class NetworkLayer extends PLayer {
	private BasicStroke stroke;

	private HashMap<PNode, HashMap<PNode, PPath>> sourceDestEdgeMap;
	
	public NetworkLayer(BasicStroke stroke) {
		this.stroke = stroke;
		this.sourceDestEdgeMap = new HashMap<PNode, HashMap<PNode, PPath>>();
	}

	public void addRelationship(PNode node1, PNode node2, double strength) {
		PPath edge = null;
		if (node1 == node2) {
			edge = PPath.createEllipse((float) node1.getBounds().getCenterX(),
					(float) node1.getBounds().getCenterY(), 30, 30);
			edge.setPaint(new Color(1,1,1,1));
			//edge.setTransparency(1);
		} else {
			line.setLine(node1.getBounds().getCenter2D().getX(), node1
					.getBounds().getCenter2D().getY(), node2.getBounds()
					.getCenter2D().getX(), node2.getBounds().getCenter2D()
					.getY());
			edge = new PPath(line);
		}
		edge.setStroke(stroke);
		this.addChild(edge);
		edge.repaint();
		getDestEdgeMap(node1).put(node2, edge);
	}
	
	private HashMap<PNode, PPath> getDestEdgeMap(PNode source) {
		if (sourceDestEdgeMap.get(source) == null) {
			sourceDestEdgeMap.put(source, new HashMap<PNode, PPath>());
		}
		return sourceDestEdgeMap.get(source);
	}

	public Stroke getStroke() {
		return stroke;
	}

	Line2D line = new Line2D.Float();

//	class PEdge extends PPath {
//		PNode start;
//
//		PNode end;
//
//		public PEdge(PNode node1, PNode node2, double strength) {
//			start = node1;
//			end = node2;
//			line.setLine(node1.getBounds().getCenter2D().getX(), node1
//					.getBounds().getCenter2D().getY(), node2.getBounds()
//					.getCenter2D().getX(), node2.getBounds().getCenter2D()
//					.getY());
//			this.setPathTo(line);
//			this.setPaint(Color.BLACK);
//		}
//
//		@Override
//		public void paint(PPaintContext context) {
////			System.out.println("painting");
//			super.paint(context);
//		}
//	}

	public void removeRelationship(PNode source, PNode target) {
		removeChild(this.sourceDestEdgeMap.get(source).get(target));
		this.sourceDestEdgeMap.get(source).remove(target);
	}
}
