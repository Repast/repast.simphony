/**
 */
package repast.simphony.statecharts.scmodel.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import repast.simphony.statecharts.scmodel.AbstractState;
import repast.simphony.statecharts.scmodel.Action;
import repast.simphony.statecharts.scmodel.CompositeState;
import repast.simphony.statecharts.scmodel.ConditionChecker;
import repast.simphony.statecharts.scmodel.ConditionTrigger;
import repast.simphony.statecharts.scmodel.DefaultTrigger;
import repast.simphony.statecharts.scmodel.EqualsChecker;
import repast.simphony.statecharts.scmodel.FinalState;
import repast.simphony.statecharts.scmodel.History;
import repast.simphony.statecharts.scmodel.LanguageTypes;
import repast.simphony.statecharts.scmodel.MessageChecker;
import repast.simphony.statecharts.scmodel.MessageCheckerTypes;
import repast.simphony.statecharts.scmodel.MessageTrigger;
import repast.simphony.statecharts.scmodel.ProbabilityTrigger;
import repast.simphony.statecharts.scmodel.PseudoState;
import repast.simphony.statecharts.scmodel.PseudoStateTypes;
import repast.simphony.statecharts.scmodel.State;
import repast.simphony.statecharts.scmodel.StateMachine;
import repast.simphony.statecharts.scmodel.StatechartFactory;
import repast.simphony.statecharts.scmodel.StatechartPackage;
import repast.simphony.statecharts.scmodel.Transition;
import repast.simphony.statecharts.scmodel.Trigger;
import repast.simphony.statecharts.scmodel.TriggerTypes;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class StatechartPackageImpl extends EPackageImpl implements StatechartPackage {
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass stateMachineEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass stateEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass finalStateEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass transitionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass compositeStateEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass abstractStateEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass pseudoStateEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass historyEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass triggerEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass defaultTriggerEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass probabilityTriggerEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass conditionTriggerEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass messageTriggerEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass messageCheckerEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass conditionCheckerEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass equalsCheckerEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass actionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EEnum pseudoStateTypesEEnum = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EEnum triggerTypesEEnum = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EEnum messageCheckerTypesEEnum = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EEnum languageTypesEEnum = null;

  /**
   * Creates an instance of the model <b>Package</b>, registered with
   * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
   * package URI value.
   * <p>Note: the correct way to create the package is via the static
   * factory method {@link #init init()}, which also performs
   * initialization of the package, or returns the registered package,
   * if one already exists.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.emf.ecore.EPackage.Registry
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#eNS_URI
   * @see #init()
   * @generated
   */
  private StatechartPackageImpl() {
    super(eNS_URI, StatechartFactory.eINSTANCE);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static boolean isInited = false;

  /**
   * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
   * 
   * <p>This method is used to initialize {@link StatechartPackage#eINSTANCE} when that field is accessed.
   * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #eNS_URI
   * @see #createPackageContents()
   * @see #initializePackageContents()
   * @generated
   */
  public static StatechartPackage init() {
    if (isInited) return (StatechartPackage)EPackage.Registry.INSTANCE.getEPackage(StatechartPackage.eNS_URI);

    // Obtain or create and register package
    StatechartPackageImpl theStatechartPackage = (StatechartPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof StatechartPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new StatechartPackageImpl());

    isInited = true;

    // Create package meta-data objects
    theStatechartPackage.createPackageContents();

    // Initialize created meta-data
    theStatechartPackage.initializePackageContents();

    // Mark meta-data to indicate it can't be changed
    theStatechartPackage.freeze();

  
    // Update the registry and return the package
    EPackage.Registry.INSTANCE.put(StatechartPackage.eNS_URI, theStatechartPackage);
    return theStatechartPackage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getStateMachine() {
    return stateMachineEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getStateMachine_States() {
    return (EReference)stateMachineEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getStateMachine_Transitions() {
    return (EReference)stateMachineEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getStateMachine_AgentType() {
    return (EAttribute)stateMachineEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getStateMachine_Package() {
    return (EAttribute)stateMachineEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getStateMachine_ClassName() {
    return (EAttribute)stateMachineEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getStateMachine_Language() {
    return (EAttribute)stateMachineEClass.getEStructuralFeatures().get(5);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getState() {
    return stateEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getState_OnExit() {
    return (EReference)stateEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getState_OnEnter() {
    return (EReference)stateEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getFinalState() {
    return finalStateEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getTransition() {
    return transitionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getTransition_From() {
    return (EReference)transitionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getTransition_To() {
    return (EReference)transitionEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTransition_Priority() {
    return (EAttribute)transitionEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTransition_OnTransition() {
    return (EAttribute)transitionEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTransition_OutOfBranch() {
    return (EAttribute)transitionEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTransition_DefaultTransition() {
    return (EAttribute)transitionEClass.getEStructuralFeatures().get(5);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getTransition_Trigger() {
    return (EReference)transitionEClass.getEStructuralFeatures().get(6);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getCompositeState() {
    return compositeStateEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getCompositeState_Children() {
    return (EReference)compositeStateEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getCompositeState_OnEnter() {
    return (EReference)compositeStateEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getCompositeState_OnExit() {
    return (EReference)compositeStateEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getAbstractState() {
    return abstractStateEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getAbstractState_Id() {
    return (EAttribute)abstractStateEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getPseudoState() {
    return pseudoStateEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getPseudoState_Type() {
    return (EAttribute)pseudoStateEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getHistory() {
    return historyEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getHistory_Shallow() {
    return (EAttribute)historyEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getTrigger() {
    return triggerEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTrigger_Type() {
    return (EAttribute)triggerEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getDefaultTrigger() {
    return defaultTriggerEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDefaultTrigger_Time() {
    return (EAttribute)defaultTriggerEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getProbabilityTrigger() {
    return probabilityTriggerEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getProbabilityTrigger_Probability() {
    return (EAttribute)probabilityTriggerEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getConditionTrigger() {
    return conditionTriggerEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getConditionTrigger_Condition() {
    return (EAttribute)conditionTriggerEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMessageTrigger() {
    return messageTriggerEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getMessageTrigger_Checker() {
    return (EReference)messageTriggerEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMessageChecker() {
    return messageCheckerEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMessageChecker_Type() {
    return (EAttribute)messageCheckerEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getConditionChecker() {
    return conditionCheckerEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getConditionChecker_Condition() {
    return (EAttribute)conditionCheckerEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getEqualsChecker() {
    return equalsCheckerEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getEqualsChecker_Clazz() {
    return (EAttribute)equalsCheckerEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getEqualsChecker_Obj() {
    return (EAttribute)equalsCheckerEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getAction() {
    return actionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getAction_Code() {
    return (EAttribute)actionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getAction_Language() {
    return (EAttribute)actionEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EEnum getPseudoStateTypes() {
    return pseudoStateTypesEEnum;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EEnum getTriggerTypes() {
    return triggerTypesEEnum;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EEnum getMessageCheckerTypes() {
    return messageCheckerTypesEEnum;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EEnum getLanguageTypes() {
    return languageTypesEEnum;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public StatechartFactory getStatechartFactory() {
    return (StatechartFactory)getEFactoryInstance();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private boolean isCreated = false;

  /**
   * Creates the meta-model objects for the package.  This method is
   * guarded to have no affect on any invocation but its first.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void createPackageContents() {
    if (isCreated) return;
    isCreated = true;

    // Create classes and their features
    stateMachineEClass = createEClass(STATE_MACHINE);
    createEReference(stateMachineEClass, STATE_MACHINE__STATES);
    createEReference(stateMachineEClass, STATE_MACHINE__TRANSITIONS);
    createEAttribute(stateMachineEClass, STATE_MACHINE__AGENT_TYPE);
    createEAttribute(stateMachineEClass, STATE_MACHINE__PACKAGE);
    createEAttribute(stateMachineEClass, STATE_MACHINE__CLASS_NAME);
    createEAttribute(stateMachineEClass, STATE_MACHINE__LANGUAGE);

    stateEClass = createEClass(STATE);
    createEReference(stateEClass, STATE__ON_EXIT);
    createEReference(stateEClass, STATE__ON_ENTER);

    finalStateEClass = createEClass(FINAL_STATE);

    transitionEClass = createEClass(TRANSITION);
    createEReference(transitionEClass, TRANSITION__FROM);
    createEReference(transitionEClass, TRANSITION__TO);
    createEAttribute(transitionEClass, TRANSITION__PRIORITY);
    createEAttribute(transitionEClass, TRANSITION__ON_TRANSITION);
    createEAttribute(transitionEClass, TRANSITION__OUT_OF_BRANCH);
    createEAttribute(transitionEClass, TRANSITION__DEFAULT_TRANSITION);
    createEReference(transitionEClass, TRANSITION__TRIGGER);

    compositeStateEClass = createEClass(COMPOSITE_STATE);
    createEReference(compositeStateEClass, COMPOSITE_STATE__CHILDREN);
    createEReference(compositeStateEClass, COMPOSITE_STATE__ON_ENTER);
    createEReference(compositeStateEClass, COMPOSITE_STATE__ON_EXIT);

    abstractStateEClass = createEClass(ABSTRACT_STATE);
    createEAttribute(abstractStateEClass, ABSTRACT_STATE__ID);

    pseudoStateEClass = createEClass(PSEUDO_STATE);
    createEAttribute(pseudoStateEClass, PSEUDO_STATE__TYPE);

    historyEClass = createEClass(HISTORY);
    createEAttribute(historyEClass, HISTORY__SHALLOW);

    triggerEClass = createEClass(TRIGGER);
    createEAttribute(triggerEClass, TRIGGER__TYPE);

    defaultTriggerEClass = createEClass(DEFAULT_TRIGGER);
    createEAttribute(defaultTriggerEClass, DEFAULT_TRIGGER__TIME);

    probabilityTriggerEClass = createEClass(PROBABILITY_TRIGGER);
    createEAttribute(probabilityTriggerEClass, PROBABILITY_TRIGGER__PROBABILITY);

    conditionTriggerEClass = createEClass(CONDITION_TRIGGER);
    createEAttribute(conditionTriggerEClass, CONDITION_TRIGGER__CONDITION);

    messageTriggerEClass = createEClass(MESSAGE_TRIGGER);
    createEReference(messageTriggerEClass, MESSAGE_TRIGGER__CHECKER);

    messageCheckerEClass = createEClass(MESSAGE_CHECKER);
    createEAttribute(messageCheckerEClass, MESSAGE_CHECKER__TYPE);

    conditionCheckerEClass = createEClass(CONDITION_CHECKER);
    createEAttribute(conditionCheckerEClass, CONDITION_CHECKER__CONDITION);

    equalsCheckerEClass = createEClass(EQUALS_CHECKER);
    createEAttribute(equalsCheckerEClass, EQUALS_CHECKER__CLAZZ);
    createEAttribute(equalsCheckerEClass, EQUALS_CHECKER__OBJ);

    actionEClass = createEClass(ACTION);
    createEAttribute(actionEClass, ACTION__CODE);
    createEAttribute(actionEClass, ACTION__LANGUAGE);

    // Create enums
    pseudoStateTypesEEnum = createEEnum(PSEUDO_STATE_TYPES);
    triggerTypesEEnum = createEEnum(TRIGGER_TYPES);
    messageCheckerTypesEEnum = createEEnum(MESSAGE_CHECKER_TYPES);
    languageTypesEEnum = createEEnum(LANGUAGE_TYPES);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private boolean isInitialized = false;

  /**
   * Complete the initialization of the package and its meta-model.  This
   * method is guarded to have no affect on any invocation but its first.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void initializePackageContents() {
    if (isInitialized) return;
    isInitialized = true;

    // Initialize package
    setName(eNAME);
    setNsPrefix(eNS_PREFIX);
    setNsURI(eNS_URI);

    // Create type parameters

    // Set bounds for type parameters

    // Add supertypes to classes
    stateEClass.getESuperTypes().add(this.getAbstractState());
    finalStateEClass.getESuperTypes().add(this.getState());
    compositeStateEClass.getESuperTypes().add(this.getAbstractState());
    pseudoStateEClass.getESuperTypes().add(this.getAbstractState());
    historyEClass.getESuperTypes().add(this.getState());
    defaultTriggerEClass.getESuperTypes().add(this.getTrigger());
    probabilityTriggerEClass.getESuperTypes().add(this.getDefaultTrigger());
    conditionTriggerEClass.getESuperTypes().add(this.getDefaultTrigger());
    messageTriggerEClass.getESuperTypes().add(this.getDefaultTrigger());
    conditionCheckerEClass.getESuperTypes().add(this.getMessageChecker());
    equalsCheckerEClass.getESuperTypes().add(this.getMessageChecker());

    // Initialize classes and features; add operations and parameters
    initEClass(stateMachineEClass, StateMachine.class, "StateMachine", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getStateMachine_States(), this.getAbstractState(), null, "states", null, 0, -1, StateMachine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getStateMachine_Transitions(), this.getTransition(), null, "transitions", null, 0, -1, StateMachine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getStateMachine_AgentType(), ecorePackage.getEString(), "agentType", null, 0, 1, StateMachine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getStateMachine_Package(), ecorePackage.getEString(), "package", null, 0, 1, StateMachine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getStateMachine_ClassName(), ecorePackage.getEString(), "className", null, 0, 1, StateMachine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getStateMachine_Language(), this.getLanguageTypes(), "language", null, 0, 1, StateMachine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(stateEClass, State.class, "State", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getState_OnExit(), this.getAction(), null, "onExit", null, 1, 1, State.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getState_OnEnter(), this.getAction(), null, "onEnter", null, 1, 1, State.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(finalStateEClass, FinalState.class, "FinalState", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(transitionEClass, Transition.class, "Transition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getTransition_From(), this.getAbstractState(), null, "from", null, 1, 1, Transition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getTransition_To(), this.getAbstractState(), null, "to", null, 1, 1, Transition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTransition_Priority(), ecorePackage.getEInt(), "priority", "0", 0, 1, Transition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTransition_OnTransition(), ecorePackage.getEString(), "onTransition", null, 0, 1, Transition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTransition_OutOfBranch(), ecorePackage.getEBoolean(), "outOfBranch", "false", 0, 1, Transition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTransition_DefaultTransition(), ecorePackage.getEBoolean(), "defaultTransition", "false", 0, 1, Transition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getTransition_Trigger(), this.getTrigger(), null, "trigger", null, 1, 1, Transition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(compositeStateEClass, CompositeState.class, "CompositeState", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getCompositeState_Children(), this.getAbstractState(), null, "children", null, 0, -1, CompositeState.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getCompositeState_OnEnter(), this.getAction(), null, "onEnter", null, 1, 1, CompositeState.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getCompositeState_OnExit(), this.getAction(), null, "onExit", null, 1, 1, CompositeState.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(abstractStateEClass, AbstractState.class, "AbstractState", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getAbstractState_Id(), ecorePackage.getEString(), "id", null, 0, 1, AbstractState.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(pseudoStateEClass, PseudoState.class, "PseudoState", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getPseudoState_Type(), this.getPseudoStateTypes(), "type", null, 0, 1, PseudoState.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(historyEClass, History.class, "History", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getHistory_Shallow(), ecorePackage.getEBoolean(), "shallow", null, 0, 1, History.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(triggerEClass, Trigger.class, "Trigger", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getTrigger_Type(), this.getTriggerTypes(), "type", null, 0, 1, Trigger.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(defaultTriggerEClass, DefaultTrigger.class, "DefaultTrigger", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getDefaultTrigger_Time(), ecorePackage.getEDouble(), "time", "1.0", 0, 1, DefaultTrigger.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(probabilityTriggerEClass, ProbabilityTrigger.class, "ProbabilityTrigger", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getProbabilityTrigger_Probability(), ecorePackage.getEDouble(), "probability", null, 0, 1, ProbabilityTrigger.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(conditionTriggerEClass, ConditionTrigger.class, "ConditionTrigger", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getConditionTrigger_Condition(), ecorePackage.getEString(), "condition", null, 0, 1, ConditionTrigger.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(messageTriggerEClass, MessageTrigger.class, "MessageTrigger", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getMessageTrigger_Checker(), this.getMessageChecker(), null, "checker", null, 1, 1, MessageTrigger.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(messageCheckerEClass, MessageChecker.class, "MessageChecker", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMessageChecker_Type(), this.getMessageCheckerTypes(), "type", null, 0, 1, MessageChecker.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(conditionCheckerEClass, ConditionChecker.class, "ConditionChecker", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getConditionChecker_Condition(), ecorePackage.getEString(), "condition", null, 0, 1, ConditionChecker.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(equalsCheckerEClass, EqualsChecker.class, "EqualsChecker", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getEqualsChecker_Clazz(), ecorePackage.getEString(), "clazz", null, 0, 1, EqualsChecker.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getEqualsChecker_Obj(), ecorePackage.getEString(), "obj", null, 0, 1, EqualsChecker.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(actionEClass, Action.class, "Action", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getAction_Code(), ecorePackage.getEString(), "code", null, 0, 1, Action.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getAction_Language(), this.getLanguageTypes(), "language", null, 0, 1, Action.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    // Initialize enums and add enum literals
    initEEnum(pseudoStateTypesEEnum, PseudoStateTypes.class, "PseudoStateTypes");
    addEEnumLiteral(pseudoStateTypesEEnum, PseudoStateTypes.INITIAL);
    addEEnumLiteral(pseudoStateTypesEEnum, PseudoStateTypes.ENTRY);
    addEEnumLiteral(pseudoStateTypesEEnum, PseudoStateTypes.CHOICE);

    initEEnum(triggerTypesEEnum, TriggerTypes.class, "TriggerTypes");
    addEEnumLiteral(triggerTypesEEnum, TriggerTypes.ALWAYS);
    addEEnumLiteral(triggerTypesEEnum, TriggerTypes.TIMED);
    addEEnumLiteral(triggerTypesEEnum, TriggerTypes.EXPONENTIAL);
    addEEnumLiteral(triggerTypesEEnum, TriggerTypes.PROBABILITY);
    addEEnumLiteral(triggerTypesEEnum, TriggerTypes.CONDITION);
    addEEnumLiteral(triggerTypesEEnum, TriggerTypes.MESSAGE);

    initEEnum(messageCheckerTypesEEnum, MessageCheckerTypes.class, "MessageCheckerTypes");
    addEEnumLiteral(messageCheckerTypesEEnum, MessageCheckerTypes.CONDITIONAL);
    addEEnumLiteral(messageCheckerTypesEEnum, MessageCheckerTypes.EQUALS);
    addEEnumLiteral(messageCheckerTypesEEnum, MessageCheckerTypes.UNCONDITIONAL);
    addEEnumLiteral(messageCheckerTypesEEnum, MessageCheckerTypes.ALWAYS);

    initEEnum(languageTypesEEnum, LanguageTypes.class, "LanguageTypes");
    addEEnumLiteral(languageTypesEEnum, LanguageTypes.JAVA);
    addEEnumLiteral(languageTypesEEnum, LanguageTypes.GROOVY);
    addEEnumLiteral(languageTypesEEnum, LanguageTypes.RELOGO);

    // Create resource
    createResource(eNS_URI);
  }

} //StatechartPackageImpl
