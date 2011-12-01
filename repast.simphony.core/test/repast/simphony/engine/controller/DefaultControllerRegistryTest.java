/*CopyrightHere*/
package repast.simphony.engine.controller;

import junit.framework.TestCase;
import junitx.util.PrivateAccessor;
import repast.simphony.engine.controller.DefaultControllerRegistry;

public class DefaultControllerRegistryTest extends TestCase {
	DefaultControllerRegistry registry;
	
	@Override
	protected void setUp() throws Exception {
		registry = new DefaultControllerRegistry();
	}
	
	
	/*
	 * Test method for 'repast.simphony.engine.controller.SimpleControllerRegistry.SimpleControllerRegistry()'
	 */
	public void testSimpleControllerRegistry() {

	}

	/*
	 * Test method for 'repast.simphony.engine.controller.SimpleControllerRegistry.addAction(Object, ControllerAction, ControllerAction)'
	 */
	public void testAddAction() {

	}

	/*
	 * Test method for 'repast.simphony.engine.controller.SimpleControllerRegistry.getActionTree(Object)'
	 */
	public void testGetActionTree() {

	}

	/*
	 * Test method for 'repast.simphony.engine.controller.SimpleControllerRegistry.createAndStoreActionTree(Object)'
	 */
	public void testCreateAndStoreActionTree() {

	}

	/*
	 * Test method for 'repast.simphony.engine.controller.SimpleControllerRegistry.removeAction(Object, ControllerAction)'
	 */
	public void testRemoveAction() {

	}

	/*
	 * Test method for 'repast.simphony.engine.controller.SimpleControllerRegistry.replaceAction(Object, ControllerAction, ControllerAction)'
	 */
	public void testReplaceAction() {

	}

	/*
	 * Test method for 'repast.simphony.engine.controller.SimpleControllerRegistry.getContextIdTree()'
	 */
	public void testGetContextIdTree() {

	}

	/*
	 * Test method for 'repast.simphony.engine.controller.SimpleControllerRegistry.addContextId(Object, Object)'
	 */
	public void testAddContextId() {
		String newContextId = "newId";
		
		registry.addContextId(registry.getMasterContextId(), newContextId);
	}

	/*
	 * Test method for 'repast.simphony.engine.controller.SimpleControllerRegistry.addContextId(Object, Object)'
	 */
	public void testAddContextIdWDiffMaster() {
		String newMaster = "newMaster";
		
		registry.setMasterContextId(newMaster);
		
		String newContextId = "newId";
		
		registry.addContextId(newMaster, newContextId);
	}
	
	/*
	 * Test method for 'repast.simphony.engine.controller.SimpleControllerRegistry.removeContextId(Object)'
	 */
	public void testRemoveContextId() {

	}

	/*
	 * Test method for 'repast.simphony.engine.controller.SimpleControllerRegistry.setMasterContextId(Object)'
	 */
	public void testSetMasterContextId() throws NoSuchFieldException {
		String newMaster = "newMaster";
		
		registry.setMasterContextId(newMaster);
		
		assertSame(newMaster, PrivateAccessor.getField(registry, "masterContextId"));
	}

	/*
	 * Test method for 'repast.simphony.engine.controller.SimpleControllerRegistry.getMasterContextId()'
	 */
	public void testGetMasterContextId() {

	}

	/*
	 * Test method for 'repast.simphony.engine.controller.SimpleControllerRegistry.replaceContextId(Object, Object)'
	 */
	public void testReplaceContextId() {
		String id1 = "id1";
		String id2 = "id2";
		String id3 = "id3";
		
		registry.addContextId(registry.getMasterContextId(), id1);
		registry.addContextId(id1, id2);
		
		registry.replaceContextId(id1, id3);
		
		assertSame(id3, registry.getContextIdTree().getChildren(registry.getMasterContextId()).iterator().next());
		assertSame(id2, registry.getContextIdTree().getChildren(id3).iterator().next());
	}

}
