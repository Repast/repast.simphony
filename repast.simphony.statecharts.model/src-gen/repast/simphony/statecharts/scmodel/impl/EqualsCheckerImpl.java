/**
 */
package repast.simphony.statecharts.scmodel.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import repast.simphony.statecharts.scmodel.EqualsChecker;
import repast.simphony.statecharts.scmodel.StatechartPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Equals Checker</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link repast.simphony.statecharts.scmodel.impl.EqualsCheckerImpl#getClazz <em>Clazz</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.impl.EqualsCheckerImpl#getObj <em>Obj</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EqualsCheckerImpl extends MessageCheckerImpl implements EqualsChecker {
  /**
   * The default value of the '{@link #getClazz() <em>Clazz</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getClazz()
   * @generated
   * @ordered
   */
  protected static final String CLAZZ_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getClazz() <em>Clazz</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getClazz()
   * @generated
   * @ordered
   */
  protected String clazz = CLAZZ_EDEFAULT;

  /**
   * The default value of the '{@link #getObj() <em>Obj</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getObj()
   * @generated
   * @ordered
   */
  protected static final String OBJ_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getObj() <em>Obj</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getObj()
   * @generated
   * @ordered
   */
  protected String obj = OBJ_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected EqualsCheckerImpl() {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass() {
    return StatechartPackage.Literals.EQUALS_CHECKER;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getClazz() {
    return clazz;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setClazz(String newClazz) {
    String oldClazz = clazz;
    clazz = newClazz;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StatechartPackage.EQUALS_CHECKER__CLAZZ, oldClazz, clazz));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getObj() {
    return obj;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setObj(String newObj) {
    String oldObj = obj;
    obj = newObj;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StatechartPackage.EQUALS_CHECKER__OBJ, oldObj, obj));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType) {
    switch (featureID) {
      case StatechartPackage.EQUALS_CHECKER__CLAZZ:
        return getClazz();
      case StatechartPackage.EQUALS_CHECKER__OBJ:
        return getObj();
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
      case StatechartPackage.EQUALS_CHECKER__CLAZZ:
        setClazz((String)newValue);
        return;
      case StatechartPackage.EQUALS_CHECKER__OBJ:
        setObj((String)newValue);
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
      case StatechartPackage.EQUALS_CHECKER__CLAZZ:
        setClazz(CLAZZ_EDEFAULT);
        return;
      case StatechartPackage.EQUALS_CHECKER__OBJ:
        setObj(OBJ_EDEFAULT);
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
      case StatechartPackage.EQUALS_CHECKER__CLAZZ:
        return CLAZZ_EDEFAULT == null ? clazz != null : !CLAZZ_EDEFAULT.equals(clazz);
      case StatechartPackage.EQUALS_CHECKER__OBJ:
        return OBJ_EDEFAULT == null ? obj != null : !OBJ_EDEFAULT.equals(obj);
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
    result.append(" (clazz: ");
    result.append(clazz);
    result.append(", obj: ");
    result.append(obj);
    result.append(')');
    return result.toString();
  }

} //EqualsCheckerImpl
