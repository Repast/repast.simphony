/**
 */
package repast.simphony.statecharts.scmodel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Pseudo State Types</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see repast.simphony.statecharts.scmodel.StatechartPackage#getPseudoStateTypes()
 * @model
 * @generated
 */
public enum PseudoStateTypes implements Enumerator {
  /**
   * The '<em><b>Initial</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #INITIAL_VALUE
   * @generated
   * @ordered
   */
  INITIAL(0, "initial", "initial"),

  /**
   * The '<em><b>Entry</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #ENTRY_VALUE
   * @generated
   * @ordered
   */
  ENTRY(0, "entry", "entry"),

  /**
   * The '<em><b>Choice</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #CHOICE_VALUE
   * @generated
   * @ordered
   */
  CHOICE(0, "choice", "choice");

  /**
   * The '<em><b>Initial</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Initial</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #INITIAL
   * @model name="initial"
   * @generated
   * @ordered
   */
  public static final int INITIAL_VALUE = 0;

  /**
   * The '<em><b>Entry</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Entry</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #ENTRY
   * @model name="entry"
   * @generated
   * @ordered
   */
  public static final int ENTRY_VALUE = 0;

  /**
   * The '<em><b>Choice</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Choice</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #CHOICE
   * @model name="choice"
   * @generated
   * @ordered
   */
  public static final int CHOICE_VALUE = 0;

  /**
   * An array of all the '<em><b>Pseudo State Types</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static final PseudoStateTypes[] VALUES_ARRAY =
    new PseudoStateTypes[] {
      INITIAL,
      ENTRY,
      CHOICE,
    };

  /**
   * A public read-only list of all the '<em><b>Pseudo State Types</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final List<PseudoStateTypes> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

  /**
   * Returns the '<em><b>Pseudo State Types</b></em>' literal with the specified literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static PseudoStateTypes get(String literal) {
    for (int i = 0; i < VALUES_ARRAY.length; ++i) {
      PseudoStateTypes result = VALUES_ARRAY[i];
      if (result.toString().equals(literal)) {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Pseudo State Types</b></em>' literal with the specified name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static PseudoStateTypes getByName(String name) {
    for (int i = 0; i < VALUES_ARRAY.length; ++i) {
      PseudoStateTypes result = VALUES_ARRAY[i];
      if (result.getName().equals(name)) {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Pseudo State Types</b></em>' literal with the specified integer value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static PseudoStateTypes get(int value) {
    switch (value) {
      case INITIAL_VALUE: return INITIAL;
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
  private PseudoStateTypes(int value, String name, String literal) {
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
  
} //PseudoStateTypes
