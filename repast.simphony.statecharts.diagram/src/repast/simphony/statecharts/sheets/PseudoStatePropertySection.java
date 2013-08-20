
package repast.simphony.statecharts.sheets;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.widgets.Composite;


/**
 * Property sheet for a PseudoState.
 * 
 * @author Nick Collier
 */
public class PseudoStatePropertySection extends AbstractEditorPropertySection {

  public void createControls(Composite parent) {
    sheet = new PseudoStateSheet(getToolkit(), parent);
    GridDataFactory.fillDefaults().grab(true, true).applyTo((Composite)sheet);
  }

  @Override
  public void bindModel(EMFDataBindingContext context) {
    sheet.bindModel(context, getEObject());
  }
}
