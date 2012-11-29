/**
 */
package repast.simphony.statecharts.scmodel.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import repast.simphony.statecharts.scmodel.Action;
import repast.simphony.statecharts.scmodel.State;
import repast.simphony.statecharts.scmodel.StatechartPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>State</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link repast.simphony.statecharts.scmodel.impl.StateImpl#getOnExit <em>On Exit</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.impl.StateImpl#getOnEnter <em>On Enter</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class StateImpl extends AbstractStateImpl implements State {
  /**
   * The cached value of the '{@link #getOnExit() <em>On Exit</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOnExit()
   * @generated
   * @ordered
   */
  protected Action onExit;

  /**
   * The cached value of the '{@link #getOnEnter() <em>On Enter</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOnEnter()
   * @generated
   * @ordered
   */
  protected Action onEnter;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected StateImpl() {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass() {
    return StatechartPackage.Literals.STATE;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Action getOnExit() {
    if (onExit != null && onExit.eIsProxy()) {
      InternalEObject oldOnExit = (InternalEObject)onExit;
      onExit = (Action)eResolveProxy(oldOnExit);
      if (onExit != oldOnExit) {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, StatechartPackage.STATE__ON_EXIT, oldOnExit, onExit));
      }
    }
    return onExit;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Action basicGetOnExit() {
    return onExit;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setOnExit(Action newOnExit) {
    Action oldOnExit = onExit;
    onExit = newOnExit;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StatechartPackage.STATE__ON_EXIT, oldOnExit, onExit));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Action getOnEnter() {
    if (onEnter != null && onEnter.eIsProxy()) {
      InternalEObject oldOnEnter = (InternalEObject)onEnter;
      onEnter = (Action)eResolveProxy(oldOnEnter);
      if (onEnter != oldOnEnter) {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, StatechartPackage.STATE__ON_ENTER, oldOnEnter, onEnter));
      }
    }
    return onEnter;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Action basicGetOnEnter() {
    return onEnter;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setOnEnter(Action newOnEnter) {
    Action oldOnEnter = onEnter;
    onEnter = newOnEnter;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StatechartPackage.STATE__ON_ENTER, oldOnEnter, onEnter));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType) {
    switch (featureID) {
      case StatechartPackage.STATE__ON_EXIT:
        if (resolve) return getOnExit();
        return basicGetOnExit();
      case StatechartPackage.STATE__ON_ENTER:
        if (resolve) return getOnEnter();
        return basicGetOnEnter();
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
      case StatechartPackage.STATE__ON_EXIT:
        setOnExit((Action)newValue);
        return;
      case StatechartPackage.STATE__ON_ENTER:
        setOnEnter((Action)newValue);
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
      case StatechartPackage.STATE__ON_EXIT:
        setOnExit((Action)null);
        return;
      case StatechartPackage.STATE__ON_ENTER:
        setOnEnter((Action)null);
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
      case StatechartPackage.STATE__ON_EXIT:
        return onExit != null;
      case StatechartPackage.STATE__ON_ENTER:
        return onEnter != null;
    }
    return super.eIsSet(featureID);
  }

} //StateImpl
