package repast.simphony.visualization.gis3D.style;

import java.awt.Color;

import gov.nasa.worldwind.render.SurfacePolyline;
import repast.simphony.space.graph.RepastEdge;

/**
 * Interface for network projection surface lines in the 3D GIS display.
 * 
 * @author Eric Tatara
 */
public interface NetworkStyleGIS extends StyleGIS<RepastEdge> {

	public SurfacePolyline getSurfaceShape(RepastEdge edge, SurfacePolyline shape);

	public Color getLineColor(RepastEdge edge);

	public double getLineOpacity(RepastEdge edge);

	public double getLineWidth(RepastEdge edge);

	// TODO GIS directed line arrow head / tail
	// TODO GIS line type, e.g. dashed or dotted
}
