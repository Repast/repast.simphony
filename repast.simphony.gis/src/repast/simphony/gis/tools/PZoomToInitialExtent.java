package repast.simphony.gis.tools;

import org.geotools.geometry.jts.ReferencedEnvelope;
import repast.simphony.gis.display.PiccoloMapPanel;
import simphony.util.ThreadUtilities;
import simphony.util.messages.MessageCenter;

import java.awt.*;

public class PZoomToInitialExtent implements MapTool {

	MessageCenter center = MessageCenter.getMessageCenter(getClass());

	private AreaOfInterestCallback callback;

	public PZoomToInitialExtent(AreaOfInterestCallback callback) {
		this.callback = callback;
	}

	public void activate(final PiccoloMapPanel panel) {
		ThreadUtilities.runLaterInEventThread(new Runnable() {
			public void run() {
				panel.getCanvas().setAreaOfInterest(
						new ReferencedEnvelope(callback.getAreaOfInterest(),
								panel.getCanvas().getCRS()));
			}
		});
	}

  public void deactivate() {}

	public Cursor getCursor() {
		return null;
	}
}
