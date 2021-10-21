package repast.simphony.eclipse.ide;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import repast.simphony.eclipse.RepastSimphonyPlugin;
import repast.simphony.eclipse.util.Utilities;

/**
 * Handler for updating the user model installer files to the latest version
 *   contained in the Repast plugin location.  This will overwrite the user /installer
 *   files with new IzPack config files
 * 
 * @author Eric Tatara
 *
 */
public class UpdateInstallerFilesHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event)
				.getActivePage().getSelection();

		if (selection != null & selection instanceof IStructuredSelection) {
			IStructuredSelection strucSelection = (IStructuredSelection) selection;

			// The first element should be a Java project
			Object firstElement = strucSelection.getFirstElement();
			if (firstElement instanceof IAdaptable){
				IJavaProject project = 
						(IJavaProject)((IAdaptable)firstElement).getAdapter(IJavaProject.class);

				// The project name should be the same as the model name in the installer files
				String projectName = project.getProject().getName();
				String scenarioDirectory = projectName + ".rs";

				// The path to a source folder in the project is needed to pass to the 
				//   model installer file creator which uses this to find the installer folder
				IPath srcPath = null;
				IClasspathEntry list[] = null;
				try {
					list = project.getRawClasspath();
				} catch (JavaModelException e) {
					e.printStackTrace();
				}
				for (IClasspathEntry entry : list) {
					if (entry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
						srcPath = entry.getPath();
					}
				}
				if (srcPath != null) {
					IFolder srcFolder = project.getProject().getFolder(srcPath.removeFirstSegments(1));

					// Ask for confirmation of the project name
					InputDialog dialog = new InputDialog(HandlerUtil.getActiveShell(event), 
							"Installer files updater model name", 
							"Please verify the model name which will be inserted into the new installer files.", 
							projectName, null);

					dialog.open();
					projectName = dialog.getValue();	

					// The map holds string in the installer files that will be replaced
					String[][] variableMap = { { "%MODEL_NAME%", projectName},
						  { "%PROJECT_NAME%", projectName},
		          { "%SCENARIO_DIRECTORY%", scenarioDirectory }};
					
					// Last chance to cancel
					boolean doUpdate = MessageDialog.openConfirm(
							HandlerUtil.getActiveShell(event), "Model installer update confirmation", 
							"Overwrite existing installer files with new versions?");
					
					// Do the file update
					if (doUpdate){
						try {
							// Copy the model installer builder IzPack config files
				      IFolder destFolder = srcFolder.getFolder("../installer");
				      Utilities.copyFolderFromPluginInstallation("installer", destFolder,  variableMap, new NullProgressMonitor());
				      				      
							MessageDialog.openInformation(HandlerUtil.getActiveShell(event), 
									"Model installer update", 
									"Installer files updated successfully.");
						} catch (Exception e) {
							RepastSimphonyPlugin.getInstance().log(e);
							
							MessageDialog.openError(HandlerUtil.getActiveShell(event), 
									"Model installer update", 
									"Installer files update failed!  See error log for details.");
						}
					}
				}
			}
		}
		return null;
	}
}