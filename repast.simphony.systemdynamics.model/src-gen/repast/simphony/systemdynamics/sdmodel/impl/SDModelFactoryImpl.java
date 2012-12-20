/**
 */
package repast.simphony.systemdynamics.sdmodel.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import repast.simphony.systemdynamics.sdmodel.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class SDModelFactoryImpl extends EFactoryImpl implements SDModelFactory {
  /**
   * Creates the default factory implementation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static SDModelFactory init() {
    try {
      SDModelFactory theSDModelFactory = (SDModelFactory)EPackage.Registry.INSTANCE.getEFactory("http://repast.sf.net/systemdynamics"); 
      if (theSDModelFactory != null) {
        return theSDModelFactory;
      }
    }
    catch (Exception exception) {
      EcorePlugin.INSTANCE.log(exception);
    }
    return new SDModelFactoryImpl();
  }

  /**
   * Creates an instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SDModelFactoryImpl() {
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
      case SDModelPackage.SYSTEM_MODEL: return createSystemModel();
      case SDModelPackage.CAUSAL_LINK: return createCausalLink();
      case SDModelPackage.VARIABLE: return createVariable();
      case SDModelPackage.CLOUD: return createCloud();
      case SDModelPackage.STOCK: return createStock();
      case SDModelPackage.RATE: return createRate();
      default:
        throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SystemModel createSystemModel() {
    SystemModelImpl systemModel = new SystemModelImpl();
    return systemModel;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public CausalLink createCausalLink() {
    CausalLinkImpl causalLink = new CausalLinkImpl();
    return causalLink;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Variable createVariable() {
    VariableImpl variable = new VariableImpl();
    return variable;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Cloud createCloud() {
    CloudImpl cloud = new CloudImpl();
    return cloud;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Stock createStock() {
    StockImpl stock = new StockImpl();
    return stock;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Rate createRate() {
    RateImpl rate = new RateImpl();
    return rate;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SDModelPackage getSDModelPackage() {
    return (SDModelPackage)getEPackage();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
  @Deprecated
  public static SDModelPackage getPackage() {
    return SDModelPackage.eINSTANCE;
  }

} //SDModelFactoryImpl
