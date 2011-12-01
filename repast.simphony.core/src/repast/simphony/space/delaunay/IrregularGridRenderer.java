package repast.simphony.space.delaunay;

import java.awt.Color;
import java.awt.Graphics;

/**
 * 
 *
 * @author Howe
 */
public class IrregularGridRenderer {

	/**
	 * 
	 */
	public IrregularGridRenderer() {
		super();
	}
	
	public void drawPoint(TriangulationPoint p, Graphics g, Color clPoint) {
		g.setColor(clPoint);
		g.fillOval((int) p.getX() - 3, (int) p.getY() - 3, 6, 6);
	}

	public void drawGraph(DelaunayGraph graph, Graphics g, Color dcl, Color vcl, Color hcl,
			boolean drawD, boolean drawV, boolean drawH) {
		if (drawD || drawH) {
			for (int i = 0; i < graph.edges.size(); i += 4) {
				QuadEdge e = graph.edges.get(i);

				g.setColor(dcl);
				if (e.isValidEdge()) {
					if (e.getLNext().isValidEdge()
							&& e.getRNext().isValidEdge()) {
						if (drawD) // draw Delaunay Triangulation
							g.drawLine((int) e.getOrg().x, (int) e.getOrg().y,
									(int) e.getDest().x, (int) e.getDest().y);
					} else {
						if (drawH) // draw Convex Hull
							g.setColor(hcl);
						g.drawLine((int) e.getOrg().x, (int) e.getOrg().y,
								(int) e.getDest().x, (int) e.getDest().y);
					}
				}
			}
		}

		if (drawV) { // draw Voronoi Diagram
			graph.getVoronoiDiagram();
			g.setColor(vcl);
			for (int i = 1; i < graph.edges.size(); i += 4) {
				QuadEdge e = graph.edges.get(i);
				if (e.isValidEdge())
					g.drawLine((int) e.getOrg().x, (int) e.getOrg().y, (int) e
							.getDest().x, (int) e.getDest().y);
			}
		}
	}
}
