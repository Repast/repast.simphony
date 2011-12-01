package repast.simphony.dataLoader.engine;

import java.io.Serializable;

import repast.simphony.context.Context;
import repast.simphony.context.Contexts;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.DefaultControllerAction;
import repast.simphony.engine.environment.RunState;
import repast.simphony.parameter.BoundParameters;
import repast.simphony.parameter.Parameters;
import repast.simphony.scenario.data.ContextData;
import repast.simphony.util.ContextUtils;
import simphony.util.messages.MessageCenter;

/**
 * @author Nick Collier
 * @author Jerry Vos
 */
public class DataLoaderControllerAction<T extends ContextBuilder> extends DefaultControllerAction  {

  private static MessageCenter msgCenter = MessageCenter
      .getMessageCenter(DataLoaderControllerAction.class);

  private T builder;
  private String title;
  private ContextData rootContext;

  public DataLoaderControllerAction(String title, T builder, ContextData root) {
    this.title = title;
    this.builder = builder;
    this.rootContext = root;
  }

  @Override
  public void runInitialize(RunState runState, Object contextId, Parameters params) {
    /*
     * if data loader is an instance of context create it and pass it to the
     * builder, otherwise create a DefaultContext and pass that to the builder.
     */
    if (runState.getMasterContext() == null) {
      // create the root context
      Context<?> context = createContext(contextId);
      if (params instanceof BoundParameters) {
        ((BoundParameters) params).setBean(context);
      }

      context = builder.build(context);
      runState.setMasterContext(context);
      // nc: removed -- user is responsible for adding subcontexts
      // addSubContext(context);

    } else {
      Context<?> context = runState.getMasterContext().findContext(contextId);
      Context<?> newContext = builder.build(context);
      if (!newContext.equals(context)) {
        // replace the context with the new context in the parent
        Context parent = ContextUtils.getParentContext(context);
        parent.removeSubContext(context);
        parent.addSubContext(newContext);
      }
      // nc: removed -- user is responsible for adding subcontexts
      // addSubContext(context);
    }

  }

  /*
   * // adds subcontexts if they haven't been added already private void
   * addSubContext(Context context) { // if specified context has no subcontexts
   * and // and subcontexts of this context are defined in // .score file then
   * create and add them if (!context.getSubContexts().iterator().hasNext() &&
   * rootContext != null) { ContextData subContext = findSubContext(context); if
   * (sContext != null && sContext.getSubContexts().size() > 0) { for (SContext
   * sub : sContext.getSubContexts()) { Context subContext = createContext(sub,
   * sub.getLabel()); context.addSubContext(subContext); } } } }
   * 
   * // find the private SContext findSubContext(Context context) { for
   * (SContext sc : rootSContext.getAllContexts()) { if
   * (sc.getLabel().equals(context.getTypeID())) return sc; } return null; }
   */

  private Context createContext(Object contextId) {
    ContextData data = rootContext.find(contextId.toString());
    if (data.getContextClassName() != null) {
      try {

        Class<?> clazz = Class.forName(data.getContextClassName());
        if (Context.class.isAssignableFrom(clazz)) {
          Context context = (Context) clazz.newInstance();
          context.setId(contextId);
          context.setTypeID(contextId);
          return context;
        }

      } catch (IllegalAccessException e) {
        msgCenter.error("Error while running data loader action", e);
      } catch (InstantiationException e) {
        msgCenter.error("Error while running data loader action", e);
      } catch (ClassNotFoundException e) {
        msgCenter.error("Error while running data loader action", e);
      }
    }
    
    return Contexts.createContext(Object.class, contextId);

  }

  @Override
  public void runCleanup(RunState runState, Object contextId) {
    super.runCleanup(runState, contextId);
    // ContextUtils.unregisterRootContext(runState.getMasterContext());
  }

  @Override
  public String toString() {
    return title;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public T getBuilder() {
    return builder;
  }

  public void setBuilder(T loader) {
    this.builder = loader;
  }
}
