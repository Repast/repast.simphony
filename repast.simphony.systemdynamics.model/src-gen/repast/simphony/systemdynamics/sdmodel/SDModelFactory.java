/**
 */
package repast.simphony.systemdynamics.sdmodel;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see repast.simphony.systemdynamics.sdmodel.SDModelPackage
 * @generated
 */
public interface SDModelFactory extends EFactory {
  /**
   * The singleton instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  SDModelFactory eINSTANCE = repast.simphony.systemdynamics.sdmodel.impl.SDModelFactoryImpl.init();

  /**
   * Returns a new object of class '<em>System Model</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>System Model</em>'.
   * @generated
   */
  SystemModel createSystemModel();

  /**
   * Returns a new object of class '<em>Causal Link</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Causal Link</em>'.
   * @generated
   */
  CausalLink createCausalLink();

  /**
   * Returns a new object of class '<em>Variable</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Variable</em>'.
   * @generated
   */
  Variable createVariable();

  /**
   * Returns a new object of class '<em>Cloud</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Cloud</em>'.
   * @generated
   */
  Cloud createCloud();

  /**
   * Returns a new object of class '<em>Stock</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Stock</em>'.
   * @generated
   */
  Stock createStock();

  /**
   * Returns a new object of class '<em>Rate</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Rate</em>'.
   * @generated
   */
  Rate createRate();

  /**
   * Returns the package supported by this factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the package supported by this factory.
   * @generated
   */
  SDModelPackage getSDModelPackage();

} //SDModelFactory
