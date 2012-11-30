/**
 */
package repast.simphony.statecharts.scmodel;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Composite State</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link repast.simphony.statecharts.scmodel.CompositeState#getChildren <em>Children</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.CompositeState#getOnEnter <em>On Enter</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.CompositeState#getOnExit <em>On Exit</em>}</li>
 * </ul>
 * </p>
 *
 * @see repast.simphony.statecharts.scmodel.StatechartPackage#getCompositeState()
 * @model
 * @generated
 */
public interface CompositeState extends AbstractState {
  /**
   * Returns the value of the '<em><b>Children</b></em>' containment reference list.
   * The list contents are of type {@link repast.simphony.statecharts.scmodel.AbstractState}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Children</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Children</em>' containment reference list.
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getCompositeState_Children()
   * @model containment="true"
   * @generated
   */
  EList<AbstractState> getChildren();

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
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getCompositeState_OnEnter()
   * @model required="true"
   * @generated
   */
  Action getOnEnter();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.CompositeState#getOnEnter <em>On Enter</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>On Enter</em>' reference.
   * @see #getOnEnter()
   * @generated
   */
  void setOnEnter(Action value);

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
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getCompositeState_OnExit()
   * @model required="true"
   * @generated
   */
  Action getOnExit();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.CompositeState#getOnExit <em>On Exit</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>On Exit</em>' reference.
   * @see #getOnExit()
   * @generated
   */
  void setOnExit(Action value);

} // CompositeState
