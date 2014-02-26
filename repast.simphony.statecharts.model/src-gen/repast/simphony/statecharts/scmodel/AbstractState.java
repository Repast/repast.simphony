/**
 */
package repast.simphony.statecharts.scmodel;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Abstract State</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link repast.simphony.statecharts.scmodel.AbstractState#getId <em>Id</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.AbstractState#getOnEnter <em>On Enter</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.AbstractState#getOnExit <em>On Exit</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.AbstractState#getLanguage <em>Language</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.AbstractState#getUuid <em>Uuid</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.AbstractState#getOnEnterImports <em>On Enter Imports</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.AbstractState#getOnExitImports <em>On Exit Imports</em>}</li>
 * </ul>
 * </p>
 *
 * @see repast.simphony.statecharts.scmodel.StatechartPackage#getAbstractState()
 * @model abstract="true"
 * @generated
 */
public interface AbstractState extends EObject {
  /**
   * Returns the value of the '<em><b>Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Id</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Id</em>' attribute.
   * @see #setId(String)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getAbstractState_Id()
   * @model
   * @generated
   */
  String getId();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.AbstractState#getId <em>Id</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Id</em>' attribute.
   * @see #getId()
   * @generated
   */
  void setId(String value);

  /**
   * Returns the value of the '<em><b>On Enter</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>On Enter</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>On Enter</em>' attribute.
   * @see #setOnEnter(String)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getAbstractState_OnEnter()
   * @model
   * @generated
   */
  String getOnEnter();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.AbstractState#getOnEnter <em>On Enter</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>On Enter</em>' attribute.
   * @see #getOnEnter()
   * @generated
   */
  void setOnEnter(String value);

  /**
   * Returns the value of the '<em><b>On Exit</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>On Exit</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>On Exit</em>' attribute.
   * @see #setOnExit(String)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getAbstractState_OnExit()
   * @model
   * @generated
   */
  String getOnExit();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.AbstractState#getOnExit <em>On Exit</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>On Exit</em>' attribute.
   * @see #getOnExit()
   * @generated
   */
  void setOnExit(String value);

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
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getAbstractState_Language()
   * @model
   * @generated
   */
  LanguageTypes getLanguage();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.AbstractState#getLanguage <em>Language</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Language</em>' attribute.
   * @see repast.simphony.statecharts.scmodel.LanguageTypes
   * @see #getLanguage()
   * @generated
   */
  void setLanguage(LanguageTypes value);

  /**
   * Returns the value of the '<em><b>Uuid</b></em>' attribute.
   * The default value is <code>""</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Uuid</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Uuid</em>' attribute.
   * @see #setUuid(String)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getAbstractState_Uuid()
   * @model default=""
   * @generated
   */
  String getUuid();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.AbstractState#getUuid <em>Uuid</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Uuid</em>' attribute.
   * @see #getUuid()
   * @generated
   */
  void setUuid(String value);

  /**
   * Returns the value of the '<em><b>On Enter Imports</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>On Enter Imports</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>On Enter Imports</em>' attribute.
   * @see #setOnEnterImports(String)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getAbstractState_OnEnterImports()
   * @model
   * @generated
   */
  String getOnEnterImports();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.AbstractState#getOnEnterImports <em>On Enter Imports</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>On Enter Imports</em>' attribute.
   * @see #getOnEnterImports()
   * @generated
   */
  void setOnEnterImports(String value);

  /**
   * Returns the value of the '<em><b>On Exit Imports</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>On Exit Imports</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>On Exit Imports</em>' attribute.
   * @see #setOnExitImports(String)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getAbstractState_OnExitImports()
   * @model
   * @generated
   */
  String getOnExitImports();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.AbstractState#getOnExitImports <em>On Exit Imports</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>On Exit Imports</em>' attribute.
   * @see #getOnExitImports()
   * @generated
   */
  void setOnExitImports(String value);

} // AbstractState
