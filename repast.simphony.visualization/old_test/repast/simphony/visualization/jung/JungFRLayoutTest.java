package repast.visualization.jung;

import junit.framework.TestCase;
import repast.context.DefaultContext;
import repast.context.space.graph.NetworkFactoryFinder;
import repast.engine.environment.RunState;
import repast.space.graph.Network;
import repast.space.graph.RepastEdge;

import java.util.Iterator;
import java.util.Map;

public class JungFRLayoutTest extends TestCase{
	
	private DefaultContext<Object> context;
	private JungFRLayout fr;
	
	public void setUp() {
		RunState.init(null, null, null);
		
		context = new DefaultContext<Object>("parent");
		
		NetworkFactoryFinder.createNetworkFactory(null).createNetwork(
				context, "Network 1", true);
				
		String hello="Hello";
		String goodbye="Goodbye";
		context.add(hello);
		context.add(goodbye);
		
		Network<String> net1 = context.getProjection(Network.class, "Network 1");
		
		net1.addEdge(hello,goodbye);
		
		fr = new JungFRLayout();
		fr.setProjection(net1);
		
	}
	
	public void testFRUpdate() {
		fr.update();
		
		Map location=fr.getLocationData();
		
		Iterator ic = location.values().iterator();
		
		
		while(ic.hasNext()) {
			try {
				assertTrue(ic.next() instanceof double[]);
			}
			
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void testCalculate() {
		Network<String> net1 = context.getProjection(Network.class, "Network 1");
		fr.update();
		
		Object o = context.getRandomObject();
		fr.calcPositions(o);
		
		fr.calcRepulsion(o);
		
		Iterator i = (Iterator) net1.getEdges();
		
		while(i.hasNext()) {
			RepastEdge re = (RepastEdge) i.next();
			fr.calcAttraction(re);
		}
	}

}
