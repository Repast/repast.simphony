/**
 */
package repast.simphony.systemdynamics.sdmodel;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Influence Link</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link repast.simphony.systemdynamics.sdmodel.InfluenceLink#getUuid <em>Uuid</em>}</li>
 *   <li>{@link repast.simphony.systemdynamics.sdmodel.InfluenceLink#getFrom <em>From</em>}</li>
 *   <li>{@link repast.simphony.systemdynamics.sdmodel.InfluenceLink#getTo <em>To</em>}</li>
 * </ul>
 * </p>
 *
 * @see repast.simphony.systemdynamics.sdmodel.SDModelPackage#getInfluenceLink()
 * @model
 * @generated
 */
public interface InfluenceLink extends EObject {
  /**
   * Returns the value of the '<em><b>Uuid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Uuid</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Uuid</em>' attribute.
   * @see #setUuid(String)
   * @see repast.simphony.systemdynamics.sdmodel.SDModelPackage#getInfluenceLink_Uuid()
   * @model
   * @generated
   */
  String getUuid();

  /**
   * Sets the value of the '{@link repast.simphony.systemdynamics.sdmodel.InfluenceLink#getUuid <em>Uuid</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Uuid</em>' attribute.
   * @see #getUuid()
   * @generated
   */
  void setUuid(String value);

  /**
   * Returns the value of the '<em><b>From</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>From</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>From</em>' reference.
   * @see #setFrom(Variable)
   * @see repast.simphony.systemdynamics.sdmodel.SDModelPackage#getInfluenceLink_From()
   * @model required="true"
   * @generated
   */
  Variable getFrom();

  /**
   * Sets the value of the '{@link repast.simphony.systemdynamics.sdmodel.InfluenceLink#getFrom <em>From</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>From</em>' reference.
   * @see #getFrom()
   * @generated
   */
  void setFrom(Variable value);

  /**
   * Returns the value of the '<em><b>To</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>To</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>To</em>' reference.
   * @see #setTo(Variable)
   * @see repast.simphony.systemdynamics.sdmodel.SDModelPackage#getInfluenceLink_To()
   * @model required="true"
   * @generated
   */
  Variable getTo();

  /**
   * Sets the value of the '{@link repast.simphony.systemdynamics.sdmodel.InfluenceLink#getTo <em>To</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>To</em>' reference.
   * @see #getTo()
   * @generated
   */
  void setTo(Variable value);

} // InfluenceLink
