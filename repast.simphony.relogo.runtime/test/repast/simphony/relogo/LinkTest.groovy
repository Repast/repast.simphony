/**
 * 
 */
package repast.simphony.relogo

import repast.simphony.space.graph.RepastEdge
import static repast.simphony.relogo.Utility.*import repast.simphony.space.graph.UndirectedJungNetworkimport repast.simphony.space.graph.DirectedJungNetwork
/**
 * @author jozik
 *
 */
public class LinkTest extends GroovyTestCase{
	
	public void testBothEnds(){		
		def source = "source"
		def target = "target"
		def link = new BaseLink(source,target,true)
		assert(link.bothEnds().getClass() == AgentSet)
		assert(link.bothEnds().size() == 2)
		assertTrue(link.bothEnds().any {it == "source"})
		assertTrue(link.bothEnds().any {it == "target"})
	}
	
	public void testLinkCasting(){
		BaseLink l = new BaseLink(new Integer(1), new Integer(2), true);
		l.setLinkVar1(67);
		assert(l.getLinkVar1() == 67)
		RepastEdge re = (RepastEdge) l;
		re.setWeight(1.45);
		Link rle2 = (Link) re;
		assert(rle2.getLinkVar1() == 67)
		rle2.setLinkVar1("45");
		assert(rle2.getLinkVar1() == '45')
	}
		
	public void testJungNetworks(){
		UndirectedJungNetwork network1 = new UndirectedJungNetwork("undirected")
		RepastEdge e1 = network1.addEdge("source","target",1)
		RepastEdge e2 = network1.getEdge("target","source")
		RepastEdge e3 = network1.getEdge("source","target")
		assert(e2 == e1)
		assert(e3 == e2)
		DirectedJungNetwork network2 = new DirectedJungNetwork("directed")
		RepastEdge e4 = network2.addEdge("source","target",1)
		RepastEdge e5 = network2.getEdge("target","source")
		RepastEdge e6 = network2.getEdge("source","target")
		assert(e5 == null)
		assert(e6 == e4)
		assert(e3 == e2)
		RepastEdge e7 = new RepastEdge("s1","s2",false)
		assert(!e7.isDirected())
		println e7
		println e7.isDirected()
		network2.addEdge(e7)
		assert(e7.isDirected())
		println e7
		println e7.isDirected()
		RepastEdge e8 = network2.getEdge("s1","s2")
		assert(e7.isDirected())
	}
	
	
}
