package repast.simphony.engine.schedule;

import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import repast.simphony.util.ClassUtilities;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * An IAction created from a method name and method parameters. When a CallBackAction is executed
 * it will call the named method passing the parameters.
 *
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 */
public class CallBackAction implements IAction {

  static final long serialVersionUID = 3628821571113796716L;

  private Object target;
  // not serializable so don't serialize it
  private transient FastMethod method;
  private Object[] args;
  // we need to serialize this as we can't serialize the method name
  private String methodName;

  /**
   * Creates a CallBackAction from the specified target and method. When executed, the CallBackAction
   * will call the the method on the target.
   *
   * @param target the object to call the method on
   * @param method the method to call
   */
  public CallBackAction(Object target, Method method) {
    args = new Object[method.getParameterTypes().length];
    this.target = target;
    this.method = FastClass.create(target.getClass()).getMethod(method);
    methodName = this.method.getName();
  }

  /**
   * Creates a CallBackAction from the specified target, method and parameters. When executed, the CallBackAction
   * will call the the method on the target passing the parameters.
   *
   * @param target     the object to call the method on
   * @param method     the method to call
   * @param parameters the parameters to pass to the method call
   */
  public CallBackAction(Object target, Method method, Object... parameters) {
    args = new Object[parameters.length];
    this.target = target;
    this.method = FastClass.create(target.getClass()).getMethod(method);
    System.arraycopy(parameters, 0, args, 0, parameters.length);
    methodName = this.method.getName();
  }

  /**
   * Creates a CallBackAction from the specified target, method name, and method parameters. When executed,
   * the CallBack action will call the named method on the target passing the specified parameters.
   *
   * @param target     the object to call the method on
   * @param methodName the name of the method to call
   * @param parameters the parameters to pass to the method
   * @throws IllegalArgumentException if the target does not contain a method with the appropriate signature.
   */
  public CallBackAction(Object target, String methodName, Object... parameters) {
    this.target = target;
    this.methodName = methodName;
    // assumes all params are non-null
    args = new Object[parameters.length];
    System.arraycopy(parameters, 0, args, 0, parameters.length);
    Class[] paramTypes = new Class[parameters.length];
    for (int i = 0; i < args.length; i++) {
      paramTypes[i] = args[i].getClass();
    }
    initMethod(target, methodName, paramTypes);
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
      // todo add some logging
      throw new IllegalArgumentException("Method " + target.getClass().getName() + "." + methodName + pTypes + " not found!");
    }
  }

  /**
   * Sets the argument that is passed to the wrapped method. Note that using this method is only necessary
   * if the parameters passed into the constructor do not act as references. For example, if an int value
   * is passed in the constructor that int value will be automatically updated as a reference to some other
   * int value. This method allows client code to set the value of that int.
   *
   * @param index the index into the argument array
   * @param arg   the argument to pass to the method
   */
  public void setArgs(int index, Object arg) {
    args[index] = arg;
  }
  
  public Object[] getArgs(){
	  return args;
  }

  /**
   * Calls the method named in the constructor on the target named in the constructor.
   */
  public void execute() {
    try {
      method.invoke(target, args);
    } catch (InvocationTargetException e) {
      // todo add proper logging!!!!
      System.err.print("Tried to call: " + method.toString());
      e.printStackTrace();
    }
  }

  /**
   * Recreates the FastMethod member using the serialized methodName, target and args.
   *
   * @param stream
   * @throws IOException
   * @throws ClassNotFoundException
   */
  private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
    stream.defaultReadObject();
    Class[] params = new Class[this.args.length];
    for (int i = 0; i < this.args.length; i++) {
      params[i] = this.args[i].getClass();
    }

    initMethod(this.target, this.methodName, params);
  }

  /**
   * @return the name of the method to be called on the target object.
   */
  public String getMethodName() {
    return methodName;
  }

  /**
   * Gets the method that this callback action will call.
   *
   * @return the method that this callback action will call.
   */
  public Method getMethod() {
    return this.method.getJavaMethod();
  }
  
  public FastMethod getFastMethod(){
	  return this.method;
  }

  /**
   * @return the target object whose method will be called in the execution of this action.
   */
  public Object getTarget() {
    return target;
  }

  /**
   * Sets the target of the method call.
   *
   * @param obj the new target of the method call
   */
  public void setTarget(Object obj) {
    this.target = obj;
  }
}

