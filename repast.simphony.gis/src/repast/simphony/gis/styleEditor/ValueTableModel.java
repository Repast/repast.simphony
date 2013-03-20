package repast.simphony.gis.styleEditor;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.CompareFilter;
import org.geotools.filter.Expression;
import org.geotools.filter.LiteralExpression;
import org.geotools.styling.Rule;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Literal;

import com.vividsolutions.jts.geom.Geometry;

import repast.simphony.gis.GeometryUtil;
import repast.simphony.gis.display.LegendIconMaker;

import javax.swing.Icon;
import javax.swing.table.AbstractTableModel;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Table for the Value panel that shows how the icon fill appears according to
 * the value rules.
 * 
 * @author Nick Collier
 * @author Eric Tatara
 */
public class ValueTableModel extends AbstractTableModel {

	private static FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory();
	private static String[] COL_NAMES = {"Symbol", "Value", "Label"};

	private List<Rule> rules = new ArrayList<Rule>();
//  private SimpleFeature sample;
	private Rule defaultRule;
	private Map<Class, ObjectConvertor> convertors = new HashMap<Class, ObjectConvertor>();
	private ObjectConvertor convertor;
	private PreviewLabel preview;

	public ValueTableModel(SimpleFeatureType featureType, PreviewLabel preview) {
		RuleCreator creator = new RuleCreator();
		Geometry geom = (Geometry)featureType.getGeometryDescriptor().getDefaultValue();
		defaultRule = creator.createDefaultRule(Color.RED, GeometryUtil.findGeometryType(geom));
		addRule(defaultRule);

		this.preview = preview;
		
		convertors.put(Double.class, new DoubleConvertor());
		convertors.put(double.class, new DoubleConvertor());
		convertors.put(int.class, new IntegerConvertor());
		convertors.put(Integer.class, new IntegerConvertor());
		convertors.put(float.class, new FloatConvertor());
		convertors.put(Float.class, new FloatConvertor());
		convertors.put(Long.class, new LongConvertor());
		convertors.put(long.class, new LongConvertor());
		convertors.put(Boolean.class, new BooleanConvertor());
		convertors.put(boolean.class, new BooleanConvertor());
		convertors.put(String.class, new StringConvertor());
	}

	public void init(Class attributeType) {
		clear();
		convertor = convertors.get(attributeType);
	}

	public void init(Class attributeType, List<Rule> rules) {
		this.rules.clear();
		this.rules.addAll(rules);
		defaultRule = this.rules.get(0);
		convertor = convertors.get(attributeType);
	}

	public Rule getDefaultRule() {
		return defaultRule;
	}

	public void addRule(Rule rule) {
		rules.add(rule);
		fireTableRowsInserted(rules.size() - 1, rules.size() - 1);
	}


	/**
	 * This empty implementation is provided so users don't have to implement
	 * this method if their data model is not editable.
	 *
	 * @param aValue      value to assign to cell
	 * @param rowIndex    row of cell
	 * @param columnIndex column of cell
	 */
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		String strVal = aValue == null ? "" : aValue.toString();
		Rule rule = rules.get(rowIndex);
		if (columnIndex == 1) {
			Object obj = convertor.convert(strVal);
			if (obj != null) {
				updateRuleLiteral(rule, obj);
			}

		} else if (columnIndex == 2) {
			rule.setTitle(strVal);
		}
		fireTableRowsUpdated(rowIndex, rowIndex);
	}

	/**
	 * Returns false.  This is the default implementation for all cells.
	 *
	 * @param rowIndex    the row being queried
	 * @param columnIndex the column being queried
	 * @return false
	 */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (columnIndex == 0) return false;
		if (rowIndex == 0 && columnIndex == 1) return false;
		if (columnIndex == 2) return convertor != null;
		return true;
	}

	/**
	 * Returns a default name for the column using spreadsheet conventions:
	 * A, B, C, ... Z, AA, AB, etc.  If <code>column</code> cannot be found,
	 * returns an empty string.
	 *
	 * @param column the column being queried
	 * @return a string containing the default name of <code>column</code>
	 */
	@Override
	public String getColumnName(int column) {
		return COL_NAMES[column];
	}

	/**
	 * Returns the number of columns in the model. A
	 * <code>JTable</code> uses this method to determine how many columns it
	 * should create and display by default.
	 *
	 * @return the number of columns in the model
	 * @see #getRowCount
	 */
	public int getColumnCount() {
		return COL_NAMES.length;
	}

	/**
	 * Returns the number of rows in the model. A
	 * <code>JTable</code> uses this method to determine how many rows it
	 * should display.  This method should be quick, as it
	 * is called frequently during rendering.
	 *
	 * @return the number of rows in the model
	 * @see #getColumnCount
	 */
	public int getRowCount() {
		return rules.size();
	}

	public Color getColorAt(int row) {
		Rule rule = rules.get(row);
		return RuleCreator.getColor(rule);
	}

	public void setColorAt(int row, Color color) {
		Rule rule = rules.get(row);
		RuleCreator.setColor(rule, color);
	}

	/**
	 * Returns the value for the cell at <code>columnIndex</code> and
	 * <code>rowIndex</code>.
	 *
	 * @return the value Object at the specified cell
	 * @param	rowIndex	the row whose value is to be queried
	 * @param	columnIndex the column whose value is to be queried
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {
		Rule rule = rules.get(rowIndex);
		switch (columnIndex) {
			case 0:
				return getIcon(rule);
			case 1:
				return literalValue(rule);
			case 2:
				return rule.getDescription().getTitle().toString();
			default:
				return "";
		}
	}

	/**
	 * Delete the selected rows.
	 *
	 * @param rows the rows to delete.
	 */
	public void delete(int[] rows) {
		List<Rule> itemsToDelete = new ArrayList<Rule>();
		for (int row : rows) {
			if (row != 0) {
				itemsToDelete.add(rules.get(row));
			}
		}
		rules.removeAll(itemsToDelete);
		if (rows.length == 1) fireTableRowsDeleted(rows[0], rows[0]);
		else fireTableDataChanged();
	}

	/**
	 * Gets the rules that this model holds.
	 *
	 * @param includeDefault whether or not the default rule should be included
	 * @return the rules that this model holds.
	 */
	public List<Rule> getRules(boolean includeDefault) {
		return includeDefault ? rules : rules.subList(1, rules.size());
	}

	private Icon getIcon(Rule rule) {
	// TODO Geotools
//		return LegendIconMaker.makeLegendIcon(12, rule, sample);
		return preview.getSmallIcon();
		
	}

	// TODO Geotools deprecated
	private LiteralExpression findLiteralExpression(Rule rule) {
		CompareFilter filter = (CompareFilter) rule.getFilter();
		Expression exp = filter.getRightValue();
		if (exp instanceof LiteralExpression) return (LiteralExpression) exp;
		else return (LiteralExpression) filter.getLeftValue();
	}

	private String literalValue(Rule rule) {
		if (!rule.hasElseFilter()) {
			LiteralExpression exp = findLiteralExpression(rule);
			return exp.getLiteral().toString();
		}
		return "<Default>";
	}

	private void updateRuleLiteral(Rule rule, Object obj) {
		LiteralExpression literal = findLiteralExpression(rule);
		literal.setLiteral(obj);
	}

	/**
	 * Clears all the rules but the default.
	 */
	public void clear() {
		if (rules.size() > 1) {
			int lastRow = rules.size() - 1;
			rules.clear();
			fireTableRowsDeleted(1, lastRow);
			addRule(defaultRule);
		}
	}

	public void useDefaultChanged() {
		fireTableRowsUpdated(0, 0);
	}

	/**
	 * Gets the rule at the specified row.
	 *
	 * @param row the index of the rule to get
	 *
	 * @return the rule at the specified row.
	 */
	public Rule getRule(int row) {
		if (row > -1 && row < rules.size()) return rules.get(row);
		return null;
	}

	/**
	 * Sets the rules at the specified row to the specified rule.
	 *
	 * @param row the index of the rule
	 * @param rule the new rule
	 */
	public void setRule(int row, Rule rule) {
		rules.remove(row);
		if (row == rules.size()) rules.add(rule);
		else rules.add(row, rule);
		fireTableRowsUpdated(row, row);
	}

	private static interface ObjectConvertor {
		Object convert(String obj);
	}

	private static class DoubleConvertor implements ObjectConvertor {
		public Object convert(String obj) {
			try {
				return Double.parseDouble(obj);
			} catch (NumberFormatException ex) {}
			return null;
		}
	}

	private static class LongConvertor implements ObjectConvertor {
		public Object convert(String obj) {
			try {
				return Long.parseLong(obj);
			} catch (NumberFormatException ex) {}
			return null;
		}
	}

	private static class IntegerConvertor implements ObjectConvertor {
		public Object convert(String obj) {
			try {
				return Integer.parseInt(obj);
			} catch (NumberFormatException ex) {}
			return null;
		}
	}

	private static class FloatConvertor implements ObjectConvertor {
		public Object convert(String obj) {
			try {
				return Float.parseFloat(obj);
			} catch (NumberFormatException ex) {}
			return null;
		}
	}

	private static class BooleanConvertor implements ObjectConvertor {
		public Object convert(String obj) {
			try {
				return Boolean.parseBoolean(obj);
			} catch (NumberFormatException ex) {}
			return null;
		}
	}

	private static class StringConvertor implements ObjectConvertor {
		public Object convert(String obj) {
			return obj;
		}
	}
}
