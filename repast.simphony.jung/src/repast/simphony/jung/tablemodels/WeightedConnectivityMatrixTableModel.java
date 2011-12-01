package repast.simphony.jung.tablemodels;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.space.graph.RepastEdge;
import repast.simphony.ui.probe.Utils;
import edu.uci.ics.jung.graph.Graph;

/*
 * @author Michael J. North
 *
 */
public class WeightedConnectivityMatrixTableModel extends DefaultTableModel {

	Graph graph = null;
	List vertixList = new ArrayList();

	public WeightedConnectivityMatrixTableModel(Graph graph) {
		this.graph = graph;
		this.vertixList.addAll(graph.getVertices());
	}

	@Override
	public int getColumnCount() {
		return Math.max(this.vertixList.size() + 1, 0);
	}

	@Override
	public String getColumnName(int col) {
		if (col == 0) {
			return "";
		} else {
			return this.vertixList.get(col - 1).toString();
		}
	}

	@Override
	public int getRowCount() {
		return this.vertixList.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		if (col == 0) {
			return this.vertixList.get(row).toString();
		} else {
			Object d = this.graph.findEdge(this.vertixList.get(row),
					this.vertixList.get(col - 1));
			if (d != null) {
				if (d instanceof RepastEdge) {
					return Utils.getNumberFormatInstance().format(
							((RepastEdge) d).getWeight());
				} else {
					return "1.0";
				}
			} else {
				return "0.0";
			}
		}
	}

}
