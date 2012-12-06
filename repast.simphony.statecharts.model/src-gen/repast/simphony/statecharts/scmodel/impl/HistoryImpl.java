/**
 */
package repast.simphony.statecharts.scmodel.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import repast.simphony.statecharts.scmodel.History;
import repast.simphony.statecharts.scmodel.StatechartPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>History</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link repast.simphony.statecharts.scmodel.impl.HistoryImpl#isShallow <em>Shallow</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class HistoryImpl extends StateImpl implements History {

  private static final String DEEP_NAME = "Deep History";
  private static final String SHALLOW_NAME = "Shallow History";
  /**
   * The default value of the '{@link #isShallow() <em>Shallow</em>}' attribute.
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @see #isShallow()
   * @generated
   * @ordered
   */
  protected static final boolean SHALLOW_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isShallow() <em>Shallow</em>}' attribute.
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @see #isShallow()
   * @generated
   * @ordered
   */
  protected boolean shallow = SHALLOW_EDEFAULT;

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  protected HistoryImpl() {
    super();
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass() {
    return StatechartPackage.Literals.HISTORY;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public boolean isShallow() {
    return shallow;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated NOT
   */
  public void setShallow(boolean newShallow) {
    boolean oldShallow = shallow;
    shallow = newShallow;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StatechartPackage.HISTORY__SHALLOW,
          oldShallow, shallow));
    if (getId() != null) {
      if (shallow && getId().startsWith(DEEP_NAME)) {
        setId(SHALLOW_NAME + getId().substring(DEEP_NAME.length()));
      } else if (!shallow && getId().startsWith(SHALLOW_NAME)) {
        setId(DEEP_NAME + getId().substring(SHALLOW_NAME.length()));
      }
    }
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType) {
    switch (featureID) {
      case StatechartPackage.HISTORY__SHALLOW:
        return isShallow();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eSet(int featureID, Object newValue) {
    switch (featureID) {
      case StatechartPackage.HISTORY__SHALLOW:
        setShallow((Boolean)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID) {
    switch (featureID) {
      case StatechartPackage.HISTORY__SHALLOW:
        setShallow(SHALLOW_EDEFAULT);
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID) {
    switch (featureID) {
      case StatechartPackage.HISTORY__SHALLOW:
        return shallow != SHALLOW_EDEFAULT;
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString() {
    if (eIsProxy()) return super.toString();

    StringBuffer result = new StringBuffer(super.toString());
    result.append(" (shallow: ");
    result.append(shallow);
    result.append(')');
    return result.toString();
  }

} // HistoryImpl
