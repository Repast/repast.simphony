package repast.simphony.matlab.link;

import junit.framework.TestCase;

/**
 * Test class for LinkMatlab
 * @author Mark Altaweel
 */
public class LinkMatlabTester extends TestCase{

	public void testMatlabBufferOuput() {
		String method="array=randn( 2 )";
		LinkMatlab.openMatlab();
		String output=LinkMatlab.evaluateMatlabFunction(method);
		LinkMatlab.closeMatlab();
		assertTrue(output.length()>2);
		System.out.println("Array Output: "+output);
	}
	
	public void testGraphAndClose() {
		LinkMatlab.openMatlab();
		String output1=LinkMatlab.evaluateMatlabFunction("figure(1);surf(peaks)");
		String output2=LinkMatlab.evaluateMatlabFunction("figure(2);;plot(rand(50,4))");
		
		assertTrue(output1.length()<=1);
		
		assertTrue(output2.length()<=1);
		
		LinkMatlab.closeMatlab();
	}
}
