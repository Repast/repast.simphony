package repast.simphony.gis.id;

import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.filter.AttributeExpression;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.FilterType;
import org.geotools.filter.GeometryFilter;
import org.geotools.filter.LiteralExpression;
import org.geotools.geometry.jts.JTS;
import org.geotools.map.MapContext;
import org.geotools.referencing.CRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

import simphony.util.messages.MessageCenter;

import com.vividsolutions.jts.geom.Point;

public class IDLayer {

	MessageCenter center = MessageCenter.getMessageCenter(getClass());

	private FilterFactory ff;

	public IDLayer(FilterFactory factory) {
		this.ff = factory;
	}

	public FeatureCollection selectFeatures(Point point,
			FeatureSource featureSource, MapContext context) throws Exception {
		CoordinateReferenceSystem viewCrs = context
				.getCoordinateReferenceSystem();
		CoordinateReferenceSystem sourceCrs = featureSource.getSchema()
				.getDefaultGeometry().getCoordinateSystem();
		if (!viewCrs.equals(sourceCrs)) {

			MathTransform transform = CRS.findMathTransform(viewCrs, sourceCrs, true);
			point = (Point) JTS.transform(point, transform);
		}
		center.info(point);
		LiteralExpression pointExpression = ff.createLiteralExpression();
		pointExpression.setLiteral(point);
		String geometryName = featureSource.getSchema().getDefaultGeometry()
				.getName();
		AttributeExpression geometryExpression = ff
				.createAttributeExpression(geometryName);
		GeometryFilter withinFilter = ff
				.createGeometryFilter(FilterType.GEOMETRY_INTERSECTS);
		withinFilter.addLeftGeometry(geometryExpression);
		withinFilter.addRightGeometry(pointExpression);
		return featureSource.getFeatures(withinFilter);
	}
}
