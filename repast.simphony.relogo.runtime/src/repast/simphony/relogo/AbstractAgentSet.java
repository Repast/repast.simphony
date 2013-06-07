package repast.simphony.relogo;

import groovy.lang.Closure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import repast.simphony.random.RandomHelper;
import repast.simphony.util.SimUtilities;

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
	
	/**
	 * Ask agentset to execute commands in random order.
	 * @param cl
	 */
	public void askAgentSet(Closure cl){
		cl.setResolveStrategy(Closure.DELEGATE_FIRST);
		ArrayList<ReLogoAgent> temp = new ArrayList<ReLogoAgent>(this);
		SimUtilities.shuffle(temp, RandomHelper.getUniform());
		for (ReLogoAgent o : temp){
			cl.setDelegate(o);
			cl.call(o);
		}
	}
}
