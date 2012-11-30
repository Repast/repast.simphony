/**
 */
package repast.simphony.statecharts.scmodel.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import repast.simphony.statecharts.scmodel.AbstractState;
import repast.simphony.statecharts.scmodel.Action;
import repast.simphony.statecharts.scmodel.CompositeState;
import repast.simphony.statecharts.scmodel.StatechartPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Composite State</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link repast.simphony.statecharts.scmodel.impl.CompositeStateImpl#getChildren <em>Children</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.impl.CompositeStateImpl#getOnEnter <em>On Enter</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.impl.CompositeStateImpl#getOnExit <em>On Exit</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CompositeStateImpl extends AbstractStateImpl implements CompositeState {
  /**
   * The cached value of the '{@link #getChildren() <em>Children</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getChildren()
   * @generated
   * @ordered
   */
  protected EList<AbstractState> children;

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
   * The cached value of the '{@link #getOnExit() <em>On Exit</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOnExit()
   * @generated
   * @ordered
   */
  protected Action onExit;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected CompositeStateImpl() {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass() {
    return StatechartPackage.Literals.COMPOSITE_STATE;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<AbstractState> getChildren() {
    if (children == null) {
      children = new EObjectContainmentEList<AbstractState>(AbstractState.class, this, StatechartPackage.COMPOSITE_STATE__CHILDREN);
    }
    return children;
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
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, StatechartPackage.COMPOSITE_STATE__ON_ENTER, oldOnEnter, onEnter));
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
      eNotify(new ENotificationImpl(this, Notification.SET, StatechartPackage.COMPOSITE_STATE__ON_ENTER, oldOnEnter, onEnter));
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
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, StatechartPackage.COMPOSITE_STATE__ON_EXIT, oldOnExit, onExit));
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
      eNotify(new ENotificationImpl(this, Notification.SET, StatechartPackage.COMPOSITE_STATE__ON_EXIT, oldOnExit, onExit));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
    switch (featureID) {
      case StatechartPackage.COMPOSITE_STATE__CHILDREN:
        return ((InternalEList<?>)getChildren()).basicRemove(otherEnd, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType) {
    switch (featureID) {
      case StatechartPackage.COMPOSITE_STATE__CHILDREN:
        return getChildren();
      case StatechartPackage.COMPOSITE_STATE__ON_ENTER:
        if (resolve) return getOnEnter();
        return basicGetOnEnter();
      case StatechartPackage.COMPOSITE_STATE__ON_EXIT:
        if (resolve) return getOnExit();
        return basicGetOnExit();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public void eSet(int featureID, Object newValue) {
    switch (featureID) {
      case StatechartPackage.COMPOSITE_STATE__CHILDREN:
        getChildren().clear();
        getChildren().addAll((Collection<? extends AbstractState>)newValue);
        return;
      case StatechartPackage.COMPOSITE_STATE__ON_ENTER:
        setOnEnter((Action)newValue);
        return;
      case StatechartPackage.COMPOSITE_STATE__ON_EXIT:
        setOnExit((Action)newValue);
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
      case StatechartPackage.COMPOSITE_STATE__CHILDREN:
        getChildren().clear();
        return;
      case StatechartPackage.COMPOSITE_STATE__ON_ENTER:
        setOnEnter((Action)null);
        return;
      case StatechartPackage.COMPOSITE_STATE__ON_EXIT:
        setOnExit((Action)null);
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
      case StatechartPackage.COMPOSITE_STATE__CHILDREN:
        return children != null && !children.isEmpty();
      case StatechartPackage.COMPOSITE_STATE__ON_ENTER:
        return onEnter != null;
      case StatechartPackage.COMPOSITE_STATE__ON_EXIT:
        return onExit != null;
    }
    return super.eIsSet(featureID);
  }

} //CompositeStateImpl
