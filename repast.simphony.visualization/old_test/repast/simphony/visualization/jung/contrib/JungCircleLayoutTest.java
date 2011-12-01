package repast.visualization.jung.contrib;

import junit.framework.TestCase;
import repast.context.DefaultContext;
import repast.context.space.graph.NetworkFactoryFinder;
import repast.engine.environment.RunState;
import repast.space.graph.Network;

public class JungCircleLayoutTest extends TestCase{
	private DefaultContext<Object> context;
	private JungCircleLayout cl;

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
		net1.addEdge(tomorrow,hello);
		
		cl = new JungCircleLayout();
		cl.setProjection(net1);
		cl.setRadius(2.0);
		
	}
	
	public void testUpdate() {
		Object o=context.getRandomObject();
		float xy[]=cl.getLocation(o);
		
		cl.update();
		
		float xy1[]=cl.getLocation(o);
		
		assertTrue(xy[0]!=xy1[0]);
		assertTrue(xy[1]!=xy1[1]);
		
	}
}
