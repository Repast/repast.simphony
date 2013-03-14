package repast.simphony.gis.styleEditor;

import java.io.IOException;

import org.geotools.data.memory.MemoryDataStore;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.map.FeatureLayer;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;

public class SampleLayer {

	public static FeatureLayer getSampleLayer() throws IOException {
		SimpleFeatureTypeBuilder ftBuilder = new SimpleFeatureTypeBuilder();
		ftBuilder.setName("myfeature");
		
		ftBuilder.add("Location", Polygon.class);
		ftBuilder.add("name", String.class);
		
		final SimpleFeatureType TYPE = ftBuilder.buildFeatureType();
		SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);
		
		Coordinate[] coord = new Coordinate[5];
		coord[0] = new Coordinate(0, 0);
		coord[1] = new Coordinate(10, 0);
		coord[2] = new Coordinate(10, 10);
		coord[3] = new Coordinate(0, 10);
		coord[4] = new Coordinate(0, 0);
		
		GeometryFactory fac = JTSFactoryFinder.getGeometryFactory( null );
		Polygon p = fac.createPolygon(fac.createLinearRing(coord), new LinearRing[0]);
		
		featureBuilder.set("Location",p);
		featureBuilder.set("name","Buddy");
		SimpleFeature feature = featureBuilder.buildFeature(null);
		
		MemoryDataStore store = new MemoryDataStore(new SimpleFeature[] { feature });
		
		return new FeatureLayer(store
				.getFeatureSource(store.getTypeNames()[0]), DefaultStyler
				.getDefaultStyle(store
						.getFeatureSource(store.getTypeNames()[0])));
	}
}
