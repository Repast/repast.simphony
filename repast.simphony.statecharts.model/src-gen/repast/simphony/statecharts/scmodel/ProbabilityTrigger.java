/**
 */
package repast.simphony.statecharts.scmodel;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Probability Trigger</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link repast.simphony.statecharts.scmodel.ProbabilityTrigger#getProbability <em>Probability</em>}</li>
 * </ul>
 * </p>
 *
 * @see repast.simphony.statecharts.scmodel.StatechartPackage#getProbabilityTrigger()
 * @model
 * @generated
 */
public interface ProbabilityTrigger extends DefaultTrigger {
  /**
   * Returns the value of the '<em><b>Probability</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Probability</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Probability</em>' attribute.
   * @see #setProbability(double)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getProbabilityTrigger_Probability()
   * @model
   * @generated
   */
  double getProbability();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.ProbabilityTrigger#getProbability <em>Probability</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Probability</em>' attribute.
   * @see #getProbability()
   * @generated
   */
  void setProbability(double value);

} // ProbabilityTrigger
