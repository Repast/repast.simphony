/*
 * Created on Aug 3, 2005
 */
package repast.simphony.engine.environment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.swing.JPanel;

import junit.framework.TestCase;
import junitx.util.PrivateAccessor;
import repast.simphony.TestUtils;
import repast.simphony.engine.environment.DefaultGUIRegistry;
import repast.simphony.engine.environment.GUIRegistryType;
import repast.simphony.util.collections.Pair;

public class DefaultGUIRegistryTest extends TestCase {
	DefaultGUIRegistry guiRegistry;
	
	@Override
	protected void setUp() throws Exception {
		guiRegistry = new DefaultGUIRegistry();
	}
	
	/*
	 * Test method for 'repast.simphony.engine.engine.DefaultGUIRegistry.DefaultGUIRegistry()'
	 */
	public void testDefaultGUIRegistry() {
		guiRegistry = new DefaultGUIRegistry();
	}

	/*
	 * Test method for 'repast.simphony.engine.engine.DefaultGUIRegistry.addComponent(JComponent, String, String)'
	 */
	public void testAddComponent() throws NoSuchFieldException {
		JPanel panel = new JPanel();
		
		guiRegistry.addComponent(panel, GUIRegistryType.DISPLAY, "name1");
		
		assertTrue(((Collection) ((Map) PrivateAccessor.getField(guiRegistry,
				"typeComponentTable")).get(GUIRegistryType.DISPLAY)).size() == 1);
		assertSame(panel, ((Collection) ((Map) PrivateAccessor.getField(
				guiRegistry, "typeComponentTable")).get(GUIRegistryType.DISPLAY)).iterator()
				.next());
		
		assertSame(GUIRegistryType.DISPLAY, ((Map) PrivateAccessor.getField(guiRegistry,
				"componentTypeTable")).get(panel));
		
		assertSame("name1", ((Map) PrivateAccessor.getField(guiRegistry,
		"componentNameTable")).get(panel));
	}

	/*
	 * Test method for 'repast.simphony.engine.engine.DefaultGUIRegistry.removeComponent(JComponent)'
	 */
	public void testRemoveComponent() throws NoSuchFieldException {
		JPanel panel = new JPanel();
		
		// doesn't contain the panel
		assertFalse(guiRegistry.removeComponent(panel));
		
		guiRegistry.addComponent(panel, GUIRegistryType.DISPLAY, "name1");
		
		guiRegistry.removeComponent(panel);
		
		assertTrue(((Collection) ((Map) PrivateAccessor.getField(guiRegistry,
				"typeComponentTable")).get(GUIRegistryType.DISPLAY)).isEmpty());
		assertNull(((Map) PrivateAccessor.getField(guiRegistry,
				"componentTypeTable")).get(panel));
		assertNull(((Map) PrivateAccessor.getField(guiRegistry,
		"componentNameTable")).get("name1"));
	}



	/*
	 * Test method for 'repast.simphony.engine.engine.DefaultGUIRegistry.getName(JComponent)'
	 */
	public void testGetName() throws NoSuchFieldException {
		JPanel panel = new JPanel();
		
		guiRegistry.addComponent(panel, GUIRegistryType.DISPLAY, "name1");

		assertSame("name1", guiRegistry.getName(panel));
	}

	/*
	 * Test method for 'repast.simphony.engine.engine.DefaultGUIRegistry.getTypesAndComponents()'
	 */
	@SuppressWarnings("unchecked")
	public void testGetTypesAndComponents() {

		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		
		guiRegistry.addComponent(panel1, GUIRegistryType.DISPLAY, "name1");
		guiRegistry.addComponent(panel2, GUIRegistryType.CHART, "name2");
		
		ArrayList pairList = new ArrayList();
		
		ArrayList type1List = new ArrayList();
		type1List.add(panel1);
		
		pairList.add(new Pair(GUIRegistryType.DISPLAY, type1List));
		
		ArrayList type2List = new ArrayList();
		type2List.add(panel2);
		
		pairList.add(new Pair(GUIRegistryType.CHART, type2List));
		
		assertTrue(TestUtils.collectionsContentsEqual(pairList, guiRegistry
				.getTypesAndComponents()));
	}

}
