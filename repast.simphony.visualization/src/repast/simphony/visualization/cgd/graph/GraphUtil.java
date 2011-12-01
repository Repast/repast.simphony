package repast.simphony.visualization.cgd.graph;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import repast.simphony.visualization.cgd.util.CGDTreeSet;
import repast.simphony.visualization.cgd.util.Clan;
import repast.simphony.visualization.cgd.util.ParseClanTree;


public class GraphUtil {
	public double distH = 30;
	public double distV = 40;
	private double scale = 1.0;
	
	private CGDGraph graph=null;
	private ParseClanTree[] treeLookup;
	private int numNodes,initialNumNodes;
	private TreeSet[] parentRelation;
	
	public double getHDistance() {
		return 1 / scale * distH;
	}
	public double getVDistance() {
		return 1 / scale * distV;
	}
	
	/**
	 * Initializing bounding box placement
	 *
	 */
	public void buildNodeLayout(CGDGraph _graph, ParseClanTree _root, int _numNodes, int _initialNumNodes, TreeSet[] parentRel){
		graph=_graph;
		numNodes=_numNodes;
		parentRelation=parentRel;
		initialNumNodes=_initialNumNodes;
		
		bbSizeAttribute(_root, false);

		fillLeftSiblings(_root);
		bbCornerAttribute(_root);

		treeLookup = new ParseClanTree[numNodes];
		setLookup(_root);
		setHeightInTree(_root, 0);
		longEdgeHeuristic();
		treeLookup = new ParseClanTree[numNodes];
		setLookup(_root);
		bbSizeAttribute(_root, true);
		bbCornerAttribute(_root);
		realSizes(_root);
		angleFix();
		// First angleFix may cause more sharp angles, so call it again.
		angleFix();

		// Find a source node, the position of which will
		// not change.
		CGDNode first_source = _graph.getNode(0);
		int i;
		for (i = 0; i < numNodes; i++)
			if (parentRelation[i].isEmpty()) {
				first_source = _graph.getNode(i);
				break;
			}
		Point2D.Double offset = first_source.getPosition();

		copyCorner(_root);
		removeBends();

		Point2D.Double pos = first_source.getPosition();
		offset.x -= pos.x;
		offset.y -= pos.y;

		ArrayList nodes=_graph.getNodes();
		CGDNode tmpnode;
		for (int j=0;j<nodes.size();j++) {
			tmpnode=(CGDNode)nodes.get(j);
			pos = tmpnode.getPosition();
			tmpnode.setPosition(pos.x + offset.x, pos.y + offset.y);
		}
	}
	
	private void bbSizeAttribute(ParseClanTree node, boolean repeat) {
		if (node == null)
			return;
		bbSizeAttribute(node.firstChild, repeat);
		node.extraheight = 0.0;
		node.size = bbSize(node, repeat);
		bbSizeAttribute(node.nextSibling, repeat);
	}
	
	private Point2D.Double bbSize(ParseClanTree node, boolean repeat) {
		Point2D.Double size = new Point2D.Double(0.0, 0.0);

		if (node.clan.clanType == Clan.LINEAR) {
			size.x = childMax(node, 1); // width
			size.y = childSum(node, 2); // height
		} else if (node.clan.clanType == Clan.SINGLETON) {
			if (!repeat) {
				size = graph.getNode(node.clan.id).getBoundingBox();
				size.x += distH;
				size.y += distV;
			} else {
				size.x = node.size.x;
				size.y = node.size.y;
			}
		} else {
			//	independent
			if (node.size.y < childMax(node, 2)) {
				size.y = childMax(node, 2);
			} else
				size.y = node.size.y;
			// Adjust the vertical spacing of the children of linear
			// children, and the width of singleton children.
			setExtras(node, size.y);
			size.x = childSum(node, 1);
		}

		return size;
	}

	private double childMax(ParseClanTree node, int axis) {
		double max = 0;

		ParseClanTree child;
		for (child = node.firstChild; child != null; child = child.nextSibling)
			if ((axis == 1 && child.size.x > max)
					|| (axis == 2 && child.size.y > max))
				max = axis == 1 ? child.size.x : child.size.y;

		return max;
	}

	private double childSum(ParseClanTree node, int axis) {
		double sum = 0;

		ParseClanTree child;
		for (child = node.firstChild; child != null; child = child.nextSibling)
			sum += axis == 1 ? child.size.x : child.size.y;

		return sum;
	}

	/**
	 * Find extra spacing for the children of linear clans.
	 */ 
	private void setExtras(ParseClanTree node, double height) {
		ParseClanTree child;
		for (child = node.firstChild; child != null; child = child.nextSibling)
			if (child.clan.clanType == Clan.LINEAR
					&& child.size.y < height) {
				int children = 0;
				for (ParseClanTree tmp = child.firstChild; tmp != null; tmp = tmp.nextSibling)
					children++;
				if (children > 1) {
					child.extraheight = (height - child.size.y) / ((double) (children - 1));
					child.size.y = height;
				}
			}
	}

	// Recursive
	private void fillLeftSiblings(ParseClanTree node) {
		ParseClanTree tmpnode, prevnode;

		for (tmpnode = node.firstChild, prevnode = null; tmpnode != null; prevnode = tmpnode, tmpnode = tmpnode.nextSibling)
			tmpnode.leftSibling = prevnode;

		for (tmpnode = node.firstChild; tmpnode != null; tmpnode = tmpnode.nextSibling)
			fillLeftSiblings(tmpnode);
	}
	
	// Recursive.
	private void bbCornerAttribute(ParseClanTree node) {
		if (node == null)
			return;

		if (node.parent == null) {
			node.position.x = 0;
			node.position.y = 0;
		} else if (node.parent.clan.clanType == Clan.LINEAR) {
			node.position.x = node.parent.position.x
					+ (node.parent.size.x - node.size.x) / 2.0;
			if (node.leftSibling != null)
				node.position.y = node.leftSibling.position.y
						- node.leftSibling.size.y
						- node.parent.extraheight;
			else
				node.position.y = node.parent.position.y;

		} else // parent is independent
		{
			node.position.y = node.parent.position.y
					- (node.parent.size.y - node.size.y) / 2.0;
			if (node.leftSibling != null)
				node.position.x = node.leftSibling.position.x
						+ node.leftSibling.size.x;
			else
				node.position.x = node.parent.position.x;

		}

		bbCornerAttribute(node.firstChild);
		bbCornerAttribute(node.nextSibling);
	}

	/** 
	 *  Fill the lookup table that relates graph nodes to
	 *  clan tree nodes.
	 */
	private void setLookup(ParseClanTree node) {
		if (node == null)
			return;

		if (node.clan.clanType == Clan.SINGLETON)
			treeLookup[node.clan.id] = node;
		setLookup(node.firstChild);
		setLookup(node.nextSibling);
	}

	/**
	 * Fill the heightInTree fields in the clan tree nodes.
	 */ 
	private void setHeightInTree(ParseClanTree node, int height) {
		if (node == null)
			return;

		node.heightInTree = height;
		setHeightInTree(node.firstChild, height + 1);
		setHeightInTree(node.nextSibling, height);
	}

	private void longEdgeHeuristic() {
		int i;
		int old_numnodes = numNodes;

		for (i = 0; i < old_numnodes; i++) {
			if(graph.getNode(i)==null)
				continue;
			CGDTreeSet<Integer> children = (CGDTreeSet<Integer>) graph.getNode(i).getChildren().clone();
			Iterator it=children.iterator();
			while(it.hasNext()){
				int j=(Integer)it.next();
				ParseClanTree nodei, nodej;
				nodei = treeLookup[i];
				nodej = treeLookup[j];

				int topnode = i, bottomnode = j;

				ParseClanTree sparenti = nodei.parent;
				ParseClanTree sparentj = nodej.parent;

				while (sparenti != null
						&& sparenti.clan.clanType != Clan.LINEAR)
					sparenti = sparenti.parent;
				while (sparentj != null
						&& sparentj.clan.clanType != Clan.LINEAR)
					sparentj = sparentj.parent;

				if (sparenti != null && sparentj != null) {
					// Find least common ancestor and left-right order.
					ParseClanTree lca = nodei;
					ParseClanTree lca2 = nodej;
					while (lca.parent != lca2.parent) {
						if (lca.heightInTree >= lca2.heightInTree)
							lca = lca.parent;
						if (lca.heightInTree < lca2.heightInTree)
							lca2 = lca2.parent;
					}

					ParseClanTree left_node, right_node;
					for (; lca2 != null && lca2 != lca; lca2 = lca2.nextSibling)
						;
					if (lca2 == null) // Then lca2 was on the right.
					{
						left_node = nodei;
						right_node = nodej;
					} else {
						left_node = nodej;
						right_node = nodei;
					}
					lca = lca.parent;

					ParseClanTree tmpnode;
					for (; left_node.parent != lca; left_node = left_node.parent)
						if (left_node.parent.clan.clanType == Clan.LINEAR)
							for (tmpnode = left_node.nextSibling; tmpnode != null; tmpnode = tmpnode.nextSibling)
								topnode = addDummy(tmpnode, topnode,
										bottomnode, nodei, nodej);
					for (; right_node.parent != lca; right_node = right_node.parent)
						if (right_node.parent.clan.clanType == Clan.LINEAR)
							for (tmpnode = right_node.leftSibling; tmpnode != null; tmpnode = tmpnode.leftSibling)
								bottomnode = addDummy(tmpnode, topnode,
										bottomnode, nodei, nodej);
					for (left_node = left_node.nextSibling; left_node != right_node; left_node = left_node.nextSibling)
						topnode = addDummy(left_node, topnode, bottomnode,
								nodei, nodej);
				}
			}
		}
	}

	/**
	 *  Add a dummy graph node and tree node.
	 */ 
	public int addDummy(ParseClanTree treenode, int top, int bottom,
			ParseClanTree edgesource, ParseClanTree edgesink) {
		// Insert the dummy node.
		numNodes++;
		int newnodeindex = graph.getNodes().size();
		graph.removeEdge(top, bottom);
		graph.addEdge(top, newnodeindex);
		graph.addEdge(newnodeindex, bottom);
		graph.addNode(newnodeindex);
		
		// If node is a singleton, make it an independent with
		// a singleton child.
		if (treenode.clan.clanType == Clan.SINGLETON) {
			ParseClanTree copy = new ParseClanTree();
			copy.clan = treenode.clan;
			copy.size.setLocation(treenode.size);
			copy.position.setLocation(treenode.position); // move
			copy.parent = treenode;

			treenode.clan = new Clan(Clan.INDEPENDENT, new CGDTreeSet<Integer>(), null, null, 0);
			treenode.firstChild = copy;
		}

		// Create the new tree node.
		ParseClanTree tnode = new ParseClanTree();
		CGDTreeSet<Integer> newTreeSet=new CGDTreeSet<Integer>();
		newTreeSet.add(newnodeindex);
		tnode.clan = new Clan(Clan.SINGLETON, newTreeSet, null, null, 0);
		tnode.clan.id = newnodeindex;
		tnode.parent = treenode;

		// Find the desired position.
		double x1 = edgesource.position.x + edgesource.size.x / 2.0; // width
		double y1 = edgesource.position.y + edgesource.size.y / 2.0; // height
		double x2 = edgesink.position.x + edgesink.size.x / 2.0; // width
		double y2 = edgesink.position.y + edgesink.size.y / 2.0; // height

		double y = treenode.position.y + treenode.size.y / 2.0;
		double idealx = x2 + (x1 - x2) / (y1 - y2) * (y - y2);

		// Find out which bounding box border the desired position
		// is closest to.
		int closest = 0;
		int index = 1;
		double cdist = Math.abs(treenode.position.x - idealx);
		for (ParseClanTree tmpnode = treenode.firstChild; tmpnode != null; tmpnode = tmpnode.nextSibling) {
			if (Math.abs(tmpnode.position.x + tmpnode.size.x - idealx) <= cdist) {
				cdist = Math.abs(tmpnode.position.x + tmpnode.size.x
						- idealx);
				closest = index;
			}
			index++;
		}

		// Insert the new tree node at the proper position.
		if (closest == 0) {
			tnode.nextSibling = treenode.firstChild;
			treenode.firstChild = tnode;
		} else {
			ParseClanTree tmpnode = treenode.firstChild;
			for (index = 1; index < closest; index++)
				tmpnode = tmpnode.nextSibling;

			tnode.nextSibling = tmpnode.nextSibling;
			tmpnode.nextSibling = tnode;
			tnode.leftSibling = tmpnode;
		}

		if (tnode.nextSibling != null)
			tnode.nextSibling.leftSibling = tnode;

		tnode.size.setLocation(distH, distV);
		CGDNode newnode = graph.getNode(newnodeindex);
		newnode.setBoundingBox(distH, distV);

		return newnodeindex;
	}

	// Recursive.
	private void realSizes(ParseClanTree node) {
		if (node == null)
			return;

		if (node.parent != null)
			if (node.parent.clan.clanType == Clan.LINEAR) {
				node.size.x = node.parent.size.x; // width
				node.position.x = node.parent.position.x;
			} else {
				// parent is independent
				node.size.y = node.parent.size.y; // height
				node.position.y = node.parent.position.y;
			}

		realSizes(node.firstChild);
		realSizes(node.nextSibling);
	}

	/**
	 * Add dummy nodes for edges that might intersect
	 * nodes because of a steep angle.
	 */ 
	private void angleFix() {
		int i, j, k;

		for (i = 0; i < numNodes; i++) {
			ParseClanTree tnodei = treeLookup[i];
			for (k = 0; k < 2; k++) {
				CGDTreeSet connections;
				if (k == 0)
					connections = graph.parents(i);
				else
					connections = graph.children(i);
				
				Iterator it=connections.iterator();
				while(it.hasNext()){
					j=(Integer)it.next();
					double x1, x2, y1, y2;

					if (j < numNodes) {
						ParseClanTree tnodej = treeLookup[j];
						x2 = tnodej.position.x + tnodej.size.x / 2.0; // width
						y2 = tnodej.position.y - tnodej.size.y / 2.0; // height

					} else {
						Point2D.Double pos = graph.getNode(j).getPosition();
						x2 = pos.x;
						y2 = pos.y;
					}

					x1 = tnodei.position.x + tnodei.size.x / 2.0; // width
					y1 = tnodei.position.y - tnodei.size.y / 2.0; // height

					double dx, dy;
					dx = Math.abs(x1 - x2);
					if (dx == 0)
						continue;
					dy = Math.abs(y1 - y2);

					double dy2;
					double dist;
					dy2 = dy / dx * tnodei.size.x / 2.0; // width
					dist = tnodei.size.y / 2.0; // height
					dist -= dy2;
					if (dist <= distV / 2.0)
						continue;

					double offs = dy * dy / (dx * dx + dy * dy);
					offs = Math.sqrt(offs);
					offs *= tnodei.size.x / 2.0; // width
					if (x2 > x1)
						offs = tnodei.size.x - offs; // width

					int newnodeindex = graph.getNodes().size()-1;
					CGDNode newnode = graph.getNode(newnodeindex);

					if (k == 0) {
						graph.removeEdge(j, i);
						graph.addEdge(j, newnodeindex);
						graph.addEdge(newnodeindex, i);

						newnode.setPosition(tnodei.position.x + offs,
								tnodei.position.y - distV / 4.0);
					} else {
						graph.removeEdge(i, j);
						graph.addEdge(i, newnodeindex);
						graph.addEdge(newnodeindex, j);

						newnode.setPosition(tnodei.position.x + offs,
								tnodei.position.y - tnodei.size.y
										+ distV / 4.0);
					}
				}
			}
		}
	}
	
	// Recursive.
	private void copyCorner(ParseClanTree node) {
		if (node == null)
			return;

		if (node.clan.clanType == Clan.SINGLETON) {
			Point2D.Double center_position = new Point2D.Double(node.position.x,node.position.y);
			center_position.x += node.size.x / 2.0; // width
			center_position.y -= node.size.y / 2.0; // height
			graph.getNode(node.clan.id).setPosition(center_position);
		}

		copyCorner(node.firstChild);
		copyCorner(node.nextSibling);
	}

	private void removeBends() {
		boolean needed;

		ArrayList nodes=graph.getNodes();
		for (int i=0; i<nodes.size();i++){
			CGDNode tmpnode=(CGDNode)nodes.get(i);
			if (tmpnode.getIndex() >= initialNumNodes) {
				needed = false;
				int index = tmpnode.getIndex();
				CGDNode node1=null;
				CGDNode node2=null;
				try{
					node1 = graph.getNode((Integer)graph.parents(index).first());
				}catch(Exception e){
					System.out.println("GraphUtil.removeBends():Parent not found for node="+index+".");
					return;
				}
				try{
					node2 = graph.getNode(tmpnode.getChildren().first());
				}catch(Exception e){
					System.out.println("GraphUtil.removeBends():Child not found for node="+index+".");
					return;
				}
				Point2D.Double p1 = node1.getPosition();
				Point2D.Double p2 = node2.getPosition();
				double dxdy = (p2.x - p1.x) / (p2.y - p1.y);

				double minx = Math.min(p1.x, p2.x);
				double miny = Math.min(p1.y, p2.y);
				double maxx = Math.max(p1.x, p2.x);
				double maxy = Math.max(p1.y, p2.y);

				for (int j=0;j<nodes.size();j++){
					CGDNode tmpnode2=(CGDNode)nodes.get(i);;
					if (tmpnode2.getIndex() < initialNumNodes && 
							tmpnode2 != node1 && tmpnode2 != node2) {
						Point2D.Double p = tmpnode2.getPosition();
						Point2D.Double bbx = tmpnode2.getBoundingBox();
						double x1 = p.x - bbx.x / 2.0 - distH / 10.0; // width
						double x2 = p.x + bbx.x / 2.0 + distH / 10.0; // width
						double y1 = p.y - bbx.y / 2.0 - distV / 10.0; // height
						double y2 = p.y + bbx.y / 2.0 + distV / 10.0; // height

						if (x1 <= minx && x2 <= minx || x1 >= maxx
								&& x2 >= maxx)
							continue;
						if (y1 <= miny && y2 <= miny || y1 >= maxy
								&& y2 >= maxy)
							continue;

						double cross;
						if (y1 > miny && y1 < maxy) {
							cross = p1.x + dxdy * (y1 - p1.y);
							if (x1 <= cross && cross <= x2) {
								needed = true;
								break;
							}
						}
						if (y2 > miny && y2 < maxy) {
							cross = p1.x + dxdy * (y2 - p1.y);
							if (x1 <= cross && cross <= x2) {
								needed = true;
								break;
							}
						}
						if (x1 > minx && x1 < maxx) {
							cross = p1.y + (x1 - p1.x) / dxdy;
							if (y1 <= cross && cross <= y2) {
								needed = true;
								break;
							}
						}
						if (x2 > minx && x2 < maxx) {
							cross = p1.y + (x2 - p1.x) / dxdy;
							if (y1 <= cross && cross <= y2) {
								needed = true;
								break;
							}
						}
					}
				}
				if (!needed) {
					graph.removeEdge(node1.getIndex(), index);
					graph.removeEdge(index, node2.getIndex());
					graph.addEdge(node1.getIndex(), node2.getIndex());
				}
			}
		}
	}

	/**
	 * Convert dummy nodes to edge paths.
	 */
	public void dummysToEdgePaths(CGDGraph _graph) {
		graph=_graph;
		CGDNode tmpnode;
		ArrayList nodes=graph.getNodes();
		for (int i=0;i<nodes.size();i++) {
			tmpnode=(CGDNode)nodes.get(i);
			if (!tmpnode.isDummy){
				Iterator it=tmpnode.children.iterator();
				while(it.hasNext()){
					int child = (Integer)it.next();
					CGDNode childnode = graph.getNode(child);
					CGDNode tmpchild = childnode;
					int numdummies = 0;

					// Only the first child is used.
					while (tmpchild != null && tmpchild.isDummy) {
						numdummies++;
						tmpchild = graph.getNode(tmpchild.children.first());
					}
					if (numdummies > 0 && tmpchild != null) {
						Point2D.Double[] edge_points = new Point2D.Double[numdummies];
						tmpchild = childnode;
						int dummy = 0;
						while (tmpchild.isDummy) {
							Point2D.Double position=tmpchild.getPosition();
							edge_points[dummy++] = new Point2D.Double(position.x,position.y);
							tmpchild = graph.getNode(tmpchild.children.first());
						}
						// tmpnode is the first nodummy node of the edge
						// tmpchild is the last nodummy node of the edge
						graph.addEdge(tmpnode.getIndex(), tmpchild.getIndex(),edge_points);
					}
				}
			}
		}

		// Remove all dummy nodes.
		for (int j=0;j<nodes.size();j++){
			tmpnode=(CGDNode)nodes.get(j);
			if (tmpnode.isDummy)
				nodes.remove(j);
		}
	}
}