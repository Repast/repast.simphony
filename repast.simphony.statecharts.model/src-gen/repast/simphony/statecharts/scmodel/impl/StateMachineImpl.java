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
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

import repast.simphony.statecharts.scmodel.AbstractState;
import repast.simphony.statecharts.scmodel.LanguageTypes;
import repast.simphony.statecharts.scmodel.StateMachine;
import repast.simphony.statecharts.scmodel.StatechartPackage;
import repast.simphony.statecharts.scmodel.Transition;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>State Machine</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link repast.simphony.statecharts.scmodel.impl.StateMachineImpl#getStates <em>States</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.impl.StateMachineImpl#getTransitions <em>Transitions</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.impl.StateMachineImpl#getAgentType <em>Agent Type</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.impl.StateMachineImpl#getPackage <em>Package</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.impl.StateMachineImpl#getClassName <em>Class Name</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.impl.StateMachineImpl#getLanguage <em>Language</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.impl.StateMachineImpl#getNextID <em>Next ID</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.impl.StateMachineImpl#getId <em>Id</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.impl.StateMachineImpl#getUuid <em>Uuid</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.impl.StateMachineImpl#getPriority <em>Priority</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class StateMachineImpl extends EObjectImpl implements StateMachine {
  /**
   * The cached value of the '{@link #getStates() <em>States</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getStates()
   * @generated
   * @ordered
   */
  protected EList<AbstractState> states;

  /**
   * The cached value of the '{@link #getTransitions() <em>Transitions</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTransitions()
   * @generated
   * @ordered
   */
  protected EList<Transition> transitions;

  /**
   * The default value of the '{@link #getAgentType() <em>Agent Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAgentType()
   * @generated
   * @ordered
   */
  protected static final String AGENT_TYPE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getAgentType() <em>Agent Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAgentType()
   * @generated
   * @ordered
   */
  protected String agentType = AGENT_TYPE_EDEFAULT;

  /**
   * The default value of the '{@link #getPackage() <em>Package</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPackage()
   * @generated
   * @ordered
   */
  protected static final String PACKAGE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getPackage() <em>Package</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPackage()
   * @generated
   * @ordered
   */
  protected String package_ = PACKAGE_EDEFAULT;

  /**
   * The default value of the '{@link #getClassName() <em>Class Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getClassName()
   * @generated
   * @ordered
   */
  protected static final String CLASS_NAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getClassName() <em>Class Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getClassName()
   * @generated
   * @ordered
   */
  protected String className = CLASS_NAME_EDEFAULT;

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
   * The default value of the '{@link #getNextID() <em>Next ID</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getNextID()
   * @generated
   * @ordered
   */
  protected static final int NEXT_ID_EDEFAULT = 0;

  /**
   * The cached value of the '{@link #getNextID() <em>Next ID</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getNextID()
   * @generated
   * @ordered
   */
  protected int nextID = NEXT_ID_EDEFAULT;

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
   * The default value of the '{@link #getUuid() <em>Uuid</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getUuid()
   * @generated NOT
   * @ordered
   */
  protected static final String UUID_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getUuid() <em>Uuid</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getUuid()
   * @generated NOT
   * @ordered
   */
  protected String uuid = EcoreUtil.generateUUID();

  /**
   * The default value of the '{@link #getPriority() <em>Priority</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPriority()
   * @generated
   * @ordered
   */
  protected static final double PRIORITY_EDEFAULT = 0.0;

  /**
   * The cached value of the '{@link #getPriority() <em>Priority</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPriority()
   * @generated
   * @ordered
   */
  protected double priority = PRIORITY_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected StateMachineImpl() {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass() {
    return StatechartPackage.Literals.STATE_MACHINE;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<AbstractState> getStates() {
    if (states == null) {
      states = new EObjectContainmentEList<AbstractState>(AbstractState.class, this, StatechartPackage.STATE_MACHINE__STATES);
    }
    return states;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<Transition> getTransitions() {
    if (transitions == null) {
      transitions = new EObjectContainmentEList<Transition>(Transition.class, this, StatechartPackage.STATE_MACHINE__TRANSITIONS);
    }
    return transitions;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getAgentType() {
    return agentType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setAgentType(String newAgentType) {
    String oldAgentType = agentType;
    agentType = newAgentType;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StatechartPackage.STATE_MACHINE__AGENT_TYPE, oldAgentType, agentType));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getPackage() {
    return package_;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setPackage(String newPackage) {
    String oldPackage = package_;
    package_ = newPackage;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StatechartPackage.STATE_MACHINE__PACKAGE, oldPackage, package_));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getClassName() {
    return className;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setClassName(String newClassName) {
    String oldClassName = className;
    className = newClassName;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StatechartPackage.STATE_MACHINE__CLASS_NAME, oldClassName, className));
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
      eNotify(new ENotificationImpl(this, Notification.SET, StatechartPackage.STATE_MACHINE__LANGUAGE, oldLanguage, language));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public int getNextID() {
    int tmp = nextID;
    nextID++;
    return tmp;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setNextID(int newNextID) {
    int oldNextID = nextID;
    nextID = newNextID;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StatechartPackage.STATE_MACHINE__NEXT_ID, oldNextID, nextID));
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
      eNotify(new ENotificationImpl(this, Notification.SET, StatechartPackage.STATE_MACHINE__ID, oldId, id));
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
      eNotify(new ENotificationImpl(this, Notification.SET, StatechartPackage.STATE_MACHINE__UUID, oldUuid, uuid));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public double getPriority() {
    return priority;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setPriority(double newPriority) {
    double oldPriority = priority;
    priority = newPriority;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StatechartPackage.STATE_MACHINE__PRIORITY, oldPriority, priority));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
    switch (featureID) {
      case StatechartPackage.STATE_MACHINE__STATES:
        return ((InternalEList<?>)getStates()).basicRemove(otherEnd, msgs);
      case StatechartPackage.STATE_MACHINE__TRANSITIONS:
        return ((InternalEList<?>)getTransitions()).basicRemove(otherEnd, msgs);
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
      case StatechartPackage.STATE_MACHINE__STATES:
        return getStates();
      case StatechartPackage.STATE_MACHINE__TRANSITIONS:
        return getTransitions();
      case StatechartPackage.STATE_MACHINE__AGENT_TYPE:
        return getAgentType();
      case StatechartPackage.STATE_MACHINE__PACKAGE:
        return getPackage();
      case StatechartPackage.STATE_MACHINE__CLASS_NAME:
        return getClassName();
      case StatechartPackage.STATE_MACHINE__LANGUAGE:
        return getLanguage();
      case StatechartPackage.STATE_MACHINE__NEXT_ID:
        return getNextID();
      case StatechartPackage.STATE_MACHINE__ID:
        return getId();
      case StatechartPackage.STATE_MACHINE__UUID:
        return getUuid();
      case StatechartPackage.STATE_MACHINE__PRIORITY:
        return getPriority();
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
      case StatechartPackage.STATE_MACHINE__STATES:
        getStates().clear();
        getStates().addAll((Collection<? extends AbstractState>)newValue);
        return;
      case StatechartPackage.STATE_MACHINE__TRANSITIONS:
        getTransitions().clear();
        getTransitions().addAll((Collection<? extends Transition>)newValue);
        return;
      case StatechartPackage.STATE_MACHINE__AGENT_TYPE:
        setAgentType((String)newValue);
        return;
      case StatechartPackage.STATE_MACHINE__PACKAGE:
        setPackage((String)newValue);
        return;
      case StatechartPackage.STATE_MACHINE__CLASS_NAME:
        setClassName((String)newValue);
        return;
      case StatechartPackage.STATE_MACHINE__LANGUAGE:
        setLanguage((LanguageTypes)newValue);
        return;
      case StatechartPackage.STATE_MACHINE__NEXT_ID:
        setNextID((Integer)newValue);
        return;
      case StatechartPackage.STATE_MACHINE__ID:
        setId((String)newValue);
        return;
      case StatechartPackage.STATE_MACHINE__UUID:
        setUuid((String)newValue);
        return;
      case StatechartPackage.STATE_MACHINE__PRIORITY:
        setPriority((Double)newValue);
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
      case StatechartPackage.STATE_MACHINE__STATES:
        getStates().clear();
        return;
      case StatechartPackage.STATE_MACHINE__TRANSITIONS:
        getTransitions().clear();
        return;
      case StatechartPackage.STATE_MACHINE__AGENT_TYPE:
        setAgentType(AGENT_TYPE_EDEFAULT);
        return;
      case StatechartPackage.STATE_MACHINE__PACKAGE:
        setPackage(PACKAGE_EDEFAULT);
        return;
      case StatechartPackage.STATE_MACHINE__CLASS_NAME:
        setClassName(CLASS_NAME_EDEFAULT);
        return;
      case StatechartPackage.STATE_MACHINE__LANGUAGE:
        setLanguage(LANGUAGE_EDEFAULT);
        return;
      case StatechartPackage.STATE_MACHINE__NEXT_ID:
        setNextID(NEXT_ID_EDEFAULT);
        return;
      case StatechartPackage.STATE_MACHINE__ID:
        setId(ID_EDEFAULT);
        return;
      case StatechartPackage.STATE_MACHINE__UUID:
        setUuid(UUID_EDEFAULT);
        return;
      case StatechartPackage.STATE_MACHINE__PRIORITY:
        setPriority(PRIORITY_EDEFAULT);
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
      case StatechartPackage.STATE_MACHINE__STATES:
        return states != null && !states.isEmpty();
      case StatechartPackage.STATE_MACHINE__TRANSITIONS:
        return transitions != null && !transitions.isEmpty();
      case StatechartPackage.STATE_MACHINE__AGENT_TYPE:
        return AGENT_TYPE_EDEFAULT == null ? agentType != null : !AGENT_TYPE_EDEFAULT.equals(agentType);
      case StatechartPackage.STATE_MACHINE__PACKAGE:
        return PACKAGE_EDEFAULT == null ? package_ != null : !PACKAGE_EDEFAULT.equals(package_);
      case StatechartPackage.STATE_MACHINE__CLASS_NAME:
        return CLASS_NAME_EDEFAULT == null ? className != null : !CLASS_NAME_EDEFAULT.equals(className);
      case StatechartPackage.STATE_MACHINE__LANGUAGE:
        return language != LANGUAGE_EDEFAULT;
      case StatechartPackage.STATE_MACHINE__NEXT_ID:
        return nextID != NEXT_ID_EDEFAULT;
      case StatechartPackage.STATE_MACHINE__ID:
        return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
      case StatechartPackage.STATE_MACHINE__UUID:
        return UUID_EDEFAULT == null ? uuid != null : !UUID_EDEFAULT.equals(uuid);
      case StatechartPackage.STATE_MACHINE__PRIORITY:
        return priority != PRIORITY_EDEFAULT;
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
    result.append(" (agentType: ");
    result.append(agentType);
    result.append(", package: ");
    result.append(package_);
    result.append(", className: ");
    result.append(className);
    result.append(", language: ");
    result.append(language);
    result.append(", nextID: ");
    result.append(nextID);
    result.append(", id: ");
    result.append(id);
    result.append(", uuid: ");
    result.append(uuid);
    result.append(", priority: ");
    result.append(priority);
    result.append(')');
    return result.toString();
  }

} //StateMachineImpl
