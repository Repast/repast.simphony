package repast.simphony.visualization.gis3D.style;

import java.awt.Color;

import gov.nasa.worldwind.render.SurfacePolyline;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.visualization.editedStyle.EditedEdgeStyleData;
import repast.simphony.visualization.editedStyle.EditedStyleUtils;

public class EditedNetworkStyleGIS implements NetworkStyleGIS {

	private EditedEdgeStyleData<?> edgeData;

	public EditedNetworkStyleGIS(String fileName) {
		edgeData = EditedStyleUtils.getEdgeStyle(fileName);
	}

	@Override
	public SurfacePolyline getSurfaceShape(RepastEdge edge, SurfacePolyline shape) {
  	return new SurfacePolyline();
	}

	@Override
	public Color getLineColor(RepastEdge edge) {
		return EditedStyleUtils.getColor(edgeData, edge);
	}

	@Override
	public double getLineOpacity(RepastEdge edge) {
		return 1.0;
	}

	@Override
	public double getLineWidth(RepastEdge edge) {
		return EditedStyleUtils.getSize(edgeData, edge);
	}
}
