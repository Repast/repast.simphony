package repast.simphony.dataLoader.engine;

import java.lang.reflect.InvocationTargetException;

import net.sf.cglib.reflect.FastMethod;
import repast.simphony.context.Context;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunState;
import repast.simphony.scenario.data.ContextData;
import simphony.util.messages.MessageCenter;

/**
 * ContextBuilder implementation that will delegate the building to a
 * ContextXMLBuilder and then call a ModelInitializer class to complete the
 * build.
 * 
 * @author Nick Collier
 */
public class MIContextXMLBuilder implements ContextBuilder<Object> {

  private static MessageCenter msg = MessageCenter.getMessageCenter(MIContextXMLBuilder.class);

  private ContextXMLBuilder builder;
  private FastMethod initializer = null;
  private Class<?> initClass = null;

  public MIContextXMLBuilder(ContextData context, Class<?> initClass, FastMethod initializer) {
    builder = new ContextXMLBuilder(context);
    this.initClass = initClass;
    this.initializer = initializer;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * repast.simphony.dataLoader.ContextBuilder#build(repast.simphony.context
   * .Context)
   */
  @Override
  public Context<?> build(Context<Object> context) {
    Context<?> ret = builder.build(context);
    RunState.setSafeMasterContext(context);
    try {
      Object obj = initClass.newInstance();
      initializer.invoke(obj, new Object[0]);
    } catch (IllegalAccessException ex) {
      msg.error("Error while invoking ModelInitializer '" + initClass.getName() + "'", ex);
    } catch (InstantiationException ex) {
      msg.error("Error while invoking ModelInitializer '" + initClass.getName() + "'", ex);
    } catch (InvocationTargetException ex) {
      msg.error("Error while invoking ModelInitializer '" + initClass.getName() + "'", ex);
    }
    return ret;
  }

}
