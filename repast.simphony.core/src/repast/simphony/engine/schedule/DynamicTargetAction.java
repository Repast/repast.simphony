package repast.simphony.engine.schedule;

import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import repast.simphony.util.ClassUtilities;
import simphony.util.messages.MessageCenter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * An IAction created from a Method. The target of the method can be changed.
 *
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 */
public class DynamicTargetAction implements IAction {

	static MessageCenter msgCenter = MessageCenter.getMessageCenter(DynamicTargetAction.class);

  static final long serialVersionUID = 3628821571113796716L;
	private static final Class[] EMPTY_CLASS_ARRAY = new Class[]{};

  private Object target;
	private Class<?> targetClass;
	 // not serializable so don't serialize it
  private transient FastMethod method;
  // we need to serialize this as we can't serialize the method name
  private String methodName;

  /**
   * Creates a DynamicTargetAction from the specified tmethod. When executed, the DynamicAction
   * will call the the method on whatever the current target is.
   *
   * @param method the method to call
   */
  public DynamicTargetAction(Method method) {
	  targetClass = method.getDeclaringClass();
	  this.method = FastClass.create(targetClass).getMethod(method);
    methodName = this.method.getName();
  }

  // initializes the method field
  private void initMethod(String methodName) {
    FastClass fClass = FastClass.create(targetClass);
    try {
      // try for the quick find
      method = fClass.getMethod(methodName, EMPTY_CLASS_ARRAY);
    } catch (NoSuchMethodError er) {
      Method jMethod = ClassUtilities.findMethod(target.getClass(), methodName, EMPTY_CLASS_ARRAY);
      if (jMethod != null) method = FastClass.create(jMethod.getDeclaringClass()).getMethod(jMethod);
    }

    if (method == null) {
      // todo add some logging
      throw new IllegalArgumentException("Method " + target.getClass().getName() + "." + methodName +"."+
				      " not found!");
    }
  }


  /**
   * Calls the method named in the constructor on the target named in the constructor.
   */
  public void execute() {
    try {
      method.invoke(target, EMPTY_CLASS_ARRAY);
    } catch (InvocationTargetException e) {
      msgCenter.error("Tried to call: " + method.toString(), e);
    }
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
    initMethod(this.methodName);
  }

  /**
   *
   * @return the name of the method to be called on the target object.
   */
  public String getMethodName() {
    return methodName;
  }

	/**
	 * Sets the target of this action.
	 *
	 * @param obj the target
	 */
	public void setTarget(Object obj) {
		this.target = obj;
	}

  /**
   *
   * @return the target object whose method will be called in the execution of this action.
   */
  public Object getTarget() {
    return target;
  }
}
