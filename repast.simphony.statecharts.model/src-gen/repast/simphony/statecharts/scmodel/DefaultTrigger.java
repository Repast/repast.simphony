/**
 */
package repast.simphony.statecharts.scmodel;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Default Trigger</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link repast.simphony.statecharts.scmodel.DefaultTrigger#getTime <em>Time</em>}</li>
 * </ul>
 * </p>
 *
 * @see repast.simphony.statecharts.scmodel.StatechartPackage#getDefaultTrigger()
 * @model
 * @generated
 */
public interface DefaultTrigger extends Trigger {
  /**
   * Returns the value of the '<em><b>Time</b></em>' attribute.
   * The default value is <code>"1.0"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Time</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Time</em>' attribute.
   * @see #setTime(double)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getDefaultTrigger_Time()
   * @model default="1.0"
   * @generated
   */
  double getTime();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.DefaultTrigger#getTime <em>Time</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Time</em>' attribute.
   * @see #getTime()
   * @generated
   */
  void setTime(double value);

} // DefaultTrigger
