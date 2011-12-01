package repast.simphony.visualization.editor;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

/**
 * Clones one object into another using introspection and bean properties.
 *
 * @author Nick Collier
 * @deprecated 2D piccolo based code is being removed
 */
public class AgentCloner {

  private Object agent;

  /**
   * Creates an AgentCloner that will clone the specified object.
   *
   * @param agent the agent to clone
   */
  public AgentCloner(Object agent) {
    this.agent = agent;
  }

  /**
   * Clone the object passed in the constructor.
   *
   * @return the clone.
   */
  public Object createClone() throws IntrospectionException, IllegalAccessException,
          InstantiationException, InvocationTargetException {

    Object clone = agent.getClass().newInstance();
    BeanInfo info = Introspector.getBeanInfo(agent.getClass(), Object.class);
    for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
      if (pd.getReadMethod() != null && pd.getWriteMethod() != null) {
        Object val = pd.getReadMethod().invoke(agent);
        pd.getWriteMethod().invoke(clone, val);
      }
    }

    return clone;
  }
}
