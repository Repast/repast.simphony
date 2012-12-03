
package repast.simphony.statecharts.sheets;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;


/**
 * Property sheet for a PseudoState.
 * 
 * @author Nick Collier
 */
public class PseudoStatePropertySection extends AbstractEditorPropertySection {

  private PseudoStateSheet sheet;

  public void createControls(Composite parent) {
    sheet = new PseudoStateSheet(getToolkit(), parent, SWT.NONE);
    GridDataFactory.fillDefaults().grab(true, true).applyTo(sheet);
  }

  @Override
  public void bindModel(EMFDataBindingContext context) {
    sheet.bindModel(context, getEObject());
  }
}
