package repast.simphony.relogo.ide.wizards;

import org.eclipse.core.resources.IProjectNatureDescriptor;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.ui.internal.ide.model.WorkbenchWorkspace;

public class ListAvailableNatures {

	static public void main(String[] args) {
		IProjectNatureDescriptor[] descriptors = ResourcesPlugin.getWorkspace().getNatureDescriptors();
		for (int i=0; i<descriptors.length; i++) {
			System.out.println("*** Nature descriptor "+i);
			System.out.println("\tLabel:\t"+descriptors[i].getLabel());
			System.out.println("\tID:\t"+descriptors[i].getNatureId());
			System.out.println("\tSet IDs:\t"+descriptors[i].getNatureSetIds());
			System.out.println("\tReqd:\t"+descriptors[i].getRequiredNatureIds());
			System.out.println("\tLink?:\t"+descriptors[i].isLinkingAllowed());
		}
	}
}
