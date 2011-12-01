/*CopyrightHere*/
package repast.simphony.integration;

import junit.framework.TestCase;
import repast.simphony.integration.ObjectHolder.StorageType;

/**
 * Simple test for this container object.
 * 
 * @author Jerry Vos
 */
public class ObjectHolderTest extends TestCase {

	private String parent;
	private String name;
	private String value;
	private StorageType storageType;
	private ObjectHolder holder;

	@Override
	protected void setUp() throws Exception {
		parent = "parentValue";
		name = "nameValue";
		value = "valueValue";
		storageType = StorageType.CHILD;
		
		holder = new ObjectHolder(parent, name, value, storageType);
	}
	
	/*
	 * Test method for 'repast.simphony.integration.ObjectHolder.ObjectHolder(Object, String, Object, StorageType)'
	 */
	public void testObjectHolder() {
		// just make sure this works
		@SuppressWarnings("unused") ObjectHolder holder = new ObjectHolder(parent, name, value, storageType);
	}

	/*
	 * Test method for 'repast.simphony.integration.ObjectHolder.getName()'
	 */
	public void testGetName() {
		assertEquals(name, holder.getName());
	}

	/*
	 * Test method for 'repast.simphony.integration.ObjectHolder.getParent()'
	 */
	public void testGetParent() {
		assertEquals(parent, holder.getParent());
	}

	/*
	 * Test method for 'repast.simphony.integration.ObjectHolder.getValue()'
	 */
	public void testGetValue() {
		assertEquals(value, holder.getValue());
	}

	/*
	 * Test method for 'repast.simphony.integration.ObjectHolder.getStorageType()'
	 */
	public void testGetStorageType() {
		assertEquals(storageType, holder.getStorageType());
	}

}
