package repast.simphony.ui;

import repast.simphony.engine.environment.ControllerRegistry;

/**
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:26:02 $
 */
public interface IControllerRegistryBuilder {

  public ControllerRegistry buildRegistry(ControllerRegistry registry);
  
}
