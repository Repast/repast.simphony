package repast.simphony.gis.id;

import javax.swing.table.AbstractTableModel;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.filter.AttributeExpression;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.FilterFactoryFinder;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.FeatureType;

public class FeatureTableModel extends AbstractTableModel {
	SimpleFeature[] features;

	SimpleFeatureType type;

	AttributeExpression[] exprs;

	FilterFactory fac = FilterFactoryFinder.createFilterFactory();

	public FeatureTableModel(SimpleFeatureCollection collection, SimpleFeatureType type) {
		features = new SimpleFeature[collection.size()];
		SimpleFeatureIterator iter = collection.features();
		try {
			int i = 0;
			while (iter.hasNext()) {
				SimpleFeature feature = iter.next();
				features[i++] = feature;
			}
		} finally {
			collection.close(iter);
		}
		this.type = type;
		exprs = new AttributeExpression[type.getAttributeCount()];
		AttributeType[] types = type.getAttributeTypes();
		for (int i = 0; i < types.length; i++) {
			exprs[i] = fac.createAttributeExpression(types[i].getName());
		}
	}

	public FeatureType getFeatureType() {
		return type;
	}

	@Override
	public String getColumnName(int arg0) {
		return exprs[arg0].getAttributePath();
	}

	public int getColumnCount() {
		return exprs.length;
	}

	public int getRowCount() {
		return features.length;
	}

	public Object getValueAt(int arg0, int arg1) {
		return exprs[arg1].getValue(features[arg0]);
	}
}
