/**
 */
package repast.simphony.statecharts.scmodel;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Action</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link repast.simphony.statecharts.scmodel.Action#getCode <em>Code</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.Action#getLanguage <em>Language</em>}</li>
 * </ul>
 * </p>
 *
 * @see repast.simphony.statecharts.scmodel.StatechartPackage#getAction()
 * @model
 * @generated
 */
public interface Action extends EObject {
  /**
   * Returns the value of the '<em><b>Code</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Code</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Code</em>' attribute.
   * @see #setCode(String)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getAction_Code()
   * @model
   * @generated
   */
  String getCode();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.Action#getCode <em>Code</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Code</em>' attribute.
   * @see #getCode()
   * @generated
   */
  void setCode(String value);

  /**
   * Returns the value of the '<em><b>Language</b></em>' attribute.
   * The literals are from the enumeration {@link repast.simphony.statecharts.scmodel.LanguageTypes}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Language</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Language</em>' attribute.
   * @see repast.simphony.statecharts.scmodel.LanguageTypes
   * @see #setLanguage(LanguageTypes)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getAction_Language()
   * @model
   * @generated
   */
  LanguageTypes getLanguage();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.Action#getLanguage <em>Language</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Language</em>' attribute.
   * @see repast.simphony.statecharts.scmodel.LanguageTypes
   * @see #getLanguage()
   * @generated
   */
  void setLanguage(LanguageTypes value);

} // Action
