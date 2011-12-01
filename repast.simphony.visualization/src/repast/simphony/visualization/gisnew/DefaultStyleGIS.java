package repast.simphony.visualization.gisnew;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Stroke;

import repast.simphony.visualization.gisnew.GeoShape.FeatureType;

/**
 * 
 * @author Eric tatara
 *
 * @param <T>
 */
public class DefaultStyleGIS<T> implements StyleGIS<T> {

	public FeatureType getFeatureType(T obj) {
		return FeatureType.POINT;
	}
	public GeoShape getShape(T obj) {		
		return GeoShapeFactory.createCircle(250);
	}
	public Stroke getBorder(T obj) {
		return new BasicStroke(1.0f);
	}
	public Color getBorderColor(T obj) {
		return Color.RED;
	}
	public double getBorderOpacity(T obj) {
		return 0.5;
	}
	public Color getFillColor(T obj) {
		return Color.RED;
	}
	public double getFillOpacity(T obj) {
		return 0.5;
	}
	public String getLabel(T obj) {
		return null;
	}
	public Color getLabelColor(T obj) {
		return null;
	}
	public Font getLabelFont(T obj) {
		return null;
	}
	public float getRotation(T obj) {
		return 0;
	}
	public float getScale(T obj) {
		return 0;
	}
	public boolean isScaled(T object) {
		return false;
	}
}