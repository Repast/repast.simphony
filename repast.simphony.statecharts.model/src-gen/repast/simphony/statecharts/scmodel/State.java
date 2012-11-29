/**
 */
package repast.simphony.statecharts.scmodel;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>State</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link repast.simphony.statecharts.scmodel.State#getOnExit <em>On Exit</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.State#getOnEnter <em>On Enter</em>}</li>
 * </ul>
 * </p>
 *
 * @see repast.simphony.statecharts.scmodel.StatechartPackage#getState()
 * @model
 * @generated
 */
public interface State extends AbstractState {
  /**
   * Returns the value of the '<em><b>On Exit</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>On Exit</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>On Exit</em>' reference.
   * @see #setOnExit(Action)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getState_OnExit()
   * @model required="true"
   * @generated
   */
  Action getOnExit();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.State#getOnExit <em>On Exit</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>On Exit</em>' reference.
   * @see #getOnExit()
   * @generated
   */
  void setOnExit(Action value);

  /**
   * Returns the value of the '<em><b>On Enter</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>On Enter</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>On Enter</em>' reference.
   * @see #setOnEnter(Action)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getState_OnEnter()
   * @model required="true"
   * @generated
   */
  Action getOnEnter();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.State#getOnEnter <em>On Enter</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>On Enter</em>' reference.
   * @see #getOnEnter()
   * @generated
   */
  void setOnEnter(Action value);

} // State
