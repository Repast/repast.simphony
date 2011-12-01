package repast.simphony.visualization.cgd.util;

import java.util.Iterator;
import java.util.TreeSet;

import repast.simphony.visualization.cgd.graph.CGDGraph;



public class Clan{
	public final static int UNKNOWN = 0;
	public final static int INDEPENDENT = 1;
	public final static int LINEAR = 2;
	public final static int PRIMITIVE = 3;
	public final static int PSEUDOINDEPENDENT = 4;
	public final static int SINGLETON = 5;

	public int clanType;
	public int id;

	public TreeSet nodes;
	public TreeSet sources;
	public TreeSet sinks;

	public int size; // number of nodes in the clan
	public int order; // ordering for clan

	public Clan next; // For use in stack of clans.
	public Clan listnext; // For use in list of clans.
	
	public Clan(int type, TreeSet _nodes, TreeSet _sources, TreeSet _sinks, int _order) {
		clanType = type;
		nodes = _nodes;
		size = nodes.size();
		sources = _sources;
		sinks = _sinks;
		order = _order;
		next = listnext = null;
	}

	// This set of toString methods is needed mostly for debugging purpose.
	public String toString() {
		String string = new String();

		string += new String(" ILPiS").charAt(clanType);
		string += ": ";
		string += nodes.toString();

		return string;
	}

	public String toString(CGDGraph graph) {
		String string = new String();

		string += new String(" ILPiS").charAt(clanType);
		string += ": ";

		string += "(";
		int index;
		boolean first = true;
		int numnodes = graph.getNumOfNodes();
		Iterator it=nodes.iterator();
		while(it.hasNext()){
			index=(Integer)it.next();
			if (!first)
				string += ", ";
			else
				first = false;

			if (index < numnodes)
				string = string + index;
			else
				string += "d_" + index;

		}
		string = string + ")";

		return string;
	}
}