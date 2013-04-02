/**
 */
package repast.simphony.statecharts.scmodel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Trigger Types</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see repast.simphony.statecharts.scmodel.StatechartPackage#getTriggerTypes()
 * @model
 * @generated
 */
public enum TriggerTypes implements Enumerator {
  /**
   * The '<em><b>Always</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #ALWAYS_VALUE
   * @generated
   * @ordered
   */
  ALWAYS(0, "always", "always"),

  /**
   * The '<em><b>Timed</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #TIMED_VALUE
   * @generated
   * @ordered
   */
  TIMED(1, "timed", "timed"),

  /**
   * The '<em><b>Exponential</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #EXPONENTIAL_VALUE
   * @generated
   * @ordered
   */
  EXPONENTIAL(4, "exponential", "exponential"),

  /**
   * The '<em><b>Probability</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #PROBABILITY_VALUE
   * @generated
   * @ordered
   */
  PROBABILITY(3, "probability", "probability"),

  /**
   * The '<em><b>Condition</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #CONDITION_VALUE
   * @generated
   * @ordered
   */
  CONDITION(2, "condition", "condition"),

  /**
   * The '<em><b>Message</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #MESSAGE_VALUE
   * @generated
   * @ordered
   */
  MESSAGE(5, "message", "message");

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
  public static final int ALWAYS_VALUE = 0;

  /**
   * The '<em><b>Timed</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Timed</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #TIMED
   * @model name="timed"
   * @generated
   * @ordered
   */
  public static final int TIMED_VALUE = 1;

  /**
   * The '<em><b>Exponential</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Exponential</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #EXPONENTIAL
   * @model name="exponential"
   * @generated
   * @ordered
   */
  public static final int EXPONENTIAL_VALUE = 4;

  /**
   * The '<em><b>Probability</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Probability</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #PROBABILITY
   * @model name="probability"
   * @generated
   * @ordered
   */
  public static final int PROBABILITY_VALUE = 3;

  /**
   * The '<em><b>Condition</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Condition</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #CONDITION
   * @model name="condition"
   * @generated
   * @ordered
   */
  public static final int CONDITION_VALUE = 2;

  /**
   * The '<em><b>Message</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Message</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #MESSAGE
   * @model name="message"
   * @generated
   * @ordered
   */
  public static final int MESSAGE_VALUE = 5;

  /**
   * An array of all the '<em><b>Trigger Types</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static final TriggerTypes[] VALUES_ARRAY =
    new TriggerTypes[] {
      ALWAYS,
      TIMED,
      EXPONENTIAL,
      PROBABILITY,
      CONDITION,
      MESSAGE,
    };

  /**
   * A public read-only list of all the '<em><b>Trigger Types</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final List<TriggerTypes> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

  /**
   * Returns the '<em><b>Trigger Types</b></em>' literal with the specified literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static TriggerTypes get(String literal) {
    for (int i = 0; i < VALUES_ARRAY.length; ++i) {
      TriggerTypes result = VALUES_ARRAY[i];
      if (result.toString().equals(literal)) {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Trigger Types</b></em>' literal with the specified name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static TriggerTypes getByName(String name) {
    for (int i = 0; i < VALUES_ARRAY.length; ++i) {
      TriggerTypes result = VALUES_ARRAY[i];
      if (result.getName().equals(name)) {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Trigger Types</b></em>' literal with the specified integer value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static TriggerTypes get(int value) {
    switch (value) {
      case ALWAYS_VALUE: return ALWAYS;
      case TIMED_VALUE: return TIMED;
      case EXPONENTIAL_VALUE: return EXPONENTIAL;
      case PROBABILITY_VALUE: return PROBABILITY;
      case CONDITION_VALUE: return CONDITION;
      case MESSAGE_VALUE: return MESSAGE;
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
  private TriggerTypes(int value, String name, String literal) {
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
  
} //TriggerTypes
