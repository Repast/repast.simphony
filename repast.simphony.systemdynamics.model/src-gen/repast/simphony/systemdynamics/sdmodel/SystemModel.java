/**
 */
package repast.simphony.systemdynamics.sdmodel;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>System Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link repast.simphony.systemdynamics.sdmodel.SystemModel#getLinks <em>Links</em>}</li>
 *   <li>{@link repast.simphony.systemdynamics.sdmodel.SystemModel#getVariables <em>Variables</em>}</li>
 * </ul>
 * </p>
 *
 * @see repast.simphony.systemdynamics.sdmodel.SDModelPackage#getSystemModel()
 * @model
 * @generated
 */
public interface SystemModel extends EObject {
  /**
   * Returns the value of the '<em><b>Links</b></em>' containment reference list.
   * The list contents are of type {@link repast.simphony.systemdynamics.sdmodel.CausalLink}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Links</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Links</em>' containment reference list.
   * @see repast.simphony.systemdynamics.sdmodel.SDModelPackage#getSystemModel_Links()
   * @model containment="true"
   * @generated
   */
  EList<CausalLink> getLinks();

  /**
   * Returns the value of the '<em><b>Variables</b></em>' containment reference list.
   * The list contents are of type {@link repast.simphony.systemdynamics.sdmodel.AbstractVariable}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Variables</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Variables</em>' containment reference list.
   * @see repast.simphony.systemdynamics.sdmodel.SDModelPackage#getSystemModel_Variables()
   * @model containment="true"
   * @generated
   */
  EList<AbstractVariable> getVariables();

} // SystemModel
