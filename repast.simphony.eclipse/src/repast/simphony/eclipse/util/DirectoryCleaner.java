/**
 * 
 */
package repast.simphony.eclipse.util;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

/**
 * Cleans out a tree of directories.
 * 
 * @author Nick Collier
 * @author jozik
 */
public class DirectoryCleaner {

	private static Set<String> excludes = new HashSet<String>();
	static {
		excludes.add("CVS");
		excludes.add(".cvsignore");
		excludes.add(".svn");
		excludes.add(".gitignore");
	}

	private ToDeleteFilter filter;

	public DirectoryCleaner(ToDeleteFilter filter) {
		this.filter = filter;
	}

	IResource rootFile;

	/**
	 * Cleans the directories starting at root.
	 * 
	 * @param root
	 */
	public void run(String root) {
		File file = new File(root);
		if (file.exists()){
		if (file.isDirectory()){
			rootFile = getIContainer(root);
		}
		else {
			rootFile = getIFile(root);
		}
		try{
			process(rootFile);
		} catch(CoreException ce){
			ce.printStackTrace();
		}
		}
	}
	
	
	private IFile getIFile(String string){
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IPath location = Path.fromOSString(string); 
		return workspace.getRoot().getFileForLocation(location);
	}
	
	private IContainer getIContainer(String string){
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IPath location = Path.fromOSString(string); 
		return workspace.getRoot().getContainerForLocation(location);
	}

//	private IResource getIResource(File file) {
//		IWorkspace workspace = ResourcesPlugin.getWorkspace();
//		IPath location = Path.fromOSString(file.getAbsolutePath());
//		return workspace.getRoot().findMember(location);
//	}

	private void process(IResource resource) throws CoreException {
		if (!excludes.contains(resource.getName())) {
			if (resource.getType() == IResource.FOLDER) {
				IFolder folder = (IFolder) resource;
				for (IResource child : folder.members()) {
					if (child.getType() == IResource.FOLDER || child.getType() == IResource.FILE) {
						process(child);
						if (folder.members().length == 0 && !resource.equals(rootFile)) {
							resource.delete(true, null);
						}
					}
				}
			} else {
				IFile ifile = (IFile)resource;
				if (filter.delete(ifile)) {
					try {
						ifile.delete(true, null);
					} catch (CoreException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
