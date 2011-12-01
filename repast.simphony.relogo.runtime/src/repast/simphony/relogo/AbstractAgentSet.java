package repast.simphony.relogo;

import groovy.lang.Closure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public abstract class AbstractAgentSet<E extends ReLogoAgent> extends ArrayList<E> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1132438687545680786L;

	public AbstractAgentSet(){
		super();
	}
	
	public AbstractAgentSet(Collection<E> c){
		super(c);
	}
	
	public void askAgentSet(Closure cl){
		cl.setResolveStrategy(Closure.DELEGATE_FIRST);
		ArrayList<ReLogoAgent> temp = new ArrayList<ReLogoAgent>(this);
		Collections.shuffle(temp);
		for (ReLogoAgent o : temp){
			cl.setDelegate(o);
			cl.call(o);
		}
	}
}
