package repast.simphony.render;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:35 $
 */
public class RendererManager implements RenderListener {

	private List<Renderer> renderers = new ArrayList<Renderer>();
	private Object monitor = new Object();
	private int count;

	private void notifyMonitor() {
		synchronized (monitor) {
			monitor.notify();
		}
	}

	public void addRenderer(Renderer renderer) {
		synchronized (renderers) {
			renderers.add(renderer);
			renderer.addRenderListener(this);
		}
	}

	public void render() {
		synchronized (monitor) {
			List<Renderer> workingList;
			synchronized (renderers) {
				count = renderers.size();
				workingList = (List<Renderer>) ((ArrayList) renderers).clone();
			}

			for (Renderer renderer : workingList) {
				renderer.render();
			}

			while (count > 0) {
				try {
					monitor.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
					// I think we need to break otherwise
					// we will be stuck in the loop
					break;
				}
			}
		}
	}

	public void setPause(boolean pause) {
		synchronized (renderers) {
			for (Renderer renderer : renderers) {
				renderer.setPause(pause);
			}
		}

	}

	// this is synchronized so that two threads don't touch at
	// the same time screwing up the count decrement and check.
	public synchronized void renderFinished(RenderEvent evt) {
		// I don't think this needs to be synchronized as
		// operations on ints are atomic?

		count--;
		if (count == 0) {
			notifyMonitor();
		}
	}

	public void clear() {
		renderers.clear();
	}
}
