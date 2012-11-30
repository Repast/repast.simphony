/**
 */
package repast.simphony.statecharts.scmodel.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import repast.simphony.statecharts.scmodel.ProbabilityTrigger;
import repast.simphony.statecharts.scmodel.StatechartPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Probability Trigger</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link repast.simphony.statecharts.scmodel.impl.ProbabilityTriggerImpl#getProbability <em>Probability</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ProbabilityTriggerImpl extends DefaultTriggerImpl implements ProbabilityTrigger {
  /**
   * The default value of the '{@link #getProbability() <em>Probability</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getProbability()
   * @generated
   * @ordered
   */
  protected static final double PROBABILITY_EDEFAULT = 0.0;

  /**
   * The cached value of the '{@link #getProbability() <em>Probability</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getProbability()
   * @generated
   * @ordered
   */
  protected double probability = PROBABILITY_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ProbabilityTriggerImpl() {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass() {
    return StatechartPackage.Literals.PROBABILITY_TRIGGER;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public double getProbability() {
    return probability;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setProbability(double newProbability) {
    double oldProbability = probability;
    probability = newProbability;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StatechartPackage.PROBABILITY_TRIGGER__PROBABILITY, oldProbability, probability));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType) {
    switch (featureID) {
      case StatechartPackage.PROBABILITY_TRIGGER__PROBABILITY:
        return getProbability();
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
      case StatechartPackage.PROBABILITY_TRIGGER__PROBABILITY:
        setProbability((Double)newValue);
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
      case StatechartPackage.PROBABILITY_TRIGGER__PROBABILITY:
        setProbability(PROBABILITY_EDEFAULT);
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
      case StatechartPackage.PROBABILITY_TRIGGER__PROBABILITY:
        return probability != PROBABILITY_EDEFAULT;
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
    result.append(" (probability: ");
    result.append(probability);
    result.append(')');
    return result.toString();
  }

} //ProbabilityTriggerImpl
