/**
 */
package repast.simphony.systemdynamics.sdmodel.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import repast.simphony.systemdynamics.sdmodel.InfluenceLink;
import repast.simphony.systemdynamics.sdmodel.SDModelPackage;
import repast.simphony.systemdynamics.sdmodel.Variable;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Influence Link</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link repast.simphony.systemdynamics.sdmodel.impl.InfluenceLinkImpl#getUuid <em>Uuid</em>}</li>
 *   <li>{@link repast.simphony.systemdynamics.sdmodel.impl.InfluenceLinkImpl#getFrom <em>From</em>}</li>
 *   <li>{@link repast.simphony.systemdynamics.sdmodel.impl.InfluenceLinkImpl#getTo <em>To</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class InfluenceLinkImpl extends EObjectImpl implements InfluenceLink {
  /**
   * The default value of the '{@link #getUuid() <em>Uuid</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getUuid()
   * @generated
   * @ordered
   */
  protected static final String UUID_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getUuid() <em>Uuid</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getUuid()
   * @generated
   * @ordered
   */
  protected String uuid = UUID_EDEFAULT;

  /**
   * The cached value of the '{@link #getFrom() <em>From</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFrom()
   * @generated
   * @ordered
   */
  protected Variable from;

  /**
   * The cached value of the '{@link #getTo() <em>To</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTo()
   * @generated
   * @ordered
   */
  protected Variable to;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected InfluenceLinkImpl() {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass() {
    return SDModelPackage.Literals.INFLUENCE_LINK;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getUuid() {
    return uuid;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setUuid(String newUuid) {
    String oldUuid = uuid;
    uuid = newUuid;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SDModelPackage.INFLUENCE_LINK__UUID, oldUuid, uuid));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Variable getFrom() {
    if (from != null && from.eIsProxy()) {
      InternalEObject oldFrom = (InternalEObject)from;
      from = (Variable)eResolveProxy(oldFrom);
      if (from != oldFrom) {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, SDModelPackage.INFLUENCE_LINK__FROM, oldFrom, from));
      }
    }
    return from;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Variable basicGetFrom() {
    return from;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setFrom(Variable newFrom) {
    Variable oldFrom = from;
    from = newFrom;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SDModelPackage.INFLUENCE_LINK__FROM, oldFrom, from));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Variable getTo() {
    if (to != null && to.eIsProxy()) {
      InternalEObject oldTo = (InternalEObject)to;
      to = (Variable)eResolveProxy(oldTo);
      if (to != oldTo) {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, SDModelPackage.INFLUENCE_LINK__TO, oldTo, to));
      }
    }
    return to;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Variable basicGetTo() {
    return to;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setTo(Variable newTo) {
    Variable oldTo = to;
    to = newTo;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SDModelPackage.INFLUENCE_LINK__TO, oldTo, to));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType) {
    switch (featureID) {
      case SDModelPackage.INFLUENCE_LINK__UUID:
        return getUuid();
      case SDModelPackage.INFLUENCE_LINK__FROM:
        if (resolve) return getFrom();
        return basicGetFrom();
      case SDModelPackage.INFLUENCE_LINK__TO:
        if (resolve) return getTo();
        return basicGetTo();
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
      case SDModelPackage.INFLUENCE_LINK__UUID:
        setUuid((String)newValue);
        return;
      case SDModelPackage.INFLUENCE_LINK__FROM:
        setFrom((Variable)newValue);
        return;
      case SDModelPackage.INFLUENCE_LINK__TO:
        setTo((Variable)newValue);
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
      case SDModelPackage.INFLUENCE_LINK__UUID:
        setUuid(UUID_EDEFAULT);
        return;
      case SDModelPackage.INFLUENCE_LINK__FROM:
        setFrom((Variable)null);
        return;
      case SDModelPackage.INFLUENCE_LINK__TO:
        setTo((Variable)null);
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
      case SDModelPackage.INFLUENCE_LINK__UUID:
        return UUID_EDEFAULT == null ? uuid != null : !UUID_EDEFAULT.equals(uuid);
      case SDModelPackage.INFLUENCE_LINK__FROM:
        return from != null;
      case SDModelPackage.INFLUENCE_LINK__TO:
        return to != null;
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
    result.append(" (uuid: ");
    result.append(uuid);
    result.append(')');
    return result.toString();
  }

} //InfluenceLinkImpl
