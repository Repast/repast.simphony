package repast.simphony.relogo.ide.plugin;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

import repast.simphony.relogo.ide.handlers.ReLogoBuilder;

public class ReLogoResourceDeltaVisitor implements IResourceDeltaVisitor {

	@Override
	public boolean visit(IResourceDelta delta) throws CoreException {
		if (delta != null) {
			IResource res = delta.getResource();
			if (res != null) {
				IPath path = res.getRawLocation();
				// System.out.println("statechart builder running: " + delta);
				if (path != null && path.getFileExtension() != null
						&& (path.getFileExtension().equals("groovy") || path.getFileExtension().equals("java"))) {

					ReLogoBuilder.ReLogoResourceResult rrr = ReLogoBuilder.examineResourceReLogo(delta
							.getResource());
					if (rrr.isInReLogoPackage()) {
						if (delta.getKind() == IResourceDelta.REMOVED) {
							
//							 System.out.print("ReLogo resource ");
//							 System.out.print(res.getFullPath());
//							 System.out.println(" was removed.");
//							 System.out.println("The resource was in a relogo subpackage: "
//							 + rrr.isInReLogoPackage());
							
							 boolean needsSanitizing = false;
							if (delta.getMovedToPath() == null)
								needsSanitizing = true;
							else {
								IPath movedToPath = delta.getMovedToPath();
								IWorkspace myWorkspace = ResourcesPlugin.getWorkspace();
								IWorkspaceRoot workspaceRoot = myWorkspace.getRoot();
								IResource resource = workspaceRoot.findMember(movedToPath);
								if (resource == null) {
									needsSanitizing = true;
								} else {
									ReLogoBuilder.ReLogoResourceResult rrrMovedTo = ReLogoBuilder
											.examineResourceReLogo(resource);
									if (!rrrMovedTo.isInReLogoPackage()
											|| (!rrr.getInstrumentingPackageName().equals(
													rrrMovedTo.getInstrumentingPackageName()))) {
										needsSanitizing = true;
									}
								}
							}
							if (needsSanitizing) {
//								System.out.print("Needs Sanitizing.");
								// check context.xml to see if it exists
								IFolder rsFolder = res.getProject().getFolder(res.getProject().getName() + ".rs");
								if (rsFolder.exists()) {
									IFile contextIFile = rsFolder.getFile("context.xml");
									if (contextIFile.exists()) {
										ContextDisplayNetworkSanitizer.sanitize(contextIFile, res);
									}
								}
							}
						}
					}
				}
			}
		}
		return true; // visit the children
	}
}
