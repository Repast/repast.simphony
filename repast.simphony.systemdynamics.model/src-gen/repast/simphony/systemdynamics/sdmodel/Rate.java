/**
 */
package repast.simphony.systemdynamics.sdmodel;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Rate</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link repast.simphony.systemdynamics.sdmodel.Rate#getTo <em>To</em>}</li>
 *   <li>{@link repast.simphony.systemdynamics.sdmodel.Rate#getFrom <em>From</em>}</li>
 * </ul>
 * </p>
 *
 * @see repast.simphony.systemdynamics.sdmodel.SDModelPackage#getRate()
 * @model
 * @generated
 */
public interface Rate extends Variable {
  /**
   * Returns the value of the '<em><b>To</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>To</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>To</em>' reference.
   * @see #setTo(Stock)
   * @see repast.simphony.systemdynamics.sdmodel.SDModelPackage#getRate_To()
   * @model required="true"
   * @generated
   */
  Stock getTo();

  /**
   * Sets the value of the '{@link repast.simphony.systemdynamics.sdmodel.Rate#getTo <em>To</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>To</em>' reference.
   * @see #getTo()
   * @generated
   */
  void setTo(Stock value);

  /**
   * Returns the value of the '<em><b>From</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>From</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>From</em>' reference.
   * @see #setFrom(Stock)
   * @see repast.simphony.systemdynamics.sdmodel.SDModelPackage#getRate_From()
   * @model required="true"
   * @generated
   */
  Stock getFrom();

  /**
   * Sets the value of the '{@link repast.simphony.systemdynamics.sdmodel.Rate#getFrom <em>From</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>From</em>' reference.
   * @see #getFrom()
   * @generated
   */
  void setFrom(Stock value);

} // Rate
