package repast.simphony.engine.environment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.swing.JPanel;

import junit.framework.TestCase;
import junitx.util.PrivateAccessor;
import repast.simphony.TestUtils;
import repast.simphony.util.collections.Pair;
import repast.simphony.visualization.IDisplay;

public class DefaultGUIRegistryTest extends TestCase {
	DefaultGUIRegistry guiRegistry;
	
	@Override
	protected void setUp() throws Exception {
		guiRegistry = new DefaultGUIRegistry();
	}
	
	public void testDefaultGUIRegistry() {
		guiRegistry = new DefaultGUIRegistry();
	}

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

	public void testGetName() throws NoSuchFieldException {
		JPanel panel = new JPanel();
		
		guiRegistry.addComponent(panel, GUIRegistryType.DISPLAY, "name1");

		assertSame("name1", guiRegistry.getName(panel));
	}

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
	
	public void testAddDisplay(){
		IDisplay display = new TestDisplay();
		JPanel panel = display.getPanel();
		
		guiRegistry.addDisplay("Display ABC", GUIRegistryType.DISPLAY, display);
		
		assertSame(display, guiRegistry.getDisplayForComponent(panel));
		
		assertNotSame(display, guiRegistry.getDisplayForComponent(new JPanel()));
		
		assertSame(display, guiRegistry.getDisplayForName("Display ABC"));
		
		assertEquals("Display ABC", guiRegistry.getName(panel));
		
		assertNull(guiRegistry.getDisplayForName("bazinga"));
		
		assertSame(display, guiRegistry.getDisplays().iterator().next());
	}

}
