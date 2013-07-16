/**
 * 
 */
package repast.simphony.relogo



/**
 * @author jozik
 *
 */
public class AgentSet<E extends ReLogoAgent> extends AbstractAgentSet<E>{
		
	public AgentSet(){
		super()
	}
	
	public AgentSet(Collection<E> c){
		super(c)
	}
	
	/**
     * Reports a new agentset containing only those agents that have the minimum value of the given reporter.
     */ 
    public AgentSet withMin(Closure reporter){
		reporter.resolveStrategy = Closure.DELEGATE_FIRST
		Closure cl = { reporter.delegate = it; reporter(it) }
		def minValue = cl(this.min(cl))
		return new AgentSet(this.findAll({cl(it) == minValue}))
	}
	
    /**
     * Reports a new agentset containing only those agents that have the maximum value of the given reporter.
     */
    public AgentSet withMax(Closure reporter){
    	reporter.resolveStrategy = Closure.DELEGATE_FIRST
		Closure cl = { reporter.delegate = it; reporter(it) }
		def maxValue = cl(this.max(cl))
		return new AgentSet(this.findAll({cl(it) == maxValue}))
	}

// withR = with reporter
	public AgentSet with(Closure cl){
		cl.resolveStrategy = Closure.DELEGATE_FIRST
		def list = this.findAll {
			cl.delegate = it
			cl(it)
			} 
		return (new AgentSet(list))
	}
	
	public boolean equals(AgentSet two){
		if (this == two) return true
		else {
			def temp1 = this.clone()
			def temp2 = two.clone()
			return (temp1.sort() == temp2.sort())
		}
	}
		
	
}
