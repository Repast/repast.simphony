/**
 * 
 */
package repast.simphony.statecharts.runtime;

import javax.swing.JComponent;

import repast.simphony.statecharts.StateChart;
import repast.simphony.ui.RSApplication;
import repast.simphony.ui.probe.FieldPropertyDescriptor;
import repast.simphony.ui.probe.PPUICreatorFactory;
import repast.simphony.ui.probe.ProbedPropertyUICreator;

/**
 * PPUICreatorFactory for creating the UI probe component for a
 * statechart. 
 * 
 * @author Nick Collier
 */
public class UICreatorFactory implements PPUICreatorFactory {

  /* (non-Javadoc)
   * @see repast.simphony.ui.probe.PPUICreatorFactory#init(repast.simphony.ui.RSApplication)
   */
  @Override
  public void init(RSApplication app) {
    // TODO Auto-generated method stub
    
  }

  /* (non-Javadoc)
   * @see repast.simphony.ui.probe.PPUICreatorFactory#createUICreator(java.lang.Object, repast.simphony.ui.probe.FieldPropertyDescriptor)
   */
  @Override
  public ProbedPropertyUICreator createUICreator(Object obj, FieldPropertyDescriptor fpd)
      throws IllegalAccessException, IllegalArgumentException {
    return new PPUICreator((StateChart<?>)fpd.getField().get(obj), fpd.getDisplayName());
  }
  
  
  private static class PPUICreator implements ProbedPropertyUICreator {
    
    private StateChart<?> statechart;
    private String name;
    
    public PPUICreator(StateChart<?> statechart, String name) {
      this.statechart = statechart;
      this.name = name;
    }

    /* (non-Javadoc)
     * @see repast.simphony.ui.probe.ProbedPropertyUICreator#getDisplayName()
     */
    @Override
    public String getDisplayName() {
      return name;
    }

    @Override
    public JComponent getComponent(PresentationModel<Object> model) {
      // TODO Auto-generated method stub
      return null;
    }
    
    
    
  }
  
  

}
