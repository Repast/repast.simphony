package repast.simphony.systemdynamics.sheets;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.ui.properties.sections.AbstractModelerPropertySection;
import org.eclipse.jface.dialogs.IDialogLabelKeys;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.help.IWorkbenchHelpSystem;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

/**
 * 
 */
public abstract class AbstractEditorPropertySection extends AbstractModelerPropertySection {

  public abstract void createControls(Composite parent);

  public abstract void bindModel(EMFDataBindingContext context);

  private FormToolkit toolkit;

  private EMFDataBindingContext bindingContext;

  @Override
  public void refresh() {
    super.refresh();
    if (bindingContext == null) {
      bindingContext = new EMFDataBindingContext();
      bindModel(bindingContext);
    }
  }
  
  @Override
  protected void setEObject(EObject object) {
    super.setEObject(object);
    if (bindingContext != null) {
      bindingContext.dispose();
    }
    bindingContext = new EMFDataBindingContext();
    bindModel(bindingContext);
  }

  @Override
  public void dispose() {
    super.dispose();
    if (bindingContext != null)
      bindingContext.dispose();
    if (toolkit != null)
      toolkit.dispose();
  }

  @Override
  public final void createControls(Composite parent,
      TabbedPropertySheetPage aTabbedPropertySheetPage) {
    super.createControls(parent, aTabbedPropertySheetPage);
    
    toolkit = new FormToolkit(parent.getDisplay());
    toolkit.setBorderStyle(SWT.BORDER);
    GridDataFactory.fillDefaults().grab(true, true).applyTo(parent);
    parent.setLayout(new GridLayout(1, true));
    createControls(parent);
  }
  
  public FormToolkit getToolkit() {
    return toolkit;
  }

  public EObject getContextObject() {
    return getEObject();
  }
}
