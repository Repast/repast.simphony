package repast.simphony.visualization.cgd.util;

import java.util.Iterator;
import java.util.TreeSet;


public class CGDTreeSet<I extends Number> extends TreeSet<I>{
	private static final long serialVersionUID = -2479143000061671589L;
	
	public CGDTreeSet(){
		super();
	}
	
	public void intersect(TreeSet b) {
		CGDTreeSet<Integer> tmp=(CGDTreeSet<Integer>)this.clone();
		Iterator it=tmp.iterator();
		while(it.hasNext()){
			I o=(I)it.next();
			if(!b.contains(o)){
				this.remove(o);
			}
		}
	}

	public void union(TreeSet child){
		Iterator it=child.iterator();
		I o;
		
		while(it.hasNext()){
			o=(I)it.next();
			this.add(o);
		}
	}
	
	public void indexUnion(TreeSet[] base, TreeSet index){
		Iterator it=index.iterator();
		while(it.hasNext()){
			Object o=it.next();
			this.union((TreeSet)base[((I)o).intValue()]);
		}
	}

	/**
	 * Removes each node from the parent list which is 
	 * a child for both parent and child node.
	 * 
	 * @param child
	 */
	public void difference(TreeSet child){
		TreeSet child2=(TreeSet)child.clone();
		
		Iterator it=child2.iterator();
		
		while(it.hasNext()){
			Object o=it.next();
			this.remove(o);
		}
		
		child.addAll(child2);
	}
	
	/**
	 * Check if ts is a subset of this set.
	 * 
	 * @param ts
	 * @return
	 */
	public boolean isSubset(TreeSet ts) {
		Iterator it=ts.iterator();
		
		while(it.hasNext()){
			Object o=it.next();
			if(!this.contains(o))
				return false;
		}
		
		return true;
	}
	
	public boolean intersects(TreeSet ts){
		TreeSet tmpL=null;
		TreeSet tmpS=null;
		if(this.size()>ts.size()){
			tmpL=this;
			tmpS=ts;
		} else {
			tmpL=ts;
			tmpS=this;
		}
		Iterator it=tmpL.iterator();
		while(it.hasNext()){
			I o=(I)it.next();
			if(tmpS.contains(o)){
				return true;
			}
		}
		
		return false;
	}
}