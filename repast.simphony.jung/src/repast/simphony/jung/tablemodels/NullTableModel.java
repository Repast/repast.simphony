package repast.simphony.jung.tablemodels;

/*
 * @author Michael J. North
 *
 */
public class NullTableModel extends DefaultTableModel {

	@Override
	public int getColumnCount() {
		return 0;
	}

	@Override
	public String getColumnName(int col) {
		return "";
	}

	@Override
	public int getRowCount() {
		return 0;
	}

	@Override
	public Object getValueAt(int row, int col) {
		return "";
	}

}
