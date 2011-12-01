/*CopyrightHere*/
package repast.simphony.integration;

import junit.framework.TestCase;


/**
 * Simple tests for {@link repast.simphony.integration.DataContent}.
 * 
 * @author Jerry Vos
 */
public class DataContentTest extends TestCase {
	DataContent content;
	Object value = "value";
	
	@Override
	protected void setUp() throws Exception {
		content = new DataContent(value);
	}
	
	/*
	 * Test method for 'repast.simphony.integration.DataContent.DataContent()'
	 */
	public void testDataContent() {
		content = new DataContent();
	}

	/*
	 * Test method for 'repast.simphony.integration.DataContent.DataContent(Object)'
	 */
	public void testDataContentObject() {
		content = new DataContent(value);
	}

	/*
	 * Test method for 'repast.simphony.integration.DataContent.getValue()'
	 */
	public void testGetValue() {
		assertEquals(value.toString(), content.getValue());
		content = new DataContent();
		assertNull(content.getValue());
	}

	/*
	 * Test method for 'repast.simphony.integration.DataContent.getData()'
	 */
	public void testGetData() {
		assertEquals(value, content.getData());
	}

	/*
	 * Test method for 'repast.simphony.integration.DataContent.setData(Object)'
	 */
	public void testSetData() {
		Object value2 = "value2";
		content.setData(value2);
		
		assertEquals(value2, content.getData());
	}

	/*
	 * Test method for 'repast.simphony.integration.DataContent.setData(Object)'
	 */
	public void testToString() {
		// test it against the docs
		assertEquals(content.getValue(), content.toString());
	}

}
