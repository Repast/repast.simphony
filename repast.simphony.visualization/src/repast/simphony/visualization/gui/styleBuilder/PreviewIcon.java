package repast.simphony.visualization.gui.styleBuilder;

import java.awt.Color;
import java.awt.Font;
import java.io.File;

public interface PreviewIcon {

	public void setEditorFontColor(Color color);
	public void setEditorFont(Font font);
	public void setFillColor(Color fillColor);
	public void setMark(String mark);
	public void setMarkSize(float markSize);
	public void setIconFile(File file);
	
	public void updatePreview();
	
	
}
