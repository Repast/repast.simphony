package repast.simphony.visualization.gis3D.style;

import java.awt.Color;

import gov.nasa.worldwind.render.SurfaceShape;

public class DefaultSurfaceShapeStyle<T> implements SurfaceShapeStyle<T> {

	@Override
	public SurfaceShape getSurfaceShape(T object, SurfaceShape shape) {
		return null;
	}

	@Override
	public Color getFillColor(T obj) {
		return Color.BLUE;
	}

	@Override
	public double getFillOpacity(T obj) {
		return 0.25;
	}

	@Override
	public Color getLineColor(T obj) {
		return Color.GREEN;
	}

	@Override
	public double getLineOpacity(T obj) {
		return 1.0;
	}

	@Override
	public double getLineWidth(T obj) {
		return 3;
	}
}