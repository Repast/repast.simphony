package repast.simphony.relogo.ide.plugin;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;


public class ReLogoResourceDeltaVisitor implements IResourceDeltaVisitor {

	@Override
	public boolean visit(IResourceDelta delta) throws CoreException {
		IResource res = delta.getResource();
		if (res.getFullPath().toOSString().contains("/relogo/")) {
			if (delta.getKind() == IResourceDelta.REMOVED) {
//				System.out.print("ReLogo resource ");
//				System.out.print(res.getFullPath());
//				System.out.println(" was removed.");
//				System.out.println("The resource was in a relogo subpackage: "
//						+ res.getFullPath().toOSString().contains("/relogo/"));
				if (delta.getMovedToPath() == null || !delta.getMovedToPath().toOSString().contains("/relogo/")) {
					// check context.xml to see if it exists
					IFile contextIFile = res.getProject().getFile(res.getProject().getName() + ".rs/context.xml");
					if (contextIFile.exists()){
						ContextDisplayNetworkSanitizer.sanitize(contextIFile, res);
					}
				}
			}
			/*else if (delta.getKind() == IResourceDelta.CHANGED){
//				System.out.print("ReLogo resource ");
//				System.out.print(res.getFullPath());
//				System.out.println(" was changed.");
				IResource relogoContainer = res.getParent();
				while(relogoContainer!= null && !relogoContainer.getFullPath().toOSString().endsWith("relogo")){
					relogoContainer = relogoContainer.getParent();
				}
				if (relogoContainer != null){
					ReLogoAndContextPackageToucher.touch(relogoContainer);
				}
			}*/
		}
		return true; // visit the children
	}
}
