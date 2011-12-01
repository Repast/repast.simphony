package repast.simphony.jung.tablemodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import repast.simphony.jung.statistics.RepastJungGraphStatistics;
import repast.simphony.ui.probe.Utils;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraDistance;
import edu.uci.ics.jung.algorithms.shortestpath.Distance;
import edu.uci.ics.jung.graph.Graph;

/*
 * @author Michael J. North
 *
 */
public class AverageWeightedDistanceTableModel extends DefaultTableModel {

	Graph graph = null;
	Map<Object, Double> resultsMap = new HashMap<Object, Double>();
	List keys = new ArrayList();

	public AverageWeightedDistanceTableModel(Graph graph) {
		this.graph = graph;
		final DijkstraDistance dijkstraDistance = new DijkstraDistance(graph);
		Distance<Object> distance = new Distance<Object>() {

			public Number getDistance(Object source, Object target) {
				return dijkstraDistance.getDistance(source, target);
			}

			public Map<Object, Number> getDistanceMap(Object source) {
				return dijkstraDistance.getDistanceMap(source);
			}

		};
		this.resultsMap = RepastJungGraphStatistics.averageDistances(graph,
				distance);
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
			return "Average Weighted Distance";
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
