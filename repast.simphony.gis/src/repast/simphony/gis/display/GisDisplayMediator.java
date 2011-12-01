package repast.simphony.gis.display;

import edu.umd.cs.piccolo.PLayer;
import org.geotools.map.MapLayer;
import org.geotools.map.event.*;
import simphony.util.messages.MessageCenter;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GisDisplayMediator implements MapBoundsListener, MapLayerListener,
				ComponentListener, MapLayerListListener {

	private static MessageCenter msg = MessageCenter.getMessageCenter(GisDisplayMediator.class);

	class UpdatingLayers {
		private Map<MapLayer, List<Runnable>> updatingLayers = new HashMap<MapLayer, List<Runnable>>();

		public void put(MapLayer layer, Runnable runnable) {
			List<Runnable> list = updatingLayers.get(layer);
			if (list == null) {
				list = new ArrayList<Runnable>();
				updatingLayers.put(layer, list);
			}
			if (runnable != null) list.add(runnable);
		}

		public boolean contains(MapLayer layer) {
			return updatingLayers.containsKey(layer);
		}

		public List<Runnable> remove(MapLayer layer) {
			return updatingLayers.remove(layer);
		}
	}

	class PoolManager {

		// for now we can only have 1 thread
		// because > 1 screws up the adding of layers at specified indices
		private int numThreads = 1;
		private ExecutorService service = Executors.newFixedThreadPool(numThreads);
		private UpdatingLayers updatingLayers = new UpdatingLayers();
		//private Map<MapLayer, Runnable> updatingLayers = new HashMap<MapLayer, Runnable>();

		public synchronized void resize(Dimension size, PGisLayer layer) {
			updatingLayers.put(layer.getLayer(), null);
			service.execute(new ResizeLayer(size, layer, this));
		}

		public synchronized void update(PGisLayer layer) {
			updatingLayers.put(layer.getLayer(), null);
			service.execute(new UpdateLayer(layer, this));
		}

		public synchronized void add(MapLayer layer, Runnable adder) {
			RunnableWrapper wrapper = new RunnableWrapper(layer, this, adder);
			if (updatingLayers.contains(layer)) {
				updatingLayers.put(layer, wrapper);
			} else {
				service.execute(wrapper);
			}
		}

		public synchronized void remove(PLayer layer, MapLayer mapLayer, Runnable remover) {
			RunnableWrapper wrapper = new RunnableWrapper(mapLayer, this, remover);
			if (updatingLayers.contains(mapLayer)) {
				updatingLayers.put(mapLayer, wrapper);
			} else {
				service.execute(wrapper);
			}
		}

		public synchronized void updateFinished(PGisLayer layer) {
			updateFinished(layer.getLayer());
		}

		public synchronized void updateFinished(MapLayer layer) {
			List<Runnable> runnables = updatingLayers.remove(layer);
			// may be null if just did an add the update size etc.
			// had already completed
			if (runnables != null) {
				for (Runnable runnable : runnables) {
					service.execute(runnable);
				}
			}
		}
	}

	private List<PGisLayer> layers = new ArrayList<PGisLayer>();
	private PoolManager manager = new PoolManager();

	private Map<MapLayer, PGisLayer> layerMap = new HashMap<MapLayer, PGisLayer>();

	public void layerAdded(MapLayerListEvent event) {
		// TODO Auto-generated method stub

	}

	public void layerChanged(MapLayerListEvent event) {
		manager.update(layerMap.get((MapLayer) event.getSource()));
	}

	public void layerMoved(MapLayerListEvent event) {
		// TODO Auto-generated method stub

	}

	public void layerRemoved(MapLayerListEvent event) {
	}

	public void removeLayer(PLayer gisLayer, MapLayer mapLayer, Runnable runnable) {
		layers.remove(gisLayer);
		layerMap.remove(mapLayer);
		mapLayer.removeMapLayerListener(this);
		manager.remove(gisLayer, mapLayer, runnable);
	}

	// we don't add this to the list of layers etc.
	// because all we are interested in is making sure it syncs with the
	// adding of the non-dynamic layers, and not with resizing etc.
	public void addLayer(MapLayer layer, PDynamicGisLayer gisLayer, Runnable runnable) {
		msg.debug("Adding dynamic to manager");
		manager.add(layer, runnable);
	}

	public void addLayer(PGisLayer display, Dimension size, Runnable runnable) {
		layers.add(display);
		layerMap.put(display.getLayer(), display);
		// resizing sets the layers size and does
		// the update that UpdateLayer would do as well.
		//manager.resize(size, display);
		msg.debug("Adding non-dynamic to manage");
		manager.add(display.getLayer(), new Adder(display, size, runnable));
		display.getLayer().addMapLayerListener(this);
	}

	public void componentResized(ComponentEvent e) {
		for (PGisLayer layer : layers) {
			manager.resize(e.getComponent().getSize(), layer);
		}
	}

	public void mapBoundsChanged(MapBoundsEvent event) {
		for (PGisLayer layer : layers) {
			layer.clear();
			// layer.fireDrawingStarted();
		}
		for (PGisLayer layer : layers) {
			manager.update(layer);
		}
	}

	public void layerChanged(MapLayerEvent event) {
		if (event.getSource() instanceof MapLayer) {
			MapLayer layer = (MapLayer) event.getSource();
			switch (event.getReason()) {
				case MapLayerEvent.DATA_CHANGED:
					manager.update(layerMap.get(layer));
					break;
				case MapLayerEvent.FILTER_CHANGED:
					manager.update(layerMap.get(layer));
					break;
				case MapLayerEvent.STYLE_CHANGED:
					manager.update(layerMap.get(layer));
					break;
				default:

			}
		}
	}

	public void layerHidden(MapLayerEvent event) {
		// TODO Auto-generated method stub

	}

	public void layerShown(MapLayerEvent event) {
		// TODO Auto-generated method stub

	}

	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	private class UpdateLayer implements Runnable {

		protected PGisLayer layer;
		protected PoolManager manager;

		public UpdateLayer(PGisLayer layer, PoolManager manager) {
			this.layer = layer;
			this.manager = manager;
		}

		public void run() {
			layer.update();
			manager.updateFinished(layer);
		}
	}

	private class Adder implements Runnable {

		private Runnable runnable;
		private PGisLayer layer;
		private PoolManager manager;
		private Dimension size;

		public Adder(PGisLayer layer, Dimension size, Runnable runnable) {
			this.runnable = runnable;
			this.layer = layer;
			this.size = size;
		}

		public void run() {
			layer.setSize(size.width, size.height);
			runnable.run();
		}

	}

	private class RunnableWrapper implements Runnable {

		private Runnable runnable;
		private MapLayer layer;
		private PoolManager manager;

		public RunnableWrapper(MapLayer layer, PoolManager manager, Runnable runnable) {
			this.runnable = runnable;
			this.manager = manager;
			this.layer = layer;
		}

		public void run() {
			runnable.run();
			manager.updateFinished(layer);
		}
	}

	private class ResizeLayer extends UpdateLayer {

		Dimension size;

		public ResizeLayer(Dimension size, PGisLayer layer, PoolManager manager) {
			super(layer, manager);
			this.size = size;
		}

		public void run() {
			layer.setSize(size.width, size.height);
			manager.updateFinished(layer);
		}
	}
}
