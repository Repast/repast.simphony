package repast.simphony.gis.display;

import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.AttributeType;
import org.geotools.feature.FeatureCollection;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.FilterFactoryFinder;
import org.geotools.filter.function.QuantileFunction;

import java.io.File;

public class ClassificationTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		ShapefileDataStore store = new ShapefileDataStore(new File(
				"../../bootstrap/baseData/ChicagoMetroCounties.shp").toURL());
		FeatureCollection collection = store.getFeatureSource().getFeatures();
		for (AttributeType type : collection.getSchema().getAttributeTypes()) {
			System.out.println(type.getName());
		}
		QuantileFunction function = new QuantileFunction();
		function.setCollection(collection);
		function.setNumberOfClasses(6);
		FilterFactory fac = FilterFactoryFinder.createFilterFactory();
		function.setExpression(fac.createAttributeExpression("POP2004"));
		for (int i = 0; i < 5; i++) {
			System.out.println(function.getMin(i));
		}

	}
}
