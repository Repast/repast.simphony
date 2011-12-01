/**
 * 
 */
package repast.simphony.relogo



/**
 * @author jozik
 *
 */
public class AgentSetTest extends GroovyTestCase{
	
	public void testWithMin(){
		def agentOne = new Expando(toString: {"a1"}, getVar1: {1})
		def agentTwo = new Expando(toString: {"a2"}, getVar1: {1})
		def agentThree = new Expando(toString: {"a3"}, getVar1: {2})
		def agentFour = new Expando(toString: {"a4"}, getVar1: {2})
		AgentSet a = new AgentSet([agentOne,agentTwo,agentThree,agentFour])
		def methodName = 'getVar1'
		def result = a.withMin({"$methodName"()})
		//Note: this could generate an error if the agentset is shuffled
		assertArrayEquals(result as Object[], [agentOne,agentTwo] as Object[])
		assertTrue(result.grep(agentOne) == [agentOne] && result.grep(agentTwo) == [agentTwo] && result.grep(agentThree) == [] && result.grep(agentFour) == [])
	}
	
	public void testWithMax(){
		def agentOne = new Expando(toString: {"a1"}, getVar1: {1})
		def agentTwo = new Expando(toString: {"a2"}, getVar1: {1})
		def agentThree = new Expando(toString: {"a3"}, getVar1: {2})
		def agentFour = new Expando(toString: {"a4"}, getVar1: {2})
		AgentSet a = new AgentSet([agentOne,agentTwo,agentThree,agentFour])
		def methodName = 'getVar1'
		def result = a.withMax({"$methodName"()})
		//Note: this could generate an error if the agentset is shuffled
		assertArrayEquals(result as Object[], [agentThree,agentFour] as Object[])
		assertTrue(result.grep(agentOne) == [] && result.grep(agentTwo) == [] && result.grep(agentThree) == [agentThree] && result.grep(agentFour) == [agentFour])
	}
	
	//TODO: create a test for the "of" method
}
