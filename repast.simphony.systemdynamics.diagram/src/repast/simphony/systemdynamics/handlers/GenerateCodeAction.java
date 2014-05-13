/**
 * 
 */
package repast.simphony.systemdynamics.handlers;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;

import repast.simphony.systemdynamics.diagram.part.SystemdynamicsDiagramEditorPlugin;
import repast.simphony.systemdynamics.engine.Engine;


/**
 * @author Nick Collier
 */
public class GenerateCodeAction extends AbstractToolbarAction  {
  
  private static String ID = "repast.simphony.diagram.GenerateCodeAction";
  
  public GenerateCodeAction(IWorkbenchPage workbenchPage) {
    super(workbenchPage);
    setText("Generate Code");
    setId(ID);
    setToolTipText("Generate Code");
    setImageDescriptor(SystemdynamicsDiagramEditorPlugin.getBundledImageDescriptor("icons/obj16/build_tab.gif"));
  }
  
  

  /* (non-Javadoc)
   * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#doRun(org.eclipse.core.runtime.IProgressMonitor)
   */
  @Override
  protected void doRun(IProgressMonitor progressMonitor) {

	  IEditorPart editor = getWorkbenchPage().getActiveEditor();
	  IFileEditorInput input = (IFileEditorInput)editor.getEditorInput();
	  IFile file = input.getFile();
	  IProject project = file.getProject();
	  
	  boolean hybridCompatibility = MessageDialog.openQuestion(null, "System Dynamics/ABMS Hybrid Integration Compatibility", 
			  "If you wish to maintain complete compatibility within a System Dynamics/ABMS hybrid environment, click \"Yes\"\n\n"+
			  "If you wish to create a standalone System Dynamics model click \"No\"");


	  Engine engine = new Engine(model, project, progressMonitor, hybridCompatibility);
	  boolean success = engine.validateGenerateRSD(model, true);

	  GenerateCodeDialog dialog = new GenerateCodeDialog(Display.getCurrent().getActiveShell(), success, "Code Generation:   ", engine.getMessages());
	  dialog.open();

  }
}

 
