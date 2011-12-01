package repast.visualization.visualization3D;

import junit.framework.TestCase;
import repast.context.DefaultContext;
import repast.context.space.graph.NetworkFactoryFinder;
import repast.engine.environment.RunState;
import repast.space.graph.Network;
import repast.space.graph.RepastEdge;

import java.util.Iterator;
import java.util.Map;


public class JungFR3DTest extends TestCase{
	private DefaultContext<Object> context;
	private FR3DLayout fr;
	
	public void setUp() {
		RunState.init(null, null, null);
		
		context = new DefaultContext<Object>("parent");
		
		NetworkFactoryFinder.createNetworkFactory(null).createNetwork(
				context, "Network 1", true);
				
		String hello="Hello";
		String goodbye="Goodbye";
		String today="Today";
		
		context.add(hello);
		context.add(goodbye);
		context.add(today);
		
		Network<String> net1 = context.getProjection(Network.class, "Network 1");
		
		net1.addEdge(hello,goodbye);
		net1.addEdge(hello,today);
		
		fr = new FR3DLayout();
		fr.setProjection(net1);
		
	}
	
	public void testFRUpdate() {
		fr.update();
		
		Map location=fr.getLocationData();
		
		Iterator ic = location.values().iterator();
		
		double x=0;
		double y=0;
		double z=0;
		
		while(ic.hasNext()) {
			try {
				double xyz[]=(double[])ic.next();
				x=xyz[0];
				y=xyz[1];
				z=xyz[2];
				
				assertTrue(xyz.length==3);
			}
			
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		fr.update();
		Map loc=fr.getLocationData();
		Iterator il = loc.values().iterator();
		
		while(il.hasNext()) {
			double xyz[]=(double[])il.next();
			
			assertTrue(xyz[0]!=x);
			assertTrue(xyz[1]!=y);
			assertTrue(xyz[2]!=z);
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
