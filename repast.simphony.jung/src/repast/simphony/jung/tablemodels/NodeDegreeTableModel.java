package repast.simphony.jung.tablemodels;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.jung.statistics.RepastJungDegreeDistributions;
import repast.simphony.ui.probe.Utils;
import cern.colt.list.DoubleArrayList;
import cern.colt.matrix.impl.SparseDoubleMatrix2D;
import edu.uci.ics.jung.algorithms.matrix.GraphMatrixOperations;
import edu.uci.ics.jung.graph.Graph;

/*
 * @author Michael J. North
 *
 */
public class NodeDegreeTableModel extends DefaultTableModel {

	public enum Type {
		IN, OUT, UNDIRECTED
	};

	GraphMatrixOperations operations = null;
	Graph graph = null;
	DoubleArrayList resultsArray = null;
	List vertixList = new ArrayList();
	Type type = null;

	public NodeDegreeTableModel(Graph graph, Type type) {
		this.graph = graph;
		this.vertixList.addAll(graph.getVertices());
		this.type = type;
		if (this.type == Type.IN) {
			this.resultsArray = RepastJungDegreeDistributions
					.getIndegreeValues(graph);
		} else if (this.type == Type.OUT) {
			this.resultsArray = RepastJungDegreeDistributions
					.getOutdegreeValues(graph);
		} else {
			this.resultsArray = RepastJungDegreeDistributions
					.getDegreeValues(graph);
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
			if (this.type == Type.IN) {
				return "In Degree";
			} else if (this.type == Type.OUT) {
				return "Out Degree";
			} else {
				return "Undirected Degree";
			}
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
			return Utils.getNumberFormatInstance().format(
					this.resultsArray.get(row));
		}
	}
}
