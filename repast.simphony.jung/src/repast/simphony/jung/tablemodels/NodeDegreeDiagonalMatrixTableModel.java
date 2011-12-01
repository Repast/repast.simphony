package repast.simphony.jung.tablemodels;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.ui.probe.Utils;
import cern.colt.matrix.impl.SparseDoubleMatrix2D;
import edu.uci.ics.jung.algorithms.matrix.GraphMatrixOperations;
import edu.uci.ics.jung.graph.Graph;

/*
 * @author Michael J. North
 *
 */
public class NodeDegreeDiagonalMatrixTableModel extends DefaultTableModel {

	GraphMatrixOperations operations = null;
	Graph graph = null;
	SparseDoubleMatrix2D results = null;
	List vertixList = new ArrayList();

	public NodeDegreeDiagonalMatrixTableModel(Graph graph) {
		this.graph = graph;
		this.vertixList.addAll(graph.getVertices());
		this.results = GraphMatrixOperations
				.createVertexDegreeDiagonalMatrix(graph);
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
			return Utils.getNumberFormatInstance().format(
					this.results.getQuick(row, col - 1));
		}
	}

}
