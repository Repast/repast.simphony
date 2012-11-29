/**
 */
package repast.simphony.statecharts.scmodel;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Trigger</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link repast.simphony.statecharts.scmodel.Trigger#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see repast.simphony.statecharts.scmodel.StatechartPackage#getTrigger()
 * @model abstract="true"
 * @generated
 */
public interface Trigger extends EObject {
  /**
   * Returns the value of the '<em><b>Type</b></em>' attribute.
   * The literals are from the enumeration {@link repast.simphony.statecharts.scmodel.TriggerTypes}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Type</em>' attribute.
   * @see repast.simphony.statecharts.scmodel.TriggerTypes
   * @see #setType(TriggerTypes)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getTrigger_Type()
   * @model
   * @generated
   */
  TriggerTypes getType();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.Trigger#getType <em>Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Type</em>' attribute.
   * @see repast.simphony.statecharts.scmodel.TriggerTypes
   * @see #getType()
   * @generated
   */
  void setType(TriggerTypes value);

} // Trigger
