/**
 */
package repast.simphony.systemdynamics.sdmodel;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>System Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link repast.simphony.systemdynamics.sdmodel.SystemModel#getLinks <em>Links</em>}</li>
 *   <li>{@link repast.simphony.systemdynamics.sdmodel.SystemModel#getVariables <em>Variables</em>}</li>
 *   <li>{@link repast.simphony.systemdynamics.sdmodel.SystemModel#getStartTime <em>Start Time</em>}</li>
 *   <li>{@link repast.simphony.systemdynamics.sdmodel.SystemModel#getEndTime <em>End Time</em>}</li>
 *   <li>{@link repast.simphony.systemdynamics.sdmodel.SystemModel#getTimeStep <em>Time Step</em>}</li>
 *   <li>{@link repast.simphony.systemdynamics.sdmodel.SystemModel#getUnits <em>Units</em>}</li>
 *   <li>{@link repast.simphony.systemdynamics.sdmodel.SystemModel#getReportingInterval <em>Reporting Interval</em>}</li>
 *   <li>{@link repast.simphony.systemdynamics.sdmodel.SystemModel#getSubscripts <em>Subscripts</em>}</li>
 * </ul>
 * </p>
 *
 * @see repast.simphony.systemdynamics.sdmodel.SDModelPackage#getSystemModel()
 * @model
 * @generated
 */
public interface SystemModel extends EObject {
  /**
   * Returns the value of the '<em><b>Links</b></em>' containment reference list.
   * The list contents are of type {@link repast.simphony.systemdynamics.sdmodel.InfluenceLink}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Links</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Links</em>' containment reference list.
   * @see repast.simphony.systemdynamics.sdmodel.SDModelPackage#getSystemModel_Links()
   * @model containment="true"
   * @generated
   */
  EList<InfluenceLink> getLinks();

  /**
   * Returns the value of the '<em><b>Variables</b></em>' containment reference list.
   * The list contents are of type {@link repast.simphony.systemdynamics.sdmodel.Variable}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Variables</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Variables</em>' containment reference list.
   * @see repast.simphony.systemdynamics.sdmodel.SDModelPackage#getSystemModel_Variables()
   * @model containment="true"
   * @generated
   */
  EList<Variable> getVariables();

  /**
   * Returns the value of the '<em><b>Start Time</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Start Time</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Start Time</em>' attribute.
   * @see #setStartTime(double)
   * @see repast.simphony.systemdynamics.sdmodel.SDModelPackage#getSystemModel_StartTime()
   * @model
   * @generated
   */
  double getStartTime();

  /**
   * Sets the value of the '{@link repast.simphony.systemdynamics.sdmodel.SystemModel#getStartTime <em>Start Time</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Start Time</em>' attribute.
   * @see #getStartTime()
   * @generated
   */
  void setStartTime(double value);

  /**
   * Returns the value of the '<em><b>End Time</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>End Time</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>End Time</em>' attribute.
   * @see #setEndTime(double)
   * @see repast.simphony.systemdynamics.sdmodel.SDModelPackage#getSystemModel_EndTime()
   * @model
   * @generated
   */
  double getEndTime();

  /**
   * Sets the value of the '{@link repast.simphony.systemdynamics.sdmodel.SystemModel#getEndTime <em>End Time</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>End Time</em>' attribute.
   * @see #getEndTime()
   * @generated
   */
  void setEndTime(double value);

  /**
   * Returns the value of the '<em><b>Time Step</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Time Step</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Time Step</em>' attribute.
   * @see #setTimeStep(double)
   * @see repast.simphony.systemdynamics.sdmodel.SDModelPackage#getSystemModel_TimeStep()
   * @model
   * @generated
   */
  double getTimeStep();

  /**
   * Sets the value of the '{@link repast.simphony.systemdynamics.sdmodel.SystemModel#getTimeStep <em>Time Step</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Time Step</em>' attribute.
   * @see #getTimeStep()
   * @generated
   */
  void setTimeStep(double value);

  /**
   * Returns the value of the '<em><b>Units</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Units</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Units</em>' attribute.
   * @see #setUnits(String)
   * @see repast.simphony.systemdynamics.sdmodel.SDModelPackage#getSystemModel_Units()
   * @model
   * @generated
   */
  String getUnits();

  /**
   * Sets the value of the '{@link repast.simphony.systemdynamics.sdmodel.SystemModel#getUnits <em>Units</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Units</em>' attribute.
   * @see #getUnits()
   * @generated
   */
  void setUnits(String value);

  /**
   * Returns the value of the '<em><b>Reporting Interval</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Reporting Interval</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Reporting Interval</em>' attribute.
   * @see #setReportingInterval(double)
   * @see repast.simphony.systemdynamics.sdmodel.SDModelPackage#getSystemModel_ReportingInterval()
   * @model
   * @generated
   */
  double getReportingInterval();

  /**
   * Sets the value of the '{@link repast.simphony.systemdynamics.sdmodel.SystemModel#getReportingInterval <em>Reporting Interval</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Reporting Interval</em>' attribute.
   * @see #getReportingInterval()
   * @generated
   */
  void setReportingInterval(double value);

  /**
   * Returns the value of the '<em><b>Subscripts</b></em>' containment reference list.
   * The list contents are of type {@link repast.simphony.systemdynamics.sdmodel.Subscript}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Subscripts</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Subscripts</em>' containment reference list.
   * @see repast.simphony.systemdynamics.sdmodel.SDModelPackage#getSystemModel_Subscripts()
   * @model containment="true"
   * @generated
   */
  EList<Subscript> getSubscripts();

} // SystemModel
