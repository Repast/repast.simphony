package repast.simphony.scenario.data;

import junit.framework.TestCase;

/**
 * @author Jonathan Ozik
 * @version $Revision$ $Date$
 */
public class AgentDataTest extends TestCase {


	public void testShortName() {
		AgentData ad = new AgentData("one.two.Three");
		assertEquals("Three", ad.getShortName());
		
		ad = new AgentData("Four");
		assertEquals("Four", ad.getShortName());
		
	}

}
