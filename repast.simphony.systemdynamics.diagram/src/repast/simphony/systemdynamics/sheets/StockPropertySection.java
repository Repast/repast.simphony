
package repast.simphony.systemdynamics.sheets;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.widgets.Composite;


/**
 * Property sheet for a State.
 * 
 * @author Nick Collier
 */
public class StockPropertySection extends AbstractEditorPropertySection {

  private StockSheet sheet;

  public void createControls(Composite parent) {
    sheet = new StockSheet(getToolkit(), parent);
    GridDataFactory.fillDefaults().grab(true, true).applyTo(sheet);
  }

  @Override
  public void bindModel(EMFDataBindingContext context) {
    sheet.bindModel(context, getEObject());
  }
}
