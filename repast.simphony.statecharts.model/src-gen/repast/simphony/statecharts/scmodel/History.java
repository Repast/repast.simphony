/**
 */
package repast.simphony.statecharts.scmodel;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>History</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link repast.simphony.statecharts.scmodel.History#isShallow <em>Shallow</em>}</li>
 * </ul>
 * </p>
 *
 * @see repast.simphony.statecharts.scmodel.StatechartPackage#getHistory()
 * @model
 * @generated
 */
public interface History extends State {
  /**
   * Returns the value of the '<em><b>Shallow</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Shallow</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Shallow</em>' attribute.
   * @see #setShallow(boolean)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getHistory_Shallow()
   * @model
   * @generated
   */
  boolean isShallow();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.History#isShallow <em>Shallow</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Shallow</em>' attribute.
   * @see #isShallow()
   * @generated
   */
  void setShallow(boolean value);

} // History
