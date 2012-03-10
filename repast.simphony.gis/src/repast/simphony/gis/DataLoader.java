package repast.simphony.gis;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureCollection;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.FeatureType;

import com.vividsolutions.jts.geom.Geometry;

public class DataLoader {

	public static <T extends Object> Map<T, Geometry> loadData(
			ShapefileDescriptor desc, Class<T> clazz) throws Exception {
		Map<T, Geometry> map = new HashMap<T, Geometry>();
		try {
			FeatureObjectFiller test = new FeatureObjectFiller(clazz);
			for (AttributeType type : desc.getFeatureType().getAttributeTypes()) {
				test.addAttribute(type);
			}
			FeatureCollection collection = desc.getFeatures();
			Iterator iter = collection.iterator();
			while (iter.hasNext()) {
				Feature feature = (Feature) iter.next();
				T o = clazz.newInstance();
				test.fillObject(feature, o);
				map.put(o, feature.getDefaultGeometry());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	public static <T extends Object> Map<T, Geometry> loadData(
			URL shapeFileURL, Class<T> clazz) throws Exception {
		Map<T, Geometry> map = new HashMap<T, Geometry>();
		try {
			ShapefileDataStore store = new ShapefileDataStore(shapeFileURL);
			FeatureSource source = store.getFeatureSource();
			FeatureObjectFiller test = new FeatureObjectFiller(clazz);
			for (AttributeType type : store.getSchema().getAttributeTypes()) {
				test.addAttribute(type);
			}
			FeatureCollection collection = source.getFeatures();
			Iterator iter = collection.iterator();
			while (iter.hasNext()) {
				SimpleFeature feature = (SimpleFeature) iter.next();
				T o = clazz.newInstance();
				test.fillObject(feature, o);
				map.put(o, feature.getDefaultGeometry());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	public static <T extends Object> Map<T, Geometry> loadData(
			FeatureType type, FeatureCollection collection, Class<T> clazz) {
		Map<T, Geometry> map = new HashMap<T, Geometry>();
		try {
			FeatureObjectFiller test = new FeatureObjectFiller(clazz);
			for (AttributeType at : type.getAttributeTypes()) {
				test.addAttribute(at);
			}
			Iterator iter = collection.iterator();
			while (iter.hasNext()) {
				Feature feature = (Feature) iter.next();
				T o = clazz.newInstance();
				test.fillObject(feature, o);
				map.put(o, feature.getDefaultGeometry());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
