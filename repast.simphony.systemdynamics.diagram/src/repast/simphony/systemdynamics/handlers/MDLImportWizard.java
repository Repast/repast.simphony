/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package repast.simphony.systemdynamics.handlers;

import java.io.IOException;
import java.util.Collections;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.emf.workspace.util.WorkspaceSynchronizer;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.core.services.ViewService;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.emf.core.GMFEditingDomainFactory;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

import repast.simphony.systemdynamics.diagram.edit.parts.SystemModelEditPart;
import repast.simphony.systemdynamics.diagram.part.Messages;
import repast.simphony.systemdynamics.diagram.part.SystemdynamicsDiagramEditor;
import repast.simphony.systemdynamics.diagram.part.SystemdynamicsDiagramEditorPlugin;
import repast.simphony.systemdynamics.diagram.part.SystemdynamicsDiagramEditorUtil;
import repast.simphony.systemdynamics.sdmodel.SDModelFactory;
import repast.simphony.systemdynamics.sdmodel.SDModelPackage;
import repast.simphony.systemdynamics.sdmodel.SystemModel;
import repast.simphony.systemdynamics.translator.MDLToSystemModel;

public class MDLImportWizard extends Wizard implements IImportWizard {

  MDLImportWizardPage mainPage;

  public MDLImportWizard() {
    super();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.wizard.Wizard#performFinish()
   */
  public boolean performFinish() {
    IFile file = mainPage.createNewFile();
    if (file == null)
      return false;

    boolean translate = translateFile(file);

    return translate;
  }

  private boolean translateFile(final IFile rsdFile) {

    TransactionalEditingDomain editingDomain = GMFEditingDomainFactory.INSTANCE
        .createEditingDomain();
    final String mdlFile = mainPage.editor.getStringValue();
    final Resource diagramResource = editingDomain.getResourceSet().createResource(
        URI.createFileURI(rsdFile.getLocation().toPortableString()));

    AbstractTransactionalCommand command = new AbstractTransactionalCommand(editingDomain,
        Messages.SystemdynamicsDiagramEditorUtil_CreateDiagramCommandLabel, Collections.EMPTY_LIST) {
      protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info)
          throws ExecutionException {
        SystemModel model = SDModelFactory.eINSTANCE.createSystemModel();
        
        model.setClassName(mainPage.getClassName());
        model.setPackage(mainPage.getPackage());

        Diagram diagram = ViewService.createDiagram(model, SystemModelEditPart.MODEL_ID,
            SystemdynamicsDiagramEditorPlugin.DIAGRAM_PREFERENCES_HINT);
        if (diagram != null) {
          diagram.setName(rsdFile.getProjectRelativePath().lastSegment());
          diagram.setElement(model);
        }

        MDLToSystemModel trans = new MDLToSystemModel();
        trans.run(model, diagram, mdlFile);
        GenerateCodeDialog dialog = null;
        

        diagramResource.getContents().add(model);
        diagramResource.getContents().add(diagram);
        try {
          diagramResource
              .save(repast.simphony.systemdynamics.diagram.part.SystemdynamicsDiagramEditorUtil
                  .getSaveOptions());

          SystemdynamicsDiagramEditorUtil
              .setCharset(WorkspaceSynchronizer.getFile(diagramResource));

          rsdFile.getProject().refreshLocal(IResource.DEPTH_INFINITE, null);
          
          IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
          page.openEditor(new FileEditorInput(rsdFile), SystemdynamicsDiagramEditor.ID);

        } catch (IOException | CoreException e) {
          SystemdynamicsDiagramEditorPlugin.getInstance().logError(
              "Unable to store import model and diagram resources", e); //$NON-NLS-1$
        }
        
        if (trans.isFatal()) {
            dialog = new GenerateCodeDialog(Display.getCurrent().getActiveShell(), false, "MDL Import:   ", trans.getFatalMessages());
        } else {
        	dialog = new GenerateCodeDialog(Display.getCurrent().getActiveShell(), true, "MDL Import:   ", "Import Successful");
        }
        	  dialog.open();
    	  
        return CommandResult.newOKCommandResult();
      }
    };

    try {
      OperationHistoryFactory.getOperationHistory().execute(command, null, null);
    } catch (ExecutionException e) {
      SystemdynamicsDiagramEditorPlugin.getInstance().logError("Unable import mdl file", e); //$NON-NLS-1$
    }
    return true;
  }
  
  private IResource extractSelection(IStructuredSelection selection) {
    IStructuredSelection ss = (IStructuredSelection) selection;
    Object element = ss.getFirstElement();
    if (element instanceof IResource)
      return (IResource) element;
    if (!(element instanceof IAdaptable))
      return null;
    IAdaptable adaptable = (IAdaptable) element;
    Object adapter = adaptable.getAdapter(IResource.class);
    return (IResource) adapter;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
   * org.eclipse.jface.viewers.IStructuredSelection)
   */
  public void init(IWorkbench workbench, IStructuredSelection selection) {
    setWindowTitle("MDL Import Wizard"); // NON-NLS-1
    setNeedsProgressMonitor(true);
    IResource resource = extractSelection(selection);
    IProject project = null;
    if (resource != null) {
      project = resource.getProject();
    }
    mainPage = new MDLImportWizardPage("Import MDL File", selection, project); // NON-NLS-1
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.wizard.IWizard#addPages()
   */
  public void addPages() {
    super.addPages();
    addPage(mainPage);
  }

}
