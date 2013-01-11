/**
 */
package repast.simphony.systemdynamics.sdmodel;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Stock</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link repast.simphony.systemdynamics.sdmodel.Stock#getInitialValue <em>Initial Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see repast.simphony.systemdynamics.sdmodel.SDModelPackage#getStock()
 * @model
 * @generated
 */
public interface Stock extends Variable {
  /**
   * Returns the value of the '<em><b>Initial Value</b></em>' attribute.
   * The default value is <code>"0"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Initial Value</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Initial Value</em>' attribute.
   * @see #setInitialValue(double)
   * @see repast.simphony.systemdynamics.sdmodel.SDModelPackage#getStock_InitialValue()
   * @model default="0"
   * @generated
   */
  double getInitialValue();

  /**
   * Sets the value of the '{@link repast.simphony.systemdynamics.sdmodel.Stock#getInitialValue <em>Initial Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Initial Value</em>' attribute.
   * @see #getInitialValue()
   * @generated
   */
  void setInitialValue(double value);

} // Stock
