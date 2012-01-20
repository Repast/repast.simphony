package repast.simphony.relogo.util;
import groovy.util.GroovyTestCase

import java.util.regex.Pattern

import org.codehaus.groovy.ast.builder.*

import sun.net.www.protocol.file.FileURLConnection;

class ReLogoReferenceTest extends GroovyTestCase {
	
	ReLogoReferenceCreator rrc
	
	public void setUp() {
		rrc = new ReLogoReferenceCreator()
	}
	
//	public void testStringRegex(){
//		String input = "allQ(Collection)"
//		String expected = "allQ(java.util.Collection)"
//		Pattern word = Pattern.compile(/\b\w+/)
//		Closure closure = {
//			
//			"XXX"
//		}
//		String result = input.replaceAll(word, closure)
//		println result
//	}
	
	public void testTransformMethodString(){
		List inputs = []
		List expecteds = []
		inputs.add("allLinks()")
		expecteds.add("allLinks()")
		
		inputs.add("allQ(Collection, Closure)")
		expecteds.add("allQ(java.util.Collection,%20groovy.lang.Closure)")
		
		inputs.add("ask(AgentSet<? extends ReLogoAgent>, Closure)")
		expecteds.add("ask(repast.simphony.relogo.AgentSet,%20groovy.lang.Closure)")
		
		for (int i = 0; i < inputs.size(); i++){
			assertEquals("input ${inputs[i]} failed",expecteds[i],rrc.transformMethodString(inputs[i]))
		}
	}
	
	public void testURL(){
		String methodName = "ask(repast.simphony.relogo.AgentSet,%20groovy.lang.Closure)"
		assertEquals(true,rrc.isValidURL("Observer", methodName))
		methodName = "hatch(java.lang.Number,%20groovy.lang.Closure,%20java.lang.String)"
		assertEquals(true,rrc.isValidURL("Turtle", methodName))
	}
	
	public void tearDown(){
		
	}
	
	
	

}
