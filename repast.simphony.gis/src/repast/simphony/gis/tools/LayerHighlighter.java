package repast.simphony.gis.tools;

import java.awt.Color;

import org.geotools.map.MapLayer;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Graphic;
import org.geotools.styling.Mark;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyleFactoryFinder;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class LayerHighlighter {
	static Color highlightColor = new Color((float) (127 / 256),
			(float) (255 / 256), 0f, .5f);

	Style style;

	FeatureTypeStyle[] oldStyle;

	MapLayer layer;

	StyleFactory fac = StyleFactoryFinder.createStyleFactory();

	StyleBuilder builder = new StyleBuilder();

	public LayerHighlighter(MapLayer layer) {
		this.layer = layer;
		style = layer.getStyle();
		Class geomType = layer.getFeatureSource().getSchema()
				.getDefaultGeometry().getType();
		if (geomType.equals(Polygon.class)
				|| geomType.equals(MultiPolygon.class)) {
			processPolygon(style);
		} else if (geomType.equals(LineString.class)
				|| geomType.equals(MultiLineString.class)) {
			processLine(style);
		} else if (geomType.equals(Point.class)
				|| geomType.equals(MultiPoint.class)) {
			processPoint(style);
		}
	}

	public void removeHighlight() {
		style.setFeatureTypeStyles(oldStyle);
	}

	private void processPoint(Style style2) {
		Mark mark = builder.createMark("circle", highlightColor);
		Graphic graphic = builder.createGraphic(null, mark, null);
		FeatureTypeStyle fts = builder.createFeatureTypeStyle(builder
				.createPointSymbolizer(graphic));
		highlightStyle(fts);
	}

	private void highlightStyle(FeatureTypeStyle fts) {
		FeatureTypeStyle[] styles = new FeatureTypeStyle[oldStyle.length + 1];
		System.arraycopy(oldStyle, 0, styles, 0, oldStyle.length);
		styles[styles.length - 1] = fts;
	}

	private void processLine(Style style2) {
		FeatureTypeStyle fts = builder.createFeatureTypeStyle(builder
				.createLineSymbolizer(highlightColor, 5));
		highlightStyle(fts);
	}

	private void processPolygon(Style style2) {
		FeatureTypeStyle fts = builder.createFeatureTypeStyle(builder
				.createPolygonSymbolizer(highlightColor, highlightColor, 4));
		highlightStyle(fts);
	}
}
