/**
 */
package repast.simphony.statecharts.scmodel;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Message Checker</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link repast.simphony.statecharts.scmodel.MessageChecker#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see repast.simphony.statecharts.scmodel.StatechartPackage#getMessageChecker()
 * @model
 * @generated
 */
public interface MessageChecker extends EObject {
  /**
   * Returns the value of the '<em><b>Type</b></em>' attribute.
   * The literals are from the enumeration {@link repast.simphony.statecharts.scmodel.MessageCheckerTypes}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Type</em>' attribute.
   * @see repast.simphony.statecharts.scmodel.MessageCheckerTypes
   * @see #setType(MessageCheckerTypes)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getMessageChecker_Type()
   * @model
   * @generated
   */
  MessageCheckerTypes getType();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.MessageChecker#getType <em>Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Type</em>' attribute.
   * @see repast.simphony.statecharts.scmodel.MessageCheckerTypes
   * @see #getType()
   * @generated
   */
  void setType(MessageCheckerTypes value);

} // MessageChecker
