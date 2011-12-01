package repast.simphony.gis.util;

import java.awt.geom.Point2D;

import org.geotools.feature.FeatureCollection;
import org.geotools.filter.AbstractFilter;
import org.geotools.filter.BBoxExpression;
import org.geotools.filter.Expression;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.FilterFactoryFinder;
import org.geotools.filter.GeometryFilter;
import org.geotools.map.MapLayer;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

public class LayerQuery {

	MapLayer layer;

	GeometryFactory fac = new GeometryFactory();

	private double tolerance = .001;

	public FeatureCollection query(Point2D point) {
		Coordinate coord = new Coordinate(point.getX(), point.getY());
		Geometry geom = fac.createPoint(coord);
		geom = geom.buffer(tolerance);
		FilterFactory fac = FilterFactoryFinder.createFilterFactory();
		Envelope e = geom.getEnvelopeInternal();
		try {
			Expression geometry = fac.createAttributeExpression(layer
					.getFeatureSource().getSchema().getDefaultGeometry()
					.getName());
			GeometryFilter bboxFilter = fac
					.createGeometryFilter(AbstractFilter.GEOMETRY_BBOX);
			bboxFilter.addLeftGeometry(geometry);
			bboxFilter.addRightGeometry(geometry);
			return layer.getFeatureSource().getFeatures(bboxFilter);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) {
		Point2D point = new Point2D.Double(-88, 42);
		LayerQuery query = new LayerQuery();
		query.query(point);
	}
}
