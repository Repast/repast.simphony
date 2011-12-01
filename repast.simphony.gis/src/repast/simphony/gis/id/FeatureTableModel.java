package repast.simphony.gis.id;

import javax.swing.table.AbstractTableModel;

import org.geotools.feature.AttributeType;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.FeatureType;
import org.geotools.filter.AttributeExpression;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.FilterFactoryFinder;

public class FeatureTableModel extends AbstractTableModel {
	Feature[] features;

	FeatureType type;

	AttributeExpression[] exprs;

	FilterFactory fac = FilterFactoryFinder.createFilterFactory();

	public FeatureTableModel(FeatureCollection collection, FeatureType type) {
		features = new Feature[collection.size()];
		FeatureIterator iter = collection.features();
		try {
			int i = 0;
			while (iter.hasNext()) {
				Feature feature = iter.next();
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
