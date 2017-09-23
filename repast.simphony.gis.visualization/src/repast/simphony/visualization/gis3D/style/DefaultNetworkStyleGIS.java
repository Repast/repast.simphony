package repast.simphony.visualization.gis3D.style;

import java.awt.Color;

import gov.nasa.worldwind.render.SurfacePolyline;
import repast.simphony.space.graph.RepastEdge;

public class DefaultNetworkStyleGIS implements NetworkStyleGIS {

	@Override
	public SurfacePolyline getSurfaceShape(RepastEdge edge, SurfacePolyline shape) {
		 return new SurfacePolyline();
	}

	@Override
	public Color getLineColor(RepastEdge edge) {
		return Color.RED;
	}

	@Override
	public double getLineOpacity(RepastEdge edge) {
		return 1.0;
	}

	@Override
	public double getLineWidth(RepastEdge edge) {
		return edge.getWeight();
	}
}
