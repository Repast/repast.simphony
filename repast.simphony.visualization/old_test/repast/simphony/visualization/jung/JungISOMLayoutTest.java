package repast.visualization.jung;

import junit.framework.TestCase;
import repast.context.DefaultContext;
import repast.context.space.graph.NetworkFactoryFinder;
import repast.engine.environment.RunState;
import repast.space.graph.Network;
import repast.space.graph.RepastEdge;

import java.util.Iterator;
import java.util.Map;

public class JungISOMLayoutTest extends TestCase{
	private DefaultContext<Object> context;
	private JungISOMLayout isom;

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
		
		context.add(hello);
		context.add(goodbye);
		context.add(goodDay);
		context.add(goodNight);
		context.add(seeYaa);
		
		Network<String> net1 = context.getProjection(Network.class, "Network 1");
		
		net1.addEdge(hello,goodbye);
		net1.addEdge(hello,goodDay);
		net1.addEdge(hello,goodNight);
		net1.addEdge(hello,seeYaa);
		
		isom = new JungISOMLayout();
		isom.setProjection(net1);
		isom.initialize_local();
		
	}
	
	public void testISOMUpdate() {
		isom.update();
		
		Map location=isom.getLocationData();
		
		Iterator ic = location.values().iterator();
		
		
		while(ic.hasNext()) {
			assertTrue(ic.next() instanceof double[]);
		}
	}
	
	public void testPositions() {
		Network<String> net1 = context.getProjection(Network.class, "Network 1");
		isom.update();
		isom.advancePositions();
		
		Object o = context.getRandomObject();
		double xy[]=isom.getCoordinates(o);
		
		
		Iterator i = (Iterator) net1.getEdges();
		
		while(i.hasNext()) {
			RepastEdge re = (RepastEdge) i.next();
			Object oo = re.getSource();
			Object ooo = re.getTarget();
			if(oo==o) {
				double xy1[] = isom.getCoordinates(oo);
				assertTrue(xy1[1]==xy[1]);
			}
			
			if(ooo==o) {
				double xy1[] = isom.getCoordinates(ooo);
				assertTrue(xy1[1]==xy[1]);
			}
		}
	}
}
