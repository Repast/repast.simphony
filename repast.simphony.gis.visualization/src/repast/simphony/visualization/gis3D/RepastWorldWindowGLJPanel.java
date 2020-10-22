package repast.simphony.visualization.gis3D;

import java.beans.PropertyChangeListener;

import gov.nasa.worldwind.awt.WorldWindowGLJPanel;
import gov.nasa.worldwind.globes.Earth;
import gov.nasa.worldwind.layers.LayerList;

/**
 * This implementation of WorldWindowGLJPanel fixes bugs relating to disposal
 *   and WW shutdown to make sure all objects within are disposed properly.
 * 
 * @author Eric Tatara
 *
 */
public class RepastWorldWindowGLJPanel extends WorldWindowGLJPanel {
    
    public RepastWorldWindowGLJPanel() {
//	setSurfaceScale(new int[] { ScalableSurface.IDENTITY_PIXELSCALE,
//                ScalableSurface.IDENTITY_PIXELSCALE });
    }
	
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
