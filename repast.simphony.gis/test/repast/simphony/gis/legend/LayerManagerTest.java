package repast.simphony.gis.legend;

import junit.framework.TestCase;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.map.MapLayer;
import org.geotools.map.event.MapLayerListener;
import org.geotools.styling.Style;

import java.util.Collection;

public class LayerManagerTest extends TestCase {

	LayerManager manager;

	protected void setUp() throws Exception {
		super.setUp();
		manager = new DefaultLayerManager();
	}

	public void testAddLayer() {
		MapLayer layer = new TestMapLayer();
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
		MapLayer layer = new TestMapLayer();
		manager.addLayer(layer, "Actions", "CBC");
		manager.removeLayer(layer, "Actions", "CBC");
		assertEquals(0, manager.getLayers("Actions", "CBC").size());
		manager.addLayer(layer);
		manager.removeLayer(layer);
		assertEquals(0, manager.getLayers(new Object[] {}).size());
	}

	public void testRemoveChild() {
		MapLayer layer = new TestMapLayer();
		manager.addLayer(layer, "Actions", "CBC");
		manager.removePath("Actions", "CBC");
		assertEquals(0, manager.getChildren("Actions").size());
	}

	public void testGetChildren() {
		manager.addPath("Actions", "CBC");
		assertTrue(manager.getChildren("Actions").contains("CBC"));
	}

	public void testGetLayers() {
		MapLayer layer = new TestMapLayer();
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

	static class TestMapLayer extends MapLayer {

		public TestMapLayer(){
			super(null);
		}
		
		public void addMapLayerListener(MapLayerListener arg0) {
			// TODO Auto-generated method stub

		}

		public FeatureSource getFeatureSource() {
			// TODO Auto-generated method stub
			return null;
		}

		public Query getQuery() {
			// TODO Auto-generated method stub
			return null;
		}

		public Style getStyle() {
			// TODO Auto-generated method stub
			return null;
		}

		public String getTitle() {
			// TODO Auto-generated method stub
			return null;
		}

		public boolean isVisible() {
			// TODO Auto-generated method stub
			return false;
		}

		public void removeMapLayerListener(MapLayerListener arg0) {
			// TODO Auto-generated method stub

		}

		public void setQuery(Query arg0) {
			// TODO Auto-generated method stub

		}

		public void setStyle(Style arg0) {
			// TODO Auto-generated method stub

		}

		public void setTitle(String arg0) {
			// TODO Auto-generated method stub

		}

		public void setVisible(boolean arg0) {
			// TODO Auto-generated method stub

		}

	}
}
