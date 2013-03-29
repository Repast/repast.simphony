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

} // CompositeState
