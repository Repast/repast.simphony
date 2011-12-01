package repast.simphony.relogo.ide.plugin;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;


public class ReLogoResourceChangeListener implements IResourceChangeListener {

	@Override
	public void resourceChanged(IResourceChangeEvent event) {

		IResource res = event.getResource();
		if (event.getType() != IResourceChangeEvent.POST_CHANGE){
			return;
		}
//		System.out.println("Resources have changed.");
        try {
			event.getDelta().accept(new ReLogoResourceDeltaVisitor());
		} catch (CoreException e) {
			e.printStackTrace();
		}
		IResourceDelta rootDelta = event.getDelta();

	}

}
