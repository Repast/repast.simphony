package repast.simphony.gis.styleEditor;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import org.geotools.data.memory.MemoryDataStore;
import org.geotools.factory.FactoryConfigurationError;
import org.geotools.feature.*;
import org.geotools.map.DefaultMapLayer;
import org.geotools.map.MapLayer;

import java.io.IOException;

public class SampleLayer {

	public static MapLayer getSampleLayer() throws FactoryConfigurationError,
			SchemaException, IllegalAttributeException, IOException {
		AttributeType type = AttributeTypeFactory.newAttributeType("name",
				String.class);
		AttributeType geom = AttributeTypeFactory.newAttributeType("the_geom",
				Polygon.class);
		FeatureType ft = FeatureTypeBuilder.newFeatureType(new AttributeType[] {
				type, geom }, "myfeature");
		Coordinate[] coord = new Coordinate[5];
		coord[0] = new Coordinate(0, 0);
		coord[1] = new Coordinate(10, 0);
		coord[2] = new Coordinate(10, 10);
		coord[3] = new Coordinate(0, 10);
		coord[4] = new Coordinate(0, 0);
		GeometryFactory fac = new GeometryFactory();
		Feature feature = ft.create(new Object[] {
				"Tom",
				fac.createPolygon(fac.createLinearRing(coord),
						new LinearRing[0]) });
		MemoryDataStore store = new MemoryDataStore(new Feature[] { feature });
		return new DefaultMapLayer(store
				.getFeatureSource(store.getTypeNames()[0]), DefaultStyler
				.getDefaultStyle(store
						.getFeatureSource(store.getTypeNames()[0])));
	}
}
