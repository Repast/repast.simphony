package repast.simphony.ui;

/**
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:26:02 $
 */
public class ControllerRegistryBuilderFactory {

  public static IControllerRegistryBuilder createRegistryBuilder(String className) throws ClassNotFoundException,
          IllegalAccessException, InstantiationException
  {
    Class clazz = Class.forName(className);
    IControllerRegistryBuilder builder = (IControllerRegistryBuilder) clazz.newInstance();
    return builder;
  }
}
