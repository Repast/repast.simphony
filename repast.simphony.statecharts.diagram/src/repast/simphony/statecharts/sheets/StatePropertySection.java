
package repast.simphony.statecharts.sheets;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;


/**
 * Property sheet for a State.
 * 
 * @author Nick Collier
 */
public class StatePropertySection extends AbstractEditorPropertySection {

  private StateSheet sheet;

  public void createControls(Composite parent) {
    sheet = new StateSheet(getToolkit(), parent, SWT.NONE);
    GridDataFactory.fillDefaults().grab(true, true).applyTo(sheet);
  }

  @Override
  public void bindModel(EMFDataBindingContext context) {
    sheet.bindModel(context, getEObject());
  }
}
