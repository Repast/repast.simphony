package repast.visualization.jung.contrib;

import junit.framework.TestCase;
import repast.context.DefaultContext;
import repast.context.space.graph.NetworkFactoryFinder;
import repast.engine.environment.RunState;
import repast.space.graph.Network;
import repast.space.graph.RepastEdge;

import java.util.Iterator;
import java.util.Map;


public class JungKKLayoutTest extends TestCase{
	private DefaultContext<Object> context;
	private JungKKLayout kkl;

	public void setUp() {
		RunState.init(null, null, null);
		
		context = new DefaultContext<Object>("parent");
		
		NetworkFactoryFinder.createNetworkFactory(null).createNetwork(
				context, "Network 1", true);
				
		String hello="Hello";
		String goodbye="Goodbye";
		String goodDay="GoodDay";
		String goodNight="GoodNight";
		String seeYaa="SeeYaa";
		String tomorrow="Tomorrow";
		
		context.add(hello);
		context.add(goodbye);
		context.add(goodDay);
		context.add(goodNight);
		context.add(seeYaa);
		context.add(tomorrow);
		
		Network<String> net1 = context.getProjection(Network.class, "Network 1");
		
		net1.addEdge(hello,goodbye);
		net1.addEdge(hello,goodDay);
		net1.addEdge(hello,goodNight);
		net1.addEdge(hello,seeYaa);
		net1.addEdge(goodDay,tomorrow);
		
		kkl = new JungKKLayout();
		kkl.setProjection(net1);
		kkl.initialize_local();
		
	}
	
	public void testKKUpdate() {
		kkl.update();
		
		Map location=kkl.getLocationData();
		
		Iterator ic = location.values().iterator();
		
		
		while(ic.hasNext()) {
			assertTrue(ic.next() instanceof double[]);
		}
	}
	
	public void testPositions() {
		Network<String> net1 = context.getProjection(Network.class, "Network 1");
		kkl.update();
		kkl.advancePositions();
		kkl.adjustForGravity();
		
		
		Object o = context.getRandomObject();
		kkl.forceMove(0,400,400);
		double xy[]=kkl.getCoordinates(o);
		
		
		Iterator i = (Iterator) net1.getEdges();
		
		while(i.hasNext()) {
			RepastEdge re = (RepastEdge) i.next();
			Object oo = re.getSource();
			Object ooo = re.getTarget();
			if(oo==o) {
				double xy1[] = kkl.getCoordinates(oo);
				assertTrue(xy1[1]==xy[1]);
			}
			
			if(ooo==o) {
				double xy1[] = kkl.getCoordinates(ooo);
				assertTrue(xy1[1]==xy[1]);
			}
		}
	}
	
	public void testDontMove() {
		Network<String> net1 = context.getProjection(Network.class, "Network 1");
		
		Object o = context.getRandomObject();
		double xy[]=kkl.getCoordinates(o);
		
		kkl.dontMove(o);
		kkl.update();
		double xy1[]=kkl.getCoordinates(o);
		
		assertTrue(xy1[0]==xy[0]);
		assertTrue(xy1[1]==xy[1]);
		
	}
}
