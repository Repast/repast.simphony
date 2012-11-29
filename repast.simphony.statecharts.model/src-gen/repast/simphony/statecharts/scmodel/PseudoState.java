/**
 */
package repast.simphony.statecharts.scmodel;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Pseudo State</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link repast.simphony.statecharts.scmodel.PseudoState#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see repast.simphony.statecharts.scmodel.StatechartPackage#getPseudoState()
 * @model
 * @generated
 */
public interface PseudoState extends AbstractState {
  /**
   * Returns the value of the '<em><b>Type</b></em>' attribute.
   * The literals are from the enumeration {@link repast.simphony.statecharts.scmodel.PseudoStateTypes}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Type</em>' attribute.
   * @see repast.simphony.statecharts.scmodel.PseudoStateTypes
   * @see #setType(PseudoStateTypes)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getPseudoState_Type()
   * @model
   * @generated
   */
  PseudoStateTypes getType();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.PseudoState#getType <em>Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Type</em>' attribute.
   * @see repast.simphony.statecharts.scmodel.PseudoStateTypes
   * @see #getType()
   * @generated
   */
  void setType(PseudoStateTypes value);

} // PseudoState
