/**
 * 
 */
package repast.simphony.systemdynamics.handlers;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
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
	
    System.out.println("generate code");
    Engine engine = new Engine(model);
//    boolean success2 = engine.validateGenerateRSD(model, true);
    boolean success = engine.validateGenerateMDL("C:/eclipse15Dec2010/eclipse/workspaces/workspaceMSC/RSSD/mdl/EnergySecurity8_3_1.mdl", true);
    
    MessageBox msgBox = null;
    
    if (success) {
    	msgBox = new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_INFORMATION);
    } else {
    	msgBox = new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_ERROR);
    }
    
    msgBox.setMessage(engine.getMessages());
    msgBox.open();
    
  }
}

 
