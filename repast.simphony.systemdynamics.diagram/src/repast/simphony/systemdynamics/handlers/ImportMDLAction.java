/**
 * 
 */
package repast.simphony.systemdynamics.handlers;

import java.io.IOException;
import java.util.Collections;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.workspace.util.WorkspaceSynchronizer;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler;
import org.eclipse.gmf.runtime.diagram.core.services.ViewService;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.emf.core.GMFEditingDomainFactory;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.ui.IWorkbenchPage;

import repast.simphony.systemdynamics.diagram.edit.parts.SystemModelEditPart;
import repast.simphony.systemdynamics.diagram.part.Messages;
import repast.simphony.systemdynamics.diagram.part.SystemdynamicsDiagramEditorPlugin;
import repast.simphony.systemdynamics.diagram.part.SystemdynamicsDiagramEditorUtil;
import repast.simphony.systemdynamics.sdmodel.SDModelFactory;
import repast.simphony.systemdynamics.sdmodel.SystemModel;
import repast.simphony.systemdynamics.translator.MDLToSystemModel;

/**
 * @author Nick Collier
 */
public class ImportMDLAction extends AbstractActionHandler {

  private static String ID = "repast.simphony.diagram.ImportMDLAction";

  public ImportMDLAction(IWorkbenchPage workbenchPage) {
    super(workbenchPage);
    setText("Import MDL File");
    setId(ID);
    setToolTipText("Import MDL File");
    // setImageDescriptor(SystemdynamicsDiagramEditorPlugin
    // .getBundledImageDescriptor("icons/obj16/debugtt_obj.gif"));
    setEnabled(true);
  }

  // needs to true so that refresh is called when selections changes.
  public boolean isSelectionListener() {
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#doRun(org
   * .eclipse.core.runtime.IProgressMonitor)
   */
  @Override
  protected void doRun(IProgressMonitor progressMonitor) {
    // TODO show a dialog getting the mdl file and the output file
    // call the translator with mdl file, passing the diagram
    
    String rsdFile = "/Users/nick/Documents/workspace/runtime-SystemDynamicsDiagram/sample/test.rsd";
    TransactionalEditingDomain editingDomain = GMFEditingDomainFactory.INSTANCE.createEditingDomain();
    final URI rsdURI = URI.createFileURI(rsdFile);
    final String mdlFile = "/Users/nick/Documents/repos/repast.simphony/repast.simphony.systemdynamics.generator/test_data/EPIDEMIC.MDL";
    final Resource diagramResource = editingDomain.getResourceSet().createResource(rsdURI);
    
    AbstractTransactionalCommand command = new AbstractTransactionalCommand(editingDomain,
        Messages.SystemdynamicsDiagramEditorUtil_CreateDiagramCommandLabel, Collections.EMPTY_LIST) {
      protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info)
          throws ExecutionException {
        SystemModel model = SDModelFactory.eINSTANCE.createSystemModel();
        

        Diagram diagram = ViewService.createDiagram(model, SystemModelEditPart.MODEL_ID,
            SystemdynamicsDiagramEditorPlugin.DIAGRAM_PREFERENCES_HINT);
        if (diagram != null) {
          diagram.setName(rsdURI.lastSegment());
          diagram.setElement(model);
        }
        
        MDLToSystemModel trans = new MDLToSystemModel();
        trans.run(model, diagram, mdlFile);
        
        diagramResource.getContents().add(model);
        diagramResource.getContents().add(diagram);
        try {
          diagramResource
              .save(repast.simphony.systemdynamics.diagram.part.SystemdynamicsDiagramEditorUtil
                  .getSaveOptions());
        } catch (IOException e) {
          SystemdynamicsDiagramEditorPlugin.getInstance().logError(
              "Unable to store model and diagram resources", e); //$NON-NLS-1$
        }
        return CommandResult.newOKCommandResult();
      }
    };
    
    try {
      OperationHistoryFactory.getOperationHistory().execute(command,
          new SubProgressMonitor(progressMonitor, 1), null);
    } catch (ExecutionException e) {
      SystemdynamicsDiagramEditorPlugin.getInstance().logError(
          "Unable import mdl file", e); //$NON-NLS-1$
    }
    
    SystemdynamicsDiagramEditorUtil.setCharset(WorkspaceSynchronizer.getFile(diagramResource));
   
    try {
      // TODO replace with the actual project
      ResourcesPlugin.getWorkspace().getRoot().getProject("sample").refreshLocal(IResource.DEPTH_INFINITE, progressMonitor);
      //SystemdynamicsDiagramEditorUtil.openDiagram(diagramResource);
    } catch (CoreException ex) {
      SystemdynamicsDiagramEditorPlugin.getInstance().logError(
          "Unable to open " + diagramResource, ex);
    }

  }

  @Override
  public void refresh() {
    setEnabled(true);
  }
}
