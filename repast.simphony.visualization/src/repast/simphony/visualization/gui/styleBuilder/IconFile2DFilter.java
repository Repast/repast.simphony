package repast.simphony.visualization.gui.styleBuilder;

import java.io.File;

/**
 * 
 * @author Eric Tatara
 *
 */
public class IconFile2DFilter extends javax.swing.filechooser.FileFilter {
	public boolean accept(File f) {
		return f.isDirectory() || f.getName().toLowerCase().endsWith(".gif")
		                       || f.getName().toLowerCase().endsWith(".jpg")
		                       || f.getName().toLowerCase().endsWith(".png")
		                       || f.getName().toLowerCase().endsWith(".bmp");
	}

	public String getDescription() {
		return "*.gif,*.jpg,*.png,*.bmp";
	}
}


