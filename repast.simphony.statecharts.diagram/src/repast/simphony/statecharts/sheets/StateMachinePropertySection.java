
package repast.simphony.statecharts.sheets;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.widgets.Composite;


/**
 * Property sheet for a History state.
 * 
 * @author Nick Collier
 */
public class StateMachinePropertySection extends AbstractEditorPropertySection {

  private StateMachineSheet sheet;

  public void createControls(Composite parent) {
    sheet = new StateMachineSheet(getToolkit(), parent);
    GridDataFactory.fillDefaults().grab(true, true).applyTo(sheet);
  }

  @Override
  public void bindModel(EMFDataBindingContext context) {
    sheet.bindModel(context, getEObject());
  }
}
