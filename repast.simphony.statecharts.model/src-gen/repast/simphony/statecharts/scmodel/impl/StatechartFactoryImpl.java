/**
 */
package repast.simphony.statecharts.scmodel.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import repast.simphony.statecharts.scmodel.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class StatechartFactoryImpl extends EFactoryImpl implements StatechartFactory {
  /**
   * Creates the default factory implementation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static StatechartFactory init() {
    try {
      StatechartFactory theStatechartFactory = (StatechartFactory)EPackage.Registry.INSTANCE.getEFactory(StatechartPackage.eNS_URI);
      if (theStatechartFactory != null) {
        return theStatechartFactory;
      }
    }
    catch (Exception exception) {
      EcorePlugin.INSTANCE.log(exception);
    }
    return new StatechartFactoryImpl();
  }

  /**
   * Creates an instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public StatechartFactoryImpl() {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EObject create(EClass eClass) {
    switch (eClass.getClassifierID()) {
      case StatechartPackage.STATE_MACHINE: return createStateMachine();
      case StatechartPackage.STATE: return createState();
      case StatechartPackage.FINAL_STATE: return createFinalState();
      case StatechartPackage.TRANSITION: return createTransition();
      case StatechartPackage.COMPOSITE_STATE: return createCompositeState();
      case StatechartPackage.PSEUDO_STATE: return createPseudoState();
      case StatechartPackage.HISTORY: return createHistory();
      default:
        throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object createFromString(EDataType eDataType, String initialValue) {
    switch (eDataType.getClassifierID()) {
      case StatechartPackage.PSEUDO_STATE_TYPES:
        return createPseudoStateTypesFromString(eDataType, initialValue);
      case StatechartPackage.TRIGGER_TYPES:
        return createTriggerTypesFromString(eDataType, initialValue);
      case StatechartPackage.MESSAGE_CHECKER_TYPES:
        return createMessageCheckerTypesFromString(eDataType, initialValue);
      case StatechartPackage.LANGUAGE_TYPES:
        return createLanguageTypesFromString(eDataType, initialValue);
      default:
        throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String convertToString(EDataType eDataType, Object instanceValue) {
    switch (eDataType.getClassifierID()) {
      case StatechartPackage.PSEUDO_STATE_TYPES:
        return convertPseudoStateTypesToString(eDataType, instanceValue);
      case StatechartPackage.TRIGGER_TYPES:
        return convertTriggerTypesToString(eDataType, instanceValue);
      case StatechartPackage.MESSAGE_CHECKER_TYPES:
        return convertMessageCheckerTypesToString(eDataType, instanceValue);
      case StatechartPackage.LANGUAGE_TYPES:
        return convertLanguageTypesToString(eDataType, instanceValue);
      default:
        throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public StateMachine createStateMachine() {
    StateMachineImpl stateMachine = new StateMachineImpl();
    return stateMachine;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public State createState() {
    StateImpl state = new StateImpl();
    return state;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public FinalState createFinalState() {
    FinalStateImpl finalState = new FinalStateImpl();
    return finalState;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Transition createTransition() {
    TransitionImpl transition = new TransitionImpl();
    return transition;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public CompositeState createCompositeState() {
    CompositeStateImpl compositeState = new CompositeStateImpl();
    return compositeState;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public PseudoState createPseudoState() {
    PseudoStateImpl pseudoState = new PseudoStateImpl();
    return pseudoState;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public History createHistory() {
    HistoryImpl history = new HistoryImpl();
    return history;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public PseudoStateTypes createPseudoStateTypesFromString(EDataType eDataType, String initialValue) {
    PseudoStateTypes result = PseudoStateTypes.get(initialValue);
    if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertPseudoStateTypesToString(EDataType eDataType, Object instanceValue) {
    return instanceValue == null ? null : instanceValue.toString();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TriggerTypes createTriggerTypesFromString(EDataType eDataType, String initialValue) {
    TriggerTypes result = TriggerTypes.get(initialValue);
    if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertTriggerTypesToString(EDataType eDataType, Object instanceValue) {
    return instanceValue == null ? null : instanceValue.toString();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MessageCheckerTypes createMessageCheckerTypesFromString(EDataType eDataType, String initialValue) {
    MessageCheckerTypes result = MessageCheckerTypes.get(initialValue);
    if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertMessageCheckerTypesToString(EDataType eDataType, Object instanceValue) {
    return instanceValue == null ? null : instanceValue.toString();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public LanguageTypes createLanguageTypesFromString(EDataType eDataType, String initialValue) {
    LanguageTypes result = LanguageTypes.get(initialValue);
    if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertLanguageTypesToString(EDataType eDataType, Object instanceValue) {
    return instanceValue == null ? null : instanceValue.toString();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public StatechartPackage getStatechartPackage() {
    return (StatechartPackage)getEPackage();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
  @Deprecated
  public static StatechartPackage getPackage() {
    return StatechartPackage.eINSTANCE;
  }

} //StatechartFactoryImpl
