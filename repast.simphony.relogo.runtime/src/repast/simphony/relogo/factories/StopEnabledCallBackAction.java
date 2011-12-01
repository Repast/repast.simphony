package repast.simphony.relogo.factories;

import java.lang.reflect.InvocationTargetException;

import net.sf.cglib.reflect.FastMethod;
import repast.simphony.engine.schedule.CallBackAction;
import repast.simphony.engine.schedule.IAction;
import repast.simphony.relogo.Stop;

public class StopEnabledCallBackAction implements IAction{
	
	BindableBoolean bool;
	String actionName;
	CallBackAction action;
	Object target;
	FastMethod method;
	Object[] args;
	
	
	public StopEnabledCallBackAction(BindableBoolean bool, Object target, String method,Object... parameters){
		this.bool = bool;
		this.action = new CallBackAction(target, method, parameters);
		this.target = this.action.getTarget();
		this.method = this.action.getFastMethod();
		this.args = this.action.getArgs();
	}

	/**
	   * Calls the method named in the constructor on the target named in the constructor.
	   * If the method invoked returns the enum Stop, the boolean value of the BindableBoolean instance
	   * is set to false. The intent is for the entity bound to the BindableBoolean to be notified of the change.
	   */
	public void execute() {
		Object result = null;
		try {
			result = method.invoke(target, args);
		} catch (InvocationTargetException e) {
			System.err.print("Tried to call: " + method.toString());
			e.printStackTrace();
		}
		if (result != null && result instanceof Stop){
			bool.setBooleanValue(false);
		}
	}

}
