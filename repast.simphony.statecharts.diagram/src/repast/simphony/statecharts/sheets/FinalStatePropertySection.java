
package repast.simphony.statecharts.sheets;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;


/**
 * Property sheet for a State.
 * 
 * @author Nick Collier
 */
public class FinalStatePropertySection extends AbstractEditorPropertySection {

  public void createControls(Composite parent) {
    sheet = new FinalStateSheet(getToolkit(), parent);
    GridDataFactory.fillDefaults().grab(true, true).applyTo((Control)sheet);
  }

  @Override
  public void bindModel(EMFDataBindingContext context) {
    sheet.bindModel(context, getEObject());
  }
}
