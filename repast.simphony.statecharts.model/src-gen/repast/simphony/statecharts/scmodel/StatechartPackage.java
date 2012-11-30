/**
 */
package repast.simphony.statecharts.scmodel;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see repast.simphony.statecharts.scmodel.StatechartFactory
 * @model kind="package"
 * @generated
 */
public interface StatechartPackage extends EPackage {
  /**
   * The package name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNAME = "scmodel";

  /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_URI = "http://repast.sf.net/statecharts";

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_PREFIX = "";

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  StatechartPackage eINSTANCE = repast.simphony.statecharts.scmodel.impl.StatechartPackageImpl.init();

  /**
   * The meta object id for the '{@link repast.simphony.statecharts.scmodel.impl.StateMachineImpl <em>State Machine</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see repast.simphony.statecharts.scmodel.impl.StateMachineImpl
   * @see repast.simphony.statecharts.scmodel.impl.StatechartPackageImpl#getStateMachine()
   * @generated
   */
  int STATE_MACHINE = 0;

  /**
   * The feature id for the '<em><b>States</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STATE_MACHINE__STATES = 0;

  /**
   * The feature id for the '<em><b>Transitions</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STATE_MACHINE__TRANSITIONS = 1;

  /**
   * The feature id for the '<em><b>Agent Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STATE_MACHINE__AGENT_TYPE = 2;

  /**
   * The feature id for the '<em><b>Package</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STATE_MACHINE__PACKAGE = 3;

  /**
   * The feature id for the '<em><b>Class Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STATE_MACHINE__CLASS_NAME = 4;

  /**
   * The feature id for the '<em><b>Language</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STATE_MACHINE__LANGUAGE = 5;

  /**
   * The number of structural features of the '<em>State Machine</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STATE_MACHINE_FEATURE_COUNT = 6;

  /**
   * The meta object id for the '{@link repast.simphony.statecharts.scmodel.impl.AbstractStateImpl <em>Abstract State</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see repast.simphony.statecharts.scmodel.impl.AbstractStateImpl
   * @see repast.simphony.statecharts.scmodel.impl.StatechartPackageImpl#getAbstractState()
   * @generated
   */
  int ABSTRACT_STATE = 5;

  /**
   * The feature id for the '<em><b>Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ABSTRACT_STATE__ID = 0;

  /**
   * The feature id for the '<em><b>On Enter</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ABSTRACT_STATE__ON_ENTER = 1;

  /**
   * The feature id for the '<em><b>On Exit</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ABSTRACT_STATE__ON_EXIT = 2;

  /**
   * The feature id for the '<em><b>Language</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ABSTRACT_STATE__LANGUAGE = 3;

  /**
   * The number of structural features of the '<em>Abstract State</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ABSTRACT_STATE_FEATURE_COUNT = 4;

  /**
   * The meta object id for the '{@link repast.simphony.statecharts.scmodel.impl.StateImpl <em>State</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see repast.simphony.statecharts.scmodel.impl.StateImpl
   * @see repast.simphony.statecharts.scmodel.impl.StatechartPackageImpl#getState()
   * @generated
   */
  int STATE = 1;

  /**
   * The feature id for the '<em><b>Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STATE__ID = ABSTRACT_STATE__ID;

  /**
   * The feature id for the '<em><b>On Enter</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STATE__ON_ENTER = ABSTRACT_STATE__ON_ENTER;

  /**
   * The feature id for the '<em><b>On Exit</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STATE__ON_EXIT = ABSTRACT_STATE__ON_EXIT;

  /**
   * The feature id for the '<em><b>Language</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STATE__LANGUAGE = ABSTRACT_STATE__LANGUAGE;

  /**
   * The number of structural features of the '<em>State</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STATE_FEATURE_COUNT = ABSTRACT_STATE_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link repast.simphony.statecharts.scmodel.impl.FinalStateImpl <em>Final State</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see repast.simphony.statecharts.scmodel.impl.FinalStateImpl
   * @see repast.simphony.statecharts.scmodel.impl.StatechartPackageImpl#getFinalState()
   * @generated
   */
  int FINAL_STATE = 2;

  /**
   * The feature id for the '<em><b>Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FINAL_STATE__ID = STATE__ID;

  /**
   * The feature id for the '<em><b>On Enter</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FINAL_STATE__ON_ENTER = STATE__ON_ENTER;

  /**
   * The feature id for the '<em><b>On Exit</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FINAL_STATE__ON_EXIT = STATE__ON_EXIT;

  /**
   * The feature id for the '<em><b>Language</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FINAL_STATE__LANGUAGE = STATE__LANGUAGE;

  /**
   * The number of structural features of the '<em>Final State</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FINAL_STATE_FEATURE_COUNT = STATE_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link repast.simphony.statecharts.scmodel.impl.TransitionImpl <em>Transition</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see repast.simphony.statecharts.scmodel.impl.TransitionImpl
   * @see repast.simphony.statecharts.scmodel.impl.StatechartPackageImpl#getTransition()
   * @generated
   */
  int TRANSITION = 3;

  /**
   * The feature id for the '<em><b>From</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TRANSITION__FROM = 0;

  /**
   * The feature id for the '<em><b>To</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TRANSITION__TO = 1;

  /**
   * The feature id for the '<em><b>Priority</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TRANSITION__PRIORITY = 2;

  /**
   * The feature id for the '<em><b>On Transition</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TRANSITION__ON_TRANSITION = 3;

  /**
   * The feature id for the '<em><b>Out Of Branch</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TRANSITION__OUT_OF_BRANCH = 4;

  /**
   * The feature id for the '<em><b>Default Transition</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TRANSITION__DEFAULT_TRANSITION = 5;

  /**
   * The feature id for the '<em><b>Trigger Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TRANSITION__TRIGGER_TYPE = 6;

  /**
   * The feature id for the '<em><b>Trigger Time</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TRANSITION__TRIGGER_TIME = 7;

  /**
   * The feature id for the '<em><b>Trigger Condition</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TRANSITION__TRIGGER_CONDITION = 8;

  /**
   * The feature id for the '<em><b>Trigger Condition Language</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TRANSITION__TRIGGER_CONDITION_LANGUAGE = 9;

  /**
   * The feature id for the '<em><b>Message Checker Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TRANSITION__MESSAGE_CHECKER_TYPE = 10;

  /**
   * The feature id for the '<em><b>Message Checker Class</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TRANSITION__MESSAGE_CHECKER_CLASS = 11;

  /**
   * The feature id for the '<em><b>Message Checker Obj</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TRANSITION__MESSAGE_CHECKER_OBJ = 12;

  /**
   * The feature id for the '<em><b>Trigger Probability</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TRANSITION__TRIGGER_PROBABILITY = 13;

  /**
   * The feature id for the '<em><b>Message Checker Condition</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TRANSITION__MESSAGE_CHECKER_CONDITION = 14;

  /**
   * The feature id for the '<em><b>Message Checker Condition Language</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TRANSITION__MESSAGE_CHECKER_CONDITION_LANGUAGE = 15;

  /**
   * The number of structural features of the '<em>Transition</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TRANSITION_FEATURE_COUNT = 16;

  /**
   * The meta object id for the '{@link repast.simphony.statecharts.scmodel.impl.CompositeStateImpl <em>Composite State</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see repast.simphony.statecharts.scmodel.impl.CompositeStateImpl
   * @see repast.simphony.statecharts.scmodel.impl.StatechartPackageImpl#getCompositeState()
   * @generated
   */
  int COMPOSITE_STATE = 4;

  /**
   * The feature id for the '<em><b>Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMPOSITE_STATE__ID = ABSTRACT_STATE__ID;

  /**
   * The feature id for the '<em><b>On Enter</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMPOSITE_STATE__ON_ENTER = ABSTRACT_STATE__ON_ENTER;

  /**
   * The feature id for the '<em><b>On Exit</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMPOSITE_STATE__ON_EXIT = ABSTRACT_STATE__ON_EXIT;

  /**
   * The feature id for the '<em><b>Language</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMPOSITE_STATE__LANGUAGE = ABSTRACT_STATE__LANGUAGE;

  /**
   * The feature id for the '<em><b>Children</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMPOSITE_STATE__CHILDREN = ABSTRACT_STATE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Composite State</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMPOSITE_STATE_FEATURE_COUNT = ABSTRACT_STATE_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link repast.simphony.statecharts.scmodel.impl.PseudoStateImpl <em>Pseudo State</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see repast.simphony.statecharts.scmodel.impl.PseudoStateImpl
   * @see repast.simphony.statecharts.scmodel.impl.StatechartPackageImpl#getPseudoState()
   * @generated
   */
  int PSEUDO_STATE = 6;

  /**
   * The feature id for the '<em><b>Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PSEUDO_STATE__ID = ABSTRACT_STATE__ID;

  /**
   * The feature id for the '<em><b>On Enter</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PSEUDO_STATE__ON_ENTER = ABSTRACT_STATE__ON_ENTER;

  /**
   * The feature id for the '<em><b>On Exit</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PSEUDO_STATE__ON_EXIT = ABSTRACT_STATE__ON_EXIT;

  /**
   * The feature id for the '<em><b>Language</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PSEUDO_STATE__LANGUAGE = ABSTRACT_STATE__LANGUAGE;

  /**
   * The feature id for the '<em><b>Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PSEUDO_STATE__TYPE = ABSTRACT_STATE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Pseudo State</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PSEUDO_STATE_FEATURE_COUNT = ABSTRACT_STATE_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link repast.simphony.statecharts.scmodel.impl.HistoryImpl <em>History</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see repast.simphony.statecharts.scmodel.impl.HistoryImpl
   * @see repast.simphony.statecharts.scmodel.impl.StatechartPackageImpl#getHistory()
   * @generated
   */
  int HISTORY = 7;

  /**
   * The feature id for the '<em><b>Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int HISTORY__ID = STATE__ID;

  /**
   * The feature id for the '<em><b>On Enter</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int HISTORY__ON_ENTER = STATE__ON_ENTER;

  /**
   * The feature id for the '<em><b>On Exit</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int HISTORY__ON_EXIT = STATE__ON_EXIT;

  /**
   * The feature id for the '<em><b>Language</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int HISTORY__LANGUAGE = STATE__LANGUAGE;

  /**
   * The feature id for the '<em><b>Shallow</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int HISTORY__SHALLOW = STATE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>History</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int HISTORY_FEATURE_COUNT = STATE_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link repast.simphony.statecharts.scmodel.impl.EqualsCheckerImpl <em>Equals Checker</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see repast.simphony.statecharts.scmodel.impl.EqualsCheckerImpl
   * @see repast.simphony.statecharts.scmodel.impl.StatechartPackageImpl#getEqualsChecker()
   * @generated
   */
  int EQUALS_CHECKER = 8;

  /**
   * The feature id for the '<em><b>Obj</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int EQUALS_CHECKER__OBJ = 0;

  /**
   * The number of structural features of the '<em>Equals Checker</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int EQUALS_CHECKER_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link repast.simphony.statecharts.scmodel.PseudoStateTypes <em>Pseudo State Types</em>}' enum.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see repast.simphony.statecharts.scmodel.PseudoStateTypes
   * @see repast.simphony.statecharts.scmodel.impl.StatechartPackageImpl#getPseudoStateTypes()
   * @generated
   */
  int PSEUDO_STATE_TYPES = 9;

  /**
   * The meta object id for the '{@link repast.simphony.statecharts.scmodel.TriggerTypes <em>Trigger Types</em>}' enum.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see repast.simphony.statecharts.scmodel.TriggerTypes
   * @see repast.simphony.statecharts.scmodel.impl.StatechartPackageImpl#getTriggerTypes()
   * @generated
   */
  int TRIGGER_TYPES = 10;

  /**
   * The meta object id for the '{@link repast.simphony.statecharts.scmodel.MessageCheckerTypes <em>Message Checker Types</em>}' enum.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see repast.simphony.statecharts.scmodel.MessageCheckerTypes
   * @see repast.simphony.statecharts.scmodel.impl.StatechartPackageImpl#getMessageCheckerTypes()
   * @generated
   */
  int MESSAGE_CHECKER_TYPES = 11;

  /**
   * The meta object id for the '{@link repast.simphony.statecharts.scmodel.LanguageTypes <em>Language Types</em>}' enum.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see repast.simphony.statecharts.scmodel.LanguageTypes
   * @see repast.simphony.statecharts.scmodel.impl.StatechartPackageImpl#getLanguageTypes()
   * @generated
   */
  int LANGUAGE_TYPES = 12;


  /**
   * Returns the meta object for class '{@link repast.simphony.statecharts.scmodel.StateMachine <em>State Machine</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>State Machine</em>'.
   * @see repast.simphony.statecharts.scmodel.StateMachine
   * @generated
   */
  EClass getStateMachine();

  /**
   * Returns the meta object for the containment reference list '{@link repast.simphony.statecharts.scmodel.StateMachine#getStates <em>States</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>States</em>'.
   * @see repast.simphony.statecharts.scmodel.StateMachine#getStates()
   * @see #getStateMachine()
   * @generated
   */
  EReference getStateMachine_States();

  /**
   * Returns the meta object for the containment reference list '{@link repast.simphony.statecharts.scmodel.StateMachine#getTransitions <em>Transitions</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Transitions</em>'.
   * @see repast.simphony.statecharts.scmodel.StateMachine#getTransitions()
   * @see #getStateMachine()
   * @generated
   */
  EReference getStateMachine_Transitions();

  /**
   * Returns the meta object for the attribute '{@link repast.simphony.statecharts.scmodel.StateMachine#getAgentType <em>Agent Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Agent Type</em>'.
   * @see repast.simphony.statecharts.scmodel.StateMachine#getAgentType()
   * @see #getStateMachine()
   * @generated
   */
  EAttribute getStateMachine_AgentType();

  /**
   * Returns the meta object for the attribute '{@link repast.simphony.statecharts.scmodel.StateMachine#getPackage <em>Package</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Package</em>'.
   * @see repast.simphony.statecharts.scmodel.StateMachine#getPackage()
   * @see #getStateMachine()
   * @generated
   */
  EAttribute getStateMachine_Package();

  /**
   * Returns the meta object for the attribute '{@link repast.simphony.statecharts.scmodel.StateMachine#getClassName <em>Class Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Class Name</em>'.
   * @see repast.simphony.statecharts.scmodel.StateMachine#getClassName()
   * @see #getStateMachine()
   * @generated
   */
  EAttribute getStateMachine_ClassName();

  /**
   * Returns the meta object for the attribute '{@link repast.simphony.statecharts.scmodel.StateMachine#getLanguage <em>Language</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Language</em>'.
   * @see repast.simphony.statecharts.scmodel.StateMachine#getLanguage()
   * @see #getStateMachine()
   * @generated
   */
  EAttribute getStateMachine_Language();

  /**
   * Returns the meta object for class '{@link repast.simphony.statecharts.scmodel.State <em>State</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>State</em>'.
   * @see repast.simphony.statecharts.scmodel.State
   * @generated
   */
  EClass getState();

  /**
   * Returns the meta object for class '{@link repast.simphony.statecharts.scmodel.FinalState <em>Final State</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Final State</em>'.
   * @see repast.simphony.statecharts.scmodel.FinalState
   * @generated
   */
  EClass getFinalState();

  /**
   * Returns the meta object for class '{@link repast.simphony.statecharts.scmodel.Transition <em>Transition</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Transition</em>'.
   * @see repast.simphony.statecharts.scmodel.Transition
   * @generated
   */
  EClass getTransition();

  /**
   * Returns the meta object for the reference '{@link repast.simphony.statecharts.scmodel.Transition#getFrom <em>From</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>From</em>'.
   * @see repast.simphony.statecharts.scmodel.Transition#getFrom()
   * @see #getTransition()
   * @generated
   */
  EReference getTransition_From();

  /**
   * Returns the meta object for the reference '{@link repast.simphony.statecharts.scmodel.Transition#getTo <em>To</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>To</em>'.
   * @see repast.simphony.statecharts.scmodel.Transition#getTo()
   * @see #getTransition()
   * @generated
   */
  EReference getTransition_To();

  /**
   * Returns the meta object for the attribute '{@link repast.simphony.statecharts.scmodel.Transition#getPriority <em>Priority</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Priority</em>'.
   * @see repast.simphony.statecharts.scmodel.Transition#getPriority()
   * @see #getTransition()
   * @generated
   */
  EAttribute getTransition_Priority();

  /**
   * Returns the meta object for the attribute '{@link repast.simphony.statecharts.scmodel.Transition#getOnTransition <em>On Transition</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>On Transition</em>'.
   * @see repast.simphony.statecharts.scmodel.Transition#getOnTransition()
   * @see #getTransition()
   * @generated
   */
  EAttribute getTransition_OnTransition();

  /**
   * Returns the meta object for the attribute '{@link repast.simphony.statecharts.scmodel.Transition#isOutOfBranch <em>Out Of Branch</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Out Of Branch</em>'.
   * @see repast.simphony.statecharts.scmodel.Transition#isOutOfBranch()
   * @see #getTransition()
   * @generated
   */
  EAttribute getTransition_OutOfBranch();

  /**
   * Returns the meta object for the attribute '{@link repast.simphony.statecharts.scmodel.Transition#isDefaultTransition <em>Default Transition</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Default Transition</em>'.
   * @see repast.simphony.statecharts.scmodel.Transition#isDefaultTransition()
   * @see #getTransition()
   * @generated
   */
  EAttribute getTransition_DefaultTransition();

  /**
   * Returns the meta object for the attribute '{@link repast.simphony.statecharts.scmodel.Transition#getTriggerType <em>Trigger Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Trigger Type</em>'.
   * @see repast.simphony.statecharts.scmodel.Transition#getTriggerType()
   * @see #getTransition()
   * @generated
   */
  EAttribute getTransition_TriggerType();

  /**
   * Returns the meta object for the attribute '{@link repast.simphony.statecharts.scmodel.Transition#getTriggerTime <em>Trigger Time</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Trigger Time</em>'.
   * @see repast.simphony.statecharts.scmodel.Transition#getTriggerTime()
   * @see #getTransition()
   * @generated
   */
  EAttribute getTransition_TriggerTime();

  /**
   * Returns the meta object for the attribute '{@link repast.simphony.statecharts.scmodel.Transition#getTriggerCondition <em>Trigger Condition</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Trigger Condition</em>'.
   * @see repast.simphony.statecharts.scmodel.Transition#getTriggerCondition()
   * @see #getTransition()
   * @generated
   */
  EAttribute getTransition_TriggerCondition();

  /**
   * Returns the meta object for the attribute '{@link repast.simphony.statecharts.scmodel.Transition#getTriggerConditionLanguage <em>Trigger Condition Language</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Trigger Condition Language</em>'.
   * @see repast.simphony.statecharts.scmodel.Transition#getTriggerConditionLanguage()
   * @see #getTransition()
   * @generated
   */
  EAttribute getTransition_TriggerConditionLanguage();

  /**
   * Returns the meta object for the attribute '{@link repast.simphony.statecharts.scmodel.Transition#getMessageCheckerType <em>Message Checker Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Message Checker Type</em>'.
   * @see repast.simphony.statecharts.scmodel.Transition#getMessageCheckerType()
   * @see #getTransition()
   * @generated
   */
  EAttribute getTransition_MessageCheckerType();

  /**
   * Returns the meta object for the attribute '{@link repast.simphony.statecharts.scmodel.Transition#getMessageCheckerClass <em>Message Checker Class</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Message Checker Class</em>'.
   * @see repast.simphony.statecharts.scmodel.Transition#getMessageCheckerClass()
   * @see #getTransition()
   * @generated
   */
  EAttribute getTransition_MessageCheckerClass();

  /**
   * Returns the meta object for the attribute '{@link repast.simphony.statecharts.scmodel.Transition#getMessageCheckerObj <em>Message Checker Obj</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Message Checker Obj</em>'.
   * @see repast.simphony.statecharts.scmodel.Transition#getMessageCheckerObj()
   * @see #getTransition()
   * @generated
   */
  EAttribute getTransition_MessageCheckerObj();

  /**
   * Returns the meta object for the attribute '{@link repast.simphony.statecharts.scmodel.Transition#getTriggerProbability <em>Trigger Probability</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Trigger Probability</em>'.
   * @see repast.simphony.statecharts.scmodel.Transition#getTriggerProbability()
   * @see #getTransition()
   * @generated
   */
  EAttribute getTransition_TriggerProbability();

  /**
   * Returns the meta object for the attribute '{@link repast.simphony.statecharts.scmodel.Transition#getMessageCheckerCondition <em>Message Checker Condition</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Message Checker Condition</em>'.
   * @see repast.simphony.statecharts.scmodel.Transition#getMessageCheckerCondition()
   * @see #getTransition()
   * @generated
   */
  EAttribute getTransition_MessageCheckerCondition();

  /**
   * Returns the meta object for the attribute '{@link repast.simphony.statecharts.scmodel.Transition#getMessageCheckerConditionLanguage <em>Message Checker Condition Language</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Message Checker Condition Language</em>'.
   * @see repast.simphony.statecharts.scmodel.Transition#getMessageCheckerConditionLanguage()
   * @see #getTransition()
   * @generated
   */
  EAttribute getTransition_MessageCheckerConditionLanguage();

  /**
   * Returns the meta object for class '{@link repast.simphony.statecharts.scmodel.CompositeState <em>Composite State</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Composite State</em>'.
   * @see repast.simphony.statecharts.scmodel.CompositeState
   * @generated
   */
  EClass getCompositeState();

  /**
   * Returns the meta object for the containment reference list '{@link repast.simphony.statecharts.scmodel.CompositeState#getChildren <em>Children</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Children</em>'.
   * @see repast.simphony.statecharts.scmodel.CompositeState#getChildren()
   * @see #getCompositeState()
   * @generated
   */
  EReference getCompositeState_Children();

  /**
   * Returns the meta object for class '{@link repast.simphony.statecharts.scmodel.AbstractState <em>Abstract State</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Abstract State</em>'.
   * @see repast.simphony.statecharts.scmodel.AbstractState
   * @generated
   */
  EClass getAbstractState();

  /**
   * Returns the meta object for the attribute '{@link repast.simphony.statecharts.scmodel.AbstractState#getId <em>Id</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Id</em>'.
   * @see repast.simphony.statecharts.scmodel.AbstractState#getId()
   * @see #getAbstractState()
   * @generated
   */
  EAttribute getAbstractState_Id();

  /**
   * Returns the meta object for the attribute '{@link repast.simphony.statecharts.scmodel.AbstractState#getOnEnter <em>On Enter</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>On Enter</em>'.
   * @see repast.simphony.statecharts.scmodel.AbstractState#getOnEnter()
   * @see #getAbstractState()
   * @generated
   */
  EAttribute getAbstractState_OnEnter();

  /**
   * Returns the meta object for the attribute '{@link repast.simphony.statecharts.scmodel.AbstractState#getOnExit <em>On Exit</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>On Exit</em>'.
   * @see repast.simphony.statecharts.scmodel.AbstractState#getOnExit()
   * @see #getAbstractState()
   * @generated
   */
  EAttribute getAbstractState_OnExit();

  /**
   * Returns the meta object for the attribute '{@link repast.simphony.statecharts.scmodel.AbstractState#getLanguage <em>Language</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Language</em>'.
   * @see repast.simphony.statecharts.scmodel.AbstractState#getLanguage()
   * @see #getAbstractState()
   * @generated
   */
  EAttribute getAbstractState_Language();

  /**
   * Returns the meta object for class '{@link repast.simphony.statecharts.scmodel.PseudoState <em>Pseudo State</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Pseudo State</em>'.
   * @see repast.simphony.statecharts.scmodel.PseudoState
   * @generated
   */
  EClass getPseudoState();

  /**
   * Returns the meta object for the attribute '{@link repast.simphony.statecharts.scmodel.PseudoState#getType <em>Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Type</em>'.
   * @see repast.simphony.statecharts.scmodel.PseudoState#getType()
   * @see #getPseudoState()
   * @generated
   */
  EAttribute getPseudoState_Type();

  /**
   * Returns the meta object for class '{@link repast.simphony.statecharts.scmodel.History <em>History</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>History</em>'.
   * @see repast.simphony.statecharts.scmodel.History
   * @generated
   */
  EClass getHistory();

  /**
   * Returns the meta object for the attribute '{@link repast.simphony.statecharts.scmodel.History#isShallow <em>Shallow</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Shallow</em>'.
   * @see repast.simphony.statecharts.scmodel.History#isShallow()
   * @see #getHistory()
   * @generated
   */
  EAttribute getHistory_Shallow();

  /**
   * Returns the meta object for class '{@link repast.simphony.statecharts.scmodel.EqualsChecker <em>Equals Checker</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Equals Checker</em>'.
   * @see repast.simphony.statecharts.scmodel.EqualsChecker
   * @generated
   */
  EClass getEqualsChecker();

  /**
   * Returns the meta object for the attribute '{@link repast.simphony.statecharts.scmodel.EqualsChecker#getObj <em>Obj</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Obj</em>'.
   * @see repast.simphony.statecharts.scmodel.EqualsChecker#getObj()
   * @see #getEqualsChecker()
   * @generated
   */
  EAttribute getEqualsChecker_Obj();

  /**
   * Returns the meta object for enum '{@link repast.simphony.statecharts.scmodel.PseudoStateTypes <em>Pseudo State Types</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for enum '<em>Pseudo State Types</em>'.
   * @see repast.simphony.statecharts.scmodel.PseudoStateTypes
   * @generated
   */
  EEnum getPseudoStateTypes();

  /**
   * Returns the meta object for enum '{@link repast.simphony.statecharts.scmodel.TriggerTypes <em>Trigger Types</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for enum '<em>Trigger Types</em>'.
   * @see repast.simphony.statecharts.scmodel.TriggerTypes
   * @generated
   */
  EEnum getTriggerTypes();

  /**
   * Returns the meta object for enum '{@link repast.simphony.statecharts.scmodel.MessageCheckerTypes <em>Message Checker Types</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for enum '<em>Message Checker Types</em>'.
   * @see repast.simphony.statecharts.scmodel.MessageCheckerTypes
   * @generated
   */
  EEnum getMessageCheckerTypes();

  /**
   * Returns the meta object for enum '{@link repast.simphony.statecharts.scmodel.LanguageTypes <em>Language Types</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for enum '<em>Language Types</em>'.
   * @see repast.simphony.statecharts.scmodel.LanguageTypes
   * @generated
   */
  EEnum getLanguageTypes();

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
  StatechartFactory getStatechartFactory();

  /**
   * <!-- begin-user-doc -->
   * Defines literals for the meta objects that represent
   * <ul>
   *   <li>each class,</li>
   *   <li>each feature of each class,</li>
   *   <li>each enum,</li>
   *   <li>and each data type</li>
   * </ul>
   * <!-- end-user-doc -->
   * @generated
   */
  interface Literals {
    /**
     * The meta object literal for the '{@link repast.simphony.statecharts.scmodel.impl.StateMachineImpl <em>State Machine</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see repast.simphony.statecharts.scmodel.impl.StateMachineImpl
     * @see repast.simphony.statecharts.scmodel.impl.StatechartPackageImpl#getStateMachine()
     * @generated
     */
    EClass STATE_MACHINE = eINSTANCE.getStateMachine();

    /**
     * The meta object literal for the '<em><b>States</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference STATE_MACHINE__STATES = eINSTANCE.getStateMachine_States();

    /**
     * The meta object literal for the '<em><b>Transitions</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference STATE_MACHINE__TRANSITIONS = eINSTANCE.getStateMachine_Transitions();

    /**
     * The meta object literal for the '<em><b>Agent Type</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute STATE_MACHINE__AGENT_TYPE = eINSTANCE.getStateMachine_AgentType();

    /**
     * The meta object literal for the '<em><b>Package</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute STATE_MACHINE__PACKAGE = eINSTANCE.getStateMachine_Package();

    /**
     * The meta object literal for the '<em><b>Class Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute STATE_MACHINE__CLASS_NAME = eINSTANCE.getStateMachine_ClassName();

    /**
     * The meta object literal for the '<em><b>Language</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute STATE_MACHINE__LANGUAGE = eINSTANCE.getStateMachine_Language();

    /**
     * The meta object literal for the '{@link repast.simphony.statecharts.scmodel.impl.StateImpl <em>State</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see repast.simphony.statecharts.scmodel.impl.StateImpl
     * @see repast.simphony.statecharts.scmodel.impl.StatechartPackageImpl#getState()
     * @generated
     */
    EClass STATE = eINSTANCE.getState();

    /**
     * The meta object literal for the '{@link repast.simphony.statecharts.scmodel.impl.FinalStateImpl <em>Final State</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see repast.simphony.statecharts.scmodel.impl.FinalStateImpl
     * @see repast.simphony.statecharts.scmodel.impl.StatechartPackageImpl#getFinalState()
     * @generated
     */
    EClass FINAL_STATE = eINSTANCE.getFinalState();

    /**
     * The meta object literal for the '{@link repast.simphony.statecharts.scmodel.impl.TransitionImpl <em>Transition</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see repast.simphony.statecharts.scmodel.impl.TransitionImpl
     * @see repast.simphony.statecharts.scmodel.impl.StatechartPackageImpl#getTransition()
     * @generated
     */
    EClass TRANSITION = eINSTANCE.getTransition();

    /**
     * The meta object literal for the '<em><b>From</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TRANSITION__FROM = eINSTANCE.getTransition_From();

    /**
     * The meta object literal for the '<em><b>To</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TRANSITION__TO = eINSTANCE.getTransition_To();

    /**
     * The meta object literal for the '<em><b>Priority</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TRANSITION__PRIORITY = eINSTANCE.getTransition_Priority();

    /**
     * The meta object literal for the '<em><b>On Transition</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TRANSITION__ON_TRANSITION = eINSTANCE.getTransition_OnTransition();

    /**
     * The meta object literal for the '<em><b>Out Of Branch</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TRANSITION__OUT_OF_BRANCH = eINSTANCE.getTransition_OutOfBranch();

    /**
     * The meta object literal for the '<em><b>Default Transition</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TRANSITION__DEFAULT_TRANSITION = eINSTANCE.getTransition_DefaultTransition();

    /**
     * The meta object literal for the '<em><b>Trigger Type</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TRANSITION__TRIGGER_TYPE = eINSTANCE.getTransition_TriggerType();

    /**
     * The meta object literal for the '<em><b>Trigger Time</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TRANSITION__TRIGGER_TIME = eINSTANCE.getTransition_TriggerTime();

    /**
     * The meta object literal for the '<em><b>Trigger Condition</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TRANSITION__TRIGGER_CONDITION = eINSTANCE.getTransition_TriggerCondition();

    /**
     * The meta object literal for the '<em><b>Trigger Condition Language</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TRANSITION__TRIGGER_CONDITION_LANGUAGE = eINSTANCE.getTransition_TriggerConditionLanguage();

    /**
     * The meta object literal for the '<em><b>Message Checker Type</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TRANSITION__MESSAGE_CHECKER_TYPE = eINSTANCE.getTransition_MessageCheckerType();

    /**
     * The meta object literal for the '<em><b>Message Checker Class</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TRANSITION__MESSAGE_CHECKER_CLASS = eINSTANCE.getTransition_MessageCheckerClass();

    /**
     * The meta object literal for the '<em><b>Message Checker Obj</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TRANSITION__MESSAGE_CHECKER_OBJ = eINSTANCE.getTransition_MessageCheckerObj();

    /**
     * The meta object literal for the '<em><b>Trigger Probability</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TRANSITION__TRIGGER_PROBABILITY = eINSTANCE.getTransition_TriggerProbability();

    /**
     * The meta object literal for the '<em><b>Message Checker Condition</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TRANSITION__MESSAGE_CHECKER_CONDITION = eINSTANCE.getTransition_MessageCheckerCondition();

    /**
     * The meta object literal for the '<em><b>Message Checker Condition Language</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TRANSITION__MESSAGE_CHECKER_CONDITION_LANGUAGE = eINSTANCE.getTransition_MessageCheckerConditionLanguage();

    /**
     * The meta object literal for the '{@link repast.simphony.statecharts.scmodel.impl.CompositeStateImpl <em>Composite State</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see repast.simphony.statecharts.scmodel.impl.CompositeStateImpl
     * @see repast.simphony.statecharts.scmodel.impl.StatechartPackageImpl#getCompositeState()
     * @generated
     */
    EClass COMPOSITE_STATE = eINSTANCE.getCompositeState();

    /**
     * The meta object literal for the '<em><b>Children</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference COMPOSITE_STATE__CHILDREN = eINSTANCE.getCompositeState_Children();

    /**
     * The meta object literal for the '{@link repast.simphony.statecharts.scmodel.impl.AbstractStateImpl <em>Abstract State</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see repast.simphony.statecharts.scmodel.impl.AbstractStateImpl
     * @see repast.simphony.statecharts.scmodel.impl.StatechartPackageImpl#getAbstractState()
     * @generated
     */
    EClass ABSTRACT_STATE = eINSTANCE.getAbstractState();

    /**
     * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ABSTRACT_STATE__ID = eINSTANCE.getAbstractState_Id();

    /**
     * The meta object literal for the '<em><b>On Enter</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ABSTRACT_STATE__ON_ENTER = eINSTANCE.getAbstractState_OnEnter();

    /**
     * The meta object literal for the '<em><b>On Exit</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ABSTRACT_STATE__ON_EXIT = eINSTANCE.getAbstractState_OnExit();

    /**
     * The meta object literal for the '<em><b>Language</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ABSTRACT_STATE__LANGUAGE = eINSTANCE.getAbstractState_Language();

    /**
     * The meta object literal for the '{@link repast.simphony.statecharts.scmodel.impl.PseudoStateImpl <em>Pseudo State</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see repast.simphony.statecharts.scmodel.impl.PseudoStateImpl
     * @see repast.simphony.statecharts.scmodel.impl.StatechartPackageImpl#getPseudoState()
     * @generated
     */
    EClass PSEUDO_STATE = eINSTANCE.getPseudoState();

    /**
     * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PSEUDO_STATE__TYPE = eINSTANCE.getPseudoState_Type();

    /**
     * The meta object literal for the '{@link repast.simphony.statecharts.scmodel.impl.HistoryImpl <em>History</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see repast.simphony.statecharts.scmodel.impl.HistoryImpl
     * @see repast.simphony.statecharts.scmodel.impl.StatechartPackageImpl#getHistory()
     * @generated
     */
    EClass HISTORY = eINSTANCE.getHistory();

    /**
     * The meta object literal for the '<em><b>Shallow</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute HISTORY__SHALLOW = eINSTANCE.getHistory_Shallow();

    /**
     * The meta object literal for the '{@link repast.simphony.statecharts.scmodel.impl.EqualsCheckerImpl <em>Equals Checker</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see repast.simphony.statecharts.scmodel.impl.EqualsCheckerImpl
     * @see repast.simphony.statecharts.scmodel.impl.StatechartPackageImpl#getEqualsChecker()
     * @generated
     */
    EClass EQUALS_CHECKER = eINSTANCE.getEqualsChecker();

    /**
     * The meta object literal for the '<em><b>Obj</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute EQUALS_CHECKER__OBJ = eINSTANCE.getEqualsChecker_Obj();

    /**
     * The meta object literal for the '{@link repast.simphony.statecharts.scmodel.PseudoStateTypes <em>Pseudo State Types</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see repast.simphony.statecharts.scmodel.PseudoStateTypes
     * @see repast.simphony.statecharts.scmodel.impl.StatechartPackageImpl#getPseudoStateTypes()
     * @generated
     */
    EEnum PSEUDO_STATE_TYPES = eINSTANCE.getPseudoStateTypes();

    /**
     * The meta object literal for the '{@link repast.simphony.statecharts.scmodel.TriggerTypes <em>Trigger Types</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see repast.simphony.statecharts.scmodel.TriggerTypes
     * @see repast.simphony.statecharts.scmodel.impl.StatechartPackageImpl#getTriggerTypes()
     * @generated
     */
    EEnum TRIGGER_TYPES = eINSTANCE.getTriggerTypes();

    /**
     * The meta object literal for the '{@link repast.simphony.statecharts.scmodel.MessageCheckerTypes <em>Message Checker Types</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see repast.simphony.statecharts.scmodel.MessageCheckerTypes
     * @see repast.simphony.statecharts.scmodel.impl.StatechartPackageImpl#getMessageCheckerTypes()
     * @generated
     */
    EEnum MESSAGE_CHECKER_TYPES = eINSTANCE.getMessageCheckerTypes();

    /**
     * The meta object literal for the '{@link repast.simphony.statecharts.scmodel.LanguageTypes <em>Language Types</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see repast.simphony.statecharts.scmodel.LanguageTypes
     * @see repast.simphony.statecharts.scmodel.impl.StatechartPackageImpl#getLanguageTypes()
     * @generated
     */
    EEnum LANGUAGE_TYPES = eINSTANCE.getLanguageTypes();

  }

} //StatechartPackage
