package repast.simphony.gis.tools;

import repast.simphony.gis.display.PiccoloMapPanel;

import java.awt.*;

public class PZoomToPreviousExtent implements MapTool {

	public void activate(PiccoloMapPanel panel) {
		panel.getCanvas().zoomToPreviousExtent();
	}

  public void deactivate() {

  }

	public Cursor getCursor() {
		return null;
	}
}
