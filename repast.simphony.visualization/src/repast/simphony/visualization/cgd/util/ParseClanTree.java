package repast.simphony.visualization.cgd.util;

import java.awt.geom.Point2D;
import java.util.TreeSet;

import repast.simphony.visualization.cgd.graph.CGDGraph;

public class ParseClanTree{

	public ParseClanTree parent;

	public ParseClanTree firstChild;

	public ParseClanTree nextSibling;

	public Clan clan;

	public double minx, maxx, centerx;

	public ParseClanTree leftSibling;

	public Point2D.Double size;

	public Point2D.Double position;

	public double extraheight;

	public boolean dummy;

	public int heightInTree;
	
	private int id;

	private int numClans;

	public ParseClanTree() {
		parent = firstChild = nextSibling = null;
		clan = null;
		size = new Point2D.Double(0, 0);
		position = new Point2D.Double(0, 0);
		extraheight = 0.0;
	}

	public void addClan(Clan clan) {
		ParseClanTree newpct = new ParseClanTree();
		newpct.clan = clan;
		addChild(this, newpct);
	}

	public void addChild(ParseClanTree newnode){
		addChild(this, newnode);
	}
	private void addChild(ParseClanTree node, ParseClanTree newnode) {
		Clan clan = newnode.clan;
		int order = clan.order;

		while (true) {
			if (node.firstChild == null) {
				node.firstChild = newnode;
				newnode.parent = node;
				newnode.nextSibling = null;
				return;
			}
			ParseClanTree child = node.firstChild;
			while (child != null) {
				if (((CGDTreeSet)child.clan.nodes).isSubset(clan.nodes)) {
					node = child;
					break;
				}
				child = child.nextSibling;
			}
			if (child == null)
			// Add as a new child
			{
				if (node.firstChild.clan.order > order) {
					newnode.nextSibling = node.firstChild;
					node.firstChild = newnode;
					newnode.parent = node;
					return;
				}

				ParseClanTree tmpnode = node.firstChild;
				while (tmpnode.nextSibling != null
						&& tmpnode.nextSibling.clan.order <= order)
					tmpnode = tmpnode.nextSibling;

				newnode.nextSibling = tmpnode.nextSibling;
				tmpnode.nextSibling = newnode;
				newnode.parent = node;
				return;
			}
		}
	}

	private void moveChild(ParseClanTree node, ParseClanTree newnode) {
		Clan clan = newnode.clan;
		int order = clan.order;

		if (node.firstChild == null) {
			node.firstChild = newnode;
			newnode.parent = node;
			newnode.nextSibling = null;
			return;
		}

		if (node.firstChild.clan.order > order) {
			newnode.nextSibling = node.firstChild;
			node.firstChild = newnode;
			newnode.parent = node;
			return;
		}

		ParseClanTree tmpnode = node.firstChild;
		while (tmpnode.nextSibling != null
				&& tmpnode.nextSibling.clan.order <= order)
			tmpnode = tmpnode.nextSibling;

		newnode.nextSibling = tmpnode.nextSibling;
		tmpnode.nextSibling = newnode;
		newnode.parent = node;
	}
	
	public void fixLinear(TreeSet node_subset, TreeSet child_relation[],
			TreeSet parent_relation[]) {
		ParseClanTree child;

		for (child = firstChild; child != null; child = child.nextSibling)
			child.fixLinear(node_subset, child_relation, parent_relation);

		if (clan.clanType != Clan.PRIMITIVE)
			return;

		boolean is_linear = true;

		ParseClanTree last = firstChild;
		ParseClanTree cur;

		for (cur = last.nextSibling; is_linear && cur != null; cur = last.nextSibling) {
			// If the current source is not a child
			// of the last sink, or the last sink
			// is not a parent of the current source,
			// then this is not a linear clan.

			CGDTreeSet last_children = new CGDTreeSet();
			last_children.union(child_relation[(Integer)last.clan.sinks.first()]);
			last_children.intersect(node_subset);
			CGDTreeSet cur_parents = new CGDTreeSet();
			cur_parents.union(parent_relation[(Integer)cur.clan.sources.first()]);
			cur_parents.intersect(node_subset);

			if (!(((CGDTreeSet)cur.clan.nodes).isSubset(last_children))
					|| !(((CGDTreeSet)last.clan.nodes).isSubset(cur_parents))) {
				is_linear = false;
				break;
			}
			last = cur;
		}
		if (is_linear)
			clan.clanType = Clan.LINEAR;
	}

	public void reduce(){
		reduce(this);
	}
	
	// Recursive
	private void reduce(ParseClanTree node) {
		ParseClanTree children[];
		ParseClanTree child;

		int num_children = 0;
		for (child = node.firstChild; child != null; child = child.nextSibling)
			num_children++;
		children = new ParseClanTree[num_children];

		int i;
		for (child = node.firstChild, i = 0; child != null; child = child.nextSibling, i++)
			children[i] = child;

		for (i = 0; i < num_children; i++)
			reduce(children[i]);

		int ptype = (node.parent != null ? node.parent.clan.clanType : 0);
		int ntype = node.clan.clanType;

		if (node.parent != null
				&& (ptype == ntype
						|| (ptype == Clan.PSEUDOINDEPENDENT && ntype == Clan.INDEPENDENT) || (ptype == Clan.INDEPENDENT && ntype == Clan.PSEUDOINDEPENDENT))) {
			if (ntype == Clan.PSEUDOINDEPENDENT)
				node.parent.clan.clanType = Clan.PSEUDOINDEPENDENT;
			while (node.firstChild != null) {
				ParseClanTree tmpnode = node.firstChild;
				node.firstChild = node.firstChild.nextSibling;
				moveChild(node.parent, tmpnode);
			}
			if (node.parent.firstChild == node)
				node.parent.firstChild = node.nextSibling;
			else {
				ParseClanTree tmpnode;
				for (tmpnode = node.parent.firstChild; tmpnode.nextSibling != node; tmpnode = tmpnode.nextSibling)
					;
				tmpnode.nextSibling = node.nextSibling;
			}
		}
	}
	
	public void setId(int _id){
		id=_id;
		setId(this);
		numClans = id - _id;
	}
	
	// Recursive
	private void setId(ParseClanTree node) {
		if (node.clan.clanType == Clan.SINGLETON)
			node.clan.id = (Integer)node.clan.nodes.first();
		else
			node.clan.id = id++;
		ParseClanTree tmpnode;
		for (tmpnode = node.firstChild; tmpnode != null; tmpnode = tmpnode.nextSibling)
			setId(tmpnode);
	}

	public void reorder(){
		reorder(this);
	}
	
	// Reorder the children of independent clans from left to right.
	// Dummy clans (those with no real nodes) will be placed in the center).
	private void reorder(ParseClanTree node) {
		if (node.clan.clanType == Clan.SINGLETON)
			return;

		int numchildren = 0, numdummies = 0;
		ParseClanTree tmp;
		for (tmp = node.firstChild; tmp != null; tmp = tmp.nextSibling) {
			reorder(tmp);
			numchildren++;
			if (tmp.dummy)
				numdummies++;
		}

		if (numchildren < 2 || !(node.clan.clanType == Clan.INDEPENDENT || 
				node.clan.clanType == Clan.PSEUDOINDEPENDENT))
			return;

		ParseClanTree[] children = new ParseClanTree[numchildren];
		int i = 0;
		for (tmp = node.firstChild; tmp != null; tmp = tmp.nextSibling)
			children[i++] = tmp;

		for (i = 0; i < numchildren - 1; i++)
			for (int j = i + 1; j < numchildren; j++)
				if ((!children[i].dummy && !children[j].dummy && children[j].centerx < children[i].centerx)
						|| (!children[i].dummy && children[j].dummy)) {
					ParseClanTree tmpnode = children[i];
					children[i] = children[j];
					children[j] = tmpnode;
				}

		if (numdummies > 0){
			// Move the dummy nodes to the center.
			int num_left = (numchildren - numdummies) / 2;
			for (i = 0; i < num_left; i++) {
				ParseClanTree tmpnode = children[i];
				children[i] = children[i + numdummies];
				children[i + numdummies] = tmpnode;
			}
		}

		node.firstChild = children[0];
		for (i = 0; i < numchildren - 1; i++)
			children[i].nextSibling = children[i + 1];

		children[i].nextSibling = null;
	}

	//////////////////////////////////////////////////////////////////////////
	// This set of toString methods is needed mostly for debugging purpose. //
	//////////////////////////////////////////////////////////////////////////
	public String toString() {
		return _toString(0, null);
	}

	public String toString(CGDGraph graph) {
		return _toString(0, graph);
	}

	private String _toString(int indent, CGDGraph graph) {
		String string = new String();

		int i;
		for (i = 0; i < indent; i++)
			string += "   ";

		if (graph != null)
			string += clan.toString(graph);
		else
			string += clan.toString();
		if (firstChild != null) {
			string += "\n";

			for (i = 0; i < indent + 1; i++)
				string += "   ";
			string += "(\n";

			ParseClanTree tmpclan;
			for (tmpclan = firstChild; tmpclan != null; tmpclan = tmpclan.nextSibling) {
				string += tmpclan._toString(indent + 1, graph);
				string += "\n";
			}
			for (i = 0; i < indent + 1; i++)
				string += "   ";
			string += ")";
		}
		return string;
	}
}