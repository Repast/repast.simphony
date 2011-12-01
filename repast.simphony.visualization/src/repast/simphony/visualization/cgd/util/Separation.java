package repast.simphony.visualization.cgd.util;

import java.util.Iterator;
import java.util.TreeSet;

public class Separation {
	public final static int SIBLING = 0;
	public final static int MATE = 1;
	
	private int count;

	private TreeSet[] allNodes; // set of nodes in this partition

	private TreeSet[] tmpSet;

	private TreeSet[] mixSet; // above set's nodes + descendents (ancestors)

	private int type; // SIBLING or MATE
	
	public Separation(int _type, TreeSet[] childRelation, TreeSet[] parentRelation,
			TreeSet[] descendentRelation, TreeSet[] ancestorRelation, int _numNodes,
			TreeSet allNodeSet) {
		type= _type;

		// The number of all nodes is the maximum number of TreeSets.
		int size=allNodeSet.size();
		tmpSet = new TreeSet[size];
		mixSet = new TreeSet[size];
		allNodes = new TreeSet[size];

		count = 0;
		int j=0;
		int i=0;
		Iterator it=allNodeSet.iterator();
		while(it.hasNext()){
			i=(Integer)it.next();
			TreeSet ts;
			if(type==SIBLING)
				ts=(CGDTreeSet) parentRelation[i].clone(); // parent of i.
			else
				ts=(CGDTreeSet) childRelation[i].clone();  // child of i.
			
			((CGDTreeSet)ts).intersect(allNodeSet);
			for (j = 0; j < count; j++)
				if (ts.equals(tmpSet[j]))
					break;
			
			if (j >= count){
				count++;
				tmpSet[j] = ts;
				allNodes[j] = new CGDTreeSet<Integer>();
			}

			allNodes[j].add(new Integer(i));
		}
		
		for(int k=0;k<count;k++){
			if(allNodes[k]==null)
				allNodes[k]=new CGDTreeSet<Integer>();
			
			mixSet[k]=(CGDTreeSet)allNodes[k].clone();
			if(type==SIBLING)
				((CGDTreeSet)mixSet[k]).indexUnion(descendentRelation,allNodes[k]);
			else
				((CGDTreeSet)mixSet[k]).indexUnion(ancestorRelation,allNodes[k]);
				     
			((CGDTreeSet)mixSet[k]).intersect(allNodeSet);
		}
	}
	
	public int size(){
		return count;
	}
	public TreeSet mixSetTree(int i){
		return mixSet[i];
	}
	public TreeSet members(int i) {
		return allNodes[i];
	}
}