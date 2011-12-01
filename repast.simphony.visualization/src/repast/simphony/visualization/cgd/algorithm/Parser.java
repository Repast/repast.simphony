package repast.simphony.visualization.cgd.algorithm;



import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

import repast.simphony.visualization.cgd.util.CGDTreeSet;
import repast.simphony.visualization.cgd.util.Clan;
import repast.simphony.visualization.cgd.util.ParseClanTree;
import repast.simphony.visualization.cgd.util.Separation;

public class Parser {
	private Clan firstClan;
	private ParseClanTree root=null;
	private int conComponents;
	private TreeSet[] conNodes;
	
	private TreeSet[] childR;
	private TreeSet[] parentR;
	private TreeSet[] descendentR;
	private TreeSet[] ancestorR;

	private HashMap<Integer,Integer> topObjOrder; // node's index, level.
	private int numNodes;
	
	public ParseClanTree parseSet(TreeSet[] _childR, TreeSet[] _parentR, TreeSet[] _descendentR, 
			TreeSet[] _ancestorR, TreeSet allNodes){
		
		childR=_childR;
		parentR=_parentR;
		descendentR=_descendentR;
		ancestorR=_ancestorR;
		
		ParseClanTree pct=parseSet(allNodes);
		
		breakPrimitives(pct);
		
		// Reduce the tree, i.e., for any node whose type is the same
		// as it's parent's type, replace the node by it's children.
		pct.reduce();
		
		// Assign unique id numbers to each clan.
		pct.setId(numNodes);

		return pct;
	}
	
	private ParseClanTree parseSet(TreeSet allNodes){
		ParseClanTree pct=null;
		// Partition nodes into sets S(0), ..., S(n-1) and M(0), ..., M(m-1)
		Separation S = new Separation(Separation.SIBLING, childR,
				parentR, descendentR, ancestorR,
				numNodes, allNodes);
		Separation M = new Separation(Separation.MATE, childR,
				parentR, descendentR, ancestorR,
				numNodes, allNodes);
		
		// Initialize the list of clans.
		firstClan = null;

		// Find clans as legal connected components of nodes between all
		// pairs from an S set and an M set.
		for (int i = 0; i < S.size(); i++){
			for (int j = 0; j < M.size(); j++) {
				TreeSet F = (CGDTreeSet) (S.mixSetTree(i).clone());
				((CGDTreeSet)F).intersect(M.mixSetTree(j));

				if (F.size() > 1) {
					// Break up F into its connected components.
					makeConnectedComponents(F,descendentR,ancestorR);

					// If more than one connected component is legal, then
					// the union of all legal components is an independent clan.
					// So start a count of legal components and a set of nodes.
					int legal_components = 0;
					CGDTreeSet legal_nodes = new CGDTreeSet<Integer>();

					Clan candidates = null;
				
					for (int k = 0; k < conComponents; k++) {
						if (conNodes[k].size() > 1) {
							// Get the sources and sinks of this component
							CGDTreeSet sts = (CGDTreeSet) (S.members(i).clone());
							sts.intersect(conNodes[k]);
							CGDTreeSet mts = (CGDTreeSet) (M.members(j).clone());
							mts.intersect(conNodes[k]);
							
							// Add relatives for D* and A* sets.
							CGDTreeSet ds = (CGDTreeSet) sts.clone();
							CGDTreeSet am = (CGDTreeSet) mts.clone();
							ds.indexUnion(descendentR, sts);
							am.indexUnion(ancestorR, mts);
							
							// Combine for A* U D* sets.
							CGDTreeSet ads = (CGDTreeSet) ds.clone();
							CGDTreeSet adm = (CGDTreeSet) am.clone();
							ads.indexUnion(ancestorR, sts);
							adm.indexUnion(descendentR, mts);

							// Restrict all sets to the current subset.
							ds.intersect(allNodes);
							am.intersect(allNodes);
							ads.intersect(allNodes);
							adm.intersect(allNodes);

							// Legal component if D*(S) <= A*(M) U D*(M)
							// and A*(M) <= A*(S) U D*(S)
							if (adm.isSubset(ds) && ads.isSubset(am)) {
								Clan clan = new Clan(Clan.UNKNOWN, conNodes[k],
										sts, mts, nodeOrder(conNodes[k]));
								clan.next = candidates;
								candidates = clan;
								legal_components++;
								legal_nodes.union(conNodes[k]);
							}
						} else {
							// The component is a single node, so it is
							// legal. Add to the set for an independent
							// clan, but do not add to the candidates list.
							legal_components++;
							legal_nodes.union(conNodes[k]);
						}
					}
					
					// If more than one component is legal, then the union of
					// all legal clans is an independent clan.
					if(legal_components > 1){
						CGDTreeSet sources = (CGDTreeSet)legal_nodes.clone();
						CGDTreeSet sinks = (CGDTreeSet)legal_nodes.clone();
						sources.intersect(S.members(i));
						sinks.intersect(M.members(j));
						
						Clan clan = new Clan(Clan.INDEPENDENT, legal_nodes, 
								sources, sinks, nodeOrder(sources));
						clan.next = candidates;
						candidates = clan;
					}
					
					mergeCandidateToClanList(candidates);
				}
			}
		}
		
		// Build the parse tree
		pct=buildParseTree(allNodes);
		
		// Correctly label linear clans.
		pct.fixLinear(allNodes,childR,parentR);
		
		return pct; // return to algorithm.
	}

	void makeConnectedComponents(TreeSet f, TreeSet[] descendentR, 
			TreeSet[] ancestorR) {
		int n;

		conComponents = 0;

		conNodes = new TreeSet[f.size()];
		Iterator it=f.iterator();
		while (it.hasNext()) {
			n=(Integer)it.next();
			conNodes[conComponents] = new CGDTreeSet<Integer>();
			CGDTreeSet<Integer> c = (CGDTreeSet)conNodes[conComponents];

			c.add(n);
			CGDTreeSet tmpset = (CGDTreeSet) (ancestorR[n].clone());
			tmpset.union(descendentR[n]);
			tmpset.intersect(f);
			c.union(tmpset);
			int last_size = -1;
			while (c.size() != last_size) {
				last_size = c.size();
				tmpset = new CGDTreeSet<Integer>();
				tmpset.indexUnion(ancestorR, c);
				tmpset.indexUnion(descendentR, c);
				tmpset.intersect(f);
				c.union(tmpset);
			}
			((CGDTreeSet)f).difference(c);
			it=f.iterator();
			conComponents++;
		}
	}

	private int nodeOrder(TreeSet nodeSet) {
		int order = numNodes;
		int n,tmp;
		Iterator it=nodeSet.iterator();
		while(it.hasNext()){
			n=(Integer)it.next();
			tmp=topObjOrder.get(n);
			if(tmp < order)
				order = tmp;
		}

		return order;
	}
	
	private void mergeCandidateToClanList(Clan _candidates){
		while(_candidates!=null){
			Clan c=_candidates;
			_candidates=_candidates.next;
			
			TreeSet cNodes=c.nodes;
			
			// Scan the clan list from smallest to largest.
			Clan clan, prevClan = null;
			for(clan=firstClan; clan != null; prevClan=clan, clan=clan.listnext){
				TreeSet clanNodes=clan.nodes;
				if(cNodes.equals(clanNodes)){
					if(c.clanType != Clan.UNKNOWN)
						clan.clanType = c.clanType;
					c=null;
					break;
				}
				if(((CGDTreeSet)cNodes).intersects(clanNodes) &&
						!(((CGDTreeSet)clanNodes).isSubset(cNodes) || ((CGDTreeSet)cNodes).isSubset(clanNodes))){
					((CGDTreeSet)cNodes).union(clanNodes);
					c.size=cNodes.size();
					c.clanType=Clan.LINEAR;
					
					// Remove from the list.
					if(prevClan != null)
						prevClan.listnext=clan.listnext;
					else
						firstClan=clan.listnext;
				}
			}
			if(c != null){
				// If the current candidate clan didn't get labeled
				// yet, mark it as a primitive clan.
				if (c.clanType == Clan.UNKNOWN)
					c.clanType = Clan.PRIMITIVE;

				// Transfer the candidate onto the clans list.
				addToClanList(c);				
			}
		}
	}

	private void addToClanList(Clan clan) {
		if (firstClan == null) {
			firstClan = clan;
			return;
		}
		if (firstClan.size > clan.size) {
			clan.listnext = firstClan;
			firstClan = clan;
			return;
		}

		Clan tmp = firstClan;
		while (tmp.listnext != null && tmp.listnext.size < clan.size)
			tmp = tmp.listnext;

		clan.listnext = tmp.listnext;
		tmp.listnext = clan;
	}
	
	private ParseClanTree buildParseTree(TreeSet _allNodes){
		ParseClanTree pct=new ParseClanTree();
		
		// Reverse the clan list using the "next" field.
		Clan lClan=null; // largest clan.
		Clan tmpClan=null; 
		if(firstClan!=null){
			firstClan.next = null;
		
			for( lClan=firstClan ; lClan.listnext != null ; lClan=lClan.listnext ){
				lClan.listnext.next = lClan;
			}
		}
		// The root of the tree is the largestclan found - all nodes set.
		if(root==null)
			root=pct;
		
		pct.clan = lClan;
		if(lClan!=null)
			lClan=lClan.next;
		
		while((tmpClan = lClan) != null){
			lClan = lClan.next;
			pct.addClan(tmpClan);
		}
		
		// Add singleton clans.
		Integer in=null;
		Iterator it=_allNodes.iterator();
		while(it.hasNext()){
			CGDTreeSet<Integer> ts=new CGDTreeSet<Integer>();
			in=(Integer)it.next();
			ts.add(in);
			
			Clan clan=new Clan(Clan.SINGLETON, ts, ts, ts, topObjOrder.get(in));
			pct.addClan(clan);
		}
		
		return pct;
	}

	public void setTopObjOrder(HashMap<Integer,Integer> _topObjOrder){
		topObjOrder=_topObjOrder;
	}
	public void setNumNodes(int _numNodes){
		numNodes=_numNodes;
	}
	
	private ParseClanTree breakPrimitives(ParseClanTree node) {		
		// Scan the parse tree and break down any primitives.
		// Each primitive clan will be converted into a linear clan
		// containing a (pseudo) independent and one other clan.
		// The independent will consist of all sub-clans of the
		// primitive that contain its sources.
		// The other clan will be a re-parse of the remaining nodes
		// (original primitive clan nodes - nodes added to the pseudo
		// independent).
		if (node.clan.clanType == Clan.PRIMITIVE) {
			// Re-use the existing tree node - just change its label
			node.clan.clanType = Clan.LINEAR;

			// Create a new tree for the independent - it will be
			// attached to the linear after adding all the source
			// sub-clans of the original primitive.
			ParseClanTree ind_tree = new ParseClanTree();
			ind_tree.clan = new Clan(Clan.PSEUDOINDEPENDENT, new CGDTreeSet<Integer>(),
					node.clan.sources, new CGDTreeSet<Integer>(), node.clan.order);

			// As sub-clans are added to the independent, their
			// nodes are subtracted from this set. Start with all
			// nodes from the primitive clan.
			CGDTreeSet remaining_nodes = (CGDTreeSet) (node.clan.nodes.clone());

			int currMax = 0;
			int i=0;
			int tmp;
			Iterator it=node.clan.sources.iterator();
			while(it.hasNext()){
				i=(Integer)it.next();
				tmp=topObjOrder.get(i); // level for node "i".
				if (tmp > currMax)
					currMax = tmp;
			}

			ParseClanTree subclan;
			while ((subclan = node.firstChild) != null) {
				node.firstChild = node.firstChild.nextSibling;
				if (((CGDTreeSet)node.clan.sources).intersects(subclan.clan.sources)) {
					// This subclan contains some of the primitive's
					// sources. So add its nodes and structure to the
					// new independent.
					if (topObjOrder.get((Integer)subclan.clan.sources.first()) >= currMax) {
						((CGDTreeSet)ind_tree.clan.nodes).union(subclan.clan.nodes);
						((CGDTreeSet)ind_tree.clan.sinks).union(subclan.clan.sinks);
						ind_tree.addChild(subclan);

						// Remove the nodes from this source subclan from
						// the set of all nodes of the primitive clan.
						remaining_nodes.difference(subclan.clan.nodes);
					} else {
						if (!(parentR[(Integer)subclan.clan.sources.first()]
								.isEmpty())) {
							((CGDTreeSet)ind_tree.clan.nodes).union(subclan.clan.nodes);
							((CGDTreeSet)ind_tree.clan.sinks).union(subclan.clan.sinks);
							ind_tree.addChild(subclan);
							// Remove the nodes from this source subclan from
							// the set of all nodes of the primitive clan.
							remaining_nodes.difference(subclan.clan.nodes);
						}
						// Else, the subclan goes away.
					}
				}
				// Else, the subclan goes away.
			}

			node.addChild(ind_tree);
			node.addChild(parseSet(remaining_nodes));
		}

		ParseClanTree tmpclan;
		for (tmpclan = node.firstChild; tmpclan != null; tmpclan = tmpclan.nextSibling)
			breakPrimitives(tmpclan);
		
		return node;
	}

}