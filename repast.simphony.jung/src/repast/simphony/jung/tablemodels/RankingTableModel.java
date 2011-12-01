package repast.simphony.jung.tablemodels;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.space.graph.RepastEdge;
import repast.simphony.ui.probe.Utils;
import edu.uci.ics.jung.algorithms.importance.AbstractRanker;
import edu.uci.ics.jung.algorithms.importance.Ranking;

/*
 * @author Michael J. North
 *
 */
public class RankingTableModel extends DefaultTableModel {

	protected AbstractRanker ranker = null;
	protected List rankings = new ArrayList();

	public RankingTableModel(AbstractRanker ranker) {
		this.ranker = ranker;
		this.ranker.evaluate();
		this.rankings = this.ranker.getRankings();
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public String getColumnName(int col) {
		if (col == 0) {
			if (this.ranker.isRankingNodes()) {
				return "Node";
			} else if (this.ranker.isRankingEdges()) {
				return "Edge";
			} else {
				return "";
			}
		} else if (col == 1) {
			return "Rank";
		} else {
			return "";
		}
	}

	@Override
	public int getRowCount() {
		return this.rankings.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		if (col == 0) {
			if (this.ranker.isRankingNodes()) {
				return ((Ranking) this.rankings.get(row)).getRanked()
						.toString();
			} else if (this.ranker.isRankingEdges()) {
				Object obj = ((Ranking) this.rankings.get(row)).getRanked();
				if (obj instanceof RepastEdge) {
					RepastEdge edge = (RepastEdge) obj;
					return edge.getSource().toString() + " to "
							+ edge.getTarget().toString();
				} else {
					return obj.toString();
				}
			} else {
				return "";
			}
		} else if (col == 1) {
			return Utils.getNumberFormatInstance().format(
					((Ranking) this.rankings.get(row)).rankScore);
		} else {
			return "";
		}
	}
}
