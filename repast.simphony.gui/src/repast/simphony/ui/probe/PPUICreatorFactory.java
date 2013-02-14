/**
 * 
 */
package repast.simphony.ui.probe;

import repast.simphony.ui.RSApplication;

/**
 * Interface for classes that can create custom ProbedPropertyUICreator-s.
 * 
 * @author Nick Collier
 */
public interface PPUICreatorFactory {

  /**
   * Initialize this PPUICreatorFactory.
   * 
   * @param app
   */
  void init(RSApplication app);

  /**
   * Creates the ProbedPropertyUICreator for the specified field.
   * 
   * @param obj
   * @param fpd
   * 
   * @return the ProbedPropertyUICreator for the specified object property.
   */
  ProbedPropertyUICreator createUICreator(Object obj, FieldPropertyDescriptor fpd)
      throws IllegalAccessException, IllegalArgumentException;

}
