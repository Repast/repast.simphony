
package repast.simphony.statecharts.sheets;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.widgets.Composite;

import repast.simphony.statecharts.scmodel.StatechartPackage;
import repast.simphony.statecharts.scmodel.Transition;


/**
 * Property sheet for a transition.
 * 
 * @author Nick Collier
 */
public class TransitionPropertySection extends AbstractEditorPropertySection {

  private TransitionSheet sheet;

  public void createControls(Composite parent) {
    sheet = new TransitionSheet(getToolkit(), parent);
    GridDataFactory.fillDefaults().grab(true, true).applyTo(sheet);
  }

  @Override
  public void bindModel(EMFDataBindingContext context) {
    sheet.bindModel(context, getEObject());
  }

  @Override
  public void update(Notification notification, EObject element) {
    super.update(notification, element);
    if (StatechartPackage.eINSTANCE.getTransition_OutOfBranch().equals(notification.getFeature())) {
      sheet.doOutOfChoiceCheck();
      sheet.defaultOutChanged(((Transition)element).isDefaultTransition());
    }
  }
}
