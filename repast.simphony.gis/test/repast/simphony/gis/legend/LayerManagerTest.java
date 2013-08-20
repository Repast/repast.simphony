package repast.simphony.gis.legend;

import java.util.Collection;

import junit.framework.TestCase;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.Layer;

public class LayerManagerTest extends TestCase {

	LayerManager manager;

	protected void setUp() throws Exception {
		super.setUp();
		manager = new DefaultLayerManager();
	}

	public void testAddLayer() {
		Layer layer = new TestMapLayer();
		manager.addLayer(layer);
		manager.addPath("Actions", "ABC");
		manager.addPath("Actions", "DEF");
		assertTrue(manager.getLayers(new Object[] {}).contains(layer));
		manager.addLayer(layer, "Actions", "ABC");
		assertTrue(manager.getLayers("Actions", "ABC").contains(layer));
		manager.addLayer(new TestMapLayer(), "Actions", "ABC");
		assertEquals(2, manager.getLayers("Actions", "ABC").size());
	}

	public void testRemoveLayer() {
		Layer layer = new TestMapLayer();
		manager.addLayer(layer, "Actions", "ABC");
		manager.removeLayer(layer, "Actions", "ABC");
		assertEquals(0, manager.getLayers("Actions", "ABC").size());
		manager.addLayer(layer);
		manager.removeLayer(layer);
		assertEquals(0, manager.getLayers(new Object[] {}).size());
	}

	public void testRemoveChild() {
		Layer layer = new TestMapLayer();
		manager.addLayer(layer, "Actions", "ABC");
		manager.removePath("Actions", "ABC");
		assertEquals(0, manager.getChildren("Actions").size());
	}

	public void testGetChildren() {
		manager.addPath("Actions", "ABC");
		assertTrue(manager.getChildren("Actions").contains("ABC"));
	}

	public void testGetLayers() {
		Layer layer = new TestMapLayer();
		manager.addLayer(layer, "Actions", "ABC");
		assertTrue(manager.getLayers("Actions", "ABC").contains(layer));
	}

	public void testAddPath() {
		manager.addPath("Actions", "ABC");
		assertTrue(manager.getChildren("Actions").contains("ABC"));
		manager.addPath("Actions", "ABC");
		Collection<Object> children = manager.getChildren("Actions");
		boolean ABC = false;
		for (Object child : children) {
			if (child.equals("ABC") && !ABC) {
				ABC = true;
			} else if (ABC) {
				fail();
			}
		}

	}

	static class TestMapLayer extends Layer {

		@Override
		public ReferencedEnvelope getBounds() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
}