package repast.simphony.systemsdynamics;

import groovy.lang.GroovyRuntimeException;
import groovy.lang.GroovyShell;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.AbstractAction;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Formula extends AbstractAction {

	public final static long serialVersionUID = 0;

	public enum Type {
		EQUATION, EULER
	}

	protected Object parent;
	protected String targetVariable;
	protected String formula;
	protected Type formulaType;
	protected double lastExecutionTime = 0;
	protected ISchedule schedule = RunEnvironment.getInstance()
			.getCurrentSchedule();

	public Formula(Object newParent, String newTargetVariable,
			String newFormula) {

		this(newParent, newTargetVariable, newFormula, Type.EQUATION);
		
	}
	
	public Formula(Object newParent, String newTargetVariable,
			String newFormula, Type newFormulaType) {

		this(newParent, newTargetVariable, newFormula, newFormulaType, null);

	}

	public Formula(Object newParent, String newTargetVariable,
			String newFormula, Type newFormulaType, ScheduleParameters newScheduleParameters) {

		super(newScheduleParameters == null ? ScheduleParameters.createRepeating(1, 1) : newScheduleParameters);

		this.parent = newParent;
		this.targetVariable = newTargetVariable;
		this.formula = newFormula;
		this.formulaType = newFormulaType;

	}


	public void execute() {

		double dt = this.schedule.getTickCount() - lastExecutionTime;

		String fullFormula = "import static repast.simphony.engine.environment.RunEnvironment.*\n"
				+ "import static java.lang.Math.*\n"
				+ "import static repast.simphony.random.RandomHelper.*\n"
				+ "t = " + this.schedule.getTickCount() + "\n"
				+ "dt = " + dt + "\n";
	
		if (this.formulaType == Type.EQUATION) {
			fullFormula = fullFormula + "(" + this.formula + ")" + "\n";
		} else if (this.formulaType == Type.EULER) {
			fullFormula = fullFormula + "(" + this.formula + ") * dt\n";
		} 

		// TODO Evaluate fullFormula
		parent."${targetVariable}" = runString(fullFormula,parent)

	}

	/**
	* Returns the result of interpreting the string as a script with Object o's class bindings,
	* also adding the classes static methods and fields to the script binding.
	*/
   private static Object runString(String string, Object o){
	   Map objectPropertiesAndMethods = getObjectPropertiesAndMethods(o)
	   def binding = new Binding(objectPropertiesAndMethods)
	   def shell = new GroovyShell(binding)
	   return shell.evaluate(string)
   }
   
   private static Map getObjectPropertiesAndMethods(Object o){
	   Map oProps =  o.getProperties()
	   def methodsList = o.getMetaClass().getMethods()
	   Map oMethods = [:]
	   methodsList.each {
		   oMethods[it.getName()] = this.&"${it.getName()}"
	   }
	   return oProps + oMethods
   }
   
	public Object getParent() {
		return parent;
	}

	public void setParent(Object parent) {
		this.parent = parent;
	}

	public String getTargetVariable() {
		return targetVariable;
	}

	public void setTargetVariable(String targetVariable) {
		this.targetVariable = targetVariable;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public Type getFormulaType() {
		return formulaType;
	}

	public void setFormulaType(Type formulaType) {
		this.formulaType = formulaType;
	}

	public void setScheduleParameters(ScheduleParameters scheduleParameters) {

		this.cancel();
		this.schedule.schedule(this.scheduleParameters, this);

	}

	public void cancel() {

		this.schedule.removeAction(this);

	}

	public double getLastExecutionTime() {
		return lastExecutionTime;
	}

	public void setLastExecutionTime(double lastExecutionTime) {
		this.lastExecutionTime = lastExecutionTime;
	}

}
