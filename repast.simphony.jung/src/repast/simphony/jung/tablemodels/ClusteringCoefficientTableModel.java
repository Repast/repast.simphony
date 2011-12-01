package repast.simphony.jung.tablemodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import repast.simphony.jung.statistics.RepastJungGraphStatistics;
import repast.simphony.ui.probe.Utils;
import edu.uci.ics.jung.graph.Graph;

/*
 * @author Michael J. North
 *
 */
public class ClusteringCoefficientTableModel extends DefaultTableModel {

	Graph graph = null;
	Map<Object, Double> resultsMap = new HashMap<Object, Double>();
	List keys = new ArrayList();

	public ClusteringCoefficientTableModel(Graph graph) {
		this.graph = graph;
		this.resultsMap = RepastJungGraphStatistics
				.clusteringCoefficients(graph);
		for (Object key : this.resultsMap.keySet()) {
			keys.add(key);
		}
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public String getColumnName(int col) {
		if (col == 0) {
			return "Node";
		} else {
			return "Clustering Coefficient";
		}
	}

	@Override
	public int getRowCount() {
		return this.resultsMap.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		if (col == 0) {
			return this.keys.get(row).toString();
		} else {
			return Utils.getNumberFormatInstance().format(
					this.resultsMap.get(this.keys.get(row)));
		}
	}

}
