/**
 */
package repast.simphony.systemdynamics.sdmodel;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Variable</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link repast.simphony.systemdynamics.sdmodel.Variable#getUuid <em>Uuid</em>}</li>
 *   <li>{@link repast.simphony.systemdynamics.sdmodel.Variable#getName <em>Name</em>}</li>
 *   <li>{@link repast.simphony.systemdynamics.sdmodel.Variable#getType <em>Type</em>}</li>
 *   <li>{@link repast.simphony.systemdynamics.sdmodel.Variable#getUnits <em>Units</em>}</li>
 *   <li>{@link repast.simphony.systemdynamics.sdmodel.Variable#getEquation <em>Equation</em>}</li>
 *   <li>{@link repast.simphony.systemdynamics.sdmodel.Variable#getComment <em>Comment</em>}</li>
 * </ul>
 * </p>
 *
 * @see repast.simphony.systemdynamics.sdmodel.SDModelPackage#getVariable()
 * @model
 * @generated
 */
public interface Variable extends EObject {
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
   * @see repast.simphony.systemdynamics.sdmodel.SDModelPackage#getVariable_Uuid()
   * @model
   * @generated
   */
  String getUuid();

  /**
   * Sets the value of the '{@link repast.simphony.systemdynamics.sdmodel.Variable#getUuid <em>Uuid</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Uuid</em>' attribute.
   * @see #getUuid()
   * @generated
   */
  void setUuid(String value);

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
   * @see repast.simphony.systemdynamics.sdmodel.SDModelPackage#getVariable_Name()
   * @model
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link repast.simphony.systemdynamics.sdmodel.Variable#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>Type</b></em>' attribute.
   * The default value is <code>""</code>.
   * The literals are from the enumeration {@link repast.simphony.systemdynamics.sdmodel.VariableType}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Type</em>' attribute.
   * @see repast.simphony.systemdynamics.sdmodel.VariableType
   * @see #setType(VariableType)
   * @see repast.simphony.systemdynamics.sdmodel.SDModelPackage#getVariable_Type()
   * @model default=""
   * @generated
   */
  VariableType getType();

  /**
   * Sets the value of the '{@link repast.simphony.systemdynamics.sdmodel.Variable#getType <em>Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Type</em>' attribute.
   * @see repast.simphony.systemdynamics.sdmodel.VariableType
   * @see #getType()
   * @generated
   */
  void setType(VariableType value);

  /**
   * Returns the value of the '<em><b>Units</b></em>' attribute.
   * The default value is <code>""</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Units</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Units</em>' attribute.
   * @see #setUnits(String)
   * @see repast.simphony.systemdynamics.sdmodel.SDModelPackage#getVariable_Units()
   * @model default=""
   * @generated
   */
  String getUnits();

  /**
   * Sets the value of the '{@link repast.simphony.systemdynamics.sdmodel.Variable#getUnits <em>Units</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Units</em>' attribute.
   * @see #getUnits()
   * @generated
   */
  void setUnits(String value);

  /**
   * Returns the value of the '<em><b>Equation</b></em>' attribute.
   * The default value is <code>""</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Equation</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Equation</em>' attribute.
   * @see #setEquation(String)
   * @see repast.simphony.systemdynamics.sdmodel.SDModelPackage#getVariable_Equation()
   * @model default=""
   * @generated
   */
  String getEquation();

  /**
   * Sets the value of the '{@link repast.simphony.systemdynamics.sdmodel.Variable#getEquation <em>Equation</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Equation</em>' attribute.
   * @see #getEquation()
   * @generated
   */
  void setEquation(String value);

  /**
   * Returns the value of the '<em><b>Comment</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Comment</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Comment</em>' attribute.
   * @see #setComment(String)
   * @see repast.simphony.systemdynamics.sdmodel.SDModelPackage#getVariable_Comment()
   * @model
   * @generated
   */
  String getComment();

  /**
   * Sets the value of the '{@link repast.simphony.systemdynamics.sdmodel.Variable#getComment <em>Comment</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Comment</em>' attribute.
   * @see #getComment()
   * @generated
   */
  void setComment(String value);

} // Variable
