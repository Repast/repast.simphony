/**
 * 
 */
package repast.simphony.statecharts.generator;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jdt.core.JavaCore;

import repast.simphony.statecharts.scmodel.StateMachine;

/**
 * Validates a Statemanchine semantic object.
 * 
 * @author Nick Collier
 */
public class StateMachineValidator {

  /**
   * Validates the class name property of the specified state machine. 
   * 
   * @param stateMachine
   * 
   * @return the results of the validation as an IStatus object.
   */
  public IStatus validateClassName(StateMachine stateMachine) {
    IStatus status = JavaConventions.validateJavaTypeName(stateMachine.getClassName(),
        JavaCore.getOption(JavaCore.COMPILER_SOURCE),
        JavaCore.getOption(JavaCore.COMPILER_COMPLIANCE));
    return status;
  }

  /**
   * Validates the agent type property of the specified state machine. 
   * 
   * @param stateMachine
   * 
   * @return the results of the validation as an IStatus object.
   */
  public IStatus validateAgentType(StateMachine stateMachine) {
    IStatus status = JavaConventions.validateJavaTypeName(stateMachine.getAgentType(),
        JavaCore.getOption(JavaCore.COMPILER_SOURCE),
        JavaCore.getOption(JavaCore.COMPILER_COMPLIANCE));
    return status;
  }

  /**
   * Validates the package property of the specified state machine. 
   * 
   * @param stateMachine
   * 
   * @return the results of the validation as an IStatus object.
   */
  public IStatus validatePackage(StateMachine stateMachine) {
    IStatus status = JavaConventions.validatePackageName(stateMachine.getPackage(),
        JavaCore.getOption(JavaCore.COMPILER_SOURCE),
        JavaCore.getOption(JavaCore.COMPILER_COMPLIANCE));
    return status;
  }

  /**
   * Validates the class name, package and agent type properties 
   * of the specified state machine. 
   * 
   * @param stateMachine
   * 
   * @return the results of the validation as an IStatus object.
   */
  public IStatus validate(StateMachine stateMachine) {

    IStatus status = validateClassName(stateMachine);
    if (status.getSeverity() == IStatus.ERROR) return status;

    status = validateAgentType(stateMachine);
    if (status.getSeverity() == IStatus.ERROR)
      return status;

    return validatePackage(stateMachine);
  }
}
