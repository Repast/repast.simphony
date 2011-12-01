package repast.simphony.jung.tablemodels;

import java.util.ArrayList;
import java.util.List;

import edu.uci.ics.jung.algorithms.shortestpath.DijkstraDistance;
import edu.uci.ics.jung.graph.Graph;

/*
 * @author Michael J. North
 *
 */
public class DijkstraDistanceTableModel extends DefaultTableModel {

	DijkstraDistance distance = null;
	Graph graph = null;
	List vertixList = new ArrayList();

	public DijkstraDistanceTableModel(Graph graph) {
		this.graph = graph;
		this.vertixList.addAll(graph.getVertices());
		this.distance = new DijkstraDistance(graph);
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
			Object d = this.distance.getDistance(this.vertixList.get(row),
					this.vertixList.get(col - 1));
			if (d == null) {
				return "";
			} else {
				return d;
			}
		}
	}

}
