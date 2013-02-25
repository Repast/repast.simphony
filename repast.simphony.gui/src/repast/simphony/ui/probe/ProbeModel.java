package repast.simphony.ui.probe;

import java.util.ArrayList;
import java.util.List;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.beans.PropertyAccessors;
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
  
  static {
    PropertyAccessors.setProvider(new ProbePropertyAccessProvider());
  }
  
  // This adapter is necessary because under normal use, the bindings
  // framework expects the bean that it binds the gui components to
  // to implements property change event firing when one of its properties
  // change. Our beans (the agents) can be anything so we need to be able
  // to programmatically fire updates in order to update the probe GUI
  // to reflect the current state.
  private static class ProbeBeanAdapter extends BeanAdapter<Object> {

    private List<SimplePropertyAdapter> adapters = new ArrayList<SimplePropertyAdapter>();

    public ProbeBeanAdapter(ValueModel beanChannel) {
      super(beanChannel, false);
    }

    @Override
    protected SimplePropertyAdapter createPropertyAdapter(String propertyName, String getterName,
        String setterName) {
      SimplePropertyAdapter adapter = new SimplePropertyAdapter(propertyName, getterName, setterName);
      adapters.add(adapter);
      return adapter;
    }
    
    public void fireChange() {
      for (SimplePropertyAdapter spa : adapters) {
        spa.fireChange();
      }
    }
    
    // This is necessary to expose the fireChange(Object) method.
    public class SimplePropertyAdapter extends BeanAdapter<Object>.SimplePropertyAdapter {

      protected SimplePropertyAdapter(String propertyName, String getterName, String setterName) {
        super(propertyName, getterName, setterName);
      }
      
      void fireChange() {
        fireChange(getBean());
      }
    }
  }
  
  
  private ProbeBeanAdapter adapter;
  
  
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
    adapter = new ProbeBeanAdapter(beanChannel);
    return adapter;
  }
  
  /**
   * Updates any bindings associated with this model to reflect the
   * current state of the probed object.  
   * 
   */
  public void update() {
    adapter.fireChange();
  }
}

