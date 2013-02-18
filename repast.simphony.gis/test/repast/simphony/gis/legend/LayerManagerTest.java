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
		manager.addPath("Actions", "CBC");
		manager.addPath("Actions", "HPAC");
		assertTrue(manager.getLayers(new Object[] {}).contains(layer));
		manager.addLayer(layer, "Actions", "CBC");
		assertTrue(manager.getLayers("Actions", "CBC").contains(layer));
		manager.addLayer(new TestMapLayer(), "Actions", "CBC");
		assertEquals(2, manager.getLayers("Actions", "CBC").size());
	}

	public void testRemoveLayer() {
		Layer layer = new TestMapLayer();
		manager.addLayer(layer, "Actions", "CBC");
		manager.removeLayer(layer, "Actions", "CBC");
		assertEquals(0, manager.getLayers("Actions", "CBC").size());
		manager.addLayer(layer);
		manager.removeLayer(layer);
		assertEquals(0, manager.getLayers(new Object[] {}).size());
	}

	public void testRemoveChild() {
		Layer layer = new TestMapLayer();
		manager.addLayer(layer, "Actions", "CBC");
		manager.removePath("Actions", "CBC");
		assertEquals(0, manager.getChildren("Actions").size());
	}

	public void testGetChildren() {
		manager.addPath("Actions", "CBC");
		assertTrue(manager.getChildren("Actions").contains("CBC"));
	}

	public void testGetLayers() {
		Layer layer = new TestMapLayer();
		manager.addLayer(layer, "Actions", "CBC");
		assertTrue(manager.getLayers("Actions", "CBC").contains(layer));
	}

	public void testAddPath() {
		manager.addPath("Actions", "CBC");
		assertTrue(manager.getChildren("Actions").contains("CBC"));
		manager.addPath("Actions", "CBC");
		Collection<Object> children = manager.getChildren("Actions");
		boolean cbc = false;
		for (Object child : children) {
			if (child.equals("CBC") && !cbc) {
				cbc = true;
			} else if (cbc) {
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