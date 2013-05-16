/**
 * 
 */
package repast.simphony.relogo.ide.handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;

import repast.simphony.eclipse.util.ToDeleteFilter;

/**
 * Filter for deleting all @GeneratedByReLogoBuilder classes.
 * 
 * @author jozik
 */
public class GeneratedByReLogoBuilderFilter implements ToDeleteFilter {

	private static final String GENERATED_BY_RELOGO_BUILDER = "@GeneratedByReLogoBuilder";

	@SuppressWarnings("restriction")
	private boolean isGeneratedByReLogoBuilder(IFile file) {

		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(file.getContents()));
			String line = null;
			while ((line = in.readLine()) != null) {
				if (line.contains(GENERATED_BY_RELOGO_BUILDER))
					return true;
			}
		} catch (IOException | CoreException ex) {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
				}
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
				}
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see repast.simphony.eclipse.ToDeleteFilter#delete(org.eclipse
	 * .core.resources.IFile)
	 */
	@Override
	public boolean delete(IFile file) {

		String ext = file.getFileExtension();
		if (ext != null) {
			if (ext.equals("java")) {
				return isGeneratedByReLogoBuilder(file);
			}
		}
		return false;
	}

}