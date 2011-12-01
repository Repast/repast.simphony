package repast.visualization.jung;

import junit.framework.TestCase;
import repast.context.Context;
import repast.context.DefaultContext;
import repast.context.space.graph.NetworkFactoryFinder;
import repast.engine.environment.RunState;
import repast.space.graph.Graph;
import repast.space.graph.Network;
import repast.space.graph.RepastEdge;
import repast.visualization.cgd.CGDLayout;

public class CGDLayoutTest extends TestCase {

	private Context<Object> context;

	private CGDLayout cgd;
	
	public void setUp() {
		RunState.init(null, null, null);
		
		context = new DefaultContext<Object>("parent");
				
		String hello="Hello";
		String goodbye="Goodbye";
		String goodDay="GoodDay";
		String goodNight="GoodNight";
		String seeYaa="SeeYaa";
		String tomorrow="Tomorrow";
		String someDay="someDay";
		
		context.add(hello);
		context.add(goodbye);
		context.add(goodDay);
		context.add(goodNight);
		context.add(seeYaa);
		context.add(tomorrow);
		context.add(someDay);	

		RepastEdge e1 = new RepastEdge(hello,goodbye,true);
		RepastEdge e2 = new RepastEdge(hello,goodDay,true);
		RepastEdge e3 = new RepastEdge(hello,goodNight,true);
		RepastEdge e4 = new RepastEdge(hello,seeYaa,true);
		RepastEdge e5 = new RepastEdge(goodDay,tomorrow,true);
		RepastEdge e6 = new RepastEdge(goodDay,someDay,true);
		
		RepastEdge[] re = {e1,e2,e3,e4,e5,e6,};
		Object[] obj= {hello,goodbye,goodDay,goodNight,seeYaa,tomorrow,someDay};
		
		Network n=NetworkFactoryFinder.createNetworkFactory(null).createNetwork(
				context, "Network 1", true);
		Graph g = new Graph();

		n.addEdge(e1);
		n.addEdge(e2);
		n.addEdge(e3);
		n.addEdge(e4);
		n.addEdge(e5);
		n.addEdge(e6);
		
		cgd = new CGDLayout();
		cgd.setProjection(n);
		
	}
	
	public void testLocation() {
		cgd.update();
		
		Object o = context.getRandomObject();
		
		float xy[]=cgd.getLocation(o);
		
		cgd.update();
		
		float xy1[]=cgd.getLocation(o);
		
		assertTrue(xy[0]==xy1[0]);
		assertTrue(xy[1]==xy1[1]);
	}
}

