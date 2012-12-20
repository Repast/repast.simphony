/**
 */
package repast.simphony.systemdynamics.sdmodel.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

import repast.simphony.systemdynamics.sdmodel.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see repast.simphony.systemdynamics.sdmodel.SDModelPackage
 * @generated
 */
public class SDModelAdapterFactory extends AdapterFactoryImpl {
  /**
   * The cached model package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static SDModelPackage modelPackage;

  /**
   * Creates an instance of the adapter factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SDModelAdapterFactory() {
    if (modelPackage == null) {
      modelPackage = SDModelPackage.eINSTANCE;
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
  protected SDModelSwitch<Adapter> modelSwitch =
    new SDModelSwitch<Adapter>() {
      @Override
      public Adapter caseSystemModel(SystemModel object) {
        return createSystemModelAdapter();
      }
      @Override
      public Adapter caseCausalLink(CausalLink object) {
        return createCausalLinkAdapter();
      }
      @Override
      public Adapter caseAbstractVariable(AbstractVariable object) {
        return createAbstractVariableAdapter();
      }
      @Override
      public Adapter caseVariable(Variable object) {
        return createVariableAdapter();
      }
      @Override
      public Adapter caseCloud(Cloud object) {
        return createCloudAdapter();
      }
      @Override
      public Adapter caseStock(Stock object) {
        return createStockAdapter();
      }
      @Override
      public Adapter caseRate(Rate object) {
        return createRateAdapter();
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
   * Creates a new adapter for an object of class '{@link repast.simphony.systemdynamics.sdmodel.SystemModel <em>System Model</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see repast.simphony.systemdynamics.sdmodel.SystemModel
   * @generated
   */
  public Adapter createSystemModelAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link repast.simphony.systemdynamics.sdmodel.CausalLink <em>Causal Link</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see repast.simphony.systemdynamics.sdmodel.CausalLink
   * @generated
   */
  public Adapter createCausalLinkAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link repast.simphony.systemdynamics.sdmodel.AbstractVariable <em>Abstract Variable</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see repast.simphony.systemdynamics.sdmodel.AbstractVariable
   * @generated
   */
  public Adapter createAbstractVariableAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link repast.simphony.systemdynamics.sdmodel.Variable <em>Variable</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see repast.simphony.systemdynamics.sdmodel.Variable
   * @generated
   */
  public Adapter createVariableAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link repast.simphony.systemdynamics.sdmodel.Cloud <em>Cloud</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see repast.simphony.systemdynamics.sdmodel.Cloud
   * @generated
   */
  public Adapter createCloudAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link repast.simphony.systemdynamics.sdmodel.Stock <em>Stock</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see repast.simphony.systemdynamics.sdmodel.Stock
   * @generated
   */
  public Adapter createStockAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link repast.simphony.systemdynamics.sdmodel.Rate <em>Rate</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see repast.simphony.systemdynamics.sdmodel.Rate
   * @generated
   */
  public Adapter createRateAdapter() {
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

} //SDModelAdapterFactory
