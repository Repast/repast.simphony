/**
 */
package repast.simphony.systemdynamics.sdmodel;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Subscript</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link repast.simphony.systemdynamics.sdmodel.Subscript#getName <em>Name</em>}</li>
 *   <li>{@link repast.simphony.systemdynamics.sdmodel.Subscript#getElements <em>Elements</em>}</li>
 * </ul>
 * </p>
 *
 * @see repast.simphony.systemdynamics.sdmodel.SDModelPackage#getSubscript()
 * @model
 * @generated
 */
public interface Subscript extends EObject {
  /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Name</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see repast.simphony.systemdynamics.sdmodel.SDModelPackage#getSubscript_Name()
   * @model
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link repast.simphony.systemdynamics.sdmodel.Subscript#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>Elements</b></em>' attribute list.
   * The list contents are of type {@link java.lang.String}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Elements</em>' attribute list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Elements</em>' attribute list.
   * @see repast.simphony.systemdynamics.sdmodel.SDModelPackage#getSubscript_Elements()
   * @model
   * @generated
   */
  EList<String> getElements();

} // Subscript
