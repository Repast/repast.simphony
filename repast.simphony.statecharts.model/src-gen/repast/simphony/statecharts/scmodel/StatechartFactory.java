/**
 */
package repast.simphony.statecharts.scmodel;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see repast.simphony.statecharts.scmodel.StatechartPackage
 * @generated
 */
public interface StatechartFactory extends EFactory {
  /**
   * The singleton instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  StatechartFactory eINSTANCE = repast.simphony.statecharts.scmodel.impl.StatechartFactoryImpl.init();

  /**
   * Returns a new object of class '<em>State Machine</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>State Machine</em>'.
   * @generated
   */
  StateMachine createStateMachine();

  /**
   * Returns a new object of class '<em>State</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>State</em>'.
   * @generated
   */
  State createState();

  /**
   * Returns a new object of class '<em>Final State</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Final State</em>'.
   * @generated
   */
  FinalState createFinalState();

  /**
   * Returns a new object of class '<em>Transition</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Transition</em>'.
   * @generated
   */
  Transition createTransition();

  /**
   * Returns a new object of class '<em>Composite State</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Composite State</em>'.
   * @generated
   */
  CompositeState createCompositeState();

  /**
   * Returns a new object of class '<em>Pseudo State</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Pseudo State</em>'.
   * @generated
   */
  PseudoState createPseudoState();

  /**
   * Returns a new object of class '<em>History</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>History</em>'.
   * @generated
   */
  History createHistory();

  /**
   * Returns the package supported by this factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the package supported by this factory.
   * @generated
   */
  StatechartPackage getStatechartPackage();

} //StatechartFactory
