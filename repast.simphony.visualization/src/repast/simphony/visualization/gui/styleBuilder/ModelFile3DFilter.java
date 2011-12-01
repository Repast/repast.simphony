package repast.simphony.visualization.gui.styleBuilder;

import java.io.File;

/**
 * 
 * @author Eric Tatara
 *
 */
public class ModelFile3DFilter extends javax.swing.filechooser.FileFilter {
	public boolean accept(File f) {
		return f.isDirectory() || f.getName().toLowerCase().endsWith(".ms3d")
		                       || f.getName().toLowerCase().endsWith(".lws")
		                       || f.getName().toLowerCase().endsWith(".lwo")
		                       || f.getName().toLowerCase().endsWith(".obj");
	}

	public String getDescription() {
		return "*.ms3d,*.lws,*.lwo,*.obj";
	}
}


