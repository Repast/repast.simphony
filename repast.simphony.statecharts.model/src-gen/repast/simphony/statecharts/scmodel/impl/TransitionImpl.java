/**
 */
package repast.simphony.statecharts.scmodel.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import repast.simphony.statecharts.scmodel.AbstractState;
import repast.simphony.statecharts.scmodel.LanguageTypes;
import repast.simphony.statecharts.scmodel.MessageCheckerTypes;
import repast.simphony.statecharts.scmodel.StatechartPackage;
import repast.simphony.statecharts.scmodel.Transition;
import repast.simphony.statecharts.scmodel.TriggerTypes;

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
 *   <li>{@link repast.simphony.statecharts.scmodel.impl.TransitionImpl#getTriggerType <em>Trigger Type</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.impl.TransitionImpl#getTriggerTime <em>Trigger Time</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.impl.TransitionImpl#getTriggerCondition <em>Trigger Condition</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.impl.TransitionImpl#getTriggerConditionLanguage <em>Trigger Condition Language</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.impl.TransitionImpl#getMessageCheckerType <em>Message Checker Type</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.impl.TransitionImpl#getMessageCheckerClass <em>Message Checker Class</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.impl.TransitionImpl#getMessageCheckerObj <em>Message Checker Obj</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.impl.TransitionImpl#getTriggerProbability <em>Trigger Probability</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.impl.TransitionImpl#getMessageCheckerCondition <em>Message Checker Condition</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.impl.TransitionImpl#getMessageCheckerConditionLanguage <em>Message Checker Condition Language</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.impl.TransitionImpl#getID <em>ID</em>}</li>
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
   * The default value of the '{@link #getTriggerType() <em>Trigger Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTriggerType()
   * @generated
   * @ordered
   */
  protected static final TriggerTypes TRIGGER_TYPE_EDEFAULT = TriggerTypes.ALWAYS;

  /**
   * The cached value of the '{@link #getTriggerType() <em>Trigger Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTriggerType()
   * @generated
   * @ordered
   */
  protected TriggerTypes triggerType = TRIGGER_TYPE_EDEFAULT;

  /**
   * The default value of the '{@link #getTriggerTime() <em>Trigger Time</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTriggerTime()
   * @generated
   * @ordered
   */
  protected static final double TRIGGER_TIME_EDEFAULT = 0.0;

  /**
   * The cached value of the '{@link #getTriggerTime() <em>Trigger Time</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTriggerTime()
   * @generated
   * @ordered
   */
  protected double triggerTime = TRIGGER_TIME_EDEFAULT;

  /**
   * The default value of the '{@link #getTriggerCondition() <em>Trigger Condition</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTriggerCondition()
   * @generated
   * @ordered
   */
  protected static final String TRIGGER_CONDITION_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getTriggerCondition() <em>Trigger Condition</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTriggerCondition()
   * @generated
   * @ordered
   */
  protected String triggerCondition = TRIGGER_CONDITION_EDEFAULT;

  /**
   * The default value of the '{@link #getTriggerConditionLanguage() <em>Trigger Condition Language</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTriggerConditionLanguage()
   * @generated
   * @ordered
   */
  protected static final LanguageTypes TRIGGER_CONDITION_LANGUAGE_EDEFAULT = LanguageTypes.JAVA;

  /**
   * The cached value of the '{@link #getTriggerConditionLanguage() <em>Trigger Condition Language</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTriggerConditionLanguage()
   * @generated
   * @ordered
   */
  protected LanguageTypes triggerConditionLanguage = TRIGGER_CONDITION_LANGUAGE_EDEFAULT;

  /**
   * The default value of the '{@link #getMessageCheckerType() <em>Message Checker Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMessageCheckerType()
   * @generated
   * @ordered
   */
  protected static final MessageCheckerTypes MESSAGE_CHECKER_TYPE_EDEFAULT = MessageCheckerTypes.CONDITIONAL;

  /**
   * The cached value of the '{@link #getMessageCheckerType() <em>Message Checker Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMessageCheckerType()
   * @generated
   * @ordered
   */
  protected MessageCheckerTypes messageCheckerType = MESSAGE_CHECKER_TYPE_EDEFAULT;

  /**
   * The default value of the '{@link #getMessageCheckerClass() <em>Message Checker Class</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMessageCheckerClass()
   * @generated
   * @ordered
   */
  protected static final String MESSAGE_CHECKER_CLASS_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getMessageCheckerClass() <em>Message Checker Class</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMessageCheckerClass()
   * @generated
   * @ordered
   */
  protected String messageCheckerClass = MESSAGE_CHECKER_CLASS_EDEFAULT;

  /**
   * The default value of the '{@link #getMessageCheckerObj() <em>Message Checker Obj</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMessageCheckerObj()
   * @generated
   * @ordered
   */
  protected static final String MESSAGE_CHECKER_OBJ_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getMessageCheckerObj() <em>Message Checker Obj</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMessageCheckerObj()
   * @generated
   * @ordered
   */
  protected String messageCheckerObj = MESSAGE_CHECKER_OBJ_EDEFAULT;

  /**
   * The default value of the '{@link #getTriggerProbability() <em>Trigger Probability</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTriggerProbability()
   * @generated
   * @ordered
   */
  protected static final double TRIGGER_PROBABILITY_EDEFAULT = 0.0;

  /**
   * The cached value of the '{@link #getTriggerProbability() <em>Trigger Probability</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTriggerProbability()
   * @generated
   * @ordered
   */
  protected double triggerProbability = TRIGGER_PROBABILITY_EDEFAULT;

  /**
   * The default value of the '{@link #getMessageCheckerCondition() <em>Message Checker Condition</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMessageCheckerCondition()
   * @generated
   * @ordered
   */
  protected static final String MESSAGE_CHECKER_CONDITION_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getMessageCheckerCondition() <em>Message Checker Condition</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMessageCheckerCondition()
   * @generated
   * @ordered
   */
  protected String messageCheckerCondition = MESSAGE_CHECKER_CONDITION_EDEFAULT;

  /**
   * The default value of the '{@link #getMessageCheckerConditionLanguage() <em>Message Checker Condition Language</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMessageCheckerConditionLanguage()
   * @generated
   * @ordered
   */
  protected static final LanguageTypes MESSAGE_CHECKER_CONDITION_LANGUAGE_EDEFAULT = LanguageTypes.JAVA;

  /**
   * The cached value of the '{@link #getMessageCheckerConditionLanguage() <em>Message Checker Condition Language</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMessageCheckerConditionLanguage()
   * @generated
   * @ordered
   */
  protected LanguageTypes messageCheckerConditionLanguage = MESSAGE_CHECKER_CONDITION_LANGUAGE_EDEFAULT;

  /**
   * The default value of the '{@link #getID() <em>ID</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getID()
   * @generated
   * @ordered
   */
  protected static final String ID_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getID() <em>ID</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getID()
   * @generated
   * @ordered
   */
  protected String id = ID_EDEFAULT;

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
   * @generated
   */
  public void setFrom(AbstractState newFrom) {
    AbstractState oldFrom = from;
    from = newFrom;
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
  public TriggerTypes getTriggerType() {
    return triggerType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setTriggerType(TriggerTypes newTriggerType) {
    TriggerTypes oldTriggerType = triggerType;
    triggerType = newTriggerType == null ? TRIGGER_TYPE_EDEFAULT : newTriggerType;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StatechartPackage.TRANSITION__TRIGGER_TYPE, oldTriggerType, triggerType));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public double getTriggerTime() {
    return triggerTime;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setTriggerTime(double newTriggerTime) {
    double oldTriggerTime = triggerTime;
    triggerTime = newTriggerTime;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StatechartPackage.TRANSITION__TRIGGER_TIME, oldTriggerTime, triggerTime));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getTriggerCondition() {
    return triggerCondition;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setTriggerCondition(String newTriggerCondition) {
    String oldTriggerCondition = triggerCondition;
    triggerCondition = newTriggerCondition;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StatechartPackage.TRANSITION__TRIGGER_CONDITION, oldTriggerCondition, triggerCondition));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public LanguageTypes getTriggerConditionLanguage() {
    return triggerConditionLanguage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setTriggerConditionLanguage(LanguageTypes newTriggerConditionLanguage) {
    LanguageTypes oldTriggerConditionLanguage = triggerConditionLanguage;
    triggerConditionLanguage = newTriggerConditionLanguage == null ? TRIGGER_CONDITION_LANGUAGE_EDEFAULT : newTriggerConditionLanguage;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StatechartPackage.TRANSITION__TRIGGER_CONDITION_LANGUAGE, oldTriggerConditionLanguage, triggerConditionLanguage));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MessageCheckerTypes getMessageCheckerType() {
    return messageCheckerType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setMessageCheckerType(MessageCheckerTypes newMessageCheckerType) {
    MessageCheckerTypes oldMessageCheckerType = messageCheckerType;
    messageCheckerType = newMessageCheckerType == null ? MESSAGE_CHECKER_TYPE_EDEFAULT : newMessageCheckerType;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StatechartPackage.TRANSITION__MESSAGE_CHECKER_TYPE, oldMessageCheckerType, messageCheckerType));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getMessageCheckerClass() {
    return messageCheckerClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setMessageCheckerClass(String newMessageCheckerClass) {
    String oldMessageCheckerClass = messageCheckerClass;
    messageCheckerClass = newMessageCheckerClass;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StatechartPackage.TRANSITION__MESSAGE_CHECKER_CLASS, oldMessageCheckerClass, messageCheckerClass));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getMessageCheckerObj() {
    return messageCheckerObj;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setMessageCheckerObj(String newMessageCheckerObj) {
    String oldMessageCheckerObj = messageCheckerObj;
    messageCheckerObj = newMessageCheckerObj;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StatechartPackage.TRANSITION__MESSAGE_CHECKER_OBJ, oldMessageCheckerObj, messageCheckerObj));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public double getTriggerProbability() {
    return triggerProbability;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setTriggerProbability(double newTriggerProbability) {
    double oldTriggerProbability = triggerProbability;
    triggerProbability = newTriggerProbability;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StatechartPackage.TRANSITION__TRIGGER_PROBABILITY, oldTriggerProbability, triggerProbability));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getMessageCheckerCondition() {
    return messageCheckerCondition;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setMessageCheckerCondition(String newMessageCheckerCondition) {
    String oldMessageCheckerCondition = messageCheckerCondition;
    messageCheckerCondition = newMessageCheckerCondition;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StatechartPackage.TRANSITION__MESSAGE_CHECKER_CONDITION, oldMessageCheckerCondition, messageCheckerCondition));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public LanguageTypes getMessageCheckerConditionLanguage() {
    return messageCheckerConditionLanguage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setMessageCheckerConditionLanguage(LanguageTypes newMessageCheckerConditionLanguage) {
    LanguageTypes oldMessageCheckerConditionLanguage = messageCheckerConditionLanguage;
    messageCheckerConditionLanguage = newMessageCheckerConditionLanguage == null ? MESSAGE_CHECKER_CONDITION_LANGUAGE_EDEFAULT : newMessageCheckerConditionLanguage;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StatechartPackage.TRANSITION__MESSAGE_CHECKER_CONDITION_LANGUAGE, oldMessageCheckerConditionLanguage, messageCheckerConditionLanguage));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getID() {
    return id;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setID(String newID) {
    String oldID = id;
    id = newID;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StatechartPackage.TRANSITION__ID, oldID, id));
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
      case StatechartPackage.TRANSITION__TRIGGER_TYPE:
        return getTriggerType();
      case StatechartPackage.TRANSITION__TRIGGER_TIME:
        return getTriggerTime();
      case StatechartPackage.TRANSITION__TRIGGER_CONDITION:
        return getTriggerCondition();
      case StatechartPackage.TRANSITION__TRIGGER_CONDITION_LANGUAGE:
        return getTriggerConditionLanguage();
      case StatechartPackage.TRANSITION__MESSAGE_CHECKER_TYPE:
        return getMessageCheckerType();
      case StatechartPackage.TRANSITION__MESSAGE_CHECKER_CLASS:
        return getMessageCheckerClass();
      case StatechartPackage.TRANSITION__MESSAGE_CHECKER_OBJ:
        return getMessageCheckerObj();
      case StatechartPackage.TRANSITION__TRIGGER_PROBABILITY:
        return getTriggerProbability();
      case StatechartPackage.TRANSITION__MESSAGE_CHECKER_CONDITION:
        return getMessageCheckerCondition();
      case StatechartPackage.TRANSITION__MESSAGE_CHECKER_CONDITION_LANGUAGE:
        return getMessageCheckerConditionLanguage();
      case StatechartPackage.TRANSITION__ID:
        return getID();
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
        setPriority((Double)newValue);
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
      case StatechartPackage.TRANSITION__TRIGGER_TYPE:
        setTriggerType((TriggerTypes)newValue);
        return;
      case StatechartPackage.TRANSITION__TRIGGER_TIME:
        setTriggerTime((Double)newValue);
        return;
      case StatechartPackage.TRANSITION__TRIGGER_CONDITION:
        setTriggerCondition((String)newValue);
        return;
      case StatechartPackage.TRANSITION__TRIGGER_CONDITION_LANGUAGE:
        setTriggerConditionLanguage((LanguageTypes)newValue);
        return;
      case StatechartPackage.TRANSITION__MESSAGE_CHECKER_TYPE:
        setMessageCheckerType((MessageCheckerTypes)newValue);
        return;
      case StatechartPackage.TRANSITION__MESSAGE_CHECKER_CLASS:
        setMessageCheckerClass((String)newValue);
        return;
      case StatechartPackage.TRANSITION__MESSAGE_CHECKER_OBJ:
        setMessageCheckerObj((String)newValue);
        return;
      case StatechartPackage.TRANSITION__TRIGGER_PROBABILITY:
        setTriggerProbability((Double)newValue);
        return;
      case StatechartPackage.TRANSITION__MESSAGE_CHECKER_CONDITION:
        setMessageCheckerCondition((String)newValue);
        return;
      case StatechartPackage.TRANSITION__MESSAGE_CHECKER_CONDITION_LANGUAGE:
        setMessageCheckerConditionLanguage((LanguageTypes)newValue);
        return;
      case StatechartPackage.TRANSITION__ID:
        setID((String)newValue);
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
      case StatechartPackage.TRANSITION__TRIGGER_TYPE:
        setTriggerType(TRIGGER_TYPE_EDEFAULT);
        return;
      case StatechartPackage.TRANSITION__TRIGGER_TIME:
        setTriggerTime(TRIGGER_TIME_EDEFAULT);
        return;
      case StatechartPackage.TRANSITION__TRIGGER_CONDITION:
        setTriggerCondition(TRIGGER_CONDITION_EDEFAULT);
        return;
      case StatechartPackage.TRANSITION__TRIGGER_CONDITION_LANGUAGE:
        setTriggerConditionLanguage(TRIGGER_CONDITION_LANGUAGE_EDEFAULT);
        return;
      case StatechartPackage.TRANSITION__MESSAGE_CHECKER_TYPE:
        setMessageCheckerType(MESSAGE_CHECKER_TYPE_EDEFAULT);
        return;
      case StatechartPackage.TRANSITION__MESSAGE_CHECKER_CLASS:
        setMessageCheckerClass(MESSAGE_CHECKER_CLASS_EDEFAULT);
        return;
      case StatechartPackage.TRANSITION__MESSAGE_CHECKER_OBJ:
        setMessageCheckerObj(MESSAGE_CHECKER_OBJ_EDEFAULT);
        return;
      case StatechartPackage.TRANSITION__TRIGGER_PROBABILITY:
        setTriggerProbability(TRIGGER_PROBABILITY_EDEFAULT);
        return;
      case StatechartPackage.TRANSITION__MESSAGE_CHECKER_CONDITION:
        setMessageCheckerCondition(MESSAGE_CHECKER_CONDITION_EDEFAULT);
        return;
      case StatechartPackage.TRANSITION__MESSAGE_CHECKER_CONDITION_LANGUAGE:
        setMessageCheckerConditionLanguage(MESSAGE_CHECKER_CONDITION_LANGUAGE_EDEFAULT);
        return;
      case StatechartPackage.TRANSITION__ID:
        setID(ID_EDEFAULT);
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
      case StatechartPackage.TRANSITION__TRIGGER_TYPE:
        return triggerType != TRIGGER_TYPE_EDEFAULT;
      case StatechartPackage.TRANSITION__TRIGGER_TIME:
        return triggerTime != TRIGGER_TIME_EDEFAULT;
      case StatechartPackage.TRANSITION__TRIGGER_CONDITION:
        return TRIGGER_CONDITION_EDEFAULT == null ? triggerCondition != null : !TRIGGER_CONDITION_EDEFAULT.equals(triggerCondition);
      case StatechartPackage.TRANSITION__TRIGGER_CONDITION_LANGUAGE:
        return triggerConditionLanguage != TRIGGER_CONDITION_LANGUAGE_EDEFAULT;
      case StatechartPackage.TRANSITION__MESSAGE_CHECKER_TYPE:
        return messageCheckerType != MESSAGE_CHECKER_TYPE_EDEFAULT;
      case StatechartPackage.TRANSITION__MESSAGE_CHECKER_CLASS:
        return MESSAGE_CHECKER_CLASS_EDEFAULT == null ? messageCheckerClass != null : !MESSAGE_CHECKER_CLASS_EDEFAULT.equals(messageCheckerClass);
      case StatechartPackage.TRANSITION__MESSAGE_CHECKER_OBJ:
        return MESSAGE_CHECKER_OBJ_EDEFAULT == null ? messageCheckerObj != null : !MESSAGE_CHECKER_OBJ_EDEFAULT.equals(messageCheckerObj);
      case StatechartPackage.TRANSITION__TRIGGER_PROBABILITY:
        return triggerProbability != TRIGGER_PROBABILITY_EDEFAULT;
      case StatechartPackage.TRANSITION__MESSAGE_CHECKER_CONDITION:
        return MESSAGE_CHECKER_CONDITION_EDEFAULT == null ? messageCheckerCondition != null : !MESSAGE_CHECKER_CONDITION_EDEFAULT.equals(messageCheckerCondition);
      case StatechartPackage.TRANSITION__MESSAGE_CHECKER_CONDITION_LANGUAGE:
        return messageCheckerConditionLanguage != MESSAGE_CHECKER_CONDITION_LANGUAGE_EDEFAULT;
      case StatechartPackage.TRANSITION__ID:
        return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
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
    result.append(", triggerType: ");
    result.append(triggerType);
    result.append(", triggerTime: ");
    result.append(triggerTime);
    result.append(", triggerCondition: ");
    result.append(triggerCondition);
    result.append(", triggerConditionLanguage: ");
    result.append(triggerConditionLanguage);
    result.append(", messageCheckerType: ");
    result.append(messageCheckerType);
    result.append(", messageCheckerClass: ");
    result.append(messageCheckerClass);
    result.append(", messageCheckerObj: ");
    result.append(messageCheckerObj);
    result.append(", triggerProbability: ");
    result.append(triggerProbability);
    result.append(", messageCheckerCondition: ");
    result.append(messageCheckerCondition);
    result.append(", messageCheckerConditionLanguage: ");
    result.append(messageCheckerConditionLanguage);
    result.append(", ID: ");
    result.append(id);
    result.append(')');
    return result.toString();
  }

} //TransitionImpl
