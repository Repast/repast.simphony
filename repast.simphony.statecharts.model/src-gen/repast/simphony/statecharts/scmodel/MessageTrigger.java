/**
 */
package repast.simphony.statecharts.scmodel;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Message Trigger</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link repast.simphony.statecharts.scmodel.MessageTrigger#getChecker <em>Checker</em>}</li>
 * </ul>
 * </p>
 *
 * @see repast.simphony.statecharts.scmodel.StatechartPackage#getMessageTrigger()
 * @model
 * @generated
 */
public interface MessageTrigger extends DefaultTrigger {
  /**
   * Returns the value of the '<em><b>Checker</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Checker</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Checker</em>' reference.
   * @see #setChecker(MessageChecker)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getMessageTrigger_Checker()
   * @model required="true"
   * @generated
   */
  MessageChecker getChecker();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.MessageTrigger#getChecker <em>Checker</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Checker</em>' reference.
   * @see #getChecker()
   * @generated
   */
  void setChecker(MessageChecker value);

} // MessageTrigger
