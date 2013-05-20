package repast.simphony.visualization.gis3D.style;

import gov.nasa.worldwind.render.SurfaceShape;

import java.awt.Color;

/**
 * Interface for surface shapes (lines, polygons) in the 3D GIS display.
 * 
 * @author Eric Tatara
 *
 * @param <T> the agent type
 */
public interface SurfaceShapeStyle<T> extends StyleGIS<T> {

	public SurfaceShape getSurfaceShape(T object, SurfaceShape shape);

	public Color getFillColor(T obj);

	public double getFillOpacity(T obj);

	public Color getLineColor(T obj);

	public double getLineOpacity(T obj);

	public double getLineWidth(T obj);

}
