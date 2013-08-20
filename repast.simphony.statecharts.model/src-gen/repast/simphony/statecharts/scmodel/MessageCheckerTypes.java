/**
 */
package repast.simphony.statecharts.scmodel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Message Checker Types</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see repast.simphony.statecharts.scmodel.StatechartPackage#getMessageCheckerTypes()
 * @model
 * @generated
 */
public enum MessageCheckerTypes implements Enumerator {
  /**
   * The '<em><b>Conditional</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #CONDITIONAL_VALUE
   * @generated
   * @ordered
   */
  CONDITIONAL(0, "conditional", "conditional"),

  /**
   * The '<em><b>Equals</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #EQUALS_VALUE
   * @generated
   * @ordered
   */
  EQUALS(1, "equals", "equals"),

  /**
   * The '<em><b>Unconditional</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #UNCONDITIONAL_VALUE
   * @generated
   * @ordered
   */
  UNCONDITIONAL(2, "unconditional", "unconditional"),

  /**
   * The '<em><b>Always</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #ALWAYS_VALUE
   * @generated
   * @ordered
   */
  ALWAYS(3, "always", "always");

  /**
   * The '<em><b>Conditional</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Conditional</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #CONDITIONAL
   * @model name="conditional"
   * @generated
   * @ordered
   */
  public static final int CONDITIONAL_VALUE = 0;

  /**
   * The '<em><b>Equals</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Equals</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #EQUALS
   * @model name="equals"
   * @generated
   * @ordered
   */
  public static final int EQUALS_VALUE = 1;

  /**
   * The '<em><b>Unconditional</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Unconditional</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #UNCONDITIONAL
   * @model name="unconditional"
   * @generated
   * @ordered
   */
  public static final int UNCONDITIONAL_VALUE = 2;

  /**
   * The '<em><b>Always</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Always</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #ALWAYS
   * @model name="always"
   * @generated
   * @ordered
   */
  public static final int ALWAYS_VALUE = 3;

  /**
   * An array of all the '<em><b>Message Checker Types</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static final MessageCheckerTypes[] VALUES_ARRAY =
    new MessageCheckerTypes[] {
      CONDITIONAL,
      EQUALS,
      UNCONDITIONAL,
      ALWAYS,
    };

  /**
   * A public read-only list of all the '<em><b>Message Checker Types</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final List<MessageCheckerTypes> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

  /**
   * Returns the '<em><b>Message Checker Types</b></em>' literal with the specified literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static MessageCheckerTypes get(String literal) {
    for (int i = 0; i < VALUES_ARRAY.length; ++i) {
      MessageCheckerTypes result = VALUES_ARRAY[i];
      if (result.toString().equals(literal)) {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Message Checker Types</b></em>' literal with the specified name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static MessageCheckerTypes getByName(String name) {
    for (int i = 0; i < VALUES_ARRAY.length; ++i) {
      MessageCheckerTypes result = VALUES_ARRAY[i];
      if (result.getName().equals(name)) {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Message Checker Types</b></em>' literal with the specified integer value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static MessageCheckerTypes get(int value) {
    switch (value) {
      case CONDITIONAL_VALUE: return CONDITIONAL;
      case EQUALS_VALUE: return EQUALS;
      case UNCONDITIONAL_VALUE: return UNCONDITIONAL;
      case ALWAYS_VALUE: return ALWAYS;
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
  private MessageCheckerTypes(int value, String name, String literal) {
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
  
} //MessageCheckerTypes
