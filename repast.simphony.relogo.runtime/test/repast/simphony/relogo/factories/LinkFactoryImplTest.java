package repast.simphony.relogo.factories;

import junit.framework.TestCase;
import repast.simphony.relogo.BaseLink;
import repast.simphony.relogo.BaseObserver;

public class LinkFactoryImplTest extends TestCase {

	public void testCreateLinkTTBooleanDouble() {
		LinkFactory lf = new LinkFactory(BaseLink.class);
		assertTrue(lf.getClass().equals(BaseLink.class));
		lf.init(new BaseObserver());
		Integer first = new Integer(1);
		BaseLink link = (BaseLink) lf.createLink(BaseLink.class,first, new Integer(2), true);
		assertTrue(link != null);
		assertTrue(link.getEnd1() == first);
	}
	
	/*public void testCreateLinkFail(){
		Integer temp = new Integer(1);
		LinkFactory lf;
		try {
			lf = new LinkFactory(Math.class);
			fail("Math accepted as valid even though it doesn't extend BaseLink.");
		} catch (Exception e) {
			assertTrue(true); // RuntimeException thrown
		}
	}*/

}
