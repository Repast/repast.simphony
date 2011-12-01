package repast.simphony.context.space.graph;

import simphony.util.messages.MessageCenter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Creates an agent / node from a Class. If the Class has a constructor with
 * a single String parameter, the label will be passed in the Constructor.
 *
 * @author Nick Collier
 */
public class DefaultNodeCreator implements NodeCreator {

  private static MessageCenter msg = MessageCenter.getMessageCenter(DefaultNodeCreator.class);

  private Class nodeClass;
  private Constructor constructor;


  /**
   * Creates a DefaultNodeCreator that will create nodes/agents from the
   * specified class.
   *
   * @param nodeClass the class of the agents /nodes to create
   */
  public DefaultNodeCreator(Class nodeClass) {
    this.nodeClass = nodeClass;
    try {
      constructor = nodeClass.getConstructor(String.class);
    } catch (NoSuchMethodException e) {
    }
  }

  /**
   * Creates and returns a node to be added to a network
   * via a context.
   *
   * @param label the node label. If the node label does not exist
   *              this will be an empty string
   * @return the created Node.
   */
  public Object createNode(String label) {
    try {
      if (constructor != null) {
        return constructor.newInstance(label);
      } else {
        return nodeClass.newInstance();
      }
    } catch (IllegalAccessException e) {
      msg.error("Error while creating agent / node", e);
    } catch (InvocationTargetException e) {
      msg.error("Error while creating agent / node", e);
    } catch (InstantiationException e) {
      msg.error("Error while creating agent / node", e);
    }

    return null;
  }
}
