/**
 */
package repast.simphony.statecharts.scmodel.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import repast.simphony.statecharts.scmodel.AbstractState;
import repast.simphony.statecharts.scmodel.PseudoState;
import repast.simphony.statecharts.scmodel.PseudoStateTypes;
import repast.simphony.statecharts.scmodel.StatechartPackage;
import repast.simphony.statecharts.scmodel.Transition;
import repast.simphony.statecharts.scmodel.Trigger;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Transition</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link repast.simphony.statecharts.scmodel.impl.TransitionImpl#getFrom <em>From</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.impl.TransitionImpl#getTo <em>To</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.impl.TransitionImpl#getPriority <em>Priority</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.impl.TransitionImpl#getOnTransition <em>On Transition</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.impl.TransitionImpl#isOutOfBranch <em>Out Of Branch</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.impl.TransitionImpl#isDefaultTransition <em>Default Transition</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.impl.TransitionImpl#getTrigger <em>Trigger</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TransitionImpl extends EObjectImpl implements Transition {
  /**
   * The cached value of the '{@link #getFrom() <em>From</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFrom()
   * @generated
   * @ordered
   */
  protected AbstractState from;

  /**
   * The cached value of the '{@link #getTo() <em>To</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTo()
   * @generated
   * @ordered
   */
  protected AbstractState to;

  /**
   * The default value of the '{@link #getPriority() <em>Priority</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPriority()
   * @generated
   * @ordered
   */
  protected static final int PRIORITY_EDEFAULT = 0;

  /**
   * The cached value of the '{@link #getPriority() <em>Priority</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPriority()
   * @generated
   * @ordered
   */
  protected int priority = PRIORITY_EDEFAULT;

  /**
   * The default value of the '{@link #getOnTransition() <em>On Transition</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOnTransition()
   * @generated
   * @ordered
   */
  protected static final String ON_TRANSITION_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getOnTransition() <em>On Transition</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOnTransition()
   * @generated
   * @ordered
   */
  protected String onTransition = ON_TRANSITION_EDEFAULT;

  /**
   * The default value of the '{@link #isOutOfBranch() <em>Out Of Branch</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isOutOfBranch()
   * @generated
   * @ordered
   */
  protected static final boolean OUT_OF_BRANCH_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isOutOfBranch() <em>Out Of Branch</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isOutOfBranch()
   * @generated
   * @ordered
   */
  protected boolean outOfBranch = OUT_OF_BRANCH_EDEFAULT;

  /**
   * The default value of the '{@link #isDefaultTransition() <em>Default Transition</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isDefaultTransition()
   * @generated
   * @ordered
   */
  protected static final boolean DEFAULT_TRANSITION_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isDefaultTransition() <em>Default Transition</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isDefaultTransition()
   * @generated
   * @ordered
   */
  protected boolean defaultTransition = DEFAULT_TRANSITION_EDEFAULT;

  /**
   * The cached value of the '{@link #getTrigger() <em>Trigger</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTrigger()
   * @generated
   * @ordered
   */
  protected Trigger trigger;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected TransitionImpl() {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass() {
    return StatechartPackage.Literals.TRANSITION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public AbstractState getFrom() {
    if (from != null && from.eIsProxy()) {
      InternalEObject oldFrom = (InternalEObject)from;
      from = (AbstractState)eResolveProxy(oldFrom);
      if (from != oldFrom) {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, StatechartPackage.TRANSITION__FROM, oldFrom, from));
      }
    }
    return from;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public AbstractState basicGetFrom() {
    return from;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void setFrom(AbstractState newFrom) {
    AbstractState oldFrom = from;
    from = newFrom;
    if (from instanceof PseudoState && ((PseudoState)from).getType() == PseudoStateTypes.CHOICE) {
      setOutOfBranch(true);
      setDefaultTransition(true);
    } else {
      setOutOfBranch(false);
    }
    
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StatechartPackage.TRANSITION__FROM, oldFrom, from));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public AbstractState getTo() {
    if (to != null && to.eIsProxy()) {
      InternalEObject oldTo = (InternalEObject)to;
      to = (AbstractState)eResolveProxy(oldTo);
      if (to != oldTo) {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, StatechartPackage.TRANSITION__TO, oldTo, to));
      }
    }
    return to;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public AbstractState basicGetTo() {
    return to;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setTo(AbstractState newTo) {
    AbstractState oldTo = to;
    to = newTo;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StatechartPackage.TRANSITION__TO, oldTo, to));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public int getPriority() {
    return priority;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setPriority(int newPriority) {
    int oldPriority = priority;
    priority = newPriority;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StatechartPackage.TRANSITION__PRIORITY, oldPriority, priority));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getOnTransition() {
    return onTransition;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setOnTransition(String newOnTransition) {
    String oldOnTransition = onTransition;
    onTransition = newOnTransition;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StatechartPackage.TRANSITION__ON_TRANSITION, oldOnTransition, onTransition));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isOutOfBranch() {
    return outOfBranch;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setOutOfBranch(boolean newOutOfBranch) {
    boolean oldOutOfBranch = outOfBranch;
    outOfBranch = newOutOfBranch;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StatechartPackage.TRANSITION__OUT_OF_BRANCH, oldOutOfBranch, outOfBranch));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isDefaultTransition() {
    return defaultTransition;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setDefaultTransition(boolean newDefaultTransition) {
    boolean oldDefaultTransition = defaultTransition;
    defaultTransition = newDefaultTransition;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StatechartPackage.TRANSITION__DEFAULT_TRANSITION, oldDefaultTransition, defaultTransition));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Trigger getTrigger() {
    if (trigger != null && trigger.eIsProxy()) {
      InternalEObject oldTrigger = (InternalEObject)trigger;
      trigger = (Trigger)eResolveProxy(oldTrigger);
      if (trigger != oldTrigger) {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, StatechartPackage.TRANSITION__TRIGGER, oldTrigger, trigger));
      }
    }
    return trigger;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Trigger basicGetTrigger() {
    return trigger;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setTrigger(Trigger newTrigger) {
    Trigger oldTrigger = trigger;
    trigger = newTrigger;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StatechartPackage.TRANSITION__TRIGGER, oldTrigger, trigger));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType) {
    switch (featureID) {
      case StatechartPackage.TRANSITION__FROM:
        if (resolve) return getFrom();
        return basicGetFrom();
      case StatechartPackage.TRANSITION__TO:
        if (resolve) return getTo();
        return basicGetTo();
      case StatechartPackage.TRANSITION__PRIORITY:
        return getPriority();
      case StatechartPackage.TRANSITION__ON_TRANSITION:
        return getOnTransition();
      case StatechartPackage.TRANSITION__OUT_OF_BRANCH:
        return isOutOfBranch();
      case StatechartPackage.TRANSITION__DEFAULT_TRANSITION:
        return isDefaultTransition();
      case StatechartPackage.TRANSITION__TRIGGER:
        if (resolve) return getTrigger();
        return basicGetTrigger();
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
      case StatechartPackage.TRANSITION__FROM:
        setFrom((AbstractState)newValue);
        return;
      case StatechartPackage.TRANSITION__TO:
        setTo((AbstractState)newValue);
        return;
      case StatechartPackage.TRANSITION__PRIORITY:
        setPriority((Integer)newValue);
        return;
      case StatechartPackage.TRANSITION__ON_TRANSITION:
        setOnTransition((String)newValue);
        return;
      case StatechartPackage.TRANSITION__OUT_OF_BRANCH:
        setOutOfBranch((Boolean)newValue);
        return;
      case StatechartPackage.TRANSITION__DEFAULT_TRANSITION:
        setDefaultTransition((Boolean)newValue);
        return;
      case StatechartPackage.TRANSITION__TRIGGER:
        setTrigger((Trigger)newValue);
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
      case StatechartPackage.TRANSITION__FROM:
        setFrom((AbstractState)null);
        return;
      case StatechartPackage.TRANSITION__TO:
        setTo((AbstractState)null);
        return;
      case StatechartPackage.TRANSITION__PRIORITY:
        setPriority(PRIORITY_EDEFAULT);
        return;
      case StatechartPackage.TRANSITION__ON_TRANSITION:
        setOnTransition(ON_TRANSITION_EDEFAULT);
        return;
      case StatechartPackage.TRANSITION__OUT_OF_BRANCH:
        setOutOfBranch(OUT_OF_BRANCH_EDEFAULT);
        return;
      case StatechartPackage.TRANSITION__DEFAULT_TRANSITION:
        setDefaultTransition(DEFAULT_TRANSITION_EDEFAULT);
        return;
      case StatechartPackage.TRANSITION__TRIGGER:
        setTrigger((Trigger)null);
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
      case StatechartPackage.TRANSITION__FROM:
        return from != null;
      case StatechartPackage.TRANSITION__TO:
        return to != null;
      case StatechartPackage.TRANSITION__PRIORITY:
        return priority != PRIORITY_EDEFAULT;
      case StatechartPackage.TRANSITION__ON_TRANSITION:
        return ON_TRANSITION_EDEFAULT == null ? onTransition != null : !ON_TRANSITION_EDEFAULT.equals(onTransition);
      case StatechartPackage.TRANSITION__OUT_OF_BRANCH:
        return outOfBranch != OUT_OF_BRANCH_EDEFAULT;
      case StatechartPackage.TRANSITION__DEFAULT_TRANSITION:
        return defaultTransition != DEFAULT_TRANSITION_EDEFAULT;
      case StatechartPackage.TRANSITION__TRIGGER:
        return trigger != null;
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
    result.append(" (priority: ");
    result.append(priority);
    result.append(", onTransition: ");
    result.append(onTransition);
    result.append(", outOfBranch: ");
    result.append(outOfBranch);
    result.append(", defaultTransition: ");
    result.append(defaultTransition);
    result.append(')');
    return result.toString();
  }

} //TransitionImpl
