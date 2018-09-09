package repast.simphony.relogo.ide.handlers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IType;

import groovy.util.slurpersupport.GPathResult;

public class ContextAndDisplayUtils {

	private static final Pattern DISPLAY_PATTERN = Pattern
			.compile("repast\\.simphony\\.action\\.display_.+\\.xml");

	private static String getStringFromInputStream(InputStream is)
			throws IOException {
		StringWriter writer = new StringWriter();
		IOUtils.copy(is, writer, "UTF-8");
		return writer.toString();
	}

	public static String getStringContentsFromIFile(IFile file)
			throws CoreException, IOException {
		InputStream is = file.getContents();
		String contents = null;
		try {
			contents = getStringFromInputStream(is);
		} finally {
			is.close();
		}
		return contents;
	}

	// From Groovy

	public static class DefaultDisplayFinder implements IResourceVisitor {

		protected boolean foundDefaultDisplay = false;
		protected IResource defaultDisplayResource;

		@Override
		public boolean visit(IResource resource) throws CoreException {
			if (foundDefaultDisplay)
				return false;
			IPath path = resource.getRawLocation();
			if (path != null && path.getFileExtension() != null
					&& path.getFileExtension().equals("xml")) {
				String resourceName = resource.getName();
				if (resourceName != null) {
					if (DISPLAY_PATTERN.matcher(resourceName).find()) {
						defaultDisplayResource = resource;
						foundDefaultDisplay = true;
					}
				}
			}
			return true;
		}

	}

	public static void checkToModifyContextFile(IProject project, IType type,
			IProgressMonitor monitor) {
		StringBuilder sb = new StringBuilder();
		sb.append(project.getName());
		sb.append(".rs");
		sb.append(File.separator);
		sb.append("context.xml");
		IFile contextFile = project.getFile(sb.toString());
		String className = type.getElementName();

		try {
			String contents = ContextAndDisplayUtils
					.getStringContentsFromIFile(contextFile);
			if (contents != null) {
				String fileContents = ContextAndDisplayUtilsGroovy
						.checkToModifyContextFile(contents, className);
				if (fileContents != null) {
					InputStream source = new ByteArrayInputStream(
							fileContents.getBytes());
					contextFile.setContents(source, true, true, monitor);
					contextFile.refreshLocal(0, monitor);
					source.close();

					// Add to the display's projection info
					ContextAndDisplayUtils.modifyDisplayFile(project, type,
							monitor);
				}

			}
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// TODO: this
	public static void modifyDisplayFile(IProject project, IType iType,
			IProgressMonitor monitor) {
		// TODO: bring Java contents here from
		// repast.simphony.relogo.ide.handlers.ContextAndDisplayUtilsGroovy.modifyDisplayFile(IProject,
		// IType, IProgressMonitor)
		StringBuilder sb = new StringBuilder();
		sb.append(project.getName());
		sb.append(".rs");
		IFolder rsFolder = project.getFolder(sb.toString());
		DefDisplayReturner result = findDefaultReLogoDisplayFile(rsFolder);
		if (result != null && result.displayFile.exists()) {
			String className = iType.getElementName();
			GPathResult displayRoot = result.root;
			String fileContents = ContextAndDisplayUtilsGroovy
					.getDisplayFileContents(displayRoot, className);
			if (fileContents != null) {
				InputStream source = new ByteArrayInputStream(
						fileContents.getBytes());
				if (result.displayFile instanceof IFile) {
					IFile dFile = (IFile) result.displayFile;
					try {
						dFile.setContents(source, true, true, monitor);
						dFile.refreshLocal(0, monitor);
					} catch (CoreException e) {
						e.printStackTrace();
					}

				}
				try {
					source.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static DefDisplayReturner findDefaultReLogoDisplayFile(
			IResource rsFolder) {
		if (rsFolder == null) {
			return null;
		}
		DefaultDisplayFinder ddf = new DefaultDisplayFinder();
		try {
			rsFolder.accept(ddf);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		if (ddf.foundDefaultDisplay) {
			IResource resource = ddf.defaultDisplayResource;
			if (resource instanceof IFile) {
				try {
					String contents = ContextAndDisplayUtils
							.getStringContentsFromIFile((IFile) resource);
					if (contents != null) {
						// Call Groovy side
						GPathResult displayRoot = ContextAndDisplayUtilsGroovy
								.checkIfDefaultReLogoDisplay(contents);
						if (displayRoot != null) {
							return new DefDisplayReturner(resource, displayRoot);
						}
					}
				} catch (Exception ignore) {
				}
			}
		}
		return null;
	}

	public static class DefDisplayReturner {

		public DefDisplayReturner(IResource displayFile, GPathResult root) {
			this.displayFile = displayFile;
			this.root = root;
		}

		IResource displayFile;
		GPathResult root;
	}

}
