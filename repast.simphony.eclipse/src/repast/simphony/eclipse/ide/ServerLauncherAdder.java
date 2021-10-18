package repast.simphony.eclipse.ide;

import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.IAction;

import repast.simphony.eclipse.RSProjectConfigurator;
import repast.simphony.eclipse.RepastSimphonyPlugin;
import repast.simphony.eclipse.action.FileSystemSelectionAction;

/**
 * Adds a launcher for repast simphony server to an existing project.
 * 
 * @author nick
 */
public class ServerLauncherAdder extends FileSystemSelectionAction {

    /**
     * Constructs this action instance and specifies that the selected objects
     * should be of type <code>IProject</code>
     */
    public ServerLauncherAdder() {
        super(IProject.class);
    }

    /**
     * Runs the action for the selected objects. Updates the Repast Simphony nature
     * on the selected projects.
     * 
     * @param selectedProjects the selected projects
     * @see repast.simphony.agents.designer.ui.actions.FileSystemSelectionAction#run(org.eclipse.jface.action.IAction,
     *      java.util.List)
     */
    @SuppressWarnings("rawtypes")
    @Override
    public void run(IAction action, List selectedProjects) {
        for (Object obj : selectedProjects) {
            IJavaProject project = JavaCore.create((IProject)obj);
            try {
                if (project.getProject().hasNature(RepastSimphonyPlugin.REPAST_SIMPHONY_NATURE_ID)) {
                    IFolder launchFolder = project.getProject().getFolder("./launchers");
                    if (launchFolder.exists()) {
                        ServerLauncherCreator creator = new ServerLauncherCreator();
                        creator.run(project, launchFolder);
                    }
                }
            } catch (CoreException ex) {
                RepastSimphonyPlugin.getInstance().log(ex);
            }

        }
    }

}
