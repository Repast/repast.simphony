/**
 */
package repast.simphony.statecharts.scmodel;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Transition</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link repast.simphony.statecharts.scmodel.Transition#getFrom <em>From</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.Transition#getTo <em>To</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.Transition#getPriority <em>Priority</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.Transition#getOnTransition <em>On Transition</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.Transition#isOutOfBranch <em>Out Of Branch</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.Transition#isDefaultTransition <em>Default Transition</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.Transition#getTrigger <em>Trigger</em>}</li>
 * </ul>
 * </p>
 *
 * @see repast.simphony.statecharts.scmodel.StatechartPackage#getTransition()
 * @model
 * @generated
 */
public interface Transition extends EObject {
  /**
   * Returns the value of the '<em><b>From</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>From</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>From</em>' reference.
   * @see #setFrom(AbstractState)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getTransition_From()
   * @model required="true"
   * @generated
   */
  AbstractState getFrom();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.Transition#getFrom <em>From</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>From</em>' reference.
   * @see #getFrom()
   * @generated
   */
  void setFrom(AbstractState value);

  /**
   * Returns the value of the '<em><b>To</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>To</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>To</em>' reference.
   * @see #setTo(AbstractState)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getTransition_To()
   * @model required="true"
   * @generated
   */
  AbstractState getTo();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.Transition#getTo <em>To</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>To</em>' reference.
   * @see #getTo()
   * @generated
   */
  void setTo(AbstractState value);

  /**
   * Returns the value of the '<em><b>Priority</b></em>' attribute.
   * The default value is <code>"0"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Priority</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Priority</em>' attribute.
   * @see #setPriority(int)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getTransition_Priority()
   * @model default="0"
   * @generated
   */
  int getPriority();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.Transition#getPriority <em>Priority</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Priority</em>' attribute.
   * @see #getPriority()
   * @generated
   */
  void setPriority(int value);

  /**
   * Returns the value of the '<em><b>On Transition</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>On Transition</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>On Transition</em>' attribute.
   * @see #setOnTransition(String)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getTransition_OnTransition()
   * @model
   * @generated
   */
  String getOnTransition();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.Transition#getOnTransition <em>On Transition</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>On Transition</em>' attribute.
   * @see #getOnTransition()
   * @generated
   */
  void setOnTransition(String value);

  /**
   * Returns the value of the '<em><b>Out Of Branch</b></em>' attribute.
   * The default value is <code>"false"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Out Of Branch</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Out Of Branch</em>' attribute.
   * @see #setOutOfBranch(boolean)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getTransition_OutOfBranch()
   * @model default="false"
   * @generated
   */
  boolean isOutOfBranch();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.Transition#isOutOfBranch <em>Out Of Branch</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Out Of Branch</em>' attribute.
   * @see #isOutOfBranch()
   * @generated
   */
  void setOutOfBranch(boolean value);

  /**
   * Returns the value of the '<em><b>Default Transition</b></em>' attribute.
   * The default value is <code>"false"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Default Transition</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Default Transition</em>' attribute.
   * @see #setDefaultTransition(boolean)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getTransition_DefaultTransition()
   * @model default="false"
   * @generated
   */
  boolean isDefaultTransition();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.Transition#isDefaultTransition <em>Default Transition</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Default Transition</em>' attribute.
   * @see #isDefaultTransition()
   * @generated
   */
  void setDefaultTransition(boolean value);

  /**
   * Returns the value of the '<em><b>Trigger</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Trigger</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Trigger</em>' reference.
   * @see #setTrigger(Trigger)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getTransition_Trigger()
   * @model required="true"
   * @generated
   */
  Trigger getTrigger();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.Transition#getTrigger <em>Trigger</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Trigger</em>' reference.
   * @see #getTrigger()
   * @generated
   */
  void setTrigger(Trigger value);

} // Transition
