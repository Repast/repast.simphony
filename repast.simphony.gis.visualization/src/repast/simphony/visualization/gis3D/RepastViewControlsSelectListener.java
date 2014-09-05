package repast.simphony.visualization.gis3D;

import gov.nasa.worldwind.Disposable;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.layers.ViewControlsLayer;
import gov.nasa.worldwind.layers.ViewControlsSelectListener;

/**
 * This implementation of RepastViewControlsSelectListener fixes bugs relating 
 *   to disposal and WW shutdown to make the Timer is released.
 * 
 * @author Eric Tatara
 *
 */
public class RepastViewControlsSelectListener extends ViewControlsSelectListener implements Disposable {

	public RepastViewControlsSelectListener(WorldWindow wwd,
			ViewControlsLayer layer) {
		super(wwd, layer);
	}

	@Override
	public void dispose() {
		repeatTimer.stop();
	}
	


}
