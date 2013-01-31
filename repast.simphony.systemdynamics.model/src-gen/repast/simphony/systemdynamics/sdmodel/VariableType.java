/**
 */
package repast.simphony.systemdynamics.sdmodel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Variable Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see repast.simphony.systemdynamics.sdmodel.SDModelPackage#getVariableType()
 * @model
 * @generated
 */
public enum VariableType implements Enumerator {
  /**
   * The '<em><b>Constant</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #CONSTANT_VALUE
   * @generated
   * @ordered
   */
  CONSTANT(0, "constant", "constant"),

  /**
   * The '<em><b>Auxiliary</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #AUXILIARY_VALUE
   * @generated
   * @ordered
   */
  AUXILIARY(1, "auxiliary", "auxiliary"),

  /**
   * The '<em><b>Stock</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #STOCK_VALUE
   * @generated
   * @ordered
   */
  STOCK(2, "stock", "stock"),

  /**
   * The '<em><b>Rate</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #RATE_VALUE
   * @generated
   * @ordered
   */
  RATE(3, "rate", "rate"), /**
   * The '<em><b>Lookup</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #LOOKUP_VALUE
   * @generated
   * @ordered
   */
  LOOKUP(0, "lookup", "lookup");

  /**
   * The '<em><b>Constant</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Constant</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #CONSTANT
   * @model name="constant"
   * @generated
   * @ordered
   */
  public static final int CONSTANT_VALUE = 0;

  /**
   * The '<em><b>Auxiliary</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Auxiliary</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #AUXILIARY
   * @model name="auxiliary"
   * @generated
   * @ordered
   */
  public static final int AUXILIARY_VALUE = 1;

  /**
   * The '<em><b>Stock</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Stock</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #STOCK
   * @model name="stock"
   * @generated
   * @ordered
   */
  public static final int STOCK_VALUE = 2;

  /**
   * The '<em><b>Rate</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Rate</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #RATE
   * @model name="rate"
   * @generated
   * @ordered
   */
  public static final int RATE_VALUE = 3;

  /**
   * The '<em><b>Lookup</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Lookup</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #LOOKUP
   * @model name="lookup"
   * @generated
   * @ordered
   */
  public static final int LOOKUP_VALUE = 0;

  /**
   * An array of all the '<em><b>Variable Type</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static final VariableType[] VALUES_ARRAY =
    new VariableType[] {
      CONSTANT,
      AUXILIARY,
      STOCK,
      RATE,
      LOOKUP,
    };

  /**
   * A public read-only list of all the '<em><b>Variable Type</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final List<VariableType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

  /**
   * Returns the '<em><b>Variable Type</b></em>' literal with the specified literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static VariableType get(String literal) {
    for (int i = 0; i < VALUES_ARRAY.length; ++i) {
      VariableType result = VALUES_ARRAY[i];
      if (result.toString().equals(literal)) {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Variable Type</b></em>' literal with the specified name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static VariableType getByName(String name) {
    for (int i = 0; i < VALUES_ARRAY.length; ++i) {
      VariableType result = VALUES_ARRAY[i];
      if (result.getName().equals(name)) {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Variable Type</b></em>' literal with the specified integer value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static VariableType get(int value) {
    switch (value) {
      case CONSTANT_VALUE: return CONSTANT;
      case AUXILIARY_VALUE: return AUXILIARY;
      case STOCK_VALUE: return STOCK;
      case RATE_VALUE: return RATE;
    }
    return null;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private final int value;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private final String name;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private final String literal;

  /**
   * Only this class can construct instances.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private VariableType(int value, String name, String literal) {
    this.value = value;
    this.name = name;
    this.literal = literal;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public int getValue() {
    return value;
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
  public String getLiteral() {
    return literal;
  }

  /**
   * Returns the literal value of the enumerator, which is its string representation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString() {
    return literal;
  }
  
} //VariableType
