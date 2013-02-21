package repast.simphony.gis.styleEditor;

import org.geotools.data.FeatureSource;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.Symbolizer;
import org.opengis.feature.type.GeometryType;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class DefaultStyler {
	private static StyleBuilder builder = new StyleBuilder();

	public static Style getDefaultStyle(FeatureSource source) {
		return builder.createStyle(source.getSchema().getName().getLocalPart(),
				getDefaultSymbolizer(source));
	}

	public static Symbolizer getDefaultSymbolizer(FeatureSource source) {
		GeometryType type = source.getSchema().getGeometryDescriptor().getType();
		if (type.getBinding().equals(Point.class)
				|| type.getBinding().equals(MultiPoint.class)) {
			return builder.createPointSymbolizer();
		} else if (type.getBinding().equals(LineString.class)
				|| type.getBinding().equals(MultiLineString.class)) {
			return builder.createLineSymbolizer();
		} else if (type.getBinding().equals(Polygon.class)
				|| type.getBinding().equals(MultiPolygon.class)) {
			return builder.createPolygonSymbolizer();
		} else {
			throw new IllegalArgumentException(type.getBinding().getName()
					+ " is not a supported geometry type");
		}

	}
}
