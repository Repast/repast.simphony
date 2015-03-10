package repast.simphony.visualization.gis3D;

import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.globes.Earth;
import gov.nasa.worldwind.layers.LayerList;

import java.beans.PropertyChangeListener;

/**
 * This implementation of WorldWindowGLCanvas fixes bugs relating to disposal
 *   and WW shutdown to make sure all objects within are disposed properly.
 * 
 * @author Eric Tatara
 *
 */
public class RepastWorldWindowGLCanvas extends WorldWindowGLCanvas {
	
	@Override
	public void shutdown() {
		getModel().getLayers().removeAll();
		getModel().setLayers(new LayerList());
		getModel().setGlobe(new Earth());
		super.shutdown();
		if (getParent() != null) getParent().remove(this);
	}
	
	@Override
  public synchronized void removePropertyChangeListener(String propertyName, PropertyChangeListener listener){
      super.removePropertyChangeListener(propertyName, listener);
      this.wwd.removePropertyChangeListener(propertyName, listener);
  }
}
