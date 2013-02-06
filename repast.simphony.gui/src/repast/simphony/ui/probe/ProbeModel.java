package repast.simphony.ui.probe;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.value.ValueModel;

/**
 * Wraps the probed object in a jgoodies binding PresentationModel. The binding takes
 * care of transmitting updates made in the GUI (i.e. a panel that shows the 
 * objects properties) to the actual object itself. 
 *
 * @author Nick Collier
 */
@SuppressWarnings("serial")
public class ProbeModel extends PresentationModel<Object> {
  
  public ProbeModel(Object probedObject) {
    super(probedObject);
  }

  /* (non-Javadoc)
   * @see com.jgoodies.binding.PresentationModel#createBeanAdapter(com.jgoodies.binding.value.ValueModel)
   */
  // This is necessary to override the default PresentationModel behavior that requires the
  // bean for which this is a model to implement addPropertyChangeListener, removePropertyChangeListener.
  // We are working with arbitrary objects that most likely don't conform to the bean spec.
  @Override
  protected BeanAdapter<Object> createBeanAdapter(ValueModel beanChannel) {
    return new BeanAdapter<Object>(beanChannel, false);
  }
  
  /**
   * Updates any bindings associated with this model to reflect the
   * current state of the probed object.  
   * 
   */
  public void update() {
    fireMultiplePropertiesChanged();
  }
}

