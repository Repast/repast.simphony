package repast.simphony.gis.styleEditor;

import org.geotools.data.FeatureSource;
import org.geotools.feature.GeometryAttributeType;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.Symbolizer;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class DefaultStyler {
	private static StyleBuilder builder = new StyleBuilder();

	public static Style getDefaultStyle(FeatureSource source) {
		return builder.createStyle(source.getSchema().getTypeName(),
				getDefaultSymbolizer(source));
	}

	public static Symbolizer getDefaultSymbolizer(FeatureSource source) {
		GeometryAttributeType type = source.getSchema().getDefaultGeometry();
		if (type.getType().equals(Point.class)
				|| type.getType().equals(MultiPoint.class)) {
			return builder.createPointSymbolizer();
		} else if (type.getType().equals(LineString.class)
				|| type.getType().equals(MultiLineString.class)) {
			return builder.createLineSymbolizer();
		} else if (type.getType().equals(Polygon.class)
				|| type.getType().equals(MultiPolygon.class)) {
			return builder.createPolygonSymbolizer();
		} else {
			throw new IllegalArgumentException(type.getType().getName()
					+ " is not a supported geometry type");
		}

	}
}
