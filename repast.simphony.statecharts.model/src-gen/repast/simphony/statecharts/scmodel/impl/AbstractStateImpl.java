/**
 */
package repast.simphony.statecharts.scmodel.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import repast.simphony.statecharts.scmodel.AbstractState;
import repast.simphony.statecharts.scmodel.LanguageTypes;
import repast.simphony.statecharts.scmodel.StatechartPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Abstract State</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link repast.simphony.statecharts.scmodel.impl.AbstractStateImpl#getId <em>Id</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.impl.AbstractStateImpl#getOnEnter <em>On Enter</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.impl.AbstractStateImpl#getOnExit <em>On Exit</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.impl.AbstractStateImpl#getLanguage <em>Language</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.impl.AbstractStateImpl#getUuid <em>Uuid</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class AbstractStateImpl extends EObjectImpl implements AbstractState {
  /**
   * The default value of the '{@link #getId() <em>Id</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getId()
   * @generated
   * @ordered
   */
  protected static final String ID_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getId() <em>Id</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getId()
   * @generated
   * @ordered
   */
  protected String id = ID_EDEFAULT;

  /**
   * The default value of the '{@link #getOnEnter() <em>On Enter</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOnEnter()
   * @generated
   * @ordered
   */
  protected static final String ON_ENTER_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getOnEnter() <em>On Enter</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOnEnter()
   * @generated
   * @ordered
   */
  protected String onEnter = ON_ENTER_EDEFAULT;

  /**
   * The default value of the '{@link #getOnExit() <em>On Exit</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOnExit()
   * @generated
   * @ordered
   */
  protected static final String ON_EXIT_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getOnExit() <em>On Exit</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOnExit()
   * @generated
   * @ordered
   */
  protected String onExit = ON_EXIT_EDEFAULT;

  /**
   * The default value of the '{@link #getLanguage() <em>Language</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLanguage()
   * @generated
   * @ordered
   */
  protected static final LanguageTypes LANGUAGE_EDEFAULT = LanguageTypes.JAVA;

  /**
   * The cached value of the '{@link #getLanguage() <em>Language</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLanguage()
   * @generated
   * @ordered
   */
  protected LanguageTypes language = LANGUAGE_EDEFAULT;

  /**
   * The default value of the '{@link #getUuid() <em>Uuid</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getUuid()
   * @generated
   * @ordered
   */
  protected static final String UUID_EDEFAULT = "";

  /**
   * The cached value of the '{@link #getUuid() <em>Uuid</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getUuid()
   * @generated
   * @ordered
   */
  protected String uuid = UUID_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected AbstractStateImpl() {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass() {
    return StatechartPackage.Literals.ABSTRACT_STATE;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getId() {
    return id;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setId(String newId) {
    String oldId = id;
    id = newId;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StatechartPackage.ABSTRACT_STATE__ID, oldId, id));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getOnEnter() {
    return onEnter;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setOnEnter(String newOnEnter) {
    String oldOnEnter = onEnter;
    onEnter = newOnEnter;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StatechartPackage.ABSTRACT_STATE__ON_ENTER, oldOnEnter, onEnter));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getOnExit() {
    return onExit;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setOnExit(String newOnExit) {
    String oldOnExit = onExit;
    onExit = newOnExit;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StatechartPackage.ABSTRACT_STATE__ON_EXIT, oldOnExit, onExit));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public LanguageTypes getLanguage() {
    return language;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setLanguage(LanguageTypes newLanguage) {
    LanguageTypes oldLanguage = language;
    language = newLanguage == null ? LANGUAGE_EDEFAULT : newLanguage;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StatechartPackage.ABSTRACT_STATE__LANGUAGE, oldLanguage, language));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getUuid() {
    return uuid;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setUuid(String newUuid) {
    String oldUuid = uuid;
    uuid = newUuid;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StatechartPackage.ABSTRACT_STATE__UUID, oldUuid, uuid));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType) {
    switch (featureID) {
      case StatechartPackage.ABSTRACT_STATE__ID:
        return getId();
      case StatechartPackage.ABSTRACT_STATE__ON_ENTER:
        return getOnEnter();
      case StatechartPackage.ABSTRACT_STATE__ON_EXIT:
        return getOnExit();
      case StatechartPackage.ABSTRACT_STATE__LANGUAGE:
        return getLanguage();
      case StatechartPackage.ABSTRACT_STATE__UUID:
        return getUuid();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eSet(int featureID, Object newValue) {
    switch (featureID) {
      case StatechartPackage.ABSTRACT_STATE__ID:
        setId((String)newValue);
        return;
      case StatechartPackage.ABSTRACT_STATE__ON_ENTER:
        setOnEnter((String)newValue);
        return;
      case StatechartPackage.ABSTRACT_STATE__ON_EXIT:
        setOnExit((String)newValue);
        return;
      case StatechartPackage.ABSTRACT_STATE__LANGUAGE:
        setLanguage((LanguageTypes)newValue);
        return;
      case StatechartPackage.ABSTRACT_STATE__UUID:
        setUuid((String)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID) {
    switch (featureID) {
      case StatechartPackage.ABSTRACT_STATE__ID:
        setId(ID_EDEFAULT);
        return;
      case StatechartPackage.ABSTRACT_STATE__ON_ENTER:
        setOnEnter(ON_ENTER_EDEFAULT);
        return;
      case StatechartPackage.ABSTRACT_STATE__ON_EXIT:
        setOnExit(ON_EXIT_EDEFAULT);
        return;
      case StatechartPackage.ABSTRACT_STATE__LANGUAGE:
        setLanguage(LANGUAGE_EDEFAULT);
        return;
      case StatechartPackage.ABSTRACT_STATE__UUID:
        setUuid(UUID_EDEFAULT);
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID) {
    switch (featureID) {
      case StatechartPackage.ABSTRACT_STATE__ID:
        return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
      case StatechartPackage.ABSTRACT_STATE__ON_ENTER:
        return ON_ENTER_EDEFAULT == null ? onEnter != null : !ON_ENTER_EDEFAULT.equals(onEnter);
      case StatechartPackage.ABSTRACT_STATE__ON_EXIT:
        return ON_EXIT_EDEFAULT == null ? onExit != null : !ON_EXIT_EDEFAULT.equals(onExit);
      case StatechartPackage.ABSTRACT_STATE__LANGUAGE:
        return language != LANGUAGE_EDEFAULT;
      case StatechartPackage.ABSTRACT_STATE__UUID:
        return UUID_EDEFAULT == null ? uuid != null : !UUID_EDEFAULT.equals(uuid);
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString() {
    if (eIsProxy()) return super.toString();

    StringBuffer result = new StringBuffer(super.toString());
    result.append(" (id: ");
    result.append(id);
    result.append(", onEnter: ");
    result.append(onEnter);
    result.append(", onExit: ");
    result.append(onExit);
    result.append(", language: ");
    result.append(language);
    result.append(", uuid: ");
    result.append(uuid);
    result.append(')');
    return result.toString();
  }

} //AbstractStateImpl
