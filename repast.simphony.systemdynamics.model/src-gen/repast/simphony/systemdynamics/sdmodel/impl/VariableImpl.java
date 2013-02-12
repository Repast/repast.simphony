/**
 */
package repast.simphony.systemdynamics.sdmodel.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import repast.simphony.systemdynamics.sdmodel.SDModelPackage;
import repast.simphony.systemdynamics.sdmodel.Variable;
import repast.simphony.systemdynamics.sdmodel.VariableType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Variable</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link repast.simphony.systemdynamics.sdmodel.impl.VariableImpl#getUuid <em>Uuid</em>}</li>
 *   <li>{@link repast.simphony.systemdynamics.sdmodel.impl.VariableImpl#getName <em>Name</em>}</li>
 *   <li>{@link repast.simphony.systemdynamics.sdmodel.impl.VariableImpl#getType <em>Type</em>}</li>
 *   <li>{@link repast.simphony.systemdynamics.sdmodel.impl.VariableImpl#getUnits <em>Units</em>}</li>
 *   <li>{@link repast.simphony.systemdynamics.sdmodel.impl.VariableImpl#getEquation <em>Equation</em>}</li>
 *   <li>{@link repast.simphony.systemdynamics.sdmodel.impl.VariableImpl#getComment <em>Comment</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class VariableImpl extends EObjectImpl implements Variable {
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
   * The default value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected static final String NAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected String name = NAME_EDEFAULT;

  /**
   * The default value of the '{@link #getType() <em>Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getType()
   * @generated
   * @ordered
   */
  protected static final VariableType TYPE_EDEFAULT = VariableType.CONSTANT;

  /**
   * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getType()
   * @generated
   * @ordered
   */
  protected VariableType type = TYPE_EDEFAULT;

  /**
   * The default value of the '{@link #getUnits() <em>Units</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getUnits()
   * @generated
   * @ordered
   */
  protected static final String UNITS_EDEFAULT = "";

  /**
   * The cached value of the '{@link #getUnits() <em>Units</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getUnits()
   * @generated
   * @ordered
   */
  protected String units = UNITS_EDEFAULT;

  /**
   * The default value of the '{@link #getEquation() <em>Equation</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEquation()
   * @generated
   * @ordered
   */
  protected static final String EQUATION_EDEFAULT = "";

  /**
   * The cached value of the '{@link #getEquation() <em>Equation</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEquation()
   * @generated
   * @ordered
   */
  protected String equation = EQUATION_EDEFAULT;

  /**
   * The default value of the '{@link #getComment() <em>Comment</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getComment()
   * @generated
   * @ordered
   */
  protected static final String COMMENT_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getComment() <em>Comment</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getComment()
   * @generated
   * @ordered
   */
  protected String comment = COMMENT_EDEFAULT;
  
  protected List<String> subscripts = new ArrayList<String>();

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected VariableImpl() {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass() {
    return SDModelPackage.Literals.VARIABLE;
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
      eNotify(new ENotificationImpl(this, Notification.SET, SDModelPackage.VARIABLE__UUID, oldUuid, uuid));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getName() {
    return name;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setName(String newName) {
    String oldName = name;
    name = newName;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SDModelPackage.VARIABLE__NAME, oldName, name));
    parseSubscripts(equation);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public VariableType getType() {
    return type;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setType(VariableType newType) {
    VariableType oldType = type;
    type = newType == null ? TYPE_EDEFAULT : newType;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SDModelPackage.VARIABLE__TYPE, oldType, type));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getUnits() {
    return units;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setUnits(String newUnits) {
    String oldUnits = units;
    units = newUnits;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SDModelPackage.VARIABLE__UNITS, oldUnits, units));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getEquation() {
    return equation;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setEquation(String newEquation) {
    String oldEquation = equation;
    equation = newEquation;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SDModelPackage.VARIABLE__EQUATION, oldEquation, equation));
    parseSubscripts(equation);
  }
  
  private void parseSubscripts(String equation) {
    subscripts.clear();
    if (name != null && name.length() > 0) {
      int index = equation.indexOf(name);
      if (index != -1) {
        index = index + name.length();
 
        while (index < equation.length()) {
          if (!Character.isWhitespace(equation.charAt(index))) break;
          ++index;
        }
        
        if (equation.charAt(index) == '[') {
          ++index;
          boolean complete = false;
          StringBuilder buf = new StringBuilder();
          while (index < equation.length()) {
            char c = equation.charAt(index);
            if (c == ']') {
              complete = true;
              break;
            }
            buf.append(c);
            ++index;
          }
          
          
          if (complete) {
            String[] subs = buf.toString().split(",");
            for (String sub : subs) {
              subscripts.add(sub.trim());
            }
          }
          
          
        }
      }
    }
    
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getComment() {
    return comment;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setComment(String newComment) {
    String oldComment = comment;
    comment = newComment;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SDModelPackage.VARIABLE__COMMENT, oldComment, comment));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType) {
    switch (featureID) {
      case SDModelPackage.VARIABLE__UUID:
        return getUuid();
      case SDModelPackage.VARIABLE__NAME:
        return getName();
      case SDModelPackage.VARIABLE__TYPE:
        return getType();
      case SDModelPackage.VARIABLE__UNITS:
        return getUnits();
      case SDModelPackage.VARIABLE__EQUATION:
        return getEquation();
      case SDModelPackage.VARIABLE__COMMENT:
        return getComment();
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
      case SDModelPackage.VARIABLE__UUID:
        setUuid((String)newValue);
        return;
      case SDModelPackage.VARIABLE__NAME:
        setName((String)newValue);
        return;
      case SDModelPackage.VARIABLE__TYPE:
        setType((VariableType)newValue);
        return;
      case SDModelPackage.VARIABLE__UNITS:
        setUnits((String)newValue);
        return;
      case SDModelPackage.VARIABLE__EQUATION:
        setEquation((String)newValue);
        return;
      case SDModelPackage.VARIABLE__COMMENT:
        setComment((String)newValue);
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
      case SDModelPackage.VARIABLE__UUID:
        setUuid(UUID_EDEFAULT);
        return;
      case SDModelPackage.VARIABLE__NAME:
        setName(NAME_EDEFAULT);
        return;
      case SDModelPackage.VARIABLE__TYPE:
        setType(TYPE_EDEFAULT);
        return;
      case SDModelPackage.VARIABLE__UNITS:
        setUnits(UNITS_EDEFAULT);
        return;
      case SDModelPackage.VARIABLE__EQUATION:
        setEquation(EQUATION_EDEFAULT);
        return;
      case SDModelPackage.VARIABLE__COMMENT:
        setComment(COMMENT_EDEFAULT);
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
      case SDModelPackage.VARIABLE__UUID:
        return UUID_EDEFAULT == null ? uuid != null : !UUID_EDEFAULT.equals(uuid);
      case SDModelPackage.VARIABLE__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
      case SDModelPackage.VARIABLE__TYPE:
        return type != TYPE_EDEFAULT;
      case SDModelPackage.VARIABLE__UNITS:
        return UNITS_EDEFAULT == null ? units != null : !UNITS_EDEFAULT.equals(units);
      case SDModelPackage.VARIABLE__EQUATION:
        return EQUATION_EDEFAULT == null ? equation != null : !EQUATION_EDEFAULT.equals(equation);
      case SDModelPackage.VARIABLE__COMMENT:
        return COMMENT_EDEFAULT == null ? comment != null : !COMMENT_EDEFAULT.equals(comment);
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
    result.append(", name: ");
    result.append(name);
    result.append(", type: ");
    result.append(type);
    result.append(", units: ");
    result.append(units);
    result.append(", equation: ");
    result.append(equation);
    result.append(", comment: ");
    result.append(comment);
    result.append(')');
    return result.toString();
  }

  /* (non-Javadoc)
   * @see repast.simphony.systemdynamics.sdmodel.Variable#getSubscripts()
   */
  /**
   * @generated NOT
   */
  @Override
  public List<String> getSubscripts() {
    return new ArrayList<String>(subscripts);
  }
} //VariableImpl
