
package repast.simphony.systemdynamics.sheets;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.widgets.Composite;


/**
 * Property sheet for the SystemModel.
 * 
 * @author Nick Collier
 */
public class SystemModelPropertySection extends AbstractEditorPropertySection {

  private SystemModelSheet sheet;

  public void createControls(Composite parent) {
    sheet = new SystemModelSheet(getToolkit(), parent);
    GridDataFactory.fillDefaults().grab(true, true).applyTo(sheet);
  }

  @Override
  public void bindModel(EMFDataBindingContext context) {
    sheet.bindModel(context, getEObject());
  }
}
