/**
 */
package repast.simphony.systemdynamics.sdmodel.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import repast.simphony.systemdynamics.sdmodel.InfluenceLink;
import repast.simphony.systemdynamics.sdmodel.SDModelPackage;
import repast.simphony.systemdynamics.sdmodel.SystemModel;
import repast.simphony.systemdynamics.sdmodel.Variable;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>System Model</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link repast.simphony.systemdynamics.sdmodel.impl.SystemModelImpl#getLinks <em>Links</em>}</li>
 *   <li>{@link repast.simphony.systemdynamics.sdmodel.impl.SystemModelImpl#getVariables <em>Variables</em>}</li>
 *   <li>{@link repast.simphony.systemdynamics.sdmodel.impl.SystemModelImpl#getStartTime <em>Start Time</em>}</li>
 *   <li>{@link repast.simphony.systemdynamics.sdmodel.impl.SystemModelImpl#getEndTime <em>End Time</em>}</li>
 *   <li>{@link repast.simphony.systemdynamics.sdmodel.impl.SystemModelImpl#getTimeStep <em>Time Step</em>}</li>
 *   <li>{@link repast.simphony.systemdynamics.sdmodel.impl.SystemModelImpl#getUnits <em>Units</em>}</li>
 *   <li>{@link repast.simphony.systemdynamics.sdmodel.impl.SystemModelImpl#getReportingInterval <em>Reporting Interval</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SystemModelImpl extends EObjectImpl implements SystemModel {
  /**
   * The cached value of the '{@link #getLinks() <em>Links</em>}' containment reference list.
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @see #getLinks()
   * @generated
   * @ordered
   */
  protected EList<InfluenceLink> links;

  /**
   * The cached value of the '{@link #getVariables() <em>Variables</em>}' containment reference list.
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @see #getVariables()
   * @generated
   * @ordered
   */
  protected EList<Variable> variables;

  /**
   * The default value of the '{@link #getStartTime() <em>Start Time</em>}' attribute.
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @see #getStartTime()
   * @generated
   * @ordered
   */
  protected static final double START_TIME_EDEFAULT = 0.0;

  /**
   * The cached value of the '{@link #getStartTime() <em>Start Time</em>}' attribute.
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @see #getStartTime()
   * @generated
   * @ordered
   */
  protected double startTime = START_TIME_EDEFAULT;

  /**
   * The default value of the '{@link #getEndTime() <em>End Time</em>}' attribute.
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @see #getEndTime()
   * @generated
   * @ordered
   */
  protected static final double END_TIME_EDEFAULT = 0.0;

  /**
   * The cached value of the '{@link #getEndTime() <em>End Time</em>}' attribute.
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @see #getEndTime()
   * @generated
   * @ordered
   */
  protected double endTime = END_TIME_EDEFAULT;

  /**
   * The default value of the '{@link #getTimeStep() <em>Time Step</em>}' attribute.
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @see #getTimeStep()
   * @generated
   * @ordered
   */
  protected static final double TIME_STEP_EDEFAULT = 0.0;

  /**
   * The cached value of the '{@link #getTimeStep() <em>Time Step</em>}' attribute.
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @see #getTimeStep()
   * @generated
   * @ordered
   */
  protected double timeStep = TIME_STEP_EDEFAULT;

  /**
   * The default value of the '{@link #getUnits() <em>Units</em>}' attribute.
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @see #getUnits()
   * @generated
   * @ordered
   */
  protected static final String UNITS_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getUnits() <em>Units</em>}' attribute.
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @see #getUnits()
   * @generated
   * @ordered
   */
  protected String units = UNITS_EDEFAULT;

  /**
   * The default value of the '{@link #getReportingInterval() <em>Reporting Interval</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getReportingInterval()
   * @generated
   * @ordered
   */
  protected static final double REPORTING_INTERVAL_EDEFAULT = 0.0;

  /**
   * The cached value of the '{@link #getReportingInterval() <em>Reporting Interval</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getReportingInterval()
   * @generated
   * @ordered
   */
  protected double reportingInterval = REPORTING_INTERVAL_EDEFAULT;

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  protected SystemModelImpl() {
    super();
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass() {
    return SDModelPackage.Literals.SYSTEM_MODEL;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public EList<InfluenceLink> getLinks() {
    if (links == null) {
      links = new EObjectContainmentEList<InfluenceLink>(InfluenceLink.class, this, SDModelPackage.SYSTEM_MODEL__LINKS);
    }
    return links;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public EList<Variable> getVariables() {
    if (variables == null) {
      variables = new EObjectContainmentEList<Variable>(Variable.class, this, SDModelPackage.SYSTEM_MODEL__VARIABLES);
    }
    return variables;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public double getStartTime() {
    return startTime;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public void setStartTime(double newStartTime) {
    double oldStartTime = startTime;
    startTime = newStartTime;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SDModelPackage.SYSTEM_MODEL__START_TIME, oldStartTime, startTime));
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public double getEndTime() {
    return endTime;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public void setEndTime(double newEndTime) {
    double oldEndTime = endTime;
    endTime = newEndTime;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SDModelPackage.SYSTEM_MODEL__END_TIME, oldEndTime, endTime));
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public double getTimeStep() {
    return timeStep;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public void setTimeStep(double newTimeStep) {
    double oldTimeStep = timeStep;
    timeStep = newTimeStep;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SDModelPackage.SYSTEM_MODEL__TIME_STEP, oldTimeStep, timeStep));
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public String getUnits() {
    return units;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public void setUnits(String newUnits) {
    String oldUnits = units;
    units = newUnits;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SDModelPackage.SYSTEM_MODEL__UNITS, oldUnits, units));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public double getReportingInterval() {
    return reportingInterval;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setReportingInterval(double newReportingInterval) {
    double oldReportingInterval = reportingInterval;
    reportingInterval = newReportingInterval;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SDModelPackage.SYSTEM_MODEL__REPORTING_INTERVAL, oldReportingInterval, reportingInterval));
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID,
      NotificationChain msgs) {
    switch (featureID) {
      case SDModelPackage.SYSTEM_MODEL__LINKS:
        return ((InternalEList<?>)getLinks()).basicRemove(otherEnd, msgs);
      case SDModelPackage.SYSTEM_MODEL__VARIABLES:
        return ((InternalEList<?>)getVariables()).basicRemove(otherEnd, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType) {
    switch (featureID) {
      case SDModelPackage.SYSTEM_MODEL__LINKS:
        return getLinks();
      case SDModelPackage.SYSTEM_MODEL__VARIABLES:
        return getVariables();
      case SDModelPackage.SYSTEM_MODEL__START_TIME:
        return getStartTime();
      case SDModelPackage.SYSTEM_MODEL__END_TIME:
        return getEndTime();
      case SDModelPackage.SYSTEM_MODEL__TIME_STEP:
        return getTimeStep();
      case SDModelPackage.SYSTEM_MODEL__UNITS:
        return getUnits();
      case SDModelPackage.SYSTEM_MODEL__REPORTING_INTERVAL:
        return getReportingInterval();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public void eSet(int featureID, Object newValue) {
    switch (featureID) {
      case SDModelPackage.SYSTEM_MODEL__LINKS:
        getLinks().clear();
        getLinks().addAll((Collection<? extends InfluenceLink>)newValue);
        return;
      case SDModelPackage.SYSTEM_MODEL__VARIABLES:
        getVariables().clear();
        getVariables().addAll((Collection<? extends Variable>)newValue);
        return;
      case SDModelPackage.SYSTEM_MODEL__START_TIME:
        setStartTime((Double)newValue);
        return;
      case SDModelPackage.SYSTEM_MODEL__END_TIME:
        setEndTime((Double)newValue);
        return;
      case SDModelPackage.SYSTEM_MODEL__TIME_STEP:
        setTimeStep((Double)newValue);
        return;
      case SDModelPackage.SYSTEM_MODEL__UNITS:
        setUnits((String)newValue);
        return;
      case SDModelPackage.SYSTEM_MODEL__REPORTING_INTERVAL:
        setReportingInterval((Double)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID) {
    switch (featureID) {
      case SDModelPackage.SYSTEM_MODEL__LINKS:
        getLinks().clear();
        return;
      case SDModelPackage.SYSTEM_MODEL__VARIABLES:
        getVariables().clear();
        return;
      case SDModelPackage.SYSTEM_MODEL__START_TIME:
        setStartTime(START_TIME_EDEFAULT);
        return;
      case SDModelPackage.SYSTEM_MODEL__END_TIME:
        setEndTime(END_TIME_EDEFAULT);
        return;
      case SDModelPackage.SYSTEM_MODEL__TIME_STEP:
        setTimeStep(TIME_STEP_EDEFAULT);
        return;
      case SDModelPackage.SYSTEM_MODEL__UNITS:
        setUnits(UNITS_EDEFAULT);
        return;
      case SDModelPackage.SYSTEM_MODEL__REPORTING_INTERVAL:
        setReportingInterval(REPORTING_INTERVAL_EDEFAULT);
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID) {
    switch (featureID) {
      case SDModelPackage.SYSTEM_MODEL__LINKS:
        return links != null && !links.isEmpty();
      case SDModelPackage.SYSTEM_MODEL__VARIABLES:
        return variables != null && !variables.isEmpty();
      case SDModelPackage.SYSTEM_MODEL__START_TIME:
        return startTime != START_TIME_EDEFAULT;
      case SDModelPackage.SYSTEM_MODEL__END_TIME:
        return endTime != END_TIME_EDEFAULT;
      case SDModelPackage.SYSTEM_MODEL__TIME_STEP:
        return timeStep != TIME_STEP_EDEFAULT;
      case SDModelPackage.SYSTEM_MODEL__UNITS:
        return UNITS_EDEFAULT == null ? units != null : !UNITS_EDEFAULT.equals(units);
      case SDModelPackage.SYSTEM_MODEL__REPORTING_INTERVAL:
        return reportingInterval != REPORTING_INTERVAL_EDEFAULT;
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString() {
    if (eIsProxy()) return super.toString();

    StringBuffer result = new StringBuffer(super.toString());
    result.append(" (startTime: ");
    result.append(startTime);
    result.append(", endTime: ");
    result.append(endTime);
    result.append(", timeStep: ");
    result.append(timeStep);
    result.append(", units: ");
    result.append(units);
    result.append(", reportingInterval: ");
    result.append(reportingInterval);
    result.append(')');
    return result.toString();
  }
} // SystemModelImpl
