package repast.simphony.gis.styleEditor;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.table.AbstractTableModel;

import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Rule;

/**
 * Table for the Range panel that shows how the icon fill appears according to
 * the range rules.
 * 
 * @author Nick Collier
 * @author Eric Tatara
 */
public class SampleStyleTableModel extends AbstractTableModel {

	private static String[] COL_NAMES = {"Symbol", "Range", "Label"};

	private List<Rule> rules = new ArrayList<Rule>();
	private PreviewLabel defaultPreview;

	public SampleStyleTableModel(PreviewLabel preview){
		this.defaultPreview = preview;
	}
	
	public void initStyle(FeatureTypeStyle style) {
		rules.clear();
		for (Rule rule : style.rules()) {
			rules.add(rule);
		}
		fireTableDataChanged();
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
				return ruleTitleToRange(rule.getDescription().getTitle().toString());
			case 2:
				return ruleTitleToRange(rule.getDescription().getTitle().toString());
			default:
				return "";
		}
	}

	/**
	 * Gets the rule at the specified row.
	 *
	 * @param row the index of the rule to get
	 * @return the rule at the specified row.
	 */
	public Rule getRule(int row) {
		if (row > -1 && row < rules.size()) return rules.get(row);
		return null;
	}

	/**
	 * Sets the rules at the specified row to the specified rule.
	 *
	 * @param row  the index of the rule
	 * @param rule the new rule
	 */
	public void setRule(int row, Rule rule) {
		rules.remove(row);
		if (row == rules.size()) rules.add(rule);
		else rules.add(row, rule);
		fireTableRowsUpdated(row, row);
	}

	private Icon getIcon(Rule rule) {
		return PreviewLabel.formatPreview(defaultPreview, rule);
	}

	private String ruleTitleToRange(String title) {
		return title.replace("..", " - ");
	}

	public void init(List<Rule> rules) {
		this.rules.clear();
		this.rules.addAll(rules);
	}

	public void setDefaultPreview(PreviewLabel defaultPreview) {
		this.defaultPreview = defaultPreview;
	}
}
