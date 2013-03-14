package repast.simphony.gis.styleEditor;

import java.awt.Color;

import org.geotools.map.FeatureLayer;
import org.geotools.styling.Graphic;
import org.geotools.styling.Mark;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class StyleEditor {

	FeatureLayer layer;

	StyleBuilder builder = new StyleBuilder();

	Color fillColor;

	Color strokeColor;

	int strokeWidth;

	double strokeOpacity;

	double fillOpacity;

	String markName;

	int markSize;

	public StyleEditor(FeatureLayer layer, StyleBuilder builder) {
		this.layer = layer;
		this.builder = builder;
	}

	public void applyStyle() {
		Class geomtype = 
				layer.getFeatureSource().getSchema().getGeometryDescriptor().getType().getBinding();
		if (geomtype.equals(Point.class) || geomtype.equals(MultiPoint.class)) {
			Mark mark = builder.createMark(markName, fillColor, strokeColor,
					strokeWidth);
			Graphic graphic = builder.createGraphic(null, mark, null);
			PointSymbolizer symbol = builder.createPointSymbolizer(graphic);
			Style s = builder.createStyle(layer.getStyle().getName(), symbol);
			layer.setStyle(s);
		} else if (geomtype.equals(LineString.class)
				|| geomtype.equals(MultiLineString.class)) {
			Style style = builder.createStyle(layer.getStyle().getName(),
					builder.createLineSymbolizer(strokeColor, strokeWidth));
			layer.setStyle(style);
		} else if (geomtype.equals(Polygon.class)
				|| geomtype.equals(MultiPolygon.class)) {
			Style style = builder.createStyle(layer.getStyle().getName(),
					builder.createPolygonSymbolizer(fillColor, strokeColor,
							strokeWidth));
			layer.setStyle(style);
		}
	}

	public Color getFillColor() {
		return fillColor;
	}

	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}

	public double getFillOpacity() {
		return fillOpacity;
	}

	public void setFillOpacity(double fillOpacity) {
		this.fillOpacity = fillOpacity;
	}

	public String getMarkName() {
		return markName;
	}

	public void setMarkName(String markName) {
		this.markName = markName;
	}

	public int getMarkSize() {
		return markSize;
	}

	public void setMarkSize(int markSize) {
		this.markSize = markSize;
	}

	public Color getStrokeColor() {
		return strokeColor;
	}

	public void setStrokeColor(Color strokeColor) {
		this.strokeColor = strokeColor;
	}

	public double getStrokeOpacity() {
		return strokeOpacity;
	}

	public void setStrokeOpacity(double strokeOpacity) {
		this.strokeOpacity = strokeOpacity;
	}

	public int getStrokeWidth() {
		return strokeWidth;
	}

	public void setStrokeWidth(int strokeWidth) {
		this.strokeWidth = strokeWidth;
	}

}
