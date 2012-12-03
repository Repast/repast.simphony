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
import repast.simphony.statecharts.scmodel.CompositeState;
import repast.simphony.statecharts.scmodel.EqualsChecker;
import repast.simphony.statecharts.scmodel.FinalState;
import repast.simphony.statecharts.scmodel.History;
import repast.simphony.statecharts.scmodel.LanguageTypes;
import repast.simphony.statecharts.scmodel.MessageCheckerTypes;
import repast.simphony.statecharts.scmodel.PseudoState;
import repast.simphony.statecharts.scmodel.PseudoStateTypes;
import repast.simphony.statecharts.scmodel.State;
import repast.simphony.statecharts.scmodel.StateMachine;
import repast.simphony.statecharts.scmodel.StatechartFactory;
import repast.simphony.statecharts.scmodel.StatechartPackage;
import repast.simphony.statecharts.scmodel.Transition;
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
  public EAttribute getStateMachine_NextID() {
    return (EAttribute)stateMachineEClass.getEStructuralFeatures().get(6);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getStateMachine_ID() {
    return (EAttribute)stateMachineEClass.getEStructuralFeatures().get(7);
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
  public EAttribute getTransition_TriggerType() {
    return (EAttribute)transitionEClass.getEStructuralFeatures().get(6);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTransition_TriggerTime() {
    return (EAttribute)transitionEClass.getEStructuralFeatures().get(7);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTransition_TriggerCondition() {
    return (EAttribute)transitionEClass.getEStructuralFeatures().get(8);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTransition_TriggerConditionLanguage() {
    return (EAttribute)transitionEClass.getEStructuralFeatures().get(9);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTransition_MessageCheckerType() {
    return (EAttribute)transitionEClass.getEStructuralFeatures().get(10);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTransition_MessageCheckerClass() {
    return (EAttribute)transitionEClass.getEStructuralFeatures().get(11);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTransition_MessageCheckerObj() {
    return (EAttribute)transitionEClass.getEStructuralFeatures().get(12);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTransition_TriggerProbability() {
    return (EAttribute)transitionEClass.getEStructuralFeatures().get(13);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTransition_MessageCheckerCondition() {
    return (EAttribute)transitionEClass.getEStructuralFeatures().get(14);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTransition_MessageCheckerConditionLanguage() {
    return (EAttribute)transitionEClass.getEStructuralFeatures().get(15);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTransition_ID() {
    return (EAttribute)transitionEClass.getEStructuralFeatures().get(16);
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
  public EAttribute getAbstractState_OnEnter() {
    return (EAttribute)abstractStateEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getAbstractState_OnExit() {
    return (EAttribute)abstractStateEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getAbstractState_Language() {
    return (EAttribute)abstractStateEClass.getEStructuralFeatures().get(3);
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
    createEAttribute(stateMachineEClass, STATE_MACHINE__NEXT_ID);
    createEAttribute(stateMachineEClass, STATE_MACHINE__ID);

    stateEClass = createEClass(STATE);

    finalStateEClass = createEClass(FINAL_STATE);

    transitionEClass = createEClass(TRANSITION);
    createEReference(transitionEClass, TRANSITION__FROM);
    createEReference(transitionEClass, TRANSITION__TO);
    createEAttribute(transitionEClass, TRANSITION__PRIORITY);
    createEAttribute(transitionEClass, TRANSITION__ON_TRANSITION);
    createEAttribute(transitionEClass, TRANSITION__OUT_OF_BRANCH);
    createEAttribute(transitionEClass, TRANSITION__DEFAULT_TRANSITION);
    createEAttribute(transitionEClass, TRANSITION__TRIGGER_TYPE);
    createEAttribute(transitionEClass, TRANSITION__TRIGGER_TIME);
    createEAttribute(transitionEClass, TRANSITION__TRIGGER_CONDITION);
    createEAttribute(transitionEClass, TRANSITION__TRIGGER_CONDITION_LANGUAGE);
    createEAttribute(transitionEClass, TRANSITION__MESSAGE_CHECKER_TYPE);
    createEAttribute(transitionEClass, TRANSITION__MESSAGE_CHECKER_CLASS);
    createEAttribute(transitionEClass, TRANSITION__MESSAGE_CHECKER_OBJ);
    createEAttribute(transitionEClass, TRANSITION__TRIGGER_PROBABILITY);
    createEAttribute(transitionEClass, TRANSITION__MESSAGE_CHECKER_CONDITION);
    createEAttribute(transitionEClass, TRANSITION__MESSAGE_CHECKER_CONDITION_LANGUAGE);
    createEAttribute(transitionEClass, TRANSITION__ID);

    compositeStateEClass = createEClass(COMPOSITE_STATE);
    createEReference(compositeStateEClass, COMPOSITE_STATE__CHILDREN);

    abstractStateEClass = createEClass(ABSTRACT_STATE);
    createEAttribute(abstractStateEClass, ABSTRACT_STATE__ID);
    createEAttribute(abstractStateEClass, ABSTRACT_STATE__ON_ENTER);
    createEAttribute(abstractStateEClass, ABSTRACT_STATE__ON_EXIT);
    createEAttribute(abstractStateEClass, ABSTRACT_STATE__LANGUAGE);

    pseudoStateEClass = createEClass(PSEUDO_STATE);
    createEAttribute(pseudoStateEClass, PSEUDO_STATE__TYPE);

    historyEClass = createEClass(HISTORY);
    createEAttribute(historyEClass, HISTORY__SHALLOW);

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

    // Initialize classes and features; add operations and parameters
    initEClass(stateMachineEClass, StateMachine.class, "StateMachine", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getStateMachine_States(), this.getAbstractState(), null, "states", null, 0, -1, StateMachine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getStateMachine_Transitions(), this.getTransition(), null, "transitions", null, 0, -1, StateMachine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getStateMachine_AgentType(), ecorePackage.getEString(), "agentType", null, 0, 1, StateMachine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getStateMachine_Package(), ecorePackage.getEString(), "package", null, 0, 1, StateMachine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getStateMachine_ClassName(), ecorePackage.getEString(), "className", null, 0, 1, StateMachine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getStateMachine_Language(), this.getLanguageTypes(), "language", null, 0, 1, StateMachine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getStateMachine_NextID(), ecorePackage.getEInt(), "nextID", "0", 0, 1, StateMachine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getStateMachine_ID(), ecorePackage.getEString(), "ID", null, 0, 1, StateMachine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(stateEClass, State.class, "State", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(finalStateEClass, FinalState.class, "FinalState", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(transitionEClass, Transition.class, "Transition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getTransition_From(), this.getAbstractState(), null, "from", null, 1, 1, Transition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getTransition_To(), this.getAbstractState(), null, "to", null, 1, 1, Transition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTransition_Priority(), ecorePackage.getEDouble(), "priority", "0", 0, 1, Transition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTransition_OnTransition(), ecorePackage.getEString(), "onTransition", null, 0, 1, Transition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTransition_OutOfBranch(), ecorePackage.getEBoolean(), "outOfBranch", "false", 0, 1, Transition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTransition_DefaultTransition(), ecorePackage.getEBoolean(), "defaultTransition", "false", 0, 1, Transition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTransition_TriggerType(), this.getTriggerTypes(), "triggerType", null, 0, 1, Transition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTransition_TriggerTime(), ecorePackage.getEDouble(), "triggerTime", null, 0, 1, Transition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTransition_TriggerCondition(), ecorePackage.getEString(), "triggerCondition", null, 0, 1, Transition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTransition_TriggerConditionLanguage(), this.getLanguageTypes(), "triggerConditionLanguage", null, 0, 1, Transition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTransition_MessageCheckerType(), this.getMessageCheckerTypes(), "messageCheckerType", null, 0, 1, Transition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTransition_MessageCheckerClass(), ecorePackage.getEString(), "messageCheckerClass", null, 0, 1, Transition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTransition_MessageCheckerObj(), ecorePackage.getEString(), "messageCheckerObj", null, 0, 1, Transition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTransition_TriggerProbability(), ecorePackage.getEDouble(), "triggerProbability", null, 0, 1, Transition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTransition_MessageCheckerCondition(), ecorePackage.getEString(), "messageCheckerCondition", null, 0, 1, Transition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTransition_MessageCheckerConditionLanguage(), this.getLanguageTypes(), "messageCheckerConditionLanguage", null, 0, 1, Transition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTransition_ID(), ecorePackage.getEString(), "ID", null, 0, 1, Transition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(compositeStateEClass, CompositeState.class, "CompositeState", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getCompositeState_Children(), this.getAbstractState(), null, "children", null, 0, -1, CompositeState.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(abstractStateEClass, AbstractState.class, "AbstractState", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getAbstractState_Id(), ecorePackage.getEString(), "id", null, 0, 1, AbstractState.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getAbstractState_OnEnter(), ecorePackage.getEString(), "onEnter", null, 0, 1, AbstractState.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getAbstractState_OnExit(), ecorePackage.getEString(), "onExit", null, 0, 1, AbstractState.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getAbstractState_Language(), this.getLanguageTypes(), "language", null, 0, 1, AbstractState.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(pseudoStateEClass, PseudoState.class, "PseudoState", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getPseudoState_Type(), this.getPseudoStateTypes(), "type", null, 0, 1, PseudoState.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(historyEClass, History.class, "History", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getHistory_Shallow(), ecorePackage.getEBoolean(), "shallow", null, 0, 1, History.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

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
