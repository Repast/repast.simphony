
package repast.simphony.statecharts.sheets;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.widgets.Composite;


/**
 * Property sheet for a State.
 * 
 * @author Nick Collier
 */
public class StatePropertySection extends AbstractEditorPropertySection {

  public void createControls(Composite parent) {
    sheet = new StateSheet(getToolkit(), parent);
    GridDataFactory.fillDefaults().grab(true, true).applyTo((Composite)sheet);
  }

  @Override
  public void bindModel(EMFDataBindingContext context) {
    sheet.bindModel(context, getEObject());
  }
}
