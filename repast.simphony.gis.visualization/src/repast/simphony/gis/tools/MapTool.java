package repast.simphony.gis.tools;

import java.awt.Cursor;

import repast.simphony.gis.display.PiccoloMapPanel;

public interface MapTool {

	public void activate(PiccoloMapPanel panel);

  void deactivate();

  public Cursor getCursor();

}
