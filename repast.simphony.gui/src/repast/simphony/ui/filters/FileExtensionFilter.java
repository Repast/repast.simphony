package repast.simphony.ui.filters;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/*
 * @author Michael J. North
 *
 */
public class FileExtensionFilter extends FileFilter {

	String desc = "";
	String extensions[];

	public FileExtensionFilter(String desc, String extensions[]) {
		this.desc = desc;
		this.extensions = (String[]) extensions.clone();
	}

	public String getDescription() {
		return desc;
	}

	public boolean accept(File proposedFile) {
		String path = proposedFile.getAbsolutePath().toLowerCase();
		for (int i = 0; i < extensions.length; i++) {
			String extension = extensions[i];
			if ((path.toLowerCase().endsWith(extension.toLowerCase()) && (path
					.charAt(path.length() - extension.length() - 1)) == '.')) {
				return true;
			}
		}
		return false;
	}
}
