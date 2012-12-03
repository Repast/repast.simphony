/**
 */
package repast.simphony.statecharts.scmodel.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

import repast.simphony.statecharts.scmodel.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see repast.simphony.statecharts.scmodel.StatechartPackage
 * @generated
 */
public class StatechartAdapterFactory extends AdapterFactoryImpl {
  /**
   * The cached model package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static StatechartPackage modelPackage;

  /**
   * Creates an instance of the adapter factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public StatechartAdapterFactory() {
    if (modelPackage == null) {
      modelPackage = StatechartPackage.eINSTANCE;
    }
  }

  /**
   * Returns whether this factory is applicable for the type of the object.
   * <!-- begin-user-doc -->
   * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
   * <!-- end-user-doc -->
   * @return whether this factory is applicable for the type of the object.
   * @generated
   */
  @Override
  public boolean isFactoryForType(Object object) {
    if (object == modelPackage) {
      return true;
    }
    if (object instanceof EObject) {
      return ((EObject)object).eClass().getEPackage() == modelPackage;
    }
    return false;
  }

  /**
   * The switch that delegates to the <code>createXXX</code> methods.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected StatechartSwitch<Adapter> modelSwitch =
    new StatechartSwitch<Adapter>() {
      @Override
      public Adapter caseStateMachine(StateMachine object) {
        return createStateMachineAdapter();
      }
      @Override
      public Adapter caseState(State object) {
        return createStateAdapter();
      }
      @Override
      public Adapter caseFinalState(FinalState object) {
        return createFinalStateAdapter();
      }
      @Override
      public Adapter caseTransition(Transition object) {
        return createTransitionAdapter();
      }
      @Override
      public Adapter caseCompositeState(CompositeState object) {
        return createCompositeStateAdapter();
      }
      @Override
      public Adapter caseAbstractState(AbstractState object) {
        return createAbstractStateAdapter();
      }
      @Override
      public Adapter casePseudoState(PseudoState object) {
        return createPseudoStateAdapter();
      }
      @Override
      public Adapter caseHistory(History object) {
        return createHistoryAdapter();
      }
      @Override
      public Adapter defaultCase(EObject object) {
        return createEObjectAdapter();
      }
    };

  /**
   * Creates an adapter for the <code>target</code>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param target the object to adapt.
   * @return the adapter for the <code>target</code>.
   * @generated
   */
  @Override
  public Adapter createAdapter(Notifier target) {
    return modelSwitch.doSwitch((EObject)target);
  }


  /**
   * Creates a new adapter for an object of class '{@link repast.simphony.statecharts.scmodel.StateMachine <em>State Machine</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see repast.simphony.statecharts.scmodel.StateMachine
   * @generated
   */
  public Adapter createStateMachineAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link repast.simphony.statecharts.scmodel.State <em>State</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see repast.simphony.statecharts.scmodel.State
   * @generated
   */
  public Adapter createStateAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link repast.simphony.statecharts.scmodel.FinalState <em>Final State</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see repast.simphony.statecharts.scmodel.FinalState
   * @generated
   */
  public Adapter createFinalStateAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link repast.simphony.statecharts.scmodel.Transition <em>Transition</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see repast.simphony.statecharts.scmodel.Transition
   * @generated
   */
  public Adapter createTransitionAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link repast.simphony.statecharts.scmodel.CompositeState <em>Composite State</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see repast.simphony.statecharts.scmodel.CompositeState
   * @generated
   */
  public Adapter createCompositeStateAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link repast.simphony.statecharts.scmodel.AbstractState <em>Abstract State</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see repast.simphony.statecharts.scmodel.AbstractState
   * @generated
   */
  public Adapter createAbstractStateAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link repast.simphony.statecharts.scmodel.PseudoState <em>Pseudo State</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see repast.simphony.statecharts.scmodel.PseudoState
   * @generated
   */
  public Adapter createPseudoStateAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link repast.simphony.statecharts.scmodel.History <em>History</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see repast.simphony.statecharts.scmodel.History
   * @generated
   */
  public Adapter createHistoryAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for the default case.
   * <!-- begin-user-doc -->
   * This default implementation returns null.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @generated
   */
  public Adapter createEObjectAdapter() {
    return null;
  }

} //StatechartAdapterFactory
