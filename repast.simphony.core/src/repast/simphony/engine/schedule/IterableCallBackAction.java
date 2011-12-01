package repast.simphony.engine.schedule;

import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.util.ClassUtilities;
import repast.simphony.util.SimUtilities;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * An IAction created from an Iterable, a method name and method parameters. When an IterableCallBackAction is executed
 * it will call the named method on each object returned by an Iterable, passing the parameters.
 *
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 */
public class IterableCallBackAction implements IAction {

  //static final long serialVersionUID = 3628821571113796716L;

  private Iterable<Object> target;
  // not serializable so don't serialize it
  private transient FastMethod method;
  private Object[] args;
  // we need to serialize this as we can't serialize the method name
  private String methodName;
  private transient IAction executor = new SetupExecutor();
  private boolean shuffle;

  /**
   * Creates an  IterableCallBackAction from the specified Iterable, method name, and method parameters. When executed,
   * the sction will call the named method on each element returned from the target passing the specified parameters.
   *
   * @param target     the object to call the method on
   * @param methodName the name of the method to call
   * @param parameters the parameters to pass to the method
   * @throws IllegalArgumentException if the target does not contain a method with the appropriate signature.
   */
  public IterableCallBackAction(Iterable target, String methodName, boolean shuffle, Object... parameters) {
    this.target = target;
    this.shuffle = shuffle;
    this.methodName = methodName;
    // assumes all params are non-null
    args = new Object[parameters.length];
    System.arraycopy(parameters, 0, args, 0, parameters.length);
  }

  // initializes the method field
  private void initMethod(Object target, String methodName, Class[] paramTypes) {
    FastClass fClass = FastClass.create(target.getClass());
    try {
      // try for the quick find
      method = fClass.getMethod(methodName, paramTypes);
    } catch (NoSuchMethodError er) {
      Method jMethod = ClassUtilities.findMethod(target.getClass(), methodName, paramTypes);
      if (jMethod != null) method = FastClass.create(jMethod.getDeclaringClass()).getMethod(jMethod);
    }

    if (method == null) {
      String pTypes = "(";
      for (int i = 0; i < paramTypes.length; i++) {
        if (i > 0) pTypes += ", ";
        pTypes += paramTypes[i].getName();
      }
      pTypes += ")";
      throw new IllegalArgumentException("Method " + target.getClass().getName() + "." + methodName + pTypes + " not found!");
    }
  }

  /**
   * Calls the method named in the constructor on the targets in the iterable named in the constructor.
   */
  public void execute() {
    executor.execute();
  }


  /**
   * Recreates the FastMethod member using the serialized methodName, target and args.
   *
   * @param stream
   * @throws java.io.IOException
   * @throws ClassNotFoundException
   */
  private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
    stream.defaultReadObject();
    executor = new SetupExecutor();
  }

  class SetupExecutor implements IAction {

    public void execute() {
      if (target.iterator().hasNext()) {
        Class[] paramTypes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
          paramTypes[i] = args[i].getClass();
        }
        initMethod(target.iterator().next(), methodName, paramTypes);
        if (!shuffle) executor = new NoShuffleExecutor();
        else if (target instanceof List) executor = new ShuffleListExecutor();
        else executor = new ShuffleIterableExecutor();
        
        executor.execute();
      }
    }
  }

  class NoShuffleExecutor implements IAction {
    public void execute() {
      try {
        // todo add shuffle
        for (Object obj : target) {
          method.invoke(obj, args);
        }
      } catch (InvocationTargetException e) {
        // todo add proper logging!!!!
        System.err.print("Tried to call: " + method.toString());
        e.printStackTrace();
      }
    }
  }
  
  class ShuffleIterableExecutor implements IAction {
    public void execute() {
      List list = new ArrayList();
      for (Object obj : target) {
        list.add(obj);
      }
      SimUtilities.shuffle(list, RandomHelper.getUniform());
      
      try {
        for (Object obj : list) {
          method.invoke(obj, args);
        }
      } catch (InvocationTargetException e) {
        // todo add proper logging!!!!
        System.err.print("Tried to call: " + method.toString());
        e.printStackTrace();
      }
    }
  }
  
  class ShuffleListExecutor implements IAction {
    public void execute() {
      SimUtilities.shuffle((List)target, RandomHelper.getUniform());
      try {
        for (Object obj : target) {
          method.invoke(obj, args);
        }
      } catch (InvocationTargetException e) {
        // todo add proper logging!!!!
        System.err.print("Tried to call: " + method.toString());
        e.printStackTrace();
      }
    }
  }
}

