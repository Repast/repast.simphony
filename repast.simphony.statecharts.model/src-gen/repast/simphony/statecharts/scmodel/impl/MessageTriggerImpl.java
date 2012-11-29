/**
 */
package repast.simphony.statecharts.scmodel.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import repast.simphony.statecharts.scmodel.MessageChecker;
import repast.simphony.statecharts.scmodel.MessageTrigger;
import repast.simphony.statecharts.scmodel.StatechartPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Message Trigger</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link repast.simphony.statecharts.scmodel.impl.MessageTriggerImpl#getChecker <em>Checker</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MessageTriggerImpl extends DefaultTriggerImpl implements MessageTrigger {
  /**
   * The cached value of the '{@link #getChecker() <em>Checker</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getChecker()
   * @generated
   * @ordered
   */
  protected MessageChecker checker;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected MessageTriggerImpl() {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass() {
    return StatechartPackage.Literals.MESSAGE_TRIGGER;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MessageChecker getChecker() {
    if (checker != null && checker.eIsProxy()) {
      InternalEObject oldChecker = (InternalEObject)checker;
      checker = (MessageChecker)eResolveProxy(oldChecker);
      if (checker != oldChecker) {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, StatechartPackage.MESSAGE_TRIGGER__CHECKER, oldChecker, checker));
      }
    }
    return checker;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MessageChecker basicGetChecker() {
    return checker;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setChecker(MessageChecker newChecker) {
    MessageChecker oldChecker = checker;
    checker = newChecker;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StatechartPackage.MESSAGE_TRIGGER__CHECKER, oldChecker, checker));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType) {
    switch (featureID) {
      case StatechartPackage.MESSAGE_TRIGGER__CHECKER:
        if (resolve) return getChecker();
        return basicGetChecker();
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
      case StatechartPackage.MESSAGE_TRIGGER__CHECKER:
        setChecker((MessageChecker)newValue);
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
      case StatechartPackage.MESSAGE_TRIGGER__CHECKER:
        setChecker((MessageChecker)null);
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
      case StatechartPackage.MESSAGE_TRIGGER__CHECKER:
        return checker != null;
    }
    return super.eIsSet(featureID);
  }

} //MessageTriggerImpl
